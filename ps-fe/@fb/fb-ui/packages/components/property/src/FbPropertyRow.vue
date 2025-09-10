<template>
	<tr :class="getClass">
		<slot></slot>
	</tr>
</template>

<script>
	/**
	 * FbPropertyRow
	 * (c) 2020 lincong1987
	 */
	import { prefix } from '../../../../src/config'

	export default {
		name: 'FbPropertyRow',
		props: {
			size: {
				type: String,
				default: '',
				validate (val) {
					return ['s', 'm', 'l', 'xl'].includes(val)
				},
			},

			label: {
				type: String,
				default: '',
			},

			value: {
				type: String,
				default: '',
			},

			span: {
				type: [String, Number],
				default: 1,
			},

		},

		data () {
			return {
				prefix,
			}
		},

		computed: {
			getClass () {
				let arr = [`${this.prefix}-property-row`]

				if (this.bordered) {

				}

				return arr
			},
		},
	}
</script>

<style scoped>

</style>
