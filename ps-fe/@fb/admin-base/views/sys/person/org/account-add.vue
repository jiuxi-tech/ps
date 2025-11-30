<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-form ref="fbform" :label-width="140" autocomplete="off">
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="账号" prop="userName" :rule="[{required: true}]">
							<fb-input autocomplete="nope" v-model="userName" placeholder="请输入账号"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row v-if="!deptShowFlag">
					<fb-col span="24">
						<fb-form-item label="密码" prop="userPwd" :rule="passwordRules">
							<fb-input autocomplete="new-password" v-model="userPwd" placeholder="请输入密码" type="password"></fb-input>
							<div v-if="passwordPolicyDesc" class="password-policy-hint">{{ passwordPolicyDesc }}</div>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="账号过期时间">
							<tp-datepicker v-model="formData.expiredTime" format="YYYY-MM-DD" value-format="YYYYMMDD"
										   clearable></tp-datepicker>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="是否锁定">
							
							<fb-flex pt="8px">{{ formData && formData.locked === 1 ? '已锁定' : '未锁定' }}</fb-flex>
							<fb-radio-group v-show="false" v-model="formData.locked"
											:data="[{id: 1, name: '是',}, {id: 0, name: '否',}]"
											:reader="{label:'name', value:'id'}" disabled></fb-radio-group>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item label="是否启用">
							<fb-flex pt="8px">{{ formData.enabled === 1 ? '已启用' : '已停用' }}</fb-flex>
							
							<fb-radio-group v-show="false" v-model="formData.enabled"
											:data="[{id: 1, name: '是',}, {id: 0, name: '否',}]"
											:reader="{label:'name', value:'id'}" disabled></fb-radio-group>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="多因子认证">
							<fb-checkbox-group :value="['1', '2']"
							disabled
											   :data="[{label: 'SSO登录', value: '1'}, {label: '密码登录', value: '2'}, {label: '短信登录', value: '3'}]">
							</fb-checkbox-group>
						</fb-form-item>
					</fb-col>
				</fb-row>
				

				<!-- <fb-row v-if="deptShowFlag">
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
				</fb-row> -->
			</fb-form>
		</div>

		<div class="tp-dialog-bottom">
			<fb-button style="margin-right: 12px" type="primary" @on-click="add">保存</fb-button>
			<fb-button style="margin-right: 12px" type="default" v-if="deptShowFlag" @on-click="syncToSSO">同步SSO</fb-button>
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
					multiFactorAuth: [],
				},
			}
		},

		// 方法
		methods: {

/**
		 * 记录用户操作日志信息
		 * @param {string} operateType - 操作类型：login/logout/add/delete/edit/query/pass/unpass等
		 * @param {string} message - 错误日志信息
		 */
		logInfo(operateType, message) {
			// 获取用户信息
			let userInfo = app.$datax.get('userInfo')

			// 构建日志数据对象
			let data = {
				// 模块编码
				moduleCode: 'user',
				// 模块名称
				moduleName: '用户',
				// 模块编码
				moduleCode: 'user',
				// 模块名称
				// 模块名称
				moduleName: '用户',
				// 操作类型： login/logout/add/delete/edit/query/pass/unpass, 可以自己定义
				operateType,
				// 操作人id
				operterId: userInfo.personId,
				// 操作人名称
				operterName: userInfo.personName,
				// 错误日志信息
				message
			}


			// 日志埋点
			app.$logger.send(data);
		},

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
						this.formData.username = this.userName
						this.formData.userpwd = this.userPwd
						this.formData.passKey = this.param.passKey;
						// 新增/修改，调用service方法，后台通过this.formData.accountId来区分是新增还是修改
						this.service.accountManage(this.formData).then((result) => {
							// 判断code
							if (result.code == 1) {
								if (this.formData.accountId){
									this.$message.success('修改成功！');
								} else {
									this.$message.success('新增成功！请为新账号授权，否则无法登录！');
								}
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
						let options = {
							height: 400,
							callBack: (result)=> {

							}
						}
						this.$refs.TpDialog.show(import('../../../../views/sys/person/org/account-restpwd.vue'), result, "重置密码", options);

						this.logInfo('resetpwd', '重置密码');

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
							this.logInfo('unlocked', '解锁');
						} else {
							this.$message.success('已锁定');
							this.logInfo('locked', '锁定');
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
					"passKey": this.param.passKey,
				}).then((result) => {
					// 判断code
					if (result.code == 1) {
						if (enabled === 0) {
							this.$message.success('已停用');
							this.logInfo('disabled_account', '停用');
						} else {
							this.$message.success('已启用');
							this.logInfo('enabled_account', '启用');
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

			// 同步到SSO
			syncToSSO() {
				if (!this.formData.accountId) {
					this.$message.warning('请先保存账号信息');
					return;
				}

				this.$confirm('确认将此账号同步到Keycloak SSO系统吗？',   () => {
					// 调用同步接口
					this.service.syncAccountToKeycloak({
						"accountId": this.formData.accountId,
						"passKey": this.param.passKey
					}).then((result) => {
						if (result.code == 1) {
							this.$message.success('同步SSO成功！');
							this.logInfo('sync_sso', '同步SSO');
						} else {
							this.$message.error(result.message || '同步SSO失败');
						}
					}).catch((err) => {
						console.log(err);
						this.$message.error('同步SSO失败');
					});
				});
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
						} else {
							// 新增
							this.deptShowFlag = false;
						}
					} else {
						// 服务器返回失败
						this.$message.error('查询失败')
					}

				})
			},

			// 加载密码策略
			async loadPasswordPolicy() {
				console.log('[org/account-add] 开始加载密码策略...');
				try {
					// 获取密码验证规则
					const passwordValidator = await createPasswordValidator(this.userName);
					this.passwordRules = [
						{required: true},
						passwordValidator
					];
					// 获取密码策略描述
					this.passwordPolicyDesc = await getPasswordPolicyDescription();
					console.log('[org/account-add] 密码策略加载成功:', this.passwordPolicyDesc);
					console.log('[org/account-add] 密码验证规则:', this.passwordRules);
				} catch (error) {
					console.error('[org/account-add] 加载密码策略失败:', error);
					// 降级到最基本的必填验证，不使用旧的password规则
					this.passwordRules = [{required: true}];
					this.passwordPolicyDesc = '密码策略加载失败，请确保密码符合安全要求';
				}
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
