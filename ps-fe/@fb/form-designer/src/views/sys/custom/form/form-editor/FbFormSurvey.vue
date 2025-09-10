<template>
	<fb-container class="form-add">
		<fb-container class="form-add-body">
			<fb-container mb="70px" width="100%" :style="param.styles">

				<fb-form ref="form" v-if="ready">

					<fb-empty v-if="add.rows.length === 0" text="新增表单无数据"/>

					<template v-for="(row, rowIndex) in add.rows">

						<!----------------------------------
						   单列 布局
						 ---------------------------------->
						<template v-if="row.type === 'row-24'">
							<fb-row>
								<template v-if="row.cols && row.cols[0]">
									<fb-form-editor-col-add-run
										:col="row.cols[0]"
										row-type="row-24"
										:formData="formData"
										:param="param"
									/>
								</template>
							</fb-row>
						</template>

						<!----------------------------------
						   双列 布局
						---------------------------------->
						<template v-if="row.type === 'row-12'">
							<fb-row>

								<template v-if="row.cols && row.cols[0]">
									<fb-form-editor-col-add-run
										:col="row.cols[0]"
										row-type="row-12"
										:formData="formData"
										:param="param"/>
								</template>

								<template v-if="row.cols && row.cols[1]">
									<fb-form-editor-col-add-run
										:col="row.cols[1]"
										row-type="row-12"
										:formData="formData"
										:param="param"/>
								</template>
							</fb-row>
						</template>

					</template>
				</fb-form>
			</fb-container>
		</fb-container>

		<fb-container class="form-add-bottom">
			<fb-button style="margin-right: 12px" type="primary" @on-click="handleAdd">保存</fb-button>
			<fb-button @on-click="handleClose">关闭</fb-button>
		</fb-container>

		<tp-dialog ref="TpDialog"></tp-dialog>
	</fb-container>
</template>

<script>
import {
	arrayToObject,
	uniqAndCleanArray,
	toRaw,
	computeCurrentCtx,
} from './utils/utils'
import FbFormEditorColAddRun from './FbFormEditorColAddRun'
import Page from '@/util/Page'
import { get, slice } from 'lodash-es'
import { closest } from '@/util/componentUtils'

/**
 * FbFormAdd
 * (c) 2021 lincong1987
 */

