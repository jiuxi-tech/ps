<template>
	<span :class="`${prefix}-breadcrumb-separator`">

		<template v-if="$slots.default">
			  <slot></slot>
		</template>
		<template v-if="!$slots.default">
			/
		</template>

	</span>
</template>

<script>
	import { prefix } from '../../../../src/config'

	/**
	 * FbBreadcrumbSeparator
	 * (c) 2020 lincong1987
	 */
	export default {
		name: 'FbBreadcrumbSeparator',
		props: {
			size: {
				type: String,
				default: '',
				validate (val) {
					return ['s', 'm', 'l', 'xl'].includes(val)
				},
			},
		},
		data () {
			return {
				prefix,
			}
		},
	}
</script>

<style scoped>

</style>
