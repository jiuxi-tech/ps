<template>
	<fb-container class="screen-flop-board"
				  :class="getClass" :style="getStyle" :height="height"
				  :width="width" v-bind="$attrs" v-on="$listeners">
		<div v-if="$slots.icon || icon" class="screen-flop-board__icon">
			<slot name="icon">
				<fb-avatar :icon="icon" :size="iconSize" circle
						   :style="[{background: iconBackground || iconBg}, iconStyle]"></fb-avatar>
			</slot>
		</div>
		<div v-if="$slots.label || label" class="screen-flop-board__label" :style="labelStyle">
			<slot name="label">
				<fb-text v-html="label"></fb-text>
			</slot>
		</div>
		<div v-if="$slots.title || title" class="screen-flop-board__title" :style="titleStyle">
			<slot name="title">{{ title }}</slot>
		</div>
		<div v-if="$slots.flop" class="screen-flop-board__flop" :style="flopStyle">
			<slot name="flop"></slot>
		</div>
		<div v-if="$slots.value || value || value==0" @click="$emit('on-value-click')"
			 class="screen-flop-board__value" :style="valueStyle">
			<span :class="[{'screen-flop-board__value-click': $listeners['on-value-click']}]">
				<slot name="value">
					<template v-if="isNumber(value)">
						<fb-number :end="value" :append="append" :prepend="prepend" :decimals="decimals" :use-queue="useQueue"
								   :separator="separator"></fb-number>
					</template>
					<template v-else>
						<fb-text>{{ value }}</fb-text>
					</template>
				</slot>
			</span>
			<slot name="unit">
				<fb-text v-if="unit" class="screen-flop-board__value_unit" :style="unitStyle">
					{{ unit }}
				</fb-text>
			</slot>
		</div>

		<slot></slot>
	</fb-container>
</template>

<script>
/**
 * ScreenFlopBoard
 * (c) 2021 lincong1987
 */
import {isNumber, isArray} from 'lodash-es'

export default {
	name: 'ScreenFlopBoard',
	props: {
		// l-v label左 value右
		// i-l-v icon左label左 value右
		// l-f label左 flop右

		// vertical_l-v label上 value下
		// vertical_v-l value上 label下
		// vertical_ileft-l-v icon-左 label上 value下
		// vertical_i-l-v icon-上 label中 value下
		// vertical_t-f title上 flop下
		// vertical_l-f label上 flop下

		theme: {
			type: String,
			default: 'l-v',
		},
		icon: {
			type: [String],
			default: null,
		},
		iconSize: {
			type: [String, Number],
			default: 28,
		},
		iconBg: {
			type: [String],
			default: null,
		},
		iconBackground: {
			type: [String],
			default: null,
		},
		iconStyle: {
			type: [Object, Array],
			default: null,
		},

		value: {
			type: [Number, String],
			default: null,
		}
		,
		valueStyle: {
			type: [Object],
			default: null,
		},
		label: {
			type: [Number, String],
			default: null,
		},

		labelStyle: {
			type: [Object],
			default: null,
		},

		title: {
			type: [Number, String],
			default: null,
		},

		titleStyle: {
			type: [Object],
			default: null,
		},

		flopStyle: {
			type: [Object],
			default: null,
		},

		height: {
			type: [Number, String],
			default: null,
		},

		width: {
			type: [Number, String],
			default: null,
		},

		noBg: { // 控制背景样式全无
			type: Boolean,
			default: null,
		},

		noBorder: {
			type: Boolean,
			default: null,
		},

		noShadow: {
			type: Boolean,
			default: null,
		},
		noBackground: {
			type: Boolean,
			default: null,
		},
		background: {
			type: [String],
			default: null,
		},
		prepend: {
			type: [String, Number],
			default: undefined,
		},
		append: {
			type: [String, Number],
			default: undefined,
		},
		// 小数位数
		decimals: {
			type: Number,
			default: 0,
		},
		// 千分位分隔符
		separator: {
			type: String,
			default: ',',
		},

		unit: { // 单位
			type: String,
			default: '',
		},
		unitStyle: {
			type: Object,
		},
		boardStyle: {
			type: [Object, Array],
			default: () => [],
		},
		useQueue: {
			type: Boolean,
			default: false,
		}

	},
	mounted() {
		// console.log(this.value, typeof this.value)
		// console.log(this.$listeners)
	},

	computed: {
		getStyle() {
			let {
				height,
				width,
				theme,
				background,
				iconSize,
				boardStyle
			} = this
			let style = {}

			if (height) {
				style.height = typeof height === 'number' ? `${height}px` : height
			}

			if (width) {
				style.width = typeof width === 'number' ? `${width}px` : width
			}

			if (background) {
				style.background = background
			}

			// vertical_ileft-l-v 定制 left
			if (theme === 'vertical_ileft-l-v' && iconSize) {
				style.paddingLeft = iconSize + 14 + 'px'
			}

			if (isArray(boardStyle)) {
				style = [style, ...boardStyle]
			} else {
				style = Object.assign(style, boardStyle)
			}

			return style

		},

		getClass() {
			let {
				height,
				width,
				theme,
				noBg,
				noBorder,
				noShadow,
				noBackground,
			} = this
			let arr = []

			if (theme) {
				arr.push(`screen-flop-board--theme-${theme}`)
			}

			if (noBorder) {
				arr.push(`screen-flop-board--no-border`)
			}
			if (noShadow) {
				arr.push(`screen-flop-board--no-shadow`)
			}
			if (noBackground) {
				arr.push(`screen-flop-board--no-background`)
			}
			if (noBg) {
				arr.push(`screen-flop-board--no-bg`)
			}
			if (this.$listeners['on-click']) {
				arr.push(`screen-flop-board--click`)
			}

			return arr

		}
	},

	watch: {},

	methods: {

		isNumber,
		handleClick() {
			this.$emit('on-click')
		},
	},
}
</script>

<style lang="less" scoped>

@import "../assets/styles/components/screen-flop-board.less";

</style>
