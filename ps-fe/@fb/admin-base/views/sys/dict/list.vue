<template>
	<div>
		<fb-page-tree-edit :tree-title="treeTitle" list-title="字典">
			<template slot="list-actions">
				<fb-button ref="buttonSave" @on-click="handleAdd" icon="add-circle">新增</fb-button>
			</template>

			<template slot="list">
				<div v-autoheight="136" style="overflow-y: auto; margin: -18px; padding: 0 16px;">
					<div style="padding: 15px 0">
						<fb-input v-model="dicNameOrCode" placeholder="请输入字典名称或编码">
							<fb-button slot="append-button" @on-click="handleQuery" type="primary"
									   icon="search"></fb-button>
						</fb-input>
					</div>

					<fb-list
						ref="list"
						:pager-align="'center'"
						:pager-max-length="3"
						:pager-simple="true"
						:service="service.list"
						@on-select-change="handleSelectChange">
						<fb-list-item slot="item" slot-scope="{item, index}">
							<fb-list-item-meta :description="item.dicName">
								<fb-link slot="title" :click="()=>doClick(item)" :label="item.dicCode"
										 type="primary"></fb-link>
							</fb-list-item-meta>
							<fb-tag slot="extra" :type="item.enabled == 1 ? 'success': 'danger'" effect="light">
								{{item.enabled == 1 ? '已启用': '未启用'}}{{item.enable}}
							</fb-tag>
						</fb-list-item>
					</fb-list>
				</div>
			</template>


			<template slot="tree">
				<div v-autoheight="136" style="overflow-y: auto; margin: -16px; padding: 16px;">
					<fb-tree
						ref="dicTree"
						:data="dicData"
						:reader="{value: 'id', label: 'text'}"
						@on-select-change="handleSelectChange"></fb-tree>
				</div>
			</template>


			<template slot="actions">
				<fb-button ref="buttonSave" @on-click="handleAddChild" icon="add-circle"
						   :disabled="selectNode.dicId === ''">新增下级
				</fb-button>
				<fb-button ref="buttonEdit" @on-click="handleEdit" icon="reduce-circle"
						   :disabled="selectNode.dicId === ''">
					修改
				</fb-button>
				<fb-button ref="buttonDel" @on-click="handleDel" icon="editor-square"
						   :disabled="selectNode.dicId === ''">
					删除
				</fb-button>
			</template>
			<template slot="form">
				<fb-property bordered label-width="140px" mode="form">
					<fb-row>
						<fb-col span="12">
							<fb-property-item label="字典编码">
								{{formView.dicCode}}
							</fb-property-item>
						</fb-col>
						<fb-col span="12">
							<fb-property-item label="字典名称">
								{{formView.dicName}}
							</fb-property-item>
						</fb-col>
					</fb-row>
					<fb-row>
						<fb-col span="12">
							<fb-property-item label="字典类型">
								{{formView.dicTypeName}}
							</fb-property-item>
						</fb-col>
						<fb-col span="12">
							<fb-property-item label="排序">
								{{formView.orderIndex}}
							</fb-property-item>
						</fb-col>
					</fb-row>
					<fb-row>
						<fb-col span="24">
							<fb-property-item label="字典描述">
								{{formView.dicDesc}}
							</fb-property-item>
						</fb-col>
					</fb-row>
					<fb-row>
						<fb-col span="12">
							<fb-property-item label="是否启用">
								{{formView.enabled === 1 ? "是" : "否"}}
							</fb-property-item>
						</fb-col>
					</fb-row>

				</fb-property>
			</template>
		</fb-page-tree-edit>

		<tp-dialog ref="TpDialog" @closeTpDialog="closeDialog"></tp-dialog>
	</div>
</template>

