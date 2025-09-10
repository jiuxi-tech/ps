<!-- 拖拽验证 -->
<template>
	<transition name="slide-to-down">
		<div class="login-dialog">
			<div class="login-form">
				<div class="login-form__caption">
					欢迎登录
				</div>
				<!--	拖拽表单  -->
<!--				<login-form-drag-->
<!--					ref="formDrag"-->
<!--					:clearForm="clearForm"-->
<!--					:inLoginProcess="inLoginProcess"-->
<!--					@doLogin="doLogin">-->
<!--				</login-form-drag>-->

				<!--	拖拽拼图表单  -->
				<login-form-drag-img
					ref="formDrag"
					:no-drag="noDrag"
					:captcha-type="captchaType"
					:clearForm="clearForm"
					:inLoginProcess="inLoginProcess"
					@doLogin="doLogin">
				</login-form-drag-img>

www
				<div>
					<fb-link  v-show="showForgetLink" type="primary" label="忘记密码？" @click="toForget"></fb-link>
					<fb-link  v-show="showRegisterLink" type="primary" label="点击注册" class="register-btn" @click="toRegister"></fb-link>
				</div>
			</div>

			<!-- 底部 app 下载 -->
			<template>
				<div v-show="showCardBottom" class="login-card-bottom">
					<div v-show="showPhoneText" class="login-card-bottom-left">
						<fb-icon name="telephone-fill" size="16"></fb-icon>
						<span>TEL：</span>
					</div>
					<div class="login-card-bottom-right">
						<slot>
							<fb-icon name="mobilephone" size="16"></fb-icon>
							<fb-link type="primary" label="APP下载" @click="toAppDownload"></fb-link>
						</slot>
					</div>
				</div>
			</template>
		</div>
	</transition>
</template>

<script>
	/**
	 * DefaultApplicationLogin
	 * (c) 2020 lincong1987
	 */
	import LoginFormDrag from "./LoginFormDrag";
	import LoginFormDragImg from "./LoginFormDragImg";
	export default {

		name: "loginCardDragVerify",

		components: {
			LoginFormDrag,
			LoginFormDragImg,
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
			noDrag: {
				type: Boolean,
				default: false
			}
		},

		data () {
			return {}
		},

		mounted () {
			this.$refs['loginForm'] = this.$refs.formDrag.$children[0]
		},

		methods: {
			doLogin (loginForm) {
				this.$emit('doLogin', loginForm)
			},
			toRegister () {
				this.$emit('toRegister')
			},
			toForget () {
				this.$emit('toForget')
			},
			toAppDownload () {
				this.$emit('toAppDownload')
			},
		},

	}
</script>

<style lang="less" scoped>
@import "../../assets/styles/login/login-card.less";
</style>
