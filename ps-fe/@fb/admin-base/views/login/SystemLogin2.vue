<template>
	<!--  只进行登录相关操作 - 为后续拆分做准备 -->
	<div class="login-wrapper">
		<!-- 加载状态 -->
		<div v-if="configLoading" class="config-loading">
			<fb-spin size="large" tip="正在加载系统配置..."></fb-spin>
		</div>
		
		<!-- 错误状态 -->
		<config-error 
			v-else-if="configError" 
			:error-title="errorInfo.title"
			:error-message="errorInfo.message"
			:error-code="errorInfo.code"
			:request-id="errorInfo.requestId"
			:auto-retry="errorInfo.autoRetry"
			:auto-retry-interval="30"
			@retry="loadSystemConfig"
			@contact-support="handleContactSupport"
		/>
		
		<!-- 正常登录界面 -->
		<template v-else>
			<div class="login-logo">
				<span class="logo" :style="logoStyle"></span>
				{{ loginLogoTitle }}
			</div>

		<div class="login-content">
			<div class="content">
				<login-card-scan-code-verify :inLoginProcess="inLoginProcess"
				 captcha-type="slider"
					@doLogin="doLoginNew" @toAppDownload="toAppDownload"
					 @toForget="toForget" @toRegister="toRegister"
					ref="loginCard" :show-register-link="true"
					:show-forget-link="true"  
					 v-show="!seletDepartment && !cardCode" />

				<login-card-department-list v-show="seletDepartment" :fieldObj="{ label: 'deptFullName', id: 'deptId' }"
					:departmentList="departmentList" @confirmDepartment="confirmDepartment" />

				<transition name="fade-transform">
					<router-view></router-view>
				</transition>

				<!-- <div v-show="cardCode == 3" class="login-forget">
					<appdown-card @toLogin="toLogin" qrcode-url="https://alilaoba.cn/"></appdown-card>
				</div> -->

				<div v-show="cardCode == 4">
					<force-update-pwd ref="forceUpdatePwd" @toLogin="toLogin"
						@doUpdatePwd="doUpdatePwd"></force-update-pwd>
				</div>

		 

			</div>
		</div>

			<div class="login-footer">
				<p v-html="$xss(copyright)"></p>
			</div>

			<tp-dialog ref="TpDialog"></tp-dialog>
		</template>
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
import appdownCard from '../../components/login/AppdownCard'
import forceUpdatePwd from '../../components/login/ForceUpdatePwd'
import ConfigError from '../../components/ConfigError'
import { startRefreshToken } from '../../util/tokenUtil'
export default {
	components: {
		LoginCardDepartmentList,
		loginCardDragVerify,
		loginCardCodeVerify,
		loginCardPhoneVerify,
		loginCardScanCodeVerify,
		appdownCard,
		forceUpdatePwd,
		ConfigError,
	},

	data() {
		// 默认配置值
		let defaultLoginLogoTitle = app.projectConfig.login.title || app.projectConfig.loginLogoTitle
		let defaultCopyright = this.$route.meta.copyright || app.projectConfig.router.copyright || `Copyright © 2001-${app.$dayjs().format('YYYY')} JPX.保留所有权利`
		let loginPath = this.$route.meta.rootPath || app.projectConfig.router.loginPath
		
		// 从window._publicConfig获取配置（兼容旧版本）
		if (window._publicConfig) {
			if (window._publicConfig.loginLogoTitle) {
				defaultLoginLogoTitle = window._publicConfig.loginLogoTitle
			}
			if (window._publicConfig.copyright) {
				defaultCopyright = window._publicConfig.copyright
			}
			if (window._publicConfig.loginPath) {
				loginPath = window._publicConfig.loginPath
			}
		}

		return {
			// 配置加载状态
			configLoading: true,
			configError: false,
			
			// 错误信息
			errorInfo: {
				title: '服务暂时不可用',
				message: '无法获取系统配置信息，请检查网络连接或稍后重试',
				code: '',
				requestId: '',
				autoRetry: false
			},
			
			// 动态配置数据
			loginLogoTitle: defaultLoginLogoTitle,
			copyright: defaultCopyright,
			logoUrl: window._publicConfig?.loginLogo || '/static/images/logo.svg',
			
			// 其他状态
			loginPath,
			// 0 = 登录  1 = 注册  2 = 忘记密码  3 = App下载  4 = 强制修改密码
			cardCode: -1,
			inLoginProcess: false, // 登录 loading
			seletDepartment: false,
			departmentList: [],
		}
	},

	computed: {
		// 动态logo样式
		logoStyle() {
			return {
				backgroundImage: `url("${this.logoUrl}")`
			}
		}
	},

	watch: {
		'$route': {
			handler() {
				this.initCardCode()
			},
			deep: true
		}
	},

	mounted() {

		//this.$message.info(this.$route.params)

	 
		this.initCardCode()
		// 加载系统配置
		this.loadSystemConfig()
	},

	methods: {
		// 加载系统配置
		async loadSystemConfig() {
			try {
				this.configLoading = true
				this.configError = false
				
				// 重置错误信息
				this.errorInfo = {
					title: '服务暂时不可用',
					message: '无法获取系统配置信息，请检查网络连接或稍后重试',
					code: '',
					requestId: this.generateRequestId(),
					autoRetry: false
				}
				
				// 获取登录页面相关配置
				const configKeys = ['login.title', 'login.logo', 'system.copyright']
				const promises = configKeys.map(key => 
					this.$svc.sys.config.getConfigValue(key)
						.then(res => ({ key, value: res.data, success: true }))
						.catch(err => ({ key, error: err, success: false, errorDetails: err }))
				)
				
				// 设置超时时间
				const timeoutPromise = new Promise((_, reject) => {
					setTimeout(() => reject(new Error('请求超时')), 10000)
				})
				
				const results = await Promise.race([
					Promise.all(promises),
					timeoutPromise
				])
				
				// 处理配置结果
				let hasError = false
				let errorDetails = []
				
				results.forEach(result => {
					if (result.success && result.value) {
						switch (result.key) {
							case 'login.title':
								this.loginLogoTitle = result.value
								break
							case 'login.logo':
								this.logoUrl = result.value
								break
							case 'system.copyright':
								this.copyright = result.value
								break
						}
					} else if (!result.success) {
						console.warn(`获取配置 ${result.key} 失败:`, result.error)
						hasError = true
						errorDetails.push({
							key: result.key,
							error: result.errorDetails
						})
					}
				})
				
				// 如果所有配置都失败，显示错误状态
				if (results.every(r => !r.success)) {
					this.updateErrorInfo(errorDetails)
					this.configError = true
				} else {
					// 部分成功或全部成功，显示登录界面
					this.configError = false
				}
				
			} catch (error) {
				console.error('加载系统配置失败:', error)
				this.updateErrorInfoFromException(error)
				this.configError = true
			} finally {
				this.configLoading = false
			}
		},
		
		// 生成请求ID
		generateRequestId() {
			return 'req_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9)
		},
		
		// 根据错误详情更新错误信息
		updateErrorInfo(errorDetails) {
			if (errorDetails && errorDetails.length > 0) {
				const firstError = errorDetails[0].error
				
				// 根据错误类型设置不同的错误信息
				if (firstError?.code) {
					this.errorInfo.code = firstError.code
				}
				
				if (firstError?.message) {
					if (firstError.message.includes('timeout') || firstError.message.includes('超时')) {
						this.errorInfo.title = '请求超时'
						this.errorInfo.message = '服务器响应超时，请检查网络连接后重试'
						this.errorInfo.autoRetry = true
					} else if (firstError.message.includes('Network') || firstError.message.includes('网络')) {
						this.errorInfo.title = '网络连接异常'
						this.errorInfo.message = '无法连接到服务器，请检查网络设置'
						this.errorInfo.autoRetry = true
					} else if (firstError.status === 500) {
						this.errorInfo.title = '服务器内部错误'
						this.errorInfo.message = '服务器遇到了一个错误，请稍后重试或联系技术支持'
						this.errorInfo.code = '500'
					} else if (firstError.status === 404) {
						this.errorInfo.title = '服务不存在'
						this.errorInfo.message = '请求的配置服务不存在，请联系技术支持'
						this.errorInfo.code = '404'
					} else {
						this.errorInfo.message = firstError.message
					}
				}
			}
		},
		
		// 根据异常更新错误信息
		updateErrorInfoFromException(error) {
			if (error.message.includes('请求超时')) {
				this.errorInfo.title = '请求超时'
				this.errorInfo.message = '服务器响应超时，请检查网络连接后重试'
				this.errorInfo.autoRetry = true
			} else {
				this.errorInfo.title = '系统异常'
				this.errorInfo.message = '系统遇到了未知错误，请稍后重试或联系技术支持'
				this.errorInfo.code = 'UNKNOWN_ERROR'
			}
		},
		
		// 处理联系技术支持
		handleContactSupport(supportInfo) {
			const subject = encodeURIComponent('系统配置加载失败 - 技术支持请求')
			const body = encodeURIComponent(`
错误详情：
- 错误时间：${supportInfo.timestamp}
- 错误代码：${supportInfo.errorCode || '未知'}
- 请求ID：${supportInfo.requestId || '未知'}
- 页面地址：${supportInfo.url}
- 浏览器信息：${supportInfo.userAgent}

问题描述：
系统配置加载失败，无法正常访问登录页面。

请技术支持团队协助解决此问题。
			`)
			
			// 可以根据实际情况修改邮箱地址
			const mailtoLink = `mailto:support@company.com?subject=${subject}&body=${body}`
			
			// 尝试打开邮件客户端
			try {
				window.location.href = mailtoLink
			} catch (e) {
				// 如果无法打开邮件客户端，显示提示信息
				this.$message.info({
					content: '请联系技术支持：support@company.com',
					duration: 5
				})
				
				// 复制支持信息到剪贴板
				if (navigator.clipboard) {
					navigator.clipboard.writeText(`错误代码：${supportInfo.errorCode}\n请求ID：${supportInfo.requestId}\n时间：${supportInfo.timestamp}`)
						.then(() => {
							this.$message.success('错误信息已复制到剪贴板')
						})
						.catch(() => {
							console.warn('无法复制到剪贴板')
						})
				}
			}
		},
		
		// 判断初始化 路由 是否展示登录卡片
		initCardCode() {
			let { path } = this.$route
			let loginPathChild = this.loginPath + '/'
			if (path.indexOf(loginPathChild) !== -1) {
				this.cardCode = -1
			} else {
				this.cardCode = 0
			}
		},
		// 子 =》 父 处理登录
		doLoginNew(loginForm) {


			let formData = {
				"clientUuid": loginForm.clientUuid,
				"x": loginForm.x,
				"type": loginForm.type,
				"startSlidingTime": loginForm.startSlidingTime,
				"entSlidingTime": loginForm.entSlidingTime,
			}

			app.service.request({
				url: '/platform/captcha/check-captcha',
				method: 'post', // 请求方式 post,get, 默认是 get,
				// `params` 是即将与请求一起发送的 URL 参数, get 采用
				transformRequest: [
					// 把json数据序列化成xxx=?&xx=?的格式
					function (data) {
						let ret = ''
						for (let it in data) {
							ret += encodeURIComponent(it) + '=' +
								encodeURIComponent(data[it]) + '&'
						}
						ret = ret.substring(0, ret.lastIndexOf('&'))
						return ret
					},
				],
				// `data` 是作为请求主体被发送的数据， post 采用
				data: formData,
				// `headers` 是即将被发送的自定义请求头
				headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
				// `responseType` 表示服务器响应的数据类型，可以是 'arraybuffer', 'blob',
				// 'document', 'json', 'text', 'stream'
				// responseType: 'json', // 默认的
				// `timeout` 指定请求超时的毫秒数(0 表示无超时时间)
				// 如果请求话费了超过 `timeout` 的时间，请求将被中断
				timeout: 5000,
			}).then((res) => {

				if (res) {

					this.ticket = res.data
					// 获取票据
					loginForm.ticket = res.data
					// 执行登录
					this.doLogin(loginForm)
				} else {
					this.$store.commit('login/setDragImgStatus', 'fail')
				}
			})
		},
		// 子 =》 父 处理登录
		doLoginOld_Long(loginForm) {
			// console.log(loginForm)
			// app.service.post('platform/captcha/check-captcha', loginForm).then((res) => {
			// 	console.log(res)
			// })
			app.service.request({
				url: 'captcha/check-captcha',
				method: 'post', // 请求方式 post,get, 默认是 get,
				// `params` 是即将与请求一起发送的 URL 参数, get 采用
				transformRequest: [
					// 把json数据序列化成xxx=?&xx=?的格式
					function (data) {
						let ret = ''
						for (let it in data) {
							ret += encodeURIComponent(it) + '=' +
								encodeURIComponent(data[it]) + '&'
						}
						ret = ret.substring(0, ret.lastIndexOf('&'))
						return ret
					},
				],
				// `data` 是作为请求主体被发送的数据， post 采用
				data: loginForm,
				// `headers` 是即将被发送的自定义请求头
				headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
				// `responseType` 表示服务器响应的数据类型，可以是 'arraybuffer', 'blob',
				// 'document', 'json', 'text', 'stream'
				// responseType: 'json', // 默认的
				// `timeout` 指定请求超时的毫秒数(0 表示无超时时间)
				// 如果请求话费了超过 `timeout` 的时间，请求将被中断
				timeout: 5000,
			}).then((res) => {
				if (res) {
					this.$message.success(res.message)
					this.$store.commit('login/setDragImgStatus', 'success')
				} else {
					this.$store.commit('login/setDragImgStatus', 'fail')
				}
			})
		},
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
				if (json && json.code === 403) {
					// 许可证不可用
					this.$message.error(json.message)
					this.$store.commit('login/setDragImgStatus', 'fail')
					this.handleLicence()
					return;
				}

				if (!json) {
					this.$store.commit('login/setDragImgStatus', 'fail')
					return;
				}
				// this.goToMain();

				// 								// 启动刷新token
				// 								startRefreshToken();

				// 				return
				// 判断密码是否需要强制修改
				if (json.data && json.data.restPwd === "1") {
					// 强制修改密码（原有逻辑保持不变）
					let token = this.$datax.get('token');
					this.$datax.remove('token');
					// 防止用户不修改密码直接刷新浏览器，所以这里将登陆系统的token删除，重新缓存一个临时token
					this.$datax.set('temporary', token)
					this.$store.commit('login/setDragImgStatus', 'fail')
					this.cardCode = 4;
					// 将密码框清空
					loginForm.userpwd = null;

				} else if (json.data && json.data.restPwd === "2") {
					// 密码即将过期，显示提醒询问是否修改
					this.$confirm('您的密码即将过期，是否现在修改密码？', '密码过期提醒', {
						confirmButtonText: '是',
						cancelButtonText: '否',
						type: 'warning'
					}).then(() => {
						// 用户选择修改密码，进入密码修改界面
						let token = this.$datax.get('token');
						this.$datax.remove('token');
						this.$datax.set('temporary', token)
						this.$store.commit('login/setDragImgStatus', 'fail')
						this.cardCode = 4;
						loginForm.userpwd = null;
					}).catch(() => {
						// 用户选择不修改，继续正常登录
						this.handleNormalLogin(json);
					});

				} else if (json.data && json.data.restPwd === "3") {
					// 在强制修改期内，直接引导用户进入密码修改界面
					this.$message.info('为了您的账户安全，请修改密码后继续使用系统');
					let token = this.$datax.get('token');
					this.$datax.remove('token');
					this.$datax.set('temporary', token)
					this.$store.commit('login/setDragImgStatus', 'fail')
					this.cardCode = 4;
					loginForm.userpwd = null;
				} else {
					if (json.data && json.data.deptVOList && json.data.deptVOList.length > 1) {
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

							this.$datax.set('userInfo', (res && res.data) || {})
							// 跳转页面
							let that = this;
							if (!this.seletDepartment) {
								that.goToMain();

								// 启动刷新token
								startRefreshToken();

								// this.$router.replace('/register')
								that.reportLogin();
							}
						});
					}
				}
			})
		},
		// 上传许可证
		handleLicence() {
			let that = this;
			let param = {};

			let options = {
				tabChangeConfirm: false,
				callBack: function (result) {

				}
			}
			// 打开新增界面弹出窗
			this.$refs.TpDialog.show("/login/upload-licence.vue", param, "更新许可证", options);
		},
		confirmDepartment(val) {
			if (!val) {
				this.$message.warn('请点选部门登录')
			}
			// console.log(val)
			this.$svc.platform.selectDeptLogin({ 'deptId': val }).then(res => {
				this.$datax.set('token', res.data.token)
				this.$datax.set('user', res.data)
				this.$store.dispatch('admin/setToken', res.data.token)
				// 切换部门标识恢复
				this.seletDepartment = false
				// 获取登陆人用户信息
				let that = this;
				this.$svc.platform.getUserInfo().then(res => {
					this.$datax.set('userInfo', res.data || {})
					that.goToMain();
					that.reportLogin();
				});
			})
		},
		goToMain() {

			this.$router.replace(this.$datax.get('GLOBAL_CONFIG').mainPath)

// 日志埋点
			this.$logger && this.$logger.send({
				// 模块编码
				moduleCode: 'login',
				// 模块名称
				moduleName: '登录',
				// 操作类型： login/logout/add/delete/edit/query/pass/unpass, 可以自己定义
				operateType: 'login',
				// 操作人id
				operterId:  app.$datax.get('userInfo').personId,
			});
			
			return
			const { hijack } = this.$route.meta || app.projectConfig.login
			// 获取当前登录用户信息
			let userInfo = app.$datax.get('userInfo')

			// 日志埋点
			let data = {
				// 模块编码
				moduleCode: 'login',
				// 模块名称
				moduleName: '登录',
				// 操作类型： login/logout/add/delete/edit/query/pass/unpass, 可以自己定义
				operateType: 'login',
				// 操作人id
				operterId: userInfo.personId,
			}
			this.$logger && this.$logger.send(data);

			if (hijack) {
				hijack(userInfo)
				return
			}

			this.licenceWarn(() => {

				// 跳转页面
				this.$router.replace(this.$datax.get('GLOBAL_CONFIG').mainPath)

				// 日志埋点
				this.$logger && this.$logger.send(data);
			});


		},
		// 许可证临期提醒
		licenceWarn(callback) {
			return
			app.service.get('/sys/licence/warn', {}).then((result) => {
				if (result.code === 1) {
					if (result.data) {
						this.$msgbox.confirm("系统将于" + result.data + "天后试用到期，请尽快联系系统供应商。", () => {
							callback();
						})
					} else {
						callback();
					}
				}
			})
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
			this.$router.push(this.loginPath + '/register')
		},
		toForget() {
			this.cardCode = 2
			this.$router.push(this.loginPath + '/forget-pass')
		},
		toAppDownload() {
			this.cardCode = 3
			this.$message.success('请打开手机，扫描二维码')
		},
		toForceUpdatePwd() {
			this.cardCode = 4
		},
		// 处理正常登录流程
		handleNormalLogin(json) {
			if (json.data && json.data.deptVOList && json.data.deptVOList.length > 1) {
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
					this.$datax.set('userInfo', (res && res.data) || {})
					// 跳转页面
					let that = this;
					if (!this.seletDepartment) {
						that.goToMain();

						// 启动刷新token
						startRefreshToken();

						// this.$router.replace('/register')
						that.reportLogin();
					}
				});
			}
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
// @import ".";

@import "../../assets/styles/login/login2.less";

// 配置加载状态样式
.config-loading {
	display: flex;
	justify-content: center;
	align-items: center;
	height: 100vh;
	background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
	color: white;
	
	.fb-spin {
		color: white;
	}
}

// 配置错误状态样式
.config-error {
	display: flex;
	justify-content: center;
	align-items: center;
	height: 100vh;
	background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
	padding: 20px;
	
	.fb-result {
		background: rgba(255, 255, 255, 0.95);
		border-radius: 8px;
		padding: 40px;
		box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
		max-width: 500px;
		width: 100%;
	}
}

// logo动态样式支持
.login-logo .logo {
	background-size: contain;
	background-repeat: no-repeat;
	background-position: center;
}
</style>
