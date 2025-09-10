<template>
	<fb-form-add v-if="mid || mcode" :mid="mid" :mcode="mcode" :param="param"></fb-form-add>
</template>

<script>
/*!
 * form builder
 * (c) 2020 lincong1987
 */

import FbFormAdd from '../../../../views/sys/custom/form/form-editor/FbFormAdd'

export default {
	components: {
		FbFormAdd,
	},
	inject: ['formEditor'],
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
			mode: ''
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
