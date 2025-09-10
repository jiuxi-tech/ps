<template>
	<!--  系统注册页 - 单独页	-->
	<div class="login-wrapper">

		<div class="login-logo">
			<span class="logo"></span>
			{{ logoTitle }}
		</div>

		<div class="login-content">
			<div class="content">
				<div class="login-register">
					<register-card-step
						ref="registerCard"
						@toLogin="toLogin"
						@doRegister="doRegister"
					></register-card-step>
				</div>
			</div>
		</div>


		<div class="login-footer">
			<p v-html="copyright"></p>
		</div>
	</div>
</template>

<script>
/**
 * SystemAppRegiter
 * (c) 2020 lincong1987
 */

import registerCardStep from '../../components/login/RegisterCardStep'

export default {
	components: {
		registerCardStep,
	},

	data() {
		let logoTitle = app.projectConfig.login.title || app.projectConfig.logoTitle
		let copyright = app.projectConfig.login.copyright || 'Copyright © 2001-2021 JPX.保留所有权利'
		return {
			logoTitle,
			copyright
		}
	},

	mounted() {

	},

	methods: {
		toLogin() {
			this.$router.push('/login')
		},
		// 注册逻辑
		doRegister(registerForm) {
			console.log(registerForm)
			// 异步请求后步骤加 1
			this.$message.success('注册下一步，前往登录')
			this.$refs['registerCard'].process += 1
		},
	},

}
</script>

<style lang="less" scoped>
@import "../../assets/styles/login/login2.less";

</style>
