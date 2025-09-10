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
import { throttle, isEmpty, isNull, isUndefined, debounce } from 'lodash-es'

function isFullscreen () {
	return !!(document.fullscreenElement || document.msFullscreenElement || document.mozFullScreenElement)
}

export default {
	name: 'ScreenShellToolbar',

	props: {
		container: {},
	},

	data () {

		let dev = this.$datax.get('screen.dev')
		let scale = this.$datax.get('screen.scale')
		let mode = this.$datax.get('screen.mode')
		let fullscreen = this.$datax.get('screen.fullscreen')
		let transition = localStorage.getItem('screen.transition')
		let theme = localStorage.getItem('screen.theme')

		fullscreen = isFullscreen()

		if (isUndefined(dev) || isNull(dev)) {
			this.$datax.set('screen.dev', dev = false)
		}
		if (isUndefined(scale) || isNull(scale)) {
			this.$datax.set('screen.scale', scale = '1')
		}
		if (isUndefined(fullscreen) || isNull(fullscreen)) {
			this.$datax.set('screen.fullscreen', fullscreen = false)
		}
		if (isUndefined(mode) || isNull(mode)) {
			this.$datax.set('screen.mode', mode = 'original')
		}
		if (isUndefined(mode) || isNull(mode)) {
			this.$datax.set('screen.mode', mode = 'original')
		}
		if (isUndefined(transition) || isNull(transition)) {
			localStorage.setItem('screen.transition', transition = 'slide-to-down')
		}
		if (isUndefined(theme) || isNull(theme)) {
			this.$datax.set('screen.theme', theme = 'originblue')
		}

		return {

			//
			width: 1920,
			height: 1080,

			//
			windowWidth: 1920,
			windowHeight: 1080,

			//
			mode,
			dev,
			scale,
			fullscreen,
			transition,
			theme,

			//
			scaleData: [
				{
					value: '0_25',
					label: '0.25x',
				},
				{
					value: '0_33',
					label: '0.33x',
				},
				{
					value: '0_5',
					label: '0.5x',
				},
				{
					value: '0_66',
					label: '0.66x',
				},
				{
					value: '0_88',
					label: '0.88x',
				},
				{
					value: '1',
					label: '1x',
				},
				{
					value: '1_5',
					label: '1.5x',
				},
				{
					value: '2',
					label: '2x',
				},
				{
					value: '3',
					label: '3x',
				},
				{
					value: '4',
					label: '4x',
				},
				{
					value: '5',
					label: '5x',
				},
			],
			modeData: [
				{
					value: 'original',
					label: '整比缩放',
				},
				{
					value: 'autoHeight',
					label: '填充',
				},
				{
					value: 'adapt',
					label: '适应',
				},
			],
			transitionData: [
				{
					label: '翻转',
					value: 'flip',
				},
				{
					label: '立体翻转',
					value: 'flip3D',
				},
				{
					label: '淡入淡出',
					value: 'fade',
				},
				{
					label: '渐隐渐显缩放',
					value: 'fade-transform',
				},
				{
					label: '左移淡入',
					value: 'breadcrumb',
				},
				{
					label: '向下延展',
					value: 'slide-to-down',
				},
				{
					label: '显隐',
					value: 'v-modal',
				},
				{
					label: '线性显影',
					value: 'fade-in-linear',
				},
				{
					label: 'fadeIn',
					value: 'fade-in',
				},
				{
					label: 'zoomInCenter',
					value: 'zoom-in-center',
				},
				{
					label: 'zoomInTop',
					value: 'zoom-in-top',
				},
				{
					label: 'zoomInBottom',
					value: 'zoom-in-bottom',
				},
				{
					label: 'zoomInLeft',
					value: 'zoom-in-left',
				},
				{
					label: 'list',
					value: 'list',
				},
				{
					label: 'zoomCenterToCorner',
					value: 'zoom-center-to-corner',
				},
				{
					label: 'slideDownZoomTopLeft',
					value: 'slide-down-zoom-top-left',
				},
				{
					label: 'collapseTransition',
					value: 'collapse-transition',
				},
				{
					label: 'slide-top',
					value: 'slide-top',
				},
				{
					label: 'slide-bottom',
					value: 'slide-bottom',
				},
				{
					label: 'slide-left',
					value: 'slide-left',
				},
				{
					label: 'slide-right',
					value: 'slide-right',
				},

				{
					label: 'slide-tr',
					value: 'slide-tr',
				},
				{
					label: 'slide-tl',
					value: 'slide-tl',
				},
				{
					label: 'slide-br',
					value: 'slide-br',
				},
				{
					label: 'slide-bl',
					value: 'slide-bl',
				},
				{
					label: 'ping',
					value: 'ping',
				},
				{
					label: 'pulsate-fwd',
					value: 'pulsate-fwd',
				},
				{
					label: 'heartbeat',
					value: 'heartbeat',
				},
				{
					label: 'scale-up-top',
					value: 'scale-up-top',
				},
				{
					label: 'scale-up-center',
					value: 'scale-up-center',
				},
				{
					label: 'puff-in-center',
					value: 'puff-in-center',
				},
				{
					label: 'puff-in-ver',
					value: 'puff-in-ver',
				},
				{
					label: 'puff-in-hor',
					value: 'puff-in-hor',
				},
				{
					label: 'puff-in-top',
					value: 'puff-in-top',
				},
				{
					label: 'puff-in-bottom',
					value: 'puff-in-bottom',
				},
				{
					label: 'bounce-top',
					value: 'bounce-top',
				},
				{
					label: 'bounce-bottom',
					value: 'bounce-bottom',
				},
				{
					label: 'bounce-left',
					value: 'bounce-left',
				},
				{
					label: 'bounce-right',
					value: 'bounce-right',
				},
				{
					label: 'flip-2-hor-top-1',
					value: 'flip-2-hor-top-1',
				},
				{
					label: 'flip-2-hor-top-2',
					value: 'flip-2-hor-top-2',
				},
				{
					label: 'flip-scale-up-ver',
					value: 'flip-scale-up-ver',
				},
				{
					label: 'flip-scale-down-ver',
					value: 'flip-scale-down-ver',
				},
			],
			themeData: [
				{
					value: 'originblue',
					label: '基本蓝',
				},
				{
					value: 'skyblue',
					label: '天空蓝',
				},
				{
					value: 'darkblue',
					label: '午夜蓝',
				},
			],
		}
	},

	watch: {
		dev (value) {
			this.$datax.set('screen.dev', value)
			this.setDev(value)
		},
		scale (value) {
			this.$datax.set('screen.scale', value)
			this.setScale(value)
		},
		mode (value) {
			this.$datax.set('screen.mode', value)
			this.setMode(value)
		},
		fullscreen (value) {
			this.$datax.set('screen.fullscreen', value)
			if (value) {
				this.requestFullscreen()
			} else {
				this.exitFullscreen()
			}
		},
		transition: {
			handler (value) {
				localStorage.setItem('screen.transition', value)
				this.setTransition(value)
			},
			immediate: true,
		},
		theme: {
			handler (value) {
				localStorage.setItem('screen.theme', value)
				this.setTheme(value)
			},
			immediate: true,
		},
	},

	created () {
		this.debounceScroll = debounce(this.scroll, 50)
		this.throttleCalcScale = throttle(this.calcScale, 160)
		this.$window = this.$(window)
	},

	beforeDestroy () {
		window.removeEventListener('resize', this.throttleCalcScale, false)
		window.removeEventListener('scroll', this.debounceScroll, false)
		document.onfullscreenchange = null

		this.debounceScroll.cancel()
		this.throttleCalcScale.cancel()
		this.$window = null
	},

	mounted () {

		let {
			$,
			dev,
			scale,
			mode,
			setDev,
			setScale,
			setMode,
			throttleCalcScale,
			debounceScroll,
		} = this

		setDev(dev)
		setScale(scale)
		setMode(mode)

		window.addEventListener('resize', throttleCalcScale, false)

		document.onfullscreenchange = (event) => {
			this.fullscreen = isFullscreen()
		}

		// 刷新滚动条记录
		this.$nextTick(() => {
			this.$container = this.$(this.container)

			throttleCalcScale()

			setTimeout(() => {
				let scroll = this.$datax.get('scroll')
				if (scroll) {
					window.scrollTo(scroll.x || 0, scroll.y || 0)
				}

				window.addEventListener('scroll', debounceScroll, false)

			}, 160)
		})

	},

	methods: {

		scroll () {
			this.$datax.set('scroll', {
				x: window.scrollX,
				y: window.scrollY,
			})
		},

		setScale (scale) {
			let {
				$,
				scaleData,
				$container,
			} = this

			this.$(this.container).
			removeClass(scaleData.map(n => `scale-${n.value}`).join(' ')).
			addClass(`scale-${scale}`)
		},

		setDev (dev) {
			if (dev) {
				this.$('body').addClass('dev')
			} else {
				this.$('body').removeClass('dev')
			}
		},

		setMode (mode) {
			this.throttleCalcScale()
		},
		setTheme (value) {
			this.$emit('on-change', 'theme', value)
		},
		setTransition (value) {
			this.$emit('on-change', 'transition', value)
		},

		calcScale () {
			let {
				$,
				height,
				width,
				scale,
				mode,
				$window,
				$container,
			} = this

			let $height = $window.height()
			let $width = $window.width()
			let whhw = ''

			$container = this.$('#app')

			if (mode === 'autoHeight') {
				let zoomX = ($width / width).toFixed(4)
				let zoomY = ($height / height).toFixed(4)
				$container.removeClass('wh hw').addClass(whhw).css({
					'transform': '  scale(' + zoomX + ',' + zoomY + ') ',
					opacity: 1,
					left: '0',
					top: '0',
				})
			} else if (mode === 'adapt') {
				let wh = height / width
				let $wh = $height / $width
				let aZoom = ($height / height).toFixed(4)
				if ($wh > wh) {
					aZoom = ($width / width).toFixed(4)
				}
				$container.removeClass('wh hw').addClass(whhw).css({
					'transform': '  scale(' + aZoom + ') translate(-50%, -50%)',
					opacity: 1,
					left: '50%',
					top: '50%',
				})
			} else {
				let zoom = ($width / width).toFixed(4)

				$container.removeClass('wh hw').addClass(whhw).css({
					'transform': '  scale(' + zoom + ') ',
					opacity: 1,
					left: '0',
					top: '0',
				})
			}
		},

		requestFullscreen () {

			if (isFullscreen()) {
				return
			}

			let element = document.documentElement
			if (element.requestFullscreen) {
				element.requestFullscreen()
			} else if (element.msRequestFullscreen) {
				element.msRequestFullscreen()
			} else if (element.mozRequestFullScreen) {
				element.mozRequestFullScreen()
			} else if (element.webkitRequestFullscreen) {
				element.webkitRequestFullscreen()
			}
		},

		exitFullscreen () {

			if (!isFullscreen()) {
				return
			}

			if (document.exitFullscreen) {
				document.exitFullscreen()
			} else if (document.msExitFullscreen) {
				document.msExitFullscreen()
			} else if (document.mozCancelFullScreen) {
				document.mozCancelFullScreen()
			} else if (document.webkitExitFullscreen) {
				document.webkitExitFullscreen()
			}
		},
	},

}
</script>

<style lang="less" scoped>
@import "../assets/styles/components/screen-shell-toolbar.less";
</style>
