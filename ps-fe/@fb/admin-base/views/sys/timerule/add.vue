<template>
	<div>
		<fb-form ref="form" :model="formData" :rules="rules" :label-width="120">
			<fb-row>
				<fb-col span="12">
					<fb-form-item label="规则名称" prop="ruleName">
						<fb-input v-model="formData.ruleName" placeholder="请输入规则名称"></fb-input>
					</fb-form-item>
				</fb-col>
				<fb-col span="12">
					<fb-form-item label="是否允许登录" prop="allowLogin">

						<fb-radio-group v-model="formData.allowLogin"
							:data="[{ value: 1, label: '允许' }, { value: 0, label: '拒绝' }]">
						</fb-radio-group>
					</fb-form-item>
				</fb-col>
			</fb-row>

			<fb-row>
				<fb-col span="12">
					<fb-form-item label="开始时间" prop="startTime">
						<tp-datepicker v-model="formData.startTime" format="YYYY-MM-DD HH:mm:ss"
							value-format="YYYYMMDDHHmmss" placeholder="请选择开始时间">
						</tp-datepicker>
					</fb-form-item>
				</fb-col>
				<fb-col span="12">
					<fb-form-item label="结束时间" prop="endTime">
						<tp-datepicker v-model="formData.endTime" format="YYYY-MM-DD HH:mm:ss"
							value-format="YYYYMMDDHHmmss" placeholder="请选择结束时间">
						</tp-datepicker>
					</fb-form-item>
				</fb-col>
			</fb-row>
			<fb-row>
				<fb-col span="24">
					<fb-form-item label="适用人员" prop="userIds">
						<fb-input v-model="formData.userNames" placeholder="请选择适用人员" @on-click="handleUserSelect"
							readonly style="background-color: #fff; cursor: pointer;"></fb-input>
						<fb-input v-model="formData.userIds" style="display: none;"></fb-input>
					</fb-form-item>
				</fb-col>
			</fb-row>
			<fb-row>
				<fb-col span="24">
					<fb-form-item label="适用角色" prop="roleIds">

						<fb-input v-model="formData.roleNames" placeholder="请选择适用角色" @on-click="showRoleDialog" readonly
							style="background-color: #fff; cursor: pointer;"></fb-input>
						<fb-input v-model="formData.roleIds" style="display: none;"></fb-input>

					</fb-form-item>
				</fb-col>
			</fb-row>
			<fb-row>
				<fb-col span="24">
					<fb-form-item label="备注" prop="remark">
						<fb-textarea v-model="formData.remark" :rows="3" placeholder="请输入备注信息"></fb-textarea>
					</fb-form-item>
				</fb-col>
			</fb-row>
			<fb-row>
				<fb-col span="12">
					<fb-form-item label="是否启用" prop="status">
						<fb-radio-group v-model="formData.status"
							:data="[{ value: 1, label: '启用' }, { value: 0, label: '禁用' }]">
						</fb-radio-group>
					</fb-form-item>
				</fb-col>

			</fb-row>
		</fb-form>

		<div class="form-actions">
			<fb-button @on-click="handleSave" type="primary" :loading="saving">保存</fb-button>
			<fb-button @on-click="handleClose">取消</fb-button>
		</div>

		<!-- 弹窗组件 -->
		<tp-dialog ref="TpDialog"></tp-dialog>
	</div>
</template>

<script>

