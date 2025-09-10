<template>
	<div
		:class="[
      		`${prefix}-color-picker`,
		  disabled ? 'is-disabled' : '',
		  size ? `${prefix}-color-picker--${ size }` : ''
		]"
		v-clickoutside="hide">
		<div :class="`${prefix}-color-picker__mask`" v-if="disabled"></div>
		<div :class="`${prefix}-color-picker__trigger`" @click="handleTrigger">
			  <span :class="[`${prefix}-color-picker__color`, { 'is-alpha': myAlpha }]">
				<span :class="`${prefix}-color-picker__color-inner`"
					  :style="{
							backgroundColor: displayedColor
						  }"/>
				<span :class="`${prefix}-color-picker__empty`" v-if="!value && !showPanelColor">
					<fb-icon name="close"/>
				</span>
			  </span>
			<span :class="`${prefix}-color-picker__icon`" v-show="value || showPanelColor">
<!--				<fb-icon name="down"/>-->
			</span>
		</div>

		<picker-dropdown
			ref="dropdown"
			:class="[`${prefix}-color-picker__panel`, popperClass || '']"
			v-model="showPicker"
			@on-pick="confirmValue"
			@on-change="fastConfirmValue"
			@on-clear="clearValue"
			:themePredefine="themePredefine"
			:color="color"
			:alpha="myAlpha"
			:predefine="predefine"

			:need-confirm="needConfirm"
		>
		</picker-dropdown>
	</div>
</template>

<script>
import { prefix } from '../../../../src/config'
import Color from './color'
import PickerDropdown from './components/picker-dropdown.vue'
import Clickoutside from '../../../utils/clickoutside'
import Emitter from '../../../mixins/emitter'
import FbIcon from '../../icon/src/FbIcon'

export default {
	name: 'FbColorPicker',

	mixins: [Emitter],
	components: {
		PickerDropdown,
		FbIcon,
	},

	props: {
		value: String,
		showAlpha: {
			type: Boolean,
			default: false,
		},
		colorFormat: String,
		disabled: Boolean,
		size: String,
		popperClass: String,
		predefine: Array,
		themePredefine: {
			type: Boolean,
			default: false,
		},
		alpha: {
			type: Boolean,
			default: false,
		},
		format: {
			type: String,
		},
		needConfirm: {
			type: Boolean,
			default: false,
		},
	},

	directives: {Clickoutside},

	computed: {
		displayedColor () {
			if (!this.value && !this.showPanelColor) {
				return 'transparent'
			}

			return this.displayedRgb(this.color, this.myAlpha)
		},

	},

	watch: {

		alpha (val) {
			this.myAlpha = val
			this.color.enableAlpha = val
			this.fastConfirmValue()
		},

		format (val) {
			this.myFormat = val
			this.color.format = val
			this.fastConfirmValue()
		},
		value (val) {
			if (!val) {
				this.showPanelColor = false
			} else if (val && val !== this.color.value) {
				this.color.fromString(val)
			}
		},
		color: {
			deep: true,
			handler () {
				this.showPanelColor = true
			},
		},
		displayedColor (val) {
			if (!this.showPicker) return
			const currentValueColor = new Color({
				enableAlpha: this.myAlpha,
				format: this.myFormat,
			})
			currentValueColor.fromString(this.value)

			const currentValueColorRgb = this.displayedRgb(currentValueColor, this.myAlpha)
			if (val !== currentValueColorRgb) {
				this.$emit('on-active-change', val)
			}
		},
	},

	methods: {
		handleTrigger () {
			if (this.disabled) return
			this.showPicker = !this.showPicker
		},
		confirmValue () {
			const value = this.color.value
			this.$emit('input', value)
			this.$emit('change', value)
			this.$emit('on-change', value)
			this.showPicker = false
		},
		fastConfirmValue () {
			if (this.needConfirm) return

			// const value = this.color.value
			const value = this.color.toString()
			this.$emit('input', value)
			this.$emit('change', value)
			// 防止首次触发，引起使用阶段错误
			if (value !== this.value) {
				this.$emit('on-change', value)
			}
		},
		clearValue () {

			this.$emit('change', null)
			this.$emit('on-change', null)
			this.$emit('input', null)
			this.showPanelColor = false
			this.showPicker = false
			//this.resetColor()
		},
		hide () {
			this.showPicker = false
			this.resetColor()
		},
		resetColor () {
			this.$nextTick(_ => {
				if (this.value) {
					this.color.fromString(this.value)
				} else {
					this.showPanelColor = false
				}
			})
		},
		displayedRgb (color, alpha) {
			if (!(color instanceof Color)) {
				throw Error('color should be instance of Color Class')
			}

			const {
				r,
				g,
				b,
			} = color.toRgb()
			return alpha
				? `rgba(${r}, ${g}, ${b}, ${color.get('alpha') / 100})`
				: `rgb(${r}, ${g}, ${b})`
		},
	},

	mounted () {
		const value = this.value
		if (value) {
			this.color.fromString(value)
		}
		this.popperElm = this.$refs.dropdown.$el
	},

	data () {

		let myAlpha = this.alpha || this.showAlpha
		let myFormat = this.format || this.colorFormat

		const color = new Color({
			enableAlpha: myAlpha,
			format: myFormat,
		})

		return {
			prefix,
			showPicker: false,
			showPanelColor: false,
			myAlpha,
			myFormat,
			color,
		}
	},

}
</script>
