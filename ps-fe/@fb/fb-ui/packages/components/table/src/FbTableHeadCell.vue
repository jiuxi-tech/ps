<template>

	<th>
		<pre>{{ columns }}</pre>
	</th>

</template>

<script>
import { closest } from '../../../utils/componentUtils'
import { prefix } from '../../../../src/config'

/**
 * FbTableHeadCell
 * (c) 2020 lincong1987
 */
export default {
	name: 'FbTableHeadCell',
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
	},
	data () {
		let {showHeader} = this.table
		return {
			showHeader,
			cls: `${prefix}-simple-table`,
			prefix,
		}
	},
	beforeCreate () {
		this.table = closest(this, 'FbSimpleTable')
	},

//	created () {
//		this.table = closest(this, 'FbSimpleTable')
//	},
//	beforeDestroy () {
//		this.table = null
//	},
	inject: ['table'],
	methods: {
		getStyle (col) {
			let style = {}
			style.width = style.minWidth = typeof col.width === 'number' ? `${col.width}px` : col.width
			return style
		},
	},
	computed: {
		getClass () {
			let arr = []

			return arr
		},
	},

}
</script>

<style scoped>

</style>
