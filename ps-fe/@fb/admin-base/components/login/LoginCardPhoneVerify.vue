<!-- 账号登录 / 手机登录 -->
<template>
	<transition name="slide-to-down">
		<div class="login-dialog">
			<div class="login-form">
				<div class="login-form__caption">
					<fb-tabs v-model="currentTab">
						<fb-tab label="账号密码登录" name="pass"></fb-tab>
						<fb-tab label="手机号码登录" name="phone"></fb-tab>
					</fb-tabs>
				</div>

				<!--	验证码表单  -->
				<login-form-code
					ref="formDrag"
					:clearForm="clearForm"
					v-if="currentTab == 'pass'"
					:inLoginProcess="inLoginProcess"
					@doLogin="doLogin">
				</login-form-code>

				<!--	拖拽表单  -->
<!--				<login-form-drag-->
<!--					ref="formDrag"-->
<!--					:clearForm="clearForm"-->
<!--					v-if="currentTab == 'pass'"-->
<!--					:inLoginProcess="inLoginProcess"-->
<!--					@doLogin="doLogin">-->
<!--				</login-form-drag>-->

				<!--	手机表单	  -->
				<login-form-phone
					ref="formPhone"
					:clearForm="clearForm"
					v-if="currentTab == 'phone'"
					:inLoginProcess="inLoginProcess"
					@doLogin="doLogin"
					@doShortMsg="doShortMsg">
				</login-form-phone>

				<div>
					<fb-link  v-show="showForgetLink" type="primary" label="忘记密码？" @click="toForget"></fb-link>
					<fb-link  v-show="showRegisterLink" type="primary" label="点击注册" class="register-btn" @click="toRegister"></fb-link>
				</div>
			</div>

			<!-- 底部 app 下载 -->
			<template>
				<div v-show="showCardBottom" class="login-card-bottom">
					<div class="login-card-bottom-left">
						<fb-icon name="telephone-fill" size="16"></fb-icon>
						<span>客服：</span>
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

	import LoginFormCode from "./LoginFormCode";
	import LoginFormDrag from "./LoginFormDrag";
	import LoginFormPhone from "./LoginFormPhone";

	/**
	 * DefaultApplicationLogin
	 * (c) 2020 lincong1987
	 */

	export default {

		components: {
			LoginFormCode,
			LoginFormDrag,
			LoginFormPhone,
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
			}
		},

		data () {
			return {
				currentTab: 'pass',
			}
		},

		methods: {
			doLogin (loginForm) {
				this.$emit('doLogin', loginForm)
			},
			doShortMsg () {
				this.$emit('doShortMsg')
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
	@import ~"../../assets/styles/login/login-card";


</style>
