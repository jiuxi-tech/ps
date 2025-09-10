<template>
	<div :class="[
	        `${prefix}-textarea`,
	       `${prefix}-textarea--`+size,
	        {
				[`${prefix}-textarea-group`]: hasPrepend || hasAppend ||prependButton||appendButton|| maxlength,
				[`${prefix}-textarea-group--prepend`]  :hasPrepend,
				[`${prefix}-textarea-group--append` ] :hasAppend,
				[`${prefix}-textarea-group--prepend-button`]  :prependButton,
				[`${prefix}-textarea-group--append-button` ] :appendButton,
				[`${prefix}-textarea-group--maxlength` ] : maxlength,
				[`${prefix}-textarea--readonly`]: readonly,
				[`${prefix}-textarea--disabled`]: disabled,
				[`${prefix}-textarea--round`]: round,
	        }
	        ]"
	     :style="{
	        width: `${myWidth}`
	     }">
		<div ref="prepend" v-if="hasPrepend" :class="`${prefix}-textarea__prepend`">
			<span><slot name="prepend">{{prepend}}</slot></span>
		</div>

		<div ref="prependButton" v-if="prependButton" :class="`${prefix}-textarea__prepend-button`">
			<span><slot name="prepend-button"/></span>
		</div>


		<div
			:class="`${prefix}-textarea__content`"
			@mouseenter="hovering = true"
			@mouseleave="hovering = false"
		>
			<template v-if="icon">
				<fb-icon :name="icon" @on-click.stop="handleIconClick"
				         :class="`${prefix}-textarea__icon`"/>
			</template>

			<template v-if="password">
				<fb-icon v-if="showPassword"
				         @on-click.stop="changeInputType"
				         :class="`${prefix}-textarea__icon`"
				         name="show"/>

				<fb-icon v-if="!showPassword"
				         @on-click.stop="changeInputType"
				         :class="`${prefix}-textarea__icon`"
				         name="hidden"/>
			</template>

			<template v-if="clearable && !readonly && !disabled && hovering">
				<fb-icon @on-click.stop="handleClear" :class="`${prefix}-textarea__icon`" name="close-circle"/>
			</template>

			<fb-text ref="maxlength" v-if="maxlength" :class="`${prefix}-textarea__maxlength`">
				{{ getLength(currentValue) }}/{{ maxlength }}
			</fb-text>

			<textarea
				:value="currentValue"
				:rows="rows"
				:maxlength="maxlength"
				:readonly="readonly"
				:disabled="disabled"
				:placeholder="placeholder"
				@keyup.enter="handleEnter"
				@keyup.delete="handleDelete"
				@focus="handleFocus"
				@blur="handleBlur"
				@input="handleInput"
				@click="click"
				:class="`${prefix}-textarea__el`"
			></textarea>
		</div>

		<div ref="append" v-if="hasAppend" :class="`${prefix}-textarea__append`">
			<span><slot name="append">{{ append }}</slot></span>
		</div>

		<div ref="appendButton" v-if="appendButton" :class="`${prefix}-textarea__append-button`">
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

export default {
	name: 'FbTextarea',
	components: {
		FbIcon,
		FbText,
	},
	directives: {
		// autoRow,
	},
	props: {
		value: {
			type: [Number, String],
			default: '',
		},
		// 类型
		type: {
			type: String,
			default: 'text',
		},
		// 尺寸
		size: {
			type: String,
			default: 'm',
		},
		// 提示
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
		// 清楚按钮
		clearable: {
			type: Boolean,
			default: false,
		},
		// 最大长度
		maxlength: {
			type: [Number, String],
		},
		// 图标
		icon: {
			type: String,
			default: undefined,
		},
		// 行数
		rows: {
			type: [Number, String],
			default: 2,
		},
		// 宽
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
		round: Boolean,

		prepend: {
			type: String,
		},
		append: {
			type: String,
		},
	},
	data () {
		let slotValue = this.$slots.default

		return {
			prefix,
			currentValue: slotValue && slotValue[0] && slotValue[0].text || this.value,
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
	},
	created () {

		this.fbFormItem = closest(this, 'FbFormItem')

		this.prependButton = !!this.$slots['prepend-button']
		this.appendButton = !!this.$slots['append-button']

		this.slotReady = true

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
		handleIconClick (event) {
			if (this.readonly || this.disabled) {
				return
			}
			this.$emit('on-click', event)
		},
		handleEnter (event) {
			this.$emit('on-enter', event)
		},
		handleFocus (event) {
			this.$emit('on-focus', event)
		},
		handleBlur (event) {
			this.$emit('on-blur', event)
			this.fbFormItem && this.fbFormItem.$emit('on-form-blur', this.currentValue)
		},
		handleInput (event) {
			if (this.disabled || this.readonly) return
			let value = event.target.value
			if (this.number) value = Number.isNaN(Number(value)) ? value : Number(value)
			this.$emit('input', value, event)
			this.$emit('on-input', value, event)
			this.$emit('on-change', value, event)
			this.setCurrentValue(value)
		},
		changeInputType () {
			this.showPassword = !this.showPassword
			this.currentType = this.showPassword ? 'password' : 'text'
		},
		setCurrentValue (value) {
			if (value === this.currentValue) return
			this.currentValue = value
			this.fbFormItem && this.fbFormItem.$emit('on-form-change', this.currentValue)
		},
		click (e) {
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
