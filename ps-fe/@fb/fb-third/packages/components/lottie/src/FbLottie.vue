<template>
	<div :style="getStyle" ref="el"
		 @click="handleClick"
		 @mouseenter="handleMouseEnter"></div>
</template>

<script>
/**
 * FbLottie
 * (c) 2022 lincong1987
 */

import lottie from 'lottie-web'

export default {
	name: 'FbLottie',
	props: {
		/**
		 * 渲染器类型
		 * 可选 svg / canvas / html
		 */
		renderer: {
			default: 'svg',
			type: String,
		},
		/**
		 * 循环播放
		 */
		loop: {
			type: Boolean,
			default: true,
		},
		/**
		 * 自动播放
		 */
		autoPlay: {
			type: Boolean,
			default: true,
		},
		/**
		 * 事件播放循环
		 */
		manualLoop: {
			type: Boolean,
			default: true,
		},
		/**
		 * 通过点击播放
		 */
		playWithClick: {
			type: Boolean,
			default: false,
		},
		/**
		 * 通过鼠标移入播放
		 */
		playWhenMouseEnter: {
			type: Boolean,
			default: false,
		},
		/**
		 * 动画名称
		 */
		name: {
			type: Object,
			default () {
				return {}
			},
		},
		/**
		 * 动画数据
		 */
		data: {
			type: [Object, String],
			default () {
				return {}
			},
		},
		/**
		 * 动画路径
		 */
		path: {
			type: Object,
			default () {
				return {}
			},
		},
		/**
		 * 方向
		 * 1：正向 / -1：反向
		 */
		direction: {
			default: 1,
			type: Number,
		},
		/**
		 * 速度
		 */
		speed: {
			default: 1,
			type: Number,
		},

		width: {
			default: 100,
			type: Number,
		},
		height: {
			default: 100,
			type: Number,
		},
	},
	data () {
		return {}
	},
	computed: {
		getStyle () {
			let style = {
				width: `${this.width}px`,
				height: `${this.height}px`,
			}

			return style
		},
	},

	watch: {
		data () {
			this.init()
		},
	},
	created () {
		this.lottieInstance = null

		this.$nextTick(() => {
			this.init()
		})
	},

	beforeDestroy () {
		this.destroy()
	},
	methods: {

		init () {
			this.destroy()
			this.lottieInstance = lottie.loadAnimation({
				container: this.$refs.el,
				renderer: this.renderer,
				name: this.name,
				loop: this.loop,
				autoplay: this.autoPlay,
				animationData: typeof this.data === 'string' ? JSON.parse(this.data) : this.data,
				path: this.path,
			})
			if (typeof this.speed === 'number') {
				this.setSpeed(this.speed)
			} else {
				this.setSpeed(1)
			}
			if (typeof this.direction === 'number') {
				this.setDirection(this.direction)
			} else {
				this.setDirection(1)
			}
			this.lottieInstance.onComplete = () => {
				// 非循环动画执行一次触发
				this.$emit('on-complete')
				if (this.manualLoop) {
					this.stop()
				}
			}
			this.lottieInstance.onLoopComplete = () => {
				// 循环动画每执行一次触发
				this.$emit('on-loop-complete')
			}
			this.lottieInstance.onEnterFrame = () => {
				// 动画准备完毕
				this.$emit('on-enter-frame')
			}
			this.lottieInstance.onSegmentStart = () => {
				// 	每帧执行
				this.$emit('on-segment-start')
			}
		},

		handleClick () {
			if (this.playWithClick) {
				this.play()
			}
		},
		handleMouseEnter () {
			if (this.playWhenMouseEnter) {
				this.play()
			}
		},

		stop () {
			this.lottieInstance.stop()
		},
		play () {
			this.lottieInstance.play()
		},
		pause () {
			this.lottieInstance.pause()
		},
		setSpeed (num) {
			this.lottieInstance.setSpeed(num)
		},
		setDirection (num) {
			this.lottieInstance.setDirection(num)
		},
		goToAndStop (num, type) {
			this.lottieInstance.goToAndStop(num, type)
		},
		goToAndPlay (num, type) {
			this.lottieInstance.goToAndPlay(num, type)
		},
		playSegments (arr, type) {
			this.lottieInstance.playSegments(arr, type)
		},
		destroy () {
			this.lottieInstance && this.lottieInstance.destroy && this.lottieInstance.destroy()
		},
	},
}
</script>

<style lang="less" scoped>

</style>
