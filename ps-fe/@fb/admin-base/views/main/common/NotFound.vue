<template>
	<div class="fb-unknow-page">
		<not-page imgSrc="assets/img/notpage/404.png" @on-click="$router.back()"></not-page>
	</div>
</template>

<script>
	/**
	 * 404
	 * (c) 2020 lincong1987
	 */
	import NotPage from '../../../components/notpage/not-page'

	export default {
		routerConfig: {
			path: '/404'
		},
		name: 'NotFound',
		components: {
			NotPage
		}
	}
</script>

<style lang="less" scoped>
	.fb-unknow-page {
		position: absolute;
		width: 100%;
		top: 50%;
		margin-top: -166px;
		text-align: center;
	}
</style>
