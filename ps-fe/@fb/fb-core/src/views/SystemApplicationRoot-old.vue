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

			// 2、如果没有本地 token 直接跳转
			if (!token) {
				console.log('2、如果没有本地 token 直接跳转')
				if (this.$route.path != this.$datax.GLOBAL_CONFIG.loginPath) {
					this.$router.replace(this.$datax.GLOBAL_CONFIG.loginPath)
				}
			}

			// 3、检查当前的token是否过期
			if (token) {

				this.$svc.platform.checkToken(token).then(data => {
					if (data.code == -1) {
						console.log('token 过期')
						if (this.$route.path != this.$datax.GLOBAL_CONFIG.loginPath) {
							this.$router.replace(this.$datax.GLOBAL_CONFIG.loginPath)
						}
					} else {
						console.log('token 有效', this.$datax.GLOBAL_CONFIG.mainPath)
						console.log(this.$store.state.menu.loadingStauts, 1111111111111111111111)
						// if(this.$store.state.menu.loadingStauts === false) {
						// 	console.log("load menu from root")
						// 	this.$store.state.menu.loadingStauts = "loading"
						// 	this.$svc.sys.menu.getMenus().then(data => {
						// 		this.$store.dispatch('menu/load', data)
						// 		this.$store.state.menu.loadingStauts = true
						// 	})
						// }


					}
				})
			}
		},

		beforeRouteEnter (to, from, next) {
			console.log(`beforeRouteEnter from ${from.path} to ${to.path}`)
			next()
		},
		beforeRouteUpdate (to, from, next) {
			console.log(`beforeRouteUpdate from ${from.path} to ${to.path}`)
			next()
		},
		beforeRouteLeave (to, from, next) {
			console.log(`beforeRouteLeave from ${from.path} to ${to.path}`)
			next()
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
