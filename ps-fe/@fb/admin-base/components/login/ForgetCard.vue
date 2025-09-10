<!-- 拖拽验证 -->
<template>
	<transition name="slide-to-down">
		<div class="forget-card">
			<div class="login-form">
				<div class="login-form__caption">
					密码找回
				</div>
				<div class="box" v-show="!boxCode">
					<div class="title">验证码将会发送至您注册的手机号</div>
					<fb-form :model="forgetForm0" ref="forgetOne" autocomplete="off">
						<fb-form-item prop="phone" label="手机号" :showLabel="false"
									  :rule='[{required:true}, {type: "mobile"}]'>
							<fb-input v-model="forgetForm0.phone"
									  size="l"
									  prepend-icon="mobilephone"
									  clearable
									  placeholder="请输入手机号"
									  autocomplete="off"
									maxlength="11"	
							/>
						</fb-form-item>

						<fb-form-item>
							<fb-button
								type="primary"
								size="l"
								long
								@on-click="nextOne">
								下一步
							</fb-button>
						</fb-form-item>

					</fb-form>

					<fb-link type="primary" label="想起来了，去登录>>" @click="toLogin"></fb-link>
				</div>

				<div class="box" v-show="boxCode === 1">
					<div class="title">验证码已发送至您的手机</div>
					<fb-form :model="forgetForm1" :rule="rules1" ref="forgetTwo" autocomplete="off">
						<fb-form-item prop="username" label="用户名" :showLabel="false"
									  :rule='[{required:true}]'>
							<fb-input v-model="forgetForm1.username" prepend-icon="user" size="l" disabled
									  placeholder="用户名"/>
						</fb-form-item>
						<fb-form-item prop="mobileCode" label="验证码" :showLabel="false"
									  :rule='[{required: true}, {type: "string", len: 6}]'>
							<fb-input v-model="forgetForm1.mobileCode"
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
								@on-click="doForgetShortMsg">
								{{shortBtnText}}
							</fb-button>
						</fb-form-item>
						<fb-form-item prop="password" label="密码" :showLabel="false"
									  :rule='[{required:true},{type: "password"}]'>
							<fb-input v-model="forgetForm1.password" prepend-icon="password" size="l" type="password"
									  placeholder="请输入8位以上且必须包含数字,字母,特殊符号的两种及以上的密码"/>
						</fb-form-item>
						<fb-form-item prop="confirmPass" label="密码" :showLabel="false" style="padding-bottom: 40px">
							<fb-input v-model="forgetForm1.confirmPass" prepend-icon="password" size="l" type="password"
									  placeholder="请再次输入密码"/>
						</fb-form-item>
						<fb-form-item>
							<fb-button
								type="primary"
								size="l"
								long
								:loading="inForgetProcess"
								@on-click="doForgetFinish">
								重置密码
							</fb-button>
						</fb-form-item>

					</fb-form>
				</div>

			</div>
		</div>
	</transition>
</template>

<script>
	/**
	 * DefaultApplicationLogin
	 * (c) 2020 lincong1987
	 */
	export default {

		components: {},

		props: {
			inForgetProcess: {
				type: Boolean,
				default: false
			}
		},

		data() {
			return {
				// 0 = 发送  1 = 重置
				boxCode: 0,
				isVerify: false,
				shortBtnText: '获取验证码',
				shortBtnDisabled: false,
				forgetForm0: {
					phone: ''
				},
				forgetForm1: {
					username: 'admin',
					mobileCode: '',
					password: '',
					confirmPass: ''
				},
				rules1: {
					// 校验器名称（必要，唯一，可任意）
					"confirmPass": {
						// 自定义 校验方法，方法名与参数固定不变
						validator: (rule, value, callback, source, options) => {
							// 可通过 _this 获取上下文
							if (value !== this.forgetForm1.password) {
								// 校验未通过，返回错误信息
								return callback('两次输入的密码不一致，请重新输入');
							} else {
								// 校验通过，返回空参数
								return callback();
							}
						}
					}
				},
			}
		},

		mounted() {
		},

		methods: {
			toLogin() {
				this.$emit('toLogin')
			},
			nextOne() {
				this.$refs.forgetOne.validate((result, error) => {
					if (result) {
						this.$svc.sys.person.accountFindpwdByPhone({"phone": this.forgetForm0.phone}).then(json => {
							// 登录按钮 loading
							if (json && json.code == 1) {
								this.boxCode = 1
								this.countDown()
								this.forgetForm1.username = json.data
								this.$message.success('请注意查收验证码')
							} else {
								this.$message.error(json.message)
							}
						})
					} else {
						this.$message.warn('请输入正确的手机号')
					}
				})
			},
			doForgetShortMsg() {
				if (!this.shortBtnDisabled) {
					this.$svc.sys.person.accountFindpwdByPhone({"phone": this.forgetForm0.phone}).then(json => {
						// 登录按钮 loading
						if (json && json.code == 1) {
							this.$message.success('请注意查收验证码')
						} else {
							this.$message.error(json.message)
						}
					})
					this.countDown()
				}
				this.$emit('doForgetShortMsg', this.forgetForm0)
			},
			countDown() {
				this.shortBtnDisabled = true
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
			doForgetFinish() {
				this.$refs.forgetTwo.validate((result, error) => {
					if (result) {
						this.$svc.sys.person.checkVcode({
							"phone": this.forgetForm0.phone,
							"vcode": this.forgetForm1.mobileCode,
							"userpwd": this.forgetForm1.confirmPass
						}).then(json => {
							// 登录按钮 loading
							if (json && json.code == 1) {
								this.$emit('doForgetFinish', this.forgetForm1)
								this.boxCode = 0
								this.$refs.forgetOne.resetFields()
								this.$refs.forgetTwo.resetFields()
								this.$message.success(json.data)
							} else {
								this.$message.error(json.data)
							}
						})
					} else {
						this.$message.warn('请填写正确的信息')
					}
				})
			}
		}
	}
</script>

<style lang="less" scoped>
	@import ~"../../assets/styles/common";

	.forget-card {
		.loginCardCom();

		.login-form {

			padding: 0 24px;

			.@{FbUiPrefix}-form-item {
				padding-bottom: 24px;
			}

			&__caption {
				font-size: 24px;
				color: #313C47;
				line-height: 32px;
				padding-top: 32px;
				padding-bottom: 16px;
				text-align: center;
			}

			.box {
				.title {
					font-size: 14px;
					color: #666666;
					line-height: 22px;
					text-align: center;
					margin-bottom: 24px;
				}

				.btn-sp-blue {
					font-size: 13px;
					color: #0284FE;
					line-height: 22px;
					cursor: pointer;

					&:hover {
						opacity: .7;
					}
				}
			}
		}

	}

	.phone-code-btn {
		width: 90px;
		height: 36px;
		margin-left: 8px;
	}


</style>
