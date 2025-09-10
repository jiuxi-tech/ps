<!-- 拖拽滑块登录 form -->
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

		<fb-form-item prop="isVerify" style="padding-bottom: 40px">


			<fb-drag-verify v-model="loginForm.isVerify"></fb-drag-verify>

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

export default {

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
				isVerify: false
			},
		}
	},
	mounted() {
		window.addEventListener('keyup', this.enterCall)
	},

	methods: {
		doLogin () {
			if (!this.loginForm.isVerify) {
				return this.$message.error('请拖拽滑块验证')
			}
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
@import "../../assets/styles/common.less";
.@{FbUiPrefix}-form-item {
	padding-bottom: 24px;
}
</style>
