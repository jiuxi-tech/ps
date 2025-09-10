<template>
	<div :class="[
		`${prefix}-progress`,
		`${prefix}-progress-${type == 'dashboard' ? 'circle' : type}`,
		`${prefix}-progress-status-${status}`,
		{[`${prefix}-progress-show-info`]: showInfo && !textInside},
		`${prefix}-progress-default`,
	]" @click="onClick">
		<div v-if="type === 'line'" :class="`${prefix}-progress-line`">
			<div :class="`${prefix}-progress-outer`">
				<div :class="`${prefix}-progress-inner`" :style="getStrokeInnerStyle">
					<div :class="[`${prefix}-progress-bg`, {[`${prefix}-progress-bg-text__inside`]: textInside},]"
					     :style="strokeStyle">

						<div v-if="showInfo && textInside && !$slots.text" :class="`${prefix}-progress-inner__text`">
							{{ formatPercent }}
						</div>
						<div v-if="showInfo && textInside && $slots.text" :class="`${prefix}-progress-inner__text`">
							<slot name="text"></slot>
						</div>

						<div v-if="showInfo && textInside && $slots.innerRight"
						     :class="`${prefix}-progress-bg_right_tip`">
							<slot name="innerRight"></slot>
						</div>
					</div>
				</div>
			</div>

			<span v-if="showInfo && !$slots.text && !textInside && (status === 'normal' || status === 'active')"
			      :title="`${formatPercent}`"
			      :class="`${prefix}-progress-text`"
			      :style="progressTextStyle">
				{{ formatPercent }}
			</span>
			<span v-if="showInfo && !$slots.text && !textInside && status === 'success'" :title="`${formatPercent}`"
			      :class="`${prefix}-progress-text`" :style="progressTextStyle">
				<i :class="`${prefix}-icon ${prefix}-icon-selected-circle-fill`"></i>
			</span>
			<span v-if="showInfo && !$slots.text && !textInside && status === 'warning'" :title="`${formatPercent}`"
			      :class="`${prefix}-progress-text`" :style="progressTextStyle">
				<i :class="`${prefix}-icon ${prefix}-icon-information-fill`"></i>
			</span>
			<span v-if="showInfo && !$slots.text && !textInside && status === 'danger'" :title="`${formatPercent}`"
			      :class="`${prefix}-progress-text`" :style="progressTextStyle">
				<i :class="`${prefix}-icon ${prefix}-icon-close-circle-fill`"></i>
			</span>

			<div v-if="showInfo && !textInside && $slots.text" :class="`${prefix}-progress-text`"
			     :style="progressTextStyle">
				<slot name="text"></slot>
			</div>
		</div>

		<div v-else :class="`${prefix}-progress-inner`" :style="{height: width + 'px', width: width + 'px'}">
			<svg viewBox="0 0 100 100">
				<defs v-if="Object.prototype.toString.call(strokeColor) === '[object Object]'">
					<linearGradient :id="`${prefix}-progress-gradient-${generatorId}`" x1="100%" y1="0%" x2="0%"
					                y2="0%">
						<stop v-for="(sColor, skey) in strokeColor" :key="skey" :offset="skey"
						      :stop-color="sColor"></stop>
					</linearGradient>
				</defs>
				<path
					:class="`${prefix}-progress-${type}__track`"
					:d="trackPath"
					:stroke="strokeBgColor || '#e5e9f2'"
					:stroke-width="relativeStrokeWidth"
					fill="none"
					:style="trailPathStyle"></path>
				<path
					:class="`${prefix}-progress-${type}__path`"
					:d="trackPath"
					:stroke="stroke"
					fill="none"
					:stroke-linecap="strokeLinecap"
					:stroke-width="percent ? relativeStrokeWidth : 0"
					:style="circlePathStyle"></path>
			</svg>

			<span v-if="showInfo && !$slots.text && !textInside && (status === 'normal' || status === 'active')"
			      :title="`${formatPercent}`"
			      :class="`${prefix}-progress-text`"
			      :style="progressTextStyle">
				<fb-number :end="percent" append="%" :duration="4000" :decimals="decimals"></fb-number>
			</span>
			<span v-if="showInfo && !$slots.text && !textInside && status === 'success'" :title="`${formatPercent}`"
			      :class="`${prefix}-progress-text`" :style="progressTextStyle">
				<i :class="`${prefix}-icon ${prefix}-icon-selected-circle-fill`"></i>
			</span>
			<span v-if="showInfo && !$slots.text && !textInside && status === 'warning'" :title="`${formatPercent}`"
			      :class="`${prefix}-progress-text`" :style="progressTextStyle">
				<i :class="`${prefix}-icon ${prefix}-icon-information-fill`"></i>
			</span>
			<span v-if="showInfo && !$slots.text && !textInside && status === 'danger'" :title="`${formatPercent}`"
			      :class="`${prefix}-progress-text`" :style="progressTextStyle">
				<i :class="`${prefix}-icon ${prefix}-icon-close-circle-fill`"></i>
			</span>

			<div v-if="showInfo && !textInside && $slots.text" :class="`${prefix}-progress-text`"
			     :style="progressTextStyle">
				<slot name="text"></slot>
			</div>
		</div>
	</div>
</template>

<script>
import { prefix } from '../../../../src/config'
import { isString, isObject, isFunction, isArray } from 'lodash-es'
import FbNumber from '../../number/src/FbNumber'

/**
 * FbDot
 * (c) 2020 lincong1987
 */
