<template>
	<div :class="getClass" :style="getStyle" @click="handleClick" v-on="$listeners">
		<slot></slot>
	</div>
</template>

<script>

import { prefix } from '../../../../src/config'

/**
 * FbContainer
 * (c) 2020 lincong1987
 */
export default {
	name: 'FbContainer',
	props: {

		block: {
			type: Boolean,
			default: true,
		},

		flex: {
			type: Boolean,
			default: false,
		},

		inline: {
			type: Boolean,
			default: false,
		},

		relative: {
			type: Boolean,
			default: false,
		},
		absolute: {
			type: Boolean,
			default: false,
		},
		fixed: {
			type: Boolean,
			default: false,
		},
		sticky: {
			type: Boolean,
			default: false,
		},

		ellipsis: {
			type: Boolean,
			default: false,
		},

		width: {
			type: [String, Number],
			default: null,
		},

		height: {
			type: [String, Number],
			default: null,
		},

		ml: {
			type: [String, Number],
			default: null,
		},

		mr: {
			type: [String, Number],
			default: null,
		},
		mb: {
			type: [String, Number],
			default: null,
		},
		mt: {
			type: [String, Number],
			default: null,
		},

		pl: {
			type: [String, Number],
			default: null,
		},

		pr: {
			type: [String, Number],
			default: null,
		},
		pb: {
			type: [String, Number],
			default: null,
		},
		pt: {
			type: [String, Number],
			default: null,
		},

		align: {
			type: String,
			default: null,
		},

		valign: {
			type: String,
			default: null,
		},

		position: {
			type: String,
			default: null,
		},

		display: {
			type: String,
			default: '',
		},

		top: {
			type: [String, Number],
			default: null,
		},
		left: {
			type: [String, Number],
			default: null,
		},
		right: {
			type: [String, Number],
			default: null,
		},
		bottom: {
			type: [String, Number],
			default: null,
		},

		radius: {
			type: String,
			default: null,
		},
		background: {
			type: String,
			default: null,
		},
		color: {
			type: String,
			default: null,
		},

		border: {
			type: String,
			default: null,
		},

		bt: {
			type: String,
			default: null,
		},
		br: {
			type: String,
			default: null,
		},
		bb: {
			type: String,
			default: null,
		},
		bl: {
			type: String,
			default: null,
		},

		padding: {
			type: [String, Number],
			default: null,
		},
		margin: {
			type: [String, Number],
			default: null,
		},
		zIndex: {
			type: [String, Number],
			default: null,
		},

		overflow: {
			type: String,
			default: null,
		},
		overflowX: {
			type: String,
			default: null,
		},
		overflowY: {
			type: String,
			default: null,
		},

		maxHeight: {
			type: [String, Number],
			default: null,
		},
		minHeight: {
			type: [String, Number],
			default: null,
		},
		maxWidth: {
			type: [String, Number],
			default: null,
		},
		minWidth: {
			type: [String, Number],
			default: null,
		},

		cursor: {
			type: String,
			default: null,
		},

		verticalAlign: {
			type: String,
			default: null,
		},

		va: {
			type: String,
			default: null,
		},

		lh: {
			type: String,
			default: undefined,
		},

		// ['row','column','row-reverse','column-reverse']
		flexDirection: {
			type: String,
			default: 'row',
		},
		// ['nowrap', 'wrap']
		flexWrap: {
			type: String,
			default: 'nowrap',
		},
		// alignContent = ['center','flex-start','flex-end','space-around','space-between','stretch']
		alignContent: {
			type: String,
			default: 'normal',
		},
		acCenter: {
			type: Boolean,
			default: false,
		},
		acSpaceBetween: {
			type: Boolean,
			default: false,
		},
		acSpaceAround: {
			type: Boolean,
			default: false,
		},
		acFlexStart: {
			type: Boolean,
			default: false,
		},
		acFlexEnd: {
			type: Boolean,
			default: false,
		},

		// justifyContent = ['center','flex-start','flex-end', 'space-around','space-between', 'space-evenly', ]
		justifyContent: {
			type: String,
			default: 'normal',
		},
		jcCenter: {
			type: Boolean,
			default: false,
		},
		jcFlexStart: {
			type: Boolean,
			default: false,
		},
		jcFlexEnd: {
			type: Boolean,
			default: false,
		},
		jcSpaceBetween: {
			type: Boolean,
			default: false,
		},
		jcSpaceAround: {
			type: Boolean,
			default: false,
		},
		jcSpaceEvenly: {
			type: Boolean,
			default: false,
		},

		// alignItems = ['center','flex-start','flex-end','stretch', 'baseline']
		alignItems: {
			type: String,
			default: 'normal',
		},
		aiCenter: {
			type: Boolean,
			default: false,
		},
		aiFlexStart: {
			type: Boolean,
			default: false,
		},
		aiFlexEnd: {
			type: Boolean,
			default: false,
		},
		aiSpaceBetween: {
			type: Boolean,
			default: false,
		},
		aiSpaceAround: {
			type: Boolean,
			default: false,
		},
		aiBaseline: {
			type: Boolean,
			default: false,
		},

	},

	data () {
		return {
			prefix,
		}
	},
	computed: {
		getClass () {
			let arr = [`${this.prefix}-container`]

			if (this.ellipsis) {
				arr.push(`${this.prefix}-container--ellipsis`)
			}

			return arr
		},
		getStyle () {
			let style = {}

			style.position = this.position
			this.relative && (style.position = 'relative')
			this.absolute && (style.position = 'absolute')
			this.fixed && (style.position = 'fixed')
			this.sticky && (style.position = 'sticky')

			if (this.display) {
				style.display = this.display
			}

			if (this.flex) {
				style.display = 'flex'
				style.flexDirection = this.flexDirection
				style.flexWrap = this.flexWrap

				style.alignContent = this.alignContent
				if (this.acCenter) {
					style.alignContent = 'center'
				}
				if (this.acFlexStart) {
					style.alignContent = 'flex-start'
				}
				if (this.acFlexEnd) {
					style.alignContent = 'flex-end'
				}
				if (this.acSpaceAround) {
					style.alignContent = 'space-around'
				}
				if (this.acSpaceBetween) {
					style.alignContent = 'space-between'
				}

				style.justifyContent = this.justifyContent
				if (this.jcCenter) {
					style.justifyContent = 'center'
				}
				if (this.jcFlexStart) {
					style.justifyContent = 'flex-start'
				}
				if (this.jcFlexEnd) {
					style.justifyContent = 'flex-end'
				}
				if (this.jcSpaceAround) {
					style.justifyContent = 'space-around'
				}
				if (this.jcSpaceBetween) {
					style.justifyContent = 'space-between'
				}
				if (this.jcSpaceEvenly) {
					style.justifyContent = 'space-evenly'
				}

				style.alignItems = this.alignItems
				if (this.aiCenter) {
					style.alignItems = 'center'
				}
				if (this.aiFlexStart) {
					style.alignItems = 'flex-start'
				}
				if (this.aiFlexEnd) {
					style.alignItems = 'flex-end'
				}
				if (this.aiFlexStart) {
					style.alignItems = 'space-around'
				}
				if (this.aiSpaceBetween) {
					style.alignItems = 'space-between'
				}
				if (this.aiBaseline) {
					style.alignItems = 'baseline'
				}

			}

			// height 容错指令 v-autoheight 产生的抖动的效果
			if (this.height) {
				style.height = this.unit(this.height)
			}

			style.width = this.unit(this.width)

			style.top = this.unit(this.top)
			style.left = this.unit(this.left)
			style.right = this.unit(this.right)
			style.bottom = this.unit(this.bottom)

			style.borderRadius = this.radius
			style.background = this.background
			style.color = this.color

			this.margin && (style.margin = this.unit(this.margin))
			this.mt && (style.marginTop = this.unit(this.mt))
			this.mr && (style.marginRight = this.unit(this.mr))
			this.mb && (style.marginBottom = this.unit(this.mb))
			this.ml && (style.marginLeft = this.unit(this.ml))

			this.padding && (style.padding = this.unit(this.padding))
			this.pt && (style.paddingTop = this.unit(this.pt))
			this.pr && (style.paddingRight = this.unit(this.pr))
			this.pb && (style.paddingBottom = this.unit(this.pb))
			this.pl && (style.paddingLeft = this.unit(this.pl))

			style.overflow = this.overflow
			this.overflowX && (style.overflowX = this.overflowX)
			this.overflowY && (style.overflowY = this.overflowY)

			style.maxHeight = this.unit(this.maxHeight)
			style.minHeight = this.unit(this.minHeight)
			style.maxWidth = this.unit(this.maxWidth)
			style.minWidth = this.unit(this.minWidth)

			style.textAlign = this.align

			style.border = this.border
			this.bt && (style.borderTop = this.bt)
			this.br && (style.borderRight = this.br)
			this.bb && (style.borderBottom = this.bb)
			this.bl && (style.borderLeft = this.bl)

			style.zIndex = this.zIndex

			style.cursor = this.cursor

			if (this.inline) {
				style.display = 'inline-block'
			}

			if (this.valign === 'center') {
				style.lineHeight = this.unit(style.height)
			}

			if (typeof this.lh !== 'undefined') {
				style.lineHeight = this.lh
			}

			this.va && (style.verticalAlign = this.va)

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

<style scoped>

</style>
