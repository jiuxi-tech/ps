<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-alert type="info" show-icon style="margin-bottom: 16px;">
				<template slot="message">
					以下API Key用于调用接口时的身份认证,请妥善保管。
				</template>
			</fb-alert>

			<fb-property bordered label-width="120px" mode="form">
				<fb-row>
					<fb-col span="24">
						<fb-property-item label="应用名称">
							{{formData.appName}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-property-item label="API Key">
							<div style="display: flex; align-items: center;">
								<span style="font-family:'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; font-size: 14px; flex: 1; word-break: break-all;">{{formData.apiKey}}</span>
								<fb-button @on-click="copyApiKey" size="s" style="margin-left: 12px;">复制</fb-button>
							</div>
						</fb-property-item>
					</fb-col>
				</fb-row>
			</fb-property>
		</div>

		<div class="tp-dialog-bottom">
			<fb-button @on-click="handleClose">关闭</fb-button>
		</div>
	</div>
</template>

<script>
	export default {
		name: 'secret',
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
				},
			}
		},

		// 方法
		methods: {
			// 初始化
			init(param) {
				if (param && param.id) {
					let appId = param.id;
					this.formData.appId = appId;
					this.view(appId, param.passKey);
				}
			},

			// 取消
			handleClose() {
				// 关闭弹窗
				this.closeTpDialog()
			},

			// 复制API Key
			copyApiKey() {
				const input = document.createElement('textarea');
				input.value = this.formData.apiKey;
				document.body.appendChild(input);
				input.select();
				document.execCommand('copy');
				document.body.removeChild(input);
				this.$message.success('API Key已复制到剪贴板');
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

</style>
