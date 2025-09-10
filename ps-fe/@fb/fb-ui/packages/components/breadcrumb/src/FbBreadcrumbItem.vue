<template>
	<span :class="`${prefix}-breadcrumb-item`">
		<template v-if="$slots.default">
			<slot></slot>
		</template>
		<template v-else-if="to">
			<router-link :to="to"
			             :replace="replace"
			             :append="append"
			             tag="a"
			             :class="getLinkClass"
			             :title="title"
			             :style="{color: color}">
				<fb-icon v-if="icon" :name="icon" :size="size"></fb-icon>
				{{ label }}
			</router-link>
		</template>
		<template v-else-if="href">
			<a :class="getLinkClass" :href="href" :title="title">
				<fb-icon v-if="icon" :name="icon" :size="size"></fb-icon>
				{{ label }}
			</a>
		</template>
		<template v-else-if="label">
			<span :class="getLinkClass" :title="title">{{ label }}</span>
		</template>
		<template v-else-if="!label && icon">
			<fb-icon :name="icon" :title="title" :size="size"></fb-icon>
		</template>

		<span :class="`${prefix}-breadcrumb-separator`">
			<template v-if="$slots.separator">
				<slot name="separator"></slot>
			</template>
			<template v-if="!$slots.separator">
				        {{ mySeparator }}
			</template>
		</span>

	</span>
</template>

<script>/**
 * FbBreadcrumbItem
 * (c) 2020 lincong1987
 */

import { closest } from '../../../utils/componentUtils'
import FbIcon from '../../icon/src/FbIcon'
import { prefix } from '../../../../src/config'

export default {
	name: 'FbBreadcrumbItem',
	components: {FbIcon},
	props: {
		size: {
			type: String,
			default: 'm',
		},
		separator: {
			type: String,
			default: undefined,
		},

		href: {
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
			type: String,
			default: undefined,
		},

		title: {
			type: String,
			default: undefined,
		},

		color: {
			type: String,
			default: undefined,
		},

		// on off hover
		underline: {
			type: String,
			default: undefined,
		},

	},

	data () {
		let data = {
			prefix,
		}

		data.mySeparator = this.separator || (this.breadcrumb && this.breadcrumb.separator || '/')

		data.myUnderline = this.underline || (this.breadcrumb && this.breadcrumb.underline || 'off')

		return data
	},

	created () {
		this.breadcrumb = closest(this, 'FbBreadcrumb')
	},

	beforeDestroy () {
		this.breadcrumb = null
	},

	computed: {
		getLinkClass () {
			let arr = [`${this.prefix}-breadcrumb-link`]

			if (!this.href && !this.to) {
				arr.push(`${this.prefix}-breadcrumb-link--no-href`)
			}

			if (this.myUnderline) {
				arr.push(`${this.prefix}-breadcrumb-link--underline-${this.myUnderline}`)
			}

			return arr
		},
	},

}
</script>

<style scoped>

</style>
