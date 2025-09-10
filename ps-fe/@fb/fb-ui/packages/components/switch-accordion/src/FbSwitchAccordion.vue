<template>
	<div :class="getClass" :style="getStyle">

		<fb-table-layout :class="getHeaderClass">
			<fb-table-layout-cell :class="`${prefix}-switch-accordion__header-title`">
				<fb-icon :name="icon" v-if="icon" size="m"/>
				<slot name="title">{{ title }}</slot>
			</fb-table-layout-cell>
			<fb-table-layout-cell :class="`${prefix}-switch-accordion__header-switch`" align="right">
				<fb-switch v-model="myValue" :disabled="disabled" :readonly="readonly">
					<fb-icon slot="open" name="selected"></fb-icon>
					<fb-icon slot="close" name="close"></fb-icon>
				</fb-switch>
			</fb-table-layout-cell>
		</fb-table-layout>

		<transition name="slide-to-down">
			<div :class="getBodyClass" :style="bodyStyle" v-show="myValue">
				<slot></slot>
			</div>
		</transition>

	</div>
</template>

<script>
/**
 * FbSwitchAccordion
 * (c) 2020 lincong1987
 */
import { prefix } from '../../../../src/config'
import FbTableLayout from '../../../../packages/components/table-layout/src/FbTableLayout'
import FbTableLayoutCell from '../../../../packages/components/table-layout/src/FbTableLayoutCell'
import FbCollapseTransition from '../../../../packages/components/collapse-transition/src/FbCollapseTransition'
import FbSwitch from '../../../../packages/components/switch/src/FbSwitch'
import FbIcon from '../../../../packages/components/icon/src/FbIcon'

export default {
	name: 'FbSwitchAccordion',
	components: {
		FbCollapseTransition,
		FbTableLayoutCell,
		FbTableLayout,
		FbSwitch,
		FbIcon
	},
	props: {

		icon: {
			type: String,
			default: null,
		},

		title: {
			type: String,
			default: null,
		},

		value: {
			type: [String, Boolean, Number],
			default: false,
		},

		bodyStyle: {
			type: [String, Array, Object],
			default: null,
		},

		// 警用
		disabled: {
			type: Boolean,
			default: false,
		},
		// 只读
		readonly: {
			type: Boolean,
			default: false,
		},
	},
	data () {
		return {
			prefix,

			myValue: this.value,
		}
	},

	computed: {
		getClass () {
			let arr = [`${prefix}-switch-accordion`]

			if (this.myValue) {
				arr.push(`${prefix}-switch-accordion--open`)
			} else {
				arr.push(`${prefix}-switch-accordion--close`)
			}

			return arr
		},

		getHeaderClass () {
			let arr = [`${prefix}-switch-accordion__header`]
			return arr
		},

		getBodyClass () {
			let arr = [`${prefix}-switch-accordion__body`]
			return arr
		},

		getStyle () {
			let style = {}
			return style
		},
	},

	watch: {

		value (value) {
			this.myValue = value
		},

		myValue (value) {
			this.$emit('input', value)
			this.$emit('on-change', value)
		},
	},
}
</script>

<style scoped>

</style>
