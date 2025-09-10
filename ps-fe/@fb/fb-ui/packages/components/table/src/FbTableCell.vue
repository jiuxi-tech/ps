<template>
	<div>
		table-cell
	</div>
</template>

<script>
import { prefix } from '../../../../src/config'

/**
 * FbTableCell
 * (c) 2020 lincong1987
 */
export default {
	name: 'FbTableCell',
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

		return {
			prefix,
//			cols,
		}
	},
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
