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
							<fb-link :click="()=>handleView(props.row)" :label="props.row.menuName || '未知节点'" type="primary"></fb-link>
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
	name: 'MenuHistory',
	mixins: [],
	props: {
		menuId: {
			type: String,
			default: ''
		},
		menuName: {
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
				menuId: this.menuId
			},

			formatters: {
				operationType(val) {
					const typeMap = {
						'ADD': '新增',
						'UPDATE': '修改',
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
					list: app.$svc.sys.menu.historyList
				},
				primaryKey: "historyId",
				columns: [
				
					{
						name: 'menuName',
						label: '节点名称',
						slot: 'view',
						sortable: false,
					},
					{
						name: 'operationType',
						label: '操作类型',
						slot: 'operationType',
						sortable: false,
						width: 100,
					},
					{
						name: 'operatorName',
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


		// 查看方法
		handleView(row) {
			let param = {
				historyId: row.historyId,
				menuId: row.menuId,
				menuName: row.menuName,
				operationType: row.operationType,
				operatorName: row.operatorName,
				operationTime: row.operationTime,
				nodeDataBefore: row.nodeDataBefore,
				nodeDataAfter: row.nodeDataAfter
			};
			let options = {"height": 600};
			this.$refs.TpDialog.show(import('./view.vue'), param, "查看菜单历史详情", options);
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