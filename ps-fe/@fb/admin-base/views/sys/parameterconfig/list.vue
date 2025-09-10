<template>
	<div>
		<fb-page-search>
			<template slot="query">
				<fb-form ref="query-form" mode="query" :label-width="160">
					<fb-row>
						<fb-col span="11">
							<fb-form-item label="参数名称">
								<fb-input v-model="formData.pmName"></fb-input>
							</fb-form-item>
						</fb-col>
						<fb-col span="11">
							<fb-form-item label="参数key">
								<fb-input v-model="formData.pmKey"></fb-input>
							</fb-form-item>
						</fb-col>
					</fb-row>
				</fb-form>
			</template>

			<template slot="buttons">
				<fb-button ref="buttonAdd" @on-click="handleAdd" icon="add-circle">新增</fb-button>
			</template>

			<template slot="actions">
				<fb-button type="primary" icon="search" @on-click="handleQuery">查询</fb-button>
			</template>

			<template slot="table">
				<fb-simple-table
					ref="table"
					:service="table.service.search"
					:param="formData"
					:pk="table.primaryKey"
					:columns="table.columns"
					:multiple="false"
					:formatters="table.formatters"
					:scroll="{x:1100, y: 372, autoHeight: true}"
					auto-load>

					<template v-slot:actions="props">
						<fb-space>
							<fb-button @on-click="handleEdit(props.row)" editor size="s">修改</fb-button>
							<fb-button @on-click="handleDel(props.row)"  danger size="s">删除</fb-button>
						</fb-space>
					</template>

					<template v-slot:view="props">
						<fb-link-group>
							<fb-link :click="()=>handleView(props.row)" :label="props.row.pmName" type="primary"></fb-link>
						</fb-link-group>
					</template>
				</fb-simple-table>
			</template>
		</fb-page-search>
		<tp-dialog ref="TpDialog"></tp-dialog>
		<tp-dialog-tab ref="TpDialogTab"></tp-dialog-tab>
	</div>
</template>

<script>

	export default {

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
				formData: {
					pmName: '',
					pmKey: '',
				},
				// Table列
				table: {
					// 请求的 url
					service: app.$svc.sys.parameterconfig,

					primaryKey: "pmId",
					columns: [
						{
							name: 'pmName',
							label: '参数名称',
							sortable: false,
							width: 150,
							slot: 'view',
						},
						{
							name: 'pmKey',
							label: '参数key',
							sortable: false,
							width: 150,
						},
						{
							name: 'pmVal',
							label: '参数值',
							sortable: false,
							width: 150,
						},
						{
							name: 'pmDesc',
							label: '参数配置描述',
							sortable: false,
							width: 150,
						},
						{
							name: 'orderIndex',
							label: '排序号',
							sortable: false,
							width: 50,
						},
						{
							name: 'enabled',
							label: '是否启用',
							sortable: false,
							width: 50,
						},
						{
							name: '',
							label: '操作',
							sortable: false,
							slot: 'actions',
							width: 124,
						},
					],
					formatters: {
						enabled(val) {
							return val == "1" ? '启用' : '停用'
						},
					},
				},
			}
		},

		// 方法
		methods: {
			// 列表方法
			handleQuery() {
				this.$refs.table.doSearch()
			},
			// 新增方法
			handleAdd() {
				let param = {}
				let options = {
					tabChangeConfirm: false,
					callBack: (result) => {
						this.$refs.table.doReload()
					}
				}
				this.$refs.TpDialog.show(import('../../../views/sys/parameterconfig/add.vue'), param, "新增", options);
			},
			// 修改方法
			handleEdit(row) {
				let param = {"id": row.pmId, "passKey": row.passKey};
				let options = {
					tabChangeConfirm: false,
					callBack: (result) => {
						this.$refs.table.doReload();
					}
				}

				this.$refs.TpDialog.show(import('../../../views/sys/parameterconfig/add.vue'), param, "修改", options);
			},
			// 删除方法
			handleDel(row) {
				this.$confirm('确定要删除吗？', () => {
					this.table.service.delete({"pmId": row.pmId, "passKey": row.passKey}).then((result) => {
						if (result && result.code == 1) {
							this.$message.success('删除成功');
							this.$refs.table.doReload();
						} else {
							// 服务器返回失败
							this.$message.error('删除失败: ' + result.message)
						}
					});
				})
			},
			// 查看方法
			handleView(row) {
				let param = {"id": row.pmId, "passKey": row.passKey}

				let options = {
					tabChangeConfirm: false,
					callBack: (result) => {
						console.log("=============回调查看" + result)
					}
				}
				this.$refs.TpDialog.show(import('../../../views/sys/parameterconfig/view.vue'), param, "查看", options);
			},
		}
	}
</script>

<style lang="less" scoped>
</style>
