<template>
	<fb-col :span="(()=>{
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

		<div v-if="col && col.type" class="editor-cell"
		     :class="{
						[`editor-cell--${rowType}`]: !!rowType,
						[`editor-cell--active`]: col.uuid === (activeComponent && activeComponent.uuid)
					}">
			<div class="editor-cell__mask"
			     @click="formEditor.handleActiveComponent(rowIndex, colIndex, col.type, col.uuid)">

				<fb-container absolute left="12px" top="8px">
					<fb-icon :name="col.formItemProps.show ? 'show' : 'hidden'"
					         :color="col.formItemProps.show ? colors.grey_8 : colors.red_7"
					></fb-icon>
				</fb-container>

			</div>

			<div class="editor-cell__component">
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
					<template v-if="col.type === 'fb-text'">
						<fb-text
							:icon="col.props.icon"
							:size="col.props.size"
							:color="col.props.color"
							:ellipsis="col.props.ellipsis"
							:width="(()=>{
								//if (col.props.widthUnit === 'px') {
									//return col.props.width
								//}
								return col.props.width + col.props.widthUnit
							})()"
							:bold="col.props.bold"
							:align="col.props.align"
							:va="col.props.va"
							:family="col.props.family"
							:ml="col.props.ml"
							:mr="col.props.mr"
						>{{ col.props.defaultValue }}
						</fb-text>
					</template>
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
							:append="col.props.append"
							:prepend="col.props.prepend"
						>
						</fb-input>
					</template>
					<template v-if="col.type === 'fb-input-number'">
						<fb-input-number
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
							:append="col.props.append"
							:prepend="col.props.prepend"
							:max="col.props.max"
							:min="col.props.min"

							:value="col.props.defaultValue"

							:controls="col.props.controls"
							:controls-position="col.props.controlsPosition"
							:step="col.props.step"
							:step-strictly="col.props.stepStrictly"
							:precision="col.props.precision"
						>
						</fb-input-number>
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
							:append="col.props.append"
							:prepend="col.props.prepend"
						></fb-textarea>
					</template>
					<template v-if="col.type === 'fb-select'">
						<fb-select
							:placeholder="col.props.placeholder"
							:auto-load="col.props.autoLoad"
							:size="col.props.size"
							:multiple="col.props.multiple"
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
							:multiple="col.props.multiple"
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
							:radio-space="col.props.radioSpace"
							:disabled="col.props.disabled"
							:readonly="col.props.readonly"
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
						v-if="col.type === 'fb-color-picker'">
						<fb-color-picker
							:readonly="col.props.readonly"
							:disabled="col.props.disabled"
							:clearable="col.props.clearable"
							:placeholder="col.props.placeholder"
							:show-alpha="col.props.showAlpha"
						></fb-color-picker>
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
							:param="arrayToObject(uniqAndCleanArray(col.props.param))"
							:referid="col.props.referid"
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

					<!--								<component-->
					<!--									:is="col.type"-->
					<!--								></component>-->


					<template v-if="col.type === 'biz-person-select'">

						<fb-popup-header multiple
						                 clearable
						                 icon="more"
						                 style="width: 100%;">
							<fb-badge :count="0" overflowCount="9">
								<fb-tags style="overflow:hidden;" type="primary" effect="dark"
								         :data="[]"></fb-tags>
							</fb-badge>
						</fb-popup-header>

					</template>

					<template v-if="col.type === 'biz-ent-select'">

						<fb-input
							:placeholder="col.props.placeholder"
							:icon="col.props.icon"
							:size="col.props.size"
							:type="col.props.type"
							:disabled="col.props.disabled"
							:readonly="true"
							:clearable="col.props.clearable"
							:maxlength="col.props.maxlength"
							:prependIcon="col.props.prependIcon"
							:width="col.props.width"
							:round="col.props.round"

						></fb-input>

					</template>


					<template v-if="col.type === 'biz-ent-person-single-select'">
						<fb-input
							:placeholder="col.props.placeholder"
							:icon="col.props.icon"
							:size="col.props.size"
							:type="col.props.type"
							:disabled="col.props.disabled"
							:readonly="true"
							:clearable="col.props.clearable"
							:maxlength="col.props.maxlength"
							:prependIcon="col.props.prependIcon"
							:width="col.props.width"
							:round="col.props.round"

						>
							<fb-button v-show="col.props.clearable === true"
							           slot="append-button" type="text"
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
							:clearable="col.props.clearable"
							:maxlength="col.props.maxlength"
							:prependIcon="col.props.prependIcon"
							:width="col.props.width"
							:round="col.props.round"
						>
							<fb-button v-if="col.props.clearable === true"
							           slot="append-button" type="text"
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
							:clearable="col.props.clearable"
							:maxlength="col.props.maxlength"
							:prependIcon="col.props.prependIcon"
							:width="col.props.width"
							:round="col.props.round"
						>
							<fb-button v-if="col.props.clearable === true"
							           slot="append-button" type="text"
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
							:clearable="col.props.clearable"
							:maxlength="col.props.maxlength"
							:prependIcon="col.props.prependIcon"
							:width="col.props.width"
							:round="col.props.round"
						>
							<fb-button v-if="col.props.clearable === true"
							           slot="append-button" type="text"
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
							:clearable="col.props.clearable"
							:maxlength="col.props.maxlength"
							:prependIcon="col.props.prependIcon"
							:width="col.props.width"
							:round="col.props.round"
						>
							<fb-button v-show="col.props.clearable === true"
							           slot="append-button"
							           type="text"
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
							:clearable="col.props.clearable"
							:maxlength="col.props.maxlength"
							:prependIcon="col.props.prependIcon"
							:width="col.props.width"
							:round="col.props.round"
						>
							<fb-button v-if="col.props.clearable === true"
							           slot="append-button" type="text"
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
							:clearable="col.props.clearable"
							:maxlength="col.props.maxlength"
							:prependIcon="col.props.prependIcon"
							:width="col.props.width"
							:round="col.props.round"
						>
							<fb-button v-if="col.props.clearable === true"
							           slot="append-button" type="text"
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
							:clearable="col.props.clearable"
							:maxlength="col.props.maxlength"
							:prependIcon="col.props.prependIcon"
							:width="col.props.width"
							:round="col.props.round"
						>
							<fb-button v-if="col.props.clearable === true"
							           slot="append-button" type="text"
							           icon="close-circle"
							           style="background: #F0EFF5; "
							/>
						</fb-input>
					</template>

					<template v-if="col.type === 'biz-current-ctx'">
						<fb-container height="30px" valign="center"
						              border="1px solid #D3D3D3"
						              background="#F0EFF5"
						              radius="4px"
						              pl="6px"
						              pr="6px"
						>
							<fb-text color="#313C47">
								<fb-icon name="scale"/>
								{{ computeCurrentCtx(col.props.expression) }}
							</fb-text>
						</fb-container>
					</template>
					<template v-if="col.type === 'biz-current-person'">
						<fb-container height="30px" valign="center"
						              border="1px solid #D3D3D3"
						              background="#F0EFF5"
						              radius="4px"
						              pl="6px"
						              pr="6px"
						>
							<fb-text color="#313C47">
								<fb-icon name="user"/>
								{{ ctx.personName }}
							</fb-text>
						</fb-container>
					</template>
					<template v-if="col.type === 'biz-current-dept'">
						<fb-container height="30px" valign="center"
						              border="1px solid #D3D3D3"
						              background="#F0EFF5"
						              radius="4px"
						              pl="6px"
						              pr="6px"
						>
							<fb-text color="#313C47">
								<fb-icon name="enterprise"/>
								{{ ctx.deptFullName }}
							</fb-text>
						</fb-container>
					</template>
					<template v-if="col.type === 'biz-current-org'">
						<fb-container height="30px" valign="center"
						              border="1px solid #D3D3D3"
						              background="#F0EFF5"
						              radius="4px"
						              pl="6px"
						              pr="6px"
						>
							<fb-text color="#313C47">
								<fb-icon name="government"/>
								{{ ctx.ascnName }}
							</fb-text>
						</fb-container>
					</template>
					<template v-if="col.type === 'biz-current-date-time'">
						<fb-container height="30px" valign="center"
						              border="1px solid #D3D3D3"
									  background="#F0EFF5"
						              radius="4px"
						              pl="6px"
						              pr="6px"
						>
							<fb-text color="#313C47">
								<fb-icon name="waiting"/>
								{{ $dayjs().format(col.props.format) }}
							</fb-text>
						</fb-container>
					</template>


				</fb-form-item>

			</div>

			<div class="editor-cell__toolbar">

				<div class="editor-cell__title">[{{
						formEditor.getComponentNameByType(col.type)
					}}]
				</div>

				<!--				<div class="editor-cell__name">[{{-->
				<!--						col.type && col.formItemProps.name-->
				<!--					}}]-->
				<!--				</div>-->

				<div class="editor-cell__remove">
					<fb-button size="s" round danger
					           icon="reduce-circle-fill"
					           :color="colors.white"
					           @on-click="formEditor.removeComponent('add', rowIndex, colIndex, col.uuid)"
					>删除
					</fb-button>
				</div>
			</div>
		</div>

		<div v-else class="editor-cell-empty">
			<fb-space align="center">
				<fb-select
					v-model="col.tempType"
					style="width: 160px"
					placeholder="请选择控件"
					:data="formEditor.editor.componentsNameList"
				/>

				<fb-button
					@on-click="formEditor.addComponent('add', rowIndex, colIndex, col.tempType, col.uuid)"
				>加入
				</fb-button>
			</fb-space>
		</div>

	</fb-col>
</template>

<script>
import { closest } from '../../../../../util/componentUtils'
import { arrayToObject, uniqAndCleanArray, computeCurrentCtx, toRaw } from '../../../../../views/sys/custom/form/form-editor/utils/utils'

/**
 * FbFormEditorCol
 * (c) 2021 lincong1987
 */

export default {
	name: 'FbFormEditorCol',

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

	inject: ['formEditor'],

//	created () {
//		this.formEditor = closest(this, 'FbFormEditor')
//	},
//	beforeDestroy () {
//		this.formEditor = null
//	},

	data () {


		return {
			ctx: this.formEditor.ctx,
		}
	},

	watch: {
		col: {
			handler (val) {
			},
			deep: true,
		},
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

		computeCurrentCtx (exp) {
			return computeCurrentCtx.apply(this, [exp])
		},



	},

	mounted () {

	}

}
</script>

<style lang="less" scoped>

</style>
