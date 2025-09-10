<template>
	<div :class="getClass" :style="getStyle">
		<slot></slot>
	</div>
</template>

<script>
/**
 * FbSwitchAccordionGroup
 * (c) 2020 lincong1987
 */
import { prefix } from '../../../../src/config'

export default {
	name: 'FbSwitchAccordionGroup',
	props: {
		size: {
			type: String,
			default: '',
			validate (val) {
				return ['s', 'm', 'l', 'xl'].includes(val)
			},
		},
		type: {
			type: String,
			default: null,
		},

		color: {
			type: String,
			default: null,
		},

		ellipsis: {
			type: Boolean,
			default: false,
		},

		width: {
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
			let arr = [`${prefix}-switch-accordion-group`]

			if (this.size) {
				arr.push(`${prefix}-switch-accordion-group--${this.size}`)
			}
			if (this.type) {
				arr.push(`${prefix}-switch-accordion-group--${this.type}`)
			}
			if (this.ellipsis) {
				arr.push(`${prefix}-switch-accordion-group--ellipsis`)
			}
			return arr

		},

		getStyle () {
			let style = {}
			if (this.width) {
				style.width = this.width
			}
			if (this.color) {
				style.color = this.color
			}
			return style
		},
	},
}
</script>

<style scoped>

</style>
