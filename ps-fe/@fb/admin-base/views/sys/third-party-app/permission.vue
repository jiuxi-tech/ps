<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-alert type="warning" show-icon style="margin-bottom: 20px;">
				<template slot="message">
					请勾选该应用可以访问的API接口，未勾选的接口将无法访问。保存后立即生效。
				</template>
			</fb-alert>

			<!-- <div style="margin-bottom: 15px;">
				<fb-checkbox v-model="selectAll" @on-change="handleSelectAll">全选/取消全选</fb-checkbox>
			</div> -->

			<fb-tree :data="apis" multiple doCheck="ps" doUnCheck="ps" @on-check-change="handleCheckChange"
				ref="apiTree">
				<template #node="props">
					<span>
						<fb-tag v-if="props.node.httpMethod" :type="getMethodColor(props.node.httpMethod)" effect="dark"
							size="small">
							{{ props.node.httpMethod }}
						</fb-tag>
						{{ props.node.label }}
						<span v-if="props.node.apiPath" style="color: #999; font-size: 12px; margin-left: 8px;">
							{{ props.node.apiPath }}
						</span>
					</span>
				</template>
			</fb-tree>

			<div v-if="loading" style="text-align: center; padding: 20px;">
				<fb-spin></fb-spin>
			</div>
		</div>

		<div class="tp-dialog-bottom">
			<fb-button style="margin-right: 12px" type="primary" @on-click="handleSave" :loading="saving">保存</fb-button>
			<fb-button @on-click="handleClose">关闭</fb-button>
		</div>
	</div>
</template>

<script>
export default {
	name: 'permission',
	mixins: [],
	// 接收父组件的传参
	props: {
		param: {
			type: Object,
			require: false
		},
		parentPage: {
			type: Object,
			default: null
		},
		meta: {
			type: Object,
			default: () => ({})
		}
	},
	// 初始化方法
	mounted() {
		// 执行界面初始化
		this.init(this.param);
	},
	data() {
		return {
			// 请求的 service
			service: this.$svc.sys.thirdPartyApp,
			appId: '',
			appName: '',
			passKey: '',
			apiList: [], // 原始API列表
			apis: [], // 树形结构数据
			selectAll: false,
			loading: false,
			saving: false,
		}
	},

	// 方法
	methods: {
		// 初始化
		init(param) {
			if (param && param.id) {
				this.appId = param.id;
				this.appName = param.appName || '';
				this.passKey = param.passKey;
				this.loadData();
			}
		},

		// 加载数据
		loadData() {
			this.loading = true;

			// 并发请求：获取API清单 和 当前应用的权限
			Promise.all([
				this.service.getApiDefinitions({}),
				this.service.getApiPermissions({ appId: this.appId, passKey: this.passKey })
			]).then(([apiResult, permResult]) => {
				this.loading = false;

				// 处理API清单
				if (apiResult.code == 1) {
					this.apiList = apiResult.data || [];
				} else {
					this.$message.error('获取API清单失败: ' + apiResult.message);
					return;
				}

				// 处理权限数据
				let permittedApiIds = [];
				if (permResult.code == 1) {
					// 后端返回的是TpApiDefinition对象数组，需要提取apiId
					const permData = permResult.data || [];
					permittedApiIds = permData.map(item => item.apiId);
				} else {
					this.$message.error('获取权限配置失败: ' + permResult.message);
				}

				// 构建树形结构
				this.buildTreeData(permittedApiIds);
			}).catch(err => {
				this.loading = false;
				this.$message.error('加载数据失败');
				console.log(err);
			});
		},

		// 构建树形数据结构
		buildTreeData(permittedApiIds) {
			// 按分类分组
			const categoryMap = {};
			this.apiList.forEach(api => {
				const category = api.category || '未分类';
				if (category === '用户管理') {
					if (!categoryMap[category]) {
						categoryMap[category] = [];
					}

					categoryMap[category].push(api);
				}
			});

			// 构建树形结构（使用 value 和 label 字段）
			this.apis = [];
			Object.keys(categoryMap).sort().forEach(category => {
				const categoryNode = {
					label: category,
					value: 'category_' + category,
					expand: true, // 默认展开
					noCheckbox: true,
					children: []
				};

				categoryMap[category].forEach(api => {
					const apiNode = {
						label: api.apiName,
						value: api.apiId,
						checked: permittedApiIds.includes(api.apiId),
						// 额外数据用于插槽显示
						httpMethod: api.httpMethod,
						apiPath: api.apiPath,
						apiCode: api.apiCode
					};
					categoryNode.children.push(apiNode);
				});

				this.apis.push(categoryNode);
			});

			// 使用 $nextTick 确保树组件已渲染
			this.$nextTick(() => {
				this.updateSelectAllStatus();
			});
		},

		// 全选/取消全选
		handleSelectAll() {
			if (this.$refs.apiTree) {
				if (this.selectAll) {
					// 全选：获取所有API节点的value并勾选
					const allApiValues = [];
					this.apiList.forEach(api => {
						allApiValues.push(api.apiId);
					});
					this.$refs.apiTree.checkNodesByValue(allApiValues);
				} else {
					// 取消全选
					this.$refs.apiTree.checkNodesByValue([]);
				}
			}
		},

		// 树节点勾选变化（返回所有选中的节点对象数组）
		handleCheckChange(selectedNodes) {
			// 更新内部数据的checked状态
			const selectedValues = selectedNodes.map(node => node.value);

			// 更新apis数据中的checked状态
			this.apis.forEach(category => {
				if (category.children) {
					category.children.forEach(api => {
						api.checked = selectedValues.includes(api.value);
					});
				}
			});

			// 更新全选状态
			this.updateSelectAllStatus();
		},

		// 更新全选状态
		updateSelectAllStatus() {
			// 统计已勾选的API数量
			let checkedCount = 0;
			this.apis.forEach(category => {
				if (category.children) {
					category.children.forEach(api => {
						if (api.checked) {
							checkedCount++;
						}
					});
				}
			});
			this.selectAll = checkedCount === this.apiList.length && this.apiList.length > 0;
		},

		// 获取HTTP方法对应的颜色
		getMethodColor(method) {
			const colorMap = {
				'GET': 'success',
				'POST': 'warn',
				'PUT': 'primary',
				'DELETE': 'danger',
				'PATCH': 'info',
			};
			return colorMap[method] || 'default';
		},

		// 保存
		handleSave() {
			// 从树组件获取已勾选的节点
			const selectedApiIds = [];
			// 遍历apis数据获取所有checked为true的API
			this.apis.forEach(category => {
				if (category.children) {
					category.children.forEach(api => {
						if (api.checked) {
							selectedApiIds.push(api.value);
						}
					});
				}
			});

			// 构建保存数据
			const saveData = {
				appId: this.appId,
				passKey: this.passKey,
				apiIds: selectedApiIds
			};

			this.saving = true;
			this.service.saveApiPermissions(saveData).then((result) => {
				this.saving = false;
				if (result.code == 1) {
					this.$message.success('权限配置保存成功');
					this.closeTpDialog({ success: true, action: 'permission' });
				} else {
					this.$message.error('保存失败: ' + result.message);
				}
			}).catch(err => {
				this.saving = false;
				this.$message.error('保存失败');
				console.log(err);
			});
		},

		// 关闭
		handleClose() {
			this.closeTpDialog();
		},
	}
}
</script>

<style lang="less" scoped></style>
