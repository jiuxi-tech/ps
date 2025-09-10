<template>
	<fb-container height="100%" relative>
		<fb-container absolute z-index="1" top="4px" left="4px">
			<fb-button icon="refresh" @on-click="doTestReload" type="primary" round></fb-button>
		</fb-container>

		<transition :name="transitions.zoomCenterToCorner">
			<fb-form-search v-if="!!mid" ref="editor" :mid="mid" mode="test"/>
		</transition>
	</fb-container>
</template>

<script>
/*!设计  s
 * form builder
 * (c) 2020 lincong1987
 */

import FbFormSearch from './form-editor/FbFormSearch'

export default {
	components: {
		FbFormSearch,
	},

	data () {
		return {
			mid: null,
			doTestReloadLoading: false,
		}
	},
	methods: {
		doTestReload () {
			this.mid = null
			this.$nextTick(() => {
				this.mid = this.$route.params.mid
			})
		},
	},

	async mounted () {
		this.mid = this.$route.params.mid
	},

}
</script>

<style lang="less" scoped>


</style>
