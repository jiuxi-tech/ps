<template>
	<div class="fb-admin-layout" :class="theme" :style="getLayoutStyle">

		<admin-header v-if="this.indexPath || this.indexPath !== null" :height="height" :width="width"
			:show-menu="showMenu" :menu-width="menuWidth" @change-theme="handleChangeTheme" style="z-index: 300;" />

		<admin-menu :height="height" :width="width" :show-menu="showMenu" :menu-width="menuWidth"
			@on-drag-move="handleDragMove" style="z-index: 200;" />

		<admin-tabbar v-if="this.indexPath || this.indexPath !== null" :height="height" :width="width"
			:show-menu="showMenu" :menu-width="menuWidth" style="z-index: 100;" />

		<admin-main :height="height" :width="width" :show-menu="showMenu" :menu-width="menuWidth" />
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
import { mapGetters } from 'vuex'

export default {
	routerConfig: {
		path: '/main1',
		name: 'main1',
		meta: {
			title: '后台1',
			rank: '1', // 等级
			whiteList: ['./sys', './main/common'],
		},
	},

	components: {
		AdminHeader,
		AdminMenu,
		AdminTabbar,
		AdminMain,
	},

	data() {
		let theme = app.projectConfig.TX_THEME
		return {

			height: 1080,
			width: 1920,
			menuWidth: this.$datax.get('menuWidth') || 200,
			showMenu: true,
			indexPath: null, // 第一次去的路径
			theme: theme || '',
		}

	},

	 

	computed: {
		...mapGetters({
			flatMenus: 'menu/getFlatMenus',
		}),

		key() {
			return this.$route.path
		},

		getLayoutStyle() {
			return {
				height: this.height,
				width: this.width,
			}
		},
	},
	methods: {
		init() {

			this.window = this.$(window)

			this.$ebus.$on('TOGGLE_ADMIN_MENU', () => {
				this.showMenu = !this.showMenu
				//	console.log(this.showMenu)
				this.menuWidth = this.$datax.get('menuWidth') || 200
			})

			window.addEventListener('resize', () => {
				this.getWindowSize()
			}, false)
			this.getWindowSize()

			let param = getQueryParams(window.location.search)
			//console.log(param, 111111)

			if (this.$store.state.menu.loadingStauts === false) {
				//console.log('load menu from layout')
				this.$store.state.menu.loadingStauts = 'loading'
				// if(param.mcode == 'HZ_PRODUCE_ENT' || param.mcode == 'HZ_STORAGE_ENT' || param.mcode == 'GD_CAR_WARN'){
				// 	this.$svc.sys.menu.getScreenMenu({menuCode: param.mcode}).then((result) => {
				// 		// 判断code
				// 		if (result.code == 1) {
				// 			let data = result.data;
				// 			if(data && data.length > 0){
				// 				this.$store.commit('menu/load', data)
				// 				if (data[0]) {
				// 					this.indexPath = searchIndexPath(data[0])
				// 					if (this.indexPath && !this.$route.query['_blank']) {
				// 						this.$datax.set('ADMIN_INDEX_PATH', this.indexPath)
				// 						// this.$router.replace(this.$datax.GLOBAL_CONFIG.mainPath)
				// 						// this.$router.replace(this.indexPath)
				// 					}
				// 				}
				// 			}

				// 		} else {
				// 			// 服务器返回失败
				// 			this.$message.error('错误提示:' + result.message)
				// 		}
				// 	})
				// }else{
				this.$svc.sys.menu.getMenus().then(data => {
					if (data && data.length > 0) {
						this.$store.commit('menu/load', data)

						if (data[0]) {

							this.indexPath = searchIndexPath(data[0])
							if (this.indexPath && !this.$route.query['_blank']) {
								this.$datax.set('ADMIN_INDEX_PATH', this.indexPath)
								// this.$router.replace(this.$datax.GLOBAL_CONFIG.mainPath)
								// this.$router.replace(this.indexPath)
							}
						}
					}
				})
				// }
			} else {
				this.indexPath = this.$datax.get('ADMIN_INDEX_PATH')
			}

			function searchIndexPath(menu) {
				if (menu.path) {
					return menu.path
				} else if (menu.children.length > 0) {
					return searchIndexPath(menu.children[0])
				}
			}

			function getQueryParams(queryString) {
				const params = {};
				queryString = queryString.replace(/^\?/, '');

				queryString.split('&').forEach(pair => {
					const [key, value] = pair.split('=');
					params[key] = decodeURIComponent(value || '');
				});

				return params;
			}
		},

		getWindowSize() {
			this.height = this.window.height()
			this.width = this.window.width()
		},
		handleChangeTheme(theme) {
			 
			this.theme = theme

			const htmlEl = document.getElementsByTagName('html')[0]
			htmlEl.className = ''
			htmlEl.className = `theme-${theme}`

			app.$datax.set('src.theme', theme)
		},

		handleDragMove(e) {
			// console.log(e)
			const offsetWidth = window.document.body.offsetWidth
			const minWidth = offsetWidth / 2
		//	console.log(minWidth, this.menuWidth)
			if (this.menuWidth <= minWidth || e.clientX <= minWidth) {
				this.menuWidth = e.clientX - 2
				this.$datax.set('menuWidth', this.menuWidth)
			}
		},
	},

	created() {
		this.init()
	},

	activated() {
		this.init()
	},

	mounted() {

		this.$nextTick(() => {
			this.handleChangeTheme(window.localStorage.getItem('TX_THEME') || this.theme)
		})
	},

	// beforeRouteEnter (to, from, next) {
	// 	console.log(`beforeRouteEnter from ${from.path} to ${to.path}`)
	// 	next((vm) => {
	// 		// 刷新的时候先到 main 下
	// 		if (vm.$store.state.menu.loadingStauts != true) {
	// 			// console.log('发现未授权访问,直接跳出')
	// 			next(vm.$datax.GLOBAL_CONFIG.mainPath)
	// 			// setTimeout(() => {
	// 			// 	//location.reload()
	// 			// }, 1000)
	// 		}
	// 	})
	// },
	// beforeRouteUpdate (to, from, next) {
	// 	console.log(`beforeRouteUpdate from ${from.path} to ${to.path}`)
	// 	next()
	// },
	// beforeRouteLeave (to, from, next) {
	// 	console.log(`beforeRouteLeave from ${from.path} to ${to.path}`)
	// 	next()
	// },
}
</script>

<style lang="less" scoped></style>
