<template>
	<div class="fb-unknow-page">
		<div>
			这是一个未知的页面
		</div>

		<fb-button @on-click="$router.back()" type="primary" >点击返回</fb-button>
	</div>
</template>

<script>
  /**
   * UnknowPage
   * (c) 2020 lincong1987
   */

  export default {
    name: 'UnknowPage',
  }
</script>

<style lang="less" scoped>

	.fb-unknow-page {

	}

</style>
