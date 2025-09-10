<template>
	<div class="fb-exception-page">
		<not-page imgSrc="assets/img/notpage/500.png" @on-click="$router.back()" text="500" message="连接超时，请检查网络是否通畅！"></not-page>
	</div>
</template>

<script>
	/**
	 * Execption
	 * (c) 2020 lincong1987
	 */
	import NotPage from '../../../components/notpage/not-page'

	export default {
		routerConfig: {
			path: '/500'
		},
		name: 'Exception',
		components: {
			NotPage
		}
	}
</script>

<style lang="less" scoped>
	.fb-exception-page {
		position: absolute;
		width: 100%;
		top: 50%;
		margin-top: -166px;
		text-align: center;
	}
</style>
