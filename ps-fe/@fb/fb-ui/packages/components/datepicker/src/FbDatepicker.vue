<template>
	<div ref="reference" v-clickoutside="hidePopup" :class="`${prefix}-datepicker ${prefix}-datepicker--${size}`">
		<fb-popup-header
			:class="mode"
			:value="text"
			:icon="'calendar'"
			:placeholder="placeholder"
			:disabled="disabled"
			:readonly="readonly"
			:clearable="clearable"
			@on-focus="$emit('on-focus', $event)"
			@on-blur="onBlur"
			@on-clear="clear"
			@on-click="showPopup"
			@on-icon-click="showPopup"
			@on-keydown="keydown"
		>
		            <span v-if="mode === 'range' && text.length > 0">
		                <span>{{ text[0] }}</span>
			            <span class="range-separator">{{ rangeSeparator }}</span>
			            <span>{{ text[1] }}</span>
		            </span>
		</fb-popup-header>

		<!--		<fb-popup-picker v-model="show" :position="position">-->
		<transition name="zoom-in-top">
			<div ref="popper" v-show="show" :class="[`${prefix}-popper-no-arrow`]">
				<div :class="[`${prefix}-datepicker-dropdown`]">
					<fb-calendars
						v-model="dates"
						:mode="mode"
						:format="format"
						:disabled-date="disabledDate"
						:enable-time="enableTime"
						:enable-seconds="enableSeconds"
						:only-month="onlyMonth"
						:show-confirm="showConfirm"
						:show-inline="false"
						:min-date="minDate"
						:max-date="maxDate"
						:max-range="maxRange"
						:disable="disable"
						:enable="enable"
						:showFootLeftBtns="showFootLeftBtns"
						@on-change="change"
						@on-finish="finish"
						@time-change="updatePopper"
					>
						<template v-slot:addon>
							<slot name="addon"/>
						</template>
					</fb-calendars>
				</div>
			</div>
		</transition>
	</div>
</template>

<script>
/*!
 * FbDatepicker
 * (c) 2020 lincong1987
 */

import { prefix } from '../../../../src/config'
import clickoutside from '../../../directives/clickoutside'
import { closest } from '../../../utils/componentUtils'
import { isEmpty, timeFormat, isEqual } from '../../../utils/datetime'
import keyCode from '../../../utils/keyCode'
import FbPopupHeader from '../../popup/popup-header/src/FbPopupHeader'
import FbCalendars from '../../calendar/src/FbCalendars'
import PopperMixin from '../../../utils/popper-mixin'

