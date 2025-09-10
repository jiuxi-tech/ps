<template>
	<fb-col :span="rowType === 'row-12' ? 12 : 24" v-if="col && col.type && col.formItemProps.show">

		<fb-fieldset
			v-if="col.type === 'fb-fieldset'"
			:label="col.props.label"></fb-fieldset>

		<fb-property-item
			v-else
			:label="col.formItemProps.label"
			:label-width="col.formItemProps.labelWidth || formEditor.add.props.labelWidth"
		>
			<!--                :label-position="col.formItemProps.labelPosition || formEditor.add.props.labelPosition"-->
			<!--                :ellipsis="col.formItemProps.ellipsis"-->
			<!--                :show-label="col.formItemProps.showLabel"-->
			{{ '数据' }}
		</fb-property-item>
	</fb-col>
</template>

<script>

import { isArray, remove, uniqWith, filter, each } from 'lodash-es'
import { closest } from '@/util/componentUtils'

/**
 * FbFormEditorCol
 * (c) 2021 lincong1987
 */

export default {
	name: 'FbFormEditorColViewPreview',

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
		return {}
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
