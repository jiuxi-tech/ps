<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-card>
				<div class="log-detail">
					<!-- 基本信息 -->
					<div class="detail-section">
						<h4 class="section-title">基本信息</h4>
						<fb-row>
							<fb-col span="12">
								<div class="detail-item">
									<label>日志ID：</label>
									<span>{{ logData.logId }}</span>
								</div>
							</fb-col>
							<fb-col span="12">
								<div class="detail-item">
									<label>客户端IP：</label>
									<span class="ip-address">{{ logData.clientIp }}</span>
								</div>
							</fb-col>
						</fb-row>
						<fb-row>
							<fb-col span="12">
								<div class="detail-item">
									<label>访问时间：</label>
									<span>{{ logData.accessTime }}</span>
								</div>
							</fb-col>
							<fb-col span="12">
								<div class="detail-item">
									<label>访问结果：</label>
									<fb-tag :color="logData.accessResult === 'ALLOWED' ? 'green' : 'red'">
										{{ logData.accessResult === 'ALLOWED' ? '允许访问' : '拒绝访问' }}
									</fb-tag>
								</div>
							</fb-col>
						</fb-row>
						<fb-row v-if="logData.username">
							<fb-col span="12">
								<div class="detail-item">
									<label>用户名：</label>
									<span>{{ logData.username }}</span>
								</div>
							</fb-col>
							<fb-col span="12" v-if="logData.cityCode">
								<div class="detail-item">
									<label>城市代码：</label>
									<span>{{ logData.cityCode }}</span>
								</div>
							</fb-col>
						</fb-row>
					</div>

					<!-- 请求信息 -->
					<div class="detail-section">
						<h4 class="section-title">请求信息</h4>
						<fb-row>
							<fb-col span="12">
								<div class="detail-item">
									<label>请求URI：</label>
									<span class="uri">{{ logData.requestUri || '-' }}</span>
								</div>
							</fb-col>
							<fb-col span="12">
								<div class="detail-item">
									<label>请求方法：</label>
									<fb-tag>{{ logData.requestMethod || '-' }}</fb-tag>
								</div>
							</fb-col>
						</fb-row>
						<fb-row v-if="logData.userAgent">
							<fb-col span="24">
								<div class="detail-item">
									<label>用户代理：</label>
									<div class="user-agent">{{ logData.userAgent }}</div>
								</div>
							</fb-col>
						</fb-row>
					</div>

					<!-- 规则匹配信息 -->
					<div class="detail-section" v-if="logData.matchedRule || logData.denyReason">
						<h4 class="section-title">规则匹配信息</h4>
						<fb-row v-if="logData.matchedRule">
							<fb-col span="12">
								<div class="detail-item">
									<label>匹配规则：</label>
									<span class="matched-rule">{{ logData.matchedRule }}</span>
								</div>
							</fb-col>
							<fb-col span="12" v-if="logData.ruleType">
								<div class="detail-item">
									<label>规则类型：</label>
									<fb-tag :color="getRuleTypeColor(logData.ruleType)">
										{{ getRuleTypeText(logData.ruleType) }}
									</fb-tag>
								</div>
							</fb-col>
						</fb-row>
						<fb-row v-if="logData.denyReason">
							<fb-col span="24">
								<div class="detail-item">
									<label>拒绝原因：</label>
									<fb-alert type="error" :closable="false">
										{{ getDenyReasonText(logData.denyReason) }}
									</fb-alert>
								</div>
							</fb-col>
						</fb-row>
					</div>

					<!-- 系统信息 -->
					<div class="detail-section">
						<h4 class="section-title">系统信息</h4>
						<fb-row>
							<fb-col span="12">
								<div class="detail-item">
									<label>创建时间：</label>
									<span>{{ logData.createTime }}</span>
								</div>
							</fb-col>
							<fb-col span="12">
								<div class="detail-item">
									<label>状态：</label>
									<fb-tag :color="logData.actived === 1 ? 'green' : 'gray'">
										{{ logData.actived === 1 ? '有效' : '无效' }}
									</fb-tag>
								</div>
							</fb-col>
						</fb-row>
					</div>
				</div>
			</fb-card>
		</div>
		<div class="tp-dialog-bottom">
			<fb-flex gap="8px" jc-end>
				<fb-button @on-click="handleCancel">关闭</fb-button>
			</fb-flex>
		</div>
	</div>
</template>

<script>
export default {
	name: 'LogDetail',

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
			logData: {}
		}
	},

	mounted() {
		if (this.param && this.param.logData) {
			this.logData = this.param.logData
		}
	},

	methods: {
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

		// 获取规则类型颜色
		getRuleTypeColor(ruleType) {
			const colorMap = {
				'WHITELIST': 'green',
				'BLACKLIST': 'red',
				'EXACT': 'blue',
				'WILDCARD': 'orange',
				'CIDR': 'purple',
				'RANGE': 'cyan'
			}
			return colorMap[ruleType] || 'default'
		},

		// 获取拒绝原因文本
		getDenyReasonText(denyReason) {
			const reasonMap = {
				'IP_IN_BLACKLIST': 'IP地址在黑名单中',
				'IP_NOT_IN_WHITELIST': 'IP地址不在白名单中',
				'INVALID_IP': '无效的IP地址',
				'SYSTEM_ERROR': '系统内部错误'
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

.log-detail {
	padding: 10px;
}

.detail-section {
	margin-bottom: 24px;
}

.detail-section:last-child {
	margin-bottom: 0;
}

.section-title {
	margin: 0 0 16px 0;
	padding-bottom: 8px;
	border-bottom: 1px solid #e8eaec;
	font-size: 14px;
	font-weight: 600;
	color: #333;
}

.detail-item {
	margin-bottom: 12px;
	display: flex;
	align-items: flex-start;
	gap: 8px;
}

.detail-item:last-child {
	margin-bottom: 0;
}

.detail-item label {
	font-weight: 500;
	color: #666;
	min-width: 80px;
	flex-shrink: 0;
}

.detail-item span {
	color: #333;
	flex: 1;
}

.ip-address {
	font-family: 'Courier New', monospace;
	background: #f5f5f5;
	padding: 2px 6px;
	border-radius: 3px;
}

.uri {
	font-family: 'Courier New', monospace;
	word-break: break-all;
}

.matched-rule {
	font-family: 'Courier New', monospace;
	background: #f0f9ff;
	padding: 2px 6px;
	border-radius: 3px;
	border: 1px solid #e1f5fe;
}

.user-agent {
	font-size: 12px;
	color: #666;
	word-break: break-all;
	line-height: 1.4;
	margin-top: 4px;
}
</style>