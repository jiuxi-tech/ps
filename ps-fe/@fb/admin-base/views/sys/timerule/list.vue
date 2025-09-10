<template>
	<div>
		<fb-page-search>
			<template slot="query">
				<fb-form ref="query-form" mode="query" :label-width="160">
					<fb-row>
						<fb-col span="11">
							<fb-form-item label="规则名称">
								<fb-input v-model="formData.ruleName"></fb-input>
							</fb-form-item>
						</fb-col>
						<fb-col span="11" offset="1">
							<fb-form-item label="是否启用">
								<fb-select v-model="formData.status" placeholder="请选择" clearable
									:data="[{ value: '1', label: '启用' }, { value: '0', label: '禁用' }]">
								</fb-select>
							</fb-form-item>
						</fb-col>
					</fb-row>

				</fb-form>
			</template>

			<template slot="buttons">
				<fb-button ref="buttonAdd" @on-click="handleAdd" icon="add-circle">新增
				</fb-button>
			</template>

			<template slot="actions">
				<fb-button @on-click="handleQuery" icon="search" type="primary">查 询</fb-button>
				<fb-button @on-click="formReset" icon="search">重 置</fb-button>
			</template>

			<template slot="table">
				<fb-simple-table ref="table" :service="table.service" :param="formData" :pk="table.primaryKey"
					:columns="table.columns" auto-load :multiple="false" :scroll="{ x: 1200, y: 372, autoHeight: true }"
					@on-row-select="handleTableSelect">
					<template v-slot:actions="props">
						<fb-space>
							<fb-button @on-click="handleEdit(props.row)" editor size="s">修改</fb-button>

							<fb-button @on-click="handleDel(props.row)" danger size="s">删除</fb-button>
						</fb-space>
					</template>

					<template v-slot:view="props">
						<fb-link-group>
							<fb-link :click="() => handleView(props.row)" :label="props.row.ruleName"
								type="primary"></fb-link>
						</fb-link-group>
					</template>

					<template v-slot:status="props">
						{{ getStatusText(props.row.status) }}
					</template>

					<template v-slot:allowLogin="props">
						<fb-tag :type="getAllowLoginType(props.row.allowLogin)">{{
							getAllowLoginText(props.row.allowLogin) }}</fb-tag>
					</template>

					<template v-slot:timeRange="props">
						<span>{{ formatTimeRange(props.row) }}</span>
					</template>
				</fb-simple-table>
			</template>
		</fb-page-search>
		<tp-dialog ref="TpDialog" @closeTpDialog="closeDialog"></tp-dialog>
		<tp-dialog-tab ref="TpDialogTab" @closeTpDialog="closeDialogTab"></tp-dialog-tab>
	</div>
</template>

<script>
let timeRuleService = app.$svc.sys.timerule

