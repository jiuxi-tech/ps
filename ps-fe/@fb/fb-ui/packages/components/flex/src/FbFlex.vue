<template>
	<div :class="getClass" :style="getStyle" @click="handleClick" v-on="$listeners">
		<slot></slot>
	</div>
</template>

<script>
import { prefix } from '../../../../src/config'
import position from '../../../utils/style-props/position'
import margin from '../../../utils/style-props/margin'
import padding from '../../../utils/style-props/padding'
import color from '../../../utils/style-props/color'
import background from '../../../utils/style-props/background'
import display from '../../../utils/style-props/display'
import backdropFilter from '../../../utils/style-props/backdropFilter'
import border from '../../../utils/style-props/border'
import zIndex from '../../../utils/style-props/zIndex'
import boxShadow from '../../../utils/style-props/boxShadow'
import textAlign from '../../../utils/style-props/textAlign'
import inline from '../../../utils/style-props/inline'
import boxSizing from '../../../utils/style-props/boxSizing'
import cursor from '../../../utils/style-props/cursor'
import lineHeight from '../../../utils/style-props/lineHeight'
import verticalAlign from '../../../utils/style-props/verticalAlign'
import flexDirection from '../../../utils/style-props/flexDirection'
import flexWrap from '../../../utils/style-props/flexWrap'
import placeContent from '../../../utils/style-props/placeContent'
import alignContent from '../../../utils/style-props/alignContent'
import justifyContent from '../../../utils/style-props/justifyContent'
import alignItems from '../../../utils/style-props/alignItems'
import gridTemplate from '../../../utils/style-props/gridTemplate'
import gridArea from '../../../utils/style-props/gridArea'
import flexBasis from '../../../utils/style-props/flexBasis'
import outline from '../../../utils/style-props/outline'
import fontWeight from '../../../utils/style-props/fontWeight'
import fontStyle from '../../../utils/style-props/fontStyle'
import fontFamily from '../../../utils/style-props/fontFamily'

import filter from '../../../utils/style-props/filter'
import ellipsis from '../../../utils/style-props/ellipsis'
import grid from '../../../utils/style-props/grid'
import gap from '../../../utils/style-props/gap'
import flexShrink from '../../../utils/style-props/flexShrink'
import flex from '../../../utils/style-props/flex'
import flexFlow from '../../../utils/style-props/flexFlow'
import layout from '../../../utils/style-props/layout'
import flexGrow from '../../../utils/style-props/flexGrow'
import order from '../../../utils/style-props/order'
import justifySelf from '../../../utils/style-props/justifySelf'
import { getSizeStyle } from '../../../utils/propUtils'
import flexAlign from '../../../utils/style-props/flexAlign'
import overflow from '../../../utils/style-props/overflow'

/**
 * 弹性布局
 */
