<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-property bordered label-width="140px" mode="form">
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="事件ID">
							{{ data.id || '-' }}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="事件时间">
							{{ formatTime(data.time) }}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="用户名">
							{{ data.userId || '-' }}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="事件类型">
							<fb-tag :type="getEventTypeColor(data.type)">{{ data.type || '-' }}</fb-tag>
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="应用ID">
							{{ data.clientId || '-' }}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="IP地址">
							{{ data.ipAddress || '-' }}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="会话ID">
							{{ data.sessionId || '-' }}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="用户代理">
							{{ data.userAgent || '-' }}
						</fb-property-item>
					</fb-col>
				</fb-row>
			</fb-property>

			<div v-if="data.error" style="margin-top: 16px;">
				<fb-alert type="error" :message="data.error" show-icon />
			</div>

			<div v-if="data.details" style="margin-top: 16px;">
				<fb-card>
					<template #title>事件详情</template>
					<div class="detail-content">
						<div v-for="(value, key) in parseDetails(data.details)" :key="key" class="detail-item">
							<span class="detail-key">{{ key }}:</span>
							<span class="detail-value">{{ value }}</span>
						</div>
					</div>
				</fb-card>
			</div>

			<div v-if="showRawData" style="margin-top: 16px;">
				<fb-card>
					<template #title>
						原始JSON数据
						<fb-button size="small" @on-click="toggleRawData" style="margin-left: 10px;">
							{{ showRawData ? '隐藏' : '显示' }}
						</fb-button>
					</template>
					<pre class="raw-data">{{ JSON.stringify(data, null, 2) }}</pre>
				</fb-card>
			</div>

			<div style="margin-top: 16px;">
				<fb-button @on-click="toggleRawData">
					{{ showRawData ? '隐藏原始数据' : '显示原始数据' }}
				</fb-button>
			</div>
		</div>

		<div class="tp-dialog-bottom">
			<fb-button @on-click="handleClose">关闭</fb-button>
		</div>
	</div>
</template>

<script>
export default {
	name: 'UserLogView',
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
			data: this.param.data || {},
			showRawData: false
		}
	},

	methods: {
		// 格式化时间
		formatTime(timestamp) {
			if (!timestamp) return '-'
			return app.$dayjs(timestamp).format('YYYY-MM-DD HH:mm:ss')
		},

		// 获取事件类型颜色
		getEventTypeColor(type) {
			const colorMap = {
				'LOGIN': 'success',
				'LOGOUT': 'info',
				'REGISTER': 'primary',
				'UPDATE_PROFILE': 'warning',
				'UPDATE_PASSWORD': 'warning',
				'LOGIN_ERROR': 'danger',
				'CODE_TO_TOKEN': 'success',
				'REFRESH_TOKEN': 'info'
			}
			return colorMap[type] || 'default'
		},

		// 解析详细信息
		parseDetails(details) {
			if (!details) return {}
			try {
				if (typeof details === 'string') {
					return JSON.parse(details)
				}
				return details
			} catch (error) {
				console.error('解析详细信息失败:', error)
				return { raw: details }
			}
		},

		// 切换原始数据显示
		toggleRawData() {
			this.showRawData = !this.showRawData
		},

		// 关闭对话框
		handleClose() {
			// 关闭，并传递参数
			this.closeTpDialog("xxxx");
		}
	}
}
</script>

<style lang="less" scoped>
.user-log-view {
	padding: 20px;

	.fb-form {
		.fb-form-item {
			margin-bottom: 16px;

			span {
				color: #333;
				font-size: 14px;
			}
		}
	}

	.detail-content {
		.detail-item {
			display: flex;
			margin-bottom: 8px;
			padding: 4px 0;
			border-bottom: 1px solid #f0f0f0;

			&:last-child {
				border-bottom: none;
			}

			.detail-key {
				font-weight: bold;
				color: #666;
				min-width: 120px;
				flex-shrink: 0;
			}

			.detail-value {
				color: #333;
				flex: 1;
				word-break: break-all;
			}
		}
	}

	.raw-data {
		background: #f5f5f5;
		padding: 16px;
		border-radius: 4px;
		max-height: 400px;
		overflow: auto;
		font-size: 12px;
		line-height: 1.5;
		color: #333;
		white-space: pre-wrap;
		word-break: break-all;
	}
}
</style>