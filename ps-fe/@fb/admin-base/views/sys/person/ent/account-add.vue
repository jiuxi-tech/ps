<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-form ref="fbform" :label-width="140">
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="账号" prop="userName" :rule="[{required: true}]">
							<fb-input v-model="userName" placeholder="请输入账号"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row v-if="!deptShowFlag">
					<fb-col span="24">
						<fb-form-item label="密码" prop="userPwd" :rule="passwordRules">
							<fb-input v-model="userPwd" placeholder="请输入密码" type="password"></fb-input>
							<div v-if="passwordPolicyDesc" class="password-policy-hint">{{ passwordPolicyDesc }}</div>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="账号过期时间">
							<tp-datepicker v-model="formData.expiredTime" format="YYYY-MM-DD"
										   value-format="YYYYMMDD"></tp-datepicker>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="是否锁定">
							<fb-radio-group v-model="formData.locked"
											:data="[{id: 1, name: '是',}, {id: 0, name: '否',}]"
											:reader="{label:'name', value:'id'}" disabled></fb-radio-group>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item label="是否有效">
							<fb-radio-group v-model="formData.enabled"
											:data="[{id: 1, name: '是',}, {id: 0, name: '否',}]"
											:reader="{label:'name', value:'id'}" disabled></fb-radio-group>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row v-if="deptShowFlag">
					<fb-col span="24">
						<fb-form-item label="微信">
							<fb-input v-model="formData.weixin" readonly></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row v-if="deptShowFlag">
					<fb-col span="24">
						<fb-form-item label="钉钉">
							<fb-input v-model="formData.dingding" readonly></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row v-if="deptShowFlag">
					<fb-col span="24">
						<fb-form-item label="第三方认证ID">
							<fb-input v-model="formData.threeId"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
			</fb-form>
		</div>

		<div class="tp-dialog-bottom">
			<fb-button style="margin-right: 12px" type="primary" @on-click="add">保存</fb-button>
			<fb-button style="margin-right: 12px" type="primary" v-if="deptShowFlag" @on-click="resetpwd">密码重置
			</fb-button>
			<fb-button style="margin-right: 12px" v-if="this.formData.locked === 0 && deptShowFlag" danger
					   @on-click="locked(1)">锁定
			</fb-button>
			<fb-button style="margin-right: 12px" type="link" v-if="this.formData.locked === 1"
					   @on-click="locked(0)">解锁
			</fb-button>
			<fb-button style="margin-right: 12px" v-if="this.formData.enabled === 1 && deptShowFlag" danger
					   @on-click="enabled(0)">停用
			</fb-button>
			<fb-button style="margin-right: 12px" type="link" v-if="this.formData.enabled === 0" @on-click="enabled(1)">
				启用
			</fb-button>
			<fb-button @on-click="handleClose">关闭</fb-button>
		</div>

		<tp-dialog ref="TpDialog"></tp-dialog>
	</div>
</template>

