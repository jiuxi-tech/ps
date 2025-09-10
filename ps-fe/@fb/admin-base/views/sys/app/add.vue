<template>
	<div>
		<fb-form ref="form" :model="formData" :rules="rules" :label-width="120">
			<fb-row>
				<fb-col span="12">
					<fb-form-item label="应用名称" prop="appName">
						<fb-input v-model="formData.appName" placeholder="请输入应用名称"></fb-input>
					</fb-form-item>
				</fb-col>
				<fb-col span="12">
					<fb-form-item label="应用编码" prop="appCode">
						<fb-input v-model="formData.appCode" placeholder="请输入应用编码"></fb-input>
					</fb-form-item>
				</fb-col>
			</fb-row>
			<fb-row>
				<fb-col span="12">
					<fb-form-item label="应用类型" prop="appType">
						<fb-tree-select v-model="formData.appType"
										:service="$svc.sys.dict.tree"
										:param="{'dicCode': 'APP_TYPE'}"
										:reader="{value: 'id', label: 'text'}"
										placeholder="请选择应用类型"
										clearable>
						</fb-tree-select>
					</fb-form-item>
				</fb-col>
				<fb-col span="12">
					<fb-form-item label="应用状态" prop="appStatus">
						<fb-tree-select v-model="formData.appStatus"
										:service="$svc.sys.dict.tree"
										:param="{'dicCode': 'APP_STATUS'}"
										:reader="{value: 'id', label: 'text'}"
										placeholder="请选择应用状态"
										clearable>
						</fb-tree-select>
					</fb-form-item>
				</fb-col>
			</fb-row>
			<fb-row>
				<fb-col span="12">
					<fb-form-item label="版本号" prop="appVersion">
						<fb-input v-model="formData.appVersion" placeholder="请输入版本号"></fb-input>
					</fb-form-item>
				</fb-col>
				<fb-col span="12">
					<fb-form-item label="应用地址" prop="appUrl">
						<fb-input v-model="formData.appUrl" placeholder="请输入应用访问地址"></fb-input>
					</fb-form-item>
				</fb-col>
			</fb-row>
			<fb-row>
				<fb-col span="12">
					<fb-form-item label="应用图标" prop="appIcon">
						<fb-input v-model="formData.appIcon" placeholder="请输入应用图标路径"></fb-input>
					</fb-form-item>
				</fb-col>
				<fb-col span="12">
					<fb-form-item label="排序号" prop="sortOrder">
						<fb-input-number v-model="formData.sortOrder" :min="0" placeholder="请输入排序号"></fb-input-number>
					</fb-form-item>
				</fb-col>
			</fb-row>
			<fb-row>
				<fb-col span="24">
					<fb-form-item label="应用描述" prop="appDesc">
						<fb-input v-model="formData.appDesc" type="textarea" :rows="4" placeholder="请输入应用描述"></fb-input>
					</fb-form-item>
				</fb-col>
			</fb-row>
			<fb-row>
				<fb-col span="24">
					<fb-form-item label="备注" prop="remark">
						<fb-input v-model="formData.remark" type="textarea" :rows="3" placeholder="请输入备注信息"></fb-input>
					</fb-form-item>
				</fb-col>
			</fb-row>
		</fb-form>
		
		<div class="form-actions">
			<fb-button @on-click="handleSave" type="primary" :loading="saving">保存</fb-button>
			<fb-button @on-click="handleCancel">取消</fb-button>
		</div>
	</div>
</template>

<script>
	export default {
		name: 'AppAdd',
		props: {
			id: {
				type: String,
				default: ''
			},
			passKey: {
				type: String,
				default: ''
			}
		},
		data() {
			return {
				saving: false,
				formData: {
					appName: '',
					appCode: '',
					appType: '',
					appStatus: '1', // 默认启用
					appVersion: '1.0.0',
					appUrl: '',
					appIcon: '',
					appDesc: '',
					sortOrder: 0,
					remark: ''
				},
				rules: {
					appName: [
						{ required: true, message: '请输入应用名称', trigger: 'blur' },
						{ min: 2, max: 50, message: '应用名称长度在 2 到 50 个字符', trigger: 'blur' }
					],
					appCode: [
						{ required: true, message: '请输入应用编码', trigger: 'blur' },
						{ pattern: /^[A-Z0-9_]+$/, message: '应用编码只能包含大写字母、数字和下划线', trigger: 'blur' }
					],
					appType: [
						{ required: true, message: '请选择应用类型', trigger: 'change' }
					],
					appStatus: [
						{ required: true, message: '请选择应用状态', trigger: 'change' }
					],
					appVersion: [
						{ required: true, message: '请输入版本号', trigger: 'blur' }
					],
					appUrl: [
						{ required: true, message: '请输入应用地址', trigger: 'blur' },
						{ type: 'url', message: '请输入正确的URL地址', trigger: 'blur' }
					]
				}
			}
		},
		mounted() {
			if (this.id) {
				this.loadData();
			}
		},
		methods: {
			// 加载数据
			loadData() {
				app.$svc.sys.app.get({
					appId: this.id,
					passKey: this.passKey
				}).then(result => {
					if (result.code === 1) {
						this.formData = { ...this.formData, ...result.data };
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
						this.saving = true;
						
						const saveData = { ...this.formData };
						if (this.id) {
							saveData.appId = this.id;
							saveData.passKey = this.passKey;
						}
						
						const apiMethod = this.id ? 'update' : 'save';
						
						app.$svc.sys.app[apiMethod](saveData).then(result => {
							this.saving = false;
							if (result.code === 1) {
								this.$message.success(this.id ? '修改成功' : '新增成功');
								this.$emit('success', result.data);
								this.handleCancel();
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
			}
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
</style>