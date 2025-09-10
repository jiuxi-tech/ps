<template>
	<div
		@dragstart.prevent
		@mouseenter="handleEnter"
		@mouseleave="handleLeave"
		:class="[
		  `${prefix}-input-number`,
		  inputNumberSize ? `${prefix}-input-number--` + inputNumberSize : '',
		  { 'is-disabled': inputNumberDisabled },
		  { 'is-without-controls': !controls },
		  { 'is-controls-right': controlsAtRight }
		]"
		:style="{
	        width: `${myWidth}`
	     }">
		<span
			role="button"
			v-if="controls"
			v-repeat-click="decrease"
			:class="[`${prefix}-input-number__decrease`, {'is-disabled': minDisabled}]"
			@keydown.enter="decrease">
			<fb-icon :name="controlsAtRight ? 'down' : 'reduce-normal'"></fb-icon>
		</span>
		<span
			role="button"
			v-if="controls"
			v-repeat-click="increase"
			:class="[`${prefix}-input-number__increase`, {'is-disabled': maxDisabled}]"
			@keydown.enter="increase">
			<fb-icon :name="controlsAtRight ? 'up' : 'add-normal'"></fb-icon>
		</span>

		<div :class="`${prefix}-input-number__content`">
			<fb-input
				ref="input"
				:value="displayValue"
				:placeholder="placeholder"
				:disabled="inputNumberDisabled"
				:size="inputNumberSize"
				:max="max"
				:min="min"
				:name="name"
				:label="label"
				:readonly="readonly"
				:clearable="clearable"
				:maxlength="maxlength"
				:prependIcon="prependIcon"
				:icon="icon"
				:autocomplete="autocomplete"
				:round="round"
				:elStyle="elStyle"
				:prepend="prepend"
				:append="append"
				:appendShow="appendShow"
				@keydown.up.native.prevent="increase"
				@keydown.down.native.prevent="decrease"
				@on-blur="handleBlur"
				@on-focus="handleFocus"
				@on-input="handleInput"
				@on-change="handleInputChange">
				<template v-slot:prepend>
					<slot name="prepend"></slot>
				</template>
				<template v-slot:prepend-button>
					<slot name="prepend-button"></slot>
				</template>
				<template v-slot:append>
					<slot name="append"></slot>
				</template>
				<template v-slot:append-button>
					<slot name="append-button"></slot>
				</template>
			</fb-input>
		</div>

	</div>
