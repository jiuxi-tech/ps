<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">

			<fb-property bordered label-width="140px"  >
						<fb-property-item label="应用名称" span="2">
							{{formData.appName}}
						</fb-property-item>
						<fb-property-item label="API Key" span="2">
                <span style="font-family:'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;">{{formData.apiKey}}</span>
                <fb-button :copy="formData.apiKey" icon="copy" size="s" style="margin-left: 12px;">复制</fb-button>
            </fb-property-item>
						<fb-property-item label="状态">
							<span :class="formData.status === 1 ? 'status-valid' : 'status-invalid'">
								{{ formData.status === 1 ? '启用' : '禁用' }}
							</span>
						</fb-property-item>
						<fb-property-item label="过期时间">
							{{ formatExpireTime(formData.expireTime) }}
						</fb-property-item>
						<fb-property-item label="应用描述" span="2">
							{{formData.description || '-'}}
						</fb-property-item>
						<fb-property-item label="创建人">
							{{formData.createPersonName}}
						</fb-property-item>
						<fb-property-item label="创建时间">
							{{ formatTime(formData.createTime) }}
						</fb-property-item>
						<fb-property-item label="修改人">
							{{formData.updatePersonName}}
						</fb-property-item>
						<fb-property-item label="修改时间">
							{{ formatTime(formData.updateTime) }}
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
				service: this.$svc.sys.thirdPartyApp,
				// 表单数据
				formData: {
					appId: '',
					appName: '',
					apiKey: '',
					status: 1,
					expireTime: '',
					appDesc: '',
					createTime: '',
					createPersonName: '',
					updateTime: '',
					updatePersonName: '',
				},
			}
		},

		// 方法
		methods: {
			// 设置标题
			init(param) {
				if (param && param.id) {
					let appId = param.id;
					this.formData.appId = appId;
					this.view(appId, param.passKey);
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

			// 格式化过期时间
			formatExpireTime(val) {
				if (!val || val === '' || val === null) {
					return '永不过期';
				}
				try {
					return dayjs(val, 'YYYYMMDDHHmmss').format('YYYY-MM-DD HH:mm:ss');
				} catch (e) {
					return '永不过期';
				}
			},

			view(appId, passKey) {
				// 调用查看service方法
				this.service.view({"appId": appId, "passKey": passKey}).then((result) => {
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
	.status-valid {
		color: #52c41a;
		font-weight: bold;
	}

	.status-invalid {
		color: #ff4d4f;
		font-weight: bold;
	}
</style>
