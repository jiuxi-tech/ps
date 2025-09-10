<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-dialog-table>
				<template slot="form">
					<fb-form ref="query-form" mode="query" style="padding: 0px" :label-width="80">
						<fb-row>
							<fb-col span="11">
								<fb-form-item label="人员名称">
									<fb-input v-model="formData.personName"></fb-input>
								</fb-form-item>
							</fb-col>
							<fb-col span="11" offset="1">
								<fb-form-item label="所在部门">
									<fb-tree-select v-model="formData.deptId"
									                :service="$svc.sys.dept.ent.allTree"
									                :param="{}"
									                :placeholder="'请选择'"
									                :reader="{value:'id', label: 'text'}"
									                clearable>
									</fb-tree-select>
								</fb-form-item>
							</fb-col>
						</fb-row>
						<fb-row>
							<fb-col span="11">

							</fb-col>
							<fb-col span="12">
								<fb-form-item :content-style="{textAlign: 'right'}">
									<fb-space>
										<fb-button type="primary" icon="search" @on-click="handleQuery">查 询</fb-button>
										<fb-button icon="search" @on-click="formReset">重 置</fb-button>
									</fb-space>
								</fb-form-item>
							</fb-col>
						</fb-row>
					</fb-form>
				</template>

				<template slot="table">
					<fb-card header="人员列表" :body-style="{height: '320px'}">
						<fb-simple-table
							ref="table"
							:service="table.service.ent.list"
							:param="formData"
							:pk="table.primaryKey"
							:columns="table.columns"
							auto-load
							autoSelect
							radio
							:scroll="{x:500, y: 310}"
						></fb-simple-table>
					</fb-card>
				</template>
			</fb-dialog-table>
		</div>

		<div class="tp-dialog-bottom">
			<fb-button style="margin-right: 12px" type="primary" @on-click="add">确定</fb-button>
			<fb-button @on-click="handleClose">关闭</fb-button>
		</div>
	</div>
</template>

<script>

import Page from '@/util/Page'

export default {
	name: "EntPersonSingleSelectUsingTableShow",
	mixins: [
		Page
	],
	components: {},
	// 接收父组件的传参
	props: {
		param: {
			type: Object,
			require: false,
		},
		parentPage: {
			type: Object,
			default: null,
		},
	},
	created() {
		// 当前登录人部门id
		let deptid = this.$datax.get("user").deptIds
		this.formData.deptId = deptid;
	},
	// 初始化方法
	mounted() {
		// 执行界面初始化
		this.createdPage()
	},
	data() {
		return {
			// 已选择的企业id
			checked: [],
			formData: {
				deptId: '',
				personName: '',
			},
			table: {
				// 请求的 url
				service: app.$svc.sys.person,
				primaryKey: "personId",
				columns: [
					{
						name: 'personName',
						label: '用户名称',
						sortable: false,
						width: 100,
					}, {
						name: 'deptSimpleName',
						label: '部门名称',
						sortable: false,
						width: 100
					}, {
						name: 'defaultDeptName',
						label: '是否兼职',
						sortable: false,
						width: 100
					},
					{
						name: 'phone',
						label: '手机号码',
						sortable: false,
						width: 150
					},
				],
			},

		}
	},
	methods: {
		// 列表方法
		handleQuery() {
			this.$refs.table.doSearch()
		},
		// 取消
		handleClose() {
			this.closeTpDialog()
		},
		// 确定
		add() {
			// 单弹出框新增成功，关闭弹出框，param是回传的参数
			let param = {}

			// 获取选中行
			let rows = this.$refs.table.getSelectedRows()
			if (rows.length >= 1) {
				param = rows[0]
			}
			this.closeTpDialog(param)
		},

	},
}
</script>

<style scoped>

</style>
