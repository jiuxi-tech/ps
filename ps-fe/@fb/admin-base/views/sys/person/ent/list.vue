<template>
	<div>
		<fb-page-tree-table title="机构树">
			<template slot="tree">
				<fb-tree
					v-autoheight="152"
					ref="tree"
					:service="$svc.sys.dept.ent.tree"
					:param="{deptId: '', sync: 1}"
					:reader="{value:'id', label: 'text'}"
					@on-select-change="handleSelectChange"
					@on-data-change="callBack"></fb-tree>
			</template>
			<template slot="tree-actions">
				<fb-button :icon="treeExpand ? 'tree-expansion': 'tree-closed'"
						   @on-click="handleTreeExpand"></fb-button>
			</template>

			<template slot="query">
				<fb-form ref="query-form" mode="query">
					<fb-row>
						<fb-col span="8">
							<fb-form-item label="用户名">
								<fb-input v-model="formData.personName"></fb-input>
							</fb-form-item>
						</fb-col>
						<fb-col span="8">
							<fb-form-item label="手机号码">
								<fb-input v-model="formData.phone"></fb-input>
							</fb-form-item>
						</fb-col>
						<fb-col span="8">
							<fb-form-item label="性别">
								<fb-select v-model="formData.sex"
										   :data="defaultForm.sex"
										   :placeholder="'请选择'"
										   clearable/>
							</fb-form-item>
						</fb-col>
					</fb-row>
				</fb-form>
			</template>

			<template slot="buttons">
				<fb-button ref="buttonAdd" @on-click="handleAdd" v-permission="'SYS_ENT_PERSON_ADD'" icon="add-circle"
						   :disabled="selectNode.deptId === ''">
					新增
				</fb-button>
				<fb-button ref="buttonDel" @on-click="handleDels" v-permission="'SYS_ENT_PERSON_DELETE'"
						   icon="reduce-circle" :disabled="table.selectRow.length === 0">
					批量删除
				</fb-button>
			</template>

			<template slot="actions">
				<fb-button type="primary" icon="search" @on-click="handleQuery" :disabled="selectNode.deptId === ''">
					查询
				</fb-button>
			</template>

			<template slot="table">
				<fb-simple-table
					ref="table"
					v-model="table.selectRow"
					:service="table.service.ent.list"
					:param="formData"
					:pk="table.primaryKey"
					:columns="table.columns"
					:auto-load="false"
					:scroll="{x:1000, y: 368, autoHeight: true}"
					multiple
					autoSelect>

					<template v-slot:actions="props">
						<fb-space>
							<fb-button @on-click="handleEdit(props.row)" v-permission="'SYS_ENT_PERSON_UPDATE'"
									   v-if="props.row.defaultDept == 1"
									   editor size="s">修改
							</fb-button>
							<fb-button @on-click="handleAccount(props.row)" v-permission="'SYS_ENT_PERSON_ACCOUNT'"
									   editor size="s">账号
							</fb-button>
							<fb-button @on-click="handleAuth(props.row)" v-permission="'SYS_ENT_PERSON_AUTHADD'"
									   editor size="s">授权
							</fb-button>
							<fb-button @on-click="handleParttimejob(props.row)"
									   v-permission="'SYS_ENT_PERSON_PARTTIMEADD'" v-if="props.row.defaultDept == 1"
									   editor size="s">兼职
							</fb-button>
							<fb-button @on-click="handleDataPermission(props.row)"  v-permission="'SYS_ENT_PERSON_DATA_PERM'"
									   editor size="s">数据权限</fb-button>
							<fb-button @on-click="handleDel(props.row)" v-permission="'SYS_ENT_PERSON_DELETE'"
									   danger size="s">删除
							</fb-button>
						</fb-space>
					</template>

					<template v-slot:view="props">
						<fb-link-group>
							<fb-link :click="()=>handleView(props.row)" :label="props.row.personName"
									 type="primary"></fb-link>
						</fb-link-group>
					</template>
				</fb-simple-table>
			</template>
		</fb-page-tree-table>

		<tp-dialog ref="TpDialog" @closeTpDialog="closeDialog"></tp-dialog>
		<tp-dialog-tab ref="TpDialogTab" @closeTpDialog="closeDialogTab"></tp-dialog-tab>
	</div>
</template>

