<template>
	<div>
		<fb-page-search>
			<template slot="query">
				<fb-form ref="query-form" mode="query">
					<fb-row>
						<fb-col span="24">
							<fb-form-item label="应用ID">
								<fb-input v-model="formData.search" placeholder="请输入应用ID或名称"></fb-input>
							</fb-form-item>
						</fb-col>
					</fb-row>
				</fb-form>
			</template>

			<template slot="buttons">
				<fb-button ref="buttonAdd" @on-click="handleAdd" icon="add-circle">
					注册应用
				</fb-button>
			</template>

			<template slot="actions">
				<fb-button type="primary" icon="search" @on-click="handleQuery">查询</fb-button>
			</template>

			<template slot="table">
				<fb-simple-table
					ref="table"
					:service="table.service"
					:param="formData"
					:pk="table.primaryKey"
					:columns="table.columns"
					:multiple="false"
					auto-load
					:formatters="formatters"
					:sorters="table.sorters"
					:scroll="{x:1400, y: 330, autoHeight: true}"
					@on-row-select="handleTableSelect">

					<template v-slot:actions="props">
						<fb-space>
							<fb-button @on-click="handleEdit(props.row)" 
									   editor size="s">修改</fb-button>
							<fb-button @on-click="handleDel(props.row)"  
									   danger size="s">删除</fb-button>
						</fb-space>
					</template>

					<template v-slot:view="props">
						<fb-link-group>
							<fb-link :click="()=>handleView(props.row)" :label="props.row.clientId" type="primary"></fb-link>
						</fb-link-group>
					</template>

					<template v-slot:enabled="props">
						<span :class="props.row.enabled ? 'status-enabled' : 'status-disabled'">
							{{ props.row.enabled ? '启用' : '禁用' }}
						</span>
					</template>

				</fb-simple-table>
			</template>
		</fb-page-search>

		<tp-dialog ref="TpDialog" @closeTpDialog="closeDialog"></tp-dialog>
	</div>
</template>

<script>
	export default {
		name: 'ClientList',
		mixins: [],

		// 初始化方法
		mounted() {
			// 执行界面初始化方法
		},
		data() {
			return {
				formData: {
					search: '', // 搜索关键词
				},

				formatters: {
					enabled(val) {
						return val ? '启用' : '禁用';
					},
					protocol(val) {
						return val || 'openid-connect';
					},
					publicClient(val) {
						return val ? '公共应用' : '机密应用';
					}
				},

				// Table列
				table: {
					// 请求的 service
					service: app.$svc.sys.sso.admin.client,
					//  {
					// 	// list: {
					// 	// 	url: '/sys/sso/admin/client/list',
					// 	// 	method: 'get'
					// 	// }
					// },
					primaryKey: "id",
					sorters: {
						// clientId: {},
					},
					columns: [
						{
							name: 'clientId',
							label: '应用ID',
							slot: 'view',
							width: 200,
						}, {
							name: 'name',
							label: '应用名称',
							sortable: false,
							width: 200,
						}, {
							name: 'description',
							label: '应用描述',
							sortable: false,
						}, {
							name: 'protocol',
							label: '协议',
							sortable: false,
							width: 120,
						}, {
							name: 'publicClient',
							label: '应用类型',
							sortable: false,
							width: 120,
						}, {
							name: 'enabled',
							label: '状态',
							slot: 'enabled',
							sortable: false,
							width: 100,
						}, {
							name: 'baseUrl',
							label: '基础URL',
							sortable: false,
						},
						{
							freeze: "right",
							name: '',
							label: '操作',
							sortable: false,
							slot: 'actions',
							width: 150,
						},
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

			// 新增方法
			handleAdd() {
				let param = {};
				let options = {"width": 800, "height": 600};
				// 打开新增界面弹出窗
				this.$refs.TpDialog.show(import('./add.vue'), param, "新增应用", options);
			},
			// 修改方法
			handleEdit(row) {
				let param = {"id": row.id, "clientId": row.clientId};
				let options = {"width": 800, "height": 600};

				this.$refs.TpDialog.show(import('./add.vue'), param, "修改应用", options);
			},
			// 删除方法
			handleDel(row) {
				this.$confirm('确定要删除该应用吗？删除后将无法恢复！', () => {
					this.delete(row.id);
				})
			},
			delete(clientId) {
				// 真正执行删除操作
				app.$svc.sys.sso.admin.client.delete(clientId).then((result) => {
					if (result.code == 1) {
						this.$message.success('删除成功');
						this.handleQuery();
					} else {
						// 服务器返回失败
						this.$message.error('删除失败: ' + result.message)
					}
				})
			},

			// 查看方法
			handleView(row) {
				let param = {"id": row.id, "clientId": row.clientId, "readonly": true}
				let options = {"width": 800, "height": 600};
				this.$refs.TpDialog.show(import('./add.vue'), param, "查看应用", options);
			},
			// 下拉回调
			onSelectChange(e) {
				console.log("下拉选择：" + e);
			},
			closeDialog(param) {
				this.handleQuery();
			},
			handleTableSelect(row) {
				// 表格行选择事件
			}
		}
	}
</script>

<style lang="less" scoped>
	.status-enabled {
		color: #52c41a;
		font-weight: bold;
	}

	.status-disabled {
		color: #ff4d4f;
		font-weight: bold;
	}
</style>