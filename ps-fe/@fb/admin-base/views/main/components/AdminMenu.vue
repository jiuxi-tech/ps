<template>

	<div class="fb-admin-menu" :style="getLayoutMenuStyle">

		<div class="fb-admin-menu__drag" @mousedown="resizeable = true" @mouseup="resizeable = false"></div>
		<div class="fb-admin-menu__toggle" @click="toggle" :class="getToggleClass">
			<fb-icon :name="showMenu ? 'left' : 'right'"></fb-icon>
		</div>


		<div class="fb-admin-menu__header">
			<fb-icon :name="myMenu.icon || 'module'"></fb-icon>
			{{ myMenu.text || '' }}
		</div>
		<div class="fb-admin-menu_line"></div>
		<!--  SYS1901:菜单 SYS1902:接口 SYS1903:按钮 SYS1904:外部接口 SYS1905:外部菜单  -->
		<div class="fb-admin-menu__content">
			<fb-spin color="#fff" :show="$store.state.menu.loadingStauts == 'loading'">
				<div :class="{ level1: true, active: (level1.id == activeLevel1) }"
					v-for="(level1) in filterMenu(myMenu.children || [])" :key="level1.id">
					<div class="level1__title" :title="level1.text" @click="level1Click(level1)">
						<fb-icon :name="level1.icon || myMenu.icon || 'module'"
							style="transform:none; padding-right: 10px;"></fb-icon>
						<fb-icon :name="(selectedLevel.indexOf(level1.id) >= 0) ? 'down' : 'right'"
							v-show="(filterMenu(level1.children || [])).length > 0"
							style="float:right; margin-top: 10px; transform:scale(0.7)"></fb-icon>
						{{ level1.text }}
					</div>
					<fb-collapse-transition>
						<ul class="level1__body" v-show="(selectedLevel.indexOf(level1.id) >= 0)">
							<div class="level2" :class="{ active: (level2.id == activeLevel2) }"
								v-for="(level2) in filterMenu(level1.children || [])" :key="level2.id">
								<div class="level2__title ellipsis" :title="level2.text" @click="level2Click(level2)">
									{{ level2.text }}
								</div>
							</div>
						</ul>
					</fb-collapse-transition>
				</div>
			</fb-spin>
		</div>


	</div>
</template>

