<script>

import { prefix } from '../../../../src/config'
import { closest } from '../../../utils/componentUtils'
import { includes, isNumber, isBoolean, isString } from 'lodash-es'

export default {

	render (h, context) {
		return h('div', {

			class: this.getClass,
			//style: 'border: 1px solid blue; font-size: 12px',
			on: {
				click: (e) => {
					e.stopPropagation()
					this.handleClick(e)
				},
			},

		}, [

			this.$scopedSlots.checkbox
				? h('span', {
					//class: this.getElClass,
					//style: 'border: 1px solid red; font-size: 12px',
				}, [
					this.$scopedSlots.checkbox({
						name: 'checkbox',
						label: this.label,
						value: this.value,
						disabled: this.disabled,
						readonly: this.readonly,
						checked: this.checked,
						index: this.index,
						node: this.node,
					}),
				])
				: h('span', {
					class: this.getElClass,
					//style: 'border: 1px solid red; font-size: 12px',
				}, [
					h('span', {
						class: `${this.prefix}-checkbox__el__inner`,
					}),
				]),

			h('span', {
				class: `${this.prefix}-checkbox__content`,
				//style: 'border: 1px solid green; font-size: 12px',
			}, [
				this.$scopedSlots.label ? this.$scopedSlots.label({
						name: 'label',
						label: this.label,
						value: this.value,
						disabled: this.disabled,
						readonly: this.readonly,
						checked: this.checked,
						index: this.index,
						node: this.node,
					}) :
					(this.$slots.default ? this.$slots.default : this.label),
			]),

		])
	},

	name: 'FbCheckbox',
	props: {
		//  true false "indeterminate"
		// indeterminate 半选
		value: {
			type: [String, Number, Boolean],
			default: false,
		},

		label: {
			type: [String, Number, Boolean],
			default: '',
		},
		disabled: {
			type: Boolean,
			default: false,
		},
		readonly: {
			type: Boolean,
			default: false,
		},
		node: {
			type: [Object],
			default: null,
		},

		index: {
			type: [Number],
			default: -1,
		},

		size: {
			type: String,
			default: 'm',
		},

	},
	data () {
		return {
			prefix,
			name: '',
			checked: this.fbCheckboxGroup ? false : this.value,
			group: false,
		}
	},
	created () {
		this.fbCheckboxGroup = closest(this, 'FbCheckboxGroup')
	},
	beforeDestroy () {
		if (this.fbCheckboxGroup) {
			this.fbCheckboxGroup.removeItem(this)
		}
		this.fbCheckboxGroup = null
	},
	computed: {
		getClass () {
			let arr = [`${this.prefix}-checkbox`]

			// size
			if (this.size) {
				arr.push(`${this.prefix}-checkbox--${this.size}`)
			}

			if (this.readonly || (this.fbCheckboxGroup && this.fbCheckboxGroup.readonly)) {
				arr.push(`${this.prefix}-checkbox--readonly`)
			}
			if (this.disabled || (this.fbCheckboxGroup && this.fbCheckboxGroup.disabled)) {
				arr.push(`${this.prefix}-checkbox--disabled`)
			}

			if (isString(this.checked)) {
				arr.push(`${this.prefix}-checkbox--${this.checked}`)
			}
			if (isNumber(this.checked)) {
				arr.push(`${this.prefix}-checkbox--${this.checked}`)
			}
			if (isBoolean(this.checked) && this.checked === true) {
				arr.push(`${this.prefix}-checkbox--checked`)
			}

			return arr
		},

		getElClass () {
			let arr = [`${this.prefix}-checkbox__el`]

			if (isString(this.checked)) {
				arr.push(`${this.prefix}-checkbox__el--${this.checked}`)
			}
			if (isNumber(this.checked)) {
				arr.push(`${this.prefix}-checkbox__el--${this.checked}`)
			}
			if (isBoolean(this.checked) && this.checked === true) {
				arr.push(`${this.prefix}-checkbox__el--checked`)
			}
			return arr
		},
	},

	watch: {
		value (value) {
			if (!this.group && this.checked !== value) {
				this.checked = value
			}
		},
	},

	methods: {
		handleClick (e) {
			// 当禁用只读时，不响应点击
			if (this.disabled || this.readonly) {
				return false
			}

			if (this.fbCheckboxGroup) {
				if (this.fbCheckboxGroup.disabled || this.fbCheckboxGroup.readonly) {
					return false
				}
			}

			let checked = this.checked = (this.checked === 'indeterminate' || this.checked === false)

			if (this.group) {
				this.fbCheckboxGroup.change(this.value, checked, this.node, e)
			} else {

				this.$emit('on-click', checked, e, this.node || this)

				if (!e.defaultPrevented) {
					this.$emit('input', checked, this.node || this, e)
					this.$emit('change', checked, this.node || this, e)
					this.$emit('on-change', checked, this.node || this, e)
				}
			}
		},

	},
	mounted () {

		if (this.fbCheckboxGroup) {
			if (includes(this.fbCheckboxGroup.currentValue, this.value)) {
				this.checked = true
			} else {
				this.checked = false
			}

			this.fbCheckboxGroup.addItem(this)
		}

		// console.log(this.$attrs, this.$props)
		//
		// this.options.group = this.$parent.$options.name === 'FbCheckboxGroup'
		// this.options.checked = this.checked
		// this.options.readonly = this.readonly
		// this.options.border = this.border

	},

//	beforeDestroy () {
//
//	},
}
</script>

