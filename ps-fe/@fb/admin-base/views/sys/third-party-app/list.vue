<template>
	<div>
		<fb-page-search>
			<template slot="query">
				<fb-form ref="query-form" mode="query">
					<fb-row>
						<fb-col span="16">
							<fb-form-item label="应用名称">
								<fb-input v-model="formData.appName" clearable></fb-input>
							</fb-form-item>
						</fb-col>
						<fb-col span="8">
							<fb-form-item label="状态">
								<fb-select v-model="formData.status" clearable :data="statusOptions"></fb-select>
							</fb-form-item>
						</fb-col>
					</fb-row>
				</fb-form>
			</template>

			<template slot="buttons">
				<fb-button ref="buttonAdd" @on-click="handleAdd" icon="add-circle">
					新增应用
				</fb-button>
			</template>

			<template slot="actions">
				<fb-button type="primary" icon="search" @on-click="handleQuery">查询</fb-button>
			</template>

			<template slot="table">
				<fb-simple-table
					ref="table"
					:service="table.service.list"
					:param="formData"
					:pk="table.primaryKey"
					:columns="table.columns"
					:multiple="false"
					auto-load
					:formatters="formatters"
					:scroll="{x:900, y: 330, autoHeight: true}"
					@on-row-select="handleTableSelect">

					<template v-slot:actions="props">
						<fb-space>
							<fb-button @on-click="handleEdit(props.row)" 
									   editor size="s">编辑</fb-button>
							<fb-button @on-click="handlePermission(props.row)" 
									   type="primary" size="s">权限</fb-button>
<!--							<fb-button @on-click="handleViewSecret(props.row)" -->
<!--									   size="s">密钥</fb-button>-->
							<!-- <fb-button @on-click="handleToggleStatus(props.row)" 
									   :type="props.row.status === 1 ? 'warning' : 'success'" 
									   size="s">{{ props.row.status === 1 ? '禁用' : '启用' }}</fb-button> -->
							<fb-button @on-click="handleDel(props.row)"  
									   danger size="s">删除</fb-button>
						</fb-space>
					</template>

					<template v-slot:view="props">
						<fb-link-group>
							<fb-link :click="()=>handleView(props.row)" :label="props.row.appName" type="primary"></fb-link>
						</fb-link-group>
					</template>

					<template v-slot:status="props">
						<span :class="props.row.status === 1 ? 'status-valid' : 'status-invalid'">
							{{ props.row.status === 1 ? '启用' : '禁用' }}
						</span>
					</template>

					<template v-slot:apiKey="props">
						<span style="font-family:'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;">{{ maskApiKey(props.row.apiKey) }}</span>
					</template>

				</fb-simple-table>
			</template>
		</fb-page-search>

		<tp-dialog ref="TpDialog" @closeTpDialog="closeDialog"></tp-dialog>
<!--		<tp-dialog ref="SecretDialog" @closeTpDialog="closeSecretDialog"></tp-dialog>-->
		<tp-dialog ref="PermissionDialog" @closeTpDialog="closePermissionDialog"></tp-dialog>
	</div>
</template>

