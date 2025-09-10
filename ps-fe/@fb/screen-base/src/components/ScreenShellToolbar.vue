<template>
	<fb-container class="screen-shell-toolbar">
		<fb-container>
			<fb-space align="center">
				<fb-text>开发模式</fb-text>
				<fb-switch v-model="dev"></fb-switch>
				<fb-text v-show="dev">放大</fb-text>
				<fb-select v-show="dev"
				           style="width: 80px"
				           v-model="scale"
				           :data="scaleData"
				>
				</fb-select>
				<fb-text>适应模式</fb-text>
				<fb-select style="width: 90px"
				           v-model="mode"
				           :data="modeData"
				></fb-select>
				<fb-text>全屏</fb-text>
				<fb-switch v-model="fullscreen"></fb-switch>
			</fb-space>
		</fb-container>

		<fb-container mt="8px">
			<fb-space align="center">
				<fb-text>过场动画</fb-text>
				<fb-select
					style="width: 120px"
					v-model="transition"
					:data="transitionData"
				>
				</fb-select>
				<fb-text>主题</fb-text>
				<fb-select style="width: 90px"
				           v-model="theme"
				           :data="themeData"
						   :clearable="false"
				></fb-select>
			</fb-space>
		</fb-container>

	</fb-container>
</template>

<script>
/**
 * ScreenShellToolbar
 * (c) 2021 lincong1987
 */
import { throttle, debounce } from 'lodash-es'

export default {
	name: 'ScreenShellToolbar',

	props: {
		container: {
			type: [String, Object]
		},
		resizeFunction: {
			type: [Function],
			default: undefined
		}
	},

	data () {
		return {
			mode: this.$screenUtil.getMode(),
			dev: this.$screenUtil.getDev(),
			scale: this.$screenUtil.getScale(),
			fullscreen: this.$screenUtil.getFullscreen(),
			transition: this.$screenUtil.getTransition(),
			theme: this.$screenUtil.getTheme(),
			scaleData: this.$screenUtil.getScales(),
			modeData: this.$screenUtil.getModes(),
			transitionData: this.$screenUtil.getTransitions(),
			themeData: this.$screenUtil.getThemes(),
		}
	},

	watch: {
		dev (value) {
			this.$screenUtil.setDev(value)
		},
		scale (value) {
			this.$screenUtil.setScale(value)
		},
		mode (value) {
			this.$screenUtil.setMode(value)
			this.setMode(value)
		},
		fullscreen (value) {
			this.$screenUtil.setFullscreen(value)
		},
		transition: {
			handler (value) {
				this.$screenUtil.setTransition(value)
				this.$emit('on-change', 'transition', value)
			},
			immediate: true,
		},
		theme: {
			handler (value) {
				this.$screenUtil.setTheme(value)
			},
		},
	},

	created () {
		this.debounceScroll = debounce(this.scroll, 50)
		this.throttleCalcScale = throttle(this.calcScale, 160)
	},

	beforeDestroy () {
		window.removeEventListener('resize', this.throttleCalcScale, false)
		window.removeEventListener('scroll', this.debounceScroll, false)

		this.debounceScroll.cancel()
		this.throttleCalcScale.cancel()
	},

	mounted () {
		this.$screenUtil.on('screen.dev', (dev) => {
			this.dev = dev
		})
		this.$screenUtil.on('screen.scale', (scale) => {
			this.scale = scale
		})
		this.$screenUtil.on('screen.theme', (theme) => {
			this.theme = theme
		})
		this.$screenUtil.on('screen.transition', (transition) => {
			this.transition = transition
		})
		this.$screenUtil.on('screen.fullscreen', (fullscreen) => {
			this.fullscreen = fullscreen
		})
		this.$screenUtil.on('screen.mode', (mode) => {
			this.mode = mode
		})


		let {
			$,
			mode,
			setMode,
			throttleCalcScale,
			debounceScroll,
		} = this

		setMode(mode)

		window.addEventListener('resize', throttleCalcScale, false)

		// 刷新滚动条记录
		this.$nextTick(() => {

			throttleCalcScale()

			setTimeout(() => {
				let scroll = this.$datax.get('scroll')
				if (scroll) {
					window.scrollTo(scroll.x || 0, scroll.y || 0)
				}

				window.addEventListener('scroll', debounceScroll, false)

			}, 160)
		})

		// 此事件监听不到 F11 全屏
		// document.onfullscreenchange = (event) => {
		// 	this.fullscreen = this.$screenUtil.isFullscreen()
		// }
	},

	methods: {

		scroll () {
			this.$datax.set('scroll', {
				x: window.scrollX,
				y: window.scrollY,
			})
		},

		setMode (mode) {
			this.throttleCalcScale()
		},

		calcScale () {
			// 画布计算在工具内处理
			this.$screenUtil.calcScale()
			let devicePixelRatio = window.devicePixelRatio
			// 获取:root元素，也就是<html>元素
			// let root = document.documentElement;
			// root.style.setProperty('--devicePixelRatioZoom', 1 / devicePixelRatio);
			// if (devicePixelRatio !== 1) {
			// 	this.$message.warn('请保证浏览器分辨率为100%，保障最佳观看效果！！！')
			// }

			if (this.resizeFunction) {
				this.resizeFunction()
			}
			// xxx canvas 画布不兼容计算模式处理
			// xx.calc()
		},
	},

}
</script>

<style lang="less" scoped>
@import "../assets/styles/components/screen-shell-toolbar.less";
</style>
