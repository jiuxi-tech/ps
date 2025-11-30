<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">

			<fb-property bordered label-width="140px">
				<fb-property-item label="应用名称" span="2">
					{{formData.appName || '-'}}
				</fb-property-item>
				<fb-property-item label="API路径" span="2">
					{{formData.apiPath || '-'}}
				</fb-property-item>
				<fb-property-item label="HTTP方法">
					{{formData.httpMethod || '-'}}
				</fb-property-item>
				<fb-property-item label="请求IP">
					{{formData.requestIp || '-'}}
				</fb-property-item>
				<fb-property-item label="响应状态">
					<span :class="getStatusClass(formData.responseStatus)">
						{{ formData.responseStatus || '-' }}
					</span>
				</fb-property-item>
				<fb-property-item label="响应时间">
					<span :class="getTimeClass(formData.responseTime)">
						{{ formatResponseTime(formData.responseTime) }}
					</span>
				</fb-property-item>
				<fb-property-item label="调用时间" span="2">
					{{ formatTime(formData.callTime) }}
				</fb-property-item>
				<fb-property-item label="请求参数" span="2">
					<div style="word-break: break-all; white-space: pre-wrap;">{{formData.requestParams || '-'}}</div>
				</fb-property-item>
				<fb-property-item label="错误信息" span="2">
					<div style="word-break: break-all; white-space: pre-wrap; color: #ff4d4f;">{{formData.errorMessage || '-'}}</div>
				</fb-property-item>
			</fb-property>
		</div>

		<div class="tp-dialog-bottom">
			<fb-button @on-click="handleClose">关闭</fb-button>
		</div>
	</div>
</template>

<script>
	import dayjs from "dayjs";

	export default {
		name: 'view',
		mixins: [],
		// 接收父组件的传参
		props: {
			param: {
				type: Object,
				require: false
			},
			parentPage: {
				type: Object,
				default: null
			},
			meta: {
				type: Object,
				default: () => ({})
			}
		},
		// 创建方法
		created() {},
		// 初始化方法
		mounted() {
			// 执行界面初始化
			this.init(this.param);
		},
		data() {
			return {
				// 请求的 service
				service: this.$svc.sys.apiCallLog,
				// 表单数据
				formData: {
					logId: '',
					appId: '',
					appName: '',
					apiId: '',
					apiPath: '',
					httpMethod: '',
					requestIp: '',
					requestParams: '',
					responseStatus: null,
					responseTime: null,
					errorMessage: '',
					callTime: '',
					tenantId: '',
				},
			}
		},

		// 方法
		methods: {
			// 设置标题
			init(param) {
				if (param && param.id) {
					let logId = param.id;
					this.formData.logId = logId;
					this.view(logId, param.passKey);
				}
			},

			// 取消
			handleClose() {
				// 关闭弹窗，不传递参数（表示未成功保存）
				this.closeTpDialog()
			},

			// 格式化时间
			formatTime(val) {
				if (!val || val === '' || val === null) {
					return '-';
				}
				try {
					return dayjs(val, 'YYYYMMDDHHmmss').format('YYYY-MM-DD HH:mm:ss');
				} catch (e) {
					return '-';
				}
			},

			// 格式化响应时间
			formatResponseTime(val) {
				if (!val || val === null) {
					return '-';
				}
				return val + ' 毫秒';
			},

			// 获取响应状态样式类
			getStatusClass(status) {
				if (!status) return '';
				if (status >= 200 && status < 300) {
					return 'status-success';
				} else if (status >= 400 && status < 500) {
					return 'status-client-error';
				} else if (status >= 500) {
					return 'status-server-error';
				}
				return '';
			},

			// 获取响应时间样式类
			getTimeClass(time) {
				if (!time) return 'time-normal';
				if (time < 500) {
					return 'time-normal';
				} else if (time < 2000) {
					return 'time-warning';
				} else {
					return 'time-danger';
				}
			},

			view(logId, passKey) {
				// 调用查看service方法
				this.service.view({"logId": logId, "passKey": passKey}).then((result) => {
					// 判断code
					if (result.code == 1) {
						this.formData = result.data;
					} else {
						// 服务器返回失败
						this.$message.error('查询失败: ' + result.message)
					}
				}).catch((err) => {
					// 服务器返回失败
					console.log(err);
				})
			},
		}
	}
</script>

<style lang="less" scoped>
	.status-success {
		color: #52c41a;
		font-weight: bold;
	}

	.status-client-error {
		color: #faad14;
		font-weight: bold;
	}

	.status-server-error {
		color: #ff4d4f;
		font-weight: bold;
	}

	.time-normal {
		color: inherit;
	}

	.time-warning {
		color: #faad14;
	}

	.time-danger {
		color: #ff4d4f;
		font-weight: bold;
	}
</style>
