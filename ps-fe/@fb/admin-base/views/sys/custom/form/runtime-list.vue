<template>
	<fb-container height="100%">
		<transition :name="transitions.zoomCenterToCorner">
			<fb-form-search  ref="editor" :mcode="mcode" v-if="!!mcode" />
		</transition>
	</fb-container>
</template>

<script>
/*! 实际      s
 * form builder
 * (c) 2020 lincong1987
 */


import FbFormSearch from '../../../../views/sys/custom/form/form-editor/FbFormSearch'
export default {
	components: {
		FbFormSearch,
	},

	data () {
		return {
			mcode: null,
		}
	},

	async mounted () {
		this.mcode = this.$route.params.mcode
	},

}
</script>

<style lang="less" scoped>


</style>
