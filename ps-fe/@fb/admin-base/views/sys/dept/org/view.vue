<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-property bordered label-width="140px" mode="form">
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="节点名称">
							{{ formData.orgName }}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="操作人">
							{{ formData.operatorUserName }}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="操作类型">
							<fb-tag :type="getOperationTypeColor(formData.operationType)" size="small">
								{{ getOperationTypeName(formData.operationType) }}
							</fb-tag>
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="操作时间">
							{{ formatOperationTime(formData.operationTime) }}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row v-if="formData.operationType === 'UPDATE'">
					<fb-col span="24">
						<fb-property-item label="数据对比">
							<div style="max-height: 400px; overflow-y: auto;">
								<table style="width: 100%; border-collapse: collapse; border: 1px solid #e8e8e8;">
									<thead>
										<tr style="background: #f5f5f5;">
											<th
												style="border: 1px solid #e8e8e8; padding: 8px; text-align: left; width: 33%;">
												字段名称</th>
											<th
												style="border: 1px solid #e8e8e8; padding: 8px; text-align: left; width: 33%;">
												操作前</th>
											<th
												style="border: 1px solid #e8e8e8; padding: 8px; text-align: left; width: 34%;">
												操作后</th>
										</tr>
									</thead>
									<tbody>
										<tr v-for="comparison in getDataComparison()" :key="comparison.key"
											:style="{ backgroundColor: comparison.changed ? '#fff2e8' : 'transparent' }">
											<td style="border: 1px solid #e8e8e8; padding: 8px; font-weight: bold;">
												{{ comparison.fieldName }}
											</td>
											<td style="border: 1px solid #e8e8e8; padding: 8px;"
												:style="{ color: comparison.changed ? '#f56c6c' : 'inherit' }">
												<span>{{ comparison.beforeValue || '-' }}</span>
											</td>
											<td style="border: 1px solid #e8e8e8; padding: 8px;"
												:style="{ color: comparison.changed ? '#67c23a' : 'inherit' }">
												<span>{{ comparison.afterValue || '-' }}</span>
											</td>
										</tr>
									</tbody>
								</table>
								<div v-if="getDataComparison().length === 0"
									style="text-align: center; color: #999; padding: 20px;">
									无数据对比
								</div>
							</div>
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row v-if="formData.operationType === 'CREATE'">
					<fb-col span="24">
						<fb-property-item label="新增数据">
							<div style="max-height: 400px; overflow-y: auto;">
								<table style="width: 100%; border-collapse: collapse; border: 1px solid #e8e8e8;">
									<thead>
										<tr style="background: #f5f5f5;">
											<th
												style="border: 1px solid #e8e8e8; padding: 8px; text-align: left; width: 50%;">
												字段名称</th>
											<th
												style="border: 1px solid #e8e8e8; padding: 8px; text-align: left; width: 50%;">
												新增值</th>
										</tr>
									</thead>
									<tbody>
										<tr v-for="comparison in getAddDataComparison()" :key="comparison.key">
											<td style="border: 1px solid #e8e8e8; padding: 8px; font-weight: bold;">
												{{ comparison.fieldName }}
											</td>
											<td style="border: 1px solid #e8e8e8; padding: 8px; color: #67c23a;">
												<span>{{ comparison.value || '-' }}</span>
											</td>
										</tr>
									</tbody>
								</table>
								<div v-if="getAddDataComparison().length === 0"
									style="text-align: center; color: #999; padding: 20px;">
									无数据
								</div>
							</div>
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row v-if="formData.operationType === 'DELETE'">
					<fb-col span="24">
						<fb-property-item label="删除数据">
							<div style="max-height: 400px; overflow-y: auto;">
								<table style="width: 100%; border-collapse: collapse; border: 1px solid #e8e8e8;">
									<thead>
										<tr style="background: #f5f5f5;">
											<th
												style="border: 1px solid #e8e8e8; padding: 8px; text-align: left; width: 50%;">
												字段名称</th>
											<th
												style="border: 1px solid #e8e8e8; padding: 8px; text-align: left; width: 50%;">
												删除值</th>
										</tr>
									</thead>
									<tbody>
										<tr v-for="comparison in getDeleteDataComparison()" :key="comparison.key">
											<td style="border: 1px solid #e8e8e8; padding: 8px; font-weight: bold;">
												{{ comparison.fieldName }}
											</td>
											<td style="border: 1px solid #e8e8e8; padding: 8px; color: #f56c6c;">
												<span>{{ comparison.value || '-' }}</span>
											</td>
										</tr>
									</tbody>
								</table>
								<div v-if="getDeleteDataComparison().length === 0"
									style="text-align: center; color: #999; padding: 20px;">
									无数据
								</div>
							</div>
						</fb-property-item>
					</fb-col>
				</fb-row>
			</fb-property>
		</div>

		<div class="tp-dialog-bottom">
			<fb-button @on-click="handleClose">关闭</fb-button>
		</div>
	</div>
</template>

