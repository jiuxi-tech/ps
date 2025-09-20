<script>
import { closest } from '../../../utils/componentUtils'
import { prefix } from '../../../../src/config'
import FbTableRow from './FbTableRow'
import FbTableSubRow from './FbTableSubRow'

/**
 * FbTableBody
 * (c) 2020 lincong1987
 */
export default {
	name: 'FbTableBody',
	components: {
		FbTableRow,
		FbTableSubRow,
	},
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
//		this.table
//
		//this.table = closest(this, 'FbSimpleTable')
	},

	inject: ['table'],

	beforeDestroy () {
		//this.table = null
	},
	methods: {

		handleRowHover (isHover, rowIndex) {
			this.table.currentHoverRowIndex = isHover ? rowIndex : null
		},

		renderRows (h, myData, indent, ancestorKeys = []) {

			let noop = () => {
			}

			const {
				cls,
				freeze,
				table,
			} = this

			const {
				pk,
				columnManager,
				hasSubRow,
			} = table

			const rows = []

			for (let i = 0; i < myData.length; i += 1) {
				const rowData = myData[i]

				let leafColumns
				if (freeze === 'left') {
					leafColumns = columnManager.leftLeafColumns()
				} else if (freeze === 'right') {
					leafColumns = columnManager.rightLeafColumns()
				} else {
					leafColumns = columnManager.leafColumns()//this.getColumns()
				}

				rows.push(h('fb-table-row', {
					key: rowData[pk],
					ref: `tableRow_${i}_${freeze}`,
					props: {
						freeze,
						columns: leafColumns,
						row: rowData,
						rowIndex: i,
					},
				}))

				if (hasSubRow) {
//					rows.push(h('fb-table-sub-row', {
//						ref: `tableRow_${i}_${freeze}`,
//						key: `subrow_${rowData[pk]}`,
//						props: {
//							freeze,
//							columns: leafColumns,
//							row: rowData,
//							rowIndex: i,
//						},
//					}))
				}
			}
			return rows
		},

		renderSubRows () {

		},

	},
	computed: {},

	render (h, context) {

		let {
			cls,
			table,

		} = this

		let {myData} = table

		return h('tbody', {
			'class': `${cls}-tbody`,
		}, [
			this.renderRows(h, myData, 0),
		])
	},

}
</script>

<style scoped>

</style>
