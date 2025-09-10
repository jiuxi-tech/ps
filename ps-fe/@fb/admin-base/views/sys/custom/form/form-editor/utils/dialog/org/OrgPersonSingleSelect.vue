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
									<fb-tree-select
										ref="deptTree"
										v-model="formData.deptId"
										:service="$svc.sys.dept.org.tree"
										:param="{}"
										:placeholder="'请选择'"
										:reader="{value:'id', label: 'text'}"
										:clearable="false">
									</fb-tree-select>
								</fb-form-item>
							</fb-col>
						</fb-row>
						<fb-row>
							<fb-col span="11">
								<fb-form-item label="部门层级">
									<fb-select v-model="formData.levelFlag"
									           :data="[{value: 0, label: '本级'},
												{value: 1, label: '本级及下级'},]"
									           :placeholder="'请选择'"
									           clearable/>
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
					<fb-card header="人员列表" shadow="off">
						<fb-simple-table
							ref="table"
							:service="table.service.org.list"
							:param="formData"
							:pk="table.primaryKey"
							:columns="table.columns"
							:pager="pager"
							auto-load
							autoSelect
							radio
							:scroll="{  y: 260}"
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



export default {
	name: 'OrgPersonSingleSelect',
	mixins: [

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
	created () {
		// 当前登录人部门id
		let deptid = this.$datax.get('user').deptIds
		this.formData.deptId = deptid
	},
	// 初始化方法
	mounted () {
		// 执行界面初始化
		this.createdPage()
	},
	data () {
		return {
			// 已选择的企业id
			checked: [],
			pager: {
				size: 100,
			},
			formData: {
				levelFlag: 0,
				deptId: '',
				personName: '',
			},
			table: {
				// 请求的 url
				service: this.$svc.sys.person,
				primaryKey: 'personId',
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
						width: 100,
					}, {
						name: 'defaultDeptName',
						label: '是否兼职',
						sortable: false,
						width: 100,
					},
					{
						name: 'phone',
						label: '手机号码',
						sortable: false,
						width: 150,
					},
				],
			},

		}
	},
	methods: {
		// 列表方法
		handleQuery () {
			this.$refs.table.doSearch()
		},
		// 取消
		handleClose () {
			this.closeTpDialog()
		},
		// 确定
		add () {
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

<style scoped lang="less">
// 弹出框高度，宽度，按钮样式控制
.tp-dialog {
	width:  100%;
	height: 100%;

	.tp-dialog-top {
		width:       100%;
		height:      calc(100% - 63px);
		overflow-y:  auto;
		overflow-x:  hidden;
		padding-top: 0;
	}

	.tp-dialog-bottom {
		box-sizing:       border-box;
		text-align:       right;
		background-color: #FFFFFF;
		position:         relative;

		height:           63px;
		bottom:           0;
		left:             0;
		right:            0;
		padding:          16px;
		border-top:       1px solid #eee
	}
}

/deep/ .jpx-simple-table-pager {
	padding: 0 16px;
}
</style>
