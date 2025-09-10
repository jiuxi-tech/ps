<template>
	<div :class="[
		`${prefix}-divider`,
		`${prefix}-divider--` + direction,
		`${prefix}-divider--` + marginSize,
		`${prefix}-divider--label-` + position,
		dashed ? `${prefix}-divider--dashed` : '',

		]"
		 :style="getStyle"
	>
		<div :class="[
		  `${prefix}-divider__label`   ,
		  `${prefix}-divider__label--` + position
		]">
			<template v-if="label">{{ label }}</template>
			<slot v-if="$slots"></slot>
		</div>
	</div>
</template>

<script>

import { prefix } from '../../../../src/config'

/**
 * FbDivider
 * (c) 2020 lincong1987
 */

export default {
	name: 'FbDivider',
	props: {
		label: {
			type: String,
			default: undefined,
		},
		position: {
			type: String,
			default: 'center',
			validate (value) {
				return ['left', 'center', 'right'].indexOf(value) != -1
			},
		},
		direction: {
			type: String,
			default: 'horizontal',
			validate (value) {
				return ['horizontal', 'vertical'].indexOf(value) != -1
			},
		},
		dashed: {
			type: Boolean,
			default: false,
		},
		noPadding: {
			type: Boolean,
			default: false,
		},
		noMargin: {
			type: Boolean,
			default: false,
		},
		// xs, s, m, l, xl, xxl
		marginSize: {
			type: String,
			default: 'm',
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

			if (this.noPadding) {
				style.paddingTop = 0
				style.paddingLeft = 0
				style.paddingRight = 0
				style.paddingBottom = 0
			}
			if (this.noMargin) {
				style.marginTop = 0
				style.marginLeft = 0
				style.marginRight = 0
				style.marginBottom = 0
			}

			return style
		},
	},
}
</script>

<style scoped>

</style>
