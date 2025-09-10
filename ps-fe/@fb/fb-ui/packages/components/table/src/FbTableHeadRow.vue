<script>
import { prefix } from '../../../../src/config'
import FbTableHeadCell from './FbTableHeadCell'
import { classNames } from './utils'

import VueDraggableResizable from 'vue-draggable-resizable'
import FbCheckbox from '../../checkbox/src/FbCheckbox'
import { indexOf, isArray, intersection, union } from 'lodash-es'

/**
 * FbTableHeadRow
 * (c) 2020 lincong1987
 */
export default {
	name: 'FbTableHeadRow',
	components: {
		FbTableHeadCell,
		VueDraggableResizable,
		FbCheckbox,
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

		index: {
			type: Number,
			default: null,
		},

		rows: {
			type: Array,
			default () {
				return []
			},
		},

		row: {
			type: Array,
			default () {
				return []
			},
		},

//		height: {
//			type: [Number, String],
//			default: null,
//		},

		customHeaderRow: {
			type: Function,
			default: null,
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
	inject: ['table'],
	methods: {

		getRowHeight () {
			const {
				columns,
				rows,
				freeze,
				table,
			} = this

			const {freezeColumnsHeadRowsHeight} = table
			const headerHeight = freezeColumnsHeadRowsHeight[0]

			if (!freeze) {
				return null
			}

			if (headerHeight && columns) {
				if (headerHeight === 'auto') {
					return 'auto'
				}
				return `${headerHeight / rows.length}px`
			}
			return null
		},

	},
	computed: {},

	render (h) {

		let {
			row,
			index,
			height,
			table,
			cls,
		} = this

		let {
			headerFormatters,
			headerAttrs,
			sorters,
		} = table

		const style = {}//{height: this.getRowHeight()}
		if (style.height === null) {
			delete style.height
		}

		return h('tr', {
				style: style,
				'class': [`${cls}-thead-row`],
			},
			row.map((col, cellIndex) => {

				const {
					column,
					isLast,
					children,
//					...cellProps
				} = col

				if (col.headColSpan === 0 || col.headRowSpan === 0) {
					return null
				}

				let label = headerFormatters[col.name]
					? headerFormatters[col.name](col.label)
					: col.label

				let cell = null

				let colSorter = sorters[col.name]

				if (colSorter) {
					if (!colSorter.directions) {
						colSorter.directions = ['asc', 'desc']
					}

					if (!isArray(colSorter.directions)) {
						colSorter.directions = ['asc', 'desc']
					}

					if (colSorter.directions.length === 0) {
						colSorter.directions = ['asc', 'desc']
					}

				}

				let sorterCell = null

				let currentSortByDirection = table.mySorter &&
					table.mySorter.sortByColumn ===
					col.name && table.mySorter.sortByDirection || ''

				// 下一次排序方向
				let nextSortByDirection = ''

				if (colSorter && colSorter.directions) {

//					if (colSorter.directions.length === 1) {
//
//					}
//					if (colSorter.directions.length === 2) {
//
//					}

					let index = indexOf([...colSorter.directions, ''], currentSortByDirection)

					if (index === [...colSorter.directions, ''].length - 1) {
						nextSortByDirection = colSorter.directions[0]
					} else {
						nextSortByDirection = ([...colSorter.directions, ''])[index + 1]
					}

				}

				// 排序器
				if (!!colSorter) {
					sorterCell = h('span', {
						'class': classNames([
							`${cls}-column-sorters`,
						]),
					}, [
						h('div', {
							'class': classNames([`${cls}-column-sorter-inner`]),
						}, [
							colSorter.directions.includes('asc') ? h('div', {
								'class': `${cls}-column-sorter-asc`,
							}) : null,

							colSorter.directions.includes('desc') ? h('div', {
								'class': `${cls}-column-sorter-desc`,
							}) : null,
						]),
					])
				}

				// 基本类型
				if (col.type === 'normal') {

					if (col.titleSlot) {
						// 表头作用域插槽
						if (table.$scopedSlots[col.titleSlot]) {
							cell = table.$scopedSlots[col.titleSlot]({
								column: col,
							})
						}
					} else {
						cell = h('span',
							{
								'class': classNames([
									`${cls}-header-column`,
									{
										//[`${cls}-column-sorter-desc-active`]: !!table.mySorter.sortByColumn
									},
								]),

							}, [

								!!colSorter

									? h('fb-tooltip', {

										props: {
											content: nextSortByDirection === '' ? '取消排序' : (nextSortByDirection ===
											'asc'
												? '点击升序'
												: '点击降序'),
											placement: 'top',
										},

									}, [
										h('div', {
											'class': classNames([
												`${cls}-column-sort-wrapper`, {
													[`${cls}-column-sort-wrapper--active`]: table.mySorter &&
													table.mySorter.sortByColumn ===
													col.name,
													[`${cls}-column-sort-wrapper--active-asc`]: currentSortByDirection ===
													'asc',
													[`${cls}-column-sort-wrapper--active-desc`]: currentSortByDirection ===
													'desc',
												},
											]),
										}, [
											h('span', {
												'class': `${cls}-column-title`,
												attrs: {
													'title': label,
												},
											}, [label]),

											sorterCell,
										]),
									])

									: h('span', {
										'class': `${cls}-column-title`,
										attrs: {
											'title': label,
										},
									}, [label]),
							])
					}

				}

				if (col.type === 'rownum') {
					cell = h('span', {
						'class': `${cls}-header-column`,
					}, [table.rownumTitle])
				}

				if (col.type === 'radio') {
					cell = h('span', {
						'class': `${cls}-header-column`,
					})
				}

				if (col.type === 'multiple') {

					/********
					 * isMyDataAllChecked 有以下几种情况：true false 'indeterminate'
					 * 1 true
					 * 		thisPagePks === thisPageCheckedPks
					 *
					 * 2 false
					 *     thisPageCheckedPks.length === 0
					 *
					 * 3 'indeterminate'
					 *     thisPageCheckedPks !== thisPagePks
					 *
					 ********/

					let isMyDataAllChecked = false
					let multipleDisabled = false
					// 当前页 pk 数组
					let thisPagePks = table.myData.map(n => n[table.pk])

					let thisPageCheckedPks = intersection(table.mySelectedPks, thisPagePks)

					if (thisPageCheckedPks.length !== thisPagePks.length) {
						isMyDataAllChecked = 'indeterminate'
					}

					if (thisPagePks.length === thisPageCheckedPks.length) {
						isMyDataAllChecked = true
					}

					if (thisPageCheckedPks.length === 0) {
						isMyDataAllChecked = false
					}

					if (thisPagePks.length === 0) {
						multipleDisabled = true
					}

					let tooltipContent = `已选 ${table.mySelectedPks.length}/${table.myData.length} 项`

					if (table.myPager.total) {
						tooltipContent = `已选 ${table.mySelectedPks.length}/${table.myPager.total} 项`
					}

					cell = h('span', {
						'class': `${cls}-header-column`,
					}, [
						h('fb-tooltip', {
							props: {
								content: tooltipContent,
								placement: 'top',
							},
						}, [
							h('fb-checkbox', {
								props: {
									disabled: multipleDisabled,
									value: isMyDataAllChecked,
									label: table.multipleTitle,
								},
								on: {
									'on-click' (value, event) {
										event.preventDefault()

										if (value) {
											table.mySelectedPks = union(table.mySelectedPks, thisPagePks)
										} else {
											thisPagePks.forEach((pk) => {
												let index = table.mySelectedPks.indexOf(pk)

												if (index !== -1) {
													table.mySelectedPks.splice(index, 1)
												}
											})
										}

										table.$nextTick(() => {
											table.$emit('on-row-select-all', value, table.mySelectedPks, table.myData)
										})

									},
								},
							}),
						]),

					])
				}

				if (col.type === 'subrow') {
					cell = h('span', {
						'class': `${cls}-header-column`,
					})
				}

				let events = {}

				if (colSorter) {
					events.click = (e) => {
						table.doSort({
							...colSorter,
							name: col.name,
						})
					}
				}

				return h('th',
					{
						'class': classNames([
								`${cls}-thead-row-cell`,
								`${cls}-thead-row-cell--${col.type}`,
								{
									[`${cls}-thead-row-cell--resize`]: col.resize,
								},
							],
							{
								[`${cls}-thead-row-cell--align-${col.titleAlign}`]: !!col.titleAlign,
								[`${cls}-thead-row-cell--ellipsis`]: !!col.titleEllipsis,
								[`${cls}-thead-row-cell--break-word`]: !!col.width,

								[`${cls}-thead-row-cell--has-sorter`]: !!colSorter,
							}),
						style: {...col.titleStyle},

						attrs: {
							rowSpan: typeof col.headRowSpan !== 'undefined'
								? col.headRowSpan
								: (typeof col.rowSpan !== 'undefined' ? col.rowSpan : 1),
							colSpan: typeof col.headColSpan !== 'undefined'
								? col.headColSpan
								: (typeof col.colSpan !== 'undefined' ? col.colSpan : 1),
						},

						on: events,

					},
					[
						cell,

						col.resize === true && typeof col.width === 'number' ?
							h('vue-draggable-resizable', {

								key: cellIndex + col.name || new Date().getTime(),

								'class': `${cls}-thead-row-cell-draggable-handle`,

								props: {
									w: 10,
									z: 1,
									//x: 0,//col.width,
									axis: 'x',
									scale: 1,
									resizable: false,
									draggable: true,
								},

								on: {
									dragging (left, top) {
										let width = col.width + left

										if (col.minWidth
											&& typeof col.minWidth === 'number'
											&& width <= col.minWidth) {
											width = col.minWidth
										}

										table.debounceUpdateColWidth(col.name, width)
									},
									dragstop (left, top) {

										let width = col.width + left

										if (col.minWidth
											&& typeof col.minWidth === 'number'
											&& width <= col.minWidth) {
											width = col.minWidth
										}

										table.debounceUpdateColWidth(col.name, width)

									},
								},

							}, [])
							: null,

					])
			}),
		)

	},

}
</script>

<style scoped>

</style>
