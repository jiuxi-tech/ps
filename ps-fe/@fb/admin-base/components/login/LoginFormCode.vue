<!-- 验证码填写登录 form -->
<template>
	<fb-form :model="loginForm" ref="loginForm" autocomplete="off">

		<fb-form-item prop="userName" label="用户名" :showLabel="false"
					  :rule="[{required: true, whitespace: true}]">
			<fb-input v-model="loginForm.userName"
					  size="l"
					  prepend-icon="user"
					  clearable
					  placeholder="用户名"
					  autocomplete="off"
			/>
		</fb-form-item>

		<fb-form-item prop="userpwd" label="密码" :showLabel="false"
					  :rule="[{required: true }]">
			<fb-input v-model="loginForm.userpwd"
					  size="l"
					  type="password"
					  prepend-icon="password"
					  clearable
					  placeholder="密码"
					  autocomplete="off"
			>
			</fb-input>
		</fb-form-item>

		<fb-form-item prop="capture" label="验证码" :showLabel="false"
					  :rule='[{required: true}, {type: "string", len: 4}]'
					  style="padding-bottom: 40px">
			<fb-input v-model="loginForm.capture"
					  size="l"
					  type="capture"
					  prepend-icon="verificationcode"
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
</template>

<script>

	/**
	 * DefaultApplicationLogin
	 * (c) 2020 lincong1987
	 */
	import SystemCapture from "./SystemCapture";

	export default {

		components: {
			SystemCapture
		},

		props: {
			inLoginProcess: {
				type: Boolean,
				default: false
			},
			clearForm: {
				type: Boolean,
				default: false
			}
		},

		data () {
			return {
				loginForm: {
					userName: '',
					userpwd: '',
					capture: '',
				},
			}
		},

		mounted () {
			// this.$datax.set('capturePath', 'http://admin.dlszywz.cn/include/captcha/captcha.php?')
			window.addEventListener('keyup', this.enterCall)
		},

		methods: {
			doLogin () {
				this.$refs.loginForm.validate((result, error) => {
					if (result) {
						this.$emit('doLogin', this.loginForm)
					} else {
						this.$message.warn('请填写正确的用户信息！')
					}
				})
			},
			destroy () {
				if (this.$refs['loginForm']) {
					this.$refs['loginForm'].resetFields();
				}
				window.removeEventListener('keyup', this.enterCall);
			},
			enterCall (e) {
				let code = e.keyCode || e.which
				if (code === 13) {
					this.doLogin()
				}
			}
		},

		deactivated () {
			this.destroy()
		},

		destroyed() {
			this.destroy()
		}

	}
</script>

<style lang="less" scoped>
	@import ~"../../assets/styles/common";
	.@{FbUiPrefix}-form-item {
		padding-bottom: 24px;
	}
</style>
