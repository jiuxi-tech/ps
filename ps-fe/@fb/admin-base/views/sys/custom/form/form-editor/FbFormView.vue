<template>
	<fb-container style="height: 100%; display:flex; flex-direction:column;">
		<fb-container class="center" style="overflow: auto; height: calc(100% - 0px);">
			<fb-property bordered mode="form" :body-style="{overflow: 'visible'}">
				<template v-for="(row, rowIndex) in view.rows">

					<fb-row v-if="row.type === 'row-24'"
					        :class="{[`${prefix}-row--property-fieldset`]: row.cols[0]&&row.cols[0].type && row.cols[0].type === 'fb-fieldset'}"
					>

						<template v-if="row.cols[0].type ">
							<fb-form-editor-col-view-run
								row-type="row-24"
								:col="row.cols[0]"
								:value="row.cols[0].formItemProps.name && formData[row.cols[0].formItemProps.name]"
								:formData="formData"
								:param="myParam"
							/>
						</template>
					</fb-row>
					<fb-row v-if="row.type === 'row-12'">
						<template v-if="row.cols[0].type ">
							<fb-form-editor-col-view-run
								row-type="row-12"
								:col="row.cols[0]"
								:value="row.cols[0].formItemProps.name && formData[row.cols[0].formItemProps.name]"
								:formData="formData"
								:param="myParam"
							/>
						</template>

						<template v-if="row.cols[1].type ">
							<fb-form-editor-col-view-run
								row-type="row-12"
								:col="row.cols[1]"
								:value="row.cols[1].formItemProps.name && formData[row.cols[1].formItemProps.name]"
								:formData="formData"
								:param="myParam"
							/>
						</template>
					</fb-row>
				</template>
			</fb-property>

		</fb-container>
	</fb-container>

</template>

<script>

import { arrayToObject, uniqAndCleanArray } from '../../../../../views/sys/custom/form/form-editor/utils/utils'
import { get, slice } from 'lodash-es'
import FbFormEditorColViewPreview from '../../../../../views/sys/custom/form/form-editor/FbFormEditorColViewPreview'
import FbFormEditorColViewRun from '../../../../../views/sys/custom/form/form-editor/FbFormEditorColViewRun'

/**
 * FbFormView
 * (c) 2021 lincong1987
 */

export default {
	name: 'FbFormView',
	components: {
		FbFormEditorColViewRun,
		FbFormEditorColViewPreview,
	},
	mixins: [

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

		mid: {
			type: [Number, String],
			default: null,
		},

		mcode: {
			type: [Number, String],
			default: null,
		},

	},

	data () {
		return {

			myParam: {},

			error: '',

			// 表单数据
			formData: {},

			form: {
				props: {
					name: '',
					code: '',
				},
			},
			view: {

				props: {
					labelWidth: 120,
					labelPosition: null,
				},
				rows: [],
			},

			// 请求的 service
			service: app.$svc.sys.custom['c-json'],

		}
	},

	methods: {
		arrayToObject,
		uniqAndCleanArray,

		/**
		 * 显示窗口
		 * param 参数
		 * title 标题
		 */
		init (param) {
			// 有值表示是修改方法
			if (param.id) { // 传ID表示修改
				this.myParam.id = param.id
				// 调用新增service方法
				this.service.view({
					fid: this.view.props.fid,
					id: this.myParam.id,
				}).then((result) => {
					// 判断code
					if (result.code === 1) {
						this.$set(this, 'formData', JSON.parse(result.data.dataDetail))
					} else {
						// 服务器返回失败
						this.$message.error('错误提示:' + result.message)
					}
				})
			}
		},

		// 取消
		handleClose () {
			// 隐藏
			this.closeTpDialog(this.myParam.id)
		},

	},

	async mounted () {

		let mid = this.mid
		let mcode = this.mcode

//		let formProps = await this.$svc.sys.custom.module.view({mid}).then(res => res.data)
		let view
		if (mid) {
			view = await this.$svc.sys.custom.form.mid({
				mid,
				ftype: 'view',
				fSource: 'pc',
			}).then(res => res.data)

		}
		if (mcode) {
			view = await this.$svc.sys.custom.form.mcode({
				mcode,
				ftype: 'view',
				fSource: 'pc',
			}).then(res => res.data)
		}

		this.form.props = {
			mid: view.mid,
			name: 'name',
			code: view.fjson.replace('C_JSON_', ''),
		}

		// let formProps = await this.$svc.sys.custom.module.view({mid}).then(res => res.data)

		this.form.props = {
			mid: view.mid,
			name: 'name',
			code: view.fjson.replace('C_JSON_', ''),
		}

//		let view = await this.$svc.sys.custom.form.view({
//			mid: formProps.mid,
//			ftype: 'view',
//			fSource: 'pc',
//		})

		if (view === '' || view.fjson === '') {
			this.$message.error('此表单未定义！')
		} else {
			this.view = JSON.parse(view.fjson)
		}

		if (view.fid) {
			this.view.props.fid = view.fid
		}

		let formData = {
			value: {},
			label: {},
			raw: {},
		}

		this.view.rows.forEach((n, i) => {
			if (n.type === 'row-24' && n.cols[0].type) {
				let colName = n.cols[0].type && n.cols[0].formItemProps && n.cols[0].formItemProps.name
				formData.value[colName] = ''
				formData.label[colName] = ''
				formData.raw[colName] = []
				if (n.cols[0].type === 'fb-checkbox-group') {
					formData.value[colName] = []
				}
				if (n.cols[0].type === 'tp-upload') {
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
					formData.raw[colName] = []
				}
			}
			if (n.type === 'row-12') {
				if (n.cols[0].type) {
					let colName = n.cols[0].type && n.cols[0].formItemProps && n.cols[0].formItemProps.name
					formData.value[colName] = ''
					formData.label[colName] = ''
					formData.raw[colName] = []
					if (n.cols[0].type === 'fb-checkbox-group') {
						formData.value[colName] = []
					}
					if (n.cols[0].type === 'tp-upload') {
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
				}
				if (n.cols[1].type) {
					let colName = n.cols[1].type && n.cols[1].formItemProps && n.cols[1].formItemProps.name
					formData.value[colName] = ''
					formData.label[colName] = ''
					formData.raw[colName] = []
					if (n.cols[1].type === 'fb-checkbox-group') {
						formData.value[colName] = []
					}
					if (n.cols[1].type === 'tp-upload') {
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
				}
			}
		})

		this.formData = formData

		this.$nextTick(() => {
			this.init(this.param)
		})

	},
}
</script>

<style lang="less" scoped>

</style>