</template>
<script>
	import {prefix} from '../../../../src/config'
	import FbInput from '../../input';
	// import Focus from 'element-ui/src/mixins/focus';
	import RepeatClick from '../../../directives/repeat-click';
	import FbIcon from "../../icon/src/FbIcon";
	import {getSizeStyle} from "../../../utils/propUtils";

	export default {
		name: 'FbInputNumber',
		// mixins: [Focus('input')],
		inject: {
			elForm: {
				default: ''
			},
			elFormItem: {
				default: ''
			}
		},
		directives: {
			repeatClick: RepeatClick
		},
		components: {
			FbIcon,
			FbInput
		},
		props: {
			step: {
				type: Number,
				default: 1
			},
			stepStrictly: {
				type: Boolean,
				default: false
			},
			max: {
				type: Number,
				default: Infinity
			},
			min: {
				type: Number,
				default: -Infinity
			},
			value: {},
			disabled: Boolean,
			size: String,
			controls: {
				type: Boolean,
				default: true
			},
			controlsPosition: {
				type: String,
				default: ''
			},
			name: String,
			label: String,
			placeholder: String,
			precision: {
				type: Number,
				validator(val) {
					return val >= 0 && val === parseInt(val, 10);
				}
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
		},
		data() {
			return {
				prefix,
				currentValue: 0,
				userInput: null,
				appendShow: true, // 判断 input append 显隐
			};
		},
		watch: {
			value: {
				immediate: true,
				handler(value) {
					if (typeof value === 'string') {
						this.currentValue = value
						return ''
					}
					let newVal = value === undefined ? value : Number(value);
					if (newVal !== undefined) {
						if (isNaN(newVal)) {
							return;
						}

						if (this.stepStrictly) {
							const stepPrecision = this.getPrecision(this.step);
							const precisionFactor = Math.pow(10, stepPrecision);
							newVal = Math.round(newVal / this.step) * precisionFactor * this.step / precisionFactor;
						}

						if (this.precision !== undefined) {
							newVal = this.toPrecision(newVal, this.precision);
						}
					}
					if (newVal >= this.max) newVal = this.max;
					if (newVal <= this.min) newVal = this.min;
					this.currentValue = newVal;
					this.userInput = null;
					// this.$emit('input', newVal);
				}
			}
		},
		computed: {
			myWidth () {
				return getSizeStyle(this.width)
			},
			myInputWidth() { // 计算 controlsPosition = right 时位置在 input 外边
				let width = '100%'
				let size = this.size
				if (this.controlsPosition === 'right' &&
					this.append || this.$slots.append || this.$slots['append-button']) {
					if (size === 'l') {
						width = 'calc(100% - 36px)'
					} else if (size === 's') {
						width = 'calc(100% - 24px)'
					} else {
						width = 'calc(100% - 31px)'
					}
				}
				return width
			},
			minDisabled() {
				return this._decrease(this.value, this.step) < this.min;
			},
			maxDisabled() {
				return this._increase(this.value, this.step) > this.max;
			},
			numPrecision() {
				const {value, step, getPrecision, precision} = this;
				const stepPrecision = getPrecision(step);
				if (precision !== undefined) {
					if (stepPrecision > precision) {
						console.warn('[Element Warn][InputNumber]precision should not be less than the decimal places of step');
					}
					return precision;
				} else {
					return Math.max(getPrecision(value), stepPrecision);
				}
			},
			controlsAtRight() {
				return this.controls && this.controlsPosition === 'right';
			},
			_elFormItemSize() {
				return (this.elFormItem || {}).elFormItemSize;
			},
			inputNumberSize() {
				return this.size || this._elFormItemSize || (this.$ELEMENT || {}).size;
			},
			inputNumberDisabled() {
				return this.disabled || !!(this.elForm || {}).disabled;
			},
			displayValue() {
				if (this.userInput !== null) {
					return this.userInput;
				}

				let currentValue = this.currentValue;

				if (typeof currentValue === 'number') {
					if (this.stepStrictly) {
						const stepPrecision = this.getPrecision(this.step);
						const precisionFactor = Math.pow(10, stepPrecision);
						currentValue = Math.round(currentValue / this.step) * precisionFactor * this.step / precisionFactor;
					}

					if (this.precision !== undefined) {
						currentValue = currentValue.toFixed(this.precision);
					}
				}

				return currentValue;
			}
		},
		methods: {
			toPrecision(num, precision) {
				if (precision === undefined) precision = this.numPrecision;
				return parseFloat(Math.round(num * Math.pow(10, precision)) / Math.pow(10, precision));
			},
			getPrecision(value) {
				if (value === undefined) return 0;
				const valueString = value.toString();
				const dotPosition = valueString.indexOf('.');
				let precision = 0;
				if (dotPosition !== -1) {
					precision = valueString.length - dotPosition - 1;
				}
				return precision;
			},
			_increase(val, step) {
				if (typeof val !== 'number' && val !== undefined) return this.currentValue;
				const precisionFactor = Math.pow(10, this.numPrecision);
				// Solve the accuracy problem of JS decimal calculation by converting the value to integer.
				return this.toPrecision((precisionFactor * val + precisionFactor * step) / precisionFactor);
			},
			_decrease(val, step) {
				if (typeof val !== 'number' && val !== undefined) return this.currentValue;

				const precisionFactor = Math.pow(10, this.numPrecision);

				return this.toPrecision((precisionFactor * val - precisionFactor * step) / precisionFactor);
			},
			increase() {
				if (this.inputNumberDisabled || this.maxDisabled) return;
				const value = this.currentValue || 0;
				const newVal = this._increase(value, this.step);
				this.setCurrentValue(newVal);
			},
			decrease() {
				if (this.inputNumberDisabled || this.minDisabled) return;
				const value = this.currentValue || 0;
				const newVal = this._decrease(value, this.step);
				this.setCurrentValue(newVal);
			},
			handleBlur(event) {
				this.$emit('on-blur', event);
			},
			handleFocus(event) {
				this.$emit('on-focus', event);
			},
			setCurrentValue(newVal) {
				const oldVal = this.currentValue;
				if (typeof newVal === 'number' && this.precision !== undefined) {
					newVal = this.toPrecision(newVal, this.precision);
				}
				if (newVal >= this.max) newVal = this.max;
				if (newVal <= this.min) newVal = this.min;
				if (oldVal === newVal) return;
				this.userInput = null;
				this.$emit('input', newVal);
				this.$emit('on-change', newVal, oldVal);
				this.currentValue = newVal;
			},
			handleInput(value) {
				this.userInput = value;
			},
			handleInputChange(value) {
				const newVal = value === '' ? undefined : Number(value);
				if (!isNaN(newVal) || value === '') {
					this.setCurrentValue(newVal);
				}
				this.userInput = null;
			},
			select() {
				this.$refs.input.select();
			},
			handleEnter() {

				if (this.controlsPosition === 'right') {
					this.appendShow = false
				}
			},
			handleLeave() {

				if (this.controlsPosition === 'right') {
					this.appendShow = true
				}
			},
		},
		mounted() {
			let innerInput = this.$refs.input.$refs.input;
			innerInput.setAttribute('role', 'spinbutton');
			innerInput.setAttribute('aria-valuemax', this.max);
			innerInput.setAttribute('aria-valuemin', this.min);
			innerInput.setAttribute('aria-valuenow', this.currentValue);
			innerInput.setAttribute('aria-disabled', this.inputNumberDisabled);
		},
		updated() {
			if (!this.$refs || !this.$refs.input) return;
			const innerInput = this.$refs.input.$refs.input;
			innerInput.setAttribute('aria-valuenow', this.currentValue);
		}
	};
</script>
