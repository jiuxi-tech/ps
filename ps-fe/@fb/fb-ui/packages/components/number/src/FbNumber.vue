<template>
	<span @click="handleClick" :class="[
	  `${prefix}-number` ,
	]">
		 {{ !noPlay ? myValue : end }}
	</span>
</template>

<script>
import { prefix } from '../../../../src/config'
import { filter, debounce } from 'lodash-es'

const requestFrame = (function () {
	const raf = window.requestAnimationFrame
		|| window.mozRequestAnimationFrame
		|| window.webkitRequestAnimationFrame
		|| function fallbackRAF (func) {
			return setTimeout(() => {
				func && func()
			}, 20)
		}
	return function requestFrameFunction (func) {
		return raf(func)
	}
}())

const cancelFrame = (function () {
	const cancel = window.cancelAnimationFrame
		|| window.mozCancelAnimationFrame
		|| window.webkitCancelAnimationFrame
		|| window.clearTimeout
	return function cancelFrameFunction (id) {
		return cancel(id)
	}
}())

/**
 * FbNumber
 * (c) 2021 lincong1987
 */
export default {
	name: 'FbNumber',
	props: {
		// 开始数
		start: {
			type: [Number, String],
			default: 0,
		},
		// 结尾数
		end: {
			type: [Number, String],
			default: 0,
		},
		// 动画时长
		duration: {
			type: Number,
			default: 3000,
		},
		// 自动播放
		auto: {
			type: Boolean,
			default: true,
		},
		// 小数位数
		decimals: {
			type: Number,
			default: 0,
			validator (value) {
				return value >= 0
			},
		},
		// 小数点
		decimal: {
			type: String,
			default: '.',
		},
		// 千分位分隔符
		separator: {
			type: String,
			default: ',',
		},
		// 前缀
		prepend: {
			type: String,
			default: '',
		},
		// 后缀
		append: {
			type: String,
			default: '',
		},
		useQueue: {
			type: Boolean,
			default: true,
		},
		// 缓动函数
		useEasing: {
			type: Boolean,
			default: true,
		},
		easingFn: {
			type: Function,
			default (t, b, c, d) {
				return c * (-Math.pow(2, -10 * t / d) + 1) * 1024 / 1023 + b
			},
		},
		formatter: {
			type: Function,
			default: undefined,
		},
		// 是否动画
		noPlay: {
			type: Boolean,
			default: false,
		}
	},
	data () {
		return {
			prefix,
			myStart: parseFloat(this.start),
			myEnd: parseFloat(this.end),
			myValue: this.formatNumber(this.start),
			realVal: null,
			paused: false,
			myDuration: this.duration,
			startTime: null,
			timestamp: null,
			remaining: null,
			queue: [],
		}
	},
	computed: {
		stepDown () {
			return this.start > this.end
		},
	},
	watch: {
		start (val) {
			this.myStart = parseFloat(val)
			if (this.auto) {
				this.debouncePlay()
			}
		},
		end (val) {
			this.myEnd = parseFloat(val)
			if (this.auto) {
				this.debouncePlay()
			}
		},
	},
	created () {
		this.rAF = null
		this.queueTimer = null
		this.debouncePlay = debounce(this.play, 150)
	},
	beforeDestroy () {
		cancelFrame(this.rAF)
		clearTimeout(this.queueTimer)
		this.debouncePlay.cancel()
	},
	mounted () {
		if (this.auto) {
			this.debouncePlay()
		}
		this.$emit('on-mounted')
	},

	methods: {
		play () {

			if (!this.useQueue) {
				this.myStart = this.start
				this.startTime = null
				this.paused = false
				this.rAF = requestFrame(this.step)
				return
			}

			this.queue.push({
//				duration: this.myDuration,
				start: this.myStart,
				end: this.myEnd,

				fn: () => {
					this.myStart = this.start
					this.startTime = null
					this.paused = false
					this.rAF = requestFrame(this.step)
				},
			})

			this.doQueue()
		},

		doQueue () {
			if (this.queue.length === 0) {
				return
			}

//			this.myStart = this.queue[0].start
//			this.myEnd = this.queue[0].end
//			this.myDuration = this.queue[0].duration
			this.queue[0].fn()

			clearTimeout(this.queueTimer)
			this.queueTimer = setTimeout(() => {
				this.queue.splice(0, 1)
				this.doQueue()
			}, this.myDuration)
		},
		pauseResume () {
			if (this.paused) {
				this.resume()
				this.paused = false
			} else {
				this.pause()
				this.paused = true
			}
		},
		pause () {
			cancelFrame(this.rAF)
		},
		resume () {
			this.startTime = null
			this.myDuration = +this.remaining
			this.myStart = +this.realVal
			requestFrame(this.step)
		},
		reset () {
			this.startTime = null
			cancelFrame(this.rAF)
			this.myValue = this.formatNumber(this.play)
		},
		step (timestamp) {
			if (!this.startTime) this.startTime = timestamp
			this.timestamp = timestamp
			const progress = timestamp - this.startTime
			this.remaining = this.myDuration - progress
			if (this.useEasing) {
				if (this.stepDown) {
					this.realVal = this.myStart -
						this.easingFn(progress, 0, this.myStart - this.end, this.myDuration)
				} else {
					this.realVal = this.easingFn(progress, this.myStart, this.end - this.myStart,
						this.myDuration)
				}
			} else {
				if (this.stepDown) {
					this.realVal =
						this.myStart - ((this.myStart - this.end) * (progress / this.myDuration))
				} else {
					this.realVal =
						this.myStart + (this.end - this.myStart) * (progress / this.myDuration)
				}
			}
			if (this.stepDown) {
				this.realVal = this.realVal < this.end ? this.end : this.realVal
			} else {
				this.realVal = this.realVal > this.end ? this.end : this.realVal
			}
			this.myValue = this.formatNumber(this.realVal)
			if (progress < this.myDuration) {
				this.rAF = requestFrame(this.step)
			} else {
				this.$emit('on-step')
			}
		},
		isNumber (val) {
			return !isNaN(parseFloat(val))
		},
		formatNumber (num) {

			if (this.formatter) {
				return this.formatter(num)
			}

			if (num.toFixed) {
				num = num.toFixed(this.decimals)
			}

			num += ''
			const x = num.split('.')
			let x1 = x[0]
			const x2 = x.length > 1 ? this.decimal + x[1] : ''
			const rgx = /(\d+)(\d{3})/
			if (this.separator && !this.isNumber(this.separator)) {
				while (rgx.test(x1)) {
					x1 = x1.replace(rgx, '$1' + this.separator + '$2')
				}
			}
			return this.prepend + x1 + x2 + this.append
		},
		handleClick () {
			this.$emit('on-click')
		},
	},

}
</script>

<style scoped>

</style>
