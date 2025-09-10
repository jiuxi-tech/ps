<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-form ref="form" :model="formData" :rules="rules" label-width="140px" autocomplete="off">
				<div class="audit-content">
					<h3 class="section-title">申请人信息</h3>
					<fb-row>
						<fb-col span="8">
							<fb-form-item label="申请人姓名">
								<span>{{ auditData.applicantName }}</span>
							</fb-form-item>
						</fb-col>
						<fb-col span="8">
							<fb-form-item label="部门">
								<span>{{ auditData.deptName }}</span>
							</fb-form-item>
						</fb-col>
						<fb-col span="8">
							<fb-form-item label="手机号">
								<span>{{ auditData.phone }}</span>
							</fb-form-item>
						</fb-col>
					</fb-row>

					<h3 class="section-title">修改内容对比</h3>
					<div class="compare-section">
						<div v-for="field in changedFields" :key="field.fieldName" class="field-compare">
							<div class="field-label">{{ field.fieldLabel }}：</div>
							<div class="field-values">
								<div class="old-value">
									<span class="value-label">修改前：</span>
									<span class="value-content">{{ field.oldValue || '无' }}</span>
								</div>
								<div class="new-value">
									<span class="value-label">修改后：</span>
									<span class="value-content highlight">{{ field.newValue || '无' }}</span>
								</div>
							</div>
						</div>
					</div>

					<h3 class="section-title">审核操作</h3>
					<fb-row>
						<fb-col span="24">
							<fb-form-item label="审核结果" prop="auditResult">
								<fb-radio-group v-model="formData.auditResult"
									:data="[{ value: 1, label: '通过' }, { value: 2, label: '拒绝' }]">
								</fb-radio-group>
							</fb-form-item>
						</fb-col>
					</fb-row>
					<fb-row>
						<fb-col span="24">
							<fb-form-item label="审核理由" prop="auditReason">
								<fb-textarea v-model="formData.auditReason" type="textarea" :rows="3"
									placeholder="请输入审核理由">
								</fb-textarea>
							</fb-form-item>
						</fb-col>
					</fb-row>
				</div>
			</fb-form>
		</div>

		<div class="tp-dialog-bottom">
			<fb-button style="margin-right: 12px" type="primary" @on-click="handleSubmit"
				:loading="submitting">确定</fb-button>
			<fb-button @on-click="handleCancel">取消</fb-button>
		</div>
	</div>
</template>

<script>
import dayjs from "dayjs";

