
<script>
import { prefix } from '../../../../src/config'
import Emitter from '../../../mixins/emitter'
import { addClass, removeClass, hasClass } from '../../../utils/dom'

/**
 * FbMenu
 * (c) 2020 lincong1987
 */
export default {
	name: 'FbMenu',
	componentName: 'FbMenu',
	mixins: [Emitter],

	components: {
		'fb-menu-collapse-transition': {
			functional: true,
			render (createElement, context) {
				const data = {
					props: {
						mode: 'out-in'
					},
					on: {
						beforeEnter (el) {
							el.style.opacity = 0.2;
						},

						enter (el) {
							addClass(el, 'opacity-transition');
							el.style.opacity = 1;
						},

						afterEnter (el) {
							removeClass(el, 'opacity-transition');
							el.style.opacity = '';
						},

						beforeLeave (el) {
							if (!el.dataset) el.dataset = {};

							if (hasClass(el, 'menu--collapse')) {
								removeClass(el, 'menu--collapse');
								el.dataset.oldOverflow = el.style.overflow;
								el.dataset.scrollWidth = el.clientWidth;
								addClass(el, 'menu--collapse');
							} else {
								addClass(el, 'menu--collapse');
								el.dataset.oldOverflow = el.style.overflow;
								el.dataset.scrollWidth = el.clientWidth;
								removeClass(el, 'menu--collapse');
							}

							el.style.width = el.scrollWidth + 'px';
							el.style.overflow = 'hidden';
						},

						leave (el) {
							addClass(el, 'horizontal-collapse-transition');
							el.style.width = el.dataset.scrollWidth + 'px';
						}
					}
				};
				return createElement('transition', data, context.children);
			}
		}
	},
	props: {
		mode: {
			type: String,
			default: 'inline',
		},
		theme: {
			type: String,
			default: 'light',
		},
		defaultActive: {
			type: [String],
			default: '',
		},
		value: {
			type: [String, Array],
			default: '',
		},
		menuTrigger: {
			type: String,
			default: 'hover',
		},
		uniqueOpened: Boolean,
		defaultOpeneds: Array,
		inlineCollapse: Boolean,
		collapseTransition: {
			type: Boolean,
			default: true,
		},
		paddingLeftNum: {
			type: Number,
			default: 20,
		},
		backgroundColor: String,
		textColor: String,
		activeTextColor: String,
	},

	data () {
		return {
			prefix,
			activeIndex: this.value,
			openedMenus: (this.defaultOpeneds && !this.inlineCollapse) ? this.defaultOpeneds.slice(0) : [],
			items: {},
			submenus: {},
		}
	},

	provide () {
		return {
			rootMenu: this,
		}
	},

	computed: {
		hoverBackground () {
			return this.backgroundColor ? this.mixColor(this.backgroundColor, 0.2) : ''
		},
		isMenuPopup () {
			return this.mode === 'horizontal' ||
				(this.mode === 'vertical') ||
				(this.mode === 'inline' && this.inlineCollapse)
		},
	},

	watch: {
		// defaultActive (value) {
		// 	if (!this.items[value]) {
		// 		this.activeIndex = null
		// 	}
		// 	this.updateActiveIndex(value)
		// },
		// activeIndex: {
		// 	deep: true,
		// 	handler (value) {
		// 		if (!this.items[value]) {
		// 			this.activeIndex = null
		// 		}
		// 		this.updateActiveIndex(value)
		// 	},
		// },
		value(value) {
			this.updateActiveIndex(value)
		},

		defaultOpeneds (value) {
			if (!this.inlineCollapse) {
				this.openedMenus = value
			}
		},
		inlineCollapse (value) {
			if (value) this.openedMenus = []
			this.broadcast('FbSubMenu', 'toggle-collapse', value)
		},
	},

	render (h) {
		const comAttrs = {
			key: this.inlineCollapse,
			attrs: {
				role: 'menu',
			},
			class: [
				`${prefix}-menu`,
				`${prefix}-menu-root`,
				`${prefix}-menu-${this.mode}`,
				`${prefix}-menu-${this.theme}`,
				{
					[`${prefix}-menu-${this.mode}-collapsed`]: this.inlineCollapse,
				},
			],
		}

		if (this.collapseTransition) {
			return h('fb-menu-collapse-transition', [
				h('ul', comAttrs, [
					this.$slots.default,
				]),
			])
		} else {
			return h('ul', comAttrs, [
				this.$slots.default,
			])
		}
	},

	methods: {
		updateActiveIndex (val) {
			// console.log(this.items)
			let item = this.items[val]

			if (!val || typeof val === 'object') {
				item = this.items[this.activeIndex] || this.items[this.defaultActive]
			}

			if (item) {
				this.activeIndex = item.index
				this.initOpenedMenu()
			} else {
				this.activeIndex = null
			}
		},

		getMigratingConfig () {
			return {
				props: {
					'theme': 'theme is removed.',
				},
			}
		},
		getColorChannels (color) {
			color = color.replace('#', '')
			if (/^[0-9a-fA-F]{3}$/.test(color)) {
				color = color.split('')
				for (let i = 2; i >= 0; i--) {
					color.splice(i, 0, color[i])
				}
				color = color.join('')
			}
			if (/^[0-9a-fA-F]{6}$/.test(color)) {
				return {
					red: parseInt(color.slice(0, 2), 16),
					green: parseInt(color.slice(2, 4), 16),
					blue: parseInt(color.slice(4, 6), 16),
				}
			} else {
				return {
					red: 255,
					green: 255,
					blue: 255,
				}
			}
		},
		mixColor (color, percent) {
			let {
				red,
				green,
				blue,
			} = this.getColorChannels(color)
			if (percent > 0) { // shade given color
				red *= 1 - percent
				green *= 1 - percent
				blue *= 1 - percent
			} else { // tint given color
				red += (255 - red) * percent
				green += (255 - green) * percent
				blue += (255 - blue) * percent
			}
			return `rgb(${Math.round(red)}, ${Math.round(green)}, ${Math.round(blue)})`
		},
		addItem (item) {
			this.$set(this.items, item.index, item)
			// this.items[item.index] = item
		},
		removeItem (item) {
			// delete this.items[item.index]
		},
		addSubmenu (item) {
			this.$set(this.submenus, item.index, item)
		},
		removeSubmenu (item) {
			delete this.submenus[item.index]
		},
		openMenu (index, indexPath) {
			let openedMenus = this.openedMenus
			if (openedMenus.indexOf(index) !== -1) return
			// 将不在该菜单路径下的其余菜单收起
			// collapse all menu that are not under current menu item
			if (this.uniqueOpened) {
				this.openedMenus = openedMenus.filter(index => {
					return indexPath.indexOf(index) !== -1
				})
			}
			this.openedMenus.push(index)
		},
		closeMenu (index) {
			const i = this.openedMenus.indexOf(index)
			if (i !== -1) {
				this.openedMenus.splice(i, 1)
			}
		},
		handleSubmenuClick (submenu) {
			const {
				index,
				indexPath,
			} = submenu
			let isOpened = this.openedMenus.indexOf(index) !== -1

			if (isOpened) {
				this.closeMenu(index)
				this.$emit('on-close', index, indexPath)
			} else {
				this.openMenu(index, indexPath)
				this.$emit('on-open', index, indexPath)
			}
		},
		handleItemClick (item) {
			const {
				index,
				indexPath,
			} = item
			const oldActiveIndex = this.activeIndex
			const hasIndex = item.index !== null

			if (hasIndex) {
				this.activeIndex = item.index
				this.$emit('input', this.activeIndex)
				this.$emit('on-select', index, indexPath, item)
			} else {
				console.warn('Not menu-item props index')
			}

			if (this.mode === 'horizontal' || this.inlineCollapse) {
				this.openedMenus = []
			}

			if (this.router && hasIndex) {
				this.routeToItem(item, (error) => {
					this.activeIndex = oldActiveIndex
					if (error) {
						// vue-router 3.1.0+ push/replace cause NavigationDuplicated error
						// https://github.com/ElemeFE/element/issues/17044
						if (error.name === 'NavigationDuplicated') return
						console.error(error)
					}
				})
			}
		},
		// 初始化展开菜单
		// initialize opened menu
		initOpenedMenu () {
			const index = this.activeIndex
			const activeItem = this.items[index]
			if (!activeItem || this.mode === 'horizontal' || this.inlineCollapse) return

			let indexPath = activeItem.indexPath

			// 展开该菜单项的路径上所有子菜单
			// expand all submenus of the menu item
			indexPath.forEach(index => {
				let submenu = this.submenus[index]
				submenu && this.openMenu(index, submenu.indexPath)
			})
		},
		routeToItem (item, onError) {
			let route = item.route || item.index
			try {
				this.$router.push(route, () => {
				}, onError)
			} catch (e) {
				console.error(e)
			}
		},
		open (index) {
			const {indexPath} = this.submenus[index.toString()]
			indexPath.forEach(i => this.openMenu(i, indexPath))
		},
		close (index) {
			this.closeMenu(index)
		},
	},

	mounted () {
		this.initOpenedMenu()
		this.$on('item-click', this.handleItemClick)
		this.$on('submenu-click', this.handleSubmenuClick)
		this.$watch('items', this.updateActiveIndex)
	},
	beforeDestroy () {
		this.$off('item-click')
		this.$off('submenu-click')
		// this.unwatch()
	},
	destroyed () {
	},
}
</script>

<style scoped>

</style>
