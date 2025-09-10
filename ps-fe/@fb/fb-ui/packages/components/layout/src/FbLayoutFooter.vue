<template>
	<footer :class="`${prefix}-layout-footer`"
	>
		<slot></slot>
	</footer>
</template>

<script>
	/**
	 * FbLayoutFooter
	 * (c) 2020 lincong1987
	 */

	import { prefix } from '../../../../src/config'

	export default {
		name: 'FbLayoutFooter',
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
