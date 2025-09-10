<template>
	<fb-container height="100%">
		<transition :name="transitions.zoomCenterToCorner">
			<fb-form-editor v-if="!!mid" ref="editor" :mid="mid"></fb-form-editor>
		</transition>
	</fb-container>
</template>

<script>
/*!
 * form builder
 * (c) 2020 lincong1987
 */

import FbFormEditor from '../../../../views/sys/custom/form/form-editor/FbFormEditor'

export default {
	components: {
		FbFormEditor,
	},

	data () {
		return {
			mid: null,
		}
	},

	async mounted () {
		this.mid = this.$route.query.mid
	},

}
</script>

<style lang="less" scoped>


</style>
