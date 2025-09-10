<template>
	<fb-col
		v-if="col && col.type && col.formItemProps.show"

		:span="(()=>{
			if (col && col.type && col.formItemProps && typeof col.formItemProps.colSpan !== 'undefined') {
				return col.formItemProps.colSpan
			}

			if (rowType) {
				return 	 (rowType === 'row-12' ? 12 : 24)
			}
		})()"

		:offset="(()=>{
			if (col && col.type && col.formItemProps && typeof col.formItemProps.colOffset !== 'undefined') {
				return col.formItemProps.colOffset
			}
			return 0
		})()"
	>


		<fb-fieldset v-if="col.type === 'fb-fieldset'" :label="col.props.label"/>

		<fb-form-item v-else
		              :ellipsis="col.formItemProps.ellipsis"
		              :label="col.formItemProps.label"
		              :prop="col.formItemProps.prop"
		              :label-width="col.formItemProps.labelWidth || formEditor.form.props.labelWidth"
		              :label-position="col.formItemProps.labelPosition || formEditor.form.props.labelPosition"
		              :show-label="col.formItemProps.showLabel"
		              :rule="ruleFormat(col.formItemProps.rules)"
		>

			<template v-if="col.type === 'fb-input'">
				<fb-input
					:placeholder="col.props.placeholder"
					:icon="col.props.icon"
					:size="col.props.size"
					:type="col.props.type"
					:disabled="col.props.disabled"
					:readonly="col.props.readonly"
					:clearable="col.props.clearable"
					:maxlength="col.props.maxlength"
					:prependIcon="col.props.prependIcon"
					:width="col.props.width"
					:round="col.props.round"
				></fb-input>
			</template>
			<template v-if="col.type === 'fb-textarea'">
				<fb-textarea
					:placeholder="col.props.placeholder"
					:rows="col.props.rows"
					:size="col.props.size"
					:disabled="col.props.disabled"
					:readonly="col.props.readonly"
					:clearable="col.props.clearable"
					:maxlength="col.props.maxlength"
					:prependIcon="col.props.prependIcon"
					:width="col.props.width"
				></fb-textarea>
			</template>
			<template v-if="col.type === 'fb-select'">
				<fb-select
					:placeholder="col.props.placeholder"
					:auto-load="col.props.autoLoad"
					:filterable="col.props.filterable"
					:clearable="col.props.clearable"
					:disabled="col.props.disabled"
					:readonly="col.props.readonly"
					:data="uniqAndCleanArray(col.props.data)"
					:param="arrayToObject(uniqAndCleanArray(col.props.param))"
				></fb-select>
			</template>
			<template
				v-if="col.type === 'fb-tree-select'">
				<fb-tree-select
					:placeholder="col.props.placeholder"
					:auto-load="col.props.autoLoad"
					:filterable="col.props.filterable"
					:clearable="col.props.clearable"
					:disabled="col.props.disabled"
					:readonly="col.props.readonly"
					:data="uniqAndCleanArray(col.props.data)"
					:param="col.props.param"
				></fb-tree-select>
			</template>
			<template
				v-if="col.type === 'fb-radio-group'">
				<fb-radio-group
					:data="uniqAndCleanArray(col.props.data, 'value')"
					:vertical="col.props.vertical"
					:button="col.props.button"
					:list="col.props.list"
					:disabled="col.props.disabled"
					:readonly="col.props.readonly"
					:radio-space="col.props.radioSpace"
				></fb-radio-group>
			</template>
			<template
				v-if="col.type === 'fb-checkbox-group'">
				<fb-checkbox-group
					:data="uniqAndCleanArray(col.props.data, 'value')"
					:vertical="col.props.vertical"
					:button="col.props.button"
					:list="col.props.list"
					:disabled="col.props.disabled"
					:readonly="col.props.readonly"
				></fb-checkbox-group>
			</template>
			<template v-if="col.type === 'fb-radio'">
				<fb-radio
					:disabled="col.props.disabled"
					:readonly="col.props.readonly"
					:value="col.props.value"
					:label="col.props.label"
				></fb-radio>
			</template>
			<template v-if="col.type === 'fb-checkbox'">
				<fb-checkbox
					:disabled="col.props.disabled"
					:readonly="col.props.readonly"
					:value="col.props.value"
					:label="col.props.label"
				></fb-checkbox>
			</template>
			<template
				v-if="col.type === 'tp-datepicker'">
				<tp-datepicker
					:mode="col.props.mode"
					:format="col.props.format"
					:readonly="col.props.readonly"
					:disabled="col.props.disabled"
					:clearable="col.props.clearable"
					:placeholder="col.props.placeholder"
					:min-date="col.props.minDate"
					:max-date="col.props.maxDate"
					:max-range="col.props.maxRange"
					:position="col.props.position"
				></tp-datepicker>
			</template>
			<template v-if="col.type === 'tp-upload'">
				<tp-upload
					:view="col.props.view"
					:accept="col.props.accept"
					:max-length="col.props.maxLength"
					:button-text="col.props.buttonText"
					:button-icon="col.props.buttonIcon"
					:show-preview="col.props.showPreview"
					:show-remove="col.props.showRemove"
					:show-download="col.props.showDownload"
				></tp-upload>

			</template>
			<template v-if="col.type === 'fb-editor'">
				<component
					:is="col.type"
				></component>
			</template>


			<template v-if="col.type === 'biz-ent-person-single-select'">
				<fb-input
					:placeholder="col.props.placeholder"
					:icon="col.props.icon"
					:size="col.props.size"
					:type="col.props.type"
					:disabled="col.props.disabled"
					:readonly="true"
					:clearable="true"
					:maxlength="col.props.maxlength"
					:prependIcon="col.props.prependIcon"
					:width="col.props.width"
					:round="col.props.round"

					@on-click="()=>{
												handleEntPersonSingleSelect({
												 form: formEditor
											})}"
				>
					<fb-button v-if="col.props.clearable === true" slot="append-button" type="text"
					           icon="close-circle"
					           style="background: #F0EFF5; "
					/>
				</fb-input>
			</template>

			<template v-if="col.type === 'biz-ent-person-multiple-select'">
				<fb-input
					:placeholder="col.props.placeholder"
					:icon="col.props.icon"
					:size="col.props.size"
					:type="col.props.type"
					:disabled="col.props.disabled"
					:readonly="true"
					:clearable="true"
					:maxlength="col.props.maxlength"
					:prependIcon="col.props.prependIcon"
					:width="col.props.width"
					:round="col.props.round"

					@on-click="()=>{
													handleOrgPersonMultipleSelect({
															form: formEditor
													})}"

					:el-style="col.props.clearable === true ? 'border-right: 0' : ''"
				>
					<fb-button v-if="col.props.clearable === true" slot="append-button" type="text"
					           icon="close-circle"
					           style="background: #F0EFF5; "

					/>

				</fb-input>
			</template>

			<template v-if="col.type === 'biz-ent-person-single-select-using-table-show'">
				<fb-input
					:placeholder="col.props.placeholder"
					:icon="col.props.icon"
					:size="col.props.size"
					:type="col.props.type"
					:disabled="col.props.disabled"
					:readonly="true"
					:clearable="true"
					:maxlength="col.props.maxlength"
					:prependIcon="col.props.prependIcon"
					:width="col.props.width"
					:round="col.props.round"

					@on-click="()=>{
													handleEntPersonSingleSelectUsingTableShow({
															form: formEditor
													})}"

					:el-style="col.props.clearable === true ? 'border-right: 0' : ''"
				>
					<fb-button v-if="col.props.clearable === true" slot="append-button" type="text"
					           icon="close-circle"
					           style="background: #F0EFF5; "

					/>
				</fb-input>
			</template>

			<template v-if="col.type === 'biz-ent-person-multiple-select-using-table-show'">
				<fb-input
					:placeholder="col.props.placeholder"
					:icon="col.props.icon"
					:size="col.props.size"
					:type="col.props.type"
					:disabled="col.props.disabled"
					:readonly="true"
					:clearable="true"
					:maxlength="col.props.maxlength"
					:prependIcon="col.props.prependIcon"
					:width="col.props.width"
					:round="col.props.round"

					@on-click="()=>{
													handleEntPersonMultipleSelectUsingTableShow({
														   form: formEditor
													})}"

					:el-style="col.props.clearable === true ? 'border-right: 0' : ''"
				>
					<fb-button v-if="col.props.clearable === true" slot="append-button" type="text"
					           icon="close-circle"
					           style="background: #F0EFF5; "

					/>

				</fb-input>
			</template>

			<template v-if="col.type === 'biz-org-person-single-select'">
				<fb-input
					:placeholder="col.props.placeholder"
					:icon="col.props.icon"
					:size="col.props.size"
					:type="col.props.type"
					:disabled="col.props.disabled"
					:readonly="true"
					:clearable="true"
					:maxlength="col.props.maxlength"
					:prependIcon="col.props.prependIcon"
					:width="col.props.width"
					:round="col.props.round"

					@on-click="()=>{
													handleOrgPersonSingleSelect({
														 form: formEditor
													})}"

					:el-style="col.props.clearable === true ? 'border-right: 0' : ''"
				>
					<fb-button v-if="col.props.clearable === true" slot="append-button" type="text"
					           icon="close-circle"
					           style="background: #F0EFF5; "

					/>
				</fb-input>
			</template>

			<template v-if="col.type === 'biz-org-person-multiple-select'">
				<fb-input
					:placeholder="col.props.placeholder"
					:icon="col.props.icon"
					:size="col.props.size"
					:type="col.props.type"
					:disabled="col.props.disabled"
					:readonly="true"
					:clearable="true"
					:maxlength="col.props.maxlength"
					:prependIcon="col.props.prependIcon"
					:width="col.props.width"
					:round="col.props.round"


					@on-click="()=>{
												handleOrgPersonMultipleSelect({
													  form: formEditor

											})}"

					:el-style="col.props.clearable === true ? 'border-right: 0' : ''"
				>
					<fb-button v-if="col.props.clearable === true" slot="append-button" type="text"
					           icon="close-circle"
					           style="background: #F0EFF5; "

					/>

				</fb-input>
			</template>

			<template v-if="col.type === 'biz-org-ent-single-select'">
				<fb-input
					:placeholder="col.props.placeholder"
					:icon="col.props.icon"
					:size="col.props.size"
					:type="col.props.type"
					:disabled="col.props.disabled"
					:readonly="true"
					:clearable="true"
					:maxlength="col.props.maxlength"
					:prependIcon="col.props.prependIcon"
					:width="col.props.width"
					:round="col.props.round"

					@on-click="()=>{
												handleOrgEntSingleSelect({
													  form: formEditor
											})}"

					:el-style="col.props.clearable === true ? 'border-right: 0' : ''"
				>
					<fb-button v-if="col.props.clearable === true" slot="append-button" type="text"
					           icon="close-circle"
					           style="background: #F0EFF5; "

					/>
				</fb-input>
			</template>

			<template v-if="col.type === 'biz-org-ent-multiple-select'">
				<fb-input
					:placeholder="col.props.placeholder"
					:icon="col.props.icon"
					:size="col.props.size"
					:type="col.props.type"
					:disabled="col.props.disabled"
					:readonly="true"
					:clearable="true"
					:maxlength="col.props.maxlength"
					:prependIcon="col.props.prependIcon"
					:width="col.props.width"
					:round="col.props.round"


					@on-click="()=>{
						handleOrgEntMultipleSelect({
							form: formEditor
						})
					}"

					:el-style="col.props.clearable === true ? 'border-right: 0' : ''"
				>
					<fb-button v-if="col.props.clearable === true" slot="append-button" type="text"
					           icon="close-circle"
					           style="background: #F0EFF5; "

					/>

				</fb-input>
			</template>


			<!--								<component-->
			<!--									:is="col.type"-->
			<!--								></component>-->
		</fb-form-item>


	</fb-col>
</template>

<script>
import { closest } from '../../../../util/componentUtils'
import { arrayToObject, uniqAndCleanArray } from './utils/utils'
import events from './utils/mixins/events'

/**
 * FbFormEditorColAddPreview
 * (c) 2021 lincong1987
 */

export default {
	name: 'FbFormEditorColAddPreview',

	mixins: [events],

	inject: ['formEditor'],
	provide () {
		return {
			addForm: this,
		}
	},

	props: {

		col: {
			type: [Object],
		},

		rowIndex: {
			type: Number,
		},

		colIndex: {
			type: Number,
		},

		viewType: {
			type: String,
		},

		rowType: {
			type: String,
		},

		activeComponent: {
			type: [Object],
		},

	},
	data () {
		return {}
	},

	methods: {

		uniqAndCleanArray,

		arrayToObject,

		ruleFormat (rules) {
			let required = false
			if (!rules) {
				return [{required}]
			}
			if (rules.includes('required')) {
				required = true
			}
			return [{required}]
		},

	},

	mounted () {
		this.formEditor
	},

}
</script>

<style lang="less" scoped>

</style>
