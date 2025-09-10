<template>
	<div>
		<fb-page-tree title="机构树">
			<template slot="tree">
				<!--<fb-tree
					style="overflow: auto"
					v-autoheight="152"
					ref="deptTree"
					:data="deptData"
					:reader="{value: 'id', label: 'text'}"
					:load-data="loadDeptTreeData"
					@on-select-change="handleSelectChange"></fb-tree>-->
				<fb-tree
					:data="deptData"
					:reader="{value: 'id', label: 'text'}"
					ref="deptTree"
					style="overflow: auto"
					v-autoheight="152"
					@on-select-change="handleSelectChange"></fb-tree>
			</template>
			<template slot="tree-actions">
				<fb-button :icon="treeExpand ? 'tree-expansion': 'tree-closed'"
						   @on-click="handleTreeExpand"></fb-button>
			</template>

			<template slot="actions">
				<fb-button icon="node" @on-click="addCurrent" v-permission="'SYS_ENT_DEPT_ADD'"
						   :disabled="selectNode.pid === '1111111111111111111' || selectNode.id === ''">添加同级
				</fb-button>
				<fb-button icon="node" @on-click="addChild" v-permission="'SYS_ENT_DEPT_ADD'"
						   :disabled="selectNode.id === ''">添加下级
				</fb-button>
				<fb-button icon="editor-square" @on-click="update" v-permission="'SYS_ENT_DEPT_UPDATE'"
						   :disabled="selectNode.id === ''">修改
				</fb-button>
				<fb-button icon="delete" @on-click="delChild" v-permission="'SYS_ENT_DEPT_DELETE'" danger
						   :disabled="selectNode.id === ''">删除本级
				</fb-button>
			</template>
			<template slot="form">
				<fb-fieldset label="机构基本信息"/>
				<fb-property bordered label-width="120px" mode="form">
					<fb-row>
						<fb-col span="12">
							<fb-property-item label="机构全称">
								{{formData.deptFullName}}
							</fb-property-item>
						</fb-col>
						<fb-col span="12">
							<fb-property-item label="机构简称">
								{{formData.deptSimpleName}}
							</fb-property-item>
						</fb-col>
					</fb-row>
					<fb-row>
						<fb-col span="12">
							<fb-property-item label="机构编号">
								{{formData.deptNo}}
							</fb-property-item>
						</fb-col>
						<fb-col span="12">
							<fb-property-item label="机构类型">
								{{formData.deptTypeName}}
							</fb-property-item>
						</fb-col>
					</fb-row>

					<fb-row>
						<fb-col span="24">
							<fb-property-item label="机构描述">
								{{formData.deptDesc}}
							</fb-property-item>
						</fb-col>
					</fb-row>

					<fb-row>
						<fb-col span="12">
							<fb-property-item label="机构联系人">
								{{formData.principalName}}
							</fb-property-item>
						</fb-col>
						<fb-col span="12">
							<fb-property-item label="机构联系电话">
								{{formData.principalTel}}
							</fb-property-item>
						</fb-col>
					</fb-row>

					<fb-row>
						<fb-col span="12">
							<fb-property-item label="排序">
								{{formData.orderIndex}}
							</fb-property-item>
						</fb-col>
						<fb-col span="12">
							<fb-property-item label="行政区划">
								{{formData.cityFullName}}
							</fb-property-item>
						</fb-col>
					</fb-row>

					<fb-row>
						<fb-col span="12">
							<fb-property-item label="是否启用">
								{{formData.enabled == 1?'是':'否'}}
							</fb-property-item>
						</fb-col>
					</fb-row>
				</fb-property>

				<fb-fieldset label="机构扩展信息"/>
				<fb-property bordered label-width="120px" mode="form" v-autoheight="152">
					<fb-row>
						<fb-col span="12">
							<fb-property-item label="经度">
								<fb-link :label="expFormData.longitude" @click="longitude" icon="navigation"
										 target="_blank" type="primary"></fb-link>
							</fb-property-item>
						</fb-col>
						<fb-col span="12">
							<fb-property-item label="纬度">
								<fb-link :label="expFormData.latitude" @click="longitude" icon="navigation"
										 target="_blank" type="primary"></fb-link>
							</fb-property-item>
						</fb-col>
					</fb-row>

					<fb-row>
						<fb-col span="24">
							<fb-property-item label="地址">
								{{expFormData.address}}
							</fb-property-item>
						</fb-col>
					</fb-row>

					<fb-row>
						<fb-col span="12">
							<fb-property-item label="Email">
								{{expFormData.email}}
							</fb-property-item>
						</fb-col>
					</fb-row>

				</fb-property>
			</template>
		</fb-page-tree>
		<tp-dialog ref="TpDialog" @closeTpDialog="closeDialog"></tp-dialog>
		<tp-dialog-tab ref="TpDialogTab" @closeTpDialog="closeDialogTab"></tp-dialog-tab>
	</div>
