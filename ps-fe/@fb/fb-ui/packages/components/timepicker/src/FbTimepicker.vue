<template>
	<div ref="reference" v-clickoutside="outside" :class="`${prefix}-timepicker`">
		<fb-popup-header
			:value="times"
			:placeholder="placeholder"
			:icon="icon"
			:disabled="disabled"
			:readonly="readonly"
			:clearable="clearable"
			@on-clear="clear"
			@on-click="showPopup"
			@on-icon-click="showPopup"
			@on-keydown="keydown"
		/>

		<!--		<fb-popup-picker v-model="show">-->
		<transition name="zoom-in-top">
			<div ref="popper" v-show="show" :class="[`${prefix}-popper-no-arrow`]">
				<div :class="[`${prefix}-timepicker-dropdown`]">
					<fb-time-select
						v-model="times"
						:format="format"
						:hour-step="hourStep"
						:minute-step="minuteStep"
						:second-step="secondStep"
						:disabled-hours="disabledHours"
						:disabled-minutes="disabledMinutes"
						:disabled-seconds="disabledSeconds"
						@blur="selectBlur"
					/>

					<div :class="`${prefix}-timepicker__footer`">
						<fb-button type="primary" size="s" @on-click="setTime">此刻</fb-button>
						<slot name="addon"/>
					</div>
				</div>
			</div>
		</transition>

		<!--		</fb-popup-picker>-->
	</div>
</template>

<script>
/*!
 * FbTimepicker
 * (c) 2020 lincong1987
 */

import clickoutside from '../../../directives/clickoutside'
import { closest } from '../../../utils/componentUtils'
import { timeFormat, isSameDay, isEqual, inOnePanel } from '../../../utils/datetime'
import keyCode from '../../../utils/keyCode'
import FbPopupPicker from '../../popup/popup-picker/src/FbPopupPicker'
import FbPopupHeader from '../../popup/popup-header/src/FbPopupHeader'
import FbTimeSelect from './FbTimeSelect'
import FbButton from '../../button/src/FbButton'
import { prefix } from '../../../../src/config'
import PopperMixin from "../../../utils/popper-mixin"

export default {
	name: 'FbTimepicker',
	components: {
		FbButton,
		FbTimeSelect,
		FbPopupHeader,
		FbPopupPicker,
	},
	mixins: [PopperMixin],
	directives: {
		clickoutside,
	},
	props: {
		value: {
			type: String,
			default: '',
		},
		placeholder: {
			type: String,
			default: '',
		},
		icon: {
			type: String,
			default: 'waiting',
		},
		disabled: {
			type: Boolean,
			default: false,
		},
		clearable: {
			type: Boolean,
			default: false,
		},
		readonly: {
			type: Boolean,
			default: false,
		},
		format: {
			type: String,
			default: 'HH:mm:ss',
		},
		valueFormat: {
			type: String,
			default: 'HH:mm:ss',
		},
		hourStep: {
			type: Number,
			default: 1,
		},
		minuteStep: {
			type: Number,
			default: 1,
		},
		secondStep: {
			type: Number,
			default: 1,
		},
		disabledHours: {
			type: Function,
			default: undefined,
		},
		disabledMinutes: {
			type: Function,
			default: undefined,
		},
		disabledSeconds: {
			type: Function,
			default: undefined,
		},
	},
	data () {
		let today = timeFormat(new Date(), 'YYYY-MM-DD')
		let times = this.value ? timeFormat(today + this.value, this.format) : ''
		return {
			prefix,
			today,
			show: false,
			times,
		}
	},

	created () {

		this.fbFormItem = closest(this, 'FbFormItem')
	},

	beforeDestroy () {
		this.fbFormItem = null
	},

	watch: {
		value () {
			if (this.times !== this.value) {
				// this.times = this.value
				if (this.value) {
					let times = timeFormat(this.today + this.value, this.format)
					this.times = times
				}
			}
		},
		times () {
			let times = timeFormat(this.today + this.times, this.valueFormat)
			this.$emit('input',     times)
			this.$emit('change',    times)
			this.$emit('on-change', times)
			// 适配表单
			this.fbFormItem && this.fbFormItem.$emit('on-form-change', times)
		},
	},
	methods: {
		setTime () {
			this.times = timeFormat(new Date(), this.format)
		},
		outside () {
			if (this.show) {
				this.show = false
				this.fbFormItem && this.fbFormItem.$emit('on-form-blur', this.times)
			}
		},
		showPopup () {
			if (this.readonly || this.disabled) return
			this.show = true
			this.updatePopper()
		},
		keydown (e) {
			if (!this.show) {
				if (e.keyCode == keyCode.SPACE || e.keyCode == keyCode.ENTER || e.keyCode == keyCode.MAC_ENTER ||
					e.keyCode == keyCode.TAB) {
					// e.preventDefault();
					this.showPopup()
				}
			}
		},
		selectBlur () {
			this.outside()
		},
		clear () {
			this.times = ''
			this.$emit('on-clear')
		},
	},
}
</script>

<style scoped>

</style>
