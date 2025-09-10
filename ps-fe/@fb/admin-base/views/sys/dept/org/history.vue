<template>
	<div>
		 <fb-simple-table
					ref="table"
					:service="table.service.list"
					:param="formData"
					:pk="table.primaryKey"
					:columns="table.columns"
					:multiple="false"
					auto-load
					:formatters="formatters"
					:scroll="{  y: 330}"
					@on-row-select="handleTableSelect">

					<template v-slot:actions="props">
						<fb-space>
							<fb-button @on-click="handleView(props.row)" 
									   size="s">详情</fb-button>
						</fb-space>
					</template>

					<template v-slot:view="props">
						<fb-link-group>
							<fb-link :click="()=>handleView(props.row)" :label="getNodeName(props.row)" type="primary"></fb-link>
						</fb-link-group>
					</template>

					<template v-slot:operationType="props">
						<fb-tag 
							:type="getOperationTypeColor(props.row.operationType)"
							size="small">
							{{ getOperationTypeName(props.row.operationType) }}
						</fb-tag>
					</template>

				</fb-simple-table>
		<tp-dialog ref="TpDialog" @closeTpDialog="closeDialog"></tp-dialog>
	</div>
</template>

<script>
import dayjs from "dayjs";

export default {
	name: 'History',
	mixins: [],
	props: {
		orgId: {
			type: String,
			default: ''
		},
		orgName: {
			type: String,
			default: ''
		}
	},
	// 初始化方法
	mounted() {
		// 执行界面初始化方法
	},
	data() {
		return {
			formData: {
				orgId: this.orgId
			},

			formatters: {
				orgName(val, row) {
					// 优先从afterData中获取deptFullName
					if (row.afterData) {
						try {
							const afterData = typeof row.afterData === 'string' ? JSON.parse(row.afterData) : row.afterData;
							if (afterData && afterData.deptFullName) {
								return afterData.deptFullName;
							}
						} catch (e) {
							// JSON解析失败时使用原值
						}
					}
					// 如果afterData中没有deptFullName，则使用原来的orgName
					return val || '未知节点';
				},
				operationType(val) {
					const typeMap = {
						'CREATE': '创建',
						'UPDATE': '更新',
						'DELETE': '删除'
					};
					return typeMap[val] || val;
				},
				operationTime(val) {
					if (!val) return val;
					
					// 如果是14位时间戳格式 (yyyyMMddHHmmss)
					if (val.length === 14 && /^\d{14}$/.test(val)) {
						const year = val.substring(0, 4);
						const month = val.substring(4, 6);
						const day = val.substring(6, 8);
						const hour = val.substring(8, 10);
						const minute = val.substring(10, 12);
						const second = val.substring(12, 14);
						return `${year}-${month}-${day} ${hour}:${minute}:${second}`;
					}
					
					// 如果已经是格式化的时间，使用dayjs处理
					return dayjs(val).format('YYYY-MM-DD HH:mm:ss');
				}
			},

			// Table列
			table: {
				// 请求的 url
				service: {
					list: app.$svc.sys.dept.org.history
				},
				primaryKey: "id",
				columns: [
				
					{
						name: 'orgName',
						label: '节点名称',
						slot: 'view',
						sortable: false,
						formatter: 'orgName',
					},
					{
						name: 'operationType',
						label: '操作类型',
						slot: 'operationType',
						sortable: false,
						width: 100,
					},
					{
						name: 'operatorUserName',
						label: '操作人',
						sortable: false,
						width: 120,
					},
					{
						name: 'operationTime',
						label: '操作时间',
						sortable: false,
						width: 180,
					},
				 
				],
			},
		}
	},
	// 方法
	methods: {
		// 获取节点名称
		getNodeName(row) {
			// 复用formatter逻辑
			return this.formatters.orgName(row.orgName, row);
		},

		// 查看方法
		handleView(row) {
			let param = {
				historyId: row.historyId,
				orgId: row.orgId,
				orgName: row.orgName,
				operationType: row.operationType,
				operatorUserName: row.operatorUserName,
				operationTime: row.operationTime,
				beforeData: row.beforeData,
				afterData: row.afterData
			};
			let options = {"height": 600};
			this.$refs.TpDialog.show(import('./view.vue'), param, "查看组织历史详情", options);
		},

		// 关闭弹窗回调
		closeDialog(param) {
			this.$refs.table.doSearch();
		},

		// 表格行选择事件
		handleTableSelect(row) {
			// 表格行选择事件
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
		}
	}
}
</script>

<style lang="less" scoped>
.fb-link {
	cursor: pointer;
	color: #1890ff;
	text-decoration: none;
}

.fb-link:hover {
	color: #40a9ff;
	text-decoration: underline;
}
</style>