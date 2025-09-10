<template>
	<div class="tx-demo-page fb-page-search">
		<swiper class="swiper-no-swiping" ref="mySwiper" :options="swiperOption">
			<!-- slides -->
			<swiper-slide>I'm Slide 1</swiper-slide>
			<swiper-slide>I'm Slide 2</swiper-slide>
			<swiper-slide>I'm Slide 3</swiper-slide>
			<swiper-slide>I'm Slide 4</swiper-slide>
			<swiper-slide>I'm Slide 5</swiper-slide>
			<swiper-slide>I'm Slide 6</swiper-slide>
			<swiper-slide>I'm Slide 7</swiper-slide>
			<!-- Optional controls -->
			<div class="swiper-pagination"  slot="pagination"></div>
		</swiper>
<!--		<iframe src="https://juejin.cn/post/6844903472714743816" frameborder="0" style="width: 100%; height: 100%"></iframe>-->
		<iframe :src="pdfUrl" frameborder="0" style="width: 100%; height: 100%"></iframe>
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
				pdfUrl: '',
				swiperOption: {
					loop : true,
					pagination: {
						el: '.swiper-pagination',
						clickable :true,
					},
					autoplay : {
						disableOnInteraction: false,
						delay: 3000
					},
				},
			}
		},
		// 初始化方法
		mounted () {
			let service = app.$service.service.defaults
			let url = service.target + service.baseURL + `/sys/file/preview-pdf?id=1367458439860584448`
			this.pdfUrl = encodeURI(`pdf/web/viewer.html?file=${encodeURIComponent(url)}&page=1&name=你好`)
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

	}
	.swiper-container {
		width: 100%;
		height: 300px;
	}

</style>

