<template>
	<div>

		<fb-page-tree title="行政区划树">
			<template slot="tree">
				<!-- 异步加载 -->
				<fb-tree
					style="overflow: auto"
					v-autoheight="152"
					ref="cityTree"
					:data="cityData"
					:reader="{value: 'id', label: 'text'}"
					:load-data="loadCityTreeData"
					@on-select-change="handleSelectChange"></fb-tree>
				<!-- 同步加载 -->
				<!--<fb-tree
					style="overflow: auto"
					v-autoheight="152"
					ref="cityTree"
					:data="cityData"
					:reader="{value: 'id', label: 'text'}"
					@on-select-change="handleSelectChange"></fb-tree>-->
			</template>
			<template slot="tree-actions">
				<fb-button :icon="treeExpand ? 'tree-expansion': 'tree-closed'" @on-click="handleTreeExpand"></fb-button>
			</template>

			<template slot="actions">
				<fb-button icon="node" @on-click="addChild" v-permission="'SYS_ORG_DEPT_ADD'" :disabled="cityParam.cityId === ''">添加下级
				</fb-button>
				<fb-button icon="editor-square" @on-click="update" v-permission="'SYS_ORG_DEPT_UPDATE'" :disabled="cityParam.cityId === ''">修改
				</fb-button>
				<fb-button icon="delete" @on-click="delChild" v-permission="'SYS_ORG_DEPT_DELETE'" danger :disabled="cityParam.cityId === ''">删除本级
				</fb-button>
			</template>
			<template slot="form">
				<fb-property bordered label-width="140px" mode="form" v-autoheight="152">
					<fb-fieldset label="行政区划信息"/>
					<fb-row>
						<fb-col span="12">
							<fb-property-item label="行政区划code">
								{{formData.cityCode}}
							</fb-property-item>
						</fb-col>
						<fb-col span="12">
							<fb-property-item label="行政区划名称">
								{{formData.cityName}}
							</fb-property-item>
						</fb-col>
					</fb-row>
					<fb-row>
						<fb-col span="12">
							<fb-property-item label="行政区划全称">
								{{formData.cityFullName}}
							</fb-property-item>
						</fb-col>
						<fb-col span="12">
							<fb-property-item label="行政区划简称">
								{{formData.citySimpleName}}
							</fb-property-item>
						</fb-col>
					</fb-row>
					<fb-row>
						<fb-col span="12">
							<fb-property-item label="排序">
								{{formData.orderIndex}}
							</fb-property-item>
						</fb-col>
						<fb-property-item label="是否启用">
							{{formData.enabled === 1 ? "是" : "否"}}
						</fb-property-item>
					</fb-row>

				</fb-property>

			</template>
		</fb-page-tree>
		<tp-dialog ref="TpDialog" @closeTpDialog="closeDialog"></tp-dialog>
	</div>
</template>

<script>


	export default {
		name: 'tree',
		mixins: [

		],
		// 初始化方法
		mounted() {
			// 执行界面初始化方法
			this.initCityTreeData('-1', '');
		},
		data() {
			return {
				// 请求的 service
				service: this.$svc.sys.city,
				treeExpand: true,
				selectNode: {
					children: false,
				},
				// 行政区划树参数
				cityParam: {
					pcityId: '',
					cityId: '',
				},
				// 行政区划树加载数据
				cityData: [],
				// 行政区划详情参数
				formData: {
					cityCode: '',
					cityName: '',
					cityFullName: '',
					citySimpleName: '',
					orderIndex: '',
					enabled: '',
				},

			}
		},

		// 方法
		methods: {
			// 初始化树数据
			initCityTreeData(cityId, selectCityId) {
				this.service.tree({cityId: cityId, sync: 1, expand: true, showTop: 'on'}).then((result) => {
					if (result.code == 1) {
						if (result.data.length > 0) {
							this.cityData = result.data;
							// 默认选中根节点
							this.$nextTick(() => {
								if (selectCityId) {
									this.$refs.cityTree.selectNodeByValue(selectCityId);
								} else {
									this.$refs.cityTree.selectNodeByValue(result.data[0].id);
								}
							})
						}
					} else {
						// 服务器返回失败
						this.$message.error('行政区划树加载失败' + result.message)
					}
				})
			},


			// 新增下级
			addChild() {
				// 界面跳转
				if (!this.cityParam.cityId) {
					this.$message.error('请先选择一条记录！')
					return
				}

				let param = {"id": '', "pcityId": this.cityParam.cityId};

				this.$refs.TpDialog.show(import('../../../views/sys/city/add.vue'), param, "新增");
			},


			// 修改
			update() {
				if (!this.cityParam.cityId) {
					this.$message.error('请先选择一条记录！')
					return
				}
				// 界面跳转
				let param = {"id": this.cityParam.cityId};
				this.$refs.TpDialog.show(import('../../../views/sys/city/add.vue'), param, "修改");
			},

			// 删除方法
			delChild() {
				if (!this.cityParam.cityId) {
					this.$message.error('请先选择一条记录！')
				} else {
					if (this.selectNode.children) {
						this.$message.error('所选记录有子节点，不能删除！请先删除子节点。');
						return;
					}
					this.$confirm('确定要删除吗？', () => {
						// 删除
						this.service.delete(this.formData).then((result) => {
							if (result.code == 1) {
								this.$message.success('删除成功');
								// 1、刷新树
								this.initCityTreeData('-1', '');
							} else {
								this.$message.error('删除失败：' + result.message)
							}
						})
					})
				}
			},

			// 新增 / 修改 回调方法
			handleQuery(cityId) {
				// 1、刷新树
				this.initCityTreeData('-1', cityId);
				// 2、查询新增 / 修改 的部门信息
				this.handleView(cityId);
			},
			// 查看方法
			handleView(cityId) {
				let that = this;

				this.service.view({"cityId": cityId}).then((result) => {
					// 判断code
					if (result.code == 1) {
						that.formData = result.data
					} else {
						// 服务器返回失败
						that.$message.error('查询失败')
					}
				})
			},

			handleSelectChange(node) {
				if (node) {
					this.cityParam.cityId = node.id
					this.cityParam.pcityId = node.pid
					if (node.children.length !== 0) {
						this.selectNode.children = true;
					} else {
						this.selectNode.children = false;
					}
					this.handleView(node.id);
				} else {
					this.cityParam.cityId = ''
					this.cityParam.pcityId = ''
				}
			},

			closeDialog(param) {
				// 新增/修改回来，树默认选中新增/修改的数据
				if (param) {
					this.handleQuery(param)
				}
			},

			handleTreeExpand(){
				this.treeExpand = !this.treeExpand
				this.$refs.cityTree.expandAll(this.treeExpand)
			}

		}
	}
</script>

<style lang="less" scoped>

</style>
