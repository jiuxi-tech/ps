<template>

	<div
		:class="getClass"
		:style='getStyle'
		@scroll="handleBodyScrollLeft"
	>

		<pre :class="`${cls}-debug`">debug FbTableWrapper: {{ isAnyColumnsFreeze }}</pre>


	</div>

</template>

<script>
import { closest } from '../../../utils/componentUtils'
import { prefix } from '../../../../src/config'
import { measureScrollbar } from './utils'

/**
 * FbTableWrapper
 * (c) 2020 lincong1987
 */
export default {
	name: 'FbTableWrapper',
	props: {
		columns: {
			type: Array,
			default () {
				return []
			},
		},

		freeze: {
			type: [Boolean, String],
			default: false,
		},

		isAnyColumnsFreeze: {
			type: [Boolean, String],
			default: false,
		},

	},
	data () {

		return {
			cls: `${prefix}-simple-table`,
			prefix,
		}
	},

	inject: ['table'],

//	created () {
//		this.table = closest(this, 'FbSimpleTable')
//	},
//	beforeDestroy () {
//		this.table = null
//	},
	computed: {

		getClass () {

			let arr = []
			let {
				freeze,
				cls,
			} = this

			if (freeze === 'left') {
				arr.push(`${cls}-freeze-left`)
			}

			if (freeze === 'right') {
				arr.push(`${cls}-freeze-right`)
			}

			if (!freeze) {
				arr.push(`${cls}-main`)
			}

			return arr
		},

		getStyle () {
			let style = {}

			return style
		},
	},

	methods: {
		handleBodyScrollLeft () {
		},
	},

}
</script>

<style scoped>

</style>
