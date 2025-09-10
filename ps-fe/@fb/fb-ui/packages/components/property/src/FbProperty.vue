<!--<template>-->
<!--	<div :class="getClass">-->

<!--		split: {{ split }}-->

<!--		<div :class="`${prefix}-property__header`">-->
<!--			<template v-if="$slots.header">-->
<!--				<slot name="header"></slot>-->
<!--			</template>-->
<!--			<template v-else>-->
<!--				<span :class="`${prefix}-property__title`">-->
<!--					<slot name="title">{{ title }}</slot>-->
<!--				</span>-->
<!--				<div :class="`${prefix}-property__actions`" v-if="$slots.actions">-->
<!--					<slot name="actions"></slot>-->
<!--				</div>-->
<!--			</template>-->
<!--		</div>-->

<!--		<div :class="`${prefix}-property__body`">-->
<!--&lt;!&ndash;			<table :class="`${prefix}-property__table`">&ndash;&gt;-->
<!--&lt;!&ndash;				<tbody>&ndash;&gt;-->
<!--&lt;!&ndash;					<tr v-for="(row, index) in $slots">&ndash;&gt;-->
<!--&lt;!&ndash;						{{index}}&ndash;&gt;-->
<!--&lt;!&ndash;					</tr>&ndash;&gt;-->
<!--&lt;!&ndash;				</tbody>&ndash;&gt;-->
<!--&lt;!&ndash;			</table>&ndash;&gt;-->


<!--		</div>-->

<!--		<div :class="`${prefix}-property__footer`">-->

<!--		</div>-->

<!--	</div>-->
<!--</template>-->

<script>
/**
 * FbProperty
 * (c) 2020 lincong1987
 */
import { prefix } from '../../../../src/config'
import FbPropertyItem from './FbPropertyItem'
import { sortBy, isArray, isObject, map, each, debounce } from 'lodash-es'

import { find, filterEmpty, isEmptyElement } from '../../../utils/componentUtils'

import { uid, addClass } from '../../../utils/utils'

