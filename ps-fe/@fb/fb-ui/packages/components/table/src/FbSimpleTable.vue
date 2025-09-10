<script>

import { classNames, getDataAndAriaProps, uid, debounce, measureScrollbar } from './utils'
import { prefix } from '../../../../src/config'
import classes from './classes'
import FbTableWrapper from './FbTableWrapper'
import ColumnManager, { toRaw } from './ColumnManager'
import FbTableHead from './FbTableHead'
import FbTableHeadWrapper from './FbTableHeadWrapper'
import FbTableBodyWrapper from './FbTableBodyWrapper'
import {
	isEqualWith,
	isFunction,
	map,
	reduce,
	assign,
	remove,
	filter,
	isArray,
	difference,
	isEqual,
	indexOf,
} from 'lodash-es'
import addEventListener from './event'
import FbPager from '../../pager/src/FbPager'
import FbSpin from '../../spin/src/FbSpin'
import FbEmpty from '../../empty/src/FbEmpty'

export default {
	name: 'FbSimpleTable',
	components: {
		FbTableBodyWrapper,
		FbTableHeadWrapper,
		FbTableHead,
		FbTableWrapper,
		FbPager,
		FbSpin,
		FbEmpty,
	},

	props: {

		pk: {
			type: String,
			default: 'id',
		},

		useFreezeHeader: {
			type: Boolean,
			default: false,
		},

		fixed: {
			type: Boolean,
			default: true,
		},

		showHeader: {
			type: Boolean,
			default: true,
		},

		noHeadSplitter: {
			type: Boolean,
			default: false,
		},

		longHeadSplitter: {
			type: Boolean,
			default: false,
		},

		// 显示边框
		bordered: {
			type: Boolean,
			default: false,
		},

		headBordered: {
			type: Boolean,
			default: false,
		},

		loading: {
			type: Boolean,
			default: false,
		},

		loadingText: {
			type: String,
			default: '',//'加载中',
		},

		columns: {
			type: Array,
			default () {
				return []
			},
		},

		caption: {
			type: String,
			default: null,
		},

		footer: {
			type: String,
			default: null,
		},

		tableLayout: {
			type: String,
			default: null,
		},

		noDataText: {
			type: String,
			default: '没有数据',
		},

		noEmpty: {
			type: Boolean,
			default: false,
		},

		scroll: {
			type: [Object],
			default () {
				return {}
			},
		},
		wrapperStyle: {
			type: [Object, Array, String],
			default () {
				return {}
			},
		},

		bodyStyle: {
			type: [Object, Array, String],
			default () {
				return {}
			},
		},

		rownum: {
			type: Boolean,
			default: true,
		},

		rownumWidth: {
			type: Number,
			default: 50,
		},

		rownumTitle: {
			type: String,
			default: '#',
		},

		multiple: {
			type: Boolean,
			default: false,
		},
		hideMultiple: {
			type: Boolean,
			default: false,
		},
		multipleWidth: {
			type: Number,
			default: 30,
		},

		multipleTitle: {
			type: String,
			default: '',
		},

		radio: {
			type: Boolean,
			default: false,
		},

		hideRadio: {
			type: Boolean,
			default: false,
		},

		radioWidth: {
			type: Number,
			default: 30,
		},

		radioTitle: {
			type: String,
			default: '',
		},

		// 子表格操作列宽度
		subrowWidth: {
			type: Number,
			default: 50,
		},

		// 子表格表头显示文字
		subrowTitle: {
			type: String,
			default: '',
		},

		// 单元格格式化处理器
		formatters: {
			type: [Object],
			default () {
				return {}
			},
		},

		/**
		 * 单元格样式处理器
		 * @example
		 * :cell-styles="{
		 *
		 *     name(val){
		 *         return {
		 *             background: 'red',
		 *             color: 'green'
		 *         }
		 *     }
		 *
		 * }"
		 */
		cellStyles: {
			type: [Object],
			default () {
				return {}
			},
		},

		// 单元格合并处理器
		cellSpans: {
			type: [Object],
			default () {
				return {}
			},
		},

		renders: {
			type: [Object],
			default () {
				return {}
			},
		},

		headerFormatters: {
			type: [Object],
			default () {
				return {}
			},
		},

		headerAttrs: {
			type: [Object],
			default () {
				return {}
			},
		},

		service: {
			type: [Object, Function],
			default () {
				return null
			},
		},

		// 自动加载
		autoLoad: {
			type: Boolean,
			default: true,
		},

		param: {
			type: [Object, Array, Function],
			default: undefined,
		},

		//
		beforeDataMounted: {
			type: [Function],
			default () {
				return data => data
			},
		},

		data: {
			type: [Array, Function],
			default () {
				return []
			},
		},

		// 自动生成 pk
		autoPk: {
			type: [Boolean, Function],
			default: true,
		},

		// s m l
		size: {
			type: String,
			default: 'm',
		},

		reader: {
			type: [Object, Function],
			default () {
				return {
					sortByColumn: 'sortByColumn',
					sortByDirection: 'sortByDirection',

					pagerSize: 'size',
					pagerCurrent: 'current',
					pagerTotal: 'total',
					pagerPages: 'pages',
				}
			},
		},

		// 分页配置
		pager: {

			type: [Object, Boolean, Function],
			default () {
				return {}
			},

		},

		// 显示分页
		showPager: {
			type: [Boolean],
			default: true,
		},
		// 渲染分页
		noPager: {
			type: [Boolean],
			default: false,
		},

		afterRenderData: {
			type: [Function],
			default: null,
		},

		dataParse: {
			type: [Function],
			default: (json, vm) => {

				let result = {
					data: json.data.records,
					pager: {
						total: json.data.total,
						size: json.data.size,
						current: json.data.current,
						pages: json.data.pages,
					},
				}

				return result

			},
		},

		value: {
			type: [Array, String],
			default () {
				return []
			},
		},

		// 保持选中状态, 不受分页影响
		keepSelected: {
			type: [Boolean],
			default: false,
		},

		// 点击行 选中当前行
		autoSelect: {
			type: [Boolean],
			default: false,
		},

		// 当param 改变时，自动进行查询
		autoQuery: {
			type: [Boolean],
			default: false,
		},

		beforeCellClick: {
			type: [Function],
			default () {
				return true
			},
		},

//		debug: {
//			type: [Boolean, String],
//			default: false,
//		},

		// 单元格html属性处理器
		cellAttrs: {

			type: [Object],
			default () {
				return {}
			},

		},

		otherHeightProvider: {
			type: [Object, Window, Element, Function],
			default: undefined,
		},

		rowGroups: {
			type: [Array, Object],
			default () {
				return []
			},
		},

		autoScroll: {
			type: Boolean,
			default: false,
		},

		autoScrollSpeed: {
			type: [Number, String],
			default: 100,
		},
		autoScrollStep: {
			type: [Number, String],
			default: 1,
		},
		autoScrollDelay: {
			type: [Number, String],
			default: 200,
		},

		// 无限滚动
		autoScrollInfinite: {
			type: Boolean,
			default: false,
		},

		noDataStyle: {
			type: [Object, Array, String],
			default () {
				return {}
			},
		},

		showLoading: {
			type: Boolean,
			default: true,
		},

		containerStyle: {
			type: [Object, Array, String],
			default () {
				return {}
			},
		},

		sorters: {
			type: [Object],
			default () {
				return {}
			},
		},

		// 行触碰样式
		hoverStyle: {
			type: [Object, Array, String],
			default: '',
		},

		// 文本居中调整
		textAlign: {
			type: [String],
			default: 'left',
		},

		enableResize: {
			type: [Boolean],
			default: true,
		}

	},

	provide () {
		return {table: this}
	},

	data () {

		let myColumns = []

		// let {defaultProps: {FbSimpleTable: defaultProps}} = this
		let {defaultProps} = this

		return {

			defaultProps,

			uidIndex: 0,

			allchecked: false,

			cls: `${prefix}-simple-table`,

			scrollPosition: 'left',

//			lastScrollTop: 0,
//
//			lastScrollLeft: 0,

			freezeHeader: false,

			hasSubRow: false,

			columnManager: new ColumnManager(myColumns),

			currentHoverRowPk: null,
			currentHoverSubRowPk: null,

			freezeColumnsHeadRowsHeight: 0,

			freezeColumnsBodyRowsHeight: {},
			subRowsHeight: {},

			subRowsActivated: {},

			myData: [],//this.mixData(toRaw(this.data)),

			myColumns,

			myPager: this.mixPager(this.pager),

			mySorter: null,

			myReader: this.mixReader(this.reader),

			myFilter: null,

			myLoading: this.loading,

			myQueryParam: this.param,

			mySelectedPks: this.value,

			myOtherHeight: 0,

			rowSpanGrid: {},

			autoScrollFlag: this.autoScroll === true,

			tableHeadWrapperHeight: 40,

		}
	},

	computed: {

//		getTableWrapperClass () {
//
//			let {
//				cls,
//				scroll,
//				scrollPosition,
//			} = this
//
//			const arr = classNames({
//				[`${cls}`]: true,
//				[`${cls}-freeze-header`]: (scroll && scroll.y),
//				[`${cls}-scroll-position-left ${cls}-scroll-position-right`]: scrollPosition === 'both',
//				[`${cls}-scroll-position-${scrollPosition}`]: scrollPosition !== 'both',
//				[`${cls}-layout-freeze`]: this.isTableLayoutFreeze(),
//			})
//
//			// console.log(arr)
//
//			return arr
//		},

		watchData () {
			return toRaw(this.data)
		},

	},

	watch: {

		columns: {
			deep: true,
			handler (value) {
				console.log(value)
				if (value) {
					let myColumns = this.mixColumns(toRaw(value))
					this.myColumns = myColumns
					this.columnManager.reset(myColumns)
				}
			},
		},

		rownum (value) {
			let myColumns = this.mixColumns(toRaw(this.columns))
			this.myColumns = myColumns
			this.columnManager.reset(myColumns)
		},
		multiple (value) {
			let myColumns = this.mixColumns(toRaw(this.columns))
			this.myColumns = myColumns
			this.columnManager.reset(myColumns)
		},
		radio (value) {
			let myColumns = this.mixColumns(toRaw(this.columns))
			this.myColumns = myColumns
			this.columnManager.reset(myColumns)
		},

		watchData (value, old) {

			// 差异
			// let diff = isEqual(value, old)
			// if (diff) {
			// 	return
			// }

			if (!this.keepSelected) {
				this.mySelectedPks = []
			}

			let data = value
			if (typeof value === 'function') {
				data = value()
			}

			if (data.length === 0 && this.scroll && this.scroll.x) {
				this.$nextTick(() => {
					this.resetScrollX()
				})
			}

			this.myData = this.mixData(toRaw(data))

		},

		pager (value) {
			this.myPager = this.mixPager(value)
		},

		loading (value) {

			this.myLoading = value

			if (this.defaultProps.showLoading === false) {
				this.myLoading = false
			}

		},

		value (value) {
			this.mySelectedPks = value
		},

		mySelectedPks (value, old) {
			if (value === old) {
				return
			}
			this.$emit('input', value)
		},

		allchecked (value) {
			// this.mySelectedPks = value ? this.myData.map(n => n[this.pk]) : []
			//
			// this.$emit('on-row-select-all', value, this.mySelectedPks, this.myData)
		},

		param (value) {

			if (typeof value === 'function') {
				value = value()
			}

			this.myQueryParam = value
			if (this.autoQuery) {
				this.doSearch()
			}
		},

		autoScrollSpeed () {
			this.stopAutoScroll()
			setTimeout(() => {
				this.doAutoScroll()
			}, 160)
		},

		autoScrollFlag (value) {

			if (value) {
				setTimeout(() => {
					this.autoScrollFlag = true
					this.doAutoScroll()
				}, 160)
			} else {
				this.stopAutoScroll()
			}
		},

		autoScroll (value) {
			if (value) {
				setTimeout(() => {
					this.autoScrollFlag = true
					this.doAutoScroll()
				}, 160)
			} else {
				this.stopAutoScroll()
			}
		},

	},

	methods: {
		// 是否有冻结列
		isAnyColumnsFreeze (columns) {
			return columns.some(column => !!column.freeze)
		},

		isAnyColumnsFreezeLeft (columns) {
			return columns.some(column => column.freeze && column.freeze === 'left')
		},

		mixReader (reader = {}) {
			return assign({}, {
				sortByColumn: 'sortByColumn',
				sortByDirection: 'sortByDirection',

				pagerSize: 'size',
				pagerCurrent: 'current',
				pagerTotal: 'total',
				pagerPages: 'pages',
			}, this.myReader, reader)
		},

		// 处理分页参数
		mixPager (pager) {

			return assign({}, {

				// top bottom both
				position: 'bottom',
				// left center right
				align: 'right',
				simple: false,
				current: 1,
				size: 10,
				pages: 0,
				showTotalInfo: true,
				showQuickJumper: false,
				showSizeChanger: false,
				maxLength: 6,

			}, this.myPager, pager)
		},

		// 处理数据
		mixData (value, beforeMix, autoScrollInfiniteFlag = false) {
			let {
				myPager,
				myReader,
				autoPk,
				pk,
				service,
				data,
				afterRenderData,
			} = this
			console.log('mixData')
			if (isFunction(value)) {
				value = value(this)
			}

			if (!isArray(value)) {
				value = []
			}

			this.rawData = JSON.parse(JSON.stringify(value))

			if (beforeMix) {
				value = beforeMix(value)
			}

			if (autoPk) {
				value = value.map((n) => {
					if (typeof n[pk] === 'undefined') {
						n[pk] = uid()
					}
					return n
				})
			}

			// 如果绑定了数据服务
			if (service) {
				let startNum = (myPager[myReader.pagerCurrent] - 1) * myPager[myReader.pagerSize]
				value = value.map((n, i) => {
					n.rownum = startNum + i + 1
					return n
				})
			} else if (data) {
				// 如果有静态数据
				value = value.map((n, i) => {
					n.rownum = i + 1
					return n
				})
			}

			afterRenderData && afterRenderData(value)

			if (this.rowGroups.length) {
				this.rowGroups.forEach((n, i) => {
					this.createRowSpanGrid(value, n, i)
				})
			}

			if (this.autoScroll && this.autoScrollFlag && !autoScrollInfiniteFlag) {
				this.$nextTick(() => {
					setTimeout(() => {
						this.doAutoScroll()
					}, 160)
				})
			}

			return value
		},

		// 合并列数组
		groupBy (rows, colName) {
			const groups = {}
			rows.forEach(row => {
				const group = JSON.stringify(row[colName])
				groups[group] = groups[group] || []
				groups[group].push(row)
			})
			return Object.values(groups)
		},

		// 创建列合并矩阵
		createRowSpanGrid (rows, colName, index) {
			// let arr = []

			let pos = 0

			this.rowSpanGrid[colName] = []

			// this.groupBy(rows, colName).map(n => (arr = arr.concat(n)))

			const redata = rows.map(n => n[colName])
			redata.reduce((old, cur, i) => {
				if (i === 0) {
					this.rowSpanGrid[colName].push(1)
					pos = 0
				} else {
					if (cur === old) {
						this.rowSpanGrid[colName][pos] += 1
						this.rowSpanGrid[colName].push(0)
					} else {
						this.rowSpanGrid[colName].push(1)
						pos = i
					}
				}
				return cur
			}, {})
		},

		// 处理列配置
		mixColumns (columns) {

			remove(columns, (n) => {
				if (typeof n.hidden !== 'undefined' && n.hidden === true) {
					return true
				}

				return false
			})

			let isAnyColumnsFreeze = this.isAnyColumnsFreezeLeft(columns)

			if (this.rownum) {
				columns.unshift({
					type: 'rownum',
					name: 'rownum',
					label: this.rownumTitle || '#',
					freeze: isAnyColumnsFreeze,
					width: this.rownumWidth || 50,
					ellipsis: false,
					align: 'center',
					titleAlign: 'center',
					resize: false,
					minWidth: 50,
					showTitle: false,
				})
			}

			if (this.radio) {
				columns.unshift({
					type: 'radio',
					name: 'radio',
					label: this.radioTitle || '',
					freeze: isAnyColumnsFreeze,
					width: this.radioWidth || 30,
					ellipsis: false,
					align: 'center',
					titleAlign: 'center',
					resize: false,
					minWidth: 30,
					showTitle: false,
				})
			} else if (this.multiple) {
				columns.unshift({
					type: 'multiple',
					name: 'multiple',
					label: this.multipleTitle || '',
					freeze: isAnyColumnsFreeze,
					width: this.multipleWidth || 30,
					ellipsis: false,
					align: 'center',
					titleAlign: 'center',
					resize: false,
					minWidth: 30,
					showTitle: false,
				})
			}

			if (this.$scopedSlots.subrow) {
				columns.unshift({
					type: 'subrow',
					name: 'subrow',
					label: this.subrowTitle || '',
					freeze: isAnyColumnsFreeze,
					width: this.subrowWidth || 60,
					ellipsis: false,
					align: 'center',
					titleAlign: 'center',
					resize: false,
					minWidth: 60,
					showTitle: false,
				})
			}

			columns = columns.map(column => Object.assign({}, {
				name: '',
				label: '',
				hidden: false,
				type: 'normal',
				sortable: false, // width: 20,
				align: this.textAlign || 'left',
				titleAlign: this.textAlign || 'left',
				titleStyle: null,
				ellipsis: true,
				titleEllipsis: true,
				resize: this.enableResize,
				minWidth: 80,
				showTitle: true,
			}, column))

			return columns

		},

		// 自适应模式处理
		isTableLayoutFreeze () {

			if (typeof this.tableLayout !== 'undefined') {
				return this.tableLayout === 'freeze'
			}
			if (this.columns.some(({ellipsis}) => !!ellipsis)) {
				return true
			}
			if (this.useFreezeHeader || this.scroll.y) {
				return true
			}
			if (this.scroll.x && this.scroll.x !== true && this.scroll.x !== 'max-content') {
				return true
			}
			return false
		},

		updateColWidth (name, width) {

			this.myColumns.forEach(function (col) {
				if (col.name === name) {
					col.width = width
				}
			})

		},

		// 重置滚动条至左侧
		resetScrollX () {
			this.ref_tableHeadWrapper && (this.ref_tableHeadWrapper.scrollLeft = 0)
			this.ref_tableBodyWrapper && (this.ref_tableBodyWrapper.scrollLeft = 0)
		},

		resetScrollY () {
			this.ref_tableHeadWrapper && (this.ref_tableHeadWrapper.scrollTop = 0)
			this.ref_tableBodyWrapper && (this.ref_tableBodyWrapper.scrollTop = 0)
		},

		addRef (name, component) {
			if (component && component.$refs && component.$refs[name]) {
				this[`ref_${name}`] = component.$refs[name]
				this.refs.push(`ref_${name}`)
				// // console.log(`添加引用 ref_${name}`)
			}
		},

		// 处理浏览器窗口变化
		handleWindowResize () {

			this.debounceSyncFreezeTableRowHeight()
			this.setScrollPositionClassName()
			this.debounceHandleBodyResize()
		},

		handleBodyResize () {

			let ohp
			let {getOtherHeight} = this
			this.myOtherHeight = getOtherHeight()
		},

		getOtherHeight () {
			let ohp
			let {otherHeightProvider} = this

			switch (typeof otherHeightProvider) {
				case 'undefined':
					ohp = window
					break
				case 'function':
					ohp = otherHeightProvider(window)
					break
				case 'number':
					ohp = otherHeightProvider
					break
			}

			if (typeof Window !== 'undefined' && ohp instanceof Window) {
				return ohp.innerHeight
			} else {
				return parseInt(window.getComputedStyle(ohp, null).height, 10)
				//return ohp.getBoundingClientRect().height
			}
		},

		/**
		 *
		 * @param position
		 */
		setScrollPosition (position) {
			this.scrollPosition = position
			if (this.ref_tableBodyWrapper) {
				const {cls} = this
				if (position === 'both') {
					classes(this.ref_tableWrapper).
						remove(new RegExp(`^${cls}-scroll-position-.+$`)).
						add(`${cls}-scroll-position-left`).
						add(`${cls}-scroll-position-right`)
				} else {
					classes(this.ref_tableWrapper).
						remove(new RegExp(`^${cls}-scroll-position-.+$`)).
						add(`${cls}-scroll-position-${position}`)
				}
			}
		},

		syncFreezeTableRowHeight () {
			// // console.log(`同步冻结列高度`)

			if (!this.ref_tableWrapper) {
				return
			}

			//const tableRect = this.ref_tableWrapper.getBoundingClientRect()

			const tableRectHeight = parseInt(window.getComputedStyle(this.ref_tableWrapper, null).height, 10)

			if (tableRectHeight !== undefined && tableRectHeight <= 0) {
				return
			}

			const {cls} = this
			let tableHeadWrapper = this.ref_tableHeadWrapper
				? this.ref_tableHeadWrapper
				: this.ref_tableBodyWrapper
			if (!tableHeadWrapper) {
				return
			}
			const headRows = this.ref_tableWrapper.querySelectorAll(`.${cls}-thead`)

//			let freezeColumnsHeadRowsHeight = map(headRows, row =>
//				row.getBoundingClientRect().height
//					? row.getBoundingClientRect().height - 0.5
//					: 'auto',
//			)

			let leftHead = this.ref_tableWrapper.querySelectorAll(`.${cls}-thead-left tr`)
			if (leftHead && leftHead.length > 0) {

//				let height =
//					headRows[0].getBoundingClientRect().height - 0//.5

				let height = parseInt(window.getComputedStyle(headRows[0], null).height,10)
				leftHead[0].style.height = typeof height === 'number' ? `${height}px` : height

			}

			let rightHead = this.ref_tableWrapper.querySelectorAll(`.${cls}-thead-right tr`)
			if (rightHead && rightHead.length > 0) {

//				let height =
//					headRows[0].getBoundingClientRect().height - 0//.5
				let height = parseInt(window.getComputedStyle(headRows[0], null).height,10)
				rightHead[0].style.height = typeof height === 'number' ? `${height}px` : height

			}

			if (!this.ref_tableBodyWrapper) {
				return
			}
//
			const bodyRows = this.ref_tableBodyWrapper.querySelectorAll(`.${cls}-tbody-bodyrow`) ||
				[]

			if (bodyRows.length === 0) {
				return
			}

			bodyRows.forEach((row, index) => {
				const pk = row.getAttribute('data-pk')
				// 	let height = window.getComputedStyle(headRows[0], null).height
				const height =
					//row.getBoundingClientRect().height ||
					parseInt(window.getComputedStyle(row, null).height, 10) ||
					this.freezeColumnsBodyRowsHeight[pk] ||
					'auto'

				// // console.log(height)
				if (this.ref_freezeColumnsBodyLeft) {

					if (!this.refs.includes('ref_freezeColumnsBodyLeft')) {
						this.refs.push('ref_freezeColumnsBodyLeft')
					}

					const leftRows = this.ref_freezeColumnsBodyLeft.querySelectorAll(`[data-pk-left='${pk}']`) ||
						[]

					if (leftRows.length > 0) {
						leftRows[0].style.height = typeof height === 'number' ? `${height}px` : height
					}
				}

				if (this.ref_freezeColumnsBodyRight) {

					if (!this.refs.includes('ref_freezeColumnsBodyRight')) {
						this.refs.push('ref_freezeColumnsBodyRight')
					}

					const rightRows = this.ref_freezeColumnsBodyRight.querySelectorAll(`[data-pk-right='${pk}']`) ||
						[]

					if (rightRows.length > 0) {
						rightRows[0].style.height = typeof height === 'number' ? `${height}px` : height
					}

				}

			})

		},
		setScrollPositionClassName () {
			const node = this.ref_tableBodyWrapper
			const scrollToLeft = node.scrollLeft === 0
//			const scrollToRight =
//				node.scrollLeft + 1 >=
//				node.children[0].getBoundingClientRect().width -
//				node.getBoundingClientRect().width

			const scrollToRight =
				node.scrollLeft + 1 >=
				parseInt(window.getComputedStyle(node.children[0], null).width, 10) -
				parseInt(window.getComputedStyle(node, null).width, 10)

			if (scrollToLeft && scrollToRight) {
				this.setScrollPosition('both')
			} else if (scrollToLeft) {
				this.setScrollPosition('left')
			} else if (scrollToRight) {
				this.setScrollPosition('right')
			} else if (this.scrollPosition !== 'middle') {
				this.setScrollPosition('middle')
			}
		},

		handleBodyScrollLeft (e) {

			// // console.log(`handleBodyScrollLeft`)
			if (e.currentTarget !== e.target) {
				return
			}

			const target = e.target
			const {scroll = {}} = this
			const {
				ref_tableHeadWrapper,
				ref_tableBodyWrapper,
			} = this

			if (target.scrollLeft !== this.lastScrollLeft && scroll.x) {
				if (target === ref_tableBodyWrapper && ref_tableHeadWrapper) {
					ref_tableHeadWrapper.scrollLeft = target.scrollLeft
				} else if (target === ref_tableHeadWrapper && ref_tableBodyWrapper) {
					ref_tableBodyWrapper.scrollLeft = target.scrollLeft
				}
				this.setScrollPositionClassName()
			}
			this.lastScrollLeft = target.scrollLeft
		},

		handleBodyScrollTop (e) {
			//	// console.log(`handleBodyScrollTop`)
			const target = e.target
			if (e.currentTarget !== target) {
				return
			}
			const {scroll = {}} = this
			const {
				ref_tableHeadWrapper,
				ref_tableBodyWrapper,
				ref_freezeColumnsBodyLeft,
				ref_freezeColumnsBodyRight,
			} = this

			if (target.scrollTop !== this.lastScrollTop && scroll.y && target !==
				ref_tableHeadWrapper) {
				const scrollTop = target.scrollTop
				if (ref_freezeColumnsBodyLeft && target !== ref_freezeColumnsBodyLeft) {
					ref_freezeColumnsBodyLeft.scrollTop = scrollTop
				}
				if (ref_freezeColumnsBodyRight && target !== ref_freezeColumnsBodyRight) {
					ref_freezeColumnsBodyRight.scrollTop = scrollTop
				}
				if (ref_tableBodyWrapper && target !== ref_tableBodyWrapper) {
					ref_tableBodyWrapper.scrollTop = scrollTop
				}
			}
			// Remember last scrollTop for scroll direction detecting.
			this.lastScrollTop = target.scrollTop
		},

		doAutoScroll () {

			//return

			let {
				ref_tableBodyWrapper,
				ref_freezeColumnsBodyLeft,
				ref_freezeColumnsBodyRight,
				scroll,
				doMove,
				autoScrollSpeed,
				startAutoScroll,
				stopAutoScroll,
				autoScrollInfinite,
			} = this

			if (!scroll.y) {
				return
			}

//			this.ref_tableBodyWrapper__height =
//				ref_tableBodyWrapper.getBoundingClientRect().height
			this.ref_tableBodyWrapper__height =
				parseInt(window.getComputedStyle(ref_tableBodyWrapper, null).height, 10)

		this.ref_tableBodyWrapper__table = ref_tableBodyWrapper.querySelector('table')

			if (!this.refs.includes('ref_tableBodyWrapper__table')) {
				this.refs.push('ref_tableBodyWrapper__table')
			}

			this.ref_tableBodyWrapper__table__height =
				parseInt(window.getComputedStyle(this.ref_tableBodyWrapper__table, null).height, 10)
//				this.ref_tableBodyWrapper__table.getBoundingClientRect().height

			if (this.ref_tableBodyWrapper__table__height <= this.ref_tableBodyWrapper__height) {
				return
			}

			if (autoScrollInfinite) {

				this.myData = this.mixData(this.rawData, (data) => {
					return data.concat(JSON.parse(JSON.stringify(data)))
				}, true)

				this.doInfiniteMove()
			} else {
				this.doMove()
			}
		},

		stopAutoScroll () {
			//// console.log('stopAutoScroll')
			this.autoScrollFlag = false
			clearTimeout(this.doAutoScrollTimer)
		},

		doMove (ts) {
			let {
				ref_tableBodyWrapper,
				ref_freezeColumnsBodyLeft,
				ref_freezeColumnsBodyRight,
				scroll,
				autoScrollSpeed,
				autoScrollStep,
				ref_tableBodyWrapper__height,
				ref_tableBodyWrapper__table__height,
				startAutoScrollTimeStamp,
				nowAutoScrollTimeStamp,
			} = this

			//// console.log('doMove')

			clearTimeout(this.doAutoScrollTimer)

			if (this.autoScroll === false) {
				//// console.log('stop doMove autoScroll is f')
				return
			}

			if (this.autoScrollFlag === false) {
				//// console.log('stop doMove autoScrollFlag is f')
				return
			}

			// let measureScrollbarHeight = measureScrollbar({direction: 'vertical'})

			if ((ref_tableBodyWrapper__table__height - ref_tableBodyWrapper__height) <=
				(Math.ceil(ref_tableBodyWrapper.scrollTop))) {
				clearTimeout(this.doAutoScrollTimer)
				this.doAutoScrollTimer = window.setTimeout(() => {
					// ref_tableBodyWrapper.scrollTop = 0
					this.scrollToTop()
					setTimeout(() => {
						ref_tableBodyWrapper.scrollTop = 0
						this.$emit('on-auto-scroll-bottom')
						this.doMove()
					}, 500)
				}, parseFloat(this.autoScrollDelay))
				return
			} else {

				if (ref_tableBodyWrapper.scrollTop === 0) {
					ref_tableBodyWrapper.scrollTop = this.autoScrollStep + ref_tableBodyWrapper.scrollTop
					clearTimeout(this.doAutoScrollTimer)
					this.doAutoScrollTimer = setTimeout(() => {
						this.doAutoScrollTimer = window.setTimeout(() => {
							this.doMove()
						})
					}, parseFloat(this.autoScrollDelay))
					return
				}
				let st = this.autoScrollStep + ref_tableBodyWrapper.scrollTop
				ref_tableBodyWrapper.scrollTop = st
				this.$emit('on-auto-scroll-step', st)
			}

			clearTimeout(this.doAutoScrollTimer)
			this.doAutoScrollTimer = window.setTimeout(() => {
				this.doMove()
			}, parseFloat(this.autoScrollSpeed))
		},

		doInfiniteMove () {

			let {
				autoScrollSpeed,
				ref_tableBodyWrapper__table__height,
				ref_tableBodyWrapper__height,
				ref_tableBodyWrapper,
			} = this
			//this.myData = this.myData.concat(this.myData)
			//console.table(this.myData)

			clearTimeout(this.doAutoScrollTimer)

			if (this.autoScroll === false) {
				//// console.log('stop doMove autoScroll is f')
				return
			}

			if (this.autoScrollFlag === false) {
				//// console.log('stop doMove autoScrollFlag is f')
				return
			}

			//滚动条距离顶部的值恰好等于list1的高度时，达到滚动临界点，此时将让scrollTop=0,让list1回到初始位置，实现无缝滚动
//				if (box.scrollTop >= l1.offsetHeight) {
//					box.scrollTop = 0
//				} else {
//
//				}
//			// console.log("ref_tableBodyWrapper.scrollTop", ref_tableBodyWrapper.scrollTop, (ref_tableBodyWrapper__table__height/2))

			if (ref_tableBodyWrapper.scrollTop >= (ref_tableBodyWrapper__table__height / 2)) {
				ref_tableBodyWrapper.scrollTop = 0
			} else {
				let st = this.autoScrollStep + ref_tableBodyWrapper.scrollTop
				ref_tableBodyWrapper.scrollTop = st
				this.$emit('on-auto-scroll-step', st)
			}

			clearTimeout(this.doAutoScrollTimer)
			this.doAutoScrollTimer = setTimeout(() => {
				this.doInfiniteMove()
			}, this.autoScrollSpeed)//数值越大，滚动速度越慢

		},

		isIE () {
			return new RegExp(['MSIE ', 'Trident/', 'Edge/'].join('|')).test(navigator.userAgent)
		},

		scrollToTop () {
			if (this.isIE()) {
				this.ref_tableBodyWrapper && (this.ref_tableBodyWrapper.scrollTop = 1)
			} else {
				this.ref_tableBodyWrapper && this.ref_tableBodyWrapper.scroll && this.ref_tableBodyWrapper.scroll({
					top: 1,
					behavior: 'smooth',
				})
			}
		},

		ease (k) {
			return 0.5 * (1 - Math.cos(Math.PI * k))
		},

		step (context) {
			let time = new Date().getTime()
			let value
			let currentX = 0
			let currentY = 0
			let elapsed = (time - context.startTime) / 500

			// avoid elapsed times higher than one
			elapsed = elapsed > 1 ? 1 : elapsed

			// apply easing to elapsed time
			value = this.ease(elapsed)

			// currentX = context.startX + (context.x - context.startX) * value
			currentY = context.startY + (context.y - context.startY) * value

//			context.method.call(context.scrollable, currentX, currentY)

			this.ref_tableBodyWrapper.scrollTop = currentY

			// scroll more if we have not reached our destination
			// currentX !== context.x ||
			if (currentY !== context.y) {
				window.requestAnimationFrame(this.step.bind(window, context))
			}
		},

		handleBodyScroll (e) {
			this.handleBodyScrollLeft(e)
			this.handleBodyScrollTop(e)
		},
		handleWheel (event) {

			// // console.log(`handleWheel`)
			const {scroll = {}} = this
			if (window.navigator.userAgent.match(/Trident\/7\./) && scroll.y) {
				event.preventDefault()
				const wd = event.deltaY
				const target = event.target
				const {
					ref_tableBodyWrapper: ref_tableBodyWrapper,
					ref_freezeColumnsBodyLeft: ref_freezeColumnsBodyLeft,
					ref_freezeColumnsBodyRight: ref_freezeColumnsBodyRight,
				} = this
				let scrollTop = 0

				if (this.lastScrollTop) {
					scrollTop = this.lastScrollTop + wd
				} else {
					scrollTop = wd
				}

				if (ref_freezeColumnsBodyLeft && target !== ref_freezeColumnsBodyLeft) {
					ref_freezeColumnsBodyLeft.scrollTop = scrollTop
				}
				if (ref_freezeColumnsBodyRight && target !== ref_freezeColumnsBodyRight) {
					ref_freezeColumnsBodyRight.scrollTop = scrollTop
				}
				if (ref_tableBodyWrapper && target !== ref_tableBodyWrapper) {
					ref_tableBodyWrapper.scrollTop = scrollTop
				}
			}
		},

		renderTable (h, options) {

			const {
				columns,
				freeze,
				isAnyColumnsFreeze,
			} = options

			const {
				cls,
				scroll = {},
			} = this

			const tableClassName = scroll.x || freeze ? `${cls}-freeze` : ''

			const headTable = h('fb-table-head-wrapper', {
				props: {
					key: 'head',
					columns,
					freeze,
					// handleBodyScrollLeft: this.handleBodyScrollLeft,
				},
			})

			const bodyTable = h('fb-table-body-wrapper', {
				props: {
					key: 'body',
					columns,
					freeze,
					isAnyColumnsFreeze,
//					handleWheel: this.handleWheel,
//					handleBodyScroll: this.handleBodyScroll,
				},
			})

			return [headTable, bodyTable]
		},

		renderMainTable (h) {

			const {
				scroll,
				cls,
			} = this
			const isAnyColumnsFreeze = this.columnManager.isAnyColumnsFreeze()
			const scrollable = isAnyColumnsFreeze || scroll.x || scroll.y

			const table = [
				this.renderTable(h, {
					columns: this.columnManager.groupedColumns(),
					isAnyColumnsFreeze,
				}),
				this.renderNoData(h),
			]

			return scrollable ? h('div', {
				'class': `${cls}-scroll`,
			}, table) : table
		},

		renderLeftTable (h) {
			let {cls} = this
			return h('div', {
				'class': `${cls}-freeze-left`,
			}, this.renderTable(h, {
				columns: this.columnManager.leftColumns(),
				freeze: 'left',
			}))
		},

		renderRightTable (h) {
			let {cls} = this
			return h('div', {
				'class': `${cls}-freeze-right`,
			}, this.renderTable(h, {
				columns: this.columnManager.rightColumns(),
				freeze: 'right',
			}))
		},

		renderNoData (h) {
			const {
				cls,
				caption,
				noEmpty,
				$slots,
				myData,
				noDataText,
				scroll,
				noDataStyle,
			} = this

			if (noEmpty === true) {
				return null
			}

			if (myData.length > 0) {
				return null
			}

			let style = {}

			// scroll.fillY
			if (scroll.fillY) {
				style.top = `${scroll.y / 2 - 16}px`
			}

			return $slots['no-data'] ? $slots['no-data'] : (noDataText ? (
				h('fb-empty', {
					'class': [
						`${cls}-no-data`, {
							[`${cls}-no-data--has-auto-height`]: !!scroll.autoHeight,
							[`${cls}-no-data--fill-y`]: !!scroll.fillY,
						},
					],
					style: {...style, ...noDataStyle},
					props: {
						size: 's',
						text: noDataText,
					},
				}, [])
			) : null)
//			return noDataText || $slots['no-data'] ? (
//				h('fb-empty', {
//					'class': [
//						`${cls}-no-data`, {
//							[`${cls}-no-data--has-auto-height`]: !!scroll.autoHeight,
//						},
//					],
//					props: {
//						size: 's',
//						text: noDataText,
//					},
//				}, [])
//			) : null
		},

		renderCaption (h) {
			const {
				cls,
				caption,
				$slots,
				freezeColumnsBodyRowsHeight,
			} = this

			return caption || $slots.caption ? (
				h('div', {
					'class': `${cls}-caption`,
				}, [caption || $slots.caption])
			) : null
		},

		renderFooter (h) {
			const {
				cls,
				footer,
				$slots,
			} = this

			return footer || $slots.footer ? (
				h('div', {
					'class': `${cls}-footer`,
				}, [footer || $slots.footer])
			) : null
		},

		/**
		 * 渲染分页
		 * @param h
		 * @param position
		 * @returns {null|*}
		 */
		renderPager (h, position = 'bottom') {

			let {
				myPager,
				showPager,
				noPager,
				prefix,
				cls,
				handlePageChange,
				reader,
			} = this

			if (noPager === true) {
				return null
			}

			if (showPager === false) {
				return h('div', {
					'class': [
						`${cls}-pager`,
					],
				})
			}

			if (myPager.position === position || myPager.position === 'both') {

				return h('div', {
					'class': [
						`${cls}-pager`,
					],
				}, [
					h('fb-pager', {
						'class': [
							`${cls}-pager-${position}`,
						],

						props: {
							current: myPager[reader.pagerCurrent],
							size: myPager[reader.pagerSize],
							pages: myPager[reader.pagerPages],
							total: myPager[reader.pagerTotal],
							align: myPager.align,
							simple: myPager.simple,
							showTotalInfo: myPager.showTotalInfo,
							showQuickJumper: myPager.showQuickJumper,
							sizeList: myPager.sizeList,
							showSizeChanger: myPager.showSizeChanger,
							maxLength: myPager.maxLength,
						},
						on: {
							'on-change' (pager) {
								handlePageChange(pager)
							},
						},
					}),
				])
			} else {
				return h('div', {
					'class': [
						`${cls}-pager`,
					],
				})
			}

		},

		handlePageChange (pager) {

			this.$emit('on-pager-change', pager)

			this.myPager = this.mixPager(pager)

			this.fetchData()
		},

		doSearch (param) {
			if (!this.service) {
				console.warn('service属性不存在！！！')
				return
			}

			if (param) {
				this.myQueryParam = assign({}, this.myQueryParam, param)
			}
			// 手动触发查询 分页前往第一页
			this.myPager.current = 1
			this.myPager.page = 1

			this.fetchData()
		},

		doReload () {
			if (!this.service) {
				return
			}

			this.fetchData()
		},

		reload () {
			if (!this.service) {
				return
			}

			this.fetchData()
		},

		fetchData () {

			this.myLoading = true
			if (this.defaultProps.showLoading === false) {
				this.myLoading = false
			}

//			this.loading(true)
//			this.showEmpty = false

			let service = isFunction(this.service) ? this.service : this.service.list

			service(assign({}, this.mySorter, this.myFilter, this.myQueryParam, this.myPager)).then(data => {

				this.$emit('on-data-load', data)
				let result = this.dataParse(data, this)

				if (!this.keepSelected) {
					this.mySelectedPks = []
				}

				this.myPager = this.mixPager(result.pager || {})
				this.myData = this.mixData(result.data || [])

				this.resetScrollX()
				this.myLoading = false
			}).catch(e => {
//				this.$message.error(e)
//				this.loading(false)

				this.myLoading = false
			})

		},

		getSelectedRows () {
			return filter(this.myData, n => this.mySelectedPks.includes(n[this.pk]))
		},

		doSort (sortConfig) {

			let {
				// 当前的排序
				myData,
			} = this

			if (this.mySorter === null) {

				this.unSortDate = toRaw(this.myData)

				this.mySorter = {
					sortByColumn: '',
					sortByDirection: '',
				}
			}

			let toggle = ['desc', 'asc']

			if (sortConfig && sortConfig.directions) {
				toggle = sortConfig.directions
			}

			// 1 检测当前是否处于排序状态
			// 如果不在排序状态，则进入排序状态，并按设定顺序排序(先asc还是先desc)
			if (this.mySorter.sortByColumn === '' || this.mySorter.sortByColumn !== sortConfig.name) {
				// 进入新排序状态

				this.mySorter.sortByColumn = sortConfig.name

				if (toggle.length > 0) {
					this.mySorter.sortByDirection = toggle[0]
				}

			} else {

				// 如果在排序状态，则先判断是否是最后一次排序，如果是，则清空排序，
				// 如果不是，则按顺序排序 (d+1)
				if (toggle.length === 1 || toggle[toggle.length - 1] === this.mySorter.sortByDirection) {
					this.mySorter.sortByColumn = ''
					this.mySorter.sortByDirection = ''
				} else {
					this.mySorter.sortByDirection = toggle [indexOf(toggle, this.mySorter.sortByDirection) + 1]
				}

			}

			console.log(toRaw(this.mySorter))

			if (this.service) {
				this.doSearch()
			}

			if (this.data) {

//				let sortFn = (name) => {
//					return (a, b) => {
//						return a[name] - b[name]
//					}
//				}
//				if (sortConfig && sortConfig.sorter) {
//					sortFn = sortConfig.sorter
//				}

				if (this.mySorter.sortByDirection === 'asc') {
					this.myData = sortByKey(this.myData, sortConfig.name)
				}
				if (this.mySorter.sortByDirection === 'desc') {
					this.myData = sortByKey(this.myData, sortConfig.name).reverse()
				}

				if (this.mySorter.sortByColumn === '') {
					this.myData = toRaw(this.unSortDate)
				}

			}

			function sortByKey (array, key) {
				return array.sort(function (a, b) {
					var x = a[key]
					var y = b[key]
					return ((x < y) ? -1 : ((x > y) ? 1 : 0))
				})
			}

		},

	},

	created () {

		this.refs = []

		this.rawData = []
		this.doAutoScrollTimer = null

		this.setScrollPosition('left')
		this.debouncedWindowResize = debounce(this.handleWindowResize, 150)
		this.debounceSyncFreezeTableRowHeight = debounce(this.syncFreezeTableRowHeight, 150)
		this.debounceUpdateColWidth = this.updateColWidth//debounce(this.updateColWidth, 16)
		this.debounceHandleBodyResize = debounce(this.handleBodyResize, 150)

	},

	mounted () {

		this.hasSubRow = true//!!this.$scopedSlots.subrow

		this.myColumns = this.mixColumns(toRaw(this.columns))
		this.columnManager.reset(this.myColumns)

		this.addRef('tableWrapper', this)

//		this.$on('on-allchecked-change', (value) => {
//
//		})

//		this.$on('on-row-click', (event) => {
//			// console.log(event)
//		})

//		this.$on('on-row-hover', (event, hoverStatus, row, rowIndex) => {
//			// console.log(`on-row-hover `, event, hoverStatus, row, rowIndex)
//		})
//
//		this.$on('on-row-mouseenter', (event, row, rowIndex) => {
////			// console.log(`on-row-mouseenter `, event, row, rowIndex)
//		})
//
//		this.$on('on-row-mouseleave', (event, row, rowIndex) => {
////			// console.log(`on-row-mouseleave `, event, row, rowIndex)
//		})

		//  on-cell-click

		this.$nextTick(() => {
//			if (this.columnManager.isAnyColumnsFreeze()) {

			this.handleWindowResize()
			this.resizeEvent =
				addEventListener(window, 'resize', this.debouncedWindowResize)
//			}
			if (this.ref_tableHeadWrapper) {
				this.ref_tableHeadWrapper.scrollLeft = 0
			}
			if (this.ref_tableBodyWrapper) {
				this.ref_tableBodyWrapper.scrollLeft = 0
			}

			this.myData = this.mixData(toRaw(this.data))

			this.$nextTick(() => {
				if (this.service && this.autoLoad) {
					this.fetchData()
				}
			})

			if (this.autoScroll && this.autoScrollFlag) {
				this.$nextTick(() => {
					setTimeout(() => {
						this.doAutoScroll()
					}, 160)
				})
			}

		})
	},

	beforeUnmount () {
//		// console.log('table beforeUnmount, clear all events')
		if (this.resizeEvent) {
			this.resizeEvent.remove()
		}
		if (this.debouncedWindowResize) {
			this.debouncedWindowResize.cancel()
		}

		if (this.debounceSyncFreezeTableRowHeight) {
			this.debounceSyncFreezeTableRowHeight.cancel()
		}
	},

	destroyed () {
//		// console.log('table destroyed')

		clearTimeout(this.doAutoScrollTimer)

		if (this.resizeEvent) {
			this.resizeEvent.remove()
		}
		if (this.debouncedWindowResize) {
			this.debouncedWindowResize.cancel()
		}

		if (this.debounceSyncFreezeTableRowHeight) {
			this.debounceSyncFreezeTableRowHeight.cancel()
		}
	},

	beforeDestroy () {

		clearTimeout(this.doAutoScrollTimer)

		if (this.resizeEvent) {
			this.resizeEvent.remove()
		}
		if (this.debouncedWindowResize) {
			this.debouncedWindowResize.cancel()
		}

		if (this.debounceHandleBodyResize) {
			this.debounceHandleBodyResize.cancel()
		}

		if (this.debounceSyncFreezeTableRowHeight) {
			this.debounceSyncFreezeTableRowHeight.cancel()
		}

//		if (this.debounceUpdateColWidth) {
//			this.debounceUpdateColWidth.remove()
//		}

		this.refs.forEach((i, name) => {
			this[`ref_${name}`] = null
		})

		this.refs = []

//		this[`ref_${name}`] = component.$refs[name]
//		this.refs.push(`ref_${name}`)
	},

	render (h, context) {

		let {
			columnManager,
			cls,
			scroll,
			scrollPosition,
			caption,
			footer,
			$slots,
			bordered,
			size,
			loading,
			myData,
			headBordered,
			noHeadSplitter,
			longHeadSplitter,
			wrapperStyle,
			autoScrollFlag,
		} = this

		let me = this

		let tableClass = classNames({
			[`${cls}`]: true,
			[`${cls}-freeze-header`]: (scroll && scroll.y),
			[`${cls}-no-head-splitter`]: noHeadSplitter,
			[`${cls}-long-head-splitter`]: longHeadSplitter,

			[`${cls}-bordered`]: bordered,
			[`${cls}-head-bordered`]: headBordered,
			[`${cls}-${size}`]: !!size,
			[`${cls}-empty`]: myData.length === 0,
			[`${cls}-scroll-position-left ${cls}-scroll-position-right`]: scrollPosition === 'both',
			[`${cls}-scroll-position-${scrollPosition}`]: scrollPosition !== 'both',
			[`${cls}-layout-freeze`]: this.isTableLayoutFreeze(),
			[`${cls}-layout-fixed`]: this.fixed,
			[`${cls}-auto-scroll-infinite`]: this.autoScrollInfinite,
		})

		const isAnyColumnsLeftFreeze = columnManager.isAnyColumnsLeftFreeze()
		const isAnyColumnsRightFreeze = columnManager.isAnyColumnsRightFreeze()
		const isAnyColumnsFreeze = columnManager.isAnyColumnsFreeze()

		let table = h('div', {
			'class': tableClass,
			style: {...wrapperStyle},
			ref: 'tableWrapper',

			on: {
				mouseenter (e) {
					me.autoScrollFlag = false
				},
				mouseleave (e) {
					me.autoScrollFlag = true
				},
			},

		}, [

			this.renderCaption(h),

			h('div',
				{
					'class': `${cls}-content`,
				},
				[
					this.renderMainTable(h),
					isAnyColumnsLeftFreeze && this.renderLeftTable(h),
					isAnyColumnsRightFreeze && this.renderRightTable(h),
				],
			),
		])

		let tableBody = [
			table,
			this.renderPager(h, 'bottom'),
		]

		return h('div', {

			'class': `${cls}-wrapper`,

		}, [

//			debug &&
//			h('pre', {}, [
////				`mySelectedPks:\n`,
////				JSON.stringify(this.mySelectedPks, null, '\t'),
////				`\nallchecked:`,
////				JSON.stringify(this.allchecked, null, '\t'),
//				JSON.stringify(this.myPager, null, '\t'),
//			]),

			// h('pre', {}, [
			// 	JSON.stringify(this.mySpansGrid, null, '\t'),
			// ]),
			//
//			h('pre', {}, [
//				JSON.stringify(this.autoScrollFlag, null, '\t'),
//			]),
//
//			h('pre', {}, [
//				JSON.stringify(this.autoScroll, null, '\t'),
//			]),

			h('fb-spin', {

				props: {
					text: this.loadingText,
					show: this.myLoading,
				},

				on: {
					input (event) {
						this.myLoading = event.target.value
						if (this.defaultProps.showLoading === false) {
							this.myLoading = false
						}
					},
				},

			}, tableBody),
		])

	},

}


</script>


<style scoped>

</style>

