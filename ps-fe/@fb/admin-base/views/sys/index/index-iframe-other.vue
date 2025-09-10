<template>
	<div class="tx-demo-page fb-page-search">
		<iframe src="http://192.168.90.137:8080/" frameborder="0" style="width: 100%; height: 100%"></iframe>
	</div>

</template>

<script>


	export default {
		name: 'index-iframe',
		// 接收父组件的传参
		props: {

		},
		data () {
			return {

			}
		},
		// 初始化方法
		mounted () {

		},

		// 方法
		methods: {

		}
	}
</script>

<style lang="less" scoped>
	@import "@/assets/styles/common.less";
	.tx-demo-page {
		color:     #313C47;
		font-size: 14px;
		width: 100%;
		height: 100%;
		padding: 0;
	}
	.swiper-container {
		width: 100%;
		height: 300px;
	}

</style>

