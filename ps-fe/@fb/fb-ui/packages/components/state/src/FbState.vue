<template>
	<span :class="getClass" :style="getStyle" @click="$emit('on-click', $event)">
		<slot></slot>
	</span>
</template>

<script>
/**
 * FbState
 * (c) 2023 lincong1987
 */
import { prefix } from '../../../../src/config'
import { getSizeStyle } from '../../../utils/propUtils'

export default {
	name: 'FbState',
	props: {
		size: {
			type: [String, Number],
			default: 'm',
		},
		type: {
			type: String,
			default: 'default',
		},

		color: {
			type: String,
			default: null,
		},

		active: {
			type: Boolean,
			default: false,
		},


		mr: {
			type: [String, Number],
			default: null,
		},
		ml: {
			type: [String, Number],
			default: null,
		},
		mt: {
			type: [String, Number],
			default: null,
		},

		mb: {
			type: [String, Number],
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
			let arr = [`${prefix}-state`]

			if (this.size) {
				arr.push(`${prefix}-state--${this.size}`)
			}
			if (this.type) {
				arr.push(`${prefix}-state--${this.type}`)
			}

			if (this.active) {
				arr.push(`${prefix}-state--active`)
			}
			return arr

		},

		getStyle () {
			let style = {}

			// style.lineHeight = 1.2

			if (this.width) {
				style.width = this.width
			}
			if (this.color) {
				style.backgroundColor = this.color
			}

			if (this.size !== '' && !['xs', 's', 'm', 'l', 'xl', 'xxl', 'xxxl'].includes(this.size)) {
				style.height = getSizeStyle(this.size, {})
				style.width = getSizeStyle(this.size, {})
			}

			if (this.mr) {
				style.marginRight = this.mr
			}
			if (this.ml) {
				style.marginLeft = this.ml
			}
			if (this.mt) {
				style.marginTop = this.mt
			}
			if (this.mb) {
				style.marginBottom = this.mb
			}

			return style
		},
	},
}
</script>

<style scoped>

</style>