<script>
	import dayjs from "dayjs";

	export default {
		name: 'list',
		mixins: [],

		// 初始化方法
		mounted() {
			// 执行界面初始化方法
		},
		data() {
			return {
				formData: {
					appName: '',
					status: null,
					logDelete: 0, // 只查询未删除的应用
				},

				statusOptions: [
					{ value: 1, label: '启用' },
					{ value: 0, label: '禁用' },
				],

				formatters: {
					status(val) {
						return val === 1 ? '启用' : '禁用';
					},
					expireTime(val) {
						if (!val || val === '' || val === null) {
							return '永不过期';
						}
						try {
							return dayjs(val, 'YYYYMMDDHHmmss').format('YYYY-MM-DD HH:mm:ss');
						} catch (e) {
							return '永不过期';
						}
					},
					createTime(val) {
						if (!val || val === '' || val === null) {
							return '-';
						}
						return dayjs(val, 'YYYYMMDDHHmmss').format('YYYY-MM-DD HH:mm:ss');
					},
					updateTime(val) {
						if (!val || val === '' || val === null) {
							return '-';
						}
						return dayjs(val, 'YYYYMMDDHHmmss').format('YYYY-MM-DD HH:mm:ss');
					}
				},

				// Table列
				table: {
					// 请求的 url
					service: app.$svc.sys.thirdPartyApp,
					primaryKey: "appId",
					columns: [
						{
							name: 'appName',
							label: '应用名称',
							slot: 'view',
							sortable: false,
							width: 150,
						}, {
							name: 'apiKey',
							label: 'API Key',
							slot: 'apiKey',
							sortable: false,
						}, {
							name: 'status',
							label: '状态',
							slot: 'status',
							sortable: false,
							width: 80,
						}, {
							name: 'expireTime',
							label: '过期时间',
							sortable: false,
							width: 150,
						}, {
							name: 'createPersonName',
							label: '创建人',
							sortable: false,
							width: 100,
						}, {
							name: 'createTime',
							label: '创建时间',
							sortable: false,
							width: 150,
						},
						{
							freeze: "right",
							name: '',
							label: '操作',
							sortable: false,
							slot: 'actions',
							width: 160,
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

			// 脱敏API Key
			maskApiKey(apiKey) {
				if (!apiKey || apiKey.length <= 8) {
					return apiKey;
				}
				return apiKey.substring(0, 8) + '****' + apiKey.substring(apiKey.length - 4);
			},

			// 新增方法
			handleAdd() {
				let param = {};
				let options = {"height": 500, "width": 700};
				// 打开新增界面弹出窗，传递 meta 参数标识为新增操作
				this.$refs.TpDialog.show(import('./add.vue'), param, "新增第三方应用", options, { action: 'add' });
			},
			// 修改方法
			handleEdit(row) {
				let param = {"id": row.appId, "passKey": row.passKey};
				let options = {"height": 500, "width": 700};

				// 打开修改界面弹出窗，传递 meta 参数标识为修改操作
				this.$refs.TpDialog.show(import('./add.vue'), param, "编辑第三方应用", options, { action: 'edit' });
			},
			// 删除方法
			handleDel(row) {
				this.$confirm('确定要删除该应用吗？删除后将无法恢复！', () => {
					this.delete(row.appId, row.passKey);
				})
			},
			delete(appId, passKey) {
				// 真正执行删除操作
				app.service.request('/sys/third-party-app/delete', {
					method: 'get',
					params: {"appId": appId, "passKey": passKey},
					headers: {'Content-Type': 'application/x-www-form-urlencoded'},
					responseType: 'json',
					 
				}).then((result) => {
					if (result.code == 1) {
						this.$message.success('删除成功');
						// 删除成功后使用 doReload 刷新当前页
						this.$refs.table.doReload();
					} else {
						// 服务器返回失败
						this.$message.error('删除失败: ' + result.message)
					}
				})
			},

			// 查看方法
			handleView(row) {
				let param = {"id": row.appId, "passKey": row.passKey}
				let options = {"height": 500, "width": 700};
				// 打开查看界面弹出窗，传递 meta 参数标识为查看操作
				this.$refs.TpDialog.show(import('./view.vue'), param, "查看第三方应用", options, { action: 'view' });
			},

			// 查看密钥
			handleViewSecret(row) {
				let param = {"id": row.appId, "passKey": row.passKey}
				let options = {"height": 300, "width": 600};
				// 打开查看密钥弹出窗
				this.$refs.SecretDialog.show(import('./secret.vue'), param, "查看API密钥", options, { action: 'secret' });
			},

			// 权限配置
			handlePermission(row) {
				let param = {"id": row.appId, "passKey": row.passKey, "appName": row.appName}
				let options = {"height": 600, "width": 900};
				// 打开权限配置弹出窗
				this.$refs.PermissionDialog.show(import('./permission.vue'), param, "API权限配置 - " + row.appName, options, { action: 'permission' });
			},

			// 启用/禁用应用
			handleToggleStatus(row) {
				const newStatus = row.status === 1 ? 0 : 1;
				const action = newStatus === 1 ? '启用' : '禁用';
				
				this.$confirm(`确定要${action}该应用吗？`, () => {
					app.service.request('/third-party-app/update-status', {
						method: 'post',
						transformRequest: [
							function (data) {
								let ret = ''
								for (let it in data) {
									ret += encodeURIComponent(it) + '=' +
										encodeURIComponent(data[it]) + '&'
								}
								ret = ret.substring(0, ret.lastIndexOf('&'))
								return ret
							},
						],
						data: {
							"appId": row.appId, 
							"passKey": row.passKey, 
							"status": newStatus
						},
						headers: {'Content-Type': 'application/x-www-form-urlencoded'},
						responseType: 'json',
						 
					}).then((result) => {
						if (result.code == 1) {
							this.$message.success(`${action}成功`);
							// 刷新当前页
							this.$refs.table.doReload();
						} else {
							// 服务器返回失败
							this.$message.error(`${action}失败: ` + result.message)
						}
					})
				})
			},

			// 下拉回调
			onSelectChange(e) {
				console.log("下拉选择：" + e);
			},
			closeDialog(result) {
				// 根据不同操作类型决定列表刷新方式
				if (!result || !result.success) {
					// 直接关闭或操作失败，不刷新列表
					return
				}
				
				if (result.action === 'add') {
					// 新增成功：重新查询（定位到第一页）
					this.$refs.table.doSearch()
				} else if (result.action === 'edit') {
					// 修改成功：刷新当前页
					this.$refs.table.doReload()
				}
				// 查看操作（action === 'view'）不刷新列表
			},
			closeSecretDialog(result) {
				// 查看密钥窗口关闭，不需要刷新列表
			},
			closePermissionDialog(result) {
				// 权限配置窗口关闭，不需要刷新列表
			},
			handleTableSelect(row) {
				// 表格行选择事件
			}
		}
	}
</script>

<style lang="less" scoped>
	.status-valid {
		color: #52c41a;
		font-weight: bold;
	}

	.status-invalid {
		color: #ff4d4f;
		font-weight: bold;
	}
</style>
