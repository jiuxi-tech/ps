<!-- 手机登录 form -->
<template>
	<fb-form :model="loginForm" ref="loginForm" autocomplete="off">

			<fb-form-item prop="mobile" label="手机号" :showLabel="false" :rule='[{required:true},{type: "mobile"}]'>
				<fb-input v-model="loginForm.mobile"
						  size="l"
						  prepend-icon="user"
						  clearable
						  placeholder="请输入手机号"
						  autocomplete="off"
				/>
			</fb-form-item>

			<fb-form-item prop="mobileCode" label="验证码" :showLabel="false"
						  :rule='[{required: true}, {type: "string", len: 6}]'
						  style="padding-bottom: 40px">
				<fb-input v-model="loginForm.mobileCode"
						  size="l"
						  type="capture"
						  prepend-icon="verificationcode"
						  clearable
						  placeholder="验证码"
						  autocomplete="off"
						  style="width: 214px;"
				>
				</fb-input>

				<fb-button
					class="phone-code-btn"
					type="primary"
					size="s"
					:disabled="shortBtnDisabled"
					@on-click="doShortMsg">
					{{shortBtnText}}
				</fb-button>

			</fb-form-item>

			<fb-form-item>

				<fb-button
					type="primary"
					size="l"
					long
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
				shortBtnText: '获取验证码',
				shortBtnDisabled: false,
				loginForm: {
					// 手机验证
					mobile: '',
					mobileCode: ''
				},
			}
		},

		watch: {
			shortBtnDisabled (newVal) {
				if (newVal) {
					this.countDown()
				}
			}
		},

		mounted() {
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
			doShortMsg () {
				if (!this.shortBtnDisabled) {
					this.shortBtnDisabled = true
					this.countDown()
				}
				this.$emit('doShortMsg')
			},
			countDown () {
				let num = 60
				this.shortBtnText = num + '秒后重发'
				let timer = setInterval(() => {
					num -= 1
					this.shortBtnText = num + '秒后重发'
					if (num <= 0) {
						this.shortBtnText = '获取验证码'
						this.shortBtnDisabled = false
						clearInterval(timer)
					}
				}, 1000)
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
	.phone-code-btn {
		width: 90px;
		height: 36px;
		margin-left: 8px;
	}
</style>
