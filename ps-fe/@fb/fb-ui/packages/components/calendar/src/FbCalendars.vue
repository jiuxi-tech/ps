<template>
	<div v-clickoutside="formatRange"
		 :class="[`${prefix}-calendars`, { position: inline, inline: showInline }]"
	>
		<template v-if="range">
			<div :class="`${prefix}-calendars-range`">
				<fb-calendar
					:value="dates"
					:format="vFormat"
					:mode="mode"
					:disabled-date="disabled"
					:show-right-arrow="showArrow"
					:show-time="showTime"
					@on-change="leftRangeChange"
					@onCurrentChange="leftCurrentChange"
					position="left"
				/>
				<fb-calendar
					:value="dates"
					:format="vFormat"
					:mode="mode"
					:show-left-arrow="showArrow"
					:disabled-date="disabled"
					:show-time="showTime"
					@on-change="rightRangeChange"
					@onCurrentChange="rightCurrentChange"
					position="right"
				/>
			</div>
		</template>
		<template v-else>
			<fb-calendar
				:value="dates"
				:format="vFormat"
				:mode="mode"
				:disabled-date="disabled"
				:show-time="showTime"
				@on-change="change"
			/>
		</template>
		<!--		v-if="$slots.addon || confirm"-->
		<div :class="[`${prefix}-calendars-footer`, footerClass]">
			<slot name="addon"/>


			<!--			v-if="confirm || showTimeBth"-->


			<div class="inner-footer">

				<div class="inner-footer__left">

					<template v-if="mode == 'single' && showFootLeftBtns">
						<!--						<fb-button-->
						<!--							type="primary"-->
						<!--							size="s"-->
						<!--							@on-click="setDate('today')"-->
						<!--						>今-->
						<!--						</fb-button>-->
						<!--						<fb-button-->
						<!--							type="primary"-->
						<!--							size="s"-->
						<!--							@on-click="setDate('yesterday')"-->
						<!--						>昨-->
						<!--						</fb-button>-->
						<!--						<fb-button-->
						<!--							type="primary"-->
						<!--							size="s"-->
						<!--							@on-click="setDate('oneweekago')"-->
						<!--						>一周前-->
						<!--						</fb-button>-->
						<fb-button
							v-for="btn in footLeftBtns"
							type="primary"
							size="s"
							@on-click="setDate(btn.name)"
							:key="btn.name"
							:disabled="btn.disabled"
						>
							{{ btn.text }}
						</fb-button>
					</template>

				</div>
				<div class="inner-footer__right">
					<!--				class="select-time"  :class="{ disabled: confirmDisabled }"-->
					<fb-button v-if="showTimeBth"
							   type="primary" size="s"
							   @on-click="onSelectTime"
							   :disabled="confirmDisabled"

					>
						{{ !showTime ? '显示时间' : '关闭时间' }}
					</fb-button>


					<fb-button v-if="confirm"
							   type="primary" size="s"
							   :disabled="confirmDisabled"
							   @on-click="finished">
						确定
					</fb-button>

				</div>


			</div>
		</div>
	</div>
</template>

<script>
/*!
 * FbCalendars
 * (c) 2020 lincong1987
 */

import clickoutside from '../../../directives/clickoutside'
import { isEmpty, timeFormat, contrastDate, isSameDay, isEqual } from '../../../utils/datetime'
import { isArray } from 'lodash-es'
import { isDate, isFunction, isObject } from '../../../utils/utils'
import dayjs from 'dayjs'
import FbCalendar from './FbCalendar'
import FbButton from '../../button/src/FbButton'

import { prefix } from '../../../../src/config'