</template>

<script>


	export default {
		name: 'tree',
		mixins: [

		],
		// 组件
		components: {},
		// 初始化方法
		mounted() {
			// 执行界面初始化方法
			this.initDeptTreeData('', '');
		},
		data() {
			return {
				// 请求的 service
				service: this.$svc.sys.dept,
				treeExpand: true,
				// 部门树参数
				deptParam: {
					pdeptId: '',
					deptId: '',
					sync: 1,
				},
				// 部门树根节点
				selectNode: {
					id: '',
					pid: '',
					children: false,
				},
				// 部门树加载数据
				deptData: [],
				// 部门详情参数
				formData: {
					deptId: '',
					pdeptId: '',
					deptLevelcode: '',
					deptNo: '',
					deptFullName: '',
					deptSimpleName: '',
					deptType: '',
					deptDesc: '',
					category: '',
					principalName: '',
					principalTel: '',
					orderIndex: '',
					enabled: '',
					ascnId: '',
					cityFullName: '',
				},
				expFormData: {
					longitude: '',
					latitude: '',
					address: '',
					email: '',
					lineCode: ''
				},
			}
		},

		// 方法
		methods: {
			// 初始化树数据
			initDeptTreeData(deptId, selectDeptId) {
				let that = this;
				this.service.ent.tree({deptId: deptId, 'sync': 1}).then((result) => {
					if (result.code == 1) {
						if (result.data.length > 0) {
							that.deptData = result.data;
							// 默认选中根节点
							that.$nextTick(() => {
								if (selectDeptId) {
									that.$refs.deptTree.selectNodeByValue(selectDeptId);
								} else {
									that.$refs.deptTree.selectNodeByValue(result.data[0].id);
								}
							})
						}
					} else {
						// 服务器返回失败
						this.$message.error('部门树加载失败' + result.message)
					}
				})
			},

			// 新增同级方法
			addCurrent() {
				// 界面跳转
				if (!this.deptParam.pdeptId) {
					this.$message.error('请选择部门')
					return
				}
				let param = {"id": "", "pdeptId": this.deptParam.pdeptId, "ascnId": this.formData.ascnId};
				let tabArry = [{
					url: import('../../../../views/sys/dept/ent/add-basicinfo.vue'),
					label: '部门基本信息',
					icon: "chart-line"
				},
					{
						url: import('../../../../views/sys/dept/ent/add-exinfo.vue'),
						label: '部门扩展信息',
						icon: "progressbar"
					}];
				this.$refs.TpDialogTab.show(tabArry, param, "新增");
			},
			// 新增下级
			addChild() {
				// 界面跳转
				if (!this.deptParam.deptId) {
					this.$message.error('请选择部门')
					return
				}
				let param = {"id": "", "pdeptId": this.deptParam.deptId, "ascnId": this.formData.ascnId};
				let tabArry = [{
					url: import('../../../../views/sys/dept/ent/add-basicinfo.vue'),
					label: '部门基本信息',
					icon: "chart-line"
				},
					{
						url: import('../../../../views/sys/dept/ent/add-exinfo.vue'),
						label: '部门扩展信息',
						icon: "progressbar"
					}];
				this.$refs.TpDialogTab.show(tabArry, param, "新增");
			},
			// 修改
			update() {
				if (!this.deptParam.deptId) {
					this.$message.error('请选择修改部门')
					return
				}
				// 界面跳转
				let param = {"id": this.deptParam.deptId};
				let tabArry = [{
					url: import('../../../../views/sys/dept/ent/add-basicinfo.vue'),
					label: '部门基本信息',
					icon: "chart-line"
				},
					{
						url: import('../../../../views/sys/dept/ent/add-exinfo.vue'),
						label: '部门扩展信息',
						icon: "progressbar"
					}];
				this.$refs.TpDialogTab.show(tabArry, param, "修改");
			},
			// 删除方法
			delChild() {
				if (!this.deptParam.deptId) {
					this.$message.error('请先选择一条记录！')
				} else {
					if (this.selectNode.children) {
						this.$message.error('所选部门有子部门，不能删除！请先删除子部门。');
						return;
					}

					this.$confirm('确定要删除吗？', () => {
						// 删除
						this.service.delete(this.formData).then((result) => {
							if (result.code == 1) {
								this.$message.success('删除成功');
								this.$refs.deptTree.init();
							} else {
								// 服务器返回失败
								this.$message.error('删除失败:' + result.message)
							}
						})
					})
				}
			},
			// 新增 / 修改 回调方法
			handleQuery(deptId) {
				// 1、刷新树
				this.initDeptTreeData('', deptId);
				// 2、查询新增 / 修改 的部门信息
				this.handleView(deptId);
			},
			// 查看方法
			handleView(deptId) {
				let that = this;

				// 清空扩展信息表单
				for (let key in that.expFormData) {
					that.expFormData[key] = '';
				}

				this.service.view({"deptId": deptId}).then((result) => {
					// 判断code
					if (result.code == 1) {
						that.formData = result.data
					} else {
						// 服务器返回失败
						that.$message.error('查询失败')
					}
				})

				this.service.expView({"deptId": deptId}).then((result) => {
					// 判断code
					if (result.code == 1) {
						if (result.data) {
							that.expFormData = result.data
						}
					} else {
						// 服务器返回失败
						that.$message.error('查询失败')
					}
				})
			},

			longitude() {
				var param = {'longitude': this.expFormData.longitude, 'latitude': this.expFormData.latitude}
				let options = {
					callBack: function (result) {
						console.log("=============回调查看" + result)
					}
				}
				this.$refs.TpDialog.show(import('../../../../views/common/map/view-map.vue'), param, "天地图", options);
			},

			handleSelectChange(node) {
				if (node) {
					//
					this.deptParam.deptId = node.id
					this.deptParam.pdeptId = node.pid

					// 标记选中节点
					this.selectNode.id = node.id
					this.selectNode.pid = node.pid

					if (node.children.length !== 0) {
						this.selectNode.children = true;
					} else {
						this.selectNode.children = false;
					}

					// 查询节点信息
					this.handleView(node.id);
				} else {
					this.deptParam.deptId = ''
					this.deptParam.pdeptId = ''

					// 取消标记
					this.selectNode.id = ''
					this.selectNode.pid = ''
				}
			},

			closeDialog(param) {
				if (param) {
					this.handleQuery(param)
				}
			},

			closeDialogTab(param) {
				if (param) {
					this.handleQuery(param)
				}
			},

			handleTreeExpand() {
				this.treeExpand = !this.treeExpand
				this.$refs.deptTree.expandAll(this.treeExpand)
			}

		}
	}
</script>

<style lang="less" scoped>

</style>
