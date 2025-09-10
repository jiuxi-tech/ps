<template>
	<div id="app" class="root">
		<transition name="fade-transform">
			<!--			<keep-alive>-->
			<router-view/>
			<!--			</keep-alive>-->
		</transition>
	</div>
</template>

<script>
	/**
	 * DefaultApplicationLayout
	 * (c) 2020 lincong1987
	 */

	export default {

		mounted () {

			// 1、检测本地是否存在 token
			let token = this.$datax.get('token')

			// 2、检查当前的token是否过期
			if (token) {
				this.$svc.platform.checkToken(token).then(data => {
					if (data.code == -1) {
						if (this.$route.path != this.$datax.GLOBAL_CONFIG.loginPath) {
							this.$router.replace(this.$datax.GLOBAL_CONFIG.loginPath)
						}
					} else {
						// this.$router.replace(this.$datax.get('ADMIN_INDEX_PATH'))
					}
				})
			}
		},

	}
</script>

<style lang="less">

	/*.root {*/
	/*	border: 5px solid red;*/
	/*}*/

	body {
		overflow: hidden;
	}

</style>
