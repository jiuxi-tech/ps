<script>
import { closest } from '../../../utils/componentUtils'
import { prefix } from '../../../../src/config'

/**
 * FbColGroup
 * (c) 2020 lincong1987
 */
export default {
	name: 'FbColGroup',
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
			cls: `${prefix}-simple-table`,
			prefix,
		}
	},
	created () {
		//this.table = closest(this, 'FbSimpleTable')
	},
	inject: ['table'],
	beforeDestroy () {
		//this.table = null
	},
	methods: {
		getStyle (col) {
			let style = {}
			style.width = style.minWidth = typeof col.width === 'number' ? `${col.width}px` : col.width
			return style
		},
	},
	computed: {},

	render (h, context) {

		let {
			freeze,
			getStyle,
			table,
		} = this

		let {columnManager} = table

		let columns

		if (freeze === 'left') {
			columns = columnManager.leftLeafColumns()
		} else if (freeze === 'right') {
			columns = columnManager.rightLeafColumns()
		} else {
			columns = columnManager.leafColumns()
		}

		return h('colgroup', {}, columns.map((col, index) => {
			return h('col', {
				key: index,
				style: getStyle(col),
			})
		}))

	},
}
</script>

<style scoped>

</style>
