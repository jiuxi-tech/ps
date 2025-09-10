<template>
	<div :class="[
	        `${prefix}-input`,
	       `${prefix}-input--${size}`,
	        {
				[`${prefix}-input-group`]: hasPrepend || hasAppend ||prependButton||appendButton|| maxlength,
				[`${prefix}-input-group--prepend`]  :hasPrepend,
				[`${prefix}-input-group--append`]  :hasAppend,
				[`${prefix}-input-group--prepend-button`]  :prependButton,
				[`${prefix}-input-group--append-button`]  :appendButton,
				[`${prefix}-input-group--maxlength`]  : maxlength,
				[`${prefix}-input--readonly`]: readonly,
				[`${prefix}-input--disabled`]: disabled,
				[`${prefix}-input--round`]: round,
	        }
	        ]"
	     :style="{
	        width: `${myWidth}`
	     }">

		<div ref="prepend" v-if="hasPrepend" :class="`${prefix}-input__prepend`">
			<span><slot name="prepend">{{ prepend }}</slot></span>
		</div>

		<div ref="prependButton" v-if="prependButton" :class="`${prefix}-input__prepend-button`">
			<span><slot name="prepend-button"/></span>
		</div>

		<div
			:class="`${prefix}-input__content`"
			@mouseenter="hovering = true"
			@mouseleave="hovering = false"
		>

			<template v-if="prependIcon">
				<fb-icon :name="prependIcon" @on-click="handlePrependIconClick"
				         :class="`${prefix}-input__prepend-icon`"/>
			</template>

			<template v-if="icon">
				<fb-icon :name="icon" @on-click="handleIconClick" :class="`${prefix}-input__icon`"/>
			</template>


			<template v-if="password">
				<fb-icon
					@on-click="changeInputType"
					:class="`${prefix}-input__icon`"
					:name="!showPassword ? 'show' : 'hidden'"/>
			</template>

			<template v-if="clearable && !readonly && !disabled && hovering">
				<fb-icon @on-click="handleClear" :class="`${prefix}-input__icon`" name="close-circle"/>
			</template>

			<div :class="`${prefix}-input__suffix`">
				<slot name="suffix"></slot>
			</div>

			<fb-text ref="maxlength" v-if="maxlength" :class="`${prefix}-input__maxlength`">
				{{ getLength(currentValue) }}/{{ maxlength }}
			</fb-text>

			<input
				ref="input"
					:value="currentValue"
			       :maxlength="maxlength"
			       :readonly="readonly"
			       :type="currentType"
			       :disabled="disabled"
			       :placeholder="placeholder"
			       :autocomplete="autocomplete"
			       :name="name"
			       @keyup.enter="handleEnter"
			       @keyup.delete="handleDelete"
			       @focus="handleFocus"
			       @blur="handleBlur"
			       @input="handleInput"
			       @click="handleClick"
				@change="handleChange"
			       :class="`${prefix}-input__el`"
			       :style="elStyle"
			/>
		</div>

		<div ref="append" v-if="hasAppend && appendShow" :class="`${prefix}-input__append`">
			<span><slot name="append">{{ append }}</slot></span>
		</div>

		<div ref="appendButton" v-if="appendButton && appendShow" :class="`${prefix}-input__append-button`">
			<span><slot name="append-button"/></span>
		</div>


	</div>
</template>

<script>

import { closest } from '../../../utils/componentUtils'
import FbIcon from '../../icon/src/FbIcon'
import FbText from '../../text/src/FbText'
import { getSizeStyle } from '../../../utils/propUtils'

import { prefix } from '../../../../src/config'

/**
 * slot
 * append-button
 * append
 */
