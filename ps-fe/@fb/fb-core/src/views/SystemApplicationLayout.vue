<template>
	<div class="fb-admin-layout" :style="getLayoutStyle">

		<admin-header
			:height="height"
			:width="width"
			:show-menu="showMenu"
			:menu-width="menuWidth"
			style="z-index: 300;"
		/>

		<admin-menu
			:height="height"
			:width="width"
			:show-menu="showMenu"
			:menu-width="menuWidth"
			style="z-index: 200;"
		/>

		<admin-tabbar
			:height="height"
			:width="width"
			:show-menu="showMenu"
			:menu-width="menuWidth"
			style="z-index: 100;"
		/>

		<admin-main
			:height="height"
			:width="width"
			:show-menu="showMenu"
			:menu-width="menuWidth"
		/>

	</div>
</template>

<script>
	/**
	 * DefaultApplicationLayout
	 * (c) 2020 lincong1987
	 */

	import AdminMain from './components/AdminMain'
	import AdminHeader from './components/AdminHeader'
	import AdminMenu from './components/AdminMenu'
	import AdminTabbar from './components/AdminTabbar'

	export default {

		components: {
			AdminHeader,
			AdminMenu,
			AdminTabbar,
			AdminMain,
		},

		data () {

			return {
				window: this.$(window),
				height: 1080,
				width: 1920,
				menuWidth: 200,
				showMenu: true,
			}

		},

		computed: {

			key () {
				return this.$route.path
			},

			getLayoutStyle () {
				return {
					height: this.height,
					width: this.width,
				}
			},

		},

		methods: {

			getWindowSize () {
				this.height = this.window.height()
				this.width = this.window.width()
			},

		},

		mounted () {

			this.$ebus.$on('TOGGLE_ADMIN_MENU', () => {
				this.showMenu = !this.showMenu
			})

			window.addEventListener('resize', () => {
				this.getWindowSize()
			}, false)
			this.getWindowSize()

			if (this.$store.state.menu.loadingStauts === false) {
				console.log("load menu from layout")
				this.$store.state.menu.loadingStauts = 'loading'
				this.$svc.sys.menu.getMenus().then(data => {
					this.$store.dispatch('menu/load', data)
					this.$store.state.menu.loadingStauts = true
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

<style lang="less" scoped>

	.fb-admin-layout {
		position: absolute;
		left:     0;
		right:    0;
		top:      0;
		bottom:   0;
		overflow: hidden;

		.fb-admin-menu {
			transition: all 0.4s;
		}

		.fb-admin-main {
			transition: all 0.4s;
		}

		.fb-admin-tabbar {
			transition: all 0.4s;
		}


	}


</style>
