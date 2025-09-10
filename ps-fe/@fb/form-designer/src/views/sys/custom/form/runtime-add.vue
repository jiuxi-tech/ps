<template>
	<fb-form-add v-if="!!mid" :mid="mid" :param="param"></fb-form-add>
</template>

<script>
/*!
 * form builder
 * (c) 2020 lincong1987
 */

import FbFormAdd from './form-editor/FbFormAdd'

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
		}
	},

	async mounted () {
		this.mid = this.$route.params.mid
	},

}
</script>

<style lang="less" scoped>


</style>
