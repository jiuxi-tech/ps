<template>
	<div class="login-wrapper">

		<div class="login-logo">新一代前端开发方案JPX3.0</div>


		<div class="login-dialog">

			<div class="login-form">

				<div class="login-form__caption">
					欢迎登陆
				</div>


				<fb-form ref="loginForm" :caption="loginForm" autocomplete="off">

					<fb-form-item prop="用户名"
					              :rule="[{required: true, whitespace: true}, {type: 'username'}]">
						<fb-input v-model="loginForm.username"
						          size="l"
						          prepend-icon="user"
						          clearable
						          placeholder="用户名"
						          autocomplete="off"
						/>
					</fb-form-item>

					<fb-form-item prop="密码"
					              :rule="[{required: true }]">
						<fb-input v-model="loginForm.password"
						          size="l"
						          type="password"
						          prepend-icon="password"
						          clearable
						          placeholder="密码"
						          autocomplete="off"
						>
						</fb-input>
					</fb-form-item>

					<fb-form-item prop="loginForm.capture"
					              :rule="[]"
					              style="padding-bottom: 40px">
						<fb-input v-model="loginForm.capture"
						          size="l"
						          type="capture"
						          prepend-icon="qrcode"
						          clearable
						          placeholder="验证码"
						          autocomplete="off"
						          style="width: 214px;"
						>
						</fb-input>

						<system-capture :api="$datax.capturePath"/>

					</fb-form-item>

					<fb-form-item>

						<fb-button
							type="primary"
							size="l"
							long
							:loading="inLoginProcess"
							@on-click="doLogin">
							登 录
						</fb-button>

					</fb-form-item>

				</fb-form>

				<div>
					<fb-button type="link">忘记密码？</fb-button>
				</div>

			</div>


		</div>

		<div class="login-footer">
			<p>建设单位：</p>
			<p>技术支持：</p>
		</div>

	</div>
</template>

<script>
	/**
	 * DefaultApplicationLogin
	 * (c) 2020 lincong1987
	 */

	import SystemCapture from './components/SystemCapture'

	export default {

		components: {
			SystemCapture,
		},

		data () {
			return {

				loginForm: {
					username: 'jpx',
					password: 'af5e8eb2b3fb95b494828cc54423607f',
					capture: '',
				},

				inLoginProcess: false

			}
		},

		mounted () {

			this.$datax.set('capturePath', 'http://admin.dlszywz.cn/include/captcha/captcha.php?')

			// this.$systemService.get('/test', {
			// 	a: 'aaa',
			// 	name: 'asd',
			// }).then(res => res.data).then(json => {
			// 	console.log(json)
			// }).catch(e => {
			// 	console.log(e)
			// })

			this.$nextTick(() => {

			})

		},

		methods: {
			doLogin () {

				this.inLoginProcess = true

				// 校验
				this.$refs.loginForm.validate((result, errors) => {

					console.log('登陆表单校验', result, errors)

					if (result === true) {

						this.$svc.platform.login(this.loginForm.username, this.loginForm.password).then(json => {
							this.$datax.set("token", json.data.token)
							this.$store.dispatch("admin/setToken", json.data.token)

							this.$svc.sys.menu.getMenus().then(res => {
								this.$store.commit('menu/load', res.data)
							})

							this.$router.replace(this.$datax.get("GLOBAL_CONFIG").mainPath)
						})

					}

					this.inLoginProcess = false
				})

			},
		},

	}
</script>

<style lang="less" scoped>

	.login-wrapper {
		position: absolute;
		top:      0;
		bottom:   0;
		left:     0;
		right:    0;
		width:    100%;
		height:   100%;
		overflow: hidden;
		z-index:  1;

		&::after {
			content:             "";
			position:            absolute;
			height:              100%;
			width:               100%;
			background-color:    #5DBBFE;
			background-image:    url("http://115.236.87.43:35133/planb/resource/fb-desktop/img/login/psb-8.jpg");
			background-size:     cover;
			background-position: 50% 50%;
		}

	}

	.login-logo {
		position:    absolute;
		width:       500px;
		height:      44px;
		left:        50%;
		top:         16%;
		margin-top:  -(44px * 0.8);
		margin-left: -(500px/2);
		font-size:   36px;
		color:       #ffffff;
		text-align:  center;
		font-weight: bold;
		z-index:     5;
	}

	.login-dialog {
		position:      absolute;
		width:         360px;
		height:        422px;
		top:           38.2%;
		left:          50%;
		margin-top:    -(422px * 0.382);
		margin-left:   -(360px/2);
		background:    #FFFFFF;
		box-shadow:    0px 2px 8px 0px rgba(0, 0, 0, 0.15);
		border-radius: 4px;
		z-index:       100;

		.login-form {

			padding: 0 24px;

			&__caption {
				height:      32px;
				font-size:   24px;
				color:       #313C47;
				line-height: 32px;
				padding:     32px 0;
				text-align:  center;
			}


		}

	}


	@media screen and (max-height: 720px) {

		.login-dialog {
			top:        50%;
			margin-top: -(422px * 0.5);
		}

		.login-logo {
			top:        10%;
			margin-top: -(44px * 0.8);
		}
	}


	.fb-form-item {
		padding-bottom: 24px;
	}

	.login-footer {
		position:    absolute;
		width:       360px;
		height:      44px;
		bottom:      24px;
		left:        50%;
		margin-left: -(360px/2);
		z-index:     10;

		p {
			text-align: center;
			color:      #fff;
		}
	}

</style>
