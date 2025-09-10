<template>

	<div class="fb-admin-menu" :style="getLayoutMenuStyle">

		<div class="fb-admin-menu__toggle" @click="toggle" :class="getToggleClass">
			<fb-icon :name="showMenu ? 'left':'right'"></fb-icon>
		</div>

		<div class="fb-admin-menu__header">
			<fb-icon :name="$store.state.menu.subMenu.icon || 'module'"></fb-icon>
			{{$store.state.menu.subMenu.text || ''}}
		</div>

		<!--		SYS1901:菜单 SYS1902:接口 SYS1903:按钮-->
		<div class="fb-admin-menu__content">
			<fb-spin color="#fff" :show="$store.state.menu.loadingStauts=='loading'">
				<div
					:class="{level1: true, active: (level1.code == activeLevel1)}"
					v-for="(level1, i) in filterMenu($store.state.menu.subMenu.children || [])"
					:key="level1.code"
				>
					<div class="level1__title" @click="level1Click(level1)">
						<fb-icon :name="level1.code == activeLevel1 ? 'down' : 'right'"></fb-icon>
						{{level1.text}}
					</div>
					<fb-collapse-transition>
						<ul class="level1__body" v-show="(level1.code == activeLevel1)">
							<div class="level2"
								 :class="{active: (level2.code == activeLevel2)}"
								 v-for="(level2, i) in filterMenu(level1.children || [])" :key="level2.code">
								<div class="level2__title" @click="level2Click(level2)">{{level2.text}}</div>
							</div>
						</ul>
					</fb-collapse-transition>
				</div>
			</fb-spin>
		</div>


	</div>
</template>

<script>
	import { mapMutations, mapGetters, mapState, mapActions } from 'vuex'
	import { filter, flattenDeep, each, find } from 'lodash-es'

	/**
	 * AdminMenu
	 * (c) 2020 lincong1987
	 */


	export default {
		name: 'AdminMenu',

		props: {
			routerViewKey: [String, Number],
			height: [String, Number],
			width: [String, Number],
			menuWidth: [String, Number],
			showMenu: true,
		},

		data () {
			return {
				myMenu: {},

				activeLevel1: '',
				activeLevel2: '',
			}
		},

		computed: {

			...mapState(['menu', 'tabbar']),

			getToggleClass () {
				var arr = []
				if (this.showMenu === false) {
					arr.push('fb-admin-menu__toggle--show-tail')
				}
				return arr
			},

			getLayoutMenuStyle () {
				return {
					left: `${this.showMenu ? 0 : -200}px`,
					height: `${this.height - 64}px`, // width: this.width,
				}
			},
		},

		watch: {
//			'menu.subMenu' () {
//				this.myMenu = this.menu.subMenu
//			},

			menu: {
				loadingStauts(value){
					console.log(value)
				}
			}
		},

		methods: {

			flat (arr, res) {
				arr.forEach(item => {
					res.push(item)
					if (item.children && item.children.length > 0) {
						this.flat(item.children, res)
					}
				})
			},

			filterMenu (menus) {
				return filter(menus, (menu) => {
					return menu.type === 'SYS1901'
				})

			},

			level1Click (menu) {

				if (this.activeLevel1 == menu.code) {
					this.activeLevel1 = ''
				} else {
					this.activeLevel1 = menu.code
					if (menu.path) {
						this.open(menu)
					}
				}
			},

			level2Click (menu) {
				this.activeLevel2 = menu.code
				this.open(menu)
			},

			toggle () {

				this.$ebus.$emit('TOGGLE_ADMIN_MENU', !this.showMenu)

			},

			open (menu) {

				console.log('open', menu.text, menu.path)

				let tab = find(this.tabbar.tabs, (tab) => {
					return tab.code == menu.code
				})

				let hasPermission = false

				if (this.$store.state.menu.menus.length > 0) {
					// 1、检查该path是否在菜单中
					// let sysMenus = []
					// this.flat(this.$store.state.menu.menus, sysMenus)
					// let hasMenu = find(sysMenus, (sysMenu) => {
					// 	return sysMenu.path == menu.path
					// })
					// if (!hasMenu) {
					// 	this.$message.error(`菜单 ${menu.text} 未配置菜单，不能被打开`)
					// 	return
					// }

					// 2、检查该path是否在路由表中
					// let sysRouters = []
					// this.flat(this.$router.options.routes, sysRouters)
					// let hasRouter = find(sysRouters, (route) => {
					// 	return route.path == menu.path
					// })

					let matched = this.$router.match(menu.path)

					if (matched.name == 'UnknowPage') {
						this.$message.error(`菜单 ${menu.text} 未配置路由，请先添加`)
						return
					}

					// console.log(`open [${menu.text}] ${menu.path} hasMenu: ${JSON.stringify(
					// 	hasMenu)} hasRouter: ${JSON.stringify(hasRouter)}`)
					// return
					// if (hasMenu) {
					// 	hasPermission = true
					// }
					//
					// if (hasPermission) {

					if (!tab) {

						menu.focus = true

						this.$store.commit('tabbar/add', menu)

						//if (this.$store.commit('tabbar/isCreated', menu)) {

						// this.$router.push(menu.path)
						// this.$store.commit('tabbar/add', menu)

						//} else {
						this.checkPush(menu.path)
						//this.$store.commit('tabbar/add', menu)
						//}

					} else {
						this.checkPush(menu.path)
						//this.$store.commit('tabbar/focus', menu)
					}
					//
					// } else {
					//
					// 	this.$router.push('/403')
					// }
				}

				return

				// console.log('open', menu)
				//
				// if (this.$store.commit('tabbar/isCreated', menu)) {
				//
				// 	// this.$router.push(menu.path)
				// 	// this.$store.commit('tabbar/add', menu)
				//
				// } else {
				// 	this.$router.push(menu.path)
				// 	this.$store.commit('tabbar/add', menu)
				// }

			},

			checkPush (path) {
				if (this.$router.currentRoute.path == path) {
					//this.$router(path)
				} else {
					this.$router.push(path)
				}

			},

			addTabbar () {

				let tab = {
					icon: 'home',
					name: '主页',
					code: 'xxxx',
					path: '/dashboard',
				}

				this.$router.push(tab.path)

				this.$store.commit('tabbar/add', tab)

			},

		},

	}
