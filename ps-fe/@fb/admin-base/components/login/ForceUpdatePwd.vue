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
								  :rule="passwordRules">
						<fb-input v-model="formData.userpwd"
								  size="l"
								  type="password"
								  prepend-icon="password"
								  clearable
								  placeholder="请输入新密码"
								  autocomplete="off"
						>
						</fb-input>
						<div v-if="passwordPolicyDesc" class="password-policy-hint">{{ passwordPolicyDesc }}</div>
					</fb-form-item>

					<fb-form-item prop="confirmUserPwd" label="确认密码" :showLabel="false"
								  :rule="[{required: true }]">
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
	import { createPasswordValidator, getPasswordPolicyDescription } from '../../util/passwordPolicyUtil';

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
		async mounted() {
			// 执行界面初始化
			// 加载密码策略
			await this.loadPasswordPolicy();
		},
		data() {
			return {
				// 请求的 service
				// service: this.$svc.sys.account,
				
				// 密码策略相关
				passwordRules: [{required: true}],
				passwordPolicyDesc: '',
				
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

			// 加载密码策略
			async loadPasswordPolicy() {
				try {
					// 获取密码验证规则
					const passwordValidator = await createPasswordValidator();
					this.passwordRules = [
						{required: true},
						passwordValidator
					];
					// 获取密码策略描述
					this.passwordPolicyDesc = await getPasswordPolicyDescription();
					console.log('密码策略加载成功:', this.passwordPolicyDesc);
				} catch (error) {
					console.error('加载密码策略失败:', error);
					// 降级到最基本的必填验证，不使用旧的password规则
					this.passwordRules = [{required: true}];
					this.passwordPolicyDesc = '密码策略加载失败，请确保密码符合安全要求';
				}
			},

		}
	}
</script>

<style lang="less" scoped>

	@import "../../assets/styles/common";

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
		
		.password-policy-hint {
			font-size: 12px;
			color: #999;
			margin-top: 4px;
			line-height: 1.5;
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
