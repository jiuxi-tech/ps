<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-property bordered label-width="140px" mode="form">
				<fb-row>
	
					<fb-col span="12">
						<fb-property-item label="事件时间">
							{{ formatTime(row.time) }}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="操作用户">
							{{ row.authDetails?.userId || '-' }}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="操作类型">
							<fb-tag :color="getOperationTypeColor(row.operationType)">{{ row.operationType || '-' }}</fb-tag>
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="资源类型">
							{{ row.resourceType || '-' }}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="资源路径">
							{{ row.resourcePath || '-' }}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="IP地址">
							{{ row.authDetails?.ipAddress || '-' }}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="Realm">
							{{ row.authDetails?.realmId || '-' }}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="应用ID">
							{{ row.authDetails?.clientId || '-' }}
						</fb-property-item>
					</fb-col>
				</fb-row>
			</fb-property>

			<div v-if="row.error" style="margin-top: 16px;">
				<fb-alert type="error" :message="row.error" show-icon />
			</div>

			<div v-if="row.representation" style="margin : 16px ;">
				<fb-card>
					<template #title>详细信息</template>
					<div class="detail-content">
						<div v-for="(value, key) in row.representation" :key="key" class="detail-item">
							<span class="detail-key">{{ key }}:</span>
							<span class="detail-value">{{ formatValue(value) }}</span>
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
					<pre class="raw-data">{{ JSON.stringify(row, null, 2) }}</pre>
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
	name: 'AdminLogView',
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
			row: this.param.data || {},
			showRawData: false
		}
	},
	methods: {
		formatTime(timestamp) {
			if (!timestamp) return '-'
			return new Date(timestamp).toLocaleString('zh-CN')
		},
		getOperationTypeColor(type) {
			const colorMap = {
				'CREATE': 'success',
				'UPDATE': 'warning', 
				'DELETE': 'error',
				'ACTION': 'info'
			}
			return colorMap[type] || 'default'
		},
		formatValue(value) {
			if (typeof value === 'object') {
				return JSON.stringify(value)
			}
			return value
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
.admin-log-view {
	padding: 20px;

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