export default {
	components: {},
	name: 'TimeRuleAdd',
	props: {
		id: {
			type: String,
			default: ''
		},
		param: {
			type: Object,
			default: () => ({})
		},
		parentPage: {
			type: Object,
			default: () => ({})
		}
	},
	data() {
		return {
			saving: false,
			roleLoading: false,
			userLoading: false,
			roleOptions: [],
			userOptions: [],
			formData: {
				ruleName: '',
				startTime: '',
				endTime: '',
				roleIds: '',
				roleNames: '',
				roleIdsArray: [],
				userIds: '',
				userNames: '',
				userIdsArray: [],
				status: '1', // 默认启用
				allowLogin: '1', // 默认允许
				remark: ''
			},
			rules: {
				ruleName: [
					{ required: true, message: '请输入规则名称', trigger: 'blur' },
					{ min: 2, max: 50, message: '规则名称长度在 2 到 50 个字符', trigger: 'blur' }
				],
				startTime: [
					{ required: true, message: '请选择开始时间', trigger: 'change' }
				],
				endTime: [
					{ required: true, message: '请选择结束时间', trigger: 'change' }
				],
				allowLogin: [
					{ required: true, message: '请选择是否允许登录', trigger: 'change' }
				],
				roleIds: [
					{ required: true, message: '请至少选择一个角色', trigger: 'blur' }
				],
				status: [
					{ required: true, message: '请选择规则状态', trigger: 'change' }
				]
			}
		}
	},
	mounted() {
		this.init();
	},
	watch: {
		// 监听数组变化，同步到字符串字段
		// 'formData.roleIdsArray': {
		// 	handler(newVal) {
		// 		this.formData.roleIds = newVal.join(',');
		// 		// 同步角色名称
		// 		const selectedRoles = this.roleOptions.filter(role => newVal.includes(role.roleId));
		// 		this.formData.roleNames = selectedRoles.map(role => role.roleName).join(',');
		// 	},
		// 	deep: true
		// },
		// 'formData.userIdsArray': {
		// 	handler(newVal) {
		// 		this.formData.userIds = newVal.join(',');
		// 		// 同步用户名称
		// 		const selectedUsers = this.userOptions.filter(user => newVal.includes(user.userId));
		// 		this.formData.userNames = selectedUsers.map(user => user.userName).join(',');
		// 	},
		// 	deep: true
		// }
	},
	methods: {
		// 初始化方法
		init() {
			// 从param中获取id，优先级高于props中的id
			const editId = this.param?.id || this.id;
			if (editId) {
				this.loadData(editId);
			}
		},
		handleClose() {
			// 关闭，并传递参数
			this.closeTpDialog("xxxx");
		},
		// 加载数据
		loadData(id) {
			app.$svc.sys.timerule.view({
				id: id || this.param?.id || this.id
			}).then(result => {
				if (result.code === 1) {
					const data = result.data;
					this.formData = {
						...this.formData,
						...data,
						roleIdsArray: data.roleIds ? data.roleIds.split(',') : [],
						userIdsArray: data.userIds ? data.userIds.split(',') : []
					};
				} else {
					this.$message.error('加载数据失败: ' + result.message);
				}
			}).catch(error => {
				this.$message.error('加载数据失败');
				console.error(error);
			});
		},
		// 保存
		handleSave() {
			this.$refs.form.validate((valid) => {
				if (valid) {
					// 验证时间范围
					if (this.formData.startTime >= this.formData.endTime) {
						this.$message.error('开始时间不能大于或等于结束时间');
						return;
					}

					this.saving = true;

					const saveData = { ...this.formData };
					if (this.id) {
						saveData.id = this.id;
					}

					// 移除数组字段，只保留字符串字段
					delete saveData.roleIdsArray;
					delete saveData.userIdsArray;

					const apiMethod = this.param.mode === 'edit' ? 'update' : 'add';

					app.$svc.sys.timerule[apiMethod](saveData).then(result => {
						this.saving = false;
						if (result.code === 1) {
							this.$message.success(this.id ? '修改成功' : '新增成功');
							this.$emit('success', result.data);
							this.handleClose();
						} else {
							this.$message.error((this.id ? '修改' : '新增') + '失败: ' + result.message);
						}
					}).catch(error => {
						this.saving = false;
						this.$message.error((this.id ? '修改' : '新增') + '失败');
						console.error(error);
					});
				} else {
					this.$message.error('请检查表单填写是否正确');
				}
			});
		},
		// 取消
		handleCancel() {
			this.$emit('cancel');
		},

		showRoleDialog() {
			// 准备弹窗参数
			let param = {
				selectedRoleIds: this.formData.roleIdsArray || [],
				callback: (result) => {
					// 更新表单数据
					this.formData.roleIds = result.roleIds;
					this.formData.roleNames = result.roleNames;
					this.formData.roleIdsArray = result.roleIdsArray;
				}
			};

			// 显示角色选择弹窗
			let options = {

			};

			this.$refs.TpDialog.show(import('../../../views/sys/timerule/role-select.vue'), param, "角色选择", options);
		},

		handleUserSelect() {
			// 准备已选人员数据，匹配select-person-multiple.vue的persons格式
			let selectedPersons = [];
			if (this.formData.persons && this.formData.persons.length > 0) {
				selectedPersons = this.formData.persons;
			}
			let options = {}
			let param = {
				persons: selectedPersons,

				callback: (result) => {

					if (result && result.length > 0) {
						// 提取人员IDs并去重
						const userIds = [...new Set(result.map(item => item.value))];
						// 提取人员姓名
						const userNames = result.map(item => item.label).join(',');

						// 更新表单数据
						this.formData.userIds = userIds.join(',');
						this.formData.userNames = userNames;
						this.formData.persons = result.map(item => ({
							personId: item.value,
							personName: item.label,
							deptId: item.deptId,
							deptFullName: item.deptFullName
						}));
					} else {
						// 清空选择
						this.formData.userIds = '';
						this.formData.userNames = '';
						this.formData.persons = [];
					}
				}
			}
			this.$refs.TpDialog.show(import('../../../views/common/org/select-person-multiple.vue'), param, "人员选择", options);
		},
	}
}
</script>

<style lang="less" scoped>
.form-actions {
	text-align: center;
	margin-top: 20px;
	padding-top: 20px;
	border-top: 1px solid #e8e8e8;

	.fb-button {
		margin: 0 10px;
	}
}

.fb-checkbox-group {
	.fb-checkbox {
		margin-right: 15px;
		margin-bottom: 10px;
	}
}
</style>