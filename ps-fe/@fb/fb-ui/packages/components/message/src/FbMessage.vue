<template>
	<div :class="`${prefix}-message-container`">
		<div :class="[
			`${prefix}-message`,
			`${prefix}-message--` + type
		]">
			<fb-icon v-if="myType"
			         :name="myType"
			         :size="16"
			         :rotating="type == 'loading'"/>
			<span>{{message}}</span>
		</div>
	</div>
</template>

<script>
	import { prefix } from '../../../../src/config'
	import FbIcon from '../../icon/src/FbIcon'
	/**
	 * FbMessage
	 * (c) 2020 lincong1987
	 */

	export default {
		name: 'FbMessage',
		components: { FbIcon },
		props: {
			// 消息
			message: {
				type: [String, Number, Error],
				default: null,
			},
			// 类型
			type: {
				type: String,
				default: null,
			},
		},
		data () {
			return {
				prefix,
				icons: {
					warn: 'warning-fill',
					primary: 'information',
					alert: 'information-fill',
					info: 'information-fill',
					confirm: 'question-fill',
					success: 'selected-circle-fill',
					error: 'close-circle-fill',
					danger: 'close-circle-fill',
					warning: 'warning-fill',
					loading: "loading1"
				},
			}
		},
		computed: {
			myType () {
				if (this.type) {
					return this.icons[this.type]
				}
				return 'information'
			},
		},
	}
</script>

<style scoped>

</style>
