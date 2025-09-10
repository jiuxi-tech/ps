<template>
	<fb-container class="screen-panel" :class="{
                        [`screen-panel--theme-${theme}`]: !!theme   ,
                        [`screen-panel--no-border`]: noBorder,
                        [`screen-panel--no-header`]: noHeader,
                        [`screen-panel--no-header-angle`]: noHeaderAngle,
                        [`screen-panel--show-corner`]: showCorner,
                        [`screen-panel--no-top`]: noTop,
    }"
				  v-bind="$attrs"
				  v-on="$listeners"
	>

		<transition :name="transitionName || transitions.zoomCenterToCorner" mode="out-in">

			<fb-container :id="`${myId}_wrapper`"
						  class="screen-panel__wrapper"
						  :style="{...getWrapperStyle, ...wrapperStyle}"
						  v-if="myShowWrapper"
			>
				<fb-container class="screen-panel__wrapper__top"></fb-container>
				<fb-container class="screen-panel__wrapper__border"></fb-container>
				<fb-container class="screen-panel__wrapper__bottom"></fb-container>
				<!--                <fb-container class="screen-panel__wrapper__shadow-left"></fb-container>-->

			</fb-container>
		</transition>

		<transition :name="transitionName || transitions.zoomCenterToCorner" mode="out-in">
			<fb-container :id="`${myId}_header`"
						  class="screen-panel__header"
						  :style="{...getHeaderStyle, ...headerStyle}"
						  v-if="!showCorner && !noHeader && !noHeaderAngle && myShowHeader"
			>
				<slot name="title-lottie">
					<fb-lottie v-if="myTitleLottie" class="screen-panel__header_title_lottie" :width="parseFloat(width)" :height="parseFloat(myHeaderHeight)" :data="myTitleLottie"></fb-lottie>
				</slot>


				<fb-container @on-click="$emit('on-title-click')" inline class="screen-panel__title title"
							  :style="titleStyle">
					<slot name="title">
						{{ title }}
					</slot>
				</fb-container>

				<slot name="actions"></slot>
			</fb-container>
		</transition>

		<transition :name="transitionName || transitions.zoomCenterToCorner" mode="out-in">
			<fb-container :id="`${myId}`"
						  class="screen-panel__body"
						  :class="bodyClass"
						  v-if="myShowBody"
						  :style="{...getBodyStyle, ...bodyStyle}"
						  :padding="padding"
			>
				<slot></slot>
			</fb-container>
		</transition>

		<fb-container
			class="screen-panel__radar"
			:class="{
                [`screen-panel__radar-${radarDirection}`]: !!radarDirection,
                active: myShowRadar,
                [`screen-panel__radar-${radarDelay}s`]: radarDelay > 0,

            }"
			v-if="myShowRadar"
			:style="{...getRadarStyle}"
		>
		</fb-container>


	</fb-container>

</template>

<script>
/**
 * ScreenPanel
 * (c) 2021 lincong1987
 *
 * 哒哒哒哒
 *
 */
import ceruleanBlueTitleLottie from "@fb/screen-base/src/assets/lottie-json/theme/cerulean-blue/panel-title.json"

