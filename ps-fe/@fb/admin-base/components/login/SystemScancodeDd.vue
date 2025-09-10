<template>
	<!--  钉钉二维码	-->
	<div class="login-scan-code">
		<div class="login-scan-code--img">
			<iframe ref="sonIframe" :src="srcUrl" frameborder="0" style="width: 100%; height: 100%"></iframe>
		</div>
	</div>
</template>

<script>
	/**
	 * SystemCapture
	 * (c) 2020 lincong1987
	 */

	export default {
		name: 'SystemScancode',

		data() {
			return {
				seletDepartment: false,
				srcUrl: ''
			}
		},

		computed: {},

		// 初始化方法
		mounted() {
			this.init();
			window.addEventListener('message', this.scanLogin);
		},
		beforeDestroy() {
			window.removeEventListener('message', this.scanLogin);
		},

		methods: {
			init(param) {
				// this.srcUrl = "https://login.dg-work.cn/oauth2/auth.htm?response_type=code&client_id=gyqyaqzx_dingoa_dingoa&redirect_uri=http://jpx.natapp4.cc/jpx-admin/dingd/scan-login/qr-call-back&scope=get_user_info&authType=QRCODE&embedMode=true";

				return app.service.request({
					url: '/dingd/scan-login/qr',
					method: 'get', // 请求方式 post,get, 默认是 get,
					// `data` 是作为请求主体被发送的数据， post 采用
					params: {},
					headers: {'Content-Type': 'application/x-www-form-urlencoded'},
					// `responseType` 表示服务器响应的数据类型，可以是 'arraybuffer', 'blob', 'document', 'json', 'text', 'stream'
					responseType: 'json', // 默认的
					// `timeout` 指定请求超时的毫秒数(0 表示无超时时间)
					// 如果请求话费了超过 `timeout` 的时间，请求将被中断
					timeout: 5000,
					// 是否显示loading, 默认true,
					loading: false,
				}).then(response => {
						this.srcUrl = response.data;
					},
					err => {
						console.log(err)
					})
			},

			scanLogin(event) {
				return app.service.request({
					url: '/dingd/scan-login/qr-call-back',
					method: 'get', // 请求方式 post,get, 默认是 get,
					// `data` 是作为请求主体被发送的数据， post 采用
					params: {"code": event.data.code},
					headers: {'Content-Type': 'application/x-www-form-urlencoded'},
					// `responseType` 表示服务器响应的数据类型，可以是 'arraybuffer', 'blob', 'document', 'json', 'text', 'stream'
					responseType: 'json', // 默认的
					// `timeout` 指定请求超时的毫秒数(0 表示无超时时间)
					// 如果请求话费了超过 `timeout` 的时间，请求将被中断
					timeout: 5000,
					// 是否显示loading, 默认true,
					loading: false,
				}).then(response => {
						this.$datax.set('token', response.data.token)
						this.$datax.set('user', response.data)
						this.$store.dispatch('admin/setToken', response.data.token)

						// 获取登陆人用户信息
						this.$svc.platform.getUserInfo().then(res => {
							this.$datax.set('userInfo', res.data)
							// 跳转页面
							let that = this;
							if (!this.seletDepartment) {
								that.goToMain();
							}
						});
					},
					err => {
						console.log(err)
					})
			},

			goToMain() {
				// 获取当前登录用户信息
				let userInfo = app.$datax.get('userInfo')

				// 日志埋点
				let data = {
					project: '登陆',
					// 模块编码
					moduleCode: 'login',
					// 模块名称
					moduleName: '扫码登录',
					// 操作类型： login/logout/add/delete/edit/query/pass/unpass, 可以自己定义
					operateType: 'login',
					// 操作人id
					operterId: userInfo.personId,
					// 操作人名称
					operterName: userInfo.personName,
					// 用户类型
					category: userInfo.category,
					// 用户名
					operterUserName: userInfo.userName,
					// 行政区划
					cityCode: userInfo.cityCode,
				}

				// 日志埋点
				this.$logger && this.$logger.send(data);

				// 跳转页面
				this.$router.replace(this.$datax.get('GLOBAL_CONFIG').mainPath)
			},
		},

	}
</script>

<style lang="less" scoped>

	.login-scan-code {
		position: relative;
		width: 290px;
		height: 310px;
		border-radius: 2px;
		border: 1px solid #D3D3D3;
		padding: 8px;


		.login-scan-code--img {
			width: 100%;
			height: 100%;
			background-size: 100% 100%;
		}

		.login-scan-code--mask {
			position: absolute;
			top: 0;
			left: 0;
			width: 240px;
			height: 220px;
			background: rgba(255, 255, 255, 0.7);
			border-radius: 2px;
			text-align: center;
			z-index: 999;

			> span {
				margin-top: 53px;
				margin-bottom: 17px;
				display: block;
				font-size: 14px;
				font-family: MicrosoftYaHei;
				color: #313C47;
				line-height: 22px;
			}
		}

	}

</style>
