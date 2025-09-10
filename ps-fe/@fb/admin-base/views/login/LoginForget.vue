<template>
	<!--  系统忘记密码 - login 的二级路由	-->
	<div class="login-forget">
		<!-- <forget-card
			ref="forgetCard"
			@toLogin="toLogin"
			@doForgetFinish="doForgetFinish"
		></forget-card> -->
	 <forget-card-mail
			ref="forgetCard"
			@toLogin="toLogin"
			@doForgetFinish="doForgetFinish"
		></forget-card-mail>
	</div>
</template>

<script>
/**
 * LoginForget.vue 我在login的路由下
 * (c) 2020 lincong1987
 */

import ForgetCard from "../../components/login/ForgetCard.vue";
import ForgetCardMail from "../../components/login/ForgetCardMail.vue";

export default {
	components: {
		ForgetCard,
		ForgetCardMail
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
		doForgetFinish() {
			// this.$router.go(-1)
			this.$router.push(app.projectConfig.router.loginPath)
		},
	},

}
</script>

<style lang="less" scoped>


</style>
