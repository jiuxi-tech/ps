<template>
	<div :class="getClass" :style="getStyle" @click="handleClick">
		<slot></slot>
	</div>
</template>

<script>

import { prefix } from '../../../../src/config'

/**
 * FbView
 * (c) 2020 lincong1987
 */
export default {
	name: 'FbView',
	props: {

		block: {
			type: Boolean,
			default: true,
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
			default: 'block',
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

		padding: {
			type: [String, Number],
			default: null,
		},
		margin: {
			type: [String, Number],
			default: null,
		},
		zIndex: {
			type: String,
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

			style.display = this.display

			style.height = this.unit(this.height)
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

			style.zIndex = this.zIndex

			style.cursor = this.cursor

			if (this.inline) {
				style.display = 'inline-block'
			}

			if (this.valign === 'center') {
				style.lineHeight = this.unit(style.height)
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
