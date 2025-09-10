<template>
	<fb-container height="100%">

		<transition :name="transitions.zoomCenterToCorner">
			<fb-form-view v-if="!!mid" :mid="mid" :param="param"></fb-form-view>
		</transition>
	</fb-container>
</template>

<script>
/*!
 * form builder
 * (c) 2020 lincong1987
 */

import FbFormView from './form-editor/FbFormView'

export default {
	components: {
		FbFormView,
	},

	props: {
		param: {
			type: Object,
			require: false,
			default () {
				return {}
			},
		},
	},

	data () {
		return {
			mid: null,
		}
	},

	async mounted () {

		this.mid = this.$route.params.mid
	},

}
</script>

<style lang="less" scoped>


</style>
