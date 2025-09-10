<template>
	<div
		:class="[
			`${prefix}-notification-wrapper`,
			`${prefix}-notification-wrapper--${placement}`,
		]" v-if="show">
		<slot></slot>
	</div>
</template>

<script>

import { prefix } from '../../../../src/config'

/**
 * FbNotificationWrapper
 * (c) 2020 lincong1987
 */
export default {
	name: 'FbNotificationWrapper',
	props: {
		placement: {
			type: String,
			default: 'bottom-right',
		},

	},

	data () {
		return {
			prefix,
			show: true,
		}
	},
	computed: {},
	methods: {
		hide () {
			this.show = false
		},
	},
}
</script>

<style scoped>

</style>
