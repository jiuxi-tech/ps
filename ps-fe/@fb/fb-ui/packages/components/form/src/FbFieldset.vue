<template>
	<div :class="`${prefix}-form-fieldset`">
		<div :class="`${prefix}-form-fieldset__legend`" v-if="label || $slots.label">
			<slot name="label">{{ label }}</slot>
		</div>
		<slot></slot>
	</div>
</template>

<script>
	/**
	 * FbFieldset
	 * (c) 2020 lincong1987
	 */
	import { prefix } from '../../../../src/config'

	export default {

		name: 'FbFieldset',

		props: {
			// 表单 的 legend
			label: {
				type: String,
				default: null,
			},
		},

		data () {
			return {
				prefix,
			}
		},
	}
</script>

<style lang="less" scoped>

</style>
