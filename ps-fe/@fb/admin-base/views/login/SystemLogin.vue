<template>
	<div class="login-wrapper">

		<div class="login-logo-icon"></div>

		<div class="login-logo">JPX PC视觉规范V3.0</div>

		<div class="login-content">
			<div class="content">
				<!--				<login-card-scan-code-verify-->
				<!--					v-show="!seletDepartment && !cardCode"-->
				<!--					ref="loginCard"-->
				<!--					:inLoginProcess="inLoginProcess"-->
				<!--					@doLogin="doLogin"-->
				<!--					@toRegister="toRegister"-->
				<!--					@toForget="toForget"-->
				<!--					@toAppDownload="toAppDownload"/>-->

				<!--								<login-card-phone-verify-->
				<!--									v-show="!seletDepartment && !cardCode"-->
				<!--									ref="loginCard"-->
				<!--									:inLoginProcess="inLoginProcess"-->
				<!--									@doLogin="doLogin"-->
				<!--									@toRegister="toRegister"-->
				<!--									@toForget="toForget"-->
				<!--									@toAppDownload="toAppDownload"/>-->

				<login-card-code-verify
					v-show="!seletDepartment && !cardCode"
					ref="loginCard"
					:inLoginProcess="inLoginProcess"
					@doLogin="doLogin"
					@toRegister="toRegister"
					@toForget="toForget"
					@toAppDownload="toAppDownload"/>

				<login-card-department-list
					v-show="seletDepartment"
					:fieldObj="{label: 'deptFullName', id: 'deptId'}"
					:departmentList="departmentList"
					@confirmDepartment="confirmDepartment"/>

				<div v-show="cardCode == 1" class="login-register">
					<register-card-step
						ref="registerCard"
						@toLogin="toLogin"
						@doRegister="doRegister"
					></register-card-step>
				</div>

				<div v-show="cardCode == 2" class="login-forget">
					<forget-card
						ref="forgetCard"
						@toLogin="toLogin"
					></forget-card>
				</div>

				<div v-show="cardCode == 3" class="login-forget">
					<appdown-card @toLogin="toLogin"></appdown-card>
				</div>

			</div>
		</div>

		<div class="login-footer">
			<p>建设单位：浙江省应急管理厅</p>
			<p>技术支持：JPX</p>
		</div>

	</div>
</template>

<script>
	/**
	 * DefaultApplicationLogin
	 * (c) 2020 lincong1987
	 */

	import loginCardCodeVerify from '../../components/login/LoginCardCodeVerify'
	import loginCardDragVerify from '../../components/login/LoginCardDragVerify'
	import loginCardPhoneVerify from '../../components/login/LoginCardPhoneVerify'
	import loginCardScanCodeVerify from '../../components/login/LoginCardScanCodeVerify'
	import LoginCardDepartmentList from '../../components/login/LoginCardDepartmentList'
	import registerCardStep from '../../components/login/RegisterCardStep'
	import forgetCard from '../../components/login/ForgetCard'
	import appdownCard from '../../components/login/AppdownCard'

	export default {

		routerConfig: {
			path: '/login2',
			name: 'login2',
			meta: {
				title: '登录',
				rank: '1'
			},
		},
		components: {
			LoginCardDepartmentList,
			loginCardDragVerify,
			loginCardCodeVerify,
			loginCardPhoneVerify,
			loginCardScanCodeVerify,
			registerCardStep,
			forgetCard,
			appdownCard,
		},

		data () {
			let logoTitle = app.projectConfig.logoTitle
			return {
				logoTitle,
				// 0 = 登录  1 = 注册  2 = 忘记密码
				cardCode: 0,
				inLoginProcess: false, // 登录 loading
				seletDepartment: false,
				departmentList: [],
			}
		},

		mounted () {

		},

		methods: {
			// 子 =》 父 处理登录
			doLogin (loginForm) {
				console.log(loginForm)
				// 表单验证在子组件中处理，登录只需要获取子组件数据值，进行异步请求处理
				this.inLoginProcess = true
				this.departmentList = []
				this.$svc.platform.login(loginForm.userName, loginForm.userpwd).then(json => {
					// 登录按钮 loading
					this.inLoginProcess = false
					if (json.data && json.data.deptVOList.length > 1) {

						app.trigger("on-login-success", json)

						this.$datax.set('token', json.data.token)
						// 在多个部门下
						this.departmentList = json.data.deptVOList
						// 切换部门，选择部门登陆
						this.seletDepartment = true
					} else {
						this.$datax.set('token', json.data.token)
						this.$datax.set('user', json.data)
						this.$store.dispatch('admin/setToken', json.data.token)
						if (!this.seletDepartment) {
							this.$router.replace(this.$datax.get('GLOBAL_CONFIG').mainPath)
						}
					}
				})
			},
			confirmDepartment (val) {
				if (!val) {
					this.$message.warn('请点选部门登录')
				}
				console.log(val)
				this.$svc.platform.selectDeptLogin({'deptId': val}).then(res => {
					this.$datax.set('token', res.data.token)
					this.$datax.set('user', res.data)
					this.$store.dispatch('admin/setToken', res.data.token)
					this.$router.replace(this.$datax.get('GLOBAL_CONFIG').mainPath)
					// 切换部门标识恢复
					this.seletDepartment = false
				})

			},

			toLogin () {
				this.cardCode = 0
				this.$message.success('前往登录！')
			},
			toRegister () {
				this.cardCode = 1
				this.$message.success('前往注册！')
			},
			toForget () {
				this.cardCode = 2
				this.$message.success('前往重置密码！')
			},
			toAppDownload () {
				this.cardCode = 3
				this.$message.success('请打开手机，扫描二维码')
			},
			// 注册逻辑
			doRegister (registerForm) {
				console.log(registerForm)
				// 异步请求后步骤加 1
				this.$message.success('注册下一步，前往登录')
				this.$refs['registerCard'].process += 1
			},
		},

	}
