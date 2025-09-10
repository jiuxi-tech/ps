<template>
	<div :class="getClass"
	     @click.stop="toggle"
	     @keydown="keydown"
	     :tabindex="getTabindex">
		<div :class="`${prefix}-switch__circle`"/>
		<slot v-if="currentValue" name="open"/>
		<slot v-else name="close"/>
	</div>
</template>

<script>
/**
 * FbSwitch
 * (c) 2020 lincong1987
 */

import { prefix } from '../../../../src/config'
import { closest } from '../../../utils/componentUtils'
import keyCode from '../../../utils/keyCode'

export default {
	name: 'FbSwitch',
	props: {
		value: {
			type: [Boolean, String, Number],
			default: false,
		},
		//尺寸
		size: {
			type: String,
			default: 'm',
		},
		// 警用
		disabled: {
			type: Boolean,
			default: false,
		},
		// 只读
		readonly: {
			type: Boolean,
			default: false,
		},
		// 需要确认，请返回一个promise
		confirm: {
			type: Function,
			default: undefined,
		},
		reader: {
			type: Array,
			default () {
				return [true, false]
			},
		},

		trueValue: {
			type: [Boolean, String, Number],
			default: true,
		},

		falseValue: {
			type: [Boolean, String, Number],
			default: false,
		},
	},
	data () {

		return {
			prefix,
			currentValue: this.trueValue === this.value ? this.trueValue : this.falseValue,

		}
	},
	computed: {
		// tab 键
		getTabindex () {
			if (this.disabled || this.readonly) {
				return -1
			}
			return 0
		},
		getClass () {
			const classes = [
				`${prefix}-switch`, {
//					[`${prefix}-switch--on`]: this.currentValue,
//					[`${prefix}-switch--off`]: !this.currentValue,

					[`${prefix}-switch--on`]: this.trueValue === this.currentValue,
					[`${prefix}-switch--off`]: this.falseValue === this.currentValue,
					// this.trueValue === this.value ? this.trueValue : this.falseValue

					[`${prefix}-switch--readonly`]: this.readonly,
					[`${prefix}-switch--disabled`]: this.disabled,
					[`${prefix}-switch--size--${this.size}`]: true,
				},
			]
			return classes
		},
	},
	created () {
		this.fbFormItem = closest(this, 'FbFormItem')
	},
	beforeDestroy () {
		this.fbFormItem = null
	},
	watch: {
		value (val) {
			this.currentValue = val
		},
	},
	methods: {
		toggle () {
			if (this.disabled || this.readonly) {
				return
			}
			if (this.confirm) {
				this.confirm(this.currentValue).then(() => {

					if (this.currentValue === this.trueValue) {
						this.currentValue = this.falseValue
					} else {
						this.currentValue = this.trueValue
					}

					//	this.currentValue = !this.currentValue
					this.$emit('input', this.currentValue)
					this.$emit('on-change', this.currentValue)
					this.fbFormItem && this.fbFormItem.$emit('on-form-change', [this.currentValue])
				}).catch(e => {
				})
			} else {
				if (this.currentValue === this.trueValue) {
					this.currentValue = this.falseValue
				} else {
					this.currentValue = this.trueValue
				}

				this.$emit('input', this.currentValue)
				this.$emit('on-change', this.currentValue)
				this.fbFormItem && this.fbFormItem.$emit('on-form-change', [this.currentValue])
			}
		},
		keydown (e) {
			if (this.disabled || this.readonly) return
			if (e.keyCode == keyCode.ENTER || e.keyCode == keyCode.MAC_ENTER || e.keyCode == keyCode.SPACE) {
				e.preventDefault()
				this.toggle()
			}
		},
	},

}
</script>

<style scoped>

</style>
