<template>
	<div>
		<fb-page-search>
			<template slot="query">
				<fb-form ref="query-form" mode="query">
					<fb-row>
						<fb-col span="24">
							<fb-form-item label="用户名">
								<fb-input v-model="formData.search" placeholder="请输入用户名、应用ID或IP地址"></fb-input>
							</fb-form-item>
						</fb-col>
					</fb-row>
				</fb-form>
			</template>

			<template slot="buttons">
				<fb-button ref="buttonRefresh" @on-click="handleRefresh" icon="refresh">
					刷新
				</fb-button>
				<fb-button ref="buttonBatchLogout" @on-click="handleBatchLogout" 
						   type="danger" icon="delete" :disabled="selectedRows.length === 0">
					批量注销
				</fb-button>
			</template>

			<template slot="actions">
				<fb-button type="primary" icon="search" @on-click="handleQuery">查询</fb-button>
			</template>

			<template slot="table">
				<fb-simple-table
					ref="table"
					:service="table.service.list"
					:param="formData"
					:pk="table.primaryKey"
					:columns="table.columns"
					:multiple="true"
					auto-load
					:formatters="formatters"
					:scroll="{x:1400, y: 400, autoHeight: true}"
					@on-row-select="handleTableSelect">

					<template v-slot:actions="props">
						<fb-space>
							<fb-button @on-click="handleLogout(props.row)" 
									   danger size="s" icon="logout">强制注销</fb-button>
						</fb-space>
					</template>

					<template v-slot:username="props">
						<span :title="props.row.email">{{ props.row.username || props.row.userId }}</span>
					</template>

					<template v-slot:status="props">
						<span :class="getStatusClass(props.row)">{{ getStatusText(props.row) }}</span>
					</template>

				</fb-simple-table>
			</template>
		</fb-page-search>
	</div>
</template>

<script>
	import dayjs from "dayjs";

	export default {
		name: 'session',
		mixins: [],

		// 初始化方法
		mounted() {
			// 执行界面初始化方法
		},
		data() {
			return {
				formData: {
					search: '', // 搜索关键词
				},

				selectedRows: [], // 选中的行

				formatters: {
					loginTime(val) {
						return val ? dayjs(val).format('YYYY-MM-DD HH:mm:ss') : '-';
					},
					lastAccessTime(val) {
						return val ? dayjs(val).format('YYYY-MM-DD HH:mm:ss') : '-';
					},
					duration(val, row) {
						if (row.start && row.lastAccess) {
							const duration = Math.floor((row.lastAccess - row.start) / 1000 / 60); // 分钟
							if (duration < 60) {
								return duration + ' 分钟';
							} else {
								const hours = Math.floor(duration / 60);
								const minutes = duration % 60;
								return hours + ' 小时 ' + minutes + ' 分钟';
							}
						}
						return '-';
					}
				},

				// Table列
				table: {
					// 请求的 url
					service: app.$svc.sys.sso.admin.session,
					primaryKey: "id",
					columns: [
						{
							name: 'username',
							label: '用户',
							slot: 'username',
							sortable: true,
							width: 120,
						},  {
							name: 'status',
							label: '状态',
							slot: 'status',
							sortable: false,
							width: 80,
						},{
							name: 'clientId',
							label: '应用ID',
							sortable: true,
						}, {
							name: 'ipAddress',
							label: 'IP地址',
							sortable: true,
							width: 120,
						}, {
							name: 'loginTime',
							label: '登录时间',
							sortable: true,
							width: 150,
						}, {
							name: 'lastAccessTime',
							label: '最后访问',
							sortable: true,
							width: 150,
						}, {
							name: 'duration',
							label: '会话时长',
							sortable: false,
							width: 120,
						},
						{
							name: 'id',
							label: '会话ID',
							sortable: true,
							width: 200,
							ellipsis: true,
						}, 
						{
							freeze: "right",
							name: '',
							label: '操作',
							sortable: false,
							slot: 'actions',
							width: 120,
						},
					],
				},
			}
		},

		// 方法
		methods: {
			// 查询方法
			handleQuery() {
				this.$refs.table.doSearch()
			},

			// 刷新方法
			handleRefresh() {
				this.formData.search = '';
				this.selectedRows = [];
				this.$refs.table.doSearch();
			},

			// 强制注销单个会话
			handleLogout(row) {
				this.$confirm(`确定要强制注销用户 "${row.username || row.userId}" 的会话吗？`, () => {
					this.logoutSession(row.id);
				})
			},

			// 批量注销会话
			handleBatchLogout() {
				if (this.selectedRows.length === 0) {
					this.$message.warn('请选择要注销的会话');
					return;
				}

				this.$confirm(`确定要批量注销选中的 ${this.selectedRows.length} 个会话吗？`, () => {
					const sessionIds = this.selectedRows.map(row => row.id);
					this.batchLogoutSessions(sessionIds);
				})
			},

			// 执行单个会话注销
			logoutSession(sessionId) {
				app.service.request('/sso/admin/session/logout', {
					method: 'post',
					data: { sessionId: sessionId },
					headers: {'Content-Type': 'application/json'},
					responseType: 'json',
					 
				}).then((result) => {
					if (result.code == 1) {
						this.$message.success('会话注销成功');
						this.handleQuery();
					} else {
						this.$message.error('会话注销失败: ' + result.message)
					}
				}).catch((error) => {
					this.$message.error('会话注销失败: ' + error.message);
				})
			},

			// 执行批量会话注销
			batchLogoutSessions(sessionIds) {
				app.service.request('/sso/admin/session/batch-logout', {
					method: 'post',
					data: { sessionIds: sessionIds },
					headers: {'Content-Type': 'application/json'},
					responseType: 'json',
					timeout: 30000,
				}).then((result) => {
					if (result.code == 1) {
						const data = result.data;
						this.$message.success(`批量注销完成：成功 ${data.success} 个，失败 ${data.fail} 个`);
						this.selectedRows = [];
						this.handleQuery();
					} else {
						this.$message.error('批量注销失败: ' + result.message)
					}
				}).catch((error) => {
					this.$message.error('批量注销失败: ' + error.message);
				})
			},

			// 表格行选择事件
			handleTableSelect(rows) {
				this.selectedRows = rows;
			},

			// 获取会话状态样式
			getStatusClass(row) {
				if (this.isSessionExpired(row)) {
					return 'status-expired';
				}
				return 'status-active';
			},

			// 获取会话状态文本
			getStatusText(row) {
				if (this.isSessionExpired(row)) {
					return '已过期';
				}
				return '活跃';
			},

			// 判断会话是否过期（简单判断：超过2小时未访问）
			isSessionExpired(row) {
				if (!row.lastAccess) return false;
				const now = Date.now();
				const lastAccess = new Date(row.lastAccess).getTime();
				const twoHours = 2 * 60 * 60 * 1000; // 2小时的毫秒数
				return (now - lastAccess) > twoHours;
			}
		}
	}
</script>

<style lang="less" scoped>
	.status-active {
		color: #52c41a;
		font-weight: bold;
	}

	.status-expired {
		color: #ff4d4f;
		font-weight: bold;
	}
</style>