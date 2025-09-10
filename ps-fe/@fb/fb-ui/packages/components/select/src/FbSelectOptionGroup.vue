<template>
	<div/>
</template>

<script>
	/**
	 * FbSelectOptionGroup
	 * (c) 2020 lincong1987
	 */
	import { prefix } from '../../../../src/config'


	export default {
		name: 'FbSelectOptionGroup',

		props: {
			disabled: {
				type: Boolean,
				default: false,
			},
			readonly: {
				type: Boolean,
				default: false,
			},
		},
	}
</script>

<style lang="less" scoped>

</style>