<script>
	import { createPasswordValidator, getPasswordPolicyDescription } from '../../../../util/passwordPolicyUtil';

	export default {
		name: 'account-add',
		mixins: [

		],
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
		// 创建方法
		created() {
		},
		// 初始化方法
		async mounted() {
			// 执行界面初始化
			this.init(this.param);
			// 加载密码策略
			await this.loadPasswordPolicy();
		},
		data() {
			return {
				// 请求的 service
				service: this.$svc.sys.person,
				deptShowFlag: false,

				userName: '',
				userPwd: '',

				// 密码策略相关
				passwordRules: [{required: true}],
				passwordPolicyDesc: '',

				// 表单数据
				formData: {
					personId: '',
					accountId: '',
					username: '',
					userpwd: '',
					confirmUserPwd: '',
					expiredTime: '',
					locked: 0,
					enabled: 1,
					weixin: '',
					dingding: '',
					threeId: '',
				},
			}
		},

		// 方法
		methods: {
			// 初始化参数
			init(param) {
				let personId = param.id;
				this.formData.personId = personId;
				this.view(personId);
			},

			// 取消
			handleClose() {
				// 关闭，并传递参数
				this.closeTpDialog("param");
			},

			// 新增
			add() {
				// 界面校验
				this.$refs.fbform.validate((result) => {
					if (result === true) {
						this.formData.username = this.userName;
						this.formData.userpwd = this.userPwd;
						this.formData.passKey = this.param.passKey;
						// 新增/修改，调用service方法，后台通过this.formData.accountId来区分是新增还是修改
						this.service.accountManage(this.formData).then((result) => {
							// 判断code
							if (result.code == 1) {
								this.$message.success('新增成功');
								// 关闭，并传递参数
								this.handleClose();
							} else {
								// 服务器返回失败
								this.$message.error('新增失败')
							}
						})
					}
				})
			},

			resetpwd() {
				this.service.accountResetpwd({"accountId": this.formData.accountId, "passKey": this.param.passKey}).then((result) => {
					// 判断code
					if (result.code == 1) {
						// this.$message.success('重置成功，新密码：' + result.data);
						let options = {
							height: 400,
							callBack: function (result) {

							}
						}
						this.$refs.TpDialog.show(import('../../../../views/sys/person/ent/account-restpwd.vue'), result, "重置密码", options);
					} else {
						// 服务器返回失败
						this.$message.error('重置失败')
					}
				}).catch((err) => {
					// 服务器返回失败
					console.log(err);
				})
			},

			locked(locked) {
				this.service.accountLocked({"accountId": this.formData.accountId, "locked": locked, "passKey": this.param.passKey}).then((result) => {
					// 判断code
					if (result.code == 1) {
						if (locked === 0) {
							this.$message.success('已解锁');
						} else {
							this.$message.success('已锁定');
						}
						this.formData.locked = locked;
					} else {
						// 服务器返回失败
						this.$message.error('失败')
					}
				}).catch((err) => {
					// 服务器返回失败
					console.log(err);
				})
			},

			enabled(enabled) {
				this.service.accountEnabled({
					"accountId": this.formData.accountId,
					"enabled": enabled,
					"passKey": this.param.passKey
				}).then((result) => {
					// 判断code
					if (result.code == 1) {
						if (enabled === 0) {
							this.$message.success('已停用');
						} else {
							this.$message.success('已启用');
						}
						this.formData.enabled = enabled;
					} else {
						// 服务器返回失败
						this.$message.error('失败')
					}
				}).catch((err) => {
					// 服务器返回失败
					console.log(err);
				})
			},

			view(personId) {
				// 调用新增service方法
				this.service.accountView({"personId": personId, "passKey": this.param.passKey}).then((result) => {
					// 判断code
					if (result.code == 1) {
						if (result.data.accountId) {
							// 修改
							this.deptShowFlag = true;
							this.formData = result.data
							this.userName = result.data.username
							if (typeof result.data.userpwd !== 'undefined') {
								this.userPwd = result.data.userpwd
							}
							// 检查密码过期状态
							this.checkPasswordExpiry(result.data);
						} else {
							// 新增
							this.deptShowFlag = false;
						}
					} else {
						// 服务器返回失败
						this.$message.error('查询失败')
					}

				}).catch((err) => {
					// 服务器返回失败
					console.log(err);
				})
			},

			// 加载密码策略
			async loadPasswordPolicy() {
				console.log('[account-add] 开始加载密码策略...');
				try {
					// 获取密码验证规则
					const passwordValidator = await createPasswordValidator(this.userName);
					this.passwordRules = [
						{required: true},
						passwordValidator
					];
					// 获取密码策略描述
					this.passwordPolicyDesc = await getPasswordPolicyDescription();
					console.log('[account-add] 密码策略加载成功:', this.passwordPolicyDesc);
					console.log('[account-add] 密码验证规则:', this.passwordRules);
				} catch (error) {
					console.error('[account-add] 加载密码策略失败:', error);
					// 降级到最基本的必填验证，不使用旧的password规则
					this.passwordRules = [{required: true}];
					this.passwordPolicyDesc = '密码策略加载失败，请确保密码符合安全要求';
				}
			},

			// 检查密码过期状态
			checkPasswordExpiry(accountData) {
				if (!accountData.lastPasswordChangeTime) {
					return;
				}

				// 获取系统配置
				Promise.all([
					this.$svc.sys.config.getConfigValue('password.validity.months'),
					this.$svc.sys.config.getConfigValue('password.expiry.reminder.days')
				]).then(([validityResult, reminderResult]) => {
					const validityMonths = validityResult.data ? parseInt(validityResult.data) : 3;
					const reminderDays = reminderResult.data ? parseInt(reminderResult.data) : 7;

					// 计算密码过期时间
					const lastChangeTime = new Date(accountData.lastPasswordChangeTime);
					const expiryTime = new Date(lastChangeTime);
					expiryTime.setMonth(expiryTime.getMonth() + validityMonths);

					const now = new Date();
					const timeDiff = expiryTime.getTime() - now.getTime();
					const daysDiff = Math.ceil(timeDiff / (1000 * 3600 * 24));

					if (daysDiff <= 0) {
						// 密码已过期
						this.$message.error(`该账号密码已过期，请立即重置密码！`);
					} else if (daysDiff <= reminderDays) {
						// 密码即将过期
						this.$message.warn(`该账号密码将在${daysDiff}天后过期，建议尽快重置密码！`);
					}
				}).catch(err => {
					console.warn('获取密码配置失败:', err);
				});
			},

		}
	}
</script>

<style lang="less" scoped>
	.password-policy-hint {
		font-size: 12px;
		color: #999;
		margin-top: 4px;
		line-height: 1.5;
	}
</style>
