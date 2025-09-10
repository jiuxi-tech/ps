<script>

import { prefix } from '../../../../src/config'
import {
	isFunction,
	assign,
	filter,
	isArray,
} from 'lodash-es'
import FbPager from '../../pager/src/FbPager'
import FbSpin from '../../spin/src/FbSpin'
import FbEmpty from '../../empty/src/FbEmpty'

function toRaw (obj) {
	return JSON.parse(JSON.stringify(obj))
}

let uidIndex = 0

function uid (prefix = 'uuid') {
	let now = +new Date()
	return `${prefix}-${now}-${++uidIndex}`
}

export default {
	name: 'FbPagerModel',

	components: {
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

			currentHoverRowPk: null,
			currentHoverSubRowPk: null,

			freezeColumnsHeadRowsHeight: 0,

			freezeColumnsBodyRowsHeight: {},
			subRowsHeight: {},

			subRowsActivated: {},

			myData: [],//this.mixData(toRaw(this.data)),

			myPager: this.mixPager(this.pager),

			mySorter: null,

			myReader: this.mixReader(this.reader),

			myFilter: null,

			myLoading: this.loading,

			myQueryParam: this.param,

			mySelectedPks: this.value,

			myOtherHeight: 0,

			rowSpanGrid: {},

			tableHeadWrapperHeight: 40,

		}
	},

	computed: {

//		watchData () {
//			return toRaw(this.data)
//		},

	},

	watch: {

		watchData (value, old) {

			if (!this.keepSelected) {
				this.mySelectedPks = []
			}

			let data = value
			if (typeof value === 'function') {
				data = value()
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

		param (value) {

			if (typeof value === 'function') {
				value = value()
			}

			this.myQueryParam = value
			if (this.autoQuery) {
				this.doSearch()
			}
		},

	},

	methods: {

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

			return value
		},

		addRef (name, component) {
			if (component && component.$refs && component.$refs[name]) {
				this[`ref_${name}`] = component.$refs[name]
				this.refs.push(`ref_${name}`)
				// // console.log(`添加引用 ref_${name}`)
			}
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
				return
			}

			if (param) {
				this.myQueryParam = assign({}, this.myQueryParam, param)
			}

			this.myPager.current = 1

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
				this.myLoading = false
			})

		},

		getSelectedRows () {
			return filter(this.myData, n => this.mySelectedPks.includes(n[this.pk]))
		},

	},

	created () {

		this.refs = []

		this.rawData = []

	},

	mounted () {

		this.$nextTick(() => {

			this.myData = this.mixData(toRaw(this.data))

			this.$nextTick(() => {
				if (this.service && this.autoLoad) {
					this.fetchData()
				}
			})

		})
	},

	beforeUnmount () {
	},

	destroyed () {
	},

	beforeDestroy () {

	},

	render (h, context) {

		let {
			cls,
			$slots,
			loading,
			myData,
			wrapperStyle,
			contentStyle,
		} = this

		let me = this

		let tableClass = {
			[`${cls}`]: true,
		}

		let table = h('div', {
			'class': tableClass,
			style: {...wrapperStyle},
			ref: 'tableWrapper',
		}, [

			h('div',
				{
					'class': `${cls}-content`,
					style: {
						display: 'flex',
						'flex-wrap': 'wrap', ...contentStyle,
					},
				},

				myData.map((item, i) => {
					if (this.$scopedSlots.item) {
						return this.$scopedSlots.item({
							item,
						})
					}
				}),
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

			}, myData.length > 0 ? tableBody : [this.renderNoData(h)]),
		])

	},

}


</script>


<style scoped>

</style>

