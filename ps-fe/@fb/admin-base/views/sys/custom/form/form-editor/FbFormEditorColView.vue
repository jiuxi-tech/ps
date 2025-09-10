<template>
	<fb-col :span="rowType === 'row-12' ? 12 : 24">

		<div v-if="col && col.type" class="editor-cell"
			 :class="{
						[`editor-cell--${rowType}`]: !!rowType,
						[`editor-cell--active`]: col.uuid === (activeComponent && activeComponent.uuid)
					}">
			<div class="editor-cell__mask"
				 @click="formEditor.handleActiveComponent(rowIndex, colIndex, col.type, col.uuid)"></div>

			<div class="editor-cell__component">

				<template v-if="col.type === 'fb-fieldset'">
					<fb-fieldset
						v-if="col.type === 'fb-fieldset'"
						:label="col.props.label"></fb-fieldset>
				</template>

				<template v-else>
					<fb-property-item
						:label="col.formItemProps.label"
						:label-width="col.formItemProps.labelWidth"
						:label-position="col.formItemProps.labelPosition"
						:ellipsis="col.formItemProps.ellipsis"
						:show-label="col.formItemProps.showLabel"
					>
						{{ col.props.name }}
					</fb-property-item>
				</template>

			</div>

			<div class="editor-cell__toolbar">
				<div class="editor-cell__title">[{{
						formEditor.getComponentNameByType(col.type)
					}}]
				</div>

				<div class="editor-cell__name">[{{
						col.type && col.formItemProps.name
					}}]
				</div>

<!--				<div class="editor-cell__remove">-->
<!--					<fb-button size="s" round danger-->
<!--							   icon="reduce-circle-fill"-->
<!--							   :color="colors.white"-->
<!--							   @on-click="formEditor.removeComponent('add', rowIndex, colIndex, col.uuid)"-->
<!--					>删除-->
<!--					</fb-button>-->
<!--				</div>-->
			</div>
		</div>

<!--		<div v-else class="editor-cell-empty">-->
<!--			-->
<!--&lt;!&ndash;			<fb-space align="center">&ndash;&gt;-->
<!--&lt;!&ndash;				<fb-select&ndash;&gt;-->
<!--&lt;!&ndash;					v-model="col.tempType"&ndash;&gt;-->
<!--&lt;!&ndash;					style="width: 120px"&ndash;&gt;-->
<!--&lt;!&ndash;					placeholder="请选择控件"&ndash;&gt;-->
<!--&lt;!&ndash;					:data="formEditor.editor.componentsNameList"&ndash;&gt;-->
<!--&lt;!&ndash;				/>&ndash;&gt;-->
<!--&lt;!&ndash;				{{ col.tempType }}&ndash;&gt;-->
<!--&lt;!&ndash;				<fb-button&ndash;&gt;-->
<!--&lt;!&ndash;					@on-click="formEditor.addComponent('add', rowIndex, colIndex, col.tempType, col.uuid)"&ndash;&gt;-->
<!--&lt;!&ndash;				>添加&ndash;&gt;-->
<!--&lt;!&ndash;				</fb-button>&ndash;&gt;-->
<!--&lt;!&ndash;			</fb-space>&ndash;&gt;-->
<!--		</div>-->

	</fb-col>
</template>

<script>

import { isArray, remove, uniqWith, filter, each } from 'lodash-es'
import { closest } from '../../../../../util/componentUtils'

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
	created () {
		this.formEditor = closest(this, 'FbFormEditor')
	},
	beforeDestroy () {
		this.formEditor = null
	},
	data () {
		return {
		}
	},

	methods: {

		uniqAndCleanArray (arr = [], uniqKey = 'label') {

			let data = []

			if (isArray(arr)) {
				data = uniqWith(filter(arr, (a) => a[uniqKey].trim() !== ''), (a, b) => a[uniqKey] === b[uniqKey])
			}

			console.log('uniqAndCleanArray', data)
			return data

		},

		arrayToObject (arr = [], label = 'label', value = 'value') {
			let param = {}

			if (isArray(arr)) {
				each(arr, (n, i) => {
					param[n[label]] = n[value]
				})
			}

			return param
		},

	},

}
</script>

<style lang="less" scoped>

</style>
