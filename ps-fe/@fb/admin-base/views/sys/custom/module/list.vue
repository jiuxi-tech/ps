<template>
	<div>
		<fb-page-search>
			<template slot="query">
				<fb-form ref="query-form" mode="query" :label-width="160">
					<fb-row>
						<fb-col span="11">
							<fb-form-item label="模块名称">
								<fb-input v-model="formData.name"></fb-input>
							</fb-form-item>
						</fb-col>
					</fb-row>
				</fb-form>
			</template>

			<template slot="buttons">
				<!--<fb-button ref="buttonAdd" @on-click="handleAdd" v-permission = "'SYS_ENTMANAGE_ENT_ADD'" icon="add-circle">add</fb-button> v-permission指令权限控制-->
				<!--				<fb-button ref="buttonAdd" @on-click="handleAdd" icon="add-circle">add</fb-button>-->

				<fb-button ref="buttonAdd" @on-click="handleAdd" icon="add-circle">新增</fb-button>
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
					:scroll="{x:1100, y: 324, autoHeight: true}"
					auto-load
				:formatters="{
						actived(val){
							return val ? '是' : '否';
						}
				}">

					<template v-slot:actions="props">

						<!--						<fb-popup-select :value="">-->
						<!--							<fb-button @on-click="" slot="header">操作</fb-button>-->
						<!--							<fb-container slot="picker">-->
						<fb-space>
							<fb-button @on-click="handleFormDesign(props.row)" editor size="s">设计</fb-button>
							<fb-button @on-click="handleFormTest(props.row)" editor size="s">测试</fb-button>
<!--							<fb-button @on-click="handleFormRun(props.row)" editor size="s">运行</fb-button>-->
							<fb-divider direction="vertical"></fb-divider>
							<fb-button @on-click="handleEdit(props.row)" editor size="s">修改</fb-button>
							<fb-button @on-click="handleDel(props.row)"   size="s" icon="delete" danger></fb-button>
						</fb-space>
						<!--							</fb-container>-->

						<!--						</fb-popup-select>-->

					</template>

					<template v-slot:view="props">
						<fb-link-group>
							<fb-link :click="()=>handleView(props.row)" :label="props.row.mname"
							         type="primary"></fb-link>
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
	mixins: [

	],
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
				service: app.$svc.sys.custom.module,

				primaryKey: 'mid',
				columns: [
//					{
//						name: 'mid',
//						label: '模块id',
//						sortable: false,
//						width: 150,
//					},
					{
						name: 'mname',
						label: '模块名称',
						sortable: false,
						slot: 'view',
					},
					{
						name: 'mcode',
						label: '模块编码',
						sortable: false,
						width: 150,
					},
					{
						name: 'mdesc',
						label: '模块简介',
						sortable: false,
						width: 150,
					},
					{
						name: 'actived',
						label: '有效标志',
						sortable: false,
						width: 150,
					},
//					{
//						name: 'creator',
//						label: '创建人',
//						sortable: false,
//						width: 150,
//					},
//					{
//						name: 'createTime',
//						label: '创建时间',
//						sortable: false,
//						width: 150,
//					},
//					{
//						name: 'updator',
//						label: '更新人',
//						sortable: false,
//						width: 150,
//					},
//					{
//						name: 'updateTime',
//						label: '更新时间',
//						sortable: false,
//						width: 150,
//					},
//					{
//						name: 'extend01',
//						label: '扩展字段01',
//						sortable: false,
//						width: 150,
//					},
//					{
//						name: 'extend02',
//						label: '扩展字段02',
//						sortable: false,
//						width: 150,
//					},
//					{
//						name: 'extend03',
//						label: '扩展字段03',
//						sortable: false,
//						width: 150,
//					},
					{
						freeze: 'right',
						name: '',
						label: '操作',
						sortable: false,
						slot: 'actions',
						width: 200,
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
			this.$refs.TpDialog.show(import('../../../../views/sys/custom/module/add.vue'), param, '新增')
		},
		// 修改方法
		handleEdit (row) {

			let param = {'id': row.mid}

			this.$refs.TpDialog.show(import('../../../../views/sys/custom/module/add.vue'), param, '修改')
		},
		// 删除方法
		handleDel (row) {
			this.$confirm('确定要删除吗？', () => {
				this.table.service.delete({'mids': row.mid}).then((result) => {
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

			let param = {'id': row.mid}
			this.$refs.TpDialog.show(import('../../../../views/sys/custom/module/view.vue'), param, '查看')
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

		handleFormDesign (param) {
			this.$router.push({
				path: '/sys/custom/form/design', // 配置的路由
				query: {
					mid: param.mid,
					tabLabel: `设计-${param.mname}`,
				},
				params: param,
			})
		},

		// 设计者测试
		handleFormTest (param) {
			this.$router.push({
				path: `/sys/custom/form/runtime-test/${param.mid}`, // 配置的路由
				query: {
					tabLabel: `测试-${param.mname}`,
				},
				params: param,
			})
		},

		// 实际使用
		handleFormRun (param) {
			this.$router.push({
				path: `/sys/custom/form/runtime-list/${param.mcode}`, // 配置的路由
				query: {
					tabLabel: `运行-${param.mname}`,
				},
				params: param,
			})
		},
	},
}
</script>

<style lang="less" scoped>
</style>