export default {
	name: 'FbFormAdd',
	components: {
		FbFormEditorColAddRun,
	},
	provide () {
		return {
			formAdd: this,
		}
	},
	inject: ['formEditor'],
	mixins: [
		Page,
	],
	// 接收父组件的传参
	props: {
		param: {
			type: Object,
			require: false,
			default () {
				return {}
			},
		},
		parentPage: {
			type: Object,
			default: null,
		},

		mid: {
			type: [Number, String],
			default: '',
		},

	},

	data () {

		let userInfo = this.$datax.get('userInfo')
		let ctx = {
			...toRaw(userInfo),
		}

		return {
			ready: false,

			ctx,

			// 表单数据
			initFormData: {
				value: {},
				label: {},
			},
			// 表单数据
			formData: {
				value: {},
				label: {},
			},

			// 表单数据
			formDataValue: {},
			formDataLabel: {},

			form: {
				props: {
					name: '',
					code: '',
				},
			},
			add: {

				props: {
					labelWidth: 120,
					labelPosition: null,
				},
				rows: [],
			},

			// 请求的 service
			service: this.$svc.sys.custom['c-json'],

		}
	},

	watch: {},

	methods: {
		arrayToObject,
		uniqAndCleanArray,

		computeCurrentCtx (exp) {
			return computeCurrentCtx.apply(this, [exp])
		},

		updatePathProps () {

			let arr = Array.prototype.slice.call(arguments)
			let path = slice(arr, 1, arr.length - 1)

			this.$set(
				get(this, path),
				arr[arr.length - 1],
				arr[0],
			)

			this.$forceUpdate()
		},

		/**
		 * 显示窗口
		 * param 参数
		 * title 标题
		 */
		init (param) {
			// 有值表示是修改方法
			if (param.id) { // 传ID表示修改
				this.param.id = param.id
				this.edit()
			} else {
				this.formData = this.initFormData
				this.$nextTick(() => {
					this.ready = true
				})
			}
		},

		// 取消
		handleClose () {
			// 隐藏
			let tpDialog = closest(this, 'tp-dialog')
			tpDialog && tpDialog.closeTpDialog(this.param.id)
		},

		handleAdd () {
			let that = this

			// 界面校验
			this.$refs.form.validate((result) => {
				if (result === true) {
					if (that.param.id) {
						// 调用修改service方法
						that.service.update({
							dataDetail: JSON.stringify(that.formData),
							fid: that.add.props.fid,
							id: that.param.id,
						}).then((result) => {
							// 判断code
							if (result.code === 1) {
								that.$message.success('修改成功')
								this.handleClose(that.param.id)
							} else {
								// 服务器返回失败
								that.$message.error('修改失败:' + result.message)
							}

							that.updateCount = 0
						})
					} else {
						// 调用新增service方法
						that.service.add({
							dataDetail: JSON.stringify(that.formData),
							fid: that.add.props.fid,
						}).then((result) => {
							// 判断code
							if (result.code === 1) {
								that.$message.success('新增成功')
								this.handleClose(result.data.id)
							} else {
								// 服务器返回失败
								that.$message.error('新增失败:' + result.message)
							}

							that.updateCount = 0
						})
					}
				}
			})
		},

		// 查询信息
		edit () {
			// 调用新增service方法
			this.service.view({
				fid: this.add.props.fid,
				id: this.param.id,
			}).then((result) => {
				// 判断code
				if (result.code === 1) {

					this.formData = JSON.parse(result.data.dataDetail)
					this.$nextTick(() => {
						this.updateFormCtx()
						this.$nextTick(() => {
							this.ready = true
						})
					})
					// this.$set(this, 'formData', JSON.parse(result.data.dataDetail))
				} else {
					// 服务器返回失败
					this.$message.error('错误提示:' + result.message)
				}
			})
		},

		updateFormCtx () {
			this.add.rows.forEach((n, i) => {
				if (n.type === 'row-24' && n.cols[0].type) {
					let col = n.cols[0]
					let colName = col.type && col.formItemProps && col.formItemProps.name
					if (typeof col.props.modifiable !== 'undefined' && col.props.modifiable === true) {
						if (col.type === 'biz-current-ctx') {
							let bizCurrentCtxValue = this.computeCurrentCtx(col.props.expression)
							this.formData.label[colName] = bizCurrentCtxValue
							this.formData.value[colName] = bizCurrentCtxValue
						}
						if (col.type === 'biz-current-person') {
							this.formData.label[colName] = this.ctx.personName
							this.formData.value[colName] = this.ctx.personId
						}
						if (col.type === 'biz-current-dept') {
							this.formData.label[colName] = this.ctx.deptFullName
							this.formData.value[colName] = this.ctx.deptId
						}
						if (col.type === 'biz-current-org') {
							this.formData.label[colName] = this.ctx.ascnName
							this.formData.value[colName] = this.ctx.ascnId
						}
						if (col.type === 'biz-current-date-time') {
							let bizCurrentDateTimeValue = this.$dayjs().format(col.props.format)
							this.formData.label[colName] = bizCurrentDateTimeValue
							this.formData.value[colName] = bizCurrentDateTimeValue
						}
					}
				}
				if (n.type === 'row-12') {
					if (n.cols[0].type) {
						let col = n.cols[0]
						let colName = col.type && col.formItemProps && col.formItemProps.name
						if (typeof col.props.modifiable !== 'undefined' && col.props.modifiable === true) {
							if (col.type === 'biz-current-ctx') {
								let bizCurrentCtxValue = this.computeCurrentCtx(col.props.expression)
								this.formData.label[colName] = bizCurrentCtxValue
								this.formData.value[colName] = bizCurrentCtxValue
							}
							if (col.type === 'biz-current-person') {
								this.formData.label[colName] = this.ctx.personName
								this.formData.value[colName] = this.ctx.personId
							}
							if (col.type === 'biz-current-dept') {
								this.formData.label[colName] = this.ctx.deptFullName
								this.formData.value[colName] = this.ctx.deptId
							}
							if (col.type === 'biz-current-org') {
								this.formData.label[colName] = this.ctx.ascnName
								this.formData.value[colName] = this.ctx.ascnId
							}
							if (col.type === 'biz-current-date-time') {
								let bizCurrentDateTimeValue = this.$dayjs().format(col.props.format)
								this.formData.label[colName] = bizCurrentDateTimeValue
								this.formData.value[colName] = bizCurrentDateTimeValue
							}
						}
					}
					if (n.cols[1].type) {
						let col = n.cols[1]
						let colName = col.type && col.formItemProps && col.formItemProps.name
						if (typeof col.props.modifiable !== 'undefined' && col.props.modifiable === true) {
							if (col.type === 'biz-current-ctx') {
								let bizCurrentCtxValue = this.computeCurrentCtx(col.props.expression)
								this.formData.label[colName] = bizCurrentCtxValue
								this.formData.value[colName] = bizCurrentCtxValue
							}
							if (col.type === 'biz-current-person') {
								this.formData.label[colName] = this.ctx.personName
								this.formData.value[colName] = this.ctx.personId
							}
							if (col.type === 'biz-current-dept') {
								this.formData.label[colName] = this.ctx.deptFullName
								this.formData.value[colName] = this.ctx.deptId
							}
							if (col.type === 'biz-current-org') {
								this.formData.label[colName] = this.ctx.ascnName
								this.formData.value[colName] = this.ctx.ascnId
							}
							if (col.type === 'biz-current-date-time') {
								let bizCurrentDateTimeValue = this.$dayjs().format(col.props.format)
								this.formData.label[colName] = bizCurrentDateTimeValue
								this.formData.value[colName] = bizCurrentDateTimeValue
							}
						}
					}
				}
			})
		},
	},

	async mounted () {

		let mid = this.mid

		let formProps = await this.$svc.sys.custom.module.view({mid}).then(res => res.data)

		this.form.props = {
			mid: formProps.mid,
			name: formProps.mname,
			code: formProps.mcode.replace('C_JSON_', ''),
		}

		let add = await this.$svc.sys.custom.form.view({
			mid: formProps.mid,
			ftype: 'edit',
			fSource: 'pc',
		})

		if (add.data === '' || add.data.fjson === '') {
			this.$message.error('此表单未定义！')
		} else {
			this.add = JSON.parse(add.data.fjson)
		}

		if (add.data.fid) {
			this.add.props.fid = add.data.fid
		}

		let formData = {
			value: {},
			label: {},
			raw: {},
		}

		this.add.rows.forEach((n, i) => {
			if (n.type === 'row-24' && n.cols[0].type) {
				let col = n.cols[0]

				let colName = col.type && col.formItemProps && col.formItemProps.name
				formData.value[colName] = ''
				formData.label[colName] = ''
				formData.raw[colName] = []
				if (col.type === 'fb-checkbox-group') {
					formData.value[colName] = []
				}
				if (col.type === 'tp-upload') {
					formData.value[colName] = {
						referType: '',
						fileList: [],
					}
					formData.label[colName] = {
						referType: '',
						fileList: [],
					}
				}

				if (col.type === 'biz-org-person-single-select') {

				}
				if (col.type === 'biz-org-person-multiple-select') {
					formData.raw[colName] = []
				}

				if (col.type === 'biz-current-ctx') {
					let bizCurrentCtxValue = this.computeCurrentCtx(col.props.expression)
					formData.label[colName] = bizCurrentCtxValue
					formData.value[colName] = bizCurrentCtxValue
				}
				if (col.type === 'biz-current-person') {
					formData.label[colName] = this.ctx.personName
					formData.value[colName] = this.ctx.personId
				}
				if (col.type === 'biz-current-dept') {
					formData.label[colName] = this.ctx.deptFullName
					formData.value[colName] = this.ctx.deptId
				}
				if (col.type === 'biz-current-org') {
					formData.label[colName] = this.ctx.ascnName
					formData.value[colName] = this.ctx.ascnId
				}
				if (col.type === 'biz-current-date-time') {
					let bizCurrentDateTimeValue = this.$dayjs().format(col.props.format)
					formData.label[colName] = bizCurrentDateTimeValue
					formData.value[colName] = bizCurrentDateTimeValue
				}
			}
			if (n.type === 'row-12') {
				if (n.cols[0].type) {
					let col = n.cols[0]
					let colName = col.type && col.formItemProps && col.formItemProps.name
					formData.value[colName] = ''
					formData.label[colName] = ''
					if (col.type === 'fb-checkbox-group') {
						formData.value[colName] = []
					}
					if (col.type === 'tp-upload') {
						formData.value[colName] = {
							referType: '',
							fileList: [],
						}
						formData.label[colName] = {
							referType: '',
							fileList: [],
						}
					}
					if (col.type === 'biz-org-person-single-select') {

					}
					if (col.type === 'biz-org-person-multiple-select') {
						//formData.raw[colName] = []
					}

					if (col.type === 'biz-current-ctx') {
						let bizCurrentCtxValue = this.computeCurrentCtx(col.props.expression)
						formData.label[colName] = bizCurrentCtxValue
						formData.value[colName] = bizCurrentCtxValue
					}
					if (col.type === 'biz-current-person') {
						formData.label[colName] = this.ctx.personName
						formData.value[colName] = this.ctx.personId
					}
					if (col.type === 'biz-current-dept') {
						formData.label[colName] = this.ctx.deptFullName
						formData.value[colName] = this.ctx.deptId
					}
					if (col.type === 'biz-current-org') {
						formData.label[colName] = this.ctx.ascnName
						formData.value[colName] = this.ctx.ascnId
					}
					if (col.type === 'biz-current-date-time') {
						let bizCurrentDateTimeValue = this.$dayjs().format(col.props.format)
						formData.label[colName] = bizCurrentDateTimeValue
						formData.value[colName] = bizCurrentDateTimeValue
					}
				}
				if (n.cols[1].type) {
					let col = n.cols[1]
					let colName = col.type && col.formItemProps && col.formItemProps.name
					formData.value[colName] = ''
					formData.label[colName] = ''
					if (col.type === 'fb-checkbox-group') {
						formData.value[colName] = []
					}
					if (col.type === 'tp-upload') {
						formData.value[colName] = {
							referType: '',
							fileList: [],
						}
						formData.label[colName] = {
							referType: '',
							fileList: [],
						}
					}
					if (n.cols[0].type === 'biz-org-person-single-select') {

					}
					if (n.cols[0].type === 'biz-org-person-multiple-select') {
						//formData.raw[colName] = []
					}

					if (col.type === 'biz-current-ctx') {
						let bizCurrentCtxValue = this.computeCurrentCtx(col.props.expression)
						formData.label[colName] = bizCurrentCtxValue
						formData.value[colName] = bizCurrentCtxValue
					}
					if (col.type === 'biz-current-person') {
						formData.label[colName] = this.ctx.personName
						formData.value[colName] = this.ctx.personId
					}
					if (col.type === 'biz-current-dept') {
						formData.label[colName] = this.ctx.deptFullName
						formData.value[colName] = this.ctx.deptId
					}
					if (col.type === 'biz-current-org') {
						formData.label[colName] = this.ctx.ascnName
						formData.value[colName] = this.ctx.ascnId
					}
					if (col.type === 'biz-current-date-time') {
						let bizCurrentDateTimeValue = this.$dayjs().format(col.props.format)
						formData.label[colName] = bizCurrentDateTimeValue
						formData.value[colName] = bizCurrentDateTimeValue
					}
				}
			}
		})

		this.initFormData = formData

		this.$nextTick(() => {
			this.init(this.param)
		})
	},
}
</script>

<style lang="less" scoped>

.form-add {
	height:   100%;
	position: relative;
}

.form-add-body {
	overflow:       auto;
	padding-bottom: 30px;
	height:         calc(100% - 48px);
}

.form-add-bottom {
	position:   absolute;
	bottom:     0;
	width:      100%;
	padding:    16px 0 0 0;
	text-align: center;
	border-top: 1px solid #eee;
}
</style>
