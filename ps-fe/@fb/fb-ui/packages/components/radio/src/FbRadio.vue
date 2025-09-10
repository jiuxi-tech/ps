<template>
	<div :class="getClass"
		 @click="handleClick"
	>
        <span :class="getElClass">
            <span :class="`${prefix}-radio__el__inner`"/>
        </span>

		<span v-if="label || $slots.label" :class="`${prefix}-radio__content`"><slot name="label">{{
				label
			}}</slot></span>

	</div>
</template>

<script>
import { closest } from '../../../utils/componentUtils'
import { includes, isNumber, isBoolean, isString } from 'lodash-es'

import { prefix } from '../../../../src/config'

export default {
	name: 'FbRadio',
	props: {
		// true false  半选 "indeterminate"
		value: {
			type: [String, Number, Boolean],
			default: false,
		},

		// 标签
		label: {
			type: [String, Number, Boolean],
			default: '',
		},
		// 禁用
		disabled: {
			type: Boolean,
			default: false,
		},
		// 只读
		readonly: {
			type: Boolean,
			default: false,
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
			checked: false,
			myValue: this.value,
			group: false,
		}
	},

	created () {
		this.fbRadioGroup = closest(this, 'FbRadioGroup')
	},
	beforeDestroy () {
		this.fbRadioGroup && this.fbRadioGroup.removeItem(this)

		this.fbRadioGroup = null
	},
	computed: {
		getClass () {
			let arr = [`${prefix}-radio`]

			if (this.size) {
				arr.push(`${prefix}-radio--${this.size}`)
			}

			if (isBoolean(this.checked) && this.checked === true) {
				arr.push(`${prefix}-radio--checked`)
			}

			if (this.readonly || (this.fbRadioGroup && this.fbRadioGroup.readonly)) {
				arr.push(`${prefix}-radio--readonly`)
			}
			if (this.disabled || (this.fbRadioGroup && this.fbRadioGroup.disabled)) {
				arr.push(`${prefix}-radio--disabled`)
			}

			return arr
		},

		getElClass () {
			let arr = [`${prefix}-radio__el`]

			if (isString(this.checked)) {
				arr.push(`${prefix}-radio__el--${this.checked}`)
			}
			if (isNumber(this.checked)) {
				arr.push(`${prefix}-radio__el--${this.checked}`)
			}
			if (isBoolean(this.checked) && this.checked === true) {
				arr.push(`${prefix}-radio__el--checked`)
			}
			return arr
		},
	},

	watch: {
		value: {
			immediate: true,
			handler (value) {
				if (this.group) {
					this.myValue = value
				} else {
					this.checked = value
				}
			},
		},
	},

	methods: {
		handleClick (e) {
			// 当禁用只读时，不响应点击
			if (this.disabled || this.readonly) {
				return false
			}

			if (this.fbRadioGroup) {
				if (this.fbRadioGroup.disabled || this.fbRadioGroup.readonly) {
					return false
				}
			}

			if (this.group) {
				if (this.checked) {
					return false
				}

				this.fbRadioGroup.$emit('on-click', this.myValue, this, e)
				if (!e.defaultPrevented) {
					this.fbRadioGroup.change(this.myValue, this, e)
				}
			} else {

				this.checked = !this.checked

				this.$emit('on-click', this.checked, e)

				if (!e.defaultPrevented) {
					this.$emit('input', this.checked)
					this.$emit('change', this.checked)
					this.$emit('on-change', this.checked)
				}
			}
		},

	},
	mounted () {

		if (this.fbRadioGroup) {
			if (includes(this.fbRadioGroup.currentValue, this.myValue)) {
				this.checked = true
			} else {
				this.checked = false
			}

			this.group = true
			this.name = this.fbRadioGroup.name
			this.fbRadioGroup.addItem(this)
		}

		// // console.log(this.$attrs, this.$props)
		//
		// this.options.group = this.$parent.$options.name === 'FbCheckboxGroup'
		// this.options.checked = this.checked
		// this.options.readonly = this.readonly
		// this.options.border = this.border

//		if (this.group) {
//			this.myValue = this.value
//		} else {
//			this.checked = this.value
//		}
	},
}
</script>

<style scoped>

</style>
