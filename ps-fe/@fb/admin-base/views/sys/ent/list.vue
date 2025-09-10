<template>
	<div>
		<fb-page-search>
			<template slot="query">
				<fb-form ref="query-form" mode="query" :label-width="160">
					<fb-row>
						<fb-col span="11">
							<fb-form-item label="企业名称">
								<fb-input v-model="formData.entFullName"></fb-input>
							</fb-form-item>
						</fb-col>
						<fb-col span="11" offset="1">
							<fb-form-item label="行政区划">
								<fb-tree-select v-model="formData.entAddrCode"
												:service="$svc.sys.city.tree"
												:param="{'sync': 1, 'expand': false, 'cityId': '-1'}"
												:reader="{value:'id', label: 'text'}"
												:placeholder="'请选择'"
												:header-format="cityTreeHeaderFormat"
												clearable>
								</fb-tree-select>
							</fb-form-item>
						</fb-col>
					</fb-row>
					<fb-row>
						<fb-col span="11">
							<fb-form-item label="法定代表人">
								<fb-input v-model="formData.legalRepr"></fb-input>
							</fb-form-item>
						</fb-col>
						<fb-col span="11" offset="1">
							<fb-form-item label="行业类别">
								<fb-tree-select v-model="formData.industryTypeCode"
												:service="$svc.sys.dict.tree"
												:param="{'dicCode': 'SYS16'}"
												:reader="{value: 'id', label: 'text'}"
												placeholder="请选择"
												clearable>
								</fb-tree-select>
							</fb-form-item>
						</fb-col>
					</fb-row>
					<fb-row>
						<fb-col span="11">
							<fb-form-item label="时间">
								<tp-datepicker clearable
											   format="YYYY-MM-DD HH:mm:ss" v-model="formData.currentDate"
											   value-format="YYYYMMDDHHmmss"></tp-datepicker>
							</fb-form-item>
						</fb-col>
					</fb-row>
				</fb-form>
			</template>

			<template slot="buttons">
				<fb-button ref="buttonAdd" @on-click="handleAdd" v-permission="'SYS_ENTMANAGE_ENT_ADD'"
						   icon="add-circle">新增
				</fb-button>
			</template>

			<template slot="actions">
				<fb-button @on-click="handleQuery" icon="search" type="primary">查 询</fb-button>
				<fb-button @on-click="formReset" icon="search">重 置</fb-button>
			</template>

			<template slot="table">
				<fb-simple-table
					ref="table"
					:service="table.service.list"
					:param="formData"
					:pk="table.primaryKey"
					:columns="table.columns"
					auto-load
					:multiple="false"
					:scroll="{x:1100, y: 372, autoHeight: true}"
					@on-row-select="handleTableSelect">
					<template v-slot:actions="props">
						<fb-space>
							<fb-button @on-click="handleEdit(props.row)" v-permission="'SYS_ENTMANAGE_ENT_UPDATE'"
									   editor size="s">修改</fb-button>
							<fb-button @on-click="handleDel(props.row)" v-permission="'SYS_ENTMANAGE_ENT_DELETE'"
									   danger size="s">删除</fb-button>
						</fb-space>
					</template>

					<template v-slot:view="props">
						<fb-link-group>
							<fb-link :click="()=>handleView(props.row)" :label="props.row.entFullName" type="primary"></fb-link>
						</fb-link-group>
					</template>
				</fb-simple-table>
			</template>
		</fb-page-search>
		<tp-dialog ref="TpDialog" @closeTpDialog="closeDialog"></tp-dialog>
		<tp-dialog-tab ref="TpDialogTab" @closeTpDialog="closeDialogTab"></tp-dialog-tab>
	</div>
</template>

<script>


	import FbRow from "@fb/fb-ui/packages/components/row/src/FbRow";

	export default {
		mixins: [

		],
		// 组件
		components: {
			FbRow
			// 'component-a': ComponentA,
		},
		// 初始化方法
		mounted() {
			// 执行界面初始化
			this.createdPage()
		},
		data() {
			return {
				formData: {
					entFullName: '',
					entAddrCode: '',
					legalRepr: '',
					industryTypeCode: '',
					currentDate: null,
				},
				// Table列
				table: {
					// 请求的 url
					service: app.$svc.sys.ent,

					primaryKey: "entId",
					columns: [
						{
							name: 'entFullName',
							label: '企业名称',
							slot: 'view',
							sortable: false,
							width: 200
						}, {
							name: 'prodAddrName',
							label: '行政区划',
							sortable: false,
							width: 200
						},{
							name: 'legalRepr',
							label: '法定代表人',
							sortable: false,
							width: 100
						},
						{
							name: 'legalReprTel',
							label: '联系电话',
							sortable: false,
							width: 120
						},
						{
							name: 'safetyDirectorName',
							label: '安全生产管理员',
							sortable: false,
							width: 100
						},
						{
							name: 'safetyDirectorTel',
							label: '安全生产管理员手机',
							sortable: false,
							width: 120
						},
						{
							name: 'industryTypeCodeName',
							label: '行业类别',
							sortable: false,
						},
						{
							name: '',
							label: '操作',
							sortable: false,
							slot: 'actions',
							width: 124,
						},

					],
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
				let param = {};
				let options = {
					tabChangeConfirm: false,
					callBack: (result) => {
						this.$refs.table.doReload()
					}
				}
				this.$refs.TpDialog.show(import('../../../views/sys/ent/add-basicinfo.vue'), param, "新增", options);
			},
			// 修改方法
			handleEdit(row) {

				let param = {"id": row.entId, "passKey": row.passKey};
				let options = {
					tabChangeConfirm: false,
					callBack: (result) => {
						this.$refs.table.doReload()
					}
				}
				this.$refs.TpDialog.show(import('../../../views/sys/ent/add-basicinfo.vue'), param, "修改", options);
			},
			// 删除方法
			handleDel(row) {
				this.$confirm('确定要删除吗？', () => {
					this.table.service.delete({"entId": row.entId, "passKey": row.passKey}).then((result) => {
						if (result.code == 1) {
							this.$message.success('删除成功');
							this.handleQuery();
						} else {
							// 服务器返回失败
							this.$message.error('删除失败: ' + result.message)
						}
					});
				})
			},
			// 查看方法
			handleView(row) {
				console.log("查看...");

				var id = row.entId
				var param = {"id": id, "passKey": row.passKey}

				let options = {
					tabChangeConfirm: false,
					callBack: function (result) {
						console.log("=============回调查看" + result)
					}
				}
				this.$refs.TpDialog.show(import('../../../views/sys/ent/view.vue'), param, "查看", options);
			},

			closeDialog(param) {
				this.$refs.table.doReload()
			},
			closeDialogTab(param) {
				this.$refs.table.doReload()
			},
		}
	}
</script>

<style lang="less" scoped>

</style>
