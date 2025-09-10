<template>
	<span :class="getClass" :style="getStyle" @click="$emit('on-click')">
		<slot></slot>
	</span>
</template>

<script>
/**
 * FbText
 * (c) 2020 lincong1987
 */
import { prefix } from '../../../../src/config'
import { getSizeStyle } from '../../../utils/propUtils'
import fontWeight from '../../../utils/style-props/fontWeight'
import textAlign, { textAlignMap } from '../../../utils/style-props/textAlign'
import color from '../../../utils/style-props/color'
import background from '../../../utils/style-props/background'
import layout from '../../../utils/style-props/layout'
import display from '../../../utils/style-props/display'
import margin from '../../../utils/style-props/margin'
import padding from '../../../utils/style-props/padding'
import inline from '../../../utils/style-props/inline'
import verticalAlign from '../../../utils/style-props/verticalAlign'
import family from '../../../utils/style-props/family'
import fontStyle from '../../../utils/style-props/fontStyle'
import textDecoration from '../../../utils/style-props/textDecoration'
import indent from '../../../utils/style-props/indent'
import ellipsis from '../../../utils/style-props/ellipsis'
import border from '../../..//utils/style-props/border'
import overflow from '../../../utils/style-props/overflow'
import lineHeight from '../../../utils/style-props/lineHeight'
import filter from '../../..//utils/style-props/filter'
import boxSizing from '../../..//utils/style-props/boxSizing'
import boxShadow from '../../../utils/style-props/boxShadow'
import position from '../../../utils/style-props/position'
import flex from '../../../utils/style-props/flex'
import order from '../../../utils/style-props/order'
import justifySelf from '../../../utils/style-props/justifySelf'
import cursor from '../../../utils/style-props/cursor'

export default {
	name: 'FbText',
	props: {
		...display.props,
		...inline.props,
		...position.props,
		// 颜色
		...color.props,
		// 背景
		...background.props,
		// 文本对齐
		...textAlign.props,
		// 布局
		...layout.props,
		// 文本粗细
		...fontWeight.props,
		//省略号
		...ellipsis.props,
		//  字体
		...family.props,
		// 行高
		...lineHeight.props,
		// 文字样式
		...fontStyle.props,
		//  文字装饰
		...textDecoration.props,
		// 文本缩进
		...indent.props,
		// 外部边距
		...margin.props,
		// 内边距
		...padding.props,
		...filter.props,
		...order.props,
		...border.props,
		...overflow.props,
		...boxSizing.props,
		...verticalAlign.props,
		...display.props,
		...flex.props,
		...boxShadow.props,
		...justifySelf.props,
		...cursor.props,

		// 文本大小
		size: {
			type: [ String, Number,],
			default: '',
		},
		// 文本大小
		type: {
			type: String,
			default: null,
		},
		// 渐变
		gradient: {
			type: String,
			default: null,
		},
	},
	data () {

		return {
			prefix,
		}
	},

	created () {

	},

	computed: {
		getClass () {
			let arr = [`${prefix}-text`]

			if (this.size) {
				arr.push(`${prefix}-text--${this.size}`)
			}
			if (this.type) {
				arr.push(`${prefix}-text--${this.type}`)
			}
			if (this.ellipsis) {
				arr.push(`${prefix}-text--ellipsis`)
			}
			return arr

		},

		getStyle () {
			let style = {
				...color.style.apply(this),
				...background.style.apply(this),
				...fontWeight.style.apply(this),
				...textAlign.style.apply(this),
				...verticalAlign.style.apply(this),
				...color.style.apply(this),
				...family.style.apply(this),
				...fontStyle.style.apply(this),
				...textDecoration.style.apply(this),
				...indent.style.apply(this),
				...ellipsis.style.apply(this),
				...layout.style.apply(this),
				...position.style.apply(this),
				...padding.style.apply(this),
				...margin.style.apply(this),
				...boxShadow.style.apply(this),
				...display.style.apply(this),
				...border.style.apply(this),
				...overflow.style.apply(this),
				...boxSizing.style.apply(this),
				...filter.style.apply(this),
				...lineHeight.style.apply(this),
				...order.style.apply(this),
				...flex.style.apply(this),
				...justifySelf.style.apply(this),
				...cursor.style.apply(this),
			}

			// 字体大小
			if (this.size !== '' && !['xs', 's', 'm', 'l', 'xl', 'xxl', 'xxxl'].includes(this.size)) {
				style.fontSize = getSizeStyle(this.size, {})
			}

			if (this.ellipsis) {
				style.overflow = 'hidden'
				style.textOverflow = 'ellipsis'
				style.whiteSpace = 'nowrap'
			}

			if (this.gradient) {
				style.background = this.gradient
				style.backgroundClip = 'text'
				style.color = 'transparent'
				style.webkitBackgroundClip = 'text'
			}

			return style
		},
	},
}
</script>

<style scoped>

</style>
