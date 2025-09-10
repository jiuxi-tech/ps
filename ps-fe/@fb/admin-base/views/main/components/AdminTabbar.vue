<template>
	<div class="jpx-admin-tabbar" :style="getLayoutTabbarStyle">
		<fb-tabs v-model="current" closable style="width: calc(100% - 60px);"
				 ref="tabbar"
				 @on-tab-remove="handleTabRemove"
				 @on-tab-choose="handleTabChoose"
				 @on-tab-change="handleTabChange"
				 @on-tab-dblclick="handleTabDblclick"
		>
			<fb-tab v-for="(tab ) in  tabs" :label="tab.tabLabel" :name="tab.tabName"
					:key="tab.tabName"></fb-tab>
		</fb-tabs>

		<div class="jpx-admin-tabbar-tool">
			<fb-button @on-click="toolShow = !toolShow" icon="more" size="s"></fb-button>
			<fb-popup-picker v-model="toolShow" position="bottomRight" style="top: 100%;">
				<div style="width: 80px; text-align: center; padding: 8px">
					<div class="jpx-admin-tabbar-tool_item" @click="handleToolItemClick(item.func)"
						 v-for="(item, index) in toolList" :key="`toolbar_${index}`">
						{{ item.label }}
						<fb-icon :name="item.icon" size="12"></fb-icon>
					</div>
				</div>
			</fb-popup-picker>
		</div>
	</div>
</template>

