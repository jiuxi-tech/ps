<template>
	<!--  系统注册页 - login 的二级路由	-->
	<div class="login-register">
		<register-card-step
			ref="registerCard"
			@toLogin="toLogin"
			@doRegister="doRegister"
		></register-card-step>
	</div>
</template>

<script>
/**
 * LoginRegiter.vue 我在login的路由下
 * (c) 2020 lincong1987
 */

import registerCardStep from '../../components/login/RegisterCardStep'

export default {
	components: {
		registerCardStep,
	},

	data() {
		return {

		}
	},

	mounted() {

	},

	methods: {
		toLogin() {
			this.$router.go(-1)
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

</style>
