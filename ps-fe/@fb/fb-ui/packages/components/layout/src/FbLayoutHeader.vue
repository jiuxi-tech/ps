<template>
	<header :class="`${prefix}-layout-header`">
		<slot></slot>
	</header>
</template>

<script>
	/**
	 * FbLayoutHeader
	 * (c) 2020 lincong1987
	 */

	import { prefix } from '../../../../src/config'

	export default {
		name: 'FbLayoutHeader',
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
