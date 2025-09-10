<template>
	<div>
		<fb-page-search>
			<template slot="query">
				<fb-form :label-width="160" mode="query" ref="query-form">
					<fb-row>
						<fb-col span="11">
							<fb-form-item label="合作方名称">
								<fb-input v-model="formData.partnerName"></fb-input>
							</fb-form-item>
						</fb-col>
					</fb-row>
				</fb-form>
			</template>

			<template slot="buttons">
				<!--<fb-button ref="buttonAdd" @on-click="handleAdd" v-permission = "'IOT_THREE_ACCOUNT_ADD'" icon="add-circle">add</fb-button> v-permission指令权限控制-->
				<fb-button @on-click="handleAdd" icon="add-circle" ref="buttonAdd"
						   v-permission="'SYS_ACCOUNT_THIRD_ADD'">新增
				</fb-button>
			</template>

			<template slot="actions">
				<fb-button @on-click="handleQuery" icon="search" type="primary">查询</fb-button>
			</template>

			<template slot="table">
				<fb-simple-table
					:columns="table.columns"
					:multiple="false"
					:param="formData"
					:pk="table.primaryKey"
					:scroll="{x:1100, y: 372, autoHeight: true}"
					:service="table.service.search"
					auto-load
					ref="table">

					<template v-slot:actions="props">
						<fb-space>
							<fb-button @on-click="handleReset(props.row)" editor
									   size="s" v-permission="'SYS_ACCOUNT_THIRD_RESET'">重置Secret
							</fb-button>
							<fb-button @on-click="handleDel(props.row)" danger size="s"
									   v-permission="'SYS_ACCOUNT_THIRD_DELETE'">删除
							</fb-button>
						</fb-space>
					</template>

					<template v-slot:view="props">
						<fb-text style="float: left;">{{props.row.appSecret}}</fb-text>
						<fb-link-group style="float: left;margin-left: 5px;">
							<fb-link :click="()=>handleView(props.row, props.rowIndex)" :label="props.row.showLabel"
									 type="primary"></fb-link>
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
				formData: {
					partnerName: '',
				},
				// Table列
				table: {
					// 请求的 url
					service: app.$svc.sys.accountthird,

					primaryKey: "appKey",
					columns: [
						{
							name: 'partnerName',
							label: '合作方名称',
							sortable: false,
							width: 150,
						},
						{
							name: 'createTime',
							label: '创建时间',
							sortable: false,
							width: 80,
						},
						{
							name: 'appKey',
							label: '合作方Key',
							sortable: false,
							width: 150,
						},
						{
							name: 'appSecret',
							label: '合作方Secret',
							sortable: false,
							width: 220,
							slot:'view'
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
				let param = {}
				let that = this;

				let options = {
					tabChangeConfirm: false,
					width: 600,
					height: 300,
					callBack: function (result) {
						that.$refs.table.doReload()
					}
				}

				this.$refs.TpDialog.show(import('../../../views/sys/accountthird/add.vue'), param, "新增", options);
			},
			//重置方法
			handleReset(row) {
				this.$confirm('确定要重置吗？', () => {
					let that = this;
					this.table.service.reset({"appKey": row.appKey}).then((result) => {
						if (result.code == 1) {
							this.$message.success('重置成功');
							that.$refs.table.doReload()
						} else {
							// 服务器返回失败
							this.$message.error('重置失败: ' + result.message)
						}
					});
				})
			},
			// 删除方法
			handleDel(row) {
				this.$confirm('确定要删除吗？', () => {
					this.table.service.delete({"appKeys": row.appKey}).then((result) => {
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
			handleView(row,rowIndex) {
				let data=this.$refs.table.myData
				// 当前显示
				if(data[rowIndex].showSecret){
					data[rowIndex].showLabel='显示'
					data[rowIndex].appSecret=row.appSecretHide;
					data[rowIndex].showSecret=false
				}else{
					data[rowIndex].showLabel='隐藏'
					data[rowIndex].appSecret=row.appSecretShow;
					data[rowIndex].showSecret=true
				}
			},
		}
	}
</script>

<style lang="less" scoped>
</style>