<script>



	export default {
		name: 'list',
		mixins: [

		],
		// 组件
		components: {
			// 'component-a': ComponentA,
		},
		// 初始化方法
		mounted() {
			// 执行界面初始化方法

		},
		data() {
			return {
				treeExpand: true,
				// 部门树根节点
				rootNode: {
					id: '',
					ascnId: ''
				},
				// 选中的节点
				selectNode: {
					deptId: '',
					ascnId: '',
				},
				// 列表查询参数
				formData: {
					deptId: '',
					// 0：本级   1：本级及下级（需要根据层级code模糊查询）
					levelFlag: 0,
					deptLevelcode: '',
					personName: '',
					sex: '',
					phone: '',
					office: '',
					ascnId: '',
				},
				// Table列
				table: {
					// 请求的 url
					selectRow: [],
					service: app.$svc.sys.person,
					primaryKey: "id",
					columns: [
						{
							name: 'personName',
							label: '用户名称',
							slot: 'view',
							sortable: false,
							width: 120
						}, {
							name: 'deptSimpleName',
							label: '部门名称',
							sortable: false,
							width: 120
						}, {
							name: 'defaultDeptName',
							label: '是否兼职',
							sortable: false,
							width: 100
						}, {
							name: 'personNo',
							label: '人员编号',
							sortable: false,
							width: 100
						}, {
							name: 'sexName',
							label: '性别',
							sortable: false,
						},
						{
							name: 'phone',
							label: '手机号码',
							sortable: false,
							width: 120
						},
						{
							name: '',
							label: '操作',
							sortable: false,
							slot: 'actions',
							width: 370,
						},
					],
				},
			}
		},

		// 方法
		methods: {
			// 列表方法
			handleQuery() {
				// 列表查询
				if (this.selectNode.deptId !== '') {
					this.$refs.table.doSearch()
				}
			},

			// 新增方法
			handleAdd() {
				if (!this.selectNode.deptId) {
					this.$message.error('请选择部门添加人员')
					return
				}

				// 界面跳转
				let param = {"id": "", "deptId": this.selectNode.deptId, "ascnId": this.selectNode.ascnId};
				let tabArry = [{
					url: import('../../../../views/sys/person/ent/add-basicinfo.vue'),
					label: '人员基本信息',
					icon: "chart-line"
				},
					{
						url: import('../../../../views/sys/person/ent/add-exinfo.vue'),
						label: '人员扩展信息',
						icon: "progressbar"
					}];

				this.$refs.TpDialogTab.show(tabArry, param, "新增");
			},
			// 修改方法
			handleEdit(row) {
				let param = {"id": row.personId, "deptId": row.deptId, "passKey": row.passKey};
				let tabArry = [{
					url: import('../../../../views/sys/person/ent/add-basicinfo.vue'),
					label: '人员基本信息',
					icon: "chart-line"
				},
				{
					url: import('../../../../views/sys/person/ent/add-exinfo.vue'),
					label: '人员扩展信息',
					icon: "progressbar"
				}];

				this.$refs.TpDialogTab.show(tabArry, param, "修改");
			},
			// 删除方法
			handleDel(row) {

				this.delete(row.deptId, row.personId, row.passKey);
			},
			handleDels() {
				let personIds = '';
				let deptIds = '';
				let rows = this.$refs.table.getSelectedRows()

				if (rows.length == 0) {
					this.$message.error('请先选择一条记录！')
					return;
				} else {
					rows.forEach((r)=> {
						deptIds += "," + r.deptId;
						personIds += "," + r.personId;
					});
				}
				this.delete(deptIds, personIds);
			},
			delete(deptIds, personIds, passKey) {
				this.$confirm('确定要删除吗？', () => {
					app.$svc.sys.person.delete({"deptIds": deptIds, "personIds": personIds, "passKey": passKey}).then((result) => {
						// 判断code
						if (result.code == 1) {
							this.handleQuery();
							this.$message.success('删除成功');
						} else {
							// 服务器返回失败
							this.$message.error('删除失败')
						}
					}).catch((err) => {
						// 服务器返回失败
						console.log(err);
					})
				})
			},
			// 查看方法
			handleView(row) {
				let id = row.personId
				let deptId = row.deptId
				let param = {"id": id, "deptId": deptId, "passKey": row.passKey};

				this.$refs.TpDialog.show(import('../../../../views/sys/person/ent/view.vue'), param, "查看");
			},
			// 授权角色
			handleAuth(row) {
				let param = {"id": row.personId, "deptId": row.deptId, "passKey": row.passKey};
				this.$refs.TpDialog.show(import('../../../../views/sys/person/ent/auth.vue'), param, "授权");
			},
			// 兼职部门
			handleParttimejob(row) {
				let param = {"id": row.personId, "passKey": row.passKey};
				this.$refs.TpDialog.show(import('../../../../views/sys/person/ent/parttime-job.vue'), param, "兼职部门");
			},
			// 开通账号
			handleAccount(row) {
				let param = {"id": row.personId, "passKey": row.passKey};
				this.$refs.TpDialog.show(import('../../../../views/sys/person/ent/account-add.vue'), param, "账号");
			},
			// 数据权限
			handleDataPermission(row) {
				let param = {"personId": row.personId, "deptId": row.deptId, "passKey": row.passKey};
				this.$refs.TpDialog.show(import('../../../../views/sys/person/ent/data-perm.vue'), param, "数据权限");
			},
			callBack(node) {

			},
			handleSelectChange(node) {
				if (node) {
					this.formData.deptId = node.value
					this.formData.ascnId = node.extend01
					this.selectNode.deptId = node.value
					this.selectNode.ascnId = node.extend01
					this.handleQuery();
				} else {
					// 选中节点置空
					this.selectNode.deptId = ''
					this.selectNode.ascnId = ''
					// 没有选中的节点，查询数据为根节点下本级及下级的全部数据
					this.formData.deptId = ''
					this.formData.ascnId = ''
				}
			},
			closeDialog(param) {
				console.log(param);
			},
			closeDialogTab() {
				this.handleQuery();
			},
			handleTreeExpand() {
				this.treeExpand = !this.treeExpand
				this.$refs.tree.expandAll(this.treeExpand)
			}
		}
	}
</script>

<style lang="less" scoped>

</style>