export default {
	mixins: [

	],
	// 组件
	components: {
		// 'component-a': ComponentA,
	},
	// 初始化方法
	mounted() {
		// 执行界面初始化
		this.createdPage()
	},
	data() {
		return {
			formData: {
				ruleName: '',
				status: '',
				roleNames: '',
				createTime: null,
			},
			// Table列
			table: {
				// 请求的 service
				service: timeRuleService,
				primaryKey: "id",
				columns: [
					{
						name: 'ruleName',
						label: '规则名称',
						slot: 'view',
						sortable: false,
						width: 120
					}, {
						name: 'status',
						label: '是否启用',
						slot: 'status',
						sortable: false,
						width: 80,
						align: 'center',
						titleAlign: 'center'
					},
					{
						name: 'allowLogin',
						label: '是否允许登录',
						slot: 'allowLogin',
						sortable: false,
						width: 100,

						align: 'center',
						titleAlign: 'center'
					}, {
						name: 'timeRange',
						label: '时间范围',
						slot: 'timeRange',
						sortable: false,
						width: 300
					}, {
						name: 'userNames',
						label: '适用人员',
						sortable: false,
						width: 200
					}, {
						name: 'roleNames',
						label: '适用角色',
						sortable: false,
						width: 200
					}, {
						name: 'createUserName',
						label: '创建人',
						sortable: false,
						width: 100
					}, {
						name: 'createTime',
						label: '创建时间',
						sortable: false,
						width: 150
					}, {
						freeze: "right",
						name: '',
						label: '操作',
						sortable: false,
						slot: 'actions',
						width: 120,
					}
				],
			},
		}
	},

	// 方法
	methods: {
		// 列表方法
		handleQuery() {
			this.$refs.table.doSearch()
		},
		// 重置表单
		formReset() {
			this.formData = {
				ruleName: '',
				status: '',
				roleNames: '',
				createTime: null,
			};
			this.$refs.table.doSearch();
		},
		// 新增方法
		handleAdd() {
			let param = {};
			let options = {
				tabChangeConfirm: false,
				callBack: (result) => {
					this.$refs.table.doReload()
				}
			}
			this.$refs.TpDialog.show(import('../../../views/sys/timerule/add.vue'), param, "新增时间段权限规则", options);
		},
		// 修改方法
		handleEdit(row) {
			let param = { "id": row.id, mode: "edit" };
			let options = {
				tabChangeConfirm: false,
				callBack: (result) => {
					this.$refs.table.doReload()
				}
			}
			this.$refs.TpDialog.show(import('../../../views/sys/timerule/add.vue'), param, "修改时间段权限规则", options);
		},
		// 删除方法
		handleDel(row) {
			this.$confirm('确定要删除该时间段权限规则吗？', () => {
				timeRuleService.delete({ "id": row.id }).then((result) => {
					if (result.code == 1) {
						this.$message.success('删除成功');
						this.handleQuery();
					} else {
						// 服务器返回失败
						this.$message.error('删除失败: ' + result.message)
					}
				});
			})
		},
		// 启用/禁用方法
		handleToggleStatus(row) {
			const action = row.status === '1' ? '禁用' : '启用';
			this.$confirm(`确定要${action}该时间段权限规则吗？`, () => {
				timeRuleService.toggleStatus({ "id": row.id, "status": row.status === '1' ? '0' : '1' }).then((result) => {
					if (result.code == 1) {
						this.$message.success(`${action}成功`);
						this.handleQuery();
					} else {
						// 服务器返回失败
						this.$message.error(`${action}失败: ` + result.message)
					}
				});
			})
		},
		// 查看方法
		handleView(row) {
			console.log("查看时间段权限规则...");

			var id = row.id
			var param = { "id": id }

			let options = {
				tabChangeConfirm: false,
				callBack: function (result) {
					console.log("=============回调查看" + result)
				}
			}
			this.$refs.TpDialog.show(import('../../../views/sys/timerule/view.vue'), param, "查看时间段权限规则", options);
		},
		// 表格选择
		handleTableSelect(selection, row) {
			console.log('选择行:', row);
		},
		// 获取状态类型
		getStatusType(status) {
			switch (status) {
				case 1:
				case '1':
					return 'success'; // 启用
				case 0:
				case '0':
					return 'danger'; // 禁用
				default:
					return 'default';
			}
		},
		// 获取状态文本
		getStatusText(status) {
			switch (status) {
				case 1:
				case '1':
					return '启用';
				case 0:
				case '0':
					return '禁用';
				default:
					return '未知';
			}
		},
		// 获取是否允许登录样式
		getAllowLoginType(allowLogin) {
			switch (allowLogin) {
				case 1:
				case '1':
					return 'success'; // 允许
				case 0:
				case '0':
					return 'danger'; // 拒绝
				default:
					return 'default';
			}
		},
		// 获取是否允许登录文本
		getAllowLoginText(allowLogin) {
			switch (allowLogin) {
				case 1:
				case '1':
					return '允许';
				case 0:
				case '0':
					return '拒绝';
				default:
					return '未知';
			}
		},
		// 格式化时间范围
		formatTimeRange(row) {
			if (row.startTime && row.endTime) {
				const formatDateTime = (timeStr) => {
					if (!timeStr || timeStr.length !== 14) return timeStr;
					const year = timeStr.substring(0, 4);
					const month = timeStr.substring(4, 6);
					const day = timeStr.substring(6, 8);
					const hour = timeStr.substring(8, 10);
					const minute = timeStr.substring(10, 12);
					const second = timeStr.substring(12, 14);
					return `${year}-${month}-${day} ${hour}:${minute}:${second}`;
				};
				return `${formatDateTime(row.startTime)}  ~  ${formatDateTime(row.endTime)}`;
			}
			return '-';
		},
		// 页面初始化
		createdPage() {
			console.log('时间段权限规则列表页面初始化');
		},
		// 关闭对话框
		closeDialog(param) {
			this.$refs.table.doReload()
		},
		closeDialogTab(param) {
			this.$refs.table.doReload()
		},
	}
}
</script>

<style lang="less" scoped></style>