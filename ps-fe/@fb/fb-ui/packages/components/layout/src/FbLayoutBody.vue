<template>
	<div :class="`${prefix}-layout-body`">
		<slot></slot>
	</div>
</template>

<script>
	/**
	 * FbLayoutBody
	 * (c) 2020 lincong1987
	 */

	import { prefix } from '../../../../src/config'

	export default {
		name: 'FbLayoutBody',
		props: {},
		data () {

			return {
				prefix,
			}
		},
	}
</script>

<style scoped>

</style>
