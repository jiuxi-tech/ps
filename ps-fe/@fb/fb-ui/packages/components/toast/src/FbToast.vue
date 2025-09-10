<template>
	<div :class="[
		`${prefix}-toast`
		]"
	>
		<div :class="[
		  `${prefix}-toast`,
		  `${prefix}-toast--` + type

		]">
			<fb-icon v-if="getType" :type="getType"/>
			<span v-if="message">{{message}}</span>
			<slot v-if="$slots"></slot>
		</div>
	</div>
</template>

<script>
	/**
	 * FbToast
	 * (c) 2020 lincong1987
	 */

	import FbIcon from '../../icon/src/FbIcon'
	import { prefix } from '../../../../src/config'

	export default {
		name: 'FbToast',
		components: {FbIcon},
		props: {
			message: {
				type: [String, Number],
				default: undefined,
			},
			type: {
				type: String,
				default: 'info',
				validate (value) {
					return ['info', ''].indexOf(value) != -1
				},
			},
		},
		data () {

			return {
				prefix,
				toastType: {
					'info': '',

				},
			}
		},
	}
</script>

<style scoped>

</style>