export default {
	name: 'FbFlex',
	props: {
		//   内联元素
		...inline.props,
		//定位
		...position.props,
		...layout.props,
		// 字体
		...fontFamily.props,
		...fontStyle.props,
		...fontWeight.props,

		...color.props,
		...background.props,

		// 省略号
		...ellipsis.props,
		...boxShadow.props,
		...border.props,
		...boxSizing.props,
		...margin.props,
		...padding.props,
		...filter.props,
		...display.props,
		...outline.props,
		...cursor.props,

		...verticalAlign.props,
		...lineHeight.props,

		...gap.props,
		...flex.props,
		...flexGrow.props,
		...flexShrink.props,
		...flexBasis.props,
		...flexWrap.props,
		...flexDirection.props,
		...flexFlow.props,
		...placeContent.props,
		...alignContent.props,
		...justifyContent.props,
		...alignItems.props,
		...justifySelf.props,
		...flexAlign.props,

		...grid.props,
		...gridArea.props,
		...gridTemplate.props,

		...order.props,

		...overflow.props,

		valign: {
			type: String,
			default: undefined,
		},

		textGradient: {
			type: [String, Array],
			default: undefined,
		},

		mask: {
			type: String,
			default: undefined,
		},

		textLeft: {
			type: Boolean,
			default: false,
		},
		textCenter: {
			type: Boolean,
			default: false,
		},
		textRight: {
			type: Boolean,
			default: false,
		},
		textJustify: {
			type: Boolean,
			default: false,
		},

		// 文本大小
		size: {
			type: [String, Number],
			default: '',
		},

	},

	data () {
		return {
			prefix,
		}
	},
	computed: {
		getClass () {
			let arr = [`${this.prefix}-flex`]

			if (this.ellipsis) {
				arr.push(`${this.prefix}-flex--ellipsis`)
			}

			return arr
		},
		getStyle () {

			let style = {
				...position.style.apply(this),
				...layout.style.apply(this),
				...padding.style.apply(this),
				...margin.style.apply(this),

				...fontWeight.style.apply(this),
				...fontStyle.style.apply(this),
				...fontFamily.style.apply(this),

				...textAlign.style.apply(this),
				...color.style.apply(this),
				...background.style.apply(this),
				...border.style.apply(this),

				...filter.style.apply(this),
				...backdropFilter.style.apply(this),
				...zIndex.style.apply(this),
				...boxShadow.style.apply(this),
				...boxSizing.style.apply(this),

				...flexDirection.style.apply(this),

				...gap.style.apply(this),
				...flex.style.apply(this),
				...flexGrow.style.apply(this),
				...flexShrink.style.apply(this),
				...flexBasis.style.apply(this),
				...flexWrap.style.apply(this),
				...flexFlow.style.apply(this),
				...flexAlign.style.apply(this),
				...placeContent.style.apply(this),
				...alignContent.style.apply(this),
				...justifyContent.style.apply(this),
				...alignItems.style.apply(this),
				...justifySelf.style.apply(this),
				...order.style.apply(this),

				...grid.style.apply(this),

				...outline.style.apply(this),
				...cursor.style.apply(this),
				...lineHeight.style.apply(this),
				...verticalAlign.style.apply(this),
				...ellipsis.style.apply(this),

				...display.style.apply(this),
				...overflow.style.apply(this),

			}

			if (typeof this.flex !== 'undefined') {
				style.flex = this.flex
			}

			style.display = this.display || 'flex'

			if (this.inline) {
				style.display = 'inline-flex'
			}

			if (this.block) {
				style.display = 'block'
				if (this.inline) {
					style.display = 'inline-block'
				}
			}

			if (this.grid === true || this.display === 'grid') {
				style.display = 'grid'
				if (this.inline) {
					style.display = 'inline-grid'
				}
				style = {
					...style,
					...gridTemplate.style.apply(this),
				}
			}

			style = {
				...style,
				...gridArea.style.apply(this),
			}

			// 字体大小
			if (this.size !== '' && !['xs', 's', 'm', 'l', 'xl', 'xxl', 'xxxl'].includes(this.size)) {
				style.fontSize = getSizeStyle(this.size, {})
			}

			if (this.valign === 'center') {
				style.lineHeight = this.unit(style.height)
			}

			this.textLeft && (style.textAlign = 'left')
			this.textCenter && (style.textAlign = 'center')
			this.textRight && (style.textAlign = 'right')
			this.textJustify && (style.textAlign = 'justify')

			if (this.textGradient) {
				style.background = this.textGradient
				style.backgroundClip = 'text'
				style.color = 'transparent'
				style.webkitBackgroundClip = 'text'
			}

			if (this.mask) {
				style.mask = this.mask
			}

			return style
		},
	},
	methods: {
		handleClick (e) {
			this.$emit('on-click', e)
		},

		unit (val) {
			return typeof val === 'number' ? `${val}px` : val
		},
	},
}
</script>

<style scoped lang="less">

</style>
