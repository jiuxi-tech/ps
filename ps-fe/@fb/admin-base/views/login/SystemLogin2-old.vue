<template>
	<div class="login-wrapper">

		<div class="login-logo">
			<span class="logo"></span>
			{{logoTitle}}
		</div>

		<div class="login-content">
			<div class="content">
				<login-card-scan-code-verify
					:inLoginProcess="inLoginProcess"
					@doLogin="doLogin"
					@toAppDownload="toAppDownload"
					@toForget="toForget"
					@toRegister="toRegister"
					ref="loginCard"
					v-show="!seletDepartment && !cardCode"/>

				<!--				<login-card-phone-verify-->
				<!--					v-show="!seletDepartment && !cardCode"-->
				<!--					ref="loginCard"-->
				<!--					:inLoginProcess="inLoginProcess"-->
				<!--					@doLogin="doLogin"-->
				<!--					@toRegister="toRegister"-->
				<!--					@toForget="toForget"-->
				<!--					@toAppDownload="toAppDownload"/>-->

				<!--				<login-card-drag-verify-->
				<!--					v-show="!seletDepartment && !cardCode"-->
				<!--					ref="loginCard"-->
				<!--					:inLoginProcess="inLoginProcess"-->
				<!--					@doLogin="doLogin"-->
				<!--					@toRegister="toRegister"-->
				<!--					@toForget="toForget"-->
				<!--					@toAppDownload="toAppDownload"-->
				<!--					@toForceUpdatePwd="toForceUpdatePwd"-->
				<!--				/>-->

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
						@doForgetFinish="doForgetFinish"
					></forget-card>
				</div>

				<div v-show="cardCode == 3" class="login-forget">
					<appdown-card @toLogin="toLogin"></appdown-card>
				</div>

				<div v-show="cardCode == 4">
					<force-update-pwd
						ref="forceUpdatePwd"
						@toLogin="toLogin"
						@doUpdatePwd="doUpdatePwd"
					></force-update-pwd>
				</div>

			</div>
		</div>


		<div class="login-footer">
			<p>Copyright © 2001-2021 JPX.保留所有权利</p>
		</div>

		<tp-dialog ref="TpDialog"></tp-dialog>
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
	import forceUpdatePwd from '../../components/login/ForceUpdatePwd'

	export default {

		routerConfig: {
			path: '/login',
			name: 'login',
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
			forceUpdatePwd,
		},

		data() {
			let logoTitle = app.projectConfig.logoTitle
			return {
				logoTitle,
				// 0 = 登录  1 = 注册  2 = 忘记密码  3 = App下载  4 = 强制修改密码
				cardCode: 0,
				inLoginProcess: false, // 登录 loading
				seletDepartment: false,
				departmentList: [],
			}
		},

		mounted() {

		},

		methods: {
			// 子 =》 父 处理登录
			doLogin(loginForm) {
				console.log(loginForm)
				// 表单验证在子组件中处理，登录只需要获取子组件数据值，进行异步请求处理
				this.inLoginProcess = true
				this.departmentList = []
				// 图片验证码登陆
				this.$svc.platform.captChaLogin(loginForm).then(json => {
					app.trigger('monitor-login', json)
					// 登录按钮 loading
					this.inLoginProcess = false
					if (!json) {
						this.$store.commit('login/setDragImgStatus', 'fail')
						return;
					}

					// 判断密码是否需要强制修改
					if (json.data && json.data.restPwd === "1") {
						// 强制修改密码
						let token = this.$datax.get('token');
						this.$datax.remove('token');
						// 防止用户不修改密码直接刷新浏览器，所以这里将登陆系统的token删除，重新缓存一个临时token
						this.$datax.set('temporary', token)
						this.$store.commit('login/setDragImgStatus', 'fail')
						this.cardCode = 4;
						// 将密码框清空
						loginForm.userpwd = null;

					} else {
						if (json.data && json.data.deptVOList.length > 1) {
							this.$store.commit('login/setDragImgStatus', 'success')
							this.$datax.set('token', json.data.token)
							// 在多个部门下
							this.departmentList = json.data.deptVOList
							// 切换部门，选择部门登陆
							this.seletDepartment = true
						} else {
							this.$store.commit('login/setDragImgStatus', 'success')
							this.$datax.set('token', json.data.token)
							this.$datax.set('user', json.data)
							this.$store.dispatch('admin/setToken', json.data.token)

							// 获取登陆人用户信息
							this.$svc.platform.getUserInfo().then(res => {
								this.$datax.set('userInfo', res.data)
								// 跳转页面
								let that = this;
								if (!this.seletDepartment) {
									that.goToMain();
									// this.$router.replace('/register')
									that.reportLogin();
								}
							});
						}
					}
				})
			},

			confirmDepartment(val) {
				if (!val) {
					this.$message.warn('请点选部门登录')
				}
				// console.log(val)
				this.$svc.platform.selectDeptLogin({'deptId': val}).then(res => {
					this.$datax.set('token', res.data.token)
					this.$datax.set('user', res.data)
					this.$store.dispatch('admin/setToken', res.data.token)
					// 切换部门标识恢复
					this.seletDepartment = false
					// 获取登陆人用户信息
					let that = this;
					this.$svc.platform.getUserInfo().then(res => {
						this.$datax.set('userInfo', res.data)
						that.goToMain();
						that.reportLogin();
					});
				})
			},
			goToMain() {
				// 获取当前登录用户信息
				let userInfo = app.$datax.get('userInfo')

				// 日志埋点
				let data = {
					project: '小综合',
					// 模块编码
					moduleCode: 'login',
					// 模块名称
					moduleName: '登录',
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
			reportLogin() {
				// 是否需要单点登录报表系统
				if (!app.projectConfig.sysconfig.reportSwitch) {
					return;
				}
				// 单点登录报表系统
				this.$svc.platform.reportLogin({}).then((result) => {
					if (result.code == 1) {

					} else {
						// 服务器返回失败
						this.$message.error('报表登陆失败: ' + result.message)
					}
				})

			},
			toLogin() {
				this.cardCode = 0
			},
			toRegister() {
				this.cardCode = 1
			},
			toForget() {
				this.cardCode = 2
			},
			toAppDownload() {
				this.cardCode = 3
				this.$message.success('请打开手机，扫描二维码')
			},
			toForceUpdatePwd() {
				this.cardCode = 4
			},
			// 注册逻辑
			doRegister(registerForm) {
				console.log(registerForm)
				// 异步请求后步骤加 1
				this.$message.success('注册下一步，前往登录')
				this.$refs['registerCard'].process += 1
			},
			doForgetFinish() {
				this.cardCode = 0
			},
			// 强制修改密码逻辑
			doUpdatePwd(param) {
				//	获取临时token并清空临时token
				let token = this.$datax.get('temporary');
				this.$datax.remove('temporary');
				let newUserpwd = param.userpwd;
				let newConfirmUserPwd = param.confirmUserPwd;
				// 密码传输判断是否需要加密
				let sysconfig = app.projectConfig.sysconfig;
				if (sysconfig.passwordEncryption && sysconfig.passwordEncryption === true) {
					const sm2 = require('sm-crypto').sm2
					// 1 - C1C3C2，0 - C1C2C3，默认为 1
					const cipherMode = 1
					// 公钥
					let publicKey = sysconfig.publicKey;
					// sm2加密
					newUserpwd = sm2.doEncrypt(param.userpwd, publicKey, cipherMode)
					newConfirmUserPwd = sm2.doEncrypt(param.confirmUserPwd, publicKey, cipherMode)
				}

				this.$svc.platform.update({
					"userpwd": newUserpwd,
					"confirmUserPwd": newConfirmUserPwd
				}, token).then((result) => {
					if (result.code == 1) {
						this.$message.success('修改成功！请重新登录！');
						this.cardCode = 0;
					} else {
						// 服务器返回失败
						this.$message.error('修改失败: ' + result.message)
					}
				})
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
		overflow: auto;
		z-index: 1;


	}

	.login-logo {
		width: 1366px;
		margin: 0 auto;
		height: 100px;
		padding-top: 24px;
		padding-left: 24px;
		font-size: 36px;
		font-weight: 800;
		color: #0284FE;
		line-height: 44px;

		.logo {
			display: inline-block;
			width: 60px;
			height: 60px;
			background: url("../../assets/img/login/logo-blue.png") no-repeat center;
			background-size: cover;
			vertical-align: middle;
		}
	}

	.login-content {
		min-width: 1366px;
		height: 600px;
		background: linear-gradient(180deg, #0197FF 0%, #025EFE 100%);
		position: relative;
		overflow: hidden;
		z-index: 1;

		.content {
			position: relative;
			overflow: hidden;
			z-index: 1;
			height: 600px;
			width: 1366px;
			margin: 0 auto;
			background: url("../../assets/img/login/bg2.png") no-repeat center;
			background-size: cover;
		}
	}

	.login-dialog, .login-forget {
		position: absolute;
		top: 68px;
		right: 202px;
	}

	.login-register {
		position: absolute;
		top: 20px;
		left: 183px;
		width: 1000px;
		height: 544px;
		background: #FFFFFF;
		box-shadow: 0px 2px 8px 0px rgba(0, 0, 0, 0.15);
		border-radius: 4px;
		z-index: 9;
	}

	.login-footer {
		width: 1366px;
		margin: 0 auto;
		height: 100px;
		padding-top: 24px;

		p {
			text-align: center;
			color: #666666;
			margin: 0;
		}
	}


</style>
