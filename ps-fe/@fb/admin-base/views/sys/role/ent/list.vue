<template>

	<div>
		<fb-page-search>
			<template slot="query">
				<fb-form ref="query-form" mode="query">
					<fb-row>
						<fb-col span="8">
							<fb-form-item label="角色名称">
								<fb-input v-model="formData.roleName"></fb-input>
							</fb-form-item>
						</fb-col>
					</fb-row>
				</fb-form>
			</template>

			<template slot="buttons">
				<fb-button ref="buttonAdd" @on-click="handleAdd" v-permission="'SYS_ENT_ROLE_ADD'" icon="add-circle">
					新增
				</fb-button>
			</template>

			<template slot="actions">
				<fb-button type="primary" icon="search" @on-click="handleQuery">查询</fb-button>
			</template>

			<template slot="table">
				<fb-simple-table
					ref="table"
					:service="table.service.ent.list"
					:param="formData"
					:pk="table.primaryKey"
					:columns="table.columns"
					:multiple="false"
					auto-load
					:formatters="formatters"
					:scroll="{x:1000, y: 330, autoHeight: true}"
					@on-row-select="handleTableSelect">

					<template v-slot:actions="props">
						<fb-space>
							<fb-button @on-click="handleEdit(props.row)" v-permission="'SYS_ENT_ROLE_UPDATE'"
									   editor size="s">修改</fb-button>
							<fb-button @on-click="handleAuth(props.row)" v-permission="'SYS_ENT_ROLE_AUTHADD'"
									   editor size="s">授权</fb-button>
							<fb-button @on-click="handleDel(props.row)" v-permission="'SYS_ENT_ROLE_DELETE'"
									   danger size="s">删除</fb-button>
						</fb-space>
					</template>

					<template v-slot:view="props">
						<fb-link-group>
							<fb-link :click="()=>handleView(props.row)" :label="props.row.roleName"
									 type="primary"></fb-link>
						</fb-link-group>
					</template>
				</fb-simple-table>
			</template>
		</fb-page-search>

		<tp-dialog ref="TpDialog" @closeTpDialog="closeDialog"></tp-dialog>
	</div>
</template>

<script>



	export default {
		name: 'list',
		mixins: [

		],

		// 初始化方法
		mounted() {
			// 执行界面初始化方法

		},
		data() {
			return {
				formData: {
					roleName: '',
					roleType: '',
				},
				formatters: {
					roleType(val){
						return val == 0 ? '政府' : '企业';
					}
				},
				// Table列
				table: {
					// 请求的 url
					service: app.$svc.sys.role,
					primaryKey: "roleId",
					columns: [
						{
							name: 'roleName',
							label: '角色名称',
							slot: 'view',
							sortable: false,
							width: 200,
						}, {
							name: 'roleType',
							label: '角色类型',
							sortable: false,
							width: 200,
						}, {
							name: 'createRoleName',
							label: '创建者角色',
							sortable: false,
							width: 200,
						}, {
							name: 'roleDesc',
							label: '角色描述',
							sortable: false,
							width: 220,
						}, {
							name: 'orderIndex',
							label: '排序',
							sortable: false,
						},
						{
							name: '',
							label: '操作',
							sortable: false,
							slot: 'actions',
							width: 180,
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
				let options = {"height": 350};
				// 打开新增界面弹出窗
				this.$refs.TpDialog.show(import('../../../../views/sys/role/ent/add.vue'), param, "新增", options);
			},
			// 修改方法
			handleEdit(row) {
				let param = {"id": row.roleId, "passKey": row.passKey};
				let options = {"height": 350};
				this.$refs.TpDialog.show(import('../../../../views/sys/role/ent/add.vue'), param, "修改", options);
			},
			// 删除方法
			handleDel(row) {
				this.$confirm('确定要删除吗？', () => {
					app.service.request('/sys/role/delete', {
						method: 'get', // 请求方式 post,get, 默认是 get,
						// `data` 是作为请求主体被发送的数据， post 采用
						params: {"roleId": row.roleId, "creator": row.creator, "passKey": row.passKey},

						// `headers` 是即将被发送的自定义请求头
						headers: {'Content-Type': 'application/x-www-form-urlencoded'},
						// `responseType` 表示服务器响应的数据类型，可以是 'arraybuffer', 'blob', 'document', 'json', 'text', 'stream'
						responseType: 'json', // 默认的
						// `timeout` 指定请求超时的毫秒数(0 表示无超时时间)
						// 如果请求话费了超过 `timeout` 的时间，请求将被中断
						timeout: 5000,
					}).then((result) => {
						if (result.code == 1) {
							this.$message.success('删除成功');
							this.handleQuery();
						} else {
							// 服务器返回失败
							this.$message.error('删除失败: ' + result.message)
						}
					})
				})

			},
			// 授权
			handleAuth(row) {
				let param = {"id": row.roleId, "passKey": row.passKey};
				this.$refs.TpDialog.show(import('../../../../views/sys/role/auth.vue'), param, "授权");
			},
			// 查看方法
			handleView(row) {
				let param = {"id": row.roleId, "passKey": row.passKey}
				let options = {"height": 350};
				this.$refs.TpDialog.show(import('../../../../views/sys/role/view.vue'), param, "查看", options);
			},
			// 下拉回调
			onSelectChange(e) {
				console.log("下拉选择：" + e);
				//this.formData.userGander = e;
			},
			closeDialog(param) {
				this.handleQuery();
			},
		}
	}
</script>

<style lang="less" scoped>

</style>
