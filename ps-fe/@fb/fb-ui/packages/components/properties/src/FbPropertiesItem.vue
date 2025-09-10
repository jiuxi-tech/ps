<script>
/**
 * FbPropertyItem
 * (c) 2020 lincong1987
 */
import { prefix } from '../../../../src/config'
import { closest } from '../../../utils/componentUtils'
import { debounce } from 'lodash-es'

export default {
	name: 'FbPropertiesItem',
	props: {
		size: {
			type: String,
			default: '',
			validate (val) {
				return ['s', 'm', 'l', 'xl'].includes(val)
			},
		},

		type: {
			type: String,
			default: 'label',
		},

		label: {
			type: String,
			default: '',
		},

		value: {
			type: String,
			default: '',
		},

		span: {
			type: [String, Number],
			default: 1,
		},

		colon: {
			type: Boolean,
			default: undefined,
		},

		labelWidth: {
			type: [String, Number],
			default: null,
		},

		labelAlign: {
			type: String,
			default: null,
		},

		contentAlign: {
			type: String,
			default: null,
		},

		ellipsis: {
			type: Boolean,
			default: false,
		},

		verticalAlign: {
			type: String,
			default: null,
		},

		labelStyle: {
			type: [Object],
			default: undefined,
		},



		contentStyle: {
			type: [Object],
			default: undefined,
		},

		rowIndex: {
			type: [String, Number],
			default: undefined,
		},

		colSpan: {
			type: [String, Number],
			default: undefined,
		},

		rowSpan: {
			type: [String, Number],
			default: undefined,
		},

	},

	data () {

		let fbProperty = this.fbProperty || closest(this, 'FbProperties')

		let myColon = false

		if (typeof this.colon === 'undefined') {

			if (fbProperty && typeof fbProperty.myColon !== 'undefined' && fbProperty.myColon === true) {
				myColon = true
			}

		} else if (this.colon === true) {
			myColon = true
		}

		return {
			prefix,

			myColon,

			myLabelAlign: this.labelAlign || (fbProperty && fbProperty.labelAlign),

			myContentAlign: this.contentAlign || (fbProperty && fbProperty.contentAlign),

			myLabelWidth: this.labelWidth || (fbProperty && fbProperty.labelWidth),

			myVerticalAlign: this.verticalAlign || (fbProperty && fbProperty.verticalAlign),

			myEllipsis: this.ellipsis || (fbProperty && fbProperty.ellipsis),

			myLabelStyle: this.labelStyle || (fbProperty && fbProperty.labelStyle),

			myContentStyle: this.contentStyle || (fbProperty && fbProperty.contentStyle),

			// height:   (fbProperty && fbProperty.rowHeight[rowIndex]),

		}
	},

	computed: {
		getClass () {

			let {fbRow} = this

			let arr = [`${this.prefix}-property-item`]

			if (this.myColon) {
				arr.push(`${this.prefix}-property-item--colon`)
			}

			if (this.myLabelAlign) {
				arr.push(`${this.prefix}-property-item--label-${this.myLabelAlign}`)
			}

			if (this.myContentAlign) {
				arr.push(`${this.prefix}-property-item--content-${this.myContentAlign}`)
			}

			if (this.myEllipsis) {
				arr.push(`${this.prefix}-property-item--content--ellipsis`)
			}

			if (this.myVerticalAlign) {
				arr.push(`${this.prefix}-property-item--valign-${this.myVerticalAlign}`)
			}

			if (fbRow && fbRow.rowPk && this.myMode === 'form') {
				arr.push(`${this.prefix}-property-item--row-pk-${fbRow.rowPk}`)
			}

			return arr
		},

		getLabelStyle () {
			let {
				myLabelStyle,
				myLabelWidth,
			} = this
			let style = {}

			if (myLabelWidth) {
				style.width = typeof myLabelWidth === 'number' ? `${myLabelWidth}px` : myLabelWidth
			}
			console.log({...style, ...myLabelStyle})
			return {...style, ...myLabelStyle}
		},

		getContentStyle () {
			let {
				myContentStyle,
				myLabelWidth,
			} = this
			let style = {}
			if (myLabelWidth) {

				let width = typeof myLabelWidth === 'number' ? `${myLabelWidth}px` : myLabelWidth

				style.width = `calc(100% - ${width})`

				//style.marginLeft = width
			}

			return {...style, ...myContentStyle}
		},
	},

	created () {

		this.fbProperty = closest(this, 'FbProperties')

		this.myMode = (this.fbProperty && this.fbProperty.mode) || false

		this.fbRow = this.myMode === 'form' ? closest(this, 'FbRow') : null

	},

	beforeDestroy () {
		this.fbProperty = null
		this.fbRow = null
	},
	mounted () {

	},

	beforeUnmounted () {
	},

	methods: {


	},

	render (h) {

		let {
			prefix,
			myMode,
			$slots,
			getClass,
			span,
			getLabelStyle,
			getContentStyle,
			label,
			value,
			myColon,
			fbProperty,
			rowSpan,
			colSpan
		} = this

		let {uuid} = fbProperty

		if ($slots['label-extra'] && $slots['label-extra'].length > 0) {
			$slots['label-extra'][0].data.class = [`${prefix}-property-item__label__label-extra`]
		}

//		let style = {}

//		if (['form', 'table'].includes(myMode)) {
//			style.height = '100px'
//		}

		let propertyItem = [
			h('span', {
				'class': `${prefix}-property-item__label`,
				style: getLabelStyle,
			}, [

				$slots.label || label ? h('span', {
					'class': [`${prefix}-property-item__label__text`],
				}, [$slots.label || label]) : null,

				myColon && h('span', {
					'class': [`${prefix}-property-item__label__colon`],
				}, ['：']),

				$slots['label-extra'],

//				<slot :class="`${prefix}-form-item__label__extra`" name="label-extra"/>

			]),

			h('span', {
				'class': `${prefix}-property-item__content`,
				style: getContentStyle,
			}, [$slots.default || value]),
		]

		return h(['form', 'table'].includes(myMode) ? 'div' : 'td', {
			'class': getClass,
			attrs: {
				'colspan': colSpan || span,
				'rowspan': rowSpan
			},
//			style,
		}, fbProperty.bordered && !['form'].includes(myMode) ? [
			h('div', {
				'class': `${prefix}-property-item__wrapper`,
				style: {},
			}, propertyItem),
		] : propertyItem)

	},
}
</script>

<style scoped>

</style>
