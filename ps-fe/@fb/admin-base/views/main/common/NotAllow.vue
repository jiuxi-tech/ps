<template>
	<div>
		NotAllow
		<fb-button @on-click="$router.back()" >return</fb-button>
	</div>
</template>

<script>
	/**
	 * NotAllow
	 * (c) 2020 lincong1987
	 */

	export default {
		routerConfig: {
			path: '/403',
			redirect: '/404'
		},
		name: 'NotAllow',
	}
</script>

<style lang="less" scoped>

</style>
