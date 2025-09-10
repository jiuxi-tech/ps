<script>
import FbCollapseTransition from '../../collapse-transition'
import menuMixin from './menu-mixin'
import Emitter from '../../../mixins/emitter'
import Popper from '../../../utils/vue-popper'
import { prefix } from '../../../../src/config'

const poperMixins = {
	props: {
		transformOrigin: {
			type: [Boolean, String],
			default: false,
		},
		offset: Popper.props.offset,
		boundariesPadding: Popper.props.boundariesPadding,
		popperOptions: Popper.props.popperOptions,
	},
	data: Popper.data,
	methods: Popper.methods,
	beforeDestroy: Popper.beforeDestroy,
	deactivated: Popper.deactivated,
}

export default {
	name: 'FbSubMenu',

	componentName: 'FbSubMenu',

	mixins: [menuMixin, Emitter, poperMixins],

	components: {FbCollapseTransition},

	props: {
		index: {
			type: String,
			required: true,
		},
		showTimeout: {
			type: Number,
			default: 300,
		},
		hideTimeout: {
			type: Number,
			default: 300,
		},
		popperClass: String,
		disabled: Boolean,
		popperAppendToBody: {
			type: Boolean,
			default: undefined,
		},
	},

	data () {
		return {
			prefix,
			popperJS: null,
			timeout: null,
			items: {},
			submenus: {},
			mouseInChild: false,
		}
	},
	watch: {
		opened: {
			handler (val) {
				if (this.isMenuPopup) {
					this.$nextTick(_ => {
						this.updatePopper()
					})
				}
			},
			immediate: true,
		},
	},
	computed: {
		// popper option
		appendToBody () {
			return this.popperAppendToBody === undefined
				? this.isFirstLevel
				: this.popperAppendToBody
		},
		menuTransitionName () {
			return this.rootMenu.inlineCollapse ? `zoom-in-left` : `zoom-in-top`
		},
		opened () {
			return this.rootMenu.openedMenus.indexOf(this.index) > -1
		},
		active () {
			let isActive = false
			const submenus = this.submenus
			const items = this.items

			Object.keys(items).forEach(index => {
				if (items[index].active) {
					isActive = true
				}
			})

			Object.keys(submenus).forEach(index => {
				if (submenus[index].active) {
					isActive = true
				}
			})

			return isActive
		},
		hoverBackground () {
			return this.rootMenu.hoverBackground
		},
		backgroundColor () {
			return this.rootMenu.backgroundColor || ''
		},
		activeTextColor () {
			return this.rootMenu.activeTextColor || ''
		},
		textColor () {
			return this.rootMenu.textColor || ''
		},
		mode () {
			return this.rootMenu.mode
		},
		isMenuPopup () {
			return this.rootMenu.isMenuPopup
		},
		titleStyle () {
			if (this.mode !== 'horizontal') {
				return {
					color: this.textColor,
				}
			}
			return {
				borderBottomColor: this.active
					? (this.rootMenu.activeTextColor ? this.activeTextColor : '')
					: 'transparent',
				color: this.active
					? this.activeTextColor
					: this.textColor,
			}
		},
		subMenuStyle () {
			return {
				borderBottomColor: this.active
					? (this.rootMenu.activeTextColor ? this.activeTextColor : '')
					: 'transparent',
			}
		},
		isFirstLevel () {
			let isFirstLevel = true
			let parent = this.$parent
			while (parent && parent !== this.rootMenu) {
				if (['FbSubMenu', 'FbMenuItemGroup'].indexOf(parent.$options.componentName) > -1) {
					isFirstLevel = false
					break
				} else {
					parent = parent.$parent
				}
			}
			return isFirstLevel
		},
	},
	methods: {
		handleCollapseToggle (value) {

			if (value) {
				this.initPopper()
			} else {
				this.doDestroy()
			}
		},
		addItem (item) {
			this.$set(this.items, item.index, item)
		},
		removeItem (item) {
			delete this.items[item.index]
		},
		addSubmenu (item) {
			this.$set(this.submenus, item.index, item)
		},
		removeSubmenu (item) {
			delete this.submenus[item.index]
		},
		handleClick () {
			const {
				rootMenu,
				disabled,
			} = this
			if (
				(rootMenu.menuTrigger === 'hover' && rootMenu.mode === 'horizontal') ||
				(rootMenu.menuTrigger === 'hover' && rootMenu.mode === 'vertical') ||
				(rootMenu.inlineCollapse && rootMenu.mode === 'inline') ||
				disabled
			) {
				return
			}
			this.dispatch('FbMenu', 'submenu-click', this)
		},
		handleMouseenter (event, showTimeout = this.showTimeout) {

			if (!('ActiveXObject' in window) && event.type === 'focus' && !event.relatedTarget) {
				return
			}
			const {
				rootMenu,
				disabled,
			} = this
			if (
				(rootMenu.menuTrigger === 'click' && rootMenu.mode === 'horizontal') ||
				(rootMenu.menuTrigger === 'click' && rootMenu.mode === 'vertical') ||
				(!rootMenu.inlineCollapse && rootMenu.mode === 'inline') ||
				disabled
			) {
				return
			}
			this.dispatch('FbSubMenu', 'mouse-enter-child')
			clearTimeout(this.timeout)
			this.timeout = setTimeout(() => {
				this.rootMenu.openMenu(this.index, this.indexPath)
			}, showTimeout)

			if (this.appendToBody) {
				this.$parent.$el.dispatchEvent(new MouseEvent('mouseenter'))
			}
		},
		handleMouseleave (deepDispatch = false) {
			const {rootMenu} = this
			if (
				(rootMenu.menuTrigger === 'click' && rootMenu.mode === 'horizontal') ||
				(rootMenu.menuTrigger === 'click' && rootMenu.mode === 'vertical') ||
				(!rootMenu.inlineCollapse && rootMenu.mode === 'inline')
			) {
				return
			}
			this.dispatch('FbSubMenu', 'mouse-leave-child')
			clearTimeout(this.timeout)
			this.timeout = setTimeout(() => {
				!this.mouseInChild && this.rootMenu.closeMenu(this.index)
			}, this.hideTimeout)

			if (this.appendToBody && deepDispatch) {
				if (this.$parent.$options.name === 'FbSubmenu') {
					this.$parent.handleMouseleave(true)
				}
			}
		},
		handleTitleMouseenter () {
			if (this.mode === 'horizontal' && !this.rootMenu.backgroundColor) return
			const title = this.$refs['submenu-title']
			title && (title.style.backgroundColor = this.rootMenu.hoverBackground)
		},
		handleTitleMouseleave () {
			if (this.mode === 'horizontal' && !this.rootMenu.backgroundColor) return
			const title = this.$refs['submenu-title']
			title && (title.style.backgroundColor = this.rootMenu.backgroundColor || '')
		},
		updatePlacement () {
			this.currentPlacement = this.mode === 'horizontal' && this.isFirstLevel
				? 'bottom-start'
				: 'right-start'
		},
		initPopper () {
			this.referenceElm = this.$el
			this.popperElm = this.$refs.menu
			this.updatePlacement()
		},
	},
	created () {
		this.$on('toggle-collapse', this.handleCollapseToggle)
		this.$on('mouse-enter-child', () => {
			this.mouseInChild = true
			clearTimeout(this.timeout)
		})
		this.$on('mouse-leave-child', () => {
			this.mouseInChild = false
			clearTimeout(this.timeout)
		})
	},
	mounted () {
		this.parentMenu.addSubmenu(this)
		this.rootMenu.addSubmenu(this)
		this.initPopper()
	},
	beforeDestroy () {
		this.parentMenu.removeSubmenu(this)
		this.rootMenu.removeSubmenu(this)

	},
	render (h) {
		const {
			active,
			opened,
			paddingStyle,
			titleStyle,
			subMenuStyle,
			backgroundColor,
			rootMenu,
			currentPlacement,
			menuTransitionName,
			mode,
			disabled,
			popperClass,
			$slots,
			isFirstLevel,
		} = this

		const popupMenu = h('transition', {name: menuTransitionName}, [
			h('div', {
				key: 'menu',
				ref: 'menu',
				class: [`${prefix}-menu--${mode}`, popperClass],
				directives: [
					{
						name: 'show',
						value: opened,
					},
				],
			}, [
				h('div', {
					class: [
						`${prefix}-menu-${this.rootMenu.theme}`,
						`${prefix}-menu-submenu-placement-${currentPlacement}`,
					],
					on: {
						mouseenter: ($event) => this.handleMouseenter($event, 50),
						mouseleave: () => this.handleMouseleave(true),
						focus: ($event) => this.handleMouseenter($event, 50),
					},
				}, [
					h('ul', {
						attrs: {role: 'menu'},
						class: [`${prefix}-menu ${prefix}-menu-popup ${prefix}-menu-vertical ${prefix}-menu-sub`],
						style: {backgroundColor: rootMenu.backgroundColor || ''},
					}, [this.$slots.default]),
				]),
			]),
		])

		const inlineMenu =
			// h('transition', {name: 'slide-to-down'}, [
			h('ul', {
				attrs: {role: 'menu'},
				class: [`${prefix}-menu ${prefix}-menu-${mode} ${prefix}-menu-sub`],
				style: {
					backgroundColor: rootMenu.backgroundColor || '',
				},
				directives: [
					{
						name: 'show',
						value: opened,
					},
				],
			}, [this.$slots.default])
		// ])

		const submenuTitleIcon = !this.activeTextColor ? `${prefix}-menu-submenu-arrow` : (
			rootMenu.mode === 'horizontal' && isFirstLevel ||
			rootMenu.mode === 'inline' && !rootMenu.inlineCollapse
		)
			? `${prefix}-icon ${prefix}-subtitle-icon ${prefix}-icon-down`
			: `${prefix}-icon  ${prefix}-subtitle-icon ${prefix}-icon-right`

		const submenuSuffix = mode === 'inline' ? 'inline' :
			currentPlacement === 'bottom-start' ? 'horizontal' : 'vertical'

		return h('li', {
			attrs: {
				role: 'menuitem',
				['aria-haspopup']: 'true',
				['aria-expanded']: opened,
			},
			class: [
				`${prefix}-menu-submenu`,
				`${prefix}-menu-submenu-${submenuSuffix}`,
				{

					[`${prefix}-menu-submenu-open`]: opened,
					[`${prefix}-menu-submenu-selected`]: active,
					[`${prefix}-menu-submenu-disabled`]: disabled,
				},
			],
			style: [subMenuStyle, {backgroundColor}],
			on: {
				mouseenter: this.handleMouseenter,
				mouseleave: ($event) => this.handleMouseleave($event, false),
				focus: this.handleMouseenter,
			},
		}, [
			h('div', {
				ref: 'submenu-title',
				class: [`${prefix}-menu-submenu-title`],
				on: {
					click: this.handleClick,
					mouseenter: this.handleTitleMouseenter,
					mouseleave: this.handleTitleMouseleave,
				},
				style: [paddingStyle, titleStyle, {backgroundColor}],
			}, [
				this.$slots.title,
				h('i', {
					class: [submenuTitleIcon],
					style: [titleStyle],
				}),
			]),
			this.isMenuPopup ? popupMenu : inlineMenu,
		])
	},
}

</script>
