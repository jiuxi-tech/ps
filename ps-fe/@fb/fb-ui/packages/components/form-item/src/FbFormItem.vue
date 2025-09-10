<template>
	<div :class="getClass">
		<label v-if="(label || $slots.label) && showLabel" ref="label"
		       :style="getLabelStyle" :class="[`${prefix}-form-item__label`, {
			   		[`${prefix}-form-item__label--ellipsis`]: ellipsis,
			   		[`${prefix}-form-item__label--fixed`]: myLabelFixed
			   }]">

			<span v-if="$slots.label" :class="`${prefix}-form-item__label__text`"><slot name="label"/></span>

			<span v-else :class="`${prefix}-form-item__label__text`" :style="labelTextStyle">{{ label }}</span>

			<span :class="`${prefix}-form-item__label__required`">*</span>

			<span v-if="$slots['label-extra']"
			      :class="`${prefix}-form-item__label__extra`"
			      :style="{...labelExtraStyle}"><slot name="label-extra"/></span>


		</label>
		<div :style="getContentStyle" :class="`${prefix}-form-item__content`">
			<slot/>
			<div v-if="validateState === 'error' && showMessage && form.currentShowMessage"
			     :class="`${prefix}-form-item__message`">
				<!--				{{  validateMessage  }}-->
				<div v-for="(vmsg, i) in validateMessageArray">
					{{ validateMessageArray.length > 1 ? `${(i + 1)}、` : '' }} {{ vmsg }}
				</div>

			</div>
		</div>
	</div>
</template>

<script>
/**
 * FbFormItem.vue
 * (c) 2020 lincong1987
 */

import { isArray, isObject } from 'lodash-es'
import { closest, find } from '../../../utils/componentUtils'

import AsyncValidator from '../rule'
import messages from '../messages'
import { prefix } from '../../../../src/config'

/**
 * 校验框架用的是 AsyncValidator
 */