</script>

<style lang="less" scoped>


	.fb-admin-menu {
		position:   absolute;
		top:        64px;
		left:       0;
		width:      200px;
		height:     704px;
		background: #0284FE url("../../public/img/menu-bg.png") no-repeat 0 bottom;

		&:hover {
			.fb-admin-menu__toggle {
				opacity: 1;
			}
		}
	}

	.fb-admin-menu__toggle {
		position:      absolute;
		top:           7px;
		left:          187px;
		width:         13px;
		height:        27px;
		line-height:   27px;
		background:    rgba(0, 0, 0, 0.2);
		border-radius: 2px 0px 0px 2px;
		color:         #fff;
		cursor:        pointer;
		transition:    background 0.4s;
		text-align:    center;
		font-size:     12px;
		opacity:       0;

		.fb-icon {
			font-size: 12px;
		}

		&:hover {
			background: rgba(0, 0, 0, 0.4);
		}

		&.fb-admin-menu__toggle--show-tail {
			left:    200px;
			opacity: 1;
		}
	}

	.fb-admin-menu__header {
		height:      50px;
		line-height: 50px;
		color:       #fff;
		font-size:   14px;
		padding:     0 16px;

		> .fb-icon {
			margin-right: 8px;
		}

	}


	.fb-admin-menu__content {

		padding: 0 8px;

		.level1 {

			.level1__title {

				width:         184px;
				height:        32px;
				border-radius: 4px;
				color:         #fff;
				cursor:        pointer;
				line-height:   32px;
				padding:       0 12px;
				user-select:   none;

				.fb-icon {
					font-size: 12px;
					transform: scale(0.8);
				}

			}

			.level1__body {
				margin:  0;
				padding: 0;
			}

			&.active {
				.level1__title {
				}

				.level1__body {
				}
			}

		}


		.level2 {

			.level2__title {
				user-select:   none;
				width:         184px;
				height:        32px;
				border-radius: 4px;
				color:         #fff;
				cursor:        pointer;
				line-height:   32px;
				padding:       0 12px 0 38px;
				transition:    all 0.4s;
			}

			&.active {
				.level2__title {
					background: rgba(255, 255, 255, 0.2);
				}

				.level2__body {
				}
			}

		}
	}


	/* show-hide-menu */
	.show-hide-menu-leave-active,
	.show-hide-menu-enter-active {
		/*transition: all .3s;*/
	}

	.show-hide-menu-enter {
		transform: scaleY(0);

	}

	.show-hide-menu-leave {
		transform: scaleY(1);
	}


	@keyframes rcMenuOpenSlideUpIn {
		0% {
			opacity:          0;
			transform-origin: 0% 0%;
			transform:        scaleY(0);
		}
		100% {
			opacity:          1;
			transform-origin: 0% 0%;
			transform:        scaleY(1);
		}
	}

	@keyframes rcMenuOpenSlideUpOut {
		0% {
			opacity:          1;
			transform-origin: 0% 0%;
			transform:        scaleY(1);
		}
		100% {
			opacity:          0;
			transform-origin: 0% 0%;
			transform:        scaleY(0);
		}
	}

</style>
