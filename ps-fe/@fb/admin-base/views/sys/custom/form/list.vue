<template>
	<div>
		<fb-page-search>
			<template slot="query">
				<fb-form ref="query-form" mode="query" :label-width="160">
					<fb-row>
						<fb-col span="11">
							<fb-form-item label="姓名">
								<fb-input v-model="formData.name"></fb-input>
							</fb-form-item>
							<fb-form-item label="年龄">
								<fb-input v-model="formData.age"></fb-input>
							</fb-form-item>
						</fb-col>
					</fb-row>
				</fb-form>
			</template>

			<template slot="buttons">
				<!--<fb-button ref="buttonAdd" @on-click="handleAdd" v-permission = "'SYS_ENTMANAGE_ENT_ADD'" icon="add-circle">add</fb-button> v-permission指令权限控制-->
				<fb-button ref="buttonAdd" @on-click="handleAdd" icon="add-circle">add</fb-button>
			</template>

			<template slot="actions">
				<fb-button type="primary" icon="search" @on-click="handleQuery">查询</fb-button>
			</template>

			<template slot="table">
				<fb-simple-table
					ref="table"
					:service="table.service.search"
					:param="formData"
					:pk="table.primaryKey"
					:columns="table.columns"
					:multiple="false"
					:scroll="{x:1100, y: 372, autoHeight: true}"
					auto-load>

					<template v-slot:actions="props">
						<fb-space>

							<fb-button @on-click="handleEdit(props.row)" v-permission="''" editor size="s">修改
							</fb-button>
							<fb-button @on-click="handleDel(props.row)" v-permission="''" editor size="s">删除</fb-button>
						</fb-space>
					</template>

					<template v-slot:view="props">
						<fb-link-group>
							<fb-link :click="()=>handleView(props.row)" :label="props.row.mid" type="primary"></fb-link>
						</fb-link-group>
					</template>
				</fb-simple-table>
			</template>
		</fb-page-search>
		<tp-dialog ref="TpDialog" @closeTpDialog="closeDialog"></tp-dialog>
		<tp-dialog-tab ref="TpDialogTab" @closeTpDialog="closeDialogTab"></tp-dialog-tab>
	</div>
</template>

<script>



export default {
	// 组件
	components: {
		// 'component-a': ComponentA,
	},
	// 初始化方法
	mounted () {
		// 执行界面初始化方法
	},
	data () {
		return {
			formData: {
				name: '',
				age: '',
			},
			// Table列
			table: {
				// 请求的 url
				service: app.$svc.core.tpcustomform,

				primaryKey: 'fid',
				columns: [
					{
						name: 'fid',
						label: '表单id',
						sortable: false,
						width: 150,
					},
					{
						name: 'mid',
						label: '模块id',
						sortable: false,
						width: 150,
						slot: 'view',
					},
					{
						name: 'fSource',
						label: '表单归属 app/pc',
						sortable: false,
						width: 150,
					},
					{
						name: 'ftype',
						label: '表单类型 list/edit/view',
						sortable: false,
						width: 150,
					},
					{
						name: 'fjson',
						label: '表单字段',
						sortable: false,
						width: 150,
					},
					{
						name: 'actived',
						label: '有效标志',
						sortable: false,
						width: 150,
					},
					{
						name: 'creator',
						label: '创建人',
						sortable: false,
						width: 150,
					},
					{
						name: 'createTime',
						label: '创建时间',
						sortable: false,
						width: 150,
					},
					{
						name: 'updator',
						label: '更新人',
						sortable: false,
						width: 150,
					},
					{
						name: 'updateTime',
						label: '更新时间',
						sortable: false,
						width: 150,
					},
					{
						name: 'extend01',
						label: '扩展字段01',
						sortable: false,
						width: 150,
					},
					{
						name: 'extend02',
						label: '扩展字段02',
						sortable: false,
						width: 150,
					},
					{
						name: 'extend03',
						label: '扩展字段03',
						sortable: false,
						width: 150,
					},
					{
						name: '',
						label: '操作',
						sortable: false,
						slot: 'actions',
						width: 124,
					},
				],
			},
		}
	},

	// 方法
	methods: {
		// 列表方法
		handleQuery () {
			this.$refs.table.doSearch()
		},
		// 新增方法
		handleAdd () {
			let param = {}
			// this.$refs.TpDialog.show(import('@fb/admin-base/views/core/tpcustomform/add.vue'), param, '新增')
		},
		// 修改方法
		handleEdit (row) {

			let param = {'id': row.fid}

			// this.$refs.TpDialog.show('/core/tpcustomform/add.vue', param, '修改')
		},
		// 删除方法
		handleDel (row) {
			this.$confirm('确定要删除吗？', () => {
				this.table.service.delete({'fids': row.fid}).then((result) => {
					if (result.code == 1) {
						this.$message.success('删除成功')
						this.handleQuery()
					} else {
						// 服务器返回失败
						this.$message.error('删除失败: ' + result.message)
					}
				})
			})
		},
		// 查看方法
		handleView (row) {

			let param = {'id': row.fid}
			// this.$refs.TpDialog.show('/core/tpcustomform/view.vue', param, '查看')
		},

		closeDialog (param) {
			// 单页面弹出框关闭时，回调方法，param是回传的参数
			console.log(param)
			// 刷新列表
			this.handleQuery()
		},
		closeDialogTab (param) {
			// tab切换 页面弹出框关闭时，回调方法，param是回传的参数
			if (param) {
				this.handleQuery()
			}
		},
	},
}
</script>

<style lang="less" scoped>
</style>
