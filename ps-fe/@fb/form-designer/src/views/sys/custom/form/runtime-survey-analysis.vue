<template>
	<fb-container>
		统计分析
	</fb-container>
</template>

<script>
/*!
 * form builder
 * (c) 2020 lincong1987
 */


export default {
	components: {
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
	},

}
</script>

<style lang="less" scoped>


</style>
