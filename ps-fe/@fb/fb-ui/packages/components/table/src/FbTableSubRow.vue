<script>
import { closest } from '../../../utils/componentUtils'
import { prefix } from '../../../../src/config'
import FbTableRowCell from './FbTableRowCell'
import { classNames, debounce } from './utils'

/**
 * FbTableSubRow
 * (c) 2020 lincong1987
 */
export default {
	name: 'FbTableSubRow',
	components: {FbTableRowCell},
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

		// 行数据
		row: {
			type: Object,
			default () {
				return {}
			},
		},

		// 行索引
		rowIndex: {
			type: [Number, String],
			default: undefined,
		},

		onHover: {
			type: Function,
			default () {
				return () => {
				}
			},
		},
	},
	data () {
		let {table} = this

		return {
			table,
			cls: `${prefix}-simple-table`,
			prefix,
			subRowActivated: false,
			rowPk: this.row[table.pk],
		}
	},
	methods: {

//		handleSubRowClick (event) {
//			const {
//				row,
//				rowIndex,
//				table,
//			} = this
//
//			table.$emit('on-subrow-click', row, rowIndex, event)
//		},
//
//		handleMouseEnter (event) {
//
//			const {
//				row,
//				rowIndex,
//				rowPk,
//				table,
//			} = this
//
//			this.handleSubRowHover(true, rowPk)
//			table.$emit('on-subrow-hover', true, row, rowIndex, event)
//			table.$emit('on-subrow-mouseenter', row, rowIndex, event)
//		},
//
//		handleMouseLeave (event) {
//			const {
//				row,
//				rowIndex,
//				rowPk,
//				table,
//			} = this
//
//			this.handleSubRowHover(false, rowPk)
//			table.$emit('on-subrow-hover', false, row, rowIndex, event)
//			table.$emit('on-subrow-mouseleave', row, rowIndex, event)
//		},

		setSubRowHeight () {
			const {
				table,
				rowPk,
			} = this

//			const height = this.$refs.subrow.getBoundingClientRect().height
//
//			table.subRowsHeight[rowPk] = height
		},

		updateRowHeight () {
			const {
				freeze,
				subRowActivated,
				table,
			} = this

			let {isAnyColumnsFreeze} = table

			if (!isAnyColumnsFreeze) {
				return
			}
			if (!freeze && subRowActivated) {
				this.setSubRowHeight()
			}
		},

		getRowHeight () {

			let {
				freeze,
				rowPk,
				table,
			} = this

			let {
				subRowsHeight,
			} = table

			if (!freeze) {
				// console.log(`freeze: ${freeze} rowPk: ${rowPk}`)
				return null
			}

			if (subRowsHeight[rowPk]) {
				return subRowsHeight[rowPk]
			}

			return null
		},

		handleRowHover (isHover, rowPk) {
			this.table.currentHoverSubRowPk = isHover ? rowPk : null
		},

		hovered () {
			let {
				table,
				rowPk,
			} = this
			let {
				currentHoverSubRowPk,
			} = table

			return currentHoverSubRowPk === rowPk
		},

	},
	computed: {

//		visible () {
//
//		},

//		height () {
//			return this.getRowHeight()
//		},

	},

	inject: ['table'],
//	beforeCreate () {
//		this.table = closest(this, 'FbSimpleTable')
//	},
//
//	created () {
//		this.table = closest(this, 'FbSimpleTable')
//	},
//	beforeDestroy () {
//		this.table = null
//	},
	mounted () {
		//this.$nextTick(() => {
		//this.updateRowHeight()

		//this.debouncedUpdateRowHeight = debounce(this.updateRowHeight, 150)

		//})
	},

	watch: {},

	render (h, context) {

		let {
			columns,
			freeze,
			table,
			row,
			rowIndex,
			rowPk,
			cls,
			hovered,
			height,
			subRowActivated,
		} = this

		let {
			pk,
			hasSubRow,
			columnManager,
			formatters,
			currentHoverRowPk,
			subRowsActivated,
		} = table

		let style = {}

		if (!subRowsActivated[rowPk]) {
			style.display = 'none'
		}

		return h('tr', {
				ref: 'subrow',
				style: {
					// height: typeof height === 'number' ? `${height}px` : height,
					...style,
				},
				'class': classNames([`${cls}-tbody-row`, `${cls}-tbody-subrow`], {}),
				attrs: {
					'data-pk': rowPk,
				},
			},
			[
				h('td', {
					attrs: {
						colspan: columns.length,
					},
				}, [
					table.$scopedSlots.subrow && table.$scopedSlots.subrow({
						columns,
						row,
						rowIndex,
						rowPk,
					}),
				]),
			])

	},

}
</script>

<style scoped>

</style>
