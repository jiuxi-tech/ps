<template>
	<span :class="getClass" :style="getStyle" @click="handleClick">
		<slot>{{ value }}</slot>
	</span>
</template>

<script>
/**
 * FbTableLayoutCell
 * (c) 2020 lincong1987
 */
import { prefix } from '../../../../src/config'

export default {
	name: 'FbTableLayoutCell',
	props: {

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

		align: {
			type: String,
			default: 'left',
		},

		valign: {
			type: String,
			default: null,
		},

		padding: {
			type: [String, Number],
			default: null,
		},

		value: {
			type: [String, Number, Boolean, Object, Array],
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
			let arr = [`${prefix}-table-layout-cell`]

			if (this.size) {
				arr.push(`${prefix}-table-layout-cell--${this.size}`)
			}
			if (this.type) {
				arr.push(`${prefix}-table-layout-cell--${this.type}`)
			}
			if (this.ellipsis) {
				arr.push(`${prefix}-table-layout-cell--ellipsis`)
			}
			if (this.align) {
				arr.push(`${prefix}-table-layout-cell--${this.align}`)
			}
			if (this.valign) {
				arr.push(`${prefix}-table-layout-cell--valign-${this.valign}`)
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

			if (this.padding) {
				style.padding = this.padding
			}

			return style
		},
	},
	methods: {
		handleClick (e) {
			this.$emit('on-click', e)
		},
	},
}
</script>

<style scoped>

</style>
