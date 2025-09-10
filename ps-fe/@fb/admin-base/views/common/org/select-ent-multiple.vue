<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-dialog-table-select>
				<template slot="form">
					<fb-form ref="query-form" mode="query" style="padding: 0px" :label-width="100">
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
								<fb-form-item label="法定代表人">
									<fb-input v-model="formData.legalRepr"></fb-input>
								</fb-form-item>
							</fb-col>
							<fb-col span="11" offset="1">
								<fb-form-item label="行业类别">
									<fb-tree-select v-model="formData.industryTypeCode"
													:service="$svc.sys.dict.tree"
													:param="{'dicCode': 'SYS16'}"
													:reader="{value: 'id', label: 'text'}"
													placeholder="请选择"
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
										<fb-button icon="search" @on-click="handleResetClick">重 置</fb-button>
									</fb-space>
								</fb-form-item>
							</fb-col>
						</fb-row>
					</fb-form>
				</template>
				<template slot="table">
					<fb-card header="企业列表" :body-style="{height: '280px'}">
						<fb-simple-table ref="table"
										 v-model="checked"
										 :service="table.service.list"
										 :param="formData"
										 :pk="table.primaryKey"
										 :columns="table.columns"
										 :auto-load="false"
										 multiple
										 autoSelect
										 :scroll="{x:1000, y: 186}"
										 @on-row-select="handleRowSelect"
										 @on-all-row-select="handleAllRowSelect"
						></fb-simple-table>
					</fb-card>
				</template>

				<template slot="list">
					<fb-card :header="`已选择企业 (${checked.length})`" :body-style="{height: '280px'}" body-overflow="auto">

						<fb-list :data="list">
							<fb-list-item slot="item" slot-scope="{item, index}">
								<fb-text ellipsis width="180px">
									{{ item.entFullName }}
								</fb-text>
								<fb-icon slot="extra"
										 type="link"
										 size="l"
										 name="jpx-icon-close-circle"
										 color="#313C47"
										 style="cursor:pointer;"
										 @on-click="handleListRemove(item)"
								></fb-icon>
							</fb-list-item>
						</fb-list>
						<fb-empty v-if="list.length === 0" text="请在左侧选择企业"></fb-empty>
					</fb-card>
				</template>
			</fb-dialog-table-select>
		</div>

		<div class="tp-dialog-bottom">
			<fb-button style="margin-right: 12px" type="primary" @on-click="add">确定</fb-button>
			<fb-button @on-click="handleClose">关闭</fb-button>
		</div>
	</div>
</template>

<script>



	export default {
		name: "select-ent-multiple",
		mixins: [],
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
		mounted() {
			this.handleQuery();
			this.initView(this.param);
		},
		data() {
			return {
				// 已选择的企业id
				checked: [],

				list: [],
				formData: {
					entFullName: '',
					entAddrCode: '',
					legalRepr: '',
					industryTypeCode: '',
				},
				table: {
					// 请求的 url
					service: app.$svc.sys.ent,
					primaryKey: "entId",
					columns: [
						{
							name: 'entId',
							label: '企业ID',
							width: 150,
						},
						{
							name: 'entFullName',
							label: '企业名称',
							width: 150,
						}, {
							name: 'prodAddrName',
							label: '行政区划',
							width: 150,
						}, {
							name: 'entUnifiedCode',
							label: '统一社会信用代码',
							width: 150,
						}, {
							name: 'legalRepr',
							label: '法定代表人',
							width: 100,
						},
						{
							name: 'legalReprTel',
							label: '联系电话',
							width: 100,
						},
						{
							name: 'safetyDirectorName',
							label: '安全生产管理员',
							width: 100,
						},
						{
							name: 'safetyDirectorTel',
							label: '安全生产管理员手机',
						},
						{
							name: 'industryTypeCodeName',
							label: '行业类别',
							width: 100,
						},
					],
				},
			}

		},
		methods: {

			// 回显方法
			initView(param) {
				let that = this;
				if (param && param.ents && param.ents.length >= 1) {
					param.ents.forEach(function (node) {
						that.checked.push(node.entId);
						that.list.push(node);
					})
				}
			},

			// 列表方法
			handleQuery() {
				this.$refs.table.doSearch();
			},
			// 取消
			handleClose() {
				this.closeTpDialog()
			},
			// 确定
			add() {
				// 1 单弹出框新增成功，关闭弹出框，param是回传的参数
				let param = this.list
				this.closeTpDialog(param)
			},
			handleResetClick() {
			},
			// 点击行选中
			handleRowSelect(selectId, selectedIds, selectRow, rowIndex, checked) {
				if (selectRow) {
					let inChecked = this.checked.indexOf(selectRow.entId)
					// 选中
					if (checked) {
						this.list.push(selectRow)
					} else {
						// 取消选中remove
						this.list.splice(inChecked, 1)
					}
				}

			},
			// 全选/反选
			handleAllRowSelect() {

			},
			// 从已选框中删除
			handleListRemove(row) {
				let inChecked = this.checked.indexOf(row.entId)
				if (inChecked !== -1) {
					this.checked.splice(inChecked, 1)
					this.list.splice(inChecked, 1)
				}
			},
		},
	}
</script>

<style scoped lang="less">


</style>
