<template>
	<div>
		 <div ref="component" :is="component"></div>
	</div>
</template>

<script>
	import { prefix } from '../../../../src/config'
	import { isObject } from 'lodash-es'

	/**
	 * FbComponent
	 * (c) 2020 lincong1987
	 */
	export default {

		name: 'FbComponent',

		props: {
			component: {
				type: Object,
				default: undefined,
			},
		},
		mounted () {
			if (this.component && isObject(this.component)) {

				this.component.$mount && this.component.$mount(this.$refs.component)
			}
		},
	}
</script>

<style scoped>

</style>