export default {
	name: 'FbProperty',

	components: {
		FbPropertyItem,
	},

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

		// 是否上下显示
		vertical: {
			type: Boolean,
			default: false,
		},

		// 冒号
		colon: {
			type: Boolean,
			default: undefined,
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
			let arr = [`${this.prefix}-property`]

			if (this.bordered) {
				arr.push(`${this.prefix}-property--bordered`)
			}

			if (this.mode) {
				arr.push(`${this.prefix}-property--${this.mode}`)
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

		syncItemHeight () {
			console.log('syncItemHeight')

			let {
				$refs,
				prefix,
			} = this

			if (!$refs.ref_property_body) {
				return
			}

			if (['form'].includes(this.mode)) {
				let rows = $refs.ref_property_body.querySelectorAll(`.${prefix}-row`) ||
					[]

				rows.forEach((row, index) => {
					const rowPk = row.getAttribute('data-pk')
					const items = row.querySelectorAll(`.${prefix}-property-item--row-pk-${rowPk}`) || []

					items.forEach((n, i) => {
						n.style.height = null
					})

					this.$nextTick(() => {

						let style = window.getComputedStyle(row, null)
						//const height = row.getBoundingClientRect().height || 'auto'
						const height = style.height || 'auto'
						items.forEach((n, i) => {
							n.style.height = typeof height === 'number' ? `${height}px` : height
						})
					})

				})
			} else {

				let rows = $refs.ref_property_body.querySelectorAll(`.${prefix}-property__row`) ||
					[]

				rows.forEach((row, index) => {
					const items = row.querySelectorAll(`.${prefix}-property-item__label`) || []

					items.forEach((n, i) => {
						n.style.height = null
					})

					this.$nextTick(() => {
						let style = window.getComputedStyle(row, null)
						//const height = row.getBoundingClientRect().height || 'auto'
						const height = style.height || 'auto'
						items.forEach((n, i) => {
							n.style.height = typeof height === 'number' ? `${height}px` : height
						})
					})

				})

			}

		},

		handleWindowResize () {
			this.debounceSyncItemHeight()
		},

		sync () {
			this.debounceSyncItemHeight()
//			clearTimeout(this.timer)
//			this.timer = setTimeout(() => {
//				this.sync()
//				 console.log('sync')
//			}, 1600)
		},

	},

	updated () {
		this.debounceSyncItemHeight()
	},
	created () {
		this.timer = null
	//	this.debounceSyncItemHeight = this.syncItemHeight// debounce(this.syncItemHeight, 200)
		this.debounceSyncItemHeight =  debounce(this.syncItemHeight, 200)
//		this.debounceSyncItemHeight = debounceSyncItemHeight
	},

	beforeDestroy () {
		window.removeEventListener('resize', this.debounceSyncItemHeight, false)
		clearTimeout(this.timer)

		// console.log('beforeDestroy clearTimeout sync')
		//this.debounceSyncItemHeight.cancel()

	},

//	destroyed () {
//		document.removeEventListener('resize', this.debounceSyncItemHeight)
//		clearTimeout(this.timer)
//	},
//
//	beforeMount () {
//		document.removeEventListener('resize', this.debounceSyncItemHeight)
//		clearTimeout(this.timer)
//	},

	mounted () {

		window.addEventListener('resize', this.debounceSyncItemHeight, false)

		this.$slots && filterEmpty(this.$slots.default).map((slot, i) => {
			if (slot && slot.componentOptions && slot.componentOptions.tag === 'fb-row') {
				if (i > 0) {
					let fieldset = find(slot.componentInstance, 'FbFieldset')

					if (fieldset && slot.elm) {
						addClass(slot.elm, `${this.prefix}-row--property-fieldset`)
					}
				}

			}

		})

		this.$nextTick(() => {
			this.sync()
		})
	},

//	beforeUnmount () {
//		window.removeEventListener('resize', this.debounceSyncItemHeight, true)
//	},

	render: function (h) {

		let rows = [h('tr', {'class': `${this.prefix}-property__row`}, [])]
		let rowIndex = 0

		if (this.myValue) {
			each(this.myValue, (row, i) => {
				if (((i) % this.split) === 0) {
					rowIndex += 1
					rows[rowIndex] = h('tr', {'class': `${this.prefix}-property__row`}, [])
				}

				//row.colon = this.colon
				//row.labelWidth = this.labelWidth
				rows[rowIndex].children.push(h('fb-property-item', {
					props: row,
				}, []))
			})
		} else if (['form'].includes(this.mode)) {

			this.$slots && filterEmpty(this.$slots.default).map((slot, i) => {
				if (slot && slot.componentOptions && slot.componentOptions.tag === 'fb-row') {
					let rowPk = uid()
					slot.componentOptions.propsData.rowPk = rowPk
					this.rowsHeight[rowPk] = 'auto'

				}

			})

			rows = [this.$slots.default]

		} else {

			let slots = []
			let fieldsets = []

			this.$slots && filterEmpty(this.$slots.default).map((slot, i) => {

				if (slot && slot.componentOptions && slot.componentOptions.tag === 'fb-fieldset') {
					// slots.push(slot)
					fieldsets.push({
						slot,
						i,
					})
				} else {
					slots.push(slot)
				}
				//slots.push(slot)

				if (slot && slot.componentOptions && slot.componentOptions.tag === 'fb-property-item'
					&& (slot.componentOptions.propsData.span && slot.componentOptions.propsData.span > 1)) {

					for (let i = 2; i <= slot.componentOptions.propsData.span; i++) {
						slots.push(h(''))
					}
				}
			})

			rows = []

			slots.map((slot, i) => {
				if (((i) % this.split) === 0) {
					rowIndex = rowIndex + 1
					rows[rowIndex] = h('tr', {'class': `${this.prefix}-property__row`}, [])
				}

				if (slot.componentOptions && slot.componentOptions.tag === 'fb-fieldset') {
					rowIndex = rowIndex + 1
					rows[rowIndex] = h('tr', {'class': `${this.prefix}-property__row`}, [])
				}

				rows[rowIndex].children.push(slot)
			})
		}

		return h('div', {
			'class': this.getClass,
			style: {},
		}, [

			// header
			this.$slots.header || this.$slots.title || this.$slots.actions || this.title
				? h('div', {'class': `${this.prefix}-property__header`}, (() => {
					if (this.$slots.header) {
						return this.$slots.header
					} else {
						return [
							this.$slots.title || this.title ? h('div', {'class': `${this.prefix}-property__title`},
								this.$slots.title || this.title) : null,
							this.$slots.actions ? h('div', {'class': `${this.prefix}-property__actions`}) : null,
						]
					}
				})())
				: null,

			// body
			h('div', {
				'class': `${this.prefix}-property__body`,
				style: this.bodyStyle,
				ref: this.bordered ? 'ref_property_body' : null,
			}, [
				['form'].includes(this.mode)
					? rows
					: h('table', {'class': `${this.prefix}-property__table`}, [rows]),
			]),

			// footer
			this.$slots.footer
				? h('div', {'class': `${this.prefix}-property__footer`}, this.$slots.footer)
				: null,

		])
	},

}
</script>

<style scoped>

</style>
