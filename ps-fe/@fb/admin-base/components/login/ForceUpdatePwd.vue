<!-- 选择部门 -->
<template>
	<transition name="slide-to-down">
		<div class="login-dialog">
			<div class="force-updatepwd-form">

				<div class="force-updatepwd-form__caption">
					修改密码
				</div>

				<fb-form ref="fbform" :rule="rules">
					<fb-form-item prop="userpwd" label="新密码" :showLabel="false"
								  :rule="[{required: true }, {type: 'password'}]">
						<fb-input v-model="formData.userpwd"
								  size="l"
								  type="password"
								  prepend-icon="password"
								  clearable
								  placeholder="请输入新密码"
								  autocomplete="off"
						>
						</fb-input>
					</fb-form-item>

					<fb-form-item prop="confirmUserPwd" label="确认密码" :showLabel="false"
								  :rule="[{required: true }, {type: 'password'}]">
						<fb-input v-model="formData.confirmUserPwd"
								  size="l"
								  type="password"
								  prepend-icon="password"
								  clearable
								  placeholder="请输入确认密码"
								  autocomplete="off"
						>
						</fb-input>
					</fb-form-item>
				</fb-form>
				<div class="force-updatepwd-form__tag">
					密码已被重置，请重新设置自己独有的密码。
				</div>
				<fb-button
					type="primary"
					size="l"
					long
					@on-click="fUpdatePwd">
					确 定
				</fb-button>
			</div>
		</div>
	</transition>
</template>

<script>

	export default {
		name: 'update-pwd',
		// 接收父组件的传参
		props: {
			param: {
				type: Object,
				require: false
			},
			parentPage: {
				type: Object,
				default: null
			}
		},
		// 组件
		components: {
			// 'component-a': ComponentA,
		},
		// 创建方法
		created() {
		},
		// 初始化方法
		mounted() {
			// 执行界面初始化
		},
		data() {
			return {
				// 请求的 service
				// service: this.$svc.sys.account,
				rules: {
					"confirmUserPwd": {
						validator: (rule, value, callback, source, options) => {
							// 比较两控件的值
							if (this.formData.userpwd != this.formData.confirmUserPwd) {
								// 校验未通过，返回错误信息
								return callback('确认密码不一致！');
							}
							// 校验通过
							return callback();
						}
					}
				},

				// 表单数据
				formData: {
					userpwd: '',
					confirmUserPwd: '',
				},
			}
		},

		// 方法
		methods: {
			fUpdatePwd() {
				// 新
				this.$refs['fbform'].validate((result, error) => {
					if (result) {
						this.$emit('doUpdatePwd', this.formData)
					}
				})
			},

		}
	}
</script>

<style lang="less" scoped>

	@import ~"../../assets/styles/common";

	.login-dialog {
		.loginCardCom();

		.force-updatepwd-form__caption {
			margin-bottom: 36px;
		}

		.force-updatepwd-form__tag {
			margin-top: 16px;
			font-size:   10px;
			color:       #FF2600;
			text-align:  center;
		}
	}

	.force-updatepwd-form {
		padding: 0 24px;

		.@{FbUiPrefix}-form-item {
			padding-bottom: 24px;
		}

		&__caption {
			height:      32px;
			font-size:   24px;
			color:       #313C47;
			line-height: 32px;
			padding:     32px 0;
			text-align:  center;
		}

		.@{FbUiPrefix}-button {
			margin-top: 64px;
		}
	}

</style>
