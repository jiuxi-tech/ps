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

		<fb-form-item v-if="col.type !== 'fb-fieldset'"
		              :ellipsis="col.formItemProps.ellipsis"
		              :label="col.formItemProps.label"
		              :prop="col.formItemProps.name"
		              :label-width="col.formItemProps.labelWidth"
		              :label-position="col.formItemProps.labelPosition"
		              :show-label="col.formItemProps.showLabel"
		              :rule="ruleFormat(col.formItemProps.rules, col.formItemProps)"
		>

			<template v-if="col.type === 'fb-text'">
				<fb-text
					:icon="col.props.icon"
					:size="col.props.size"
					:color="col.props.color"
					:ellipsis="col.props.ellipsis"
					:width="(()=>{
//								if (col.props.widthUnit === 'px') {
//									return col.props.width
//								}
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
			<template v-if="col.type === 'fb-color-picker'">
				<fb-color-picker
					:placeholder="col.props.placeholder"
					:icon="col.props.icon"
					:size="col.props.size"
					:disabled="col.props.disabled"
					:readonly="col.props.readonly"
					:clearable="col.props.clearable"
					:maxlength="col.props.maxlength"
					:prependIcon="col.props.prependIcon"
					:width="col.props.width"
					:round="col.props.round"
					:value="(()=>{
						if (typeof myFormData.value[col.formItemProps.name] !== 'undefined') {
							return myFormData.value[col.formItemProps.name]
						}else if (typeof col.props.value !== 'undefined') {
							return col.props.value
						} else {
							return ''
						}
					})()"
					@input="(value)=>{

						updatePropsValue(value,'formData', 'value', col.formItemProps.name)
						updatePropsLabel(value,'formData', 'label', col.formItemProps.name)
					}"
				></fb-color-picker>
			</template>
			<template v-if="col.type === 'fb-icon-select'">
				<fb-icon-select
					:placeholder="col.props.placeholder"
					:icon="col.props.icon"
					:size="col.props.size"
					:disabled="col.props.disabled"
					:readonly="col.props.readonly"
					:clearable="col.props.clearable"
					:maxlength="col.props.maxlength"
					:prependIcon="col.props.prependIcon"
					:width="col.props.width"
					:round="col.props.round"
					:value="(()=>{
						if (typeof myFormData.value[col.formItemProps.name] !== 'undefined') {
							return myFormData.value[col.formItemProps.name]
						}else if (typeof col.props.value !== 'undefined') {
							return col.props.value
						} else {
							return ''
						}
					})()"
					@input="(value)=>{
						updatePropsValue(value,'formData', 'value', col.formItemProps.name)
						updatePropsLabel(value,'formData', 'label', col.formItemProps.name)
					}"
				></fb-icon-select>
			</template>
			<template v-if="col.type === 'fb-input'">

				<fb-input
					:placeholder="col.props.placeholder"
					:icon="col.props.icon"
					:size="col.props.size"
					:disabled="col.props.disabled"
					:readonly="col.props.readonly"
					:clearable="col.props.clearable"
					:maxlength="col.props.maxlength"
					:prependIcon="col.props.prependIcon"
					:width="col.props.width"
					:round="col.props.round"
					:value="(()=>{
						if (typeof myFormData.value[col.formItemProps.name] !== 'undefined') {
							return myFormData.value[col.formItemProps.name]
						}else if (typeof col.props.value !== 'undefined') {
							return col.props.value
						} else {
							return ''
						}
					})()"
					@input="(value)=>{
						updatePropsValue(value,'formData', 'value', col.formItemProps.name)
						updatePropsLabel(value,'formData', 'label', col.formItemProps.name)
					}"
				></fb-input>
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
					:max="(()=>{
						if (col.props.max === null) {
							return Infinity
						}
					})()"
					:min="(()=>{
						if (col.props.min === null) {
							return -Infinity
						}
					})()"


					:controls="col.props.controls"
					:controls-position="col.props.controlsPosition"
					:step="col.props.step"
					:step-strictly="col.props.stepStrictly"
					:precision="col.props.precision"


					:value="(()=>{
						if (typeof myFormData.value[col.formItemProps.name] !== 'undefined') {
							return myFormData.value[col.formItemProps.name]
						}else if (typeof col.props.value !== 'undefined') {
							return col.props.value
						} else {
							return ''
						}
					})()"
					@input="(value)=>{
						updatePropsValue(value || '','formData', 'value', col.formItemProps.name)
						updatePropsLabel(value || '','formData', 'label', col.formItemProps.name)
					}"
				>
				</fb-input-number>
			</template>

			<template v-if="col.type === 'fb-textarea'">
				<fb-textarea
					:placeholder="col.props.placeholder"
					:rows="col.props.rows"
					:disabled="col.props.disabled"
					:readonly="col.props.readonly"
					:clearable="col.props.clearable"
					:maxlength="col.props.maxlength"
					:prependIcon="col.props.prependIcon"
					:width="col.props.width"
					:value="(()=>{
						if (typeof myFormData.value[col.formItemProps.name] !== 'undefined') {
							return myFormData.value[col.formItemProps.name]
						}else if (typeof col.props.value !== 'undefined') {
							return col.props.value
						} else {
							return ''
						}
					})()"
					@input="(value)=>{
						updatePropsValue(value,'formData', 'value', col.formItemProps.name)
						updatePropsLabel(value,'formData', 'label', col.formItemProps.name)
					}"
				></fb-textarea>
			</template>
			<template v-if="col.type === 'fb-select'">

				<fb-select
					:ref="col.formItemProps.name"
					:multiple="col.props.multiple"
					:placeholder="col.props.placeholder"
					:auto-load="col.props.autoLoad"
					:filterable="col.props.filterable"
					:clearable="col.props.clearable"
					:disabled="col.props.disabled"
					:readonly="col.props.readonly"
					:data="uniqAndCleanArray(col.props.data)"
					:service="getService(col.props.service)"
					:param="arrayToObject(uniqAndCleanArray(col.props.param))"
					@on-data-update="()=>{
						$nextTick(()=>{
							$refs[col.formItemProps.name].currentValue = myFormData.value[col.formItemProps.name]
							if (!isAdd) {
								$refs[col.formItemProps.name].changeCurrentValue()
							}
						})
					}"

					@on-change="(value, node)=>{
						let _label = ''
						let _value = ''

						if(col.props.multiple){
							_value = value||[]
							_label = (node||[]).map(n=>n.label).join(',')
						} else {
							_value = value || ''
							_label = node && node.label || ''
						}

						updatePropsValue(_value,'formData', 'value', col.formItemProps.name)
						updatePropsLabel(_label ,'formData', 'label', col.formItemProps.name)
					}"
				></fb-select>
			</template>

			<template
				v-if="col.type === 'fb-tree-select'">
				<fb-tree-select
					:ref="col.formItemProps.name"
					:multiple="col.props.multiple"
					:placeholder="col.props.placeholder"
					:disabled="col.props.disabled"
					:readonly="col.props.readonly"
					:position="col.props.position"
					:maxHeight="col.props.maxHeight"
					:showConfirm="col.props.showConfirm"
					:clearable="col.props.clearable"
					:showIcon="col.props.showIcon"
					:showTitle="col.props.showTitle"
					:inline="col.props.inline"
					:twiceClickSelected="col.props.twiceClickSelected"
					:doCheck="col.props.doCheck"
					:doUnCheck="col.props.doUnCheck"
					:onlyLeaf="col.props.onlyLeaf"

					:service="(()=>{
						return getService(col.props.service)
					})()"
					:data="uniqAndCleanArray(col.props.data)"
					:param="arrayToObject(uniqAndCleanArray(col.props.param))"
					@on-data-update="()=>{
						$nextTick(()=>{

							let value = myFormData.value[col.formItemProps.name]
							if (value) {
								 if (col.props.multiple) {
									$refs[col.formItemProps.name].checkNodesByValue(value)
								} else {
									$refs[col.formItemProps.name].selectNodeByValue(value)
								}
							}
						})
					}"
					@on-change="(value, node, e)=>{

						if (!e) {return}
						let _label = ''
						let _value = ''

						if(col.props.multiple){
							_value = value||[]
							_label = (node||[]).map(n=>n.label).join(',')
						} else {
							_value = value
							_label = node && node.label || ''
						}
						updatePropsValue(_value, 'formData', 'value', col.formItemProps.name)
						updatePropsLabel(_label,'formData', 'label', col.formItemProps.name)
					}"
				></fb-tree-select>
			</template>
			<template
				v-if="col.type === 'fb-radio-group'">

				<fb-radio-group
					:ref="col.formItemProps.name"
					:data="uniqAndCleanArray(col.props.data, 'value')"
					:vertical="col.props.vertical"
					:button="col.props.button"
					:list="col.props.list"
					:disabled="col.props.disabled"
					:readonly="col.props.readonly"
					:service="getService(col.props.service)"
					:param="arrayToObject(uniqAndCleanArray(col.props.param))"
					:radio-space="col.props.radioSpace"

					@on-data-update="()=>{
						$nextTick(()=>{
							$refs[col.formItemProps.name].currentValue = myFormData.value[col.formItemProps.name]
							$refs[col.formItemProps.name].changeValue(myFormData.value[col.formItemProps.name])
						})
					}"

					@input="(value, node)=>{
						updatePropsValue(value,'formData', 'value', col.formItemProps.name)
						updatePropsLabel(node && node.label || '','formData', 'label', col.formItemProps.name)
					}"
				></fb-radio-group>
			</template>
			<template v-if="col.type === 'fb-checkbox-group'">
				<fb-checkbox-group
					:data="uniqAndCleanArray(col.props.data, 'value')"
					:vertical="col.props.vertical"
					:button="col.props.button"
					:list="col.props.list"
					:disabled="col.props.disabled"
					:readonly="col.props.readonly"
					:value="(()=>{
						let value = []
						if (typeof myFormData.value[col.formItemProps.name] !== 'undefined') {
							value = (myFormData.value[col.formItemProps.name])
						}else if (typeof col.props.value !== 'undefined') {
							value = (col.props.value)
						}else if (typeof col.props.defaultValue !== 'undefined') {
							value = (col.props.defaultValue)
						}
						return value
					})()"
					@input="(checkedValues, value, checked, node, checkedNodes)=>{
						updatePropsValue(checkedValues,'formData', 'value', col.formItemProps.name)
						updatePropsLabel(checkedNodes.map(n=>n.label).join(',') ,'formData', 'label', col.formItemProps.name)
					}"
				></fb-checkbox-group>
			</template>
			<template v-if="col.type === 'fb-radio'">
				<fb-radio
					:disabled="col.props.disabled"
					:readonly="col.props.readonly"
					:label="col.props.label"
					:value="(()=>{
						if (typeof myFormData.value[col.formItemProps.name] !== 'undefined') {
							return myFormData.value[col.formItemProps.name]
						}else if (typeof col.props.value !== 'undefined') {
							return col.props.value
						} else {
							return false
						}
					})()"
					@input="(value, node)=>{
						updatePropsValue(value || '','formData', 'value', col.formItemProps.name)
						updatePropsLabel(node && node.label || '','formData', 'label', col.formItemProps.name)
					}"
				></fb-radio>
			</template>
			<template v-if="col.type === 'fb-checkbox'">
				<fb-checkbox
					:disabled="col.props.disabled"
					:readonly="col.props.readonly"
					:label="col.props.label"
					:value="(()=>{
						if (typeof myFormData.value[col.formItemProps.name] !== 'undefined') {
							return myFormData.value[col.formItemProps.name]
						}else if (typeof col.props.value !== 'undefined') {
							return col.props.value
						} else {
							return false
						}
					})()"
					@input="(value, node)=>{
						let label = ''
						if (value === true && col.props.trueLabel) {
							   label = col.props.trueLabel
						}
						if (value === false && col.props.falseLabel) {
							   label = col.props.falseLabel
						}
						updatePropsValue(value,'formData', 'value', col.formItemProps.name)
						updatePropsLabel(label,'formData', 'label', col.formItemProps.name)
					}"
				></fb-checkbox>
			</template>
			<template
				v-if="col.type === 'tp-datepicker'">
				<tp-datepicker
					:mode="col.props.mode"
					:format="col.props.format"
					:value-format="col.props.valueFormat"
					:readonly="col.props.readonly"
					:disabled="col.props.disabled"
					:clearable="col.props.clearable"
					:placeholder="col.props.placeholder"
					:min-date="col.props.minDate"
					:max-date="col.props.maxDate"
					:max-range="col.props.maxRange"
					:position="col.props.position"
					:value="(()=>{
						if (typeof myFormData.value[col.formItemProps.name] !== 'undefined') {
							return myFormData.value[col.formItemProps.name]
						}else if (typeof col.props.value !== 'undefined') {
							return col.props.value
						} else {
							return ''
						}
					})()"
					@listener="(value, valueFormat)=>{
						updatePropsValue(value || '','formData', 'value', col.formItemProps.name)
						updatePropsLabel(valueFormat || '','formData', 'label', col.formItemProps.name)
					}"
				></tp-datepicker>
			</template>
			<template v-if="col.type === 'tp-upload'">

				<!--				{{ col.props.value }}-->
				<!--				{{ col.props.param }}-->
				<!--				col.props.referid-->
				<tp-upload
					:view="col.props.view"
					:service="getService(col.props.service) || $svc.sys.file"
					:param="arrayToObject(uniqAndCleanArray(col.props.param))"
					:referid="param.id"
					:accept="col.props.accept"
					:max-length="col.props.maxLength"
					:button-text="col.props.buttonText"
					:button-icon="col.props.buttonIcon"
					:show-preview="col.props.showPreview"
					:show-remove="col.props.showRemove"
					:show-download="col.props.showDownload"

					:value="(()=>{

						if (typeof myFormData.value[col.formItemProps.name] !== 'undefined') {
							if (myFormData.value[col.formItemProps.name].fileList) {
								return myFormData.value[col.formItemProps.name].fileList
							}
							return []
						}else if (typeof col.props.value !== 'undefined') {
							return col.props.value || []
						} else {
							return []
						}
					})()"

					@listener="(value, node)=>{

						let referType = arrayToObject(uniqAndCleanArray(col.props.param)).referType

						if (!referType) {
							$message(`${col.formItemProps.name} referType 未定义`)
						}    else{

							updatePropsValue({
								referType,
								fileList: value
							},'formData', 'value', col.formItemProps.name)
							updatePropsLabel({
								referType,
								fileList: value
							},'formData', 'label', col.formItemProps.name)
						}
					}"
				></tp-upload>

			</template>
			<template v-if="col.type === 'fb-editor'">
				<component
					:is="col.type"
					:value="(()=>{
						if (typeof myFormData.value[col.formItemProps.name] !== 'undefined') {
							return myFormData.value[col.formItemProps.name]
						}else if (typeof col.props.value !== 'undefined') {
							return col.props.value
						} else {
							return ''
						}
					})()"
					@input="(value, node)=>{
						updatePropsValue(value || '','formData', 'value', col.formItemProps.name)
						updatePropsLabel(value || '','formData', 'label', col.formItemProps.name)
					}"
				></component>
			</template>

			<!--								<component-->
			<!--									:is="col.type"-->
			<!--								></component>-->


			<template v-if="col.type === 'biz-ent-person-single-select'">
				<fb-input
					:placeholder="col.props.placeholder"
					:icon="col.props.icon"
					:size="col.props.size"
					:disabled="col.props.disabled"
					:readonly="true"
					:clearable="col.props.clearable"
					:maxlength="col.props.maxlength"
					:prependIcon="col.props.prependIcon"
					:width="col.props.width"
					:round="col.props.round"
					:value="(()=>{
						if (typeof myFormData.label[col.formItemProps.name] !== 'undefined') {
							return myFormData.label[col.formItemProps.name]
						}else if (typeof col.props.value !== 'undefined') {
							return col.props.value
						} else {
							return ''
						}
					})()"

					:el-style="'color:#606266'"
					@on-click="()=>{
						handleEntPersonSingleSelect({
							form: formAdd,
							success:(result)=>{
								if(result &&  result.personId && result.personName) {
									updatePropsValue(result.personId,'formData', 'value', col.formItemProps.name)
									updatePropsLabel(result.personName,'formData', 'label', col.formItemProps.name)
								}
							}
						})}"

				></fb-input>
			</template>

			<template v-if="col.type === 'biz-ent-person-multiple-select'">
				<fb-input
					:placeholder="col.props.placeholder"
					:icon="col.props.icon"
					:size="col.props.size"
					:disabled="col.props.disabled"
					:readonly="true"
					:clearable="col.props.clearable"
					:maxlength="col.props.maxlength"
					:prependIcon="col.props.prependIcon"
					:width="col.props.width"
					:round="col.props.round"
					:value="(()=>{
						if (typeof myFormData.label[col.formItemProps.name] !== 'undefined') {
							return myFormData.label[col.formItemProps.name]
						}else if (typeof col.props.value !== 'undefined') {
							return col.props.value
						} else {
							return ''
						}
					})()"

					:el-style="'color:#606266'"
					@on-click="()=>{
						handleEntPersonMultipleSelect({
							form: formAdd,
							initData: myFormData.raw[col.formItemProps.name],
							success:(result)=>{
								if(result &&  result.length > 0) {
									updatePropsValue(result.map(n=>n.value).join(','),'formData', 'value', col.formItemProps.name)
									updatePropsLabel(result.map(n=>n.label).join(','),'formData', 'label', col.formItemProps.name)
									updatePropsRaw(result,'formData', 'raw', col.formItemProps.name)
								}
							}
						})}"

				></fb-input>
			</template>

			<template v-if="col.type === 'biz-ent-person-single-select-using-table-show'">
				<fb-input
					:placeholder="col.props.placeholder"
					:icon="col.props.icon"
					:size="col.props.size"
					:disabled="col.props.disabled"
					:readonly="true"
					:clearable="col.props.clearable"
					:maxlength="col.props.maxlength"
					:prependIcon="col.props.prependIcon"
					:width="col.props.width"
					:round="col.props.round"
					:value="(()=>{
						if (typeof myFormData.label[col.formItemProps.name] !== 'undefined') {
							return myFormData.label[col.formItemProps.name]
						}else if (typeof col.props.value !== 'undefined') {
							return col.props.value
						} else {
							return ''
						}
					})()"

					:el-style="'color:#606266'"
					@on-click="()=>{
						handleEntPersonSingleSelectUsingTableShow({
							form: formAdd,
							success:(result)=>{
								if(result &&  result.personId && result.personName) {
									updatePropsValue(result.personId,'formData', 'value', col.formItemProps.name)
									updatePropsLabel(result.personName,'formData', 'label', col.formItemProps.name)
								}
							}
						})}"

				></fb-input>
			</template>

			<template v-if="col.type === 'biz-ent-person-multiple-select-using-table-show'">
				<fb-input
					:placeholder="col.props.placeholder"
					:icon="col.props.icon"
					:size="col.props.size"
					:disabled="col.props.disabled"
					:readonly="true"
					:clearable="col.props.clearable"
					:maxlength="col.props.maxlength"
					:prependIcon="col.props.prependIcon"
					:width="col.props.width"
					:round="col.props.round"
					:value="(()=>{
						if (typeof myFormData.label[col.formItemProps.name] !== 'undefined') {
							return myFormData.label[col.formItemProps.name]
						}else if (typeof col.props.value !== 'undefined') {
							return col.props.value
						} else {
							return ''
						}
					})()"

					:el-style="'color:#606266'"
					@on-click="()=>{
						handleEntPersonMultipleSelectUsingTableShow({
							form: formAdd,
							initData: myFormData.raw[col.formItemProps.name],
							success:(result)=>{
								if(result &&  result.length > 0) {
									updatePropsValue(result.map(n=>n.value).join(','),'formData', 'value', col.formItemProps.name)
									updatePropsLabel(result.map(n=>n.label).join(','),'formData', 'label', col.formItemProps.name)
									updatePropsRaw(result,'formData', 'raw', col.formItemProps.name)
								}
							}
						})}"

				></fb-input>
			</template>

			<template v-if="col.type === 'biz-org-person-single-select'">
				<fb-input
					:placeholder="col.props.placeholder"
					:icon="col.props.icon"
					:size="col.props.size"
					:disabled="col.props.disabled"
					:readonly="true"
					:clearable="col.props.clearable"
					:maxlength="col.props.maxlength"
					:prependIcon="col.props.prependIcon"
					:width="col.props.width"
					:round="col.props.round"
					:value="(()=>{
						if (typeof myFormData.label[col.formItemProps.name] !== 'undefined') {
							return myFormData.label[col.formItemProps.name]
						}else if (typeof col.props.value !== 'undefined') {
							return col.props.value
						} else {
							return ''
						}
					})()"

					:el-style="'color:#606266'"
					@on-click="()=>{
						handleOrgPersonSingleSelect({
							form: formAdd,
							success:(result)=>{
								if(result &&  result.personId && result.personName) {
									updatePropsValue(result.personId,'formData', 'value', col.formItemProps.name)
									updatePropsLabel(result.personName,'formData', 'label', col.formItemProps.name)
								}
							}
						})}"

				></fb-input>
			</template>

			<template v-if="col.type === 'biz-org-person-multiple-select'">

				<fb-input
					:placeholder="col.props.placeholder"
					:icon="col.props.icon"
					:size="col.props.size"
					:disabled="col.props.disabled"
					:readonly="true"
					:clearable="col.props.clearable"
					:maxlength="col.props.maxlength"
					:prependIcon="col.props.prependIcon"
					:width="col.props.width"
					:round="col.props.round"
					:value="(()=>{
						if (typeof myFormData.label[col.formItemProps.name] !== 'undefined') {
							return myFormData.label[col.formItemProps.name]
						}else if (typeof col.props.value !== 'undefined') {
							return col.props.value
						} else {
							return ''
						}
					})()"

					:el-style="'color:#606266'"
					@on-click="()=>{
						handleOrgPersonMultipleSelect({
							form: formAdd,
							initData: myFormData.raw[col.formItemProps.name],
							success:(result)=>{
								if(result &&  result.length > 0) {
									updatePropsValue(result.map(n=>n.value).join(','),'formData', 'value', col.formItemProps.name)
									updatePropsLabel(result.map(n=>n.label).join(','),'formData', 'label', col.formItemProps.name)
									updatePropsRaw(result,'formData', 'raw', col.formItemProps.name)
								}
							}
						})}"

				></fb-input>
			</template>

			<template v-if="col.type === 'biz-org-ent-single-select'">


				<fb-input
					:placeholder="col.props.placeholder"
					:icon="col.props.icon"
					:size="col.props.size"
					:disabled="col.props.disabled"
					:readonly="true"
					:clearable="col.props.clearable"
					:maxlength="col.props.maxlength"
					:prependIcon="col.props.prependIcon"
					:width="col.props.width"
					:round="col.props.round"
					:value="(()=>{
						if (typeof myFormData.label[col.formItemProps.name] !== 'undefined') {
							return myFormData.label[col.formItemProps.name]
						}else if (typeof col.props.value !== 'undefined') {
							return col.props.value
						} else {
							return ''
						}
					})()"

					:el-style="'color:#606266'"
					@on-click="()=>{
						handleOrgEntSingleSelect({
							form: formAdd,
							initData: myFormData.raw[col.formItemProps.name],
							success:(result)=>{
								if(result &&  result.entId  && result.entFullName) {
									updatePropsValue(result.entId,'formData', 'value', col.formItemProps.name)
									updatePropsLabel(result.entFullName,'formData', 'label', col.formItemProps.name)
									updatePropsRaw(result,'formData', 'raw', col.formItemProps.name)
								}
							}
						})}"

				></fb-input>
			</template>

			<template v-if="col.type === 'biz-org-ent-multiple-select'">


				<fb-input
					:placeholder="col.props.placeholder"
					:icon="col.props.icon"
					:size="col.props.size"
					:disabled="col.props.disabled"
					:readonly="true"
					:clearable="col.props.clearable"
					:maxlength="col.props.maxlength"
					:prependIcon="col.props.prependIcon"
					:width="col.props.width"
					:round="col.props.round"
					:value="(()=>{
						if (typeof myFormData.label[col.formItemProps.name] !== 'undefined') {
							return myFormData.label[col.formItemProps.name]
						}else if (typeof col.props.value !== 'undefined') {
							return col.props.value
						} else {
							return ''
						}
					})()"
					:el-style="'color:#606266'"

					@on-click="()=>{
						handleOrgEntMultipleSelect({
							form: formAdd,
							initData: myFormData.raw[col.formItemProps.name],
							success:(result)=>{
								if(result &&  result.length > 0) {
									updatePropsValue(result.map(n=>n.value).join(','),'formData', 'value', col.formItemProps.name)
									updatePropsLabel(result.map(n=>n.label).join(','),'formData', 'label', col.formItemProps.name)
									updatePropsRaw(result,'formData', 'raw', col.formItemProps.name)
								}
							}
						})}"

				></fb-input>
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
						{{ myFormData.label[col.formItemProps.name] }}
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
						{{ myFormData.label[col.formItemProps.name] }}
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
						{{ myFormData.label[col.formItemProps.name] }}
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
						{{ myFormData.label[col.formItemProps.name] }}
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
						{{ myFormData.label[col.formItemProps.name] }}
					</fb-text>
				</fb-container>
			</template>


		</fb-form-item>


	</fb-col>
