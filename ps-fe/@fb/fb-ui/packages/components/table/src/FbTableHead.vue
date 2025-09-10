<script>
import { closest } from '../../../utils/componentUtils'
import { prefix } from '../../../../src/config'
import FbTableHeadRow from './FbTableHeadRow'
import { renderHeaderRows } from './utils'

/**
 * FbTableHead
 * (c) 2020 lincong1987
 */
export default {
	name: 'FbTableHead',
	components: {FbTableHeadRow},
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
		let {columns} = this
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
	methods: {},
	computed: {},

	render (h, context) {

		const {
			expander,
			columns,
			cls,
			freeze,
			table,
		} = this
		const {showHeader} = table

		if (!showHeader) {
			return null
		}

		let rows = renderHeaderRows({columns})

		let classes = []

		if (freeze === 'left') {
			classes.push(`${cls}-thead-left`)
		}
		if (freeze === 'right') {
			classes.push(`${cls}-thead-right`)
		}
		if (!freeze) {
			classes.push(`${cls}-thead`)
		}

		// 多表头
		if (rows.length > 1) {
			classes.push(`${cls}-thead-multiple`)
		}

		return h('thead', {
				'class': classes,
				attrs: {},
			},
			rows.map((row, index) => {
				return h('fb-table-head-row', {
//					'class': `${cls}-thead`,
					props: {
						key: index,
						index: index,

						row: row,
						rows: rows,

						freeze: freeze,
						columns: columns,
					},
				}, [])

			}))

	},

}
</script>

<style scoped>

</style>