export default {

	name: 'ScreenPanel',

	props: {

		transitionName: {
			type: [String],
			default: '',
		},

		top: {
			type: [Number, String],
			default: undefined,
		},

		left: {
			type: [Number, String],
			default: undefined,
		},

		right: {
			type: [Number, String],
			default: undefined,
		},

		bottom: {
			type: [Number, String],
			default: undefined,
		},

		height: {
			type: [Number, String],
			default: undefined,
		},

		width: {
			type: [Number, String],
			default: undefined,
		},

		theme: {
			type: [Number, String],
			default: 'skyblue',
		},

		id: {
			type: [Number, String],
			default: undefined,
		},

		padding: {
			type: [Number, String],
			default: '8px',
		},

		title: {
			type: [Number, String],
			default: '',
		},

		titleStyle: {
			type: [Object],
			default() {
				return {}
			},
		},
		showHeader: {
			type: [Boolean],
			default: true,
		},
		// 无top样式，兼容 ScreenPanelTab 样式布局
		noTop: {
			type: [Boolean],
			default: false,
		},
		// 无标题，圆角展示
		noHeader: {
			type: [Boolean],
			default: false,
		},
		// 无标题，上尖角展示
		noHeaderAngle: {
			type: [Boolean],
			default: false,
		},
		showWrapper: {
			type: [Boolean],
			default: true,
		},
		showBody: {
			type: [Boolean],
			default: true,
		},

		showRadar: {
			type: [Boolean],
			default: false,
		},

		/**
		 * 扫描方向
		 * 'vertical'
		 * 'horizontal'
		 */
		radarDirection: {
			type: String,
			default: 'vertical',
			validator(val) {
				return ['vertical', 'horizontal'].includes(val)
			},
		},

		radarDelay: {
			type: Number,
			default: 2,
		},

		headerStyle: {
			type: [Object],
			default() {
				return {}
			},
		},
		wrapperStyle: {
			type: [Object],
			default() {
				return {}
			},
		},
		bodyStyle: {
			type: [Object],
			default() {
				return {}
			},
		},

		bodyClass: {
			type: [Object, Array, String],
			default() {
				return []
			},
		},

		wrapperClass: {
			type: [Object, Array, String],
			default() {
				return []
			},
		},

		headerHeight: {
			type: [Number, String],
			default: 35,
		},

		noBorder: {
			type: [Boolean],
			default: false,
		},
		// 无 header 尖角号模式
		showCorner: {
			type: [Boolean],
			default: false,
		},
		// title lottie 背景效果
		titleLottie: {
			type: [Object],
			default: null,
		}
	},

	data() {
		let myId = this.id || `panel_${new Date().getTime()}`
		let height = this.headerHeight
		let titleLottie = this.titleLottie
		if (app.$screenUtil) {
			let theme = app.$screenUtil.getTheme()
			if (theme === 'trans-blue') {
				height = 48
			} else if (theme === 'geekl-blue') {
				height = 46
			} else if (theme === 'cerulean-blue') {
				height = 48
				titleLottie = titleLottie ? titleLottie : ceruleanBlueTitleLottie
			}
		}

		return {
			myId,
			myShowWrapper: false,
			myShowHeader: false,
			myShowBody: false,
			myShowRadar: false,
			myHeaderHeight: height,
			myTitleLottie: titleLottie
		}
	},

	computed: {

		getWrapperStyle() {
			let {
				top,
				left,
				right,
				bottom,
				height,
				width,
			} = this
			let style = {}

			if (top) {
				style.top = typeof top === 'number' ? `${top}px` : top
			}
			if (left) {
				style.left = typeof left === 'number' ? `${left}px` : left
			}
			if (right) {
				style.right = typeof right === 'number' ? `${right}px` : right
			}
			if (bottom) {
				style.bottom = typeof bottom === 'number' ? `${bottom}px` : bottom
			}
			if (height) {
				style.height = typeof height === 'number' ? `${height}px` : height
			}
			if (width) {
				style.width = typeof width === 'number' ? `${width}px` : width
			}

			return style
		},

		getHeaderStyle() {
			let {
				top,
				left,
				right,
				bottom,
				height,
				width,
				myHeaderHeight: headerHeight,
			} = this

			let style = {}

			if (top) {
				style.top = typeof top === 'number' ? `${top}px` : top
			}
			if (left) {
				style.left = typeof left === 'number' ? `${left}px` : left
			}
			if (right) {
				style.right = typeof right === 'number' ? `${right}px` : right
			}
			if (bottom) {
				style.bottom = typeof bottom === 'number' ? `${bottom}px` : bottom
			}
			if (height) {
				style.height = typeof headerHeight === 'number' ? `${headerHeight}px` : headerHeight
			}
			if (width) {
				style.width = typeof width === 'number' ? `${width}px` : width
			}

			return style
		},

		getBodyStyle() {
			let {
				top,
				left,
				right,
				bottom,
				height,
				width,
				myHeaderHeight: headerHeight,
				noHeader,
				noHeaderAngle,
				showCorner
			} = this
			let style = {}

			let _top = typeof top === 'number' ? `${top}px` : top
			let _height = typeof height === 'number' ? `${height}px` : height

			if (top) {
				style.top = _top
			}
			if (left) {
				style.left = typeof left === 'number' ? `${left}px` : left
			}
			if (right) {
				style.right = typeof right === 'number' ? `${right}px` : right
			}
			if (bottom) {
				style.bottom = typeof bottom === 'number' ? `${bottom}px` : bottom
			}
			if (height) {
				style.height = _height
			}
			if (headerHeight) {
				style.top = `${parseFloat(_top) + parseFloat(headerHeight)}px`
				style.height =
					`${parseFloat(_height) - parseFloat(headerHeight)}px`
			}
			if (noHeader || showCorner || noHeaderAngle) {
				style.top = _top
				style.height = _height
			}
			if (width) {
				style.width = typeof width === 'number' ? `${width}px` : width
			}

			return style
		},

		getRadarStyle() {
			let {
				top,
				left,
				right,
				bottom,
				height,
				width,
			} = this
			let style = {}

			if (top) {
				style.top = typeof top === 'number' ? `${top}px` : top
			}
			if (left) {
				style.left = typeof left === 'number' ? `${left}px` : left
			}
			if (right) {
				style.right = typeof right === 'number' ? `${right}px` : right
			}
			if (bottom) {
				style.bottom = typeof bottom === 'number' ? `${bottom}px` : bottom
			}
			if (height) {
				style.height = typeof height === 'number' ? `${height}px` : height
			}
			if (width) {
				style.width = typeof width === 'number' ? `${width}px` : width
			}

			return style
		},
	},

	watch: {
		showRadar(val) {
			this.myShowRadar = !val
			this.$nextTick(() => {
				this.myShowRadar = val
			})
		},
//        myShowRadar (val) {
//            if (val === true) {
//                setTimeout(() => {
//                    this.myShowRadar = false
//                }, 2000)
//            }
//        },

	},

	mounted() {
		this.myShowWrapper = this.showWrapper
		this.myShowHeader = this.showHeader
		this.myShowBody = this.showBody
		this.myShowRadar = this.showRadar

		if (this.$screenUtil) {
			this.$screenUtil.on('screen.theme', (theme) => {
				// 动态判断主题
				if (theme === 'trans-blue') {
					this.myHeaderHeight = 48
				} else if (theme === 'geekl-blue') {
					this.myHeaderHeight = 46
				} else if (theme === 'cerulean-blue') {
					this.myHeaderHeight = 48
				} else {
					this.myHeaderHeight = 35
				}
				this.$forceUpdate()
			})
		}
	},
	beforeDestroy() {
		// console.log('我被销毁了？？？')
	}
}
</script>

<style lang="less" scoped>

@import "../assets/styles/components/screen-panel.less";

</style>