</script>

<style lang="less" scoped>

	.login-wrapper {
		position: absolute;
		top: 0;
		bottom: 0;
		left: 0;
		right: 0;
		width: 100%;
		height: 100%;
		background: linear-gradient(180deg, #65BDFF 0%, #025EFE 100%);
		overflow: hidden;
		z-index: 1;

		&::after {
			content: "";
			position: absolute;
			height: 100%;
			width: 100%;
			background-image: url("../../assets/img/login/bg.png");
			background-size: cover;
			background-position: 50% 50%;
		}

	}

	.login-logo-icon {
		position: absolute;
		width: 96px;
		height: 96px;
		left: 50%;
		top: 16%;
		margin-top: -(96px * 0.8);
		margin-left: -(96px/2);
		background-image: url("../../assets/img/login/logo-icon.png");
		z-index: 5;
	}

	.login-logo {
		position: absolute;
		width: 500px;
		height: 44px;
		left: 50%;
		top: 24%;
		margin-top: -(44px * 0.8);
		margin-left: -(500px/2);
		font-size: 36px;
		color: #ffffff;
		text-align: center;
		font-weight: bold;
		z-index: 5;
	}

	.login-dialog, .login-forget {
		position: absolute;
		top: 47.2%;
		left: 50%;
		margin-top: -(422px * 0.382);
		margin-left: -(360px/2);
		z-index: 100;
	}

	.login-register {
		position: absolute;
		top: 266px;
		left: 50%;
		margin-left: -(1000px / 2);
		width: 1000px;
		height: 544px;
		background: #FFFFFF;
		box-shadow: 0px 2px 8px 0px rgba(0, 0, 0, 0.15);
		border-radius: 4px;
		z-index: 9;
	}

	.register-btn {
		float: right;
	}


	@media screen and (max-height: 720px) {

		.login-dialog {
			top: 50%;
			margin-top: -(422px * 0.5);
		}

		.login-logo {
			top: 10%;
			margin-top: -(44px * 0.8);
		}
	}


	.fb-form-item {
		padding-bottom: 24px;
	}

	.login-footer {
		position: absolute;
		width: 360px;
		height: 44px;
		bottom: 30px;
		left: 50%;
		margin-left: -(360px/2);
		z-index: 10;

		p {
			text-align: center;
			color: #fff;
			margin: 0;
		}
	}

</style>
