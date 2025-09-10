<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<div class="audit-content">
				<h3 class="section-title">申请人信息</h3>
				<fb-property bordered label-width="140px" mode="form">
					<fb-row>
						<fb-col span="8">
							<fb-property-item label="申请人姓名">
								{{ auditData.applicantName }}
							</fb-property-item>
						</fb-col>
						<fb-col span="8">
							<fb-property-item label="部门">
								{{ auditData.deptName }}
							</fb-property-item>
						</fb-col>
						<fb-col span="8">
							<fb-property-item label="手机号">
								{{ auditData.phone }}
							</fb-property-item>
						</fb-col>
					</fb-row>
					<fb-row v-if="auditData.submitTime">
						<fb-col span="8">
							<fb-property-item label="提交时间">
								{{ formatTime(auditData.submitTime) }}
							</fb-property-item>
						</fb-col>
						<fb-col span="8" v-if="auditData.auditStatus !== undefined">
							<fb-property-item label="审核状态">
								<span :class="getStatusClass(auditData.auditStatus)">{{ getStatusText(auditData.auditStatus) }}</span>
							</fb-property-item>
						</fb-col>
						<fb-col span="8" v-if="auditData.auditorName">
							<fb-property-item label="审核人">
								{{ auditData.auditorName }}
							</fb-property-item>
						</fb-col>
					</fb-row>
					<fb-row v-if="auditData.auditTime">
						<fb-col span="8">
							<fb-property-item label="审核时间">
								{{ formatTime(auditData.auditTime) }}
							</fb-property-item>
						</fb-col>
					</fb-row>
					<fb-row v-if="auditData.applyReason">
						<fb-col span="24">
							<fb-property-item label="申请理由">
								{{ auditData.applyReason || '无' }}
							</fb-property-item>
						</fb-col>
					</fb-row>
					<fb-row v-if="auditData.auditReason">
						<fb-col span="24">
							<fb-property-item label="审核理由">
								{{ auditData.auditReason }}
							</fb-property-item>
						</fb-col>
					</fb-row>
				</fb-property>

				<h3 class="section-title">修改内容对比</h3>
				<div class="compare-section">
					<div v-if="changedFields.length === 0" class="no-changes">
						暂无修改内容
					</div>
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
			</div>
		</div>

		<div class="tp-dialog-bottom">
			<fb-button type="primary" @on-click="handleClose">关闭</fb-button>
		</div>
	</div>
</template>

<script>
import dayjs from "dayjs";

export default {
	name: 'audit-view',
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
		return {	id:'',
			requestId: '',
			auditData: {},
			changedFields: []
		}
	},

	mounted() {
		this.loadAuditData();
	},

	methods: {
		// 加载审核数据
		async loadAuditData() {
			try {
				const response = await app.$svc.sys.person.audit.detail({
					requestId: this.requestId
				});

				if (response.success) {
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

		// 获取状态样式类
		getStatusClass(status) {
			switch (status) {
				case 0:
					return 'status-pending'
				case 1:
					return 'status-approved'
				case 2:
					return 'status-rejected'
				case 3:
					return 'status-cancelled'
				default:
					return ''
			}
		},

		// 获取状态文本
		getStatusText(status) {
			switch (status) {
				case 0:
					return '审核中'
				case 1:
					return '审核通过'
				case 2:
					return '审核驳回'
				case 3:
					return '已取消'
				default:
					return '未知'
			}
		},

		// 格式化时间
		formatTime(time) {
			return time ? dayjs(time).format('YYYY-MM-DD HH:mm:ss') : '';
		},

		// 关闭
		handleClose() {
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

.no-changes {
	text-align: center;
	color: #999;
	padding: 20px;
	font-style: italic;
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

.status-pending {
	color: #faad14;
	font-weight: bold;
}

.status-approved {
	color: #52c41a;
	font-weight: bold;
}

.status-rejected {
	color: #ff4d4f;
	font-weight: bold;
}

.status-cancelled {
	color: #8c8c8c;
	font-weight: bold;
}
</style>