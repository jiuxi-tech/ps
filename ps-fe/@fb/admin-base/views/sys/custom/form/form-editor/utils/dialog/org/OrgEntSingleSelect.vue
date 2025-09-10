<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-dialog-table>
				<template slot="form">
					<fb-form ref="query-form" mode="query" style="padding: 0px" :label-width="80">
						<fb-row>
							<fb-col span="11">
								<fb-form-item label="企业名称">
									<fb-input v-model="formData.entFullName"></fb-input>
								</fb-form-item>
							</fb-col>
							<fb-col span="11" offset="1">
								<fb-form-item label="行政区划">
									<fb-tree-select v-model="formData.entAddrCode"
									                :service="$svc.sys.city.tree"
									                :param="{'sync': 1, 'expand': true, 'cityId': '-1'}"
									                :reader="{value:'id', label: 'text'}"
									                :placeholder="'请选择'"
									                :header-format="cityTreeHeaderFormat"
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
					<fb-card header="企业列表" no-padding shadow="off">
						<fb-simple-table
							ref="table"
							:service="table.service.list"
							:param="formData"
							:pk="table.primaryKey"
							:columns="table.columns"
							auto-load
							autoSelect
							radio
							:scroll="{y: 230, fillY: true}"
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
	name: 'OrgEntSingleSelect',
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
			formData: {
				entFullName: '',
				entAddrCode: '',
			},
			// Table列
			table: {
				// 请求的 url
				service: app.$svc.sys.ent,
				primaryKey: 'entId',
				columns: [
					{
						name: 'entFullName',
						label: '企业名称',
					}, {
						name: 'prodAddrName',
						label: '行政区划',
						width: 200,
					}, {
						name: 'industryTypeCodeName',
						label: '行业类别',
						width: 200,
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