</template>

<script>
import { isArray, remove, uniqWith, filter, each, toInteger, toNumber, isNumber, isSafeInteger, get } from 'lodash-es'
import { closest } from '../../../../../util/componentUtils'
import {
	arrayToObject,
	stringToArray,
	computeCurrentCtx,
	toRaw,
	uniqAndCleanArray,
} from '../../../../../views/sys/custom/form/form-editor/utils/utils'
import events from '../../../../../views/sys/custom/form/form-editor/utils/mixins/events'

/**
 * FbFormEditorColAddRun
 * (c) 2021 lincong1987
 */

export default {
	name: 'FbFormEditorColAddRun',

	mixins: [events],

	inject: ['formEditor', 'formAdd'],

	props: {

		param: {
			type: [Object],
		},

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

		formData: {
			type: [Object],
		},

		isAdd: {
			type: Boolean
		},
	},

	data () {
//		let userInfo = this.$datax.get('userInfo')
//		let ctx = {
//			...toRaw(userInfo),
//		}

		return {
			ctx: this.formEditor.ctx,
			myFormData: this.formData,
		}
	},

	watch: {
		formData (value) {
			this.myFormData = value
		},
	},

	mounted () {
	},

	methods: {

		computeCurrentCtx (exp) {
			return computeCurrentCtx.apply(this, [exp])
		},

		stringToArray,

		isArray,

		toInteger,

		toNumber,

		isNumber,

		checkNumber (value) {
			return this.isNumber(this.toNumber(value))
		},
		checkInteger (value) {
			return isSafeInteger(this.toInteger(value))
		},

		updatePropsRaw (value, props, prop) {
			let arr = Array.prototype.slice.call(arguments)
			this.$set(this.myFormData.raw, arguments[arguments.length - 1], value)
			//this.$emit("input", value, 'label', arguments)
			//this.fbFormAdd.updatePathProps.apply(this, arr)
		},
		updatePropsLabel (value, props, prop) {
			let arr = Array.prototype.slice.call(arguments)
			this.$set(this.myFormData.label, arguments[arguments.length - 1], value)
			//this.$emit("input", value, 'label', arguments)
			//this.fbFormAdd.updatePathProps.apply(this, arr)
		},
		updatePropsValue (value, props, prop) {
			let arr = Array.prototype.slice.call(arguments)
			// this.fbFormAdd.updatePathProps.apply(this, arr)
			this.$set(this.myFormData.value, arguments[arguments.length - 1], value)
			//this.$emit("input", value, 'value', arguments)
		},
		updateProps (value, props, prop) {
			let arr = Array.prototype.slice.call(arguments)
			this.fbFormAdd.updatePathProps.apply(this, ['formData'].concat(arr))
		},

		updateIntegerProps (value, props, prop) {
			let arr = Array.prototype.slice.call(arguments)
			if (this.checkInteger(arr[0])) {
				arr[0] = toInteger(arr[0])
				this.fbFormAdd.updatePathProps.apply(this, ['formData'].concat(arr))
			}
		},

		uniqAndCleanArray,

		arrayToObject,

		ruleFormat (rules = [], props = {}) {
			let required = false
			let rs = [{required}]

			if (!rules) {
				return rs
			}

			rules.forEach((n) => {
				if (n === 'required') {
					rs[0].required = true
				} else {
					let rule = {type: n}

					if (n === 'array') {
						if (typeof props.ruleArrayLen !== 'undefined') {
							rule.len = props.ruleArrayLen
						}
						if (typeof props.ruleArrayMax !== 'undefined') {
							rule.max = props.ruleArrayMax
						}
						if (typeof props.ruleArrayMin !== 'undefined') {
							rule.min = props.ruleArrayMin
						}
					}

					if (n === 'number') {
						if (typeof props.ruleNumberMax !== 'undefined') {
							rule.max = props.ruleNumberMax
						}
						if (typeof props.ruleNumberMin !== 'undefined') {
							rule.min = props.ruleNumberMin
						}
					}

					if (n === 'integer') {
						if (typeof props.ruleIntegerMax !== 'undefined') {
							rule.max = props.ruleIntegerMax
						}
						if (typeof props.ruleIntegerMin !== 'undefined') {
							rule.min = props.ruleIntegerMin
						}
					}

					if (n === 'money') {
						if (typeof props.ruleMoneyFixed !== 'undefined') {
							rule.fixed = props.ruleMoneyFixed
						}
					}

					if (n === 'string') {
						if (typeof props.ruleStringLen !== 'undefined') {
							rule.len = props.ruleStringLen
						}
						if (typeof props.ruleStringMax !== 'undefined') {
							rule.max = props.ruleStringMax
						}
						if (typeof props.ruleStringMin !== 'undefined') {
							rule.min = props.ruleStringMin
						}
						if (typeof props.ruleStringWhitespace !== 'undefined') {
							rule.whitespace = props.ruleStringWhitespace
						}

					}

					if (n === 'enum') {
						if (typeof props.ruleEnum !== 'undefined') {
							rule.enum = (props.ruleEnum || []).map((m) => m.value)
						}
					}

					rs.push(rule)
				}
			})

			return rs
		},

		getService (servicePath) {
			let svc = get(app.$svc, servicePath)

			console.log(`getService: ${servicePath}`)
			return svc ? svc : null
		},

	},

}
</script>

<style lang="less" scoped>

</style>