<script>
import { mapState } from 'vuex'
import { cloneDeep, filter, find, throttle } from 'lodash-es'

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

	data() {
		return {
			myMenu: {},

			activeLevel1: '',
			activeLevel2: '',
			selectedLevel: [],
			resizeable: false,
		}
	},

	computed: {

		...mapState(['menu', 'tabbar']),

		getToggleClass() {
			var arr = []
			if (this.showMenu === false) {
				arr.push('fb-admin-menu__toggle--show-tail')
			}
			return arr
		},

		getLayoutMenuStyle() {
			return {
				left: `${this.showMenu ? 0 : -(this.menuWidth)}px`,
				height: `${this.height - 64}px`,
				width: `${this.menuWidth}px`,
			}
		},
	},

	watch: {
		'menu.subMenu': {
			handler() {
				if (this.menu && this.menu.subMenu) {
					this.myMenu = cloneDeep(this.menu.subMenu)
					this.selectedLevel = []
					// 确保 children 存在且是数组
					if (this.myMenu.children && Array.isArray(this.myMenu.children)) {
						this.myMenu.children.map(menu => {
							// 默认全展开
							if (menu.id) {
								this.selectedLevel.push(menu.id)
							}
							// 确保 menu.children 存在且是数组
							if (menu.children && Array.isArray(menu.children)) {
								menu.children = menu.children.filter(item => {
									return item.type === 'SYS1901' || item.type === 'SYS1905'
								})
							} else {
								menu.children = []
							}
						})
					}
				}
			},
			deep: true,
		},
	},
	created() {
		this.throttleDragMove = throttle(this.dragMove, 10)
	},
	mounted() {
		window.addEventListener('mousemove', this.throttleDragMove)
		window.addEventListener('mouseup', this.dragUp)
	},
	beforeDestroy() {
		window.removeEventListener('mousemove', this.throttleDragMove)
		window.removeEventListener('mouseup', this.dragUp)
	},
	methods: {
		dragMove(e) {
			// console.log(e, 'dargMove')
			// e.preventDefault()
			if (this.resizeable) {
				document.body.style.userSelect = 'none'
				this.$emit('on-drag-move', e)
			}
		},

		dragUp() {
			// console.log(e, 'dragUp')
			this.resizeable = false
			document.body.style.userSelect = ''
		},
		// 扁平化 菜单 列表
		flat(arr, res) {
			arr.forEach(item => {
				res.push(item)
				if (item.children && item.children.length > 0) {
					this.flat(item.children, res)
				}
			})
		},

		filterMenu(menus) {
			return filter(menus, (menu) => {
				return menu.type === 'SYS1901' || menu.type === 'SYS1905'
			})

		},

		level1Click(menu) {
			this.activeLevel2 = ''
			if (this.activeLevel1 == menu.id) {
				// this.activeLevel1 = ""
				this.open(menu)
			} else {
				// 点击二级，有三级菜单
				if (menu.children && menu.children.length > 0) {
					if (this.selectedLevel.indexOf(menu.id) >= 0) {
						this.selectedLevel.splice(this.selectedLevel.indexOf(menu.id), 1)
					} else {
						this.selectedLevel.push(menu.id)
					}
				} else {
					// 点击二级，没有三级
					this.activeLevel1 = menu.id
					this.selectedLevel = []
				}
				// 点击二级，有链接
				if (menu.path) {
					if (menu.type === 'SYS1905') {
						// 跳转固定嵌套第三方的页面的路由，并携带当前菜单的path
						this.openThird(menu)
					} else {
						this.open(menu)
					}
				}
			}
		},

		level2Click(menu) {
			this.activeLevel1 = ''
			this.activeLevel2 = menu.id
			// 确保当前二级菜单的父级菜单保持展开状态，但不影响其他菜单
			if (this.selectedLevel.indexOf(menu.pid) < 0) {
				this.selectedLevel.push(menu.pid)
			}

			if (menu.type === 'SYS1905') {
				// 跳转固定嵌套第三方的页面的路由，并携带当前菜单的path
				this.openThird(menu)
			} else {
				this.open(menu)
			}
		},

		toggle() {
			this.$ebus.$emit('TOGGLE_ADMIN_MENU', !this.showMenu)
		},

		openThird(menu) {

			// console.log('open', menu.text, menu.path)

			let tab = find(this.tabbar.tabs, (tab) => {
				return tab.id == menu.id
			})

			if (this.$store.state.menu.menus.length > 0) {

				// console.log(tab, menu)
				if (!tab) {
					menu.focus = true
					this.checkPush('/sys/third/third', menu)

				} else {
					this.checkPush(menu.path)

				}
			}

		},

		open(menu) {

			// console.log('open', menu.text, menu.path)

			let tab = find(this.tabbar.tabs, (tab) => {
				return tab.id == menu.id
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

				console.log(tab, menu)
				if (!tab) {

					menu.focus = true

					// this.$store.commit('tabbar/add', menu)

					//if (this.$store.commit('tabbar/isCreated', menu)) {

					// this.$router.push(menu.path)
					// this.$store.commit('tabbar/add', menu)

					//} else {
					this.checkPush(menu.path, menu)
					//this.$store.commit('tabbar/add', menu)
					//}

				} else {
					this.checkPush(menu.path, menu)
					// this.$store.commit('tabbar/focus', menu.id)
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

		checkPush(path, options) {
			if (this.$router.currentRoute.fullPath == path) return

			if (options && options.type === 'SYS1905') {
				this.$router.push({
					path: path, // 配置的路由
					query: {
						path: options.path,
					},
				})
			} else if (path.includes('/mjyjhik/sso/yqhx') || path.includes('/hikisolated/sso/visitorappt')) {
				this.$router.push({
					path: path, // 配置的路由
					query: {
						tabLabel: options.text,
						id: options.value,
					},
				})
			} else {
				this.$router.push({
					path: path, // 配置的路由
				})
			}

		},

		addTabbar() {

			let tab = {
				id: 'xxxx',
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
@import "../../../assets/styles/common.less";

.fb-admin-menu {
	position: absolute;
	top: 64px;
	left: 0;
	width: 200px;
	height: 704px;
	//background: #0284FE url("../../../assets/img/menu-bg.png") no-repeat 0 bottom;

	&:hover {
		.fb-admin-menu__drag {
			opacity: 1;
			background: url("../../../assets/img/main/menu-drag.svg") no-repeat center #0000004a;
		}

		.fb-admin-menu__toggle {
			opacity: 1;
		}
	}
}

.fb-admin-menu__drag {
	cursor: col-resize;
	border-radius: 2px 0 0 2px;
	width: 10px;
	height: 100%;
	margin-top: 0;
	line-height: 27px;
	position: absolute;
	top: 0;
	left: 100%;
	transition: background, opacity 0.4s;
	background: transparent;
	opacity: 0;
}

.fb-admin-menu__toggle {
	position: absolute;
	top: 7px;
	right: 0px;
	width: 13px;
	height: 27px;
	line-height: 27px;
	background: rgba(0, 0, 0, 0.2);
	border-radius: 2px 0px 0px 2px;
	color: #fff;
	cursor: pointer;
	transition: background 0.4s;
	text-align: center;
	font-size: 12px;
	opacity: 0;

	.@{FbUiPrefix}-icon {
		font-size: 12px;
	}

	&:hover {
		background: rgba(0, 0, 0, 0.4);
	}

	&.fb-admin-menu__toggle--show-tail {
		right: -13px;
		opacity: 1;
	}
}

.fb-admin-menu__header {
	height: 40px;
	line-height: 40px;
	color: #fff;
	font-size: 14px;
	font-weight: 600;
	padding: 0 16px;
	overflow: hidden;
	white-space: nowrap;
	text-overflow: ellipsis;

	>.@{FbUiPrefix}-icon {
		margin-right: 8px;
	}

}

.fb-admin-menu_line {
	height: 1px;
	background-color: #E8E8E8;
}

.fb-admin-menu__content {
	overflow: auto;
	height: calc(100% - 40px);
	overflow-x: hidden;
	padding: 0 8px;

	.level1 {

		margin: 4px 0;

		.level1__title {

			/*width: 184px;*/
			height: 32px;
			border-radius: 4px;
			color: #fff;
			cursor: pointer;
			line-height: 32px;
			padding: 0 8px;
			user-select: none;
			font-size: 14px;
			transition: all 0.4s;
			text-overflow: ellipsis;
			white-space: nowrap;
			overflow: hidden;

			.@{FbUiPrefix}-icon {
				font-size: 16px;
				transform: scale(.5);
			}

		}

		.level1__body {
			margin: 0;
			padding: 0;
		}

		&.active {
			.level1__title {
				background: rgba(255, 255, 255, 0.2);
			}

			// .level1__body {
			// }
		}

		&:hover {
			.level1__title {
				padding: 0 8px 0 10px;
			}
		}

		&:hover {
			.level1__title {
				padding: 0 8px 0 10px;
			}
		}

		&:hover {
			.level1__title {
				padding: 0 8px 0 10px;
			}
		}

		&:hover {
			.level1__title {
				padding: 0 8px 0 10px;
			}
		}

	}


	.level2 {

		margin: 1px 0;

		.level2__title {
			user-select: none;
			/*width: 184px;*/
			height: 32px;
			border-radius: 4px;
			color: #fff;
			cursor: pointer;
			line-height: 32px;
			padding: 0 12px 0 38px;
			transition: all 0.4s;

			text-overflow: ellipsis;
			white-space: nowrap;
			overflow: hidden;
		}

		&.active,
		&:hover {
			.level2__title {
				background: rgba(255, 255, 255, 0.2);
			}

			// .level2__body {
			// }
		}

		&:hover {
			.level2__title {
				padding: 0 12px 0 40px;
			}
		}

		&:hover {
			.level2__title {
				padding: 0 12px 0 40px;
			}
		}

		&:hover {
			.level2__title {
				padding: 0 12px 0 40px;
			}
		}

		&:hover {
			.level2__title {
				padding: 0 12px 0 40px;
			}
		}

	}
}


// /* show-hide-menu */
// .show-hide-menu-leave-active,
// .show-hide-menu-enter-active {
// 	/*transition: all .3s;*/
// }

.show-hide-menu-enter {
	transform: scaleY(0);

}

.show-hide-menu-leave {
	transform: scaleY(1);
}


@keyframes rcMenuOpenSlideUpIn {
	0% {
		opacity: 0;
		transform-origin: 0% 0%;
		transform: scaleY(0);
	}

	100% {
		opacity: 1;
		transform-origin: 0% 0%;
		transform: scaleY(1);
	}
}

@keyframes rcMenuOpenSlideUpOut {
	0% {
		opacity: 1;
		transform-origin: 0% 0%;
		transform: scaleY(1);
	}

	100% {
		opacity: 0;
		transform-origin: 0% 0%;
		transform: scaleY(0);
	}
}
</style>
