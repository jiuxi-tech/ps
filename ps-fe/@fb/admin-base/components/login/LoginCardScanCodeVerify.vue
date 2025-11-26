<!-- 账号登录 / 手机登录 -->
<template>
	<transition name="slide-to-down">
		<div class="login-dialog">
			<!-- 扫码登录角标  -->
			<!-- <div class="point-code" :class="[showScanCode ? 'pc' : '']" @click="changeCard"></div> -->
			<div v-if="!showScanCode" class="login-form">
				<div class="login-form__caption">
					<fb-tabs v-model="currentTab">
						<fb-tab label="账号密码登录" name="pass"></fb-tab>
						<!-- <fb-tab label="手机号码登录" name="phone"></fb-tab> -->
					</fb-tabs>
				</div>

				<!--	验证码字段  -->
				<!-- <login-form-code
					ref="formDrag"
					:clearForm="clearForm"
					v-if="currentTab == 'pass'"
					:inLoginProcess="inLoginProcess"
					@doLogin="doLogin">
				</login-form-code> -->

				<!--	拖拽表单  -->
				<login-form-drag-img v-if="currentTab == 'pass'" :captcha-type="captchaType" ref="formDrag"
					:clearForm="clearForm" :inLoginProcess="inLoginProcess" :showPhoneText="showPhoneText"
					@doLogin="doLogin">
				</login-form-drag-img>

				<!--	手机表单	  -->
				<login-form-phone ref="formPhone" :clearForm="clearForm" v-if="currentTab == 'phone'"
					:inLoginProcess="inLoginProcess" :showPhoneText="showPhoneText" @doLogin="doLogin"
					@doShortMsg="doShortMsg">
				</login-form-phone>

				<div class="login-actions">
					<!-- SSO登录按钮和忘记密码链接 -->
					<div class="sso-forget-container">
						<div class="sso-login-section">
							<fb-button type="link" icon="key" @on-click="handleSSOLogin" class="sso-login-btn">

								使用SSO统一登录
							</fb-button>
						</div>
						<div class="forget-link-section">
							<fb-button v-show="showForgetLink" type="link" @on-click="toForget"
								class="forget-password-btn">
								忘记密码？
							</fb-button>
						</div>
					</div>
					<!-- <fb-link  v-show="showRegisterLink" type="primary" label="点击注册" class="register-btn" @click="toRegister"></fb-link> -->
				</div>

			</div>

			<div v-if="false || showScanCode" class="login-code">
				<!-- <fb-tabs v-model="codeCurrentTab" :data="[
					{label: '钉钉', value: 'ding1'},
				]"></fb-tabs> -->
				<!-- <system-scancode-dd v-if="codeCurrentTab === 'ding'"></system-scancode-dd> -->
				<div>
					<SystemScancode></SystemScancode>
					<!-- <div class="texts">
						<fb-icon name="scan"></fb-icon>
						<span>打开浙政钉扫描二维码登录</span>
					</div> -->
					<div class="links">
						<span @click="toForget">忘记密码？</span>
						<!-- <em>|</em>
						<span @click="toRegister">点击注册</span> -->
					</div>
				</div>
			</div>

			<!-- 底部 app 下载 -->
			<template>
				<div v-show="showCardBottom" class="login-card-bottom">
					<div class="login-card-bottom-left">
						<fb-icon name="telephone-fill" size="16"></fb-icon>
						<span>TEL：</span>
					</div>
					<!-- <div class="login-card-bottom-right">
						<slot>
							<fb-icon name="mobilephone" size="16"></fb-icon>
							<fb-link type="primary" label="APP下载" @click="toAppDownload"></fb-link>
						</slot>
					</div> -->
				</div>
			</template>
		</div>
	</transition>
</template>

<script>

import LoginFormCode from "./LoginFormCode";
import LoginFormDragImg from "./LoginFormDragImg";
import LoginFormPhone from "./LoginFormPhone";
import SystemScancode from "./SystemScancode";
import SystemScancodeDd from "./SystemScancodeDd";

/**
 * DefaultApplicationLogin
 * (c) 2020 lincong1987
 */

