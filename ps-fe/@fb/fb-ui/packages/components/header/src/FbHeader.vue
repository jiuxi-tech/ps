<template>
	<div :class="`${prefix}-header`" :style="{height}">
		<slot></slot>
	</div>
</template>

<script>
	/**
	 * FbHeader
	 * (c) 2020 lincong1987
	 */
	import { prefix } from '../../../../src/config'

	export default {
		name: 'FbHeader',

		props: {
			height: {
				type: [String, Number],
				default: '60px',
			},
		},
		data () {
			return {
				prefix,
			}
		}
	}
</script>

<style scoped>

</style>
