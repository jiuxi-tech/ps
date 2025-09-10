<script>
import { closest } from '../../../utils/componentUtils'
import { prefix } from '../../../../src/config'
import FbTableRowCell from './FbTableRowCell'
import FbRadio from '../../radio/src/FbRadio'
import FbCheckbox from '../../checkbox/src/FbCheckbox'
import FbIcon from '../../icon/src/FbIcon'
import { classNames, debounce } from './utils'

/**
 * FbTableRow
 * (c) 2020 lincong1987
 */
export default {
	name: 'FbTableRow',
	components: {
		FbTableRowCell,
		FbRadio,
		FbCheckbox,
		FbIcon,
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
//		onRowClick: {
//			type: Function,
//			default () {
//				return () => {
//				}
//			},
//		},
//		onRowDoubleClick: {
//			type: Function,
//			default () {
//				return () => {
//				}
//			},
//		},
//		onRowContextMenu: {
//			type: Function,
//			default () {
//				return () => {
//				}
//			},
//		},
//		onRowMouseEnter: {
//			type: Function,
//			default () {
//				return () => {
//				}
//			},
//		},
//		onRowMouseLeave: {
//			type: Function,
//			default () {
//				return () => {
//				}
//			},
//		},
	},
	data () {
		let {table} = this

		return {
			cls: `${prefix}-simple-table`,
			prefix,
			subRowActivated: false,
			rowPk: this.row[table.pk],
		}
	},
	methods: {
		/**************************************/
		/* ROW EVENTS */
		/**************************************/
		handleRowClick (event) {

			const {
				cls,
				row,
				rowIndex,
				table,
				rowPk,
			} = this

			if (event.target.closest(`.${cls}-tbody-row-cell--radio`) ||
				event.target.closest(`.${cls}-tbody-row-cell--checkbox`) ||
				event.target.closest(`.${cls}-tbody-row-cell--multiple`) ||
				table.beforeCellClick(rowPk, table.mySelectedPks, row, rowIndex) === false) {
				event.preventDefault()
				return false
			}

			if (event.target.closest(`.${cls}-tbody-row-cell--rownum`)) {
				return
			}

			if (event.target.closest(`.${cls}-tbody-row-cell--subrow`)) {
				return
			}

			if (event.target.closest(`.${cls}-tbody-row-cell--slot`)) {
				return
			}

			if (event.target.closest(`.${cls}-tbody-row-cell--normal`)) {
				event.preventDefault()

				let {
					autoSelect,
					radio,
					hideRadio,
					multiple,
					hideMultiple,
					mySelectedPks,
				} = table

				if (autoSelect) {

					let index = mySelectedPks.indexOf(rowPk)

					if (radio || (!radio && hideRadio)) {
						mySelectedPks.splice(0)

						if (index === -1) {
							mySelectedPks.push(rowPk)
						}

//						if (mySelectedPks.length) {
//							mySelectedPks[0] = rowPk
//						} else{
//						}

//						setTimeout(() => {
//							mySelectedPks.splice(0)
//							this.$nextTick(() => {
//
//							})
//						}, 1000)

					} else if (multiple || (!multiple && hideMultiple)) {

						// 如果是选中
						if (index === -1) {
							if (!mySelectedPks.includes(rowPk)) {
								mySelectedPks.push(rowPk)
							}
						} else {
							if (mySelectedPks.includes(rowPk)) {
								mySelectedPks.splice(index, 1)
							}
						}

					}

					table &&
					table.$emit('on-row-select', rowPk, mySelectedPks, row, rowIndex, index === -1, table.myData, event)
				}

			}

			table && table.$emit('on-row-click', row, rowIndex, event)
		},

		handleRowDoubleClick (event) {

			event.preventDefault()

			const {
				row,
				rowIndex,
				table,
			} = this

			table && table.$emit('on-row-dbl-click', row, rowIndex, event)
		},

		handleRowContextMenu (event) {

			event.preventDefault()

			const {
				row,
				rowIndex,
				table,
			} = this

			table && table.$emit('on-row-context-menu', row, rowIndex, event)
		},

		handleRowMouseEnter (event) {

			event.preventDefault()

			const {
				row,
				rowIndex,
				rowPk,
				table,
			} = this

			this.debouncedHandleRowHover(true, rowPk)
			table && table.$emit('on-row-hover', true, row, rowIndex, event)
			table && table.$emit('on-row-mouseenter', row, rowIndex, event)
		},

		handleRowMouseLeave (event) {

			event.preventDefault()

			const {
				row,
				rowIndex,
				rowPk,
				table,
			} = this

			this.debouncedHandleRowHover(false, rowPk)
			table && table.$emit('on-row-hover', false, row, rowIndex, event)
			table && table.$emit('on-row-mouseleave', row, rowIndex, event)
		},

		/**************************************/
		/* CELL EVENTS */
		/**************************************/
		handleCellClick (event, col, cellIndex) {
			const {
				row,
				rowIndex,
				table,
			} = this

			table && table.$emit('on-cell-click', col, cellIndex, row, rowIndex, event)
		},

		handleCellDoubleClick (event, col, cellIndex) {
			const {
				row,
				rowIndex,
				table,
			} = this

			table && table.$emit('on-cell-dbl-click', col, cellIndex, row, rowIndex, event)
		},

		handleCellContextMenu (event) {
			const {
				row,
				rowIndex,
				table,
			} = this

			table && table.$emit('on-cell-context-menu', row, rowIndex, event)
		},

		handleCellMouseEnter (event) {

			const {
				row,
				rowIndex,
				rowPk,
				table,
			} = this

			table && table.$emit('on-cell-hover', true, row, rowIndex, event)
			table && table.$emit('on-cell-mouseenter', row, rowIndex, event)
		},

		handleCellMouseLeave (event) {
			const {
				row,
				rowIndex,
				rowPk,
				table,
			} = this

			table && table.$emit('on-cell-hover', false, row, rowIndex, event)
			table && table.$emit('on-cell-mouseleave', row, rowIndex, event)
		},

		/**************************************/
		/* row methods */
		/**************************************/

		setRowHeight () {
			const {
				table,
				rowPk,
			} = this
			// const height = this.$refs.row.getBoundingClientRect().height

			// table.freezeColumnsBodyRowsHeight[rowPk] = height

//			// console.log(`设置冻结列行高 ${rowPk} ${height}`)

			this.$nextTick(() => {
				table && table.debounceSyncFreezeTableRowHeight()
			})

//			setTimeout(()=>{
//				table.freezeColumnsBodyRowsHeight[rowPk] = height
//			}, 3000)

//			table.subRowsActivated[rowPk] = !table.subRowsActivated[rowPk]
//
//			if (table.freezeColumnsBodyRowsHeight && table.freezeColumnsBodyRowsHeight[rowPk]) {
//				table.freezeColumnsBodyRowsHeight[rowPk] = height
//			}
		},

		setSubRowHeight () {
			const {
				table,
				rowPk,
			} = this

//			const height = this.$refs.subrow.getBoundingClientRect().height
//
//			table.subRowsHeight[rowPk] = height
		},

		// 更新 table 里得行高度 对象
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
			if (!freeze) {
				this.setRowHeight()
			}
		},

		// 从table 获取行高
		getRowHeight () {

			let {
				freeze,
				rowPk,
				table,
			} = this

			let {
				subRowsHeight,
				freezeColumnsBodyRowsHeight,
			} = table

			// console.log(`获取行高 ${freeze} ${rowPk} ${freezeColumnsBodyRowsHeight[rowPk]}`)

			if (!freeze) {
				return null
			}

//			if (subRowsHeight[rowPk]) {
//				return subRowsHeight[rowPk]
//			}

			if (freezeColumnsBodyRowsHeight && freezeColumnsBodyRowsHeight[rowPk]) {
				return freezeColumnsBodyRowsHeight[rowPk]
			}

			return null
		},

		handleRowHover (isHover, rowPk) {
			this.table.currentHoverRowPk = isHover ? rowPk : null
		},

		hovered () {
			let {
				table,
				rowPk,
			} = this
			let {
				currentHoverRowPk,
			} = table

			return currentHoverRowPk === rowPk
		},

		handleSubrowIconClick () {
			let {
				table,
				rowPk,
			} = this
			let {
				subRowsActivated,
			} = table
			if (typeof table.subRowsActivated[rowPk] === 'undefined') {
				table.subRowsActivated[rowPk] = false
			}
			table.subRowsActivated[rowPk] = !table.subRowsActivated[rowPk]

		},

	},

	beforeCreate () {

		//this.table = closest(this, 'FbSimpleTable')
	},

	inject: ['table'],

	created () {

		//this.table = closest(this, 'FbSimpleTable')
		this.debouncedHandleRowHover = debounce(this.handleRowHover, 32)
	},

	computed: {},

	mounted () {
		this.$nextTick(() => {
			this.debouncedUpdateRowHeight = debounce(this.updateRowHeight, 150, true)
			// this.debouncedUpdateRowHeight()
		})
	},

	beforeDestroy () {
		this.debouncedHandleRowHover && this.debouncedHandleRowHover.remove && this.debouncedHandleRowHover.remove()
		this.debouncedUpdateRowHeight && this.debouncedUpdateRowHeight.remove && this.debouncedUpdateRowHeight.remove()
		this.debouncedHandleRowHover.cancel()
		this.debouncedUpdateRowHeight.cancel()
		//this.table = null

	},

	beforeUnmounted () {
	},

	watch: {
//		'table.freezeColumnsBodyRowsHeight' () {
//			// this.debouncedUpdateRowHeight()
//		},
	},

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
			subRowActivated,
			handleRowClick,
			handleRowDoubleClick,
			handleRowMouseEnter,
			handleRowMouseLeave,
			handleRowContextMenu,

			handleCellClick,
			handleCellDoubleClick,
			handleCellMouseEnter,
			handleCellMouseLeave,
			handleCellContextMenu,

			handleSubrowIconClick,
		} = this

		let {
			pk,
			hasSubRow,
			columnManager,
			formatters,
			cellStyles,
			cellSpans,
			cellAttrs,
			currentHoverRowPk,
			freezeColumnsBodyRowsHeight,

			rowSpanGrid,
		} = table

		let mySelectedPks = table.mySelectedPks
		// // console.log(pk)

		let hover = {
			hover () {
			},
		}

		if (columnManager.isAnyColumnsFreeze()) {
			hover.hover = this.handleRowHover
		}

		let classes = []

		if ((rowIndex + 1) % 2 === 0) {
			classes.push(`${cls}-tbody-row-odd`)
		} else {
			classes.push(`${cls}-tbody-row-even`)
		}

		if (this.table.currentHoverRowPk === rowPk) {
			classes.push(`${cls}-tbody-row-hovered`)
		}

		if (mySelectedPks.includes(rowPk)) {
			//console.log('mySelectedPks.includes(rowPk)', mySelectedPks.includes(rowPk))
			classes.push(`${cls}-tbody-row-selected`)
		}

