<template>
	<div :class="[
	        `${prefix}-number-input`,
	       `${prefix}-number-input--${size}`,
	        {
				[`${prefix}-number-input--group`]: true,
				[`${prefix}-number-input--readonly`]: readonly,
				[`${prefix}-number-input--disabled`]: disabled,
				[`${prefix}-number-input--round`]: round,
	        }
	        ]"
		 :style="{
	        width: `${myWidth}`
	     }">


		<div
			:class="`${prefix}-number-input__content`"
			@mouseenter="hovering = true"
			@mouseleave="hovering = false"
		>

			<fb-icon
				@mousedown="!this.readonly && !this.disabled && true"
				@mouseup="hovering = true"
				@mouseleave="hovering = false"

				:class="`${prefix}-number-input__icon ${prefix}-number-input__prepend-icon`"
				name="reduce-circle"/>


			<input :value="currentValue"
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
				   @click="click"
				   :class="`${prefix}-number-input__el`"
			/>

			<fb-icon @on-click="handleUp"
					 :class="`${prefix}-number-input__icon`"
					 name="add-circle"/>

		</div>


	</div>
</template>

<script>

	import {closest} from '../../../utils/componentUtils'
	import FbIcon from '../../icon/src/FbIcon'
	import {getSizeStyle} from '../../../utils/propUtils'
	import {prefix} from '../../../../src/config'
	import FbButton from '../../button/src/FbButton'

	function preventDefault(e) {
		e.preventDefault()
	}

	function defaultParser(input) {
		return input.replace(/[^\w\.-]+/g, '')
	}

	const SPEED = 200

	const DELAY = 600

	const MAX_SAFE_INTEGER = Number.MAX_SAFE_INTEGER || Math.pow(2, 53) - 1

	const isValidProps = value => value !== undefined && value !== null

	const isEqual = (oldValue, newValue) =>
		newValue === oldValue ||
		(typeof newValue === 'number' &&
			typeof oldValue === 'number' &&
			isNaN(newValue) &&
			isNaN(oldValue))

	/**
	 * slot
	 * append-button
	 * append
	 */
	export default {
		name: 'FbNumberInput',
		components: {
			FbButton,
			FbIcon,
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

			formatter: {
				type: Function,
				default() {
					return () => {
					}
				},
			},
			max: {
				type: Number,
				default: MAX_SAFE_INTEGER,
			},
			min: {
				type: Number,
				default: -MAX_SAFE_INTEGER,
			},
			parser: {
				type: Function,
				default() {
					return val => val.replace(/[^\w\.-]+/g, '')
				},
			},
			// 数值精度
			precision: {
				type: Number,
				default: undefined,
			},
			// 	小数点
			decimalSeparator: {
				type: Number,
				default: undefined,
			},
			// 	步数
			step: {
				type: Number,
				default: 1,
			},
			// 快速模式，按住鼠标或按住键盘
			fastMode: {
				type: Boolean,
				default: false,
			},

			// 按住ctrl点按键盘 步进*10
			ctrl: {
				type: Boolean,
				default: false,
			},
		},
		data() {

			const validValue = this.getValidValue(this.toNumber(value))

			return {
				prefix,
				currentValue: this.value,
				password: false,
				showPassword: true,
				prepend: false,
				append: false,
				currentType: this.type,
				hovering: false,
				prependButton: false,
				appendButton: false,
				formItem: closest(this, 'FbFormItem'),
			}
		},
		computed: {

			myWidth() {
				return getSizeStyle(this.width)
			},

		},
		watch: {
			value(val) {
				this.setCurrentValue(val)
			},

			type(val) {
				this.currentType = val
				this.password = (val === 'password')
			},
		},
		methods: {

			getValidValue() {

			},

			handleDelete(event) {
				// IE9删除时不触发input事件
				if (navigator.userAgent.indexOf('MSIE 9') === -1) return
				this.handleInput(event)
			},
			handlePrependIconClick(event) {
				if (this.readonly || this.disabled) {
					return
				}
				this.$emit('on-prepend-icon-click', event)
			},
			handleIconClick(event) {
				if (this.readonly || this.disabled) {
					return
				}
				this.$emit('on-icon-click', event)
			},
			handleEnter(event) {
				this.$emit('on-enter', event)
			},
			handleFocus(event) {
				this.$emit('on-focus', event)
			},
			handleBlur(event) {
				this.$emit('on-blur', event)
				if (this.formItem) {
					this.formItem.$emit('on-form-blur', this.currentValue)
				}
			},
			handleInput(event) {
				if (this.disabled || this.readonly) return
				let value = event.target.value
				if (this.number) value = Number.isNaN(Number(value)) ? value : Number(value)
				// 处理 v-model 绑定
				this.$emit('input', value, event)
				// 向外抛事件
				this.$emit('on-input', value, event)
				this.$emit('on-change', value, event)
				this.setCurrentValue(value)
			},

			changeInputType() {
				this.showPassword = !this.showPassword
				this.currentType = this.showPassword ? 'password' : 'text'
			},
			setCurrentValue(value) {
				if (value === this.currentValue) return

				this.currentValue = value
				if (this.formItem) {
					this.formItem.$emit('on-form-change', this.currentValue)
				}
			},
			click(e) {
				this.$emit('on-click', e)
			},

			getLength() {
				return this.currentValue.length
			},

			handleClear() {
				if (this.disabled || this.readonly) return
				if (this.currentValue === '') return
				this.$emit('input', '')
				this.$emit('on-input', '')
				this.$emit('on-change', '')
				this.setCurrentValue('')
			},

			handleUp() {
			},

			/**
			 * 加法
			 * @param arg1
			 * @param arg2
			 * @returns {number}
			 */
			add: function (arg1, arg2) {
				var r1, r2, m
				try {
					r1 = arg1.toString().split('.')[1].length
				} catch (e) {
					r1 = 0
				}
				try {
					r2 = arg2.toString().split('.')[1].length
				} catch (e) {
					r2 = 0
				}
				m = Math.pow(10, Math.max(r1, r2))
				return (this.multiplication(arg1, m) + this.multiplication(arg2, m)) / m
			},
			/**
			 * 减法
			 * @param arg1 被减数
			 * @param arg2 减数
			 * @returns {string}
			 */
			subtraction: function (arg1, arg2) {
				var r1, r2, m, n
				try {
					r1 = arg1.toString().split('.')[1].length
				} catch (e) {
					r1 = 0
				}
				try {
					r2 = arg2.toString().split('.')[1].length
				} catch (e) {
					r2 = 0
				}
				m = Math.pow(10, Math.max(r1, r2))
				//动态控制精度长度
				n = (r1 >= r2) ? r1 : r2
				return ((arg1 * m - arg2 * m) / m).toFixed(n)
			},
			/**
			 * 乘法
			 * @param arg1
			 * @param arg2
			 * @returns {number}
			 */
			multiplication: function (arg1, arg2) {
				var m = 0, s1 = arg1.toString(), s2 = arg2.toString()
				try {
					m += s1.split('.')[1].length
				} catch (e) {
				}
				try {
					m += s2.split('.')[1].length
				} catch (e) {
				}
				return Number(s1.replace('.', '')) * Number(s2.replace('.', '')) / Math.pow(10, m)
			},
			/**
			 * 除法
			 * @param arg1
			 * @param arg2
			 * @returns {*|number}
			 */
			division: function (arg1, arg2) {
				var t1 = 0, t2 = 0, r1, r2
				try {
					t1 = arg1.toString().split('.')[1].length
				} catch (e) {
				}
				try {
					t2 = arg2.toString().split('.')[1].length
				} catch (e) {
				}
				r1 = Number(arg1.toString().replace('.', ''))
				r2 = Number(arg2.toString().replace('.', ''))
				return this.multiplication((r1 / r2), Math.pow(10, t2 - t1))
			},

		},
	}
</script>


<style scoped>

</style>