export default {

	components: {
		LoginFormCode,
		LoginFormDragImg,
		LoginFormPhone,
		SystemScancode,
		SystemScancodeDd,
	},

	props: {
		inLoginProcess: {
			type: Boolean,
			default: false
		},
		clearForm: {
			type: Boolean,
			default: false
		},
		captchaType: {
			type: String,
			default: ''
		},
		showForgetLink: {
			type: Boolean,
			default: true
		},
		showRegisterLink: {
			type: Boolean,
			default: true
		},
		showCardBottom: {
			type: Boolean,
			default: true
		},
		showPhoneText: {
			type: Boolean,
			default: true
		},
	},

	data() {
		return {
			currentTab: 'pass',
			codeCurrentTab: 'ding',
			showScanCode: false
		}
	},

	methods: {
		doLogin(loginForm) {
			this.$emit('doLogin', loginForm)
		},
		doShortMsg() {
			this.$emit('doShortMsg')
		},
		toRegister() {
			this.$emit('toRegister')
		},
		toForget() {
			this.$emit('toForget')
		},
		toAppDownload() {
			this.$emit('toAppDownload')
		},

		changeCard() {
			this.showScanCode = !this.showScanCode
		},

		// 处理SSO登录
		async handleSSOLogin() {
			console.log('开始SSO登录流程')
			// window.location.href = window._puclicConfig.ssoLoginUrl
			// return
			try {
				// 先获取redirect_uri配置
				const configResult = await app.service.get('/sys/config/get', {
					params: {
						configKey: 'sso.keycloak.redirect.uri'
					}
				})
				console.log('获取redirect_uri配置响应:', configResult)
				
				let redirectUri = '' // ' // 默认值
				if (configResult.data) {
					redirectUri = configResult.data
				}
				console.log('redirectUri:', redirectUri)
				if(!redirectUri){
					this.$message.error('获取SSO登录地址失败')
					return
				}
				
				// 获取Keycloak登录URL
				const result = await app.service.get('/api/sso/login-url', {
					params: {
						redirect_uri: redirectUri
					}
				})
				console.log('SSO登录URL响应:', result)
				
				if (result.success && result.data && result.data.loginUrl) {
					// 跳转到Keycloak登录页面
					console.log('跳转到Keycloak登录页面:', result.data.loginUrl)
					window.location.href = result.data.loginUrl
				} else {
					console.error('SSO登录URL响应格式错误:', result)
					this.$message.error('获取SSO登录地址失败')
				}
			} catch (error) {
				console.error('SSO登录过程出错:', error)
				this.$message.error('SSO登录服务暂时不可用')
			}
		}
	},
}
</script>

<style lang="less" scoped>
@import "../../assets/styles/login/login-card.less";

.login-actions {
	.sso-forget-container {
		margin-top: 20px;
		display: flex;
		justify-content: space-between;
		align-items: flex-start;
		gap: 16px;
	}

	.sso-login-section {}

	.forget-link-section {}

	.forget-password-btn {
		padding-left: 0;
		padding-right: 0;
	}

	.sso-login-btn {
		padding-left: 0;
		padding-right: 0;
	}
}

.login-dialog {

	.point-code {
		width: 48px;
		height: 48px;
		position: absolute;
		top: 8px;
		right: 8px;
		background: url("../../assets/img/login/icon-scan-code.png") center no-repeat;
		background-size: 100% 100%;
		cursor: pointer;

		&:hover {
			opacity: 0.7;
		}

		&.pc {
			background: url("../../assets/img/login/icon-pc.png") center no-repeat;
			background-size: 100% 100%;

			&:before {
				content: '';
				display: block;
				width: 99px;
				height: 20px;
				position: absolute;
				top: 0;
				left: -99px;
				background: url("../../assets/img/login/icon-pc-text.png") center no-repeat;
				background-size: 100% 100%;
			}
		}
	}

	.login-code {
		padding: 0 24px;
		padding-top: 86px;
		font-size: 14px;

		.title {
			font-size: 16px;
			font-family: MicrosoftYaHei;
			color: #313C47;
			line-height: 24px;
			margin-bottom: 47px;
		}

		.login-scan-code {
			margin: 0 auto;
		}

		.texts {
			margin-top: 24px;
			margin-bottom: 32px;
			text-align: center;

			span {
				margin-left: 8px;
			}
		}

		.links {
			text-align: center;

			em:nth-child(2) {
				margin: 0 16px 0 10px;
				font-style: normal;
			}

			span:hover {
				cursor: pointer;
				color: #0284FE;
				text-decoration: underline;
			}
		}

	}
}
</style>
