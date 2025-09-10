<script>
import { closest } from '../../../utils/componentUtils'
import { prefix } from '../../../../src/config'
import { classNames, measureScrollbar } from './utils'
import FbBaseTable from './FbBaseTable'

/**
 * FbTableBodyWrapper
 * (c) 2020 lincong1987
 */
export default {
	name: 'FbTableBodyWrapper',
	components: {FbBaseTable},
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

		handleBodyScroll: {
			type: Function,
			default: () => {
			},
		},

		handleWheel: {
			type: Function,
			default: () => {
			},
		},

		hasSubRow: {
			type: [Boolean],
			default: false,
		},

		isAnyColumnsFreeze: {
			type: [Boolean],
			default: false,
		},
	},
	data () {

		return {

			cls: `${prefix}-simple-table`,
			prefix,
			refName: '',
		}
	},

	computed: {

		scrollbarWidth () {
			return measureScrollbar({
				direction: 'vertical',
				prefix: this.prefix,
			})
		},

		getClass () {
			let arr = [`${this.cls}-body`]

			const headStyle = {}

			if (this.scrollbarWidth > 0) {
				arr.push(`${this.cls}-hide-scrollbar`)
				return arr
			}

		},

		getStyle () {
			let style = {}
			let {scrollbarWidth} = this
			let {
				scroll,
				useFixedHeader,
				autoScrollInfinite,
			} = this.table
			if (scroll.y) {
				useFixedHeader = true
				const scrollbarWidthOfHeader = measureScrollbar({
					direction: 'horizontal',
					prefix: this.prefix,
				})
				if (scrollbarWidthOfHeader > 0 && !this.freeze) {
					style.marginBottom = `-${scrollbarWidthOfHeader}px`
					style.paddingBottom = '0px'
					style.minWidth = `${scrollbarWidth}px`
					style.overflowX = 'scroll'
					style.overflowY = scrollbarWidth === 0 ? 'hidden' : 'scroll'
				}
				if (autoScrollInfinite) {
					style.overflowY = 'hidden'
				}
			}

			return style
		},
	},

	methods: {},

	created () {
//		this.table = closest(this, 'FbSimpleTable')
	},
	inject: ['table'],
	beforeDestroy () {
//		this.table = null
	},

	mounted () {
//		this.table.addRef('fixedColumnsBodyLeft', this)
		this.table.addRef(this.refName, this)
		this.table.addRef('tableBodyWrapper', this)
	},

	render (h) {

		let {
			freeze,
			columns,
			table,
			cls,
			hasSubRow,
			isAnyColumnsFreeze,

		} = this

		let {
			scroll,
			useFixedHeader,
			addRef,
			handleWheel,
			handleBodyScroll,
			otherHeightProvider,
			myOtherHeight,
			autoScrollInfinite,
		} = table

		const bodyStyle = {...table.bodyStyle}
		const innerBodyStyle = {}

		if (scroll.x || freeze) {
			bodyStyle.overflowX = bodyStyle.overflowX || 'scroll'
			bodyStyle.WebkitTransform = 'translate3d (0, 0, 0)'
		}

		if (scroll.y) {
			let maxHeight = bodyStyle.maxHeight || scroll.y
			maxHeight = typeof maxHeight === 'number' ? `${maxHeight}px` : maxHeight
			if (freeze) {
				innerBodyStyle.maxHeight = maxHeight
				innerBodyStyle.overflowY = bodyStyle.overflowY || 'scroll'
			} else {
				bodyStyle.maxHeight = maxHeight
			}
			bodyStyle.overflowY = bodyStyle.overflowY || 'scroll'
			useFixedHeader = true

			if (autoScrollInfinite) {
				bodyStyle.overflowY = 'hidden'
			}

			const scrollbarWidth = measureScrollbar({direction: 'vertical'})
			if (scrollbarWidth > 0 && freeze) {
				bodyStyle.marginBottom = `-${scrollbarWidth}px`
				bodyStyle.paddingBottom = '0px'
			}

			if (scroll.autoHeight) {

				if (freeze) {
					delete innerBodyStyle.maxHeight
				} else {
					delete bodyStyle.maxHeight
				}

				let oh

				switch (typeof scroll.autoHeight) {
					case 'function':
						oh = scroll.autoHeight()
						break
					case 'number':
						oh = scroll.autoHeight
						break
					case 'boolean':
						oh = scroll.y
				}

				let maxHeight = myOtherHeight - oh

				maxHeight = typeof maxHeight === 'number' ? `${maxHeight}px` : maxHeight

				if (freeze) {
					innerBodyStyle.maxHeight = maxHeight
					innerBodyStyle.minHeight = maxHeight
				} else {
					bodyStyle.maxHeight = maxHeight
					bodyStyle.minHeight = maxHeight
				}
			}

			if (scroll.fillY) {
				if (scroll.y) {
					let maxHeight = typeof scroll.y === 'number' ? `${scroll.y}px` : scroll.y
					if (freeze) {
						innerBodyStyle.maxHeight = maxHeight
						innerBodyStyle.minHeight = maxHeight
					} else {
						bodyStyle.maxHeight = maxHeight
						bodyStyle.minHeight = maxHeight
					}
				}
			}
		}

		let baseTable = h('fb-base-table', {
			attrs: {},
			props: {
				hasHead: !useFixedHeader,
				hasBody: true,
				freeze,
				columns,
				hasSubRow,
				isAnyColumnsFreeze,
			},
		}, [])

		if (freeze && columns.length) {

			let refName
			if (columns[0].freeze === 'left' || columns[0].freeze === true) {
				refName = 'freezeColumnsBodyLeft'
			} else if (columns[0].freeze === 'right') {
				refName = 'freezeColumnsBodyRight'
			}

			this.refName = refName

			delete bodyStyle.overflowX
			delete bodyStyle.overflowY

			return h('div', {
				'class': classNames([`${cls}-body-outer`]),
				style: bodyStyle,
				attrs: {},
			}, [
				h('div', {
					'class': classNames([`${cls}-body-inner`]),
					style: innerBodyStyle,
					ref: refName,
					on: {
						wheel: handleWheel,
						scroll: handleBodyScroll,
					},
				}, [
					baseTable,
				]),
			])

		}

		const useTabIndex = scroll && (scroll.x || scroll.y)

		return h('div', {
			'class': classNames([`${cls}-body`]),
			style: bodyStyle,
			ref: 'tableBodyWrapper',
			attrs: {
				tabindex: useTabIndex ? -1 : undefined,
			},
			on: {
				//wheel: handleWheel,
				scroll: handleBodyScroll,
			},
		}, [
			baseTable,
		])

	},

}
</script>

<style scoped>

</style>
