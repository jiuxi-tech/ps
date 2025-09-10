<template>
	<span :class="`${prefix}-link-item`">
		<template v-if="$slots.default">
			<slot></slot>
		</template>
		<template v-else-if="to">
			<router-link :to="to"
			             :replace="replace"
			             :append="append"
			             :tag="tag"
			             :class="getLinkClass"
			             :title="title"
			             :style="{color: color}">
				<fb-icon v-if="icon" :name="icon"></fb-icon>
				{{ label }}
			</router-link>
		</template>
		<template v-else-if="href">
			<a :class="getLinkClass" :title="title" :style="{color: color}" :href="href" :target="target">
				<fb-icon v-if="icon" :name="icon" :size="size"></fb-icon>
				{{ label }}
			</a>
		</template>
		<template v-else-if="action">
			<a :class="getLinkClass" :title="title" :style="{color: color}" @click="handleClick">
				<fb-icon v-if="icon" :name="icon" :size="size"></fb-icon>
				{{ label }}
			</a>
		</template>
		<template v-else-if="!isUndefined(label)">
			<a :class="getLinkClass" :title="title" :style="{color: color}" @click="handleClick">
				<fb-icon v-if="icon" :name="icon" :size="size"></fb-icon>
				{{ label }}
			</a>
		</template>
		<template v-else-if="isUndefined(label) && icon">
			<fb-icon :class="getLinkClass" :name="icon" :size="size" :title="title" :style="{color: color}"
			         @on-click="handleClick"></fb-icon>
		</template>

		<span v-if="mySeparator||$slots.separator" :class="`${prefix}-link-separator`">
			<template v-if="$slots.separator">
				<slot name="separator"></slot>
			</template>
			<template v-else>
				{{ mySeparator }}
			</template>
		</span>

	</span>
</template>

<script>
import { closest } from '../../../utils/componentUtils'
import FbIcon from '../../icon/src/FbIcon'

import { prefix } from '../../../../src/config'
import { isUndefined } from 'lodash-es'

/**
 * FbLink
 * (c) 2020 lincong1987
 */
export default {
	name: 'FbLink',
	components: {FbIcon},
	props: {
		size: {
			type: String,
			default: 'm',
			validate (val) {
				return ['xs', 's', 'm', 'l', 'xl', 'xxl'].includes(val)
			},
		},

		click: {
			type: Function,
			default: undefined,
		},

		separator: {
			type: String,
			default: undefined,
		},

		type: {
			type: String,
			default: undefined,
		},

		tag: {
			type: String,
			default: 'a',
		},

		href: {
			type: String,
			default: undefined,
		},

		action: {
			type: String,
			default: undefined,
		},

		to: {
			type: [String, Object],
			default: undefined,
		},

		replace: {
			type: Boolean,
			default: false,
		},

		append: {
			type: Boolean,
			default: false,
		},

		target: {
			type: String,
			default: undefined,
		},

		icon: {
			type: String,
			default: undefined,
		},

		label: {
			type: [String, Number],
			default: undefined,
		},

		title: {
			type: [String, Number],
			default: undefined,
		},

		color: {
			type: String,
			default: undefined,
		},

		// on off hover
		underline: {
			type: String,
			default: 'hover',
		},

		disabled: {
			type: Boolean,
			default: false,
		},

	},
	data () {
		let data = {
			prefix,
		}

		data.mySeparator = this.separator || (data.linkGroup && data.linkGroup.separator || '')

		data.myUnderline = this.underline || (data.linkGroup && data.linkGroup.underline || 'off')

		return data
	},

	created () {
		this.linkGroup = closest(this, 'FbLinkGroup')
	},

	beforeDestroy () {
		this.linkGroup = null
	},

	computed: {
		getLinkClass () {
			let arr = [`${prefix}-link`]

			if (!this.href && !this.to) {
				arr.push(`${prefix}-link--no-href`)
			}
			if (this.myUnderline) {
				arr.push(`${prefix}-link--underline-${this.myUnderline}`)
			}
			if (this.size) {
				arr.push(`${prefix}-link--size-${this.size}`)
			}
			if (this.type) {
				arr.push(`${prefix}-link--${this.type}`)
			}

			return arr
		},
	},

	methods: {

		isUndefined,

		handleClick () {
			this.$emit('click')
			this.$emit('on-click')
			if (this.click) {
				this.click()
			}
		},
	},
}
</script>

<style scoped>

</style>