export default {

	name: 'FbFormItem',

	props: {

		// 字段名
		prop: {
			type: String,
			default: undefined,
		},
		// 标签
		label: {
			type: String,
			default: undefined,
		},
		// 显示标签
		showLabel: {
			type: Boolean,
			default: true,
		},
		// 标签宽度
		labelWidth: {
			type: [Number, String],
			default: undefined,
		},
		// 标签位置 可选 left right top
		labelPosition: {
			type: String,
			default: undefined,
		},

		labelFixed: {
			type: Boolean,
			default: false,
		},

		// 显示校验信息
		showMessage: {
			type: Boolean,
			default: true,
		},
		// 校验规则
		rule: {
			type: [Array, Object],
			default: undefined,
		},
		// 注意，表单项也是可以绑定值的
		value: {
			type: [String, Number, Array, Boolean, Object],
			default: null,
		},

		labelStyle: {
			type: [Object, Array, String],
			default: null,
		},

		labelTextStyle: {
			type: [Object, Array, String],
			default: null,
		},

		labelExtraStyle: {
			type: [Object, Array, String],
			default: null,
		},

		contentStyle: {
			type: [Object, Array, String],
			default: null,
		},

		ellipsis: {
			type: Boolean,
			default: false,
		},

		// 取消表单项下方的留白
		noPaddingBottom: {
			type: Boolean,
			default: false,
		},

	},

	data () {
		return {
			prefix,
			// hasLabel: true,
			// 状态
			validateState: '',
			// 消息
			validateMessage: '',
			// 消息数组
			validateMessageArray: [],
			// 重置开关
			validateDisabled: false,

			myLabelFixed: this.labelFixed,
		}
	},

//	created () {
//		this.form = closest(this, 'FbForm')
//	},

	inject: ['form'],

	watch: {
		labelFixed (value) {
			this.myLabelFixed = value
		},
		// 监听必填属性，控制warn提示显隐
		required (value) {
			if (!value) {
				this.validateState = ''
			}
		}
	},
	computed: {
		// 合并校验规则
		currentRule () {
			let rule = []

			if (this.rule) {
				if (isArray(this.rule)) {
					rule = rule.concat(this.rule)
				} else if (isObject(this.rule)) {
					rule = rule.concat([this.rule])
				}
			}

			// 你可以把规则写在form里, 要求是prop要对上
			if (this.form && this.form.rule && this.form.rule[this.prop]) {
				rule = rule.concat(this.form.rule[this.prop])
			}

			// 数据类型转换
			rule.forEach((n) => {
				if (n.type && ['integer', 'float', 'number'].includes(n.type)) {
					n.transform = function (value) {
						// 空串判断，Number(空串) === 0，绕过了Validator校验
						if (typeof value === 'string' && value.trim().length === 0 && n.type === 'number' && n.min >= 0) {
							return value
						}
						return Number.isNaN(Number(value)) ? value : Number(value)
					}
				}
			})
			return rule
		},

		// 监测规则里有没有必填
		required () {
			if (this.currentRule && this.currentRule.length > 0) {
				return this.currentRule.filter(rule => rule.required).length > 0
			}
			return false
		},
		getClass () {
			const {prefix} = this
			const arr = [`${prefix}-form-item`]
			if (this.required) {
				arr.push(`${prefix}-form-item--required`)
			} else {
				arr.push(`${prefix}-form-item--not-required`)
			}
			if (!!this.validateState) {
				arr.push(`${prefix}-form-item--${this.validateState}`)
				arr.push(`${prefix}-form-item--validate-${this.validateState}`)
			}
			if (this.noPaddingBottom === true) {
				arr.push(`${prefix}-form-item--no-padding-bottom`)
			}

			arr.push(
				`${prefix}-form-item--position-${this.labelPosition || (this.form && this.form.labelPosition)}`)
			return arr
		},
		getLabelStyle () {
			// console.log(`${this.labelWidth || this.form.labelWidth}px`)

			let style = {}

			style.width = 0

			if (this.label || this.$slots.label) {
				style.width = `${this.labelWidth || (this.form && this.form.labelWidth)}px`

				if (this.showLabel === false) {
					style.width = 0
				}
			}

			if (this.labelPosition === 'top' || (this.form && this.form.labelPosition === 'top')) {
				delete style.width
			}
			Object.assign(style, (this.form && this.form.labelStyle) || {}, this.labelStyle || {})

			return style
		},
		getContentStyle () {

			let style = {}

			style.marginLeft = 0

			if (this.label || this.$slots.label) {

				if ((this.labelPosition || (this.form && this.form.labelPosition)) === 'top') {
					style.marginLeft = 0
				} else {
					style.marginLeft = `${this.labelWidth || (this.form && this.form.labelWidth)}px`
				}

				if (this.showLabel === false) {
					style.marginLeft = 0
				}
			}

			Object.assign(style, (this.form && this.form.contentStyle) || {}, this.contentStyle || {})

			return style

//				return {
//					marginLeft: this.label ? ((this.labelPosition || (this.form && this.form.labelPosition)) == 'top'
//						? 0
//						: `${this.labelWidth || this.form.labelWidth}px`) : 0,
//				}
		},

	},
	mounted () {
		// 注意，有prop 才会添加校验项
		if (this.prop) {
			// 调用方法进行添加事件，直接添加事件会报错
			this.addValidateEvents()

			// 最初始的值
			let initialValue = this.getFieldValue()
			if (isArray(initialValue)) {
				initialValue = [].concat(initialValue)
			}
			Object.defineProperty(this, 'initialValue', {
				value: initialValue,
			})
		}

		this.form && this.form.addItem(this)

//		if (this.$slots['label-extra'] && this.$slots['label-extra'].length > 0 && this.$slots['label-extra'][0].data &&
//			this.$slots['label-extra'][0].data) {
//
//			this.$slots['label-extra'][0].data.style = {
//				...this.$slots['label-extra'][0].data.style,
//				...this.labelExtraStyle,
//			}
//debugger
//			this.$slots['label-extra'][0].data.class =  {
//				...this.$slots['label-extra'][0].data.class,
//				[`${this.prefix}-form-item__label__extra`]: true
//			}
//		}
	},
	beforeDestroy () {
		this.form && this.form.removeItem(this)

		this.$off('on-form-blur')
		this.$off('on-form-change')

		//	this.form = null

	},
	methods: {
		// 校验控件的当前值
		getFieldValue () {
			let value

			if (this.value) {
				return this.value
			}

			// 找到 表单项内的第一个 输入组件
			let child = find(this, [
				'FbInput',
				'FbInputNumber',
				'FbCascader',
				'FbTextarea',
				'FbRadio',
				'FbRadioGroup',
				'FbCheckbox',
				'FbCheckboxGroup',
				'FbDatepicker',
				'FbTimepicker',
				'FbSelect',
				'FbTreeSelect',
				'FbDragVerify',
				'FbEditor',
				'FbUpload',
				'TpDatepicker',
				'TpUpload',
			])

			if (!child && this.$children && this.$children[0] && this.$children[0].$options) {
				child = this.$children[0]
			}

			if (child) {
//				if (child.$options.componentName === 'FbSelect') {
//					debugger
//				}

				if (typeof child.value !== 'undefined') {
					value = child.value
				}

				if (typeof child.currentValue !== 'undefined') {
					value = child.currentValue
				}

				if (child.validate) {
					value = child.validate()
				}

				if (child.$options.name === 'FbUpload') {
					value = child.fileList
				}

			} else {

				// console.log('FbFormItem组件内没有找到表单组件')
				return
			}

			child = null

			return value
		},

		/**
		 * 表单项校验
		 * @param trigger
		 * @param callback
		 * @param currentValue
		 * @returns {boolean|void}
		 */
		validate (trigger, callback, currentValue) {
			callback = callback || (() => {
			})

			// 没有校验规则时，默认通过
			if (!this.currentRule || this.currentRule.length === 0) {
				callback()
				return true
			}
			const rules = this.currentRule.filter(rule => {
				return !rule.trigger || rule.trigger.indexOf(trigger) !== -1
			})
			if (rules.length === 0) {
				callback()
				return true
			}

			// 标记：校验中
			this.validateState = 'validating'
			const descriptor = {}
			descriptor[this.prop] = rules
			// 设置：校验内容
			const model = {}
			// model[this.prop] = value
			model[this.prop] = typeof currentValue === 'undefined' ? this.getFieldValue() : currentValue

			// 初始化：校验器
			// const validator = new AsyncValidator(descriptor, {messages})
			const validator = new AsyncValidator(descriptor)
			validator.messages(messages)

			// console.log('表单项校验:', model)
			// model:  {prop:value}
			validator.validate(model, (errors, b, c) => {
				this.form.$emit('on-item-validate', [errors])
				if (errors) {
					// 标记：校验未通过
					this.validateState = 'error'
					// this.validateMessage = errors[0].message

					console.info(errors[0].message, descriptor, errors)
					this.validateMessage = errors[0].message.replace(errors[0].field, this.label || '')
					this.validateMessageArray = errors.map(e => e.message.replace(e.field, this.label || ''))
					callback(errors)
				} else {
					// 标记：校验通过
					this.validateState = 'success'
					this.validateMessageArray = []
					callback()
				}
			})
		},
		resetField () {
			this.validateState = ''
			this.validateMessage = ''
			this.validateMessageArray = []

			this.validateDisabled = true
			if (this.form && isObject(this.form.model)) {
				if (isArray(this.getFieldValue())) {
					this.form.model[this.prop] = [].concat(this.initialValue)
				} else {
					this.form.model[this.prop] = this.initialValue
				}
			}

			// reset validateDisabled after onFieldChange triggered
			this.$nextTick(() => {
				this.validateDisabled = false
			})
		},
		addValidateEvents () {
			this.$on('on-form-blur', this.onFieldBlur)
			this.$on('on-form-change', this.onFieldChange)
		},
		onFieldBlur (currentValue) {
			// console.log('onFieldBlur')
			this.validate('blur', null, currentValue)
		},
		onFieldChange (currentValue) {
			if (this.validateDisabled) {
				this.validateDisabled = false
				return
			}
			// console.log('onFieldChange')
			this.validate('change', null, currentValue)
		},
	},
}
</script>

<style scoped lang="less">


</style>