export default {
	name: 'FbInput',
	components: {
		FbIcon,
		FbText,
	},
	props: {
		value: {
			type: [String, Number],
			default: '',
		},
		// 类型，可选 text password
		type: {
			type: String,
			default: 'text',
		},
		// 尺寸
		size: {
			type: String,
			default: 'm',
		},
		// 占位提示
		placeholder: {
			type: String,
			default: undefined,
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
		// 可清除
		clearable: {
			type: Boolean,
			default: false,
		},
		// 最大长度
		maxlength: {
			type: [Number, String],
		},
		// 前置图标
		prependIcon: {
			type: String,
			default: undefined,
		},
		// 后置图标
		icon: {
			type: String,
			default: undefined,
		},
		// 宽度
		width: {
			type: [String, Number],
			default: '100%',
		},
		name: {
			type: String,
			default: undefined,
		},
		// 自动完成
		autocomplete: {
			type: String,
			default: 'off',
		},
		// 圆角
		round: {
			type: Boolean,
			default: false,
		},

		elStyle: {
			type: [String, Array, Object],
		},

		prepend: {
			type: String,
		},
		append: {
			type: String,
		},
		appendShow: {
			type: Boolean,
			default: true,
		}
	},
	data () {
		return {
			prefix,
			currentValue: this.value,
			password: false,
			showPassword: true,
			currentType: this.type,
			hovering: false,
			prependButton: false,
			appendButton: false,
		}
	},
	computed: {

		myWidth () {
			return getSizeStyle(this.width)
		},
		hasPrepend () {
			return !!(this.$slots.prepend || this.prepend)
		},

		hasAppend () {
			return !!(this.$slots.append || this.append)
		},

	},
	watch: {
		value (val) {
			this.setCurrentValue(val)
		},

		type (val) {
			this.currentType = val
			this.password = (val === 'password')
		},
	},
	created () {

		this.fbFormItem = closest(this, 'FbFormItem')

		this.prependButton = !!this.$slots['prepend-button']
		this.appendButton = !!this.$slots['append-button']

		this.slotReady = true

		if (this.currentType === 'password') {
			this.password = true
		}

	},

	beforeDestroy () {
		this.fbFormItem = null
	},

	methods: {
		handleDelete (event) {
			// IE9删除时不触发input事件
			if (navigator.userAgent.indexOf('MSIE 9') === -1) return
			this.handleInput(event)
		},
		handlePrependIconClick (event) {
			if (this.readonly || this.disabled) {
				return
			}
			this.$emit('on-prepend-icon-click', event)
		},
		handleIconClick (event) {
			if (this.readonly || this.disabled) {
				return
			}
			this.$emit('on-icon-click', event)
		},
		handleEnter (event) {
			this.$emit('on-enter', event)
		},
		handleFocus (event) {
			this.$emit('on-focus', event)
		},
		handleBlur (event) {
			this.$emit('on-blur', event)
			if (this.formItem) {
				this.formItem.$emit('on-form-blur', this.currentValue)
			}
		},
		handleInput (event) {
			if (this.disabled || this.readonly) return
			let value = event.target.value
			if (this.number) value = Number.isNaN(Number(value)) ? value : Number(value)
			// 处理 v-model 绑定
			this.$emit('input', value, event)
			// 向外抛事件
			this.$emit('on-input', value, event)
			this.setCurrentValue(value)
		},
		handleChange(event) {
			this.$emit('on-change', event.target.value, event)
		},

		changeInputType () {
			this.showPassword = !this.showPassword
			this.currentType = this.showPassword ? 'password' : 'text'
		},
		setCurrentValue (value) {
			if (value === this.currentValue) return

			this.currentValue = value
			if (this.formItem) {
				this.formItem.$emit('on-form-change', this.currentValue)
			}
		},
		handleClick (e) {
			this.$emit('on-click', e)
		},

		getLength () {
			return this.currentValue.length
		},

		handleClear () {
			if (this.disabled || this.readonly) return
			if (this.currentValue === '') return
			this.$emit('input', '')
			this.$emit('on-input', '')
			this.$emit('on-change', '')
			this.setCurrentValue('')
		},
	},
}
</script>


<style scoped>

</style>
