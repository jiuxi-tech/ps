<template>
	<span :class="getClass" :style="getStyle">
		<slot>{{ myValue }}</slot>
	</span>
</template>

<script>
/**
 * FbTimer
 * (c) 2020 lincong1987
 */
import { prefix } from '../../../../src/config'

export default {
	name: 'FbTimer',
	props: {
		size: {
			type: String,
			default: '',
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

		bold: {
			type: Boolean,
			default: false,
		},

		weight: {
			type: [String, Number],
			default: null,
		},

		align: {
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

		myValue () {
			return this.format()
		},

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
			let style = {}
			if (this.width) {
				style.width = this.width
			}
			if (this.color) {
				style.color = this.color
			}
			if (this.bold) {
				style.fontWeight = 'bold'
			}

			if (this.weight) {
				style.fontWeight = this.weight
			}

			if (this.align) {
				style.textAlign = this.align
			}

			return style
		},
	},
}
</script>

<style scoped>

</style>
