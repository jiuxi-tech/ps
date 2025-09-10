<template>
	<transition :name="myTransition">
		<div v-show="show" :class="{
			[`${prefix}-tab`]: true,
			[`${prefix}-tab--padding`]: !noPadding,
			[`${prefix}-tab--no-padding`]: noPadding,
		}">
			<slot></slot>
		</div>
	</transition>
</template>

<script>
/**
 * ${prefix} - tab
 * (c) 2020 lincong1987
 */

import { closest } from '../../../utils/componentUtils'
import { prefix } from '../../../../src/config'

export default {
	name: 'FbTab',
	props: {
		name: {
			type: [String, Number],
			default: undefined,
		},
		label: {
			type: [String, Object],
			required: false,
		},
		icon: {
			type: String,
			default: '',
		},
		disabled: {
			type: Boolean,
			default: false,
		},
		hidden: {
			type: Boolean,
			default: false,
		},
		noClose: {
			type: Boolean,
			default: false,
		},

		noPadding: {
			type: Boolean,
			default: false,
		},

		transition: {
			type: String,
			default: null,
		},

	},
	data () {

		return {
			prefix,
			tabName: '',
			iid: '',
			myTransition: this.transition || (this.tabs && this.tabs.transition) || 'tab-fade',
		}
	},
	computed: {
		show () {
			return this.tabName == (this.tabs && this.tabs.activeKey)
		},
	},

	updated() {
		// 防止label插槽绑定值不生效
		if (this.$slots.label) {
			const index = this.tabs.tabs.indexOf(this)
			if (index !== -1) {
				this.tabs && this.tabs.tabs.splice(index, 1, this)
			}
		}

	},
	beforeCreate () {
		this.tabs = closest(this, 'FbTabs')
	},

	beforeDestroy () {
		const index = this.tabs.tabs.indexOf(this)
		if (index !== -1) {
			this.tabs && this.tabs.tabs.splice(index, 1)
		}
		this.tabs = null
	},

	mounted () {
		this.tabs && this.tabs.addTab(this)
	},
	 // destroyed () {
	 // 	const index = this.tabs.tabs.indexOf(this)
	 // 	if (index !== -1) {
	 // 		this.tabs&&this.tabs.removeTab(this, false)
	 // 	}
	 // },
}
</script>

<style scoped>

</style>