<script>



	export default {
		name: 'list',
		mixins: [

		],
		// 初始化方法
		mounted() {

			// 执行界面初始化方法


		},
		data() {
			return {
				dicNameOrCode: '',
				service: app.$svc.sys.dict,
				// 树标题
				treeTitle: '',
				// Table列
				table: {
					// 请求的 url
					primaryKey: "dicId",
					columns: [
						{
							name: 'dicId',
							hidden: true,
							width: 1,
						}, {
							name: 'pdicId',
							hidden: true,
							width: 1,
						}, {
							name: 'dicCode',
							label: '字典编码',
							sortable: false,
							align: 'left',
						}, {
							name: 'dicName',
							label: '字典名称',
							sortable: false,
							align: 'left',
						}, {
							name: 'dicDesc',
							label: '字典描述',
							sortable: false,
							align: 'left',
						},
						{
							name: 'enabled',
							label: '是否启用',
							sortable: false,
							align: 'left',
						},
						{
							name: '',
							label: '操作',
							sortable: false,
							align: 'left',
							slot: 'actions',
						},
					],
				},
				// 选中的列表
				selectListItem: {
					dicCode: '',
				},
				// 字典树数据
				dicData: [],
				// 选中节点
				selectNode: {
					dicId: '',
					pdicId: '',
					dicCode: ''
				},
				// 字典详情数据
				formView: {
					dicId: '',
					pdicId: '',
					dicCode: '',
					dicName: '',
					dicTypeName: '',
					orderIndex: '',
					dicDesc: '',
					enabled: '',
				},
			}
		},

		// 方法
		methods: {
			// 列表方法
			handleQuery() {
				this.$refs.list.doSearch({'dicName': this.dicNameOrCode, 'dicCode': this.dicNameOrCode})
			},
			doClick(item) {
				let dicCode = item.dicCode;
				// 选中的列表
				this.selectListItem.dicCode = dicCode;
				// 树标题
				this.treeTitle = dicCode;
				this.treeData(dicCode);
			},
			treeData(dicCode) {
				this.service.tree({"dicCode": dicCode, "topFlag": true}).then((result) => {
					if (result.code == 1) {
						this.dicData = result.data;
						// 默认选中根节点
						this.$nextTick(() => {
							this.$refs.dicTree.selectNodeByValue(dicCode);
						})
					} else {
						// 服务器返回失败
						this.$message.error('字典树加载失败' + result.message)
					}
				})
			},

			// 新增字典根
			handleAdd() {
				this.$refs.TpDialog.show(import('../../../views/sys/dict/add.vue'), {}, "新增");
			},
			// 新增下级
			handleAddChild() {
				let param = {"pdicId": this.selectNode.dicId, "dicCode": this.selectNode.dicCode};

				this.$refs.TpDialog.show(import('../../../views/sys/dict/add.vue'), param, "修改");
			},
			// 修改方法
			handleEdit() {
				let param = {
					"id": this.selectNode.dicId,
					"pdicId": this.selectNode.pdicId,
					"dicCode": this.selectNode.dicCode
				};

				this.$refs.TpDialog.show(import('../../../views/sys/dict/add.vue'), param, "修改");

			},
			// 删除方法
			handleDel() {
				if (this.selectListItem.dicCode === '') {
					this.$message.error('请先选择一条记录！')
				} else {
					let that = this;
					this.$confirm('该字典及子节点字典值将全部被删除，确定要删除吗？', () => {
						that.service.delete({"dicCodes": this.selectListItem.dicCode}).then((result) => {
							if (result.code == 1) {
								this.handleQuery();
								that.$message.success('删除成功');
							} else {
								// 服务器返回失败
								that.$message.error('删除失败:' + result.message)
							}
						})
					})
				}
			},
			// 查看方法
			handleView(dicId) {
				let that = this;
				this.service.view({"dicId": dicId}).then((result) => {
					// 判断code
					if (result.code == 1) {
						that.formView = result.data;
					} else {
						// 服务器返回失败
						that.$message.error('查询失败')
					}
				})
			},
			// 字典树改变回调
			handleSelectChange(node) {
				if (node) {
					this.selectNode.dicId = node.extend
					this.selectNode.pdicId = node.pid
					this.selectNode.dicCode = node.id
					this.selectListItem.dicCode = node.id

					this.handleView(node.extend);
				} else {
					// 取消标记
					this.selectNode.dicId = ''
					this.selectNode.pdicId = ''
					this.selectNode.dicCode = ''
				}
			},

			addDialogCallBack() {

			},

			closeDialog(param) {
				if (param) {
					if (param === -1) {
						this.handleQuery();
					} else {
						this.treeData(param);
					}
				}
			},
		}
	}
</script>

<style lang="less" scoped>

</style>
