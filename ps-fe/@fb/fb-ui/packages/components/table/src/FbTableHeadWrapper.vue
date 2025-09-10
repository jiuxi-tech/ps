<script>
import { closest } from '../../../utils/componentUtils'
import { prefix } from '../../../../src/config'
import { classNames, measureScrollbar } from './utils'
import FbBaseTable from './FbBaseTable'
import { debounce } from 'lodash-es'

/**
 * FbTableHeadWrapper
 * (c) 2020 lincong1987
 */
export default {
	name: 'FbTableHeadWrapper',
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

//		handleBodyScrollLeft: {
//			type: Function,
//			default () {
//				return null
//			},
//		},

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

		let {useFreezeHeader} = this.table

		return {
			cls: `${prefix}-simple-table`,
			prefix,
		}
	},

	computed: {

//		scrollbarWidth () {
//			return measureScrollbar({
//				direction: 'vertical',
//				prefix: this.prefix,
//			})
//		},
//
//		getClass () {
//			let arr = [`${this.cls}-header`]
//
//			if (this.scrollbarWidth > 0) {
//				arr.push(`${this.cls}-hide-scrollbar`)
//				return arr
//			}
//
//		},

//		getStyle () {
//			let style = {}
//			let {scrollbarWidth} = this
//			let {
//				scroll,
//				useFixedHeader,
//			} = this.table
//			if (scroll.y) {
//				useFixedHeader = true
//				const scrollbarWidthOfHeader = measureScrollbar({
//					direction: 'horizontal',
//					prefix: this.prefix,
//				})
//				if (scrollbarWidthOfHeader > 0 && !this.freeze) {
//					style.marginBottom = `-${scrollbarWidthOfHeader}px`
//					style.paddingBottom = '0px'
//					style.minWidth = `${scrollbarWidth}px`
//					style.overflowX = 'scroll'
//					style.overflowY = scrollbarWidth === 0 ? 'hidden' : 'scroll'
//				}
//			}
//
//			return style
//		},
	},

	methods: {

//		handleBodyScrollLeft () {
//		},

		updateTableHeadWrapperHeight () {
			this.tableHeadWrapper &&
			(this.table.tableHeadWrapperHeight = window.getComputedStyle(this.tableHeadWrapper, null).height)
			//(this.table.tableHeadWrapperHeight = this.tableHeadWrapper.getBoundingClientRect().height)
		},
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
		this.table.addRef('tableHeadWrapper', this)
//		this.$nextTick(() => {
//			this.debounceUpdateTableHeadWrapperHeight()
//		})
	},

	render (h) {

		const {
			cls,
			prefix,
			columns,
			freeze,
			hasSubRow,
			table,
			addRef,
		} = this
		const {
			scroll,
			showHeader,
			handleBodyScrollLeft,
			autoScrollInfinite,
		} = table
		let {useFreezeHeader} = table
		const headStyle = {}

		const scrollbarWidth = measureScrollbar({direction: 'vertical'})

		if (scroll.y) {
			useFreezeHeader = true
			const scrollbarWidthOfHeader = measureScrollbar({
				direction: 'horizontal',
				prefix,
			})
			if (scrollbarWidthOfHeader > 0 && !freeze) {
				headStyle.marginBottom = `-${scrollbarWidthOfHeader}px`
				headStyle.paddingBottom = '0px'
				headStyle.minWidth = `${scrollbarWidth}px`
				headStyle.overflowX = 'scroll'
				headStyle.overflowY = scrollbarWidth === 0 ? 'hidden' : 'scroll'
			}

			if (autoScrollInfinite) {
				headStyle.overflowY = 'hidden'
			}
		}

		if (!useFreezeHeader || !showHeader) {
			return null
		}

		return h('div', {
			ref: freeze ? null : 'tableHeadWrapper',
			'class': classNames([
				`${cls}-header`, {
					[`${cls}-hide-scrollbar`]: scrollbarWidth > 0,
				},
			]),
			style: headStyle,
			on: {
				scroll: handleBodyScrollLeft,
			},
		}, [
			h('fb-base-table', {
				props: {
					hasHead: true,
					freeze,
					columns,
					hasSubRow,
				},
			}),
		])

	},

}
</script>

<style scoped>

</style>
