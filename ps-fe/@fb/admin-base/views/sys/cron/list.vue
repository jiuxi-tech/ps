<template>
	<div>
		<fb-page-search>
			<template slot="query">
				<fb-form ref="query-form" mode="query" :label-width="160">
					<fb-row>
						<fb-col span="11">
							<fb-form-item label="任务名称">
								<fb-input v-model="formData.jobName"></fb-input>
							</fb-form-item>
						</fb-col>
						<fb-col span="11" offset="1">
							<fb-form-item label="任务组名">
								<fb-input v-model="formData.jobGroupName"></fb-input>
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
				<fb-button type="primary" icon="search" @on-click="handleQuery">查询</fb-button>
			</template>

			<template slot="table">
				<fb-simple-table
					ref="table"
					:data="table.data"
					:pk="table.primaryKey"
					:columns="table.columns"
					:auto-load="false"
					:multiple="false"
					:formatters="table.formatters"
					:scroll="{x:1000, y: 330, autoHeight: true}">
					<template v-slot:actions="props">
						<fb-space>
							<fb-button @on-click="handleEdit(props.row)" v-permission="'SYS_CRON_UPDATE'"
									   editor size="s">修改
							</fb-button>
							<fb-button @on-click="handleResume(props.row)" v-permission="'SYS_CRON_RESUME'"
									   editor size="s">重启
							</fb-button>
							<fb-button @on-click="handlePause(props.row)" v-permission="'SYS_CRON_PAUSE'"
									   editor size="s">停止
							</fb-button>
							<fb-button @on-click="handleDel(props.row)" v-permission="'SYS_CRON_DELETE'"
									   danger size="s">删除
							</fb-button>
						</fb-space>
					</template>

				</fb-simple-table>
			</template>
		</fb-page-search>
		<tp-dialog ref="TpDialog" @closeTpDialog="closeDialog"></tp-dialog>
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
			this.handleQuery();
		},
		data() {
			return {
				formData: {
					jobName: '',
					jobGroupName: '',
				},


				// Table列
				table: {
					formatters: {
						beanName(val) {
							if (jobName) {
								return jobName.split("_")[0];
							}
							return val;
						},
						methodName(val) {
							if (jobName) {
								return jobName.split("_")[1];
							}
							return val;
						},
						triggerState(val) {
							if (val == 'ACQUIRED') {
								return '正常';
							}
							if (val == 'WAITING') {
								return '等待/正常';
							}
							if (val == 'PAUSED') {
								return '暂停';
							}
							if (val == 'BLOCKED') {
								return '阻塞';
							}
							if (val == 'ERROR') {
								return '错误';
							}
							return val;
						}
					},
					// 请求的 url
					service: app.$svc.sys.cron,
					data: [],

					primaryKey: "jobName",
					columns: [
						{
							name: 'jobName',
							label: '任务名称',
							sortable: false,
							width: 250
						}, {
							name: 'jobGroupName',
							label: '任务组名',
							sortable: false,
							width: 180
						}, {
							name: 'cronExpression',
							label: '执行时机',
							sortable: false,
							width: 140
						}, {
							name: 'triggerState',
							label: '当前状态',
							sortable: false,
							width: 100
						}, {
							name: 'description',
							label: '描述',
							sortable: false,
						}, {
							name: '',
							label: '操作',
							sortable: false,
							slot: 'actions',
							width: 236
						},
					],
				},
			}
		},

		// 方法
		methods: {
			// 列表方法
			handleQuery() {

				this.table.service.list().then((result) => {
					this.table.data = result.data;
				})
			},

			// 新增方法
			handleAdd() {

				var param = {}
				this.$refs.TpDialog.show(import('../../../views/sys/cron/add.vue'), param, "新增");
			},
			// 修改方法
			handleEdit(row) {

				var param = {"jobName": row.jobName, "jobGroupName": row.jobGroupName}
				this.$refs.TpDialog.show(import('../../../views/sys/cron/add.vue'), param, "修改");
			},
			// 删除方法
			handleDel(row) {
				this.$confirm('确定要删除吗？', () => {
					this.table.service.delete({
						"jobName": row.jobName,
						"jobGroupName": row.jobGroupName
					}).then((result) => {
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
			handlePause(row) {
				this.$confirm('确定要停止吗？', () => {
					this.table.service.pause({
						"jobName": row.jobName,
						"jobGroupName": row.jobGroupName
					}).then((result) => {
						if (result.code == 1) {
							this.$message.success('停止成功');
							this.handleQuery();
						} else {
							// 服务器返回失败
							this.$message.error('停止失败: ' + result.message)
						}
					});
				})
			},
			// 重启方法
			handleResume(row) {
				this.$confirm('确定要重启吗？', () => {
					this.table.service.resume({
						"jobName": row.jobName,
						"jobGroupName": row.jobGroupName
					}).then((result) => {
						if (result.code == 1) {
							this.$message.success('重启成功');
							this.handleQuery();
						} else {
							// 服务器返回失败
							this.$message.error('重启失败: ' + result.message)
						}
					});
				})
			},

			closeDialog(param) {
				this.handleQuery();
			},
		}
	}
</script>

<style lang="less" scoped>

</style>
