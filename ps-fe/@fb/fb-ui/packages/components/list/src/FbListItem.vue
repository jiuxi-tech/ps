<template>
	<li :class="`${prefix}-list-item`">
		<div :class="`${prefix}-list-item-main`">
			<slot></slot>
		</div>
		<div :class="`${prefix}-list-item-extra`" v-if="$slots.extra">
			<slot name="extra"></slot>
		</div>
	</li>
</template>

<script>

	/**
	 * FbListItem
	 * (c) 2020 lincong1987
	 */

	import { prefix } from '../../../../src/config'
	export default {
		name: 'FbListItem',
		components: {},
		props: {},
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
