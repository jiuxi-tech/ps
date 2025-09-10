<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-property bordered label-width="140px" mode="form">
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="节点名称">
							{{ formData.menuName }}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="操作人">
							{{ formData.operatorName }}
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
											<th style="border: 1px solid #e8e8e8; padding: 8px; text-align: left; width: 33%;">字段名称</th>
											<th style="border: 1px solid #e8e8e8; padding: 8px; text-align: left; width: 33%;">操作前</th>
											<th style="border: 1px solid #e8e8e8; padding: 8px; text-align: left; width: 34%;">操作后</th>
										</tr>
									</thead>
									<tbody>
										<tr v-for="comparison in getDataComparison()" :key="comparison.key" 
											:style="{ backgroundColor: comparison.changed ? '#fff2e8' : 'transparent' }">
											<td style="border: 1px solid #e8e8e8; padding: 8px; font-weight: bold;">
												{{ comparison.fieldName }}
												<fb-tag v-if="comparison.changed" type="warning" size="mini" style="margin-left: 8px;">已修改</fb-tag>
											</td>
											<td style="border: 1px solid #e8e8e8; padding: 8px;" 
												:style="{ color: comparison.changed ? '#f56c6c' : 'inherit' }">
												<span v-if="comparison.key === 'menuIcon' && comparison.beforeValue">
													<fb-icon :name="comparison.beforeValue" style="margin-right: 4px;"></fb-icon>
													{{ comparison.beforeValue }}
												</span>
												<span v-else>{{ comparison.beforeValue || '-' }}</span>
											</td>
											<td style="border: 1px solid #e8e8e8; padding: 8px;" 
												:style="{ color: comparison.changed ? '#67c23a' : 'inherit' }">
												<span v-if="comparison.key === 'menuIcon' && comparison.afterValue">
													<fb-icon :name="comparison.afterValue" style="margin-right: 4px;"></fb-icon>
													{{ comparison.afterValue }}
												</span>
												<span v-else>{{ comparison.afterValue || '-' }}</span>
											</td>
										</tr>
									</tbody>
								</table>
								<div v-if="getDataComparison().length === 0" style="text-align: center; color: #999; padding: 20px;">
									无数据对比
								</div>
							</div>
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row v-if="formData.operationType === 'ADD'">
					<fb-col span="24">
						<fb-property-item label="新增数据">
							<div style="max-height: 400px; overflow-y: auto;">
								<table style="width: 100%; border-collapse: collapse; border: 1px solid #e8e8e8;">
									<thead>
										<tr style="background: #f5f5f5;">
											<th style="border: 1px solid #e8e8e8; padding: 8px; text-align: left; width: 50%;">字段名称</th>
											<th style="border: 1px solid #e8e8e8; padding: 8px; text-align: left; width: 50%;">新增值</th>
										</tr>
									</thead>
									<tbody>
										<tr v-for="comparison in getAddDataComparison()" :key="comparison.key">
											<td style="border: 1px solid #e8e8e8; padding: 8px; font-weight: bold;">
												{{ comparison.fieldName }}
											</td>
											<td style="border: 1px solid #e8e8e8; padding: 8px; color: #67c23a;">
												<span v-if="comparison.key === 'menuIcon' && comparison.value">
													<fb-icon :name="comparison.value" style="margin-right: 4px;"></fb-icon>
													{{ comparison.value }}
												</span>
												<span v-else>{{ comparison.value || '-' }}</span>
											</td>
										</tr>
									</tbody>
								</table>
								<div v-if="getAddDataComparison().length === 0" style="text-align: center; color: #999; padding: 20px;">
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
											<th style="border: 1px solid #e8e8e8; padding: 8px; text-align: left; width: 50%;">字段名称</th>
											<th style="border: 1px solid #e8e8e8; padding: 8px; text-align: left; width: 50%;">删除值</th>
										</tr>
									</thead>
									<tbody>
										<tr v-for="comparison in getDeleteDataComparison()" :key="comparison.key">
											<td style="border: 1px solid #e8e8e8; padding: 8px; font-weight: bold;">
												{{ comparison.fieldName }}
											</td>
											<td style="border: 1px solid #e8e8e8; padding: 8px; color: #f56c6c;">
												<span v-if="comparison.key === 'menuIcon' && comparison.value">
													<fb-icon :name="comparison.value" style="margin-right: 4px;"></fb-icon>
													{{ comparison.value }}
												</span>
												<span v-else>{{ comparison.value || '-' }}</span>
											</td>
										</tr>
									</tbody>
								</table>
								<div v-if="getDeleteDataComparison().length === 0" style="text-align: center; color: #999; padding: 20px;">
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
  components: {   },
	name: 'MenuOperationView',
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
	created() {},
	// 初始化方法
	mounted() {
		// 执行界面初始化
		this.init(this.param);
	},
	data() {
		return {
			// 表单数据
			formData: {
				historyId: '',
				menuId: '',
				menuName: '',
				operationType: '',
				operatorName: '',
				operationTime: '',
				nodeDataBefore: '',
				nodeDataAfter: ''
			},
			// 字段中文映射
			fieldMap: {
				menuId: '菜单ID',
				menuName: '菜单名称',
				menuCode: '菜单编码',
				menuType: '菜单类型',
				menuTypeName: '菜单类型名称',
				menuIcon: '菜单图标',
				menuUrl: '菜单路径',
				menuUri: '菜单URI',
				menuDesc: '菜单描述',
				menuPid: '父级菜单ID',
				menuTreePid: '菜单树父级ID',
				menuSort: '排序',
				orderIndex: '排序索引',
				menuStatus: '状态',
				actived: '激活状态',
				enabled: '启用状态',
				menuRemark: '备注',
				createTime: '创建时间',
				updateTime: '更新时间',
				creator: '创建人',
				updator: '更新人',
				menuSource: '菜单归属',
				menuSourceName: '菜单归属 (app/pc)',
				tenantId: '租户ID',
				extend01: '扩展字段1',
				extend02: '扩展字段2',
				extend03: '扩展字段3',
				permissionCode: '权限编码',
				component: '组件路径',
				redirect: '重定向',
				hidden: '是否隐藏',
				alwaysShow: '总是显示',
				breadcrumb: '面包屑',
				affix: '固定标签',
				noCache: '不缓存'
			},

			// 需要隐藏的字段
			hiddenFields: ['menuId', 'menuPid', 'menuTreePid', 'menuSource', 'actived', 'menuType']
		};
	},
	computed: {
			// 解析操作前数据
			parsedNodeDataBefore() {
				console.log('原始nodeDataBefore:', this.formData.nodeDataBefore);
				try {
					const result = this.formData.nodeDataBefore ? JSON.parse(this.formData.nodeDataBefore) : null;
					console.log('解析后nodeDataBefore:', result);
					return result;
				} catch (e) {
					console.error('解析操作前数据失败:', e);
					return null;
				}
			},

			// 解析操作后数据
			parsedNodeDataAfter() {
				console.log('原始nodeDataAfter:', this.formData.nodeDataAfter);
				try {
					const result = this.formData.nodeDataAfter ? JSON.parse(this.formData.nodeDataAfter) : null;
					console.log('解析后nodeDataAfter:', result);
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
					app.$svc.sys.menu.historyView({historyId: param.historyId}).then(res => {
						if (res.data) {
							this.formData = {
								historyId: res.data.historyId || '',
								menuId: res.data.menuId || '',
								menuName: res.data.menuName || '',
								operationType: res.data.operationType || '',
								operatorName: res.data.operatorName || '',
								operationTime: res.data.operationTime || '',
								nodeDataBefore: res.data.nodeDataBefore || '',
								nodeDataAfter: res.data.nodeDataAfter || ''
							};
						}
					}).catch(err => {
						console.error('获取历史详情失败:', err);
					});
				} else {
					// 直接使用传入的参数
					this.formData = {
						historyId: param.historyId || '',
						menuId: param.menuId || '',
						menuName: param.menuName || '',
						operationType: param.operationType || '',
						operatorName: param.operatorName || '',
						operationTime: param.operationTime || '',
						nodeDataBefore: param.nodeDataBefore || '',
						nodeDataAfter: param.nodeDataAfter || ''
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
				'ADD': '新增',
				'UPDATE': '修改',
				'DELETE': '删除'
			};
			return typeMap[type] || type;
		},

		// 获取操作类型颜色
		getOperationTypeColor(type) {
			const colorMap = {
				'ADD': 'success',
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
			if (!this.parsedNodeDataBefore || !this.parsedNodeDataAfter) {
				return [];
			}

			// 获取所有字段的并集
			const allKeys = new Set([
				...Object.keys(this.parsedNodeDataBefore),
				...Object.keys(this.parsedNodeDataAfter)
			]);

			const comparisons = [];
			for (const key of allKeys) {
				// 跳过隐藏字段
				if (this.hiddenFields.includes(key)) {
					continue;
				}
				
				const beforeValue = this.parsedNodeDataBefore[key];
				const afterValue = this.parsedNodeDataAfter[key];
				
				// 过滤掉null字符串、空字符串，但保留数字0和布尔值false
				const shouldInclude = (beforeValue !== null && beforeValue !== undefined && beforeValue !== '' && beforeValue !== 'null') ||
									  (afterValue !== null && afterValue !== undefined && afterValue !== '' && afterValue !== 'null');
				
				if (shouldInclude) {
					const fieldName = this.fieldMap[key] || key;
					const changed = String(beforeValue) !== String(afterValue);
					
					// 处理特殊字段的显示
					let displayBeforeValue = beforeValue;
					let displayAfterValue = afterValue;
					if (key === 'menuType') {
						displayBeforeValue = this.getMenuTypeText(beforeValue);
						displayAfterValue = this.getMenuTypeText(afterValue);
					} else if (key === 'createTime' || key === 'updateTime') {
						displayBeforeValue = this.formatOperationTime(beforeValue);
						displayAfterValue = this.formatOperationTime(afterValue);
					} else if (key === 'menuUri') {
						displayBeforeValue = beforeValue ? decodeURIComponent(beforeValue) : beforeValue;
						displayAfterValue = afterValue ? decodeURIComponent(afterValue) : afterValue;
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
			const afterData = this.parsedNodeDataAfter || {};
			
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
					if (key === 'menuType') {
						displayValue = this.getMenuTypeText(value);
					} else if (key === 'createTime' || key === 'updateTime') {
						displayValue = this.formatOperationTime(value);
					} else if (key === 'menuUri') {
						displayValue = value ? decodeURIComponent(value) : value;
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
			const beforeData = this.parsedNodeDataBefore || {};
			
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
					if (key === 'menuType') {
						displayValue = this.getMenuTypeText(value);
					} else if (key === 'createTime' || key === 'updateTime') {
						displayValue = this.formatOperationTime(value);
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
		
		// 获取菜单类型中文显示
		getMenuTypeText(menuType) {
			const typeMap = {
				'0': '目录',
				'1': '菜单',
				'2': '按钮',
				'3': '外链'
			};
			return typeMap[menuType] || menuType;
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