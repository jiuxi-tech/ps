<!--<template>-->

<!--</template>-->

<script>
import { prefix } from '../../../../src/config'
import { sortBy, isArray, isObject, map, each, debounce } from 'lodash-es'

import { find, filterEmpty, isEmptyElement } from '../../../utils/componentUtils'

import { uid, addClass } from '../../../utils/utils'

export default {
	name: 'FbProperties',

	components: {},

	props: {

		size: {
			type: String,
			default: '',
			validate (val) {
				return ['s', 'm', 'l', 'xl'].includes(val)
			},
		},

		title: {
			type: String,
			default: null,
		},

		split: {
			type: [Number, String],
			default: 2,
		},

		bordered: {
			type: Boolean,
			default: false,
		},

		// 冒号
		colon: {
			type: Boolean,
			default: true,
		},

		value: {
			type: [Array, Object],
			default: null,
		},

		sort: {
			type: String,
			default: 'sort',
		},

		labelWidth: {
			type: [Number, String],
			default: 120,
		},

		labelAlign: {
			type: String,
			default: 'right',
		},

		contentAlign: {
			type: String,
			default: 'left',
		},

		ellipsis: {
			type: Boolean,
			default: false,
		},

		// form
		mode: {
			type: String,
			default: '',
		},

		// top middle bottom
		verticalAlign: {
			type: String,
			default: 'top',
		},

		bodyStyle: {
			type: Object,
			default: undefined,
		},

		labelStyle: {
			type: [Object],
			default: undefined,
		},

		contentStyle: {
			type: [Object],
			default: undefined,
		},

	},
	data () {

		let myColon = true
		if (['form'].includes(this.mode) || this.bordered) {
			myColon = false
		} else {
			//myColon = true
		}

		if (typeof this.colon !== 'undefined') {
			myColon = this.colon
		}

		let pk = uid()

		return {

			prefix,

			pk,

			myColon,

			myValue: this.toCols(this.value),

			rowsHeight: [],

		}
	},

	computed: {
		getClass () {
			let arr = [`${this.prefix}-properties`]

			if (this.bordered) {
				arr.push(`${this.prefix}-properties--bordered`)
			}

			if (this.mode) {
				arr.push(`${this.prefix}-properties--${this.mode}`)
			}

			return arr
		},

	},

	watch: {

		value (value) {
			this.myValue = this.toCols(value)
		},

		colon (value) {
			this.myColon = value
		},

	},

	methods: {
		getLabelClass (propsData) {
			const {prefix, labelAlign, ellipsis, verticalAlign} = this
			const myLabelAlign = propsData.labelAlign || labelAlign
			const myEllipsis = propsData.ellipsis || ellipsis
			const myVerticalAlign = propsData.verticalAlign || verticalAlign

			let arr = [`${prefix}-properties-item`, `${prefix}-properties-item__label`]

			if (myLabelAlign) {
				arr.push(`${prefix}-properties-item--label-${myLabelAlign}`)
			}

			if (myEllipsis) {
				arr.push(`${prefix}-properties-item--content--ellipsis`)
			}

			if (myVerticalAlign) {
				arr.push(`${prefix}-properties-item--valign-${myVerticalAlign}`)
			}

			return arr
		},
		getLabelStyle(propsData) {
			const {labelWidth, labelAlign, labelStyle } = this
			const myLabelWidth = propsData.labelWidth || labelWidth
			let style = {}

			if (myLabelWidth) {
				style.width = typeof myLabelWidth === 'number' ? `${myLabelWidth}px` : myLabelWidth
			}
			if (labelAlign) {
				style.textAlign = labelAlign
				if (labelAlign == 'left') {
					style.justifyContent = 'start'
				} else if (labelAlign == 'right') {
					style.justifyContent = 'end'
				} else if (labelAlign == 'center') {
					style.justifyContent = 'center'
				}
			}
			if (labelStyle) {
				style = Object.assign(style, labelStyle)
			}
			if (propsData.labelStyle) {
				style = Object.assign(style, propsData.labelStyle)
			}
			return style
		},
		getContentClass (propsData) {
			const {prefix, labelAlign, ellipsis, verticalAlign} = this
			const myLabelAlign = propsData.labelAlign || labelAlign
			const myEllipsis = propsData.ellipsis || ellipsis
			const myVerticalAlign = propsData.verticalAlign || verticalAlign

			let arr = [`${prefix}-properties-item`, `${prefix}-properties-item__content`]

			if (myLabelAlign) {
				arr.push(`${prefix}-properties-item--content-${myLabelAlign}`)
			}

			if (myEllipsis) {
				arr.push(`${prefix}-properties-item--content--ellipsis`)
			}

			if (myVerticalAlign) {
				arr.push(`${prefix}-properties-item--valign-${myVerticalAlign}`)
			}

			return arr
		},
		getContentStyle(propsData) {
			const {contentAlign, contentStyle, split, labelWidth } = this
			const num = (propsData.span / split) * 100
			let style = {
				width: `calc(${num}% - ${labelWidth}px)`
			}
			if (contentAlign) {
				style.textAlign = contentAlign

				if (contentAlign == 'left') {
					style.justifyContent = 'start'
				} else if (contentAlign == 'right') {
					style.justifyContent = 'end'
				} else if (contentAlign == 'center') {
					style.justifyContent = 'center'
				}
			}

			if (contentStyle) {
				style = Object.assign(style, contentStyle)
			}
			if (propsData.contentStyle) {
				style = Object.assign(style, propsData.contentStyle)
			}
			return style
		},
		hasColon (propsData) {
			const {colon} = this
			const myColon = propsData.colon || colon

			return myColon
		},
		toCols (value) {

			if (!value) {
				return null
			}

			if (isArray(value)) {
				return sortBy(value, [this.sort])
			}

			let cols = null

			if (isObject(value)) {
				cols = []

				each(this.value, (v, i) => {
					cols.push(v)
				})

				cols = sortBy(cols, [this.sort])
			}

			return cols
		},
		renderRows(h) {
			const {
				span,
				split,
				getLabelStyle,
				colon
			} = this

			const splitNum = Number(split)
			let rows = [h('tr', {'class': `${this.prefix}-properties__row`}, [])]
			let rowIndex = 0

			if (this.$slots.default) {
				let stepNum = 0
				// rows = []

				let items = this.$slots.default.filter((vnode, i) => {
					const propsData = vnode.componentOptions.propsData
					if (!propsData.span) {
						propsData.span = 1
					}
					vnode.rowIndex = rowIndex
					stepNum += Number(propsData.span)

					if (stepNum%splitNum === 0) {
						rows.push( h('tr', {'class': `${this.prefix}-properties__row`}, []) )
						rowIndex += 1
					}

					return vnode.tag && vnode.componentOptions.tag === 'fb-properties-item'
				})


				items.forEach((item, i) => {
					const propsData = item.componentOptions.propsData
					const children = item.componentOptions.children
					const Ctor = item.componentOptions.Ctor

					// if (
					// 	item.isRow
					// 	// ((i) % splitNum) === 0
					// ) {
					// 	rowIndex = rowIndex + 1
					// 	rows[rowIndex] = h('tr', {'class': `${this.prefix}-properties__row`}, [])
					// }

					let label = []
					let content = children

					if (children && children.length > 0) {
						label = children.filter(child => child.data && child.data.slot && child.data.slot === 'label')
						content = children.filter(child => !child.data || !child.data.slot)

						if (label.length === 0) {
							label.push(propsData.label)
						}
						if (this.hasColon(propsData)) {
							label.push(h('span', {
								'class': [`${prefix}-properties-item__label__colon`],
							}, ['：']))
						}
					}

					rows[item.rowIndex].children.push(h('td', {
						'class': [this.getLabelClass(propsData)],
						style: this.getLabelStyle(propsData)
					}, label))
					rows[item.rowIndex].children.push(
						h('td',
							{
								class: [this.getContentClass(propsData)],
								style: this.getContentStyle(propsData),
								attrs: {
									colspan: propsData.span > 1 ? 2 * propsData.span - 1 : 1
								}
							},
						content)
					)
				})

			}

			return rows
		}

	},

	created () {

	},

	beforeDestroy () {


	},

	mounted () {

	},

	render: function (h) {


		return h('div', {
			'class': this.getClass,
			style: {},
		}, [

			// header
			this.$slots.header || this.$slots.title || this.$slots.actions || this.title
				? h('div', {'class': `${this.prefix}-properties__header`}, (() => {
					if (this.$slots.header) {
						return this.$slots.header
					} else {
						return [
							this.$slots.title || this.title ? h('div', {'class': `${this.prefix}-properties__title`},
								this.$slots.title || this.title) : null,
							this.$slots.actions ? h('div', {'class': `${this.prefix}-properties__actions`}) : null,
						]
					}
				})())
				: null,

			// body
			h('div', {
				'class': `${this.prefix}-properties__body`,
				style: this.bodyStyle,
				ref: this.bordered ? 'ref_property_body' : null,
			}, [
				['form'].includes(this.mode)
					? this.renderRows(h)
					: h('table', {'class': `${this.prefix}-properties__table`}, [this.renderRows(h)]),
			]),

			// footer
			this.$slots.footer
				? h('div', {'class': `${this.prefix}-properties__footer`}, this.$slots.footer)
				: null,

		])
	},

}
</script>

<style scoped>

</style>