<script>
export default {
	components: {},
	name: 'HistoryView',
	mixins: [],
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
	created() { },
	// 初始化方法
	mounted() {
		// 执行界面初始化
		this.init(this.param);
		// 加载部门类型字典数据
		this.loadDeptTypeDict();
	},
	data() {
		return {
			// 表单数据
			formData: {
				historyId: '',
				orgId: '',
				orgName: '',
				operationType: '',
				operatorUserName: '',
				operationTime: '',
				beforeData: '',
				afterData: ''
			},
			// 字段中文映射
			fieldMap: {
				orgId: '组织ID',
				orgName: '组织名称',
				orgCode: '组织编码',
				orgType: '组织类型',
				orgDesc: '组织描述',
				orgPid: '父级组织ID',
				orgTreePid: '组织树父级ID',
				orgSort: '排序',
				orderIndex: '排序索引',
				orgStatus: '状态',
				actived: '激活状态',
				enabled: '启用状态',
				orgRemark: '备注',
				createTime: '创建时间',
				updateTime: '更新时间',
				creator: '创建人',
				updator: '更新人',
				tenantId: '租户ID',
				extend01: '扩展字段1',
				extend02: '扩展字段2',
				extend03: '扩展字段3',
				cityFullName: '城市全称',
				deptTypeName: '部门类型名称',
				deptFullName: '部门全称',
				deptSimpleName: '部门简称',
				deptType: '部门类型'
			},

			// 部门类型映射（动态从字典表加载）
			deptTypeMap: {},

			// 需要隐藏的字段
			hiddenFields: ['orgId', 'orgPid', 'orgTreePid', 'actived', 'category', 'cityCode', 'deptId', 'deptLevelcode', 'pdeptId', 'principalName', 'cityId', 'ascnId']
		};
	},
	computed: {
		// 解析操作前数据
		parsedBeforeData() {
			console.log('原始beforeData:', this.formData.beforeData);
			try {
				const result = this.formData.beforeData ? JSON.parse(this.formData.beforeData) : null;
				console.log('解析后beforeData:', result);
				return result;
			} catch (e) {
				console.error('解析操作前数据失败:', e);
				return null;
			}
		},

		// 解析操作后数据
		parsedAfterData() {
			console.log('原始afterData:', this.formData.afterData);
			try {
				const result = this.formData.afterData ? JSON.parse(this.formData.afterData) : null;
				console.log('解析后afterData:', result);
				return result;
			} catch (e) {
				console.error('解析操作后数据失败:', e);
				return null;
			}
		}
	},

	// 方法
	methods: {
		// 设置标题
		init(param) {
			if (param) {
				// 如果有historyId，则通过接口获取详细数据
				if (param.historyId) {
					app.$svc.sys.dept.org.historyView({ historyId: param.historyId }).then(res => {
						if (res.data) {
							this.formData = {
								historyId: res.data.historyId || '',
								orgId: res.data.orgId || '',
								orgName: res.data.orgName || '',
								operationType: res.data.operationType || '',
								operatorUserName: res.data.operatorUserName || '',
								operationTime: res.data.operationTime || '',
								beforeData: res.data.beforeData || '',
								afterData: res.data.afterData || ''
							};
						}
					}).catch(err => {
						console.error('获取历史详情失败:', err);
					});
				} else {
					// 直接使用传入的参数
					this.formData = {
						historyId: param.historyId || '',
						orgId: param.orgId || '',
						orgName: param.orgName || '',
						operationType: param.operationType || '',
						operatorUserName: param.operatorUserName || '',
						operationTime: param.operationTime || '',
						beforeData: param.beforeData || '',
						afterData: param.afterData || ''
					};
				}
			}
		},

		// 取消
		handleClose() {
			// 关闭，并传递参数
			this.closeTpDialog("xxxx");
		},

		// 获取操作类型名称
		getOperationTypeName(type) {
			const typeMap = {
				'CREATE': '创建',
				'UPDATE': '更新',
				'DELETE': '删除'
			};
			return typeMap[type] || type;
		},

		// 获取操作类型颜色
		getOperationTypeColor(type) {
			const colorMap = {
				'CREATE': 'success',
				'UPDATE': 'warning',
				'DELETE': 'danger'
			};
			return colorMap[type] || 'default';
		},

		// 获取格式化的节点数据
		getFormattedNodeData(nodeData) {
			console.log('getFormattedNodeData 输入数据:', nodeData);
			if (!nodeData) {
				console.log('nodeData 为空或null');
				return {};
			}

			const result = {};
			for (const [key, value] of Object.entries(nodeData)) {
				const chineseKey = this.fieldMap[key] || key;
				// 过滤掉null字符串、空字符串，但保留数字0和布尔值false
				if (value !== null && value !== undefined && value !== '' && value !== 'null') {
					result[chineseKey] = value;
				}
			}
			console.log('getFormattedNodeData 输出结果:', result);
			return result;
		},

		// 获取数据对比结果
		getDataComparison() {
			if (!this.parsedBeforeData || !this.parsedAfterData) {
				return [];
			}

			// 获取所有字段的并集
			const allKeys = new Set([
				...Object.keys(this.parsedBeforeData),
				...Object.keys(this.parsedAfterData)
			]);

			const comparisons = [];
			for (const key of allKeys) {
				// 跳过隐藏字段
				if (this.hiddenFields.includes(key)) {
					continue;
				}

				const beforeValue = this.parsedBeforeData[key];
				const afterValue = this.parsedAfterData[key];

				// 过滤掉null字符串、空字符串，但保留数字0和布尔值false
				const shouldInclude = (beforeValue !== null && beforeValue !== undefined && beforeValue !== '' && beforeValue !== 'null') ||
					(afterValue !== null && afterValue !== undefined && afterValue !== '' && afterValue !== 'null');

				if (shouldInclude) {
					const fieldName = this.fieldMap[key] || key;
					const changed = String(beforeValue) !== String(afterValue);

					// 处理特殊字段的显示
					let displayBeforeValue = beforeValue;
					let displayAfterValue = afterValue;
					if (key === 'createTime' || key === 'updateTime') {
						displayBeforeValue = this.formatOperationTime(beforeValue);
						displayAfterValue = this.formatOperationTime(afterValue);
					} else if (key === 'deptType') {
						displayBeforeValue = this.deptTypeMap[beforeValue] || beforeValue;
						displayAfterValue = this.deptTypeMap[afterValue] || afterValue;
					}

					comparisons.push({
						key: key,
						fieldName: fieldName,
						beforeValue: displayBeforeValue,
						afterValue: displayAfterValue,
						changed: changed
					});
				}
			}

			// 按字段名称排序，修改的字段排在前面
			return comparisons.sort((a, b) => {
				if (a.changed && !b.changed) return -1;
				if (!a.changed && b.changed) return 1;
				return a.fieldName.localeCompare(b.fieldName);
			});
		},

		// 获取新增数据对比结果
		getAddDataComparison() {
			const afterData = this.parsedAfterData || {};

			const result = [];
			for (const key in afterData) {
				// 跳过隐藏字段
				if (this.hiddenFields.includes(key)) {
					continue;
				}

				const value = afterData[key];

				// 过滤掉null字符串、空字符串，但保留数字0和布尔值false
				if (value !== null && value !== undefined && value !== '' && value !== 'null') {
					// 处理特殊字段的显示
					let displayValue = value;
					if (key === 'createTime' || key === 'updateTime') {
						displayValue = this.formatOperationTime(value);
					} else if (key === 'deptType') {
						displayValue = this.deptTypeMap[value] || value;
					}

					result.push({
						key: key,
						fieldName: this.fieldMap[key] || key,
						value: displayValue
					});
				}
			}

			// 按字段名排序
			return result.sort((a, b) => a.fieldName.localeCompare(b.fieldName));
		},

		// 获取删除数据对比结果
		getDeleteDataComparison() {
			const beforeData = this.parsedBeforeData || {};

			const result = [];
			for (const key in beforeData) {
				// 跳过隐藏字段
				if (this.hiddenFields.includes(key)) {
					continue;
				}

				const value = beforeData[key];

				// 过滤掉null字符串、空字符串，但保留数字0和布尔值false
				if (value !== null && value !== undefined && value !== '' && value !== 'null') {
					// 处理特殊字段的显示
					let displayValue = value;
					if (key === 'createTime' || key === 'updateTime') {
						displayValue = this.formatOperationTime(value);
					} else if (key === 'deptType') {
						displayValue = this.deptTypeMap[value] || value;
					}

					result.push({
						key: key,
						fieldName: this.fieldMap[key] || key,
						value: displayValue
					});
				}
			}

			// 按字段名排序
			return result.sort((a, b) => a.fieldName.localeCompare(b.fieldName));
		},

		// 格式化操作时间
		formatOperationTime(timeStr) {
			if (!timeStr) return '-';

			// 如果是14位时间戳格式 (yyyyMMddHHmmss)
			if (timeStr.length === 14 && /^\d{14}$/.test(timeStr)) {
				const year = timeStr.substring(0, 4);
				const month = timeStr.substring(4, 6);
				const day = timeStr.substring(6, 8);
				const hour = timeStr.substring(8, 10);
				const minute = timeStr.substring(10, 12);
				const second = timeStr.substring(12, 14);
				return `${year}-${month}-${day} ${hour}:${minute}:${second}`;
			}

			// 如果已经是格式化的时间，直接返回
			return timeStr;
		},

		// 加载部门类型字典数据
		loadDeptTypeDict() {
			// 直接构建部门类型映射，关联字典表中的具体字典项
			this.deptTypeMap = {
				'SYS0501': '单位',
				'SYS0502': '部门',
				'SYS0503': '网格',
				'SYS0504': '其他'
			};
		}
	}
}
</script>

<style lang="less" scoped>
.tp-dialog {
	display: flex;
	flex-direction: column;
	height: 100%;
}

.tp-dialog-top {
	flex: 1;
	overflow-y: auto;
	padding: 16px;
}

.tp-dialog-bottom {
	padding: 16px;
	border-top: 1px solid #e8e8e8;
	text-align: center;
}

.fb-property {
	.fb-property-item {
		margin-bottom: 8px;
	}
}
</style>