export default {
	name: 'audit',
	props: {
		passKey: {
			type: String,
			default: ''
		},
		param: {
			type: Object,
			require: false
		},
		parentPage: {
			type: Object,
			default: null
		}
	},

	data() {
		return {
			id:'',
			requestId: '',
			auditData: {},
			changedFields: [],
			submitting: false,
			formData: {
				auditResult: 1,
				auditReason: ''
			},
			rules: {
				auditResult: [
					{ required: true, message: '请选择审核结果', trigger: 'change' }
				],
				auditReason: [
					{ required: true, message: '请输入审核理由', trigger: 'blur' },
					{ min: 5, message: '审核理由至少5个字符', trigger: 'blur' }
				]
			}
		}
	},

	mounted() {
		this.requestId = this.param.requestId;
		this.id = this.param.requestId;
		this.loadAuditData();

	},

	methods: {
		// 加载审核数据
		async loadAuditData() {
			try {
				const response = await app.$svc.sys.person.audit.detail({
					requestId: this.id
				});

				if (response.code === 1) {
					this.auditData = response.data;
					this.parseChangedFields();
				} else {
					this.$message.error(response.message || '加载审核数据失败');
				}
			} catch (error) {
				console.error('加载审核数据失败:', error);
				this.$message.error('加载审核数据失败');
			}
		},

		// 解析变更字段
		parseChangedFields() {
			if (!this.auditData.changedFields) {
				return;
			}

			try {
				const changedFieldsArray = JSON.parse(this.auditData.changedFields);
				const beforeData = this.auditData.beforeData ? JSON.parse(this.auditData.beforeData) : {};
				const afterData = this.auditData.afterData ? JSON.parse(this.auditData.afterData) : {};

				this.changedFields = changedFieldsArray.map(fieldName => {
					return {
						fieldName: fieldName,
						fieldLabel: this.getFieldLabel(fieldName),
						oldValue: this.formatFieldValue(fieldName, beforeData[fieldName]),
						newValue: this.formatFieldValue(fieldName, afterData[fieldName])
					};
				});
			} catch (error) {
				console.error('解析变更字段失败:', error);
			}
		},

		// 获取字段标签
		getFieldLabel(fieldName) {
			const fieldLabels = {
				'personName': '姓名',
				'phone': '手机号',
				'email': '邮箱',
				'idcard': '身份证号',
				'address': '地址',
				'birthday': '生日',
				'sex': '性别',
				'education': '学历',
				'marriage': '婚姻状况',
				'nation': '民族',
				'political': '政治面貌',
				'office': '职务',
				'resume': '个人简介',
				'tel': '电话',
				'emergencyContact': '紧急联系人',
				'emergencyPhone': '紧急联系电话'
			};
			return fieldLabels[fieldName] || fieldName;
		},

		// 格式化字段值
		formatFieldValue(fieldName, value) {
			if (value === null || value === undefined || value === '') {
				return '空';
			}

			// 性别字段特殊处理
			if (fieldName === 'sex') {
				const sexMap = {
					'1': '男',
					'2': '女'
				};
				return sexMap[value] || value;
			}

			return value;
		},

		// 格式化时间
		formatTime(time) {
			return time ? dayjs(time).format('YYYY-MM-DD HH:mm:ss') : '';
		},

		// 提交审核
		async handleSubmit() {
			try {
				await this.$refs.form.validate();

				this.submitting = true;

				// 获取当前用户信息作为审核人
				const userInfo = app.$datax.get('userInfo') || {};
				const auditorId = userInfo.personId;
				const auditorName = userInfo.personName;

				if (!auditorId || !auditorName) {
					this.$message.error('无法获取审核人信息，请重新登录');
					this.submitting = false;
					return;
				}

				const serviceMethod = this.formData.auditResult === 1 ? 'approve' : 'reject';
				const response = await app.$svc.sys.person.audit[serviceMethod]({
					requestId: this.id,
					auditReason: this.formData.auditReason,
					auditorId: auditorId,
					auditorName: auditorName
				});

				if (response.success) {
					this.$message.success('审核提交成功');
					this.$emit('closeTpDialog', { refresh: true });
				} else {
					this.$message.error(response.message || '审核提交失败');
				}
			} catch (error) {
				if (error.fields) {
					// 表单验证失败
					return;
				}
				console.error('审核提交失败:', error);
				this.$message.error('审核提交失败');
			} finally {
				this.submitting = false;
			}
		},

		// 取消
		handleCancel() {
			this.$emit('closeTpDialog');
		}
	}
}
</script>

<style lang="less" scoped>
.section-title {
	margin: 15px 0 12px 0;
	padding-bottom: 6px;
	border-bottom: 1px solid #e8e8e8;
	font-size: 15px;
	font-weight: 600;
	color: #333;
}

.audit-content {
	margin-bottom: 15px;
}

.compare-section {
	margin-top: 10px;
}

.field-compare {
	margin-bottom: 16px;
	padding: 12px;
	border: 1px solid #e8e8e8;
	border-radius: 4px;
	background-color: #fafafa;
}

.field-label {
	font-weight: 600;
	margin-bottom: 8px;
	color: #333;
}

.field-values {
	display: flex;
	justify-content: space-between;
	gap: 20px;
}

.old-value,
.new-value {
	flex: 1;
	padding: 8px 12px;
	border-radius: 4px;
}

.old-value {
	background-color: #fff2f0;
	border: 1px solid #ffccc7;
}

.new-value {
	background-color: #f6ffed;
	border: 1px solid #b7eb8f;
}

.value-label {
	font-weight: 500;
	color: #666;
	margin-right: 8px;
}

.value-content {
	color: #333;
}

.highlight {
	font-weight: 600;
	color: #52c41a;
}
</style>