export default {
	name: 'FbProgress',
	components: {FbNumber},
	props: {
		type: {
			type: [String],
			default: 'line',
		},
		format: {
			type: [Function],
		},
		percent: {
			type: [Number, String],
			default: 0,
		},
		decimals: {
			type: [Number, String],
			default: 0,
		},
		showInfo: {
			type: [Boolean],
			default: true,
		},
		status: {
			type: [String],
			default: 'normal',
		},
		strokeWidth: {
			type: [String, Number],
			default: 10,
		},
		strokeLinecap: {
			type: String,
			default: 'round',
		},
		width: {
			type: Number,
			default: 126,
		},
		strokeColor: {
			type: [String, Object, Array, Function],
			default: '',
		},
		strokeBgColor: {
			type: [String],
			default: '',
		},
		textInside: {
			type: [Boolean],
			default: false,
		},
		textStyle: {
			type: [Object],
		},
		borderRadius: {
			type: [String, Number],
			default: 0,
		},
		innerStyle: {
			type: [Object],
		}
	},

	data () {
		return {
			prefix,
			generatorId: Math.floor(Math.random() * 10000),
		}
	},
	computed: {
		formatPercent () {
			let num = parseFloat(this.percent)
			if (num > 100) {
				num = 100
			}
			if (typeof this.format === 'function') {
				return this.format(this.percent) || ''
			} else {
				return num + '%'
			}
		},
		strokeStyle () {
			let num = parseFloat(this.percent)
			if (num > 100) {
				num = 100
			}
			let styles = [
				{
					width: num + '%',
					height: this.strokeWidth + 'px',
				},
			]

			if (this.strokeColor) {
				styles.push({background: this.getCurrentColor(this.percent)})
			}
			if (this.borderRadius) {
				styles.push({borderRadius: this.borderRadius + 'px'})
			}

			return styles
		},
		getStrokeInnerStyle () {
			let styles = []
			if (this.strokeBgColor) {
				styles.push({background: this.strokeBgColor})
			}
			if (this.borderRadius) {
				styles.push({borderRadius: this.borderRadius + 'px'})
			}
			if (this.innerStyle) {
				styles.push(this.innerStyle)
			}
			return styles
		},
		progressTextStyle () {
			let styles = []
			if (isObject(this.textStyle)) {
				styles.push(this.textStyle)
			}
			return styles
		},
		// 环形判断
		relativeStrokeWidth () {
			return (this.strokeWidth / this.width * 100).toFixed(1)
		},
		radius () {
			if (this.type === 'circle' || this.type === 'dashboard') {
				return parseInt(50 - parseFloat(this.relativeStrokeWidth) / 2, 10)
			} else {
				return 0
			}
		},
		trackPath () {
			const radius = this.radius
			const isDashboard = this.type === 'dashboard'
			return `
				  M 50 50
				  m 0 ${isDashboard ? '' : '-'}${radius}
				  a ${radius} ${radius} 0 1 1 0 ${isDashboard ? '-' : ''}${radius * 2}
				  a ${radius} ${radius} 0 1 1 0 ${isDashboard ? '' : '-'}${radius * 2}
				  `
		},
		perimeter () {
			return 2 * Math.PI * this.radius
		},
		rate () {
			return this.type === 'dashboard' ? 0.75 : 1
		},
		strokeDashoffset () {
			const offset = -1 * this.perimeter * (1 - this.rate) / 2
			return `${offset}px`
		},
		trailPathStyle () {
			return {
				strokeDasharray: `${(this.perimeter * this.rate)}px, ${this.perimeter}px`,
				strokeDashoffset: this.strokeDashoffset,
			}
		},
		circlePathStyle () {
			return {
				strokeDasharray: `${this.perimeter * this.rate * (this.percent / 100)}px, ${this.perimeter}px`,
				strokeDashoffset: this.strokeDashoffset,
				transition: 'stroke-dasharray 0.6s ease 0s, stroke 0.6s ease',
			}
		},
		stroke () {
			let color = '#0284FE'
			if (this.strokeColor) {
				color = this.getCurrentColor(this.percent)
			} else {
				color = '#0284FE'
				if (this.status == 'success') {
					color = '#56D100'
				} else if (this.status == 'warning') {
					color = '#FFB500'
				} else if (this.status == 'danger') {
					color = '#FB544E'
				}
			}
			// console.log(color)
			return color
		},
	},

	watch: {},

	methods: {
		onClick (e) {
			this.$emit('on-click', e)
		},
		getCurrentColor (percent) {
			percent = parseFloat(percent)
			if (isString(this.strokeColor)) {
				return this.strokeColor
			} else if (isArray(this.strokeColor)) {
				return this.getLevelColor(percent)
			} else if (isFunction(this.strokeColor)) {
				return this.strokeColor(percent)
			} else if (Object.prototype.toString.call(this.strokeColor) === '[object Object]') {
				let str = ''
				for (let key in this.strokeColor) {
					let val = this.strokeColor[key]
					str += val + ', '
				}
				let colorStr = str.substr(0, str.lastIndexOf(','))
				let color = `linear-gradient(to right, ${colorStr})`

				if (this.type == 'circle' || this.type == 'dashboard') {
					color = `url(#${prefix}-progress-gradient-${this.generatorId})`
				}

				return color
			}
		},
		getLevelColor (percent) {
			const colorArray = this.getColorArray().sort((a, b) => a.percent - b.percent)
			for (let i = 0; i < colorArray.length; i++) {
				if (colorArray[i].percent > percent) {
					return colorArray[i].color
				}
			}
			return colorArray[colorArray.length - 1].color
		},
		getColorArray () {
			const color = this.strokeColor
			const span = 100 / color.length
			return color.map((seriesColor, index) => {
				if (typeof seriesColor === 'string') {
					return {
						color: seriesColor,
						percent: (index + 1) * span,
					}
				}
				return seriesColor
			})
		},
	},

	mounted () {

	},

	destroyed () {

	},
}
</script>

<style scoped>

</style>