export default {
	name: 'FbCalendars',

	components: {
		FbButton,
		FbCalendar,
	},

	directives: {clickoutside},

	props: {
		value: {

			type: [Date, Array, Number, String],
			default: null,
		},
		mode: {
			default: 'single',
			validator (value) {
				return ['single', 'multiple', 'range'].indexOf(value) !== -1
			},
		},
		showConfirm: {
			type: Boolean,
			default: false,
		},
		showInline: {
			type: Boolean,
			default: true,
		}, // 兼容老的 API
		inline: {
			type: Boolean,
			default: true,
		},
		enableTime: {
			type: Boolean,
			default: false,
		},
		enableSeconds: {
			type: Boolean,
			default: false,
		},
		onlyMonth: {
			type: Boolean,
			default: false,
		},
		format: {
			type: String,
			default: 'YYYY-MM-DD',
		},
		maxDate: {
			type: Date,
			default: null,
		},
		minDate: {
			type: Date,
			default: null,
		},
		maxRange: {
			// 7D七天， 1M一个月， 2Y两年
			type: String,
			default: undefined,
		},
		disable: {
			// 禁用的时间
			type: Array,
			default () {
				return []
			},
		},
		enable: {
			// 允许的时间
			type: Array,
			default () {
				return []
			},
		},
		disabledDate: {
			type: Function,
			default: () => false,
		},
		showFootLeftBtns: {
			type: [Array],
			default: () => ['today', 'yesterday', 'oneweekago'],
		},
	},
	leftTime: null,
	rightTime: null,
	data () {
		const nowDate = new Date()
		return {
			prefix,
			dates: null,
			showArrow: false,
			showTime: false,
			footLeftArr: [
				{
					name: 'today',
					text: '今',
					dateTime: nowDate.getTime()
				},
				{
					name: 'yesterday',
					text: '昨',
					dateTime: nowDate.setDate(nowDate.getDate() - 1)
				},
				{
					name: 'oneweekago',
					text: '一周',
					dateTime: nowDate.setDate(nowDate.getDate() - 7)
				},
			],
		}
	},
	computed: {
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
			return this.format
		},
		range () {
			return this.mode === 'range'
		},
		showTimeBth () {
			return this.vFormat.indexOf('H') !== -1
		},
		confirm () {
			return this.showConfirm || this.vFormat.indexOf('H') !== -1 || this.mode === 'multiple'
		},
		confirmDisabled () {
			if (!this.range) {
				return !this.dates || this.dates.length === 0
			}
			return !(this.dates[0] && this.dates[1])
		},
		footerClass () {
			// 判断单日历 无日期 hidden 掉 inner-footer
			let str = this.format.toLowerCase()
			if (!this.confirm && !this.showTimeBth && this.showFootLeftBtns.length == 0) {
				return 'hidden'
			}

			return ''
		},
		footLeftBtns () {
			let minDateTime = new Date(this.minDate).getTime()
			let maxDateTime = new Date(this.maxDate).getTime()

			let arr = this.footLeftArr.map(item => {
				let disabled = false
				if (minDateTime > 0 && item.dateTime < minDateTime) {
					disabled = true
				}
				if (maxDateTime > 0 && item.dateTime > maxDateTime) {
					disabled = true
				}
				if (this.disable.length > 0) {
					disabled = this.isTagTime(this.disable, item.dateTime, 'YYYY-MM-DD')
				}
				if (this.enable.length > 0) {
					disabled = !this.isTagTime(this.enable, item.dateTime, 'YYYY-MM-DD')
				}
				return {
					...item,
					disabled
				}
			}).filter(item => {
				return this.showFootLeftBtns.indexOf(item.name) !== -1
			})
			return arr
		},
	},
	watch: {
		value () {
			const dates = this.get()
			if (!isEqual(this.mode, this.value, dates)) {
				this.valueToDate(this.value)
			}
		},
	},
	created () {
		this.valueToDate(this.value)

	},

	methods: {
		timeFormat (date, format) {
			return timeFormat(date, (format || this.vFormat || this.format))
		},
		get () {
			if (isArray(this.dates)) {
				return this.dates[0] ? this.dates.map(item => item.getTime()) : null
			}
			return this.dates ? this.dates.getTime() : null
		},
		valueToDate (val) {

			if (this.mode === 'single') {
				//this.dates = val ? new Date(val) : null
				if (val == null) {
					return null
				}
				this.dates = dayjs(val, this.format).toDate()
			} else if (!isEmpty(val)) {
				if (val == null) {
					return null
				}
				this.dates = val.map(item => {
					return dayjs(item, this.format).toDate()
					// return new Date(item)
				})
			} else {
				// 使用 [null, null] 的原因是：
				// 1. 为了统一数据结构为数组。
				// 2. 用 null 填充数组是为了后续的 splice 操作，确保大的时间在右边
				this.dates = this.mode === 'range' ? [null, null] : []
			}
		},
		formatRangeStartTime (time, format) {
			const is = c => (format || this.vFormat).indexOf(c) !== -1
			const year = time.getFullYear()
			const month = time.getMonth()
			const day = time.getDate()
			const hour = time.getHours()
			const minute = time.getMinutes()

			if (is('s')) {
				return new Date(time)
			}
			if (is('m') && (is('h') || is('H'))) {
				return new Date(year, month, day, hour, minute, 0)
			}
			if (is('D')) {
				return new Date(year, month, day, 0, 0, 0)
			}
			if (is('M')) {
				return new Date(year, month, 1, 0, 0, 0)
			}
			if (is('Y')) {
				return new Date(year, 0, 1, 0, 0, 0)
			}
			return new Date(time)
		},
		formatRangeEndTime (time, format) {
			const is = c => (format || this.vFormat).indexOf(c) !== -1

			const newTime = new Date(time)
			if (is('s')) {
				return newTime
			}
			if (is('m') && (is('h') || is('H'))) {
				return new Date(newTime.getTime() + 59 * 1000)
			}
			if (is('D')) {
				return new Date(newTime.getTime() + 60 * 60 * 24 * 1000 - 1000)
			}
			if (is('M')) {
				newTime.setMonth(newTime.getMonth() + 1)
				return new Date(newTime.getTime() - 1000)
			}
			if (is('Y')) {
				newTime.setFullYear(newTime.getFullYear() + 1)
				return new Date(newTime.getTime() - 1000)
			}
			throw new Error(`Error time format: ${this.vFormat}`)
		},
		resetRangeDates () {
			if (this.rangeIsComplete()) {
				this.dates = [null, null]
			}
		},
		rangeIsComplete () {
			return this.dates && this.dates[0] && this.dates[1]
		},
		leftRangeChange (date, isTime, isOut) {
			if (isOut) {
				this.rightRangeChange(date, isTime)
				return
			}
			if (isTime) {
				this.dates.splice(0, 1, date)
			} else {
				this.resetRangeDates()
				const dates = this.dates
				if (isEmpty(dates) || !this.dates[0]) {
					dates.splice(0, 1, date)
				} else if (dates[0] > date) {
					dates.splice(1, 1, this.formatRangeEndTime(dates[0]))
					dates.splice(0, 1, date)
				} else {
					dates.splice(1, 1, this.formatRangeEndTime(date))
				}
			}

			this.rangeChange(date, isTime)
		},
		rightRangeChange (date, isTime, isOut) {
			if (isOut) {
				this.leftRangeChange(date, isTime)
				return
			}
			if (isTime) {
				this.dates.splice(1, 1, this.formatRangeEndTime(date))
			} else {
				this.resetRangeDates()
				const dates = this.dates
				if (isEmpty(dates) || !this.dates[1]) {
					dates.splice(1, 1, this.formatRangeEndTime(date))
				} else if (dates[1] > date) {
					dates.splice(0, 1, date)
				} else {
					dates.splice(0, 1, this.formatRangeStartTime(dates[1]))
					dates.splice(1, 1, this.formatRangeEndTime(date))
				}
			}

			this.rangeChange(date, isTime)
		},
		rangeChange (date, isTime) {
			if (this.rangeIsComplete()) {
				if (contrastDate(this.dates[0], this.dates[1], this.vFormat) === 1) {
					if (isTime && isSameDay(this.dates[0], this.dates[1])) {
						// 同年同月同日 只是时间不同
						this.dates.splice(1, 1, this.formatRangeEndTime(this.dates[0]))
					} else {
						const [start, end] = this.dates
						this.dates = [this.formatRangeStartTime(end), this.formatRangeEndTime(start)]
					}
				}
				this.noticeParent(date, isTime)
			}
		},
		change (date, isTime) {

			if (this.mode === 'single') {
				if (!this.value || !this.dates || contrastDate(date, this.dates, this.vFormat) !== 0) {
					this.dates = date
					this.noticeParent(date, isTime)
				}
			} else if (this.mode === 'multiple') {
				if (isTime) {
					this.dates.pop()
					this.dates.push(date)
				} else {
					const has = this.dates.findIndex(item => contrastDate(item, date, this.vFormat) === 0)
					// 删除
					if (has !== -1) {
						this.dates.splice(has, 1)
					} else {
						this.dates.push(date)
					}
				}
				this.noticeParent(date, isTime)
			}
		},
		noticeParent (date, isTime) {

			let dates = this.get()

			if (this.mode === 'single') {
				dates = this.timeFormat(dates)

			}
			if (this.mode === 'range') {
				dates = this.dates.map(date => this.timeFormat(date))
			}
			if (this.mode === 'multiple') {
				dates = this.dates.map(date => this.timeFormat(date))
			}

			// console.log('FbDatepicker#change', dates, this.dates)

			this.$emit('input', dates)
			this.$emit('on-change', dates, date)
			if (!isTime && !this.confirm) {
				this.finished()
			}
		},
		beyondTimeScope (min, max, time, format) {
			return contrastDate(time, min, format) === -1 || contrastDate(time, max, format) === 1
		},
		isTagTime (data, time, format) {
			const len = data.length
			if (len > 0) {
				for (let i = 0; i < len; i++) {
					const item = data[i]
					if (isDate(item)) {
						return contrastDate(item, time, format) === 0
					}
					if (isFunction(item)) {
						return item(time, format)
					}
					if (isObject(item) && item.from && item.to && isDate(item.from) && isDate(item.to)) {
						if (!this.beyondTimeScope(item.from, item.to, time, format)) {
							return true
						}
					}
				}
			}
			return false
		},
		maxRangeDisabled (time, format) {
			if (this.mode === 'range' && this.maxRange && this.dates) {
				const [start, end] = this.dates
				if (start && end) return false
				let flagDate = start || end
				const arr = this.maxRange.match(/(\d*)([MDY])/)
				if (flagDate && arr) {
					const length = Number(arr[1]) - 1
					const type = arr[2]
					flagDate = new Date(flagDate.getFullYear(), flagDate.getMonth(), flagDate.getDate())
					let minDate = new Date(flagDate)
					let maxDate = new Date(flagDate)
					if (type === 'D') {
						minDate.setDate(minDate.getDate() - length)
						maxDate.setDate(maxDate.getDate() + length)
						maxDate = this.formatRangeEndTime(maxDate, 'YYYY-MM-DD')
					} else if (type === 'M') {
						minDate.setMonth(minDate.getMonth() - length)
						maxDate.setMonth(maxDate.getMonth() + length)
						minDate = new Date(minDate.getFullYear(), minDate.getMonth())
						maxDate =
							this.formatRangeEndTime(new Date(maxDate.getFullYear(), maxDate.getMonth()), 'YYYY-MM')
					} else if (type === 'Y') {
						minDate = new Date(minDate.getFullYear() - length, 0)
						maxDate = this.formatRangeEndTime(new Date(maxDate.getFullYear() + length, 0), 'YYYY')
					}
					return this.beyondTimeScope(minDate, maxDate, time, format)
				}
			}
			return false
		},
		disabled (time, format) {
			const min = this.minDate && contrastDate(time, this.minDate, format) === -1
			const max = this.maxDate && contrastDate(time, this.maxDate, format) === 1
			let enable = false
			if (this.enable.length > 0) {
				enable = !this.isTagTime(this.enable, time, format)
			}

			return min || max || this.isTagTime(this.disable, time, format) || enable ||
				this.maxRangeDisabled(time, format) || this.disabledDate(time, format)
		},
		finished () {
			this.showTime = false
			this.$emit('on-finish')
		},
		is (c) {
			return this.vFormat.indexOf(c) !== -1
		},
		currentChange () {
			const {
				leftTime,
				rightTime,
			} = this.$options
			if (leftTime && rightTime) {
				if (this.is('D')) {
					const monthDiff = Math.abs(rightTime.month - leftTime.month)
					if ((rightTime.year === leftTime.year && (monthDiff === 1 || monthDiff === 11)) ||
						(rightTime.year - leftTime.year === 1 && monthDiff === 11)) {
						this.showArrow = false
					} else {
						this.showArrow = true
					}
				} else if (this.is('M')) {
					if (Math.abs(rightTime.year - leftTime.year) <= 1) {
						this.showArrow = false
					} else {
						this.showArrow = true
					}
				} else if (Math.abs((rightTime.year / 10 | 0) - (leftTime.year / 10 | 0)) <= 1) {
					this.showArrow = false
				} else {
					this.showArrow = true
				}
			}
		},
		leftCurrentChange (leftTime) {
			this.$options.leftTime = leftTime
			this.currentChange()
		},
		rightCurrentChange (rightTime) {
			this.$options.rightTime = rightTime
			this.currentChange()
		},
		onSelectTime () {
			if (this.confirmDisabled) return
			this.showTime = !this.showTime
			this.$nextTick(() => {
				this.$emit('time-change')
			})
		}, // 当用户只选择一个日期，就关闭弹窗的时候，默认将时间补充为一个范围
		formatRange () {
			if (this.range) {
				if (this.dates[1] && !this.dates[0]) {
					const startTime = this.formatRangeStartTime(this.dates[1])
					this.dates.splice(0, 1, startTime)
					this.noticeParent(startTime, false)
				} else if (this.dates[0] && !this.dates[1]) {
					const changeDate = this.formatRangeEndTime(this.dates[0])
					this.dates.splice(1, 1, changeDate)
					this.noticeParent(changeDate, false)
				}
			}
		},
		setDate (dateStr) {

			var now = new Date()
			if (dateStr === 'today') {
			}

			if (dateStr === 'yesterday') {
				now.setDate(now.getDate() - 1)
			}

			if (dateStr === 'oneweekago') {
				now.setDate(now.getDate() - 7)
			}

			this.change(now)

		},
	},
	mounted () {
		if (this.showTimeBth && this.range && this.dates[0]) {
			this.showTime = true
		}

		if (this.range) {

		} else {

			if (this.value && this.showTimeBth) {
				this.showTime = true
			}

		}
	},
}
</script>

<style scoped>

</style>
