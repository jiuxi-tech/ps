<template>
	<div>
		<fb-page-search>
			<template slot="query">
				<fb-form ref="query-form" mode="query">
					<fb-row>
						<fb-col span="8">
							<fb-form-item label="姓名">
								<fb-input v-model="formData.presonName"></fb-input>
							</fb-form-item>
						</fb-col>
						<fb-col span="8">
							<fb-form-item label="手机号">
								<fb-input v-model="formData.presonPhone"></fb-input>
							</fb-form-item>
						</fb-col>
						<fb-col span="8">
							<fb-form-item label="审核状态">
								<fb-select v-model="formData.auditStatus" :data="auditStatusOptions" clearable></fb-select>
							</fb-form-item>
						</fb-col>
					</fb-row>
					<!-- <fb-row>
						<fb-col span="8">
							<fb-form-item label="提交时间">
								<tp-datepicker v-model="formData.submitTimeStart" format="YYYY-MM-DD" value-format="YYYYMMDD" placeholder="开始日期" clearable></tp-datepicker>
							</fb-form-item>
						</fb-col>
						<fb-col span="8">
							<fb-form-item label="至">
								<tp-datepicker v-model="formData.submitTimeEnd" format="YYYY-MM-DD" value-format="YYYYMMDD" placeholder="结束日期" clearable></tp-datepicker>
							</fb-form-item>
						</fb-col>
						<fb-col span="8">
							<fb-form-item label="部门">
								<fb-input v-model="formData.deptName"></fb-input>
							</fb-form-item>
						</fb-col>
					</fb-row> -->
				</fb-form>
			</template>

			<template slot="actions">
				<fb-button type="primary" icon="search" @on-click="handleQuery">查询</fb-button>
				<fb-button icon="refresh" @on-click="handleReset">重置</fb-button>
			</template>

			<template slot="table">
				<fb-simple-table
					ref="table"
					:service="table.service.list"
					:param="formData"
					:pk="table.primaryKey"
					:columns="table.columns"
					:multiple="false"
					auto-load
					:formatters="formatters"
					:scroll="{x:900, y: 330, autoHeight: true}"
					@on-row-select="handleTableSelect">

					<template v-slot:actions="props">
						<fb-space>
							<fb-button 
								v-if="props.row.auditStatus === 0"
								@on-click="handleAudit(props.row)" 
								type="primary" 
								size="s">审核</fb-button>
							<fb-button 
							@on-click="handleDelete(props.row)" 
							danger 
							size="s">删除</fb-button>
						</fb-space>
					</template>

					<template v-slot:applicantName="props">
						<fb-link-group>
							<fb-link :click="()=>handleView(props.row)" :label="props.row.applicantName" type="primary"></fb-link>
						</fb-link-group>
					</template>

					<template v-slot:auditStatus="props">
						<span :class="getStatusClass(props.row.auditStatus)">
							{{ getStatusText(props.row.auditStatus) }}
						</span>
					</template>

				</fb-simple-table>
			</template>
		</fb-page-search>

		<tp-dialog ref="TpDialog" @closeTpDialog="closeDialog"></tp-dialog>
	</div>
</template>

<script>
	import dayjs from "dayjs";

	export default {
		name: 'audit-list',
		mixins: [],

		// 初始化方法
		mounted() {
			// 执行界面初始化方法
		},
		data() {
			return {
				formData: {
					presonName: '',
					presonPhone: '',
					auditStatus: '',
					submitTimeStart: '',
					submitTimeEnd: '',
					deptName: ''
				},

				// 审核状态选项
				auditStatusOptions: [
					{ value: 0, label: '待审核' },
					{ value: 1, label: '审核通过' },
					{ value: 2, label: '审核驳回' },
					{ value: 3, label: '已取消' }
				],

				formatters: {
					submitTime(val) {
						return val ? dayjs(val).format('YYYY-MM-DD HH:mm:ss') : val;
					},
					auditTime(val) {
						return val ? dayjs(val).format('YYYY-MM-DD HH:mm:ss') : val;
					}
				},

				// Table列
				table: {
					// 请求的 service
					service: app.$svc.sys.person.audit,
					primaryKey: "auditId",
					columns: [
						{
							name: 'personName',
							label: '申请人姓名',
							slot: 'personName',
							sortable: false,
							width: 120,
						}, 
						 {
							name: 'auditStatus',
							label: '审核状态',
							slot: 'auditStatus',
							sortable: false,
							width: 100,
						},
						{
							name: 'auditReason',
							label: '审核理由',
							sortable: false,
						},
						{
							name: 'personPhone',
							label: '手机号',
							sortable: false,
							width: 120,
						}, {
							name: 'deptName',
							label: '部门',
							sortable: false,
							width: 150,
						},   {
							name: 'submitTime',
							label: '提交时间',
							sortable: false,
							width: 150,
						}, {
							name: 'auditorName',
							label: '审核人',
							sortable: false,
							width: 100,
						}, {
							name: 'auditTime',
							label: '审核时间',
							sortable: false,
							width: 150,
						},
						{
							freeze: "right",
							name: '',
							label: '操作',
							sortable: false,
							slot: 'actions',
							width: 80,
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

			// 重置查询条件
			handleReset() {
				this.formData = {
					presonName: '',
					presonPhone: '',
					auditStatus: '',
					submitTimeStart: '',
					submitTimeEnd: '',
					deptName: ''
				}
				this.handleQuery()
			},

			// 审核方法
			handleAudit(row) {
				let param = {"auditId": row.auditId, "passKey": row.passKey};
				let options = {"height": 600, "width": 800};
 
				this.$refs.TpDialog.show(import('./add.vue'), param, "审核个人信息修改申请", options);
			},

			// 查看方法
			handleView(row) {
				let param = {"auditId": row.auditId,   "passKey": row.passKey};
				let options = {"height": 600, "width": 800};
				this.$refs.TpDialog.show(import('./view.vue'), param, "查看个人信息修改详情", options);
			},

			/**
			 * 删除审核申请
			 * @param {Object} row - 要删除的审核申请数据
			 */
			handleDelete(row) {
				this.$confirm('确定要删除吗？', () => {
					this.$service.sys.person.audit.deleteAuditRequest(row.requestId)
						.then(response => {
							if (response.code === 1) {
								this.handleQuery();
								this.$message.success('删除成功');
							} else {
								this.$message.error(response.message || '删除失败');
							}
						})
						.catch(error => {
							console.error('删除失败:', error);
							this.$message.error('删除失败，请稍后重试');
						});
				});
			},

			// 获取状态样式类
			getStatusClass(status) {
				switch (status) {
					case 0:
						return 'status-pending'
					case 1:
						return 'status-approved'
					case 2:
						return 'status-rejected'
					case 3:
						return 'status-cancelled'
					default:
						return ''
				}
			},

			// 获取状态文本
			getStatusText(status) {
				switch (status) {
					case 0:
						return '待审核'
					case 1:
						return '审核通过'
					case 2:
						return '审核驳回'
					case 3:
						return '已取消'
					default:
						return '未知'
				}
			},

			closeDialog(param) {
				this.handleQuery();
			},
			handleTableSelect(row) {
				// 表格行选择事件
			}
		}
	}
</script>

<style lang="less" scoped>
	.status-pending {
		color: #faad14;
	}

	.status-approved {
		color: #52c41a;
	}

	.status-rejected {
		color: #ff4d4f;
	}

	.status-cancelled {
		color: #8c8c8c;
	}
</style>