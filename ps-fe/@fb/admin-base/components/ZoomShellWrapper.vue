<!-- 外壳缩放书皮 -->
<template>
	<fb-container v-bind="$attrs" class="zoom-shell-wrapper">
		<slot></slot>
	</fb-container>
</template>

<script>
/**
 * 外壳缩放书皮 - 目前为大屏登录页适配分辨率
 * ZoomShellWrapper.vue
 * (c) 2021 lincong1987
 */

export default {
	name: 'ZoomShellWrapper',

	props: {},

	data () {
		return {
		}
	},

	watch: {
	},

	created () {
	},

	beforeDestroy () {
	},

	mounted () {

	},

	methods: {

	},

}
</script>

<style lang="less" scoped>
	.zoom-shell-wrapper {
		background: linear-gradient(180deg, #031952 0%, #051F59 100%);
		width: 100%;
		height: 100%;
	}
</style>
