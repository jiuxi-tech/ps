<!--<template>-->
<!--	<tr>-->
<!--		<th :rowspan="rowspan" :class="`${prefix}-simple-table-th`" ></th>-->
<!--	</tr>-->
<!--</template>-->

<script>
/**
 * FbSimpleTableColumn
 * (c) 2021 lincong1987
 */

export default {

	name: 'FbSimpleTableColumn',

	props: {

		rowspan: {
			type: [String, Number],
			default: '1',
		},

		colspan: {
			type: [String, Number],
			default: '1',
		},

	},
	render (h) {
		return h('th', {
			'class': `${this.prefix}--simple-table-th`,
			'attrs': {
				a:'aaa',
				b:'bbb'
			},
		}, [])
	},
}
</script>

<style lang="less" scoped>

</style>