export default {
	name: 'FbDatepicker',

	components: {
		FbPopupHeader,
//		FbPopupPicker,
		FbCalendars,
	},

	mixins: [PopperMixin],

	directives: {
		clickoutside,
	},

	props: {
		//
		value: {
			type: [Date, Array, Number, String],
			default: null,
		},
		// 禁用
		disabled: {
			type: Boolean,
			default: false,
		},
		//只读
		readonly: {
			type: Boolean,
			default: false,
		},
		// 面板显示位置
		position: {
			type: String,
			default: null,
		},

		// 模式 单选 多选 区间
		mode: {
			default: 'single',
			validator (value) {
				// 这个值必须匹配下列字符串中的一个
				return ['single', 'multiple', 'range'].indexOf(value) !== -1
			},
		},
		// 显示确认按钮, 要点击确认才会关掉面板
		showConfirm: {
			type: Boolean,
			default: false,
		},

		// 区间选择时显示的分隔字符
		rangeSeparator: {
			type: String,
			default: '~',
		},
		// 显示清除图标
		clearable: {
			type: Boolean,
			default: false,
		},
		// 占位符
		placeholder: {
			type: String,
			default: '请选择日期',
		},
		// 格式化 pattern
		format: {
			type: String,
			default: 'YYYY-MM-DD',
		},

		// 启用时间面板
		enableTime: {
			type: Boolean,
			default: false,
		},

		// 启用秒
		enableSeconds: {
			type: Boolean,
			default: false,
		},

		// 只显示月份
		onlyMonth: {
			type: Boolean,
			default: false,
		},

		// 能选的最小日期
		minDate: {
			type: Date,
			default: undefined,
		},
		// 能选的最大日期
		maxDate: {
			type: Date,
			default: undefined,
		},
		// 7D 七天， 1M 一个月， 2Y 两年
		// 以当前日期为中心,向左右延伸可选日期
		maxRange: {
			type: String,
			default: undefined,
		},
		// 禁用的时间, 数组
		disable: {
			type: Array,
			default () {
				return []
			},
		},
		// 允许的时间
		enable: {
			type: Array,
			default () {
				return []
			},
		},
		// 禁用的日期, 见demo
		disabledDate: {
			type: Function,
			default: () => false,
		},
		showFootLeftBtns: {
			type: [Array],
		},

		size:{
			type: String,
		}
	},

	data () {

		return {
			prefix,
			show: false,
			dates: this.value,
		}
	},

	created () {
		this.fbFormItem = closest(this, 'FbFormItem')
	},

	beforeDestroy () {
		this.fbFormItem = null
	},

	computed: {
		text () {

			const dates = this.dates
			if (isEmpty(dates)) {
				// 空字符串时，默认为null
				if (this.mode === 'single' && typeof this.dates === 'string') {
					this.dates = null
				}
				return ''
			}
			if (this.mode === 'single') {
				return dates
			}
			if (this.mode === 'range') {
				return dates.map(date => this.timeFormat(new Date(date)))
			}
			return dates.map(date => this.timeFormat(new Date(date))).join(', ')
		},

		vFormat () {
			if (this.onlyMonth) {
				return 'YYYY-MM'
			}
			if (this.enableTime) {
				if (this.enableSeconds) {
					return 'YYYY-MM-DD HH:mm:ss'
				}
				return 'YYYY-MM-DD HH:mm'
			}

			// 如果没有年月日
//				if (this.format.indexOf('YYYY') != -1
//					&& this.format.indexOf('MM') != -1
//					&& this.format.indexOf('DD') != -1) {
//					this.format = 'YYYY-MM-DD ' + this.format
//				}

			return this.format
		},
	},
	watch: {
		value (value) {

			if (!isEqual(this.mode, value, this.dates)) {
				this.change(value)
			}
		},
	},
	methods: {

		timeFormat (date, format) {
			return timeFormat(date, (format || this.vFormat || this.format))
		},

		hidePopup () {
			if (this.show) {
				this.show = false
				this.fbFormItem && this.fbFormItem.$emit('on-form-blur', this.dates)
			}
		},
		showPopup () {
			if (!(this.readonly || this.disabled)) {
				this.show = true
				this.updatePopper()
			}
		},
		keydown (e) {
			if (e.keyCode === keyCode.ENTER || e.keyCode === keyCode.MAC_ENTER) {
				this.$emit('on-enter', e)
			} else if (!this.show) {
				if (e.keyCode === keyCode.SPACE || e.keyCode === keyCode.TAB) {
					e.preventDefault()
					this.showPopup()
				}
			}
		},
		clear () {
			this.$emit('on-clear')
			this.dates = null
			this.$emit('input', this.mode === 'single' ? null : [])
		},
		change (dates, time) {

			if (this.mode === 'single') {

				if (typeof dates === 'string') {
					this.dates = dates
				} else {
					this.dates = this.timeFormat(dates)
				}

			}
			if (this.mode === 'range') {
				this.dates = dates.map(date => {
					if (typeof date === 'string') {
						return date
					} else {
						return this.timeFormat(date)
					}
				})
			}
			if (this.mode === 'multiple') {
				this.dates = dates.map(date => {
					if (typeof date === 'string') {
						return date
					} else {
						return this.timeFormat(date)
					}
				})
			}

			// console.log('FbDatepicker#change', this.dates)

			this.$emit('input', dates, time)
			this.$emit('on-input', dates, time)
			this.$emit('on-change', dates, time)
			this.$emit('on-input-change', dates, time)
			this.fbFormItem && this.fbFormItem.$emit('on-form-change', dates)
		},
		onBlur (event) {
			this.$emit('on-blur', event)
			// this.hidePopup();
		},
		finish () {
			this.hidePopup()
			this.$emit('on-finish')
		},
	},
}
</script>

<style scoped>

</style>
