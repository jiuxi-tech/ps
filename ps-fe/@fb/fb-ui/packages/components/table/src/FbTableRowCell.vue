<script>
import { closest } from '../../../utils/componentUtils'
import { prefix } from '../../../../src/config'

/**
 * FbTableRowCell
 * (c) 2020 lincong1987
 */
export default {
	name: 'FbTableRowCell',
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

		row: {
			type: Object,
			default () {
				return {}
			},
		},

		rowIndex: {
			type: [Number, String],
			default: undefined,
		},

		cell: {
			type: Object,
			default () {
				return {}
			},
		},

		cellIndex: {
			type: [Number, String],
			default: undefined,
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
	methods: {},
	computed: {},

	render (h, context) {

		let {
			cls,
			column,
			row,
			cell
		} = this

		return h('td', {
			'class': `${cls}-tbody-row-cell`,
		}, [
			cell.type,
		])
	},

}
</script>

<style scoped>

</style>
