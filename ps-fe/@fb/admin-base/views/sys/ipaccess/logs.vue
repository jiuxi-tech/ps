<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-page-search>
				<template slot="query">
					<fb-form ref="query-form" mode="query" :label-width="100">
						<fb-row>
							<fb-col span="8">
								<fb-form-item label="客户端IP">
									<fb-input v-model="queryData.clientIp" placeholder="请输入IP地址" clearable></fb-input>
								</fb-form-item>
							</fb-col>
							<fb-col span="8">
								<fb-form-item label="访问结果">
									<fb-select v-model="queryData.accessResult" placeholder="请选择访问结果" clearable>
										<fb-option value="ALLOWED" label="允许访问"></fb-option>
										<fb-option value="DENIED" label="拒绝访问"></fb-option>
									</fb-select>
								</fb-form-item>
							</fb-col>
							<fb-col span="8">
								<fb-form-item label="用户名">
									<fb-input v-model="queryData.username" placeholder="请输入用户名" clearable></fb-input>
								</fb-form-item>
							</fb-col>
						</fb-row>
						<fb-row>
							<fb-col span="16">
								<fb-form-item label="访问时间">
									<fb-date-picker 
										v-model="queryData.accessTimeRange"
										type="datetimerange"
										format="yyyy-MM-dd HH:mm:ss"
										value-format="yyyy-MM-dd HH:mm:ss"
										start-placeholder="开始时间"
										end-placeholder="结束时间"
										clearable>
									</fb-date-picker>
								</fb-form-item>
							</fb-col>
						</fb-row>
					</fb-form>
				</template>

				<template slot="buttons">
					<fb-button @on-click="handleExport" icon="download">导出日志</fb-button>
					<fb-button @on-click="handleCleanLogs" icon="delete" type="danger">清理日志</fb-button>
				</template>

				<template slot="actions">
					<fb-button type="primary" icon="search" @on-click="handleQuery">查询</fb-button>
					<fb-button @on-click="handleReset">重置</fb-button>
				</template>

				<template slot="table">
					<fb-simple-table 
						ref="table" 
						:service="table.service.search" 
						:param="queryData" 
						:pk="table.primaryKey"
						:columns="table.columns" 
						:multiple="false" 
						:formatters="table.formatters"
						:scroll="{ x: 1400, y: 300, autoHeight: true }" 
						auto-load>

						<template v-slot:accessResult="props">
							<fb-tag :color="props.row.accessResult === 'ALLOWED' ? 'green' : 'red'">
								{{ props.row.accessResult === 'ALLOWED' ? '允许访问' : '拒绝访问' }}
							</fb-tag>
						</template>

						<template v-slot:ruleType="props">
							<span v-if="props.row.ruleType">{{ getRuleTypeText(props.row.ruleType) }}</span>
							<span v-else>-</span>
						</template>

						<template v-slot:denyReason="props">
							<span v-if="props.row.denyReason">{{ getDenyReasonText(props.row.denyReason) }}</span>
							<span v-else>-</span>
						</template>

						<template v-slot:actions="props">
							<fb-button @on-click="handleViewDetail(props.row)" size="s">详情</fb-button>
						</template>
					</fb-simple-table>
				</template>
			</fb-page-search>
		</div>
		<div class="tp-dialog-bottom">
			<fb-flex gap="8px" jc-end>
				<fb-button @on-click="handleCancel">关闭</fb-button>
			</fb-flex>
		</div>

		<!-- 详情对话框 -->
		<tp-dialog ref="detailDialog"></tp-dialog>
		<!-- 清理日志对话框 -->
		<tp-dialog ref="cleanDialog"></tp-dialog>
	</div>
</template>

<script>
export default {
	name: 'IpAccessLogs',

	props: {
		param: {
			type: Object,
			require: false
		},
		parentPage: {
			type: Object,
			default: null
		}
	},

	data() {
		return {
			queryData: {
				clientIp: '',
				accessResult: '',
				username: '',
				accessTimeRange: []
			},
			table: {
				primaryKey: 'logId',
				service: {
					search: this.$svc.sys.ipaccess.getAccessLogs
				},
				columns: [
					{
						label: '客户端IP',
						name: 'clientIp',
						width: 120
					},
					{
						label: '访问时间',
						name: 'accessTime',
						width: 160
					},
					{
						label: '访问结果',
						name: 'accessResult',
						slot: 'accessResult',
						width: 100
					},
					{
						label: '拒绝原因',
						name: 'denyReason',
						slot: 'denyReason',
						width: 120
					},
					{
						label: '请求URI',
						name: 'requestUri',
						width: 200,
						ellipsis: true
					},
					{
						label: '请求方法',
						name: 'requestMethod',
						width: 80
					},
					{
						label: '用户名',
						name: 'username',
						width: 100
					},
					{
						label: '匹配规则',
						name: 'matchedRule',
						width: 150,
						ellipsis: true
					},
					{
						label: '规则类型',
						name: 'ruleType',
						slot: 'ruleType',
						width: 100
					},
					{
						label: '操作',
						name: 'actions',
						slot: 'actions',
						width: 80,
						freeze: 'right'
					}
				],
				formatters: {}
			}
		}
	},

	methods: {
		// 查询
		handleQuery() {
			// 处理时间范围
			if (this.queryData.accessTimeRange && this.queryData.accessTimeRange.length === 2) {
				this.queryData.startTime = this.queryData.accessTimeRange[0]
				this.queryData.endTime = this.queryData.accessTimeRange[1]
			} else {
				this.queryData.startTime = ''
				this.queryData.endTime = ''
			}
			this.$refs.table.doSearch()
		},

		// 重置
		handleReset() {
			this.queryData = {
				clientIp: '',
				accessResult: '',
				username: '',
				accessTimeRange: []
			}
			this.handleQuery()
		},

		// 查看详情
		handleViewDetail(row) {
			let param = { logData: row }
			let options = { width: 800, height: 500 }
			this.$refs.detailDialog.show(import('./log-detail.vue'), param, '访问日志详情', options)
		},

		// 导出日志
		handleExport() {
			this.$message.info('导出功能开发中...')
		},

		// 清理日志
		handleCleanLogs() {
			let param = {}
			let options = { width: 500, height: 300 }
			this.$refs.cleanDialog.show(import('./clean-logs.vue'), param, '清理访问日志', options)
		},

		// 关闭
		handleCancel() {
			this.$parent.close()
		},

		// 获取规则类型文本
		getRuleTypeText(ruleType) {
			const typeMap = {
				'WHITELIST': '白名单',
				'BLACKLIST': '黑名单',
				'EXACT': '精确匹配',
				'WILDCARD': '通配符',
				'CIDR': 'CIDR网段',
				'RANGE': 'IP范围'
			}
			return typeMap[ruleType] || ruleType
		},

		// 获取拒绝原因文本
		getDenyReasonText(denyReason) {
			const reasonMap = {
				'IP_IN_BLACKLIST': 'IP在黑名单中',
				'IP_NOT_IN_WHITELIST': 'IP不在白名单中',
				'INVALID_IP': '无效IP地址',
				'SYSTEM_ERROR': '系统错误'
			}
			return reasonMap[denyReason] || denyReason
		}
	}
}
</script>

<style scoped>
.tp-dialog {
	display: flex;
	flex-direction: column;
	height: 100%;
}

.tp-dialog-top {
	flex: 1;
	padding: 20px;
	overflow-y: auto;
}

.tp-dialog-bottom {
	padding: 15px 20px;
	border-top: 1px solid #e8eaec;
	text-align: right;
	background: #fafafa;
}
</style>