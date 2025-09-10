<!-- 注册步骤验证 -->
<template>
	<transition name="slide-to-down">
		<div class="resgister-dialog">
			<fb-steps :current="process">
				<fb-step v-for="item in steps" :key="item.label" :label="item.label"/>
			</fb-steps>

			<div class="form-box" v-show="process === 0">
				<fb-form ref="registerForm" :rule="rules" autocomplete="off">
					<fb-form-item label="注册方式" :label-width="120"
								  prop="regmethod" :rule='[{required:true}]'
					>
						<fb-radio-group
							v-model="registerForm.regmethod"
							button
							:data="checkData"
							@on-change="handleRadio"
						/>
					</fb-form-item>
					<fb-form-item label="工商注册号" :label-width="120"
								  prop="bizcode" :rule='[{required:true},{type: "bizcode"}]'
					>
						<fb-input v-model="registerForm.bizcode" placeholder="请输入企业13位工商注册号"/>
					</fb-form-item>
					<fb-form-item label="企业名称" :label-width="120"
								  prop="companyName" :rule='[{required:true}]'
					>
						<fb-input v-model="registerForm.companyName" placeholder="请输入企业全称"/>
					</fb-form-item>
					<fb-form-item label="用户名" :label-width="120"
								  prop="username" :rule='[{required:true},{type: "username"}]'
					>
						<fb-input v-model="registerForm.username" placeholder="请输入用户名，用于登录时使用"/>
					</fb-form-item>
					<fb-form-item label="密码" :label-width="120"
								  prop="password" :rule='[{required:true},{type: "password"}]'
					>
						<fb-input v-model="registerForm.password" type="password" placeholder="请输入8位以上且必须包含数字,字母,特殊符号的两种及以上的密码"/>
					</fb-form-item>
					<fb-form-item label="确认密码" :label-width="120"
								  prop="confirmPass"
					>
						<fb-input v-model="registerForm.confirmPass" type="password" placeholder="请再次输入密码"/>
					</fb-form-item>
					<fb-form-item label="手机号" :label-width="120"
								  prop="mobile" :rule='[{required:true},{type: "mobile"}]'
					>
						<fb-input v-model="registerForm.mobile" placeholder="请输入手机号码"/>
					</fb-form-item>

				</fb-form>

				<div class="footer">
					<fb-drag-verify v-model="isVerify"></fb-drag-verify>
					<div style="margin-top: 24px;">
						<fb-button @on-click="doRegister" type="primary" size="l">注 册</fb-button>
						<span>已有账号？<fb-link type="primary" label="去登录" @click="toLogin"></fb-link></span>
					</div>
				</div>
			</div>

			<div class="result-box" v-show="process === 1">
				<fb-result message="注册成功">
					<template v-slot:describe>
						<p>用户名密码已经以短信的形式发送至您注册的手机，请注意查收</p>
						<p>现在去登录系统完善企业信息</p>
					</template>
				</fb-result>
				<div style="margin-top: 64px; text-align: center;">
					<fb-button @on-click="toLogin" type="primary" size="l">立即登录</fb-button>
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

		props: {
			inLoginProcess: {
				type: Boolean,
				default: false
			}
		},

		data() {
			return {
				process: 0,
				steps: [
					{
						label: '账号注册',
					},
					{
						label: '登录系统',
					},
					{
						label: '完善信息',
					},
					{
						label: '信息审核',
					},
				],
				checkData: [
					{
						value: '1',
						label: '统一社会信用代码',
					}/*,
					{
						value: '2',
						label: '本工商注册号',
					},
					{
						value: '3',
						label: '组织机构代码',
					},*/
				],
				registerForm: {
					regmethod: '1',
					bizcode: '',
					companyName: '',
					username: '',
					password: '',
					confirmPass: '',
					mobile: '',
				},
				rules: {
					// 校验器名称（必要，唯一，可任意）
					"confirmPass": {
						// 自定义 校验方法，方法名与参数固定不变
						validator: (rule, value, callback, source, options) => {
							// 可通过 _this 获取上下文
							if (value !== this.registerForm.password) {
								// 校验未通过，返回错误信息
								return callback('两次输入的密码不一致，请重新输入');
							} else {
								// 校验通过，返回空参数
								return callback();
							}
						}
					}
				},
				isVerify: false,

			}
		},

		methods: {
			doRegister() {
				if (!this.isVerify) {
					this.$message.error('请拖拽验证')
					return
				}

				this.$refs.registerForm.validate((result, error) => {
					console.log(result, error, '注册表单验证')
					if (result) {
						// 请求后台，注册企业信息
						this.$refs.fbform.validate((result) => {
							if (result === true) {
								// 调用新增service方法
								that.service.add(that.formData).then((result) => {
									// 判断code
									if (result.code == 1) {
										that.$message.success('新增成功');
										let entId = result.data;
										that.formData.entId = entId;
										that.setPageParam(entId);
									} else {
										// 服务器返回失败
										that.$message.error('新增失败:' + result.message)
									}
								})
							}

						})

						this.$emit('doRegister', this.registerForm)
					} else {
						this.$message.error('请正确填写表单信息')
					}
				})
			},
			handleRadio() {

			},
			toLogin() {
				this.$emit('toLogin')
			},
			destroy() {

			}
		},

		destroyed() {
			this.destroy()
		},
		deactivated() {
			this.destroy()
		}

	}
</script>

<style lang="less" scoped>
	@import ~"../../assets/styles/common";

	.resgister-dialog {
		width: 100%;
		height: 100%;
		background: #FFFFFF;
		box-shadow: 0px 2px 8px 0px rgba(0, 0, 0, 0.15);
		border-radius: 4px;
		padding: 32px 106px;
		padding-right: 0;

		.form-box {
			margin-top: 20px;
			padding-right: 386px;

			/deep/ .@{FbUiPrefix}-radio-group--button .@{FbUiPrefix}-radio {
				width: 129px;
				text-align: center;
			}

			.footer {
				padding-left: 120px;
				margin-top: 6px;

				.@{FbUiPrefix}-button.@{FbUiPrefix}-button--primary {
					width: 200px;
					margin-right: 16px;
				}

				span {
					span {
						margin-left: 8px;
						color: #0284FE;
						cursor: pointer;

						&:hover {
							opacity: .7;
						}
					}
				}
			}
		}

		.result-box {
			margin-top: 60px;
			padding-right: 106px;

			.@{FbUiPrefix}-button.@{FbUiPrefix}-button--primary {
				width: 200px;
			}
		}
	}

</style>
