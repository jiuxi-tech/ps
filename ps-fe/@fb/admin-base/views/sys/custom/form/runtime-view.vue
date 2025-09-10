<template>
	<fb-container height="100%">

		<transition :name="transitions.zoomCenterToCorner">
			<fb-form-view v-if="mid || mcode" :mid="mid" :mcode="mcode" :param="param"></fb-form-view>
		</transition>
	</fb-container>
</template>

<script>
/*!
 * form builder
 * (c) 2020 lincong1987
 */

import FbFormView from '../../../../views/sys/custom/form/form-editor/FbFormView'

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
			mcode: null,
		}
	},

	async mounted () {

		this.mid = this.$route.params.mid
		this.mcode = this.$route.params.mcode
	},

}
</script>

<style lang="less" scoped>


</style>
