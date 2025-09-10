<template>
	<div :class="getClass">
		<slot/>
	</div>
</template>

<script>
/**
 * FbLayout
 * (c) 2020 lincong1987
 */

import { find } from '../../../utils/componentUtils'

import { prefix } from '../../../../src/config'

export default {
	name: 'FbLayout',
	props: {
		// 布局样式 default 默认样式 blank 空白，骨架
		theme: {
			type: [String],
			default: 'default',
		},
	},
	data () {
		return {
			prefix,
			sider: null,
		}
	},

	computed: {
		getClass () {
			let arr = [`${prefix}-layout`]

			if (this.sider) {
				arr.push(`${prefix}-layout--has-sider`)
			}

			if (this.theme) {
				arr.push(`${prefix}-layout--${this.theme}`)
			}

			return arr
		},
	},

	methods: {

		// sider 上来就跟 layout 讲, 你喜当爹了
		setSider (sider) {
			this.sider = sider
		},

	},

}
</script>

<style scoped>

</style>