//		let height = null
		if (freeze) {
			//height = this.getRowHeight()//freezeColumnsBodyRowsHeight[rowPk]

			table.debounceSyncFreezeTableRowHeight()
		}

		let activated = table.subRowsActivated[rowPk] || false

		let mainRow = h('tr',
			{
				key: rowPk,
				ref: 'row',
				style: {
					//height: typeof height === 'number' ? `${height}px` : height,
				},
				'class': classNames([`${cls}-tbody-row`, `${cls}-tbody-bodyrow`, ...classes], {}),
				attrs: {
					'data-pk': rowPk,
					[`data-pk-${freeze}`]: rowPk,
					'data-freeze': freeze,
				},
				on: {
					mouseenter (e) {
						handleRowMouseEnter(e)
					},
					mouseleave (e) {
						handleRowMouseLeave(e)
					},
//					contextmenu (e) {
//						handleRowContextMenu(e)
//					},
				},

			},
			columns.map((col, cellIndex) => {

				if (col.rowSpan === 0 || col.colSpan === 0) {
					return null
				}

				let style = col.style || {}
				let className = col.className || []

				if (cellStyles[col.name]) {
					let cellStyle = cellStyles[col.name](row[col.name], row, col, rowIndex, cellIndex, rowPk)
					style = Object.assign({}, style, cellStyle)
				}

				let formatter = ''

				if (formatters[col.name]) {
					formatter = formatters[col.name](row[col.name], row, col, rowIndex, cellIndex, rowPk)
				}

				let attrs = {}
				if (col.showTitle) {
					attrs.title = (formatters[col.name] && formatter) || row[col.name]
				}

				let spans = {
					colSpan: 1,
					rowSpan: 1,
				}

				if (cellSpans[col.name]) {

					spans = Object.assign({}, spans,
						cellSpans[col.name](row[col.name], row, col, rowIndex, cellIndex, rowPk))

					if (spans.rowSpan === 0 || spans.colSpan === 0) {
						return null
					}

				}

				if (rowSpanGrid[col.name]) {

					if (typeof rowSpanGrid[col.name][rowIndex] !== 'undefined') {
						spans.rowSpan = rowSpanGrid[col.name][rowIndex]
					}

					if (spans.rowSpan === 0) {
						return null
					}

				}

				attrs = Object.assign({}, attrs, spans)

				if (cellAttrs[col.name]) {
					attrs = cellAttrs[col.name](attrs, row[col.name], row, col, rowIndex, cellIndex, rowPk)
				}
				return h('td', {

						style: [
							{...style},
							table.currentHoverRowPk === rowPk ? table.hoverStyle : '', // 触碰变色，因样式写在 td 上暂时写在这里
						],

						'class': classNames(`${cls}-tbody-row-cell`,
							{
								[`${cls}-tbody-row-cell--${col.type}`]: col.type &&
								!(col.slot && table.$scopedSlots[col.slot]),
								[`${cls}-tbody-row-cell--slot`]: !!(col.slot && table.$scopedSlots[col.slot]),
								[`${cls}-tbody-row-cell--align-${col.align}`]: !!col.align,
								[`${cls}-tbody-row-cell--ellipsis`]: !!col.ellipsis,
								[`${cls}-tbody-row-cell--break-word`]: !!col.width,
								[`${cls}-tbody-row-cell--column-sort`]: (table.mySorter && table.mySorter.sortByColumn !==
									'' && col.name ===
									table.mySorter.sortByColumn),

							}, className),

						attrs: attrs,

						on: {
							click (e) {
//								e.preventDefault();
								handleCellClick(e, col, cellIndex)
								handleRowClick(e)
							},
							dblclick (e) {
//								e.preventDefault();
								handleCellDoubleClick(e, col, cellIndex)
								handleRowDoubleClick(e)
							},
//							mouseenter (e) {
//								e.preventDefault();
//								handleCellMouseEnter(e)
//							},
//							mouseleave (e) {
//								e.preventDefault();
//								handleCellMouseLeave(e)
//							},
//							contextmenu (e) {
//								e.preventDefault();
//								handleCellContextMenu(e)
//							},
						},

					},
					[

						(() => {

							if (col.type === 'rownum') {
								if (table.$scopedSlots && table.$scopedSlots.rownum) {
									return table.$scopedSlots.rownum({
										value: row[col.name],
										row,
										column: col,
										rowIndex,
										cellIndex,
										rowPk,
									})
								} else {
									return row.rownum
								}
							}

							if (col.type === 'multiple') {

								return h('fb-checkbox', {
									props: {
										value: mySelectedPks.includes(rowPk),
									},
									on: {
										'on-click' (value, event) {
											event.preventDefault()

											let index = mySelectedPks.indexOf(rowPk)

											// 如果是选中
											if (value) {
												if (index === -1) {
													mySelectedPks.push(rowPk)
												}
											} else {
												if (index !== -1) {
													mySelectedPks.splice(index, 1)
												}
											}
											table.$emit('on-row-select', rowPk, mySelectedPks, row, rowIndex,
												index === -1, table.myData, value, event)
										},
									},
								})

							}
							if (col.type === 'radio') {

								return h('fb-radio', {
									props: {
										value: (() => {
											return mySelectedPks.includes(rowPk)
										})(),
									},
									on: {
										'on-click' (value, event) {

											event.preventDefault()

											mySelectedPks.splice(0, 1)

											if (value) {
												mySelectedPks.push(rowPk)
											}

											table.$emit('on-row-select', rowPk, mySelectedPks, row, rowIndex, value,
												table.myData, event)
										},
									},
								})

							}
							if (col.type === 'subrow') {

								return h('fb-icon', {
									props: {
										name: activated ? 'reduce-square' : 'add-square',
									},
									on: {
										'on-click.stop' (e) {
											setTimeout(() => {
												handleSubrowIconClick()
											}, 150)
											e.stopPropagation()
										},
									},

								})

							}

							if (col.slot) {

								if (table.$scopedSlots[col.slot]) {
									return table.$scopedSlots[col.slot]({
										value: row[col.name],
										row,
										column: col,
										rowIndex,
										cellIndex,
										rowPk,
									})
								}

							}

							if (formatters[col.name]) {
								return formatter
							}

							//	return JSON.stringify(col)
							// return currentHoverRowPk
							return row[col.name]

						})(),

					])
			}),
		)

		return [
			mainRow,
		]

	},
}
</script>

<style scoped>

</style>
