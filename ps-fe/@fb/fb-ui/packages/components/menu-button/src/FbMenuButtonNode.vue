<template>
	<ul :class="[
		`${prefix}-menu-button-node`, {
			[`${prefix}-menu-button-node--disabled`]: !!disabled
		}
	]">
		<li :class="`${prefix}-menu-button-node__header`" :style="{color: color}" @click="handleClick">
			<slot>
				<fb-icon v-if="icon" :name="icon" size="xs"></fb-icon>
				{{ label }}
			</slot>
		</li>

		<!--		<li v-if="children" :class="`${prefix}-menu-button-node__children`">-->
		<!--			<fb-menu-button-node-->
		<!--				v-for="(node, i) in (children||[])" :key="i"-->
		<!--				:icon="node.icon"-->
		<!--				:label="node.label"-->
		<!--				:value="node.value"-->
		<!--				:children="node.children"-->
		<!--			/>-->
		<!--		</li>-->
	</ul>
</template>

<script>
/*!
 * FbMenuButtonNode
 * (c) 2021 lincong1987
 */

import FbIcon from '../../icon/src/FbIcon'
import { prefix } from '../../../../src/config'
import { closest } from '../../../utils/componentUtils'

export default {
	name: 'FbMenuButtonNode',

	components: {
		FbIcon,
	},

	props: {

		// 图标名称
		icon: {
			type: String,
			default: '',
		},
		index: {
			type: [String, Number],
			default: '',
		},
		label: {
			type: String,
			default: '',
		},
		color: {
			type: String,
			default: '',
		},

		value: {
			type: [String, Number, Boolean],
			default: undefined,
		},

		disabled: {
			type: Boolean,
			default: false,
		},

		children: {
			type: Array,
			default: undefined,
		},
	},
	data () {
		return {
			prefix,
			showMenu: false,

		}
	},
	created () {
		this.fbMenuButton = closest(this, 'FbMenuButton')
	},

	beforeDestroy () {
		this.fbMenuButton = null
	},
	methods: {
		handleClick (event) {
			if (this.disabled) {
				return
			}

			this.fbMenuButton && this.fbMenuButton.handleMenuClick(this.value, this)

		},
	},
}
</script>

<style scoped>

</style>
