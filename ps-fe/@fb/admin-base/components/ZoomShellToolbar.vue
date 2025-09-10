<!-- 外壳缩放工具栏 -->
<template>
	<fb-container class="zoom-shell-toolbar">
		<fb-container>
			<fb-space align="center">
				<fb-text>适应模式</fb-text>
				<fb-select style="width: 90px"
				           v-model="mode"
				           :data="modeData"
				></fb-select>
			</fb-space>
		</fb-container>
	</fb-container>
</template>

<script>
/**
 * 外壳缩放工具栏 - 目前为大屏登录页适配分辨率
 * ZoomShellToolbar
 * (c) 2021 lincong1987
 */
import { throttle, isEmpty, isNull, isUndefined, debounce } from 'lodash-es'

export default {
	name: 'ZoomShellToolbar',

	props: {
		/**
		 * id #xxx
		 * dom html节点
		 */
		container: {},
		zoomMode: {
			type: [String],
		},
		width: {
			type: [Number],
			default: 1920
		},
		height: {
			type: [Number],
			default: 1080
		},
		resetDomClass: {
			type: String,
			default: ''
		}
	},

	data () {
		return {
			mode: this.zoomMode || 'autoHeight',
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
		}
	},

	watch: {
		mode (value) {
			this.setMode(value)
		},
	},

	created () {
		this.throttleCalcScale = throttle(this.calcScale, 160)
		this.$window = this.$(window)
	},

	beforeDestroy () {
		window.removeEventListener('resize', this.throttleCalcScale, false)

		this.throttleCalcScale.cancel()
		this.$window = null
	},

	mounted () {

		let {
			throttleCalcScale
		} = this

		throttleCalcScale()
		window.addEventListener('resize', throttleCalcScale, false)

	},

	methods: {

		setMode (mode) {
			this.throttleCalcScale()
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
			$container = this.$(this.container)
			const $login_card = this.$('#login_card')
			const resetDom = this.$(this.resetDomClass)

			if (mode === 'autoHeight') {
				let zoomX = ($width / width).toFixed(4)
				let zoomY = ($height / height).toFixed(4)
				$container.removeClass('wh hw').addClass(whhw).css({
					'transform': '  scale(' + zoomX + ',' + zoomY + ') ',
					opacity: 1,
					left: '0',
					top: '0',
				})
				if ($login_card) {
					$login_card.css({
						'transform': 'scale(' + (1 / zoomX) + ',' + (1 / zoomY)  + ') ',
					})
				}
				if (resetDom) {
					resetDom.css({
						'transform': 'scale(' + (1 / zoomX) + ',' + (1 / zoomY)  + ') ',
					})
				}
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
				if ($login_card) {
					$login_card.css({
						'transform': 'scale(' + (1 / aZoom) + ') ',
					})
				}
				if (resetDom) {
					resetDom.css({
						'transform': 'scale(' + (1 / aZoom) + ') ',
					})
				}
			} else {
				let zoom = ($width / width).toFixed(4)

				$container.removeClass('wh hw').addClass(whhw).css({
					'transform': '  scale(' + zoom + ') ',
					opacity: 1,
					left: '0',
					top: '0',
				})
				if ($login_card) {
					$login_card.css({
						'transform': 'scale(' + (1 / zoom) + ') ',
					})
				}
				if (resetDom) {
					resetDom.css({
						'transform': 'scale(' + (1 / zoom) + ') ',
					})
				}
			}
		},
	},

}
</script>

<style lang="less" scoped>
	.zoom-shell-toolbar {
		position:      fixed;
		top:           2px;
		left:          -210px;
		width:         210px;
		padding:       10px;
		background:    linear-gradient(#18317F, #11235C);
		border:        1px solid #63A1FE99;
		color:         #FFFFFF;
		cursor:        pointer;
		transition:    left 0.4s;
		border-radius: 4px;
		box-shadow:    0 0 4px #66739E;
		z-index:       1000;

		&:before {
			content:  "";
			position: absolute;
			height:   100%;
			bottom:   0;
			left:     -10px;
			width:    10px;
		}

		&:after {
			content:  "";
			position: absolute;
			height:   100%;
			bottom:   0;
			right:    -20px;
			width:    20px;
		}

		&.hidden {
			display: none !important;
		}

		&:hover {
			left: 5px;

			&:before {
				right: -20px;
				width: 550px;
			}

			&:after {
				content:  "";
				position: absolute;
				top:      100%;
				height:   0px;
				left:     -20px;
				width:    550px;
			}

		}

	}
</style>