<script>
	/**
	 * AdminTabbar
	 * (c) 2020 lincong1987
	 */
	import { mapState, mapMutations, mapGetters } from 'vuex'
	import { findIndex } from 'lodash-es'
	import { find } from '../../../util/componentUtils'

	export default {
		name: 'AdminTabbar',

		props: {
			routerViewKey: [String, Number],
			height: [String, Number],
			width: [String, Number],
			menuWidth: [String, Number],
			showMenu: true,

		},

		data () {
			return {
				scroll: false,
				toolShow: false,
				currentTabIndex: 0,
				refreshing: false, // 防抖标志
				toolList: [
					{label: '刷新', icon: 'refresh', func: 'refresh'},
					{label: '关闭左侧', icon: 'left-arrow', func: 'removeLeft'},
					{label: '关闭右侧', icon: 'right-arrow', func: 'removeRight'},
					{label: '关闭其他', icon: 'exchange-arrow', func: 'removeOthers'},
					{label: '关闭全部', icon: 'replace', func: 'removeAll'},
				]
			}
		},

		computed: {

			...mapGetters({
				tabs: 'tabbar/getTabs',
				flatMenus: 'menu/getFlatMenus'
			}),

			current: {
				get () {
					return this.$store.state.tabbar.current
				},
				set (newVal) {
					this.$store.commit('tabbar/focus', newVal)
				}
			},

			getLayoutTabbarStyle () {
				return {
					// height: this.height,
					left: this.showMenu ? `${this.menuWidth}px` : `0px`,
					width: this.showMenu ? `${this.width - this.menuWidth}px` : `${this.width}px`,
				}
			},

			getContainerClass () {

				var arr = ['fb-admin-tabbar__container']

				if (this.scroll) {
					arr.push('fb-admin-tabbar__container--scroll')
				}

				return arr

			},

			getTabbarStyle () {
				let arr = []
				return arr

			},

		},

		watch: {
			$route: {
				handler(route) {
					if (this.tabs.length === 0) {
						this.initTabs()
						// 注释后可以实现刷新仍在当前界面
						// return false
					}
					this.addTab(route)

					let tIndex = this.currentTabIndex = findIndex(this.tabs, (tab) => {
						return tab.fullPath === route.fullPath
					})

					if (tIndex >= 0) {
						// this.current = this.tabs[tIndex].path
						this.current = this.tabs[tIndex].tabName
					}
				},
				immediate: true,
			},
			current (val, oldVal) {
				let tIndex = findIndex(this.tabs, (tab) => {
					return tab.tabName === val
				})
				if (tIndex !== -1) {
					this.$datax.set('TABBAR_ID', this.tabs[tIndex].tabName)
				}
			},

			tabs (tabs) {
				this.tabSysComps(tabs);
			}
		},

		methods: {

			initTabs () {
				let indexPath = this.$datax.get('ADMIN_INDEX_PATH')
				let index = findIndex(this.flatMenus, (f) => {
					return f.path === indexPath
				})
				let menu = this.flatMenus[index]
				if (menu && menu.path) {
					if (menu.path === indexPath) {

						this.$store.commit('tabbar/add', Object.assign({}, menu))
					} else {
						// this.$store.commit('tabbar/add', Object.assign({}, this.$route, menu))
					}
				}
			},

			addTab (route) {
				let index = findIndex(this.flatMenus, (f) => {
					return f.path === route.path
				})
				if (index !== -1) {
					let menu = this.flatMenus[index]
					this.$store.commit('tabbar/add', Object.assign({}, route, menu))
				} else if (route.meta && route.meta.title && route.path !== app.projectConfig.router.mainPath) {
					this.$store.commit('tabbar/add', Object.assign({}, route))
				} else if ((route.query.tabLabel || route.params.tabLabel) && route.path !== app.projectConfig.router.mainPath) {
					this.$store.commit('tabbar/add', Object.assign({}, route))
				}
			},

			handleTabRemove (tab, index) {
				// const AdminMain = find(this.$parent, 'AdminMain')
				// if (AdminMain.$children) {
				// 	this.deleteKeepAliveComp(AdminMain.$children[index])
				// }
				this.$store.dispatch('tabbar/remove', tab.tabName)
			},

			handleTabChoose (tab, index) {
				if (this.tabs.length > 0) {
					if (this.$route.fullPath == tab.fullPath) {
						return
					}
					let route = this.tabs[index]
					if (route && route.fullPath) {
						this.$router.push(route.fullPath)
					}
				}
			},

			handleTabChange () {
				// this.$refs.tabbar.scrollNext()

				let index = findIndex(this.tabs, (tab) => {
					return tab.tabName === this.current
				})
				if (this.$route.fullPath !== this.tabs[index].fullPath) {
					// 监听到 tabs 数组的变化
					// 首次进来会追加首页
					this.$router.push(this.tabs[index].fullPath)
				}
			},

			refresh (tab, index) {
				try {
					// 获取当前标签页索引，如果没有传入则使用当前激活的标签页
					const targetIndex = index !== undefined ? index : this.currentTabIndex
					
					// 显示刷新提示
					// this.$message.info('正在刷新页面...', 1)
					
					// 清理当前标签页的keep-alive缓存
					this.clearCurrentTabCache(targetIndex)
					
					// 使用路由刷新机制
					const currentRoute = this.$route
					if (Object.keys(currentRoute.params).length) {
						this.$router.replace({
							name: 'refresh',
							params: currentRoute.params
						})
					} else {
						this.$router.replace({
							path: '/refresh',
							query: currentRoute.query
						})
					}
				} catch (error) {
					console.error('页面刷新失败:', error)
					this.$message.error('页面刷新失败，请重试')
				}
			},

			clearCurrentTabCache(index) {
				try {
					// 获取当前标签页的路径
					const currentTab = this.tabs[index]
					if (!currentTab) return
					
					// 查找AdminMain组件
					const AdminMain = find(this.$parent, 'AdminMain')
					if (!AdminMain) return
					
					// 获取keep-alive实例
					const keepAliveInstance = this.findKeepAliveInstance(AdminMain)
					if (keepAliveInstance && keepAliveInstance.cache) {
						// 清理匹配当前路径的缓存
						this.clearCacheByPath(keepAliveInstance, currentTab.fullPath)
					}
				} catch (error) {
					console.warn('清理缓存时出错:', error)
				}
			},

			findKeepAliveInstance(component) {
				// 递归查找keep-alive组件实例
				if (!component) return null
				
				// 检查当前组件是否是keep-alive
				if (component.$options && component.$options.name === 'keep-alive') {
					return component
				}
				
				// 在子组件中查找
				if (component.$children && component.$children.length > 0) {
					for (let child of component.$children) {
						const result = this.findKeepAliveInstance(child)
						if (result) return result
					}
				}
				
				return null
			},

			clearCacheByPath(keepAliveInstance, targetPath) {
				if (!keepAliveInstance.cache || !keepAliveInstance.keys) return
				
				const cache = keepAliveInstance.cache
				const keys = keepAliveInstance.keys
				
				// 查找并删除匹配的缓存项
				for (let key of Object.keys(cache)) {
					const cachedComponent = cache[key]
					if (cachedComponent && cachedComponent.componentOptions) {
						// 尝试从组件选项中获取路径信息
						const componentPath = this.extractPathFromComponent(cachedComponent)
						if (componentPath === targetPath) {
							// 删除缓存
							delete cache[key]
							const keyIndex = keys.indexOf(key)
							if (keyIndex > -1) {
								keys.splice(keyIndex, 1)
							}
							// 销毁组件实例
							if (cachedComponent.componentInstance && cachedComponent.componentInstance.$destroy) {
								cachedComponent.componentInstance.$destroy()
							}
							break
						}
					}
				}
			},

			extractPathFromComponent(vnode) {
				// 尝试从vnode中提取路径信息
				if (vnode.key && typeof vnode.key === 'string' && vnode.key.includes('/')) {
					return vnode.key.slice(vnode.key.indexOf('/'))
				}
				return null
			},

			handleTabDblclick (tab, index) {
				// 防抖处理，避免快速双击导致多次刷新
				if (this.refreshing) {
					return
				}
				
				this.refreshing = true
				
				// 延迟重置防抖标志
				setTimeout(() => {
					this.refreshing = false
				}, 1000)
				
				// 更新当前标签页索引
				this.currentTabIndex = index
				
				// 执行刷新
				this.refresh(tab, index)
			},

			tabSysComps (tabs) {
				// let tabs = this.tabs
				const AdminMain = find(this.$parent, 'AdminMain')
				const AdminMainChild = AdminMain.$children
				let isDel = true
				if (AdminMainChild && tabs.length !== AdminMainChild.length) {
					for (let i = 0; i < AdminMainChild.length; i++) {
						let comp = AdminMainChild[i]
						let key = comp.$vnode.key == null
							? comp.$vnode.componentOptions.Ctor.cid + (comp.$vnode.componentOptions.tag ? `::${comp.$vnode.componentOptions.tag}` : '')
							: comp.$vnode.key;

						isDel = true

						if (key) {
							let sear = key.slice(key.indexOf('/'))
							for (let j = 0; j < tabs.length; j++) {
								let tab = tabs[j]
								if (sear === tab.fullPath) {
									isDel = false
								}
							}

							if (isDel) {
								this.deleteKeepAliveComp(comp)
							}
						}
					}
				}
			},

			deleteKeepAliveComp (comp) {
				if (comp && comp.$vnode && comp.$vnode.data.keepAlive) {
					if (comp.$vnode.parent && comp.$vnode.parent.componentInstance && comp.$vnode.parent.componentInstance.cache)
					{
						if (comp.$vnode.componentOptions)
						{
							var key = comp.$vnode.key == null
								? comp.$vnode.componentOptions.Ctor.cid + (comp.$vnode.componentOptions.tag ? `::${comp.$vnode.componentOptions.tag}` : '')
								: comp.$vnode.key;
							var cache = comp.$vnode.parent.componentInstance.cache;
							var keys  = comp.$vnode.parent.componentInstance.keys;
							if (cache[key])
							{
								if (keys.length) {
									var index = keys.indexOf(key);
									if (index > -1) {
										keys.splice(index, 1);
									}
								}
								delete cache[key];
							}
						}
					}
				}
				if (comp && comp.$destroy) {
					comp.$destroy();
				}
			},


			handleToolItemClick(func) {
				if (func === 'refresh') {
					this.refresh(null, this.currentTabIndex)
					return
				}
				this.$store.commit(`tabbar/${func}`)
			}
		},

	}
</script>

<style lang="less">
@import "../../../assets/styles/layout/tabbar.less";

</style>
