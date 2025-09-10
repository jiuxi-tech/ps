<template>
	<div :class="`${prefix}-list-item-meta`">
		<div :class="`${prefix}-list-item-meta-avatar`" v-if="$slots.avatar">
			<slot name="avatar"></slot>
		</div>
		<div :class="`${prefix}-list-item-meta-content`">
			<div :class="`${prefix}-list-item-meta-title`" v-if="$slots.title || title">
				<slot name="title">{{title}}</slot>
			</div>
			<div :class="`${prefix}-list-item-meta-description`" v-if="$slots.description || description">
				<slot name="description">{{description}}</slot>
			</div>
		</div>
		<div :class="`${prefix}-list-item-actions`" v-if="$slots.actions">
			<slot name="actions"></slot>
		</div>
	</div>
</template>

<script>

	import { prefix } from '../../../../src/config'
	/**
	 * FbListItemMeta
	 * (c) 2020 lincong1987
	 */

	export default {
		name: 'FbListItemMeta',
		props: {

			title: {
				type: String,
				default: null,
			},

			description: {
				type: String,
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
				let arr = [`${prefix}-list-item`]

				return arr
			},
		},

	}
</script>

<style scoped>

</style>
