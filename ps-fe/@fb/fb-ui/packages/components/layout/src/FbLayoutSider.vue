<template>
	<aside :class="`${prefix}-layout-sider`" :style="getStyle">
		<slot></slot>
	</aside>
</template>

<script>
/**
 * FbLayoutSider
 * (c) 2020 lincong1987
 */

import { prefix } from '../../../../src/config'
import { closest } from '../../../utils/componentUtils'
import { getSizeStyle } from '../../../utils/propUtils'

export default {
	name: 'FbLayoutSider',
	props: {

		width: {
			type: [String, Number],
			default: 200,
		},

	},
	data () {
		return {
			prefix,

		}
	},
	computed: {
		getStyle () {
			let style = {}

			style.width = typeof this.width === 'number' ? `${this.width}px` : this.width

			return style
		},
	},

	created () {
		this.layout = closest(this, 'FbLayout')
	},

	beforeDestroy () {
		this.layout = null
	},

	mounted () {

		this.layout && this.layout.setSider(this)

	},
}
</script>

<style scoped>

</style>
