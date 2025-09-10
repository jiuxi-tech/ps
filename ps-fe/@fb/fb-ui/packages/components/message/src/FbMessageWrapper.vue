<template>
	<div :class="getClass"/>

</template>

<script>
	import { prefix } from '../../../../src/config'
	/**
	 * FbMessageWrapper
	 * (c) 2020 lincong1987
	 */
	export default {
		name: 'FbMessageWrapper',
		props: {
			align: {
				type: String,
				default: 'center',
			},
		},
		data () {
			return {
				prefix,
			}
		},
		computed: {
			getClass () {
				let arr = [
					`${prefix}-message-wrapper`,
					`${prefix}-message-wrapper--${this.align}`
				]
				return arr
			},
		},
	}
</script>

<style scoped>

</style>
