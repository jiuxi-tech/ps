<template>
	<span :class="getClass" :style="getStyle" @click="handleClick">
		<slot></slot>
	</span>
</template>

<script>
/**
 * FbTableLayout
 * (c) 2020 lincong1987
 */
import { prefix } from '../../../../src/config'

export default {
	name: 'FbTableLayout',
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

		//
		long: {
			type: Boolean,
			default: true,
		},
	},
	data () {
		return {
			prefix,
		}
	},

	computed: {
		getClass () {
			let arr = [`${prefix}-table-layout`]

			if (this.size) {
				arr.push(`${prefix}-table-layout--${this.size}`)
			}
			if (this.type) {
				arr.push(`${prefix}-table-layout--${this.type}`)
			}
			if (this.ellipsis) {
				arr.push(`${prefix}-table-layout--ellipsis`)
			}
			if (this.long) {
				arr.push(`${prefix}-table-layout--long`)
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

	methods:{
		handleClick(e){
			this.$emit("on-click", e)
		}
	}
}
</script>

<style scoped>

</style>
