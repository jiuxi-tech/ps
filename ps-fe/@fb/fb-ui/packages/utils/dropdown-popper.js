// vue-popper 根据 https://popper.js.org/ 处理而来
// 整体改造与 el-tooltip 一致
import Popper from './vue-popper'
import {debounce} from 'lodash-es'
import {addClass, removeClass, on, off} from './dom'
import {generateId} from './utils'
import {prefix} from '../../src/config'
import Vue from 'vue'

/**
 * @namespace DropdownPopper
 * @desc 下拉弹出层组件
 * @description 基于 Popper 的下拉弹出层组件
 */

export default {
	name: 'FbDropDown',

	mixins: [Popper],

	props: {

		/**
		 * @member {Number} openDelay
		 * @desc 打开延迟时间（毫秒）
		 * @default 0
		 */
		openDelay: {
			type: Number,
			default: 0,
		},
		
		/**
		 * @member {Boolean} manual
		 * @desc 是否手动控制显示
		 * @default false
		 */
		manual: Boolean,
		
		/**
		 * @member {String} effect
		 * @desc 弹出层效果主题
		 * @default 'dark'
		 */
		effect: {
			type: String,
			default: 'dark',
		},
		
		/**
		 * @member {Number} arrowOffset
		 * @desc 箭头偏移量
		 * @default 0
		 */
		arrowOffset: {
			type: Number,
			default: 0,
		},
		
		/**
		 * @member {String} popperClass
		 * @desc 弹出层自定义类名
		 */
		popperClass: String,
		
		/**
		 * @member {String} content
		 * @desc 弹出层内容
		 */
		content: String,
		
		/**
		 * @member {Boolean} visibleArrow
		 * @desc 是否显示箭头
		 * @default false
		 */
		visibleArrow: {
			default: false,
		},
		
		/**
		 * @member {String} transition
		 * @desc 过渡动画名称
		 * @default 'fade'
		 */
		transition: {
			type: String,
			default: 'fade',
		},
		
		/**
		 * @member {Object} popperOptions
		 * @desc Popper.js 配置选项
		 * @default { boundariesPadding: 10, gpuAcceleration: false }
		 */
		popperOptions: {
			default() {
				return {
					boundariesPadding: 10,
					gpuAcceleration: false,
				}
			},
		},
		
		/**
		 * @member {Boolean} enterable
		 * @desc 鼠标是否可进入弹出层
		 * @default true
		 */
		enterable: {
			type: Boolean,
			default: true,
		},
		
		/**
		 * @member {Number} hideAfter
		 * @desc 自动隐藏延迟时间（毫秒）
		 * @default 0
		 */
		hideAfter: {
			type: Number,
			default: 0,
		},
		
		/**
		 * @member {Number} tabindex
		 * @desc 元素 tabindex 属性
		 * @default 0
		 */
		tabindex: {
			type: Number,
			default: 0,
		},
		
		/**
		 * @member {String|Object|Array} tipStyle
		 * @desc 弹出层样式
		 * @default [{ border: 'none', padding: 0, margin: 0, background: 'transparent' }]
		 */
		tipStyle: {
			type: [String, Object, Array],
			default() {
				return [{
					border: 'none',
					padding: 0,
					margin: 0,
					background: 'transparent',
				}]
			},
		},
		
		/**
		 * @member {String} focusClass
		 * @desc 聚焦时的类名
		 * @default ''
		 */
		focusClass: {
			type: String,
			default: '',
		},
	},

	data() {
		return {
			/**
			 * @member {String} dropdownId
			 * @desc 下拉菜单 ID
			 */
			dropdownId: `${prefix}-dropdown-${generateId()}`,
			
			/**
			 * @member {Number} timeoutPending
			 * @desc 延迟定时器 ID
			 */
			timeoutPending: null,
			
			/**
			 * @member {Boolean} focusing
			 * @desc 是否聚焦状态
			 */
			focusing: false,
			
			/**
			 * @member {Boolean} expectedState
			 * @desc 期望的显示状态
			 */
			expectedState: true,
		}
	},
	
	/**
	 * @desc 组件创建前钩子
	 */
	beforeCreate() {
		if (this.$isServer) return

		this.popperVM = new Vue({
			data: {node: ''},
			render(h) {
				return this.node
			},
		}).$mount()

		this.debounceClose = debounce(() => this.handleClosePopper(), 200)
	},

	/**
	 * @desc 渲染函数
	 */
	render(h) {
		if (this.popperVM) {
			this.popperVM.node = h(
				'transition', {
					attrs: {
						name: this.transition,
					},
					on: {
						afterLeave: this.doDestroy,
					},
				}, [
					h('div', {
						ref: 'popper',
						attrs: {
							role: 'dropdown',
							// id: this.dropdownId,
							'aria-hidden': !this.showPopper ? 'true' : 'false',
						},
						directives: [
							{
								name: 'show',
								value: this.showPopper,
							},
						],
						class: [
							`${prefix}-dropdown__popper`,
							'is-' + this.effect,
							this.popperClass,
						],
						style: this.tipStyle,
					}, [this.$slots.content || this.content]),
				],
			)
		}

		const firstElement = this.getFirstElement()
		if (!firstElement) return null

		const data = firstElement.data = firstElement.data || {}
		data.staticClass = this.addDropdownClass(data.staticClass)

		return firstElement
	},

	/**
	 * @desc 组件挂载后钩子
	 */
	mounted() {

		this.referenceElm = this.$el
		if (this.$el.nodeType === 1) {
			this.$el.setAttribute('aria-describedby', this.dropdownId)
			this.$el.setAttribute('tabindex', this.tabindex)
		}
		// fix issue https://github.com/ElemeFE/element/issues/14424
		if (this.value && this.popperVM) {
			this.popperVM.$nextTick(() => {
				if (this.value) {
					this.updatePopper()
				}
			})
		}
	},
	
	/**
	 * @desc 组件监听器
	 */
	watch: {
		focusing(val) {
			if (val) {
				addClass(this.referenceElm, this.focusClass)
			} else {
				removeClass(this.referenceElm, this.focusClass)
			}
		},
		expectedState(val) {
			console.log('expectedState', val)
		},
	},
	
	methods: {
		/**
		 * @desc 显示下拉菜单
		 * @example
		 * // 显示下拉菜单
		 * dropdown.show();
		 */
		show() {
			this.setExpectedState(true)
			this.handleShowPopper()
		},

		/**
		 * @desc 隐藏下拉菜单
		 * @example
		 * // 隐藏下拉菜单
		 * dropdown.hide();
		 */
		hide() {
			this.setExpectedState(false)
			this.debounceClose()
		},
		
		/**
		 * @desc 切换下拉菜单显示状态
		 * @example
		 * // 切换下拉菜单显示状态
		 * dropdown.toggle();
		 */
		toggle() {
			if (this.getExpectedState() === false) {
				this.hide()
				this.handleClosePopper()
			} else {
				this.show()
			}
		},
		
		/**
		 * @desc 处理聚焦事件
		 * @example
		 * // 处理聚焦事件
		 * dropdown.handleFocus();
		 */
		handleFocus() {
			this.focusing = true
			this.show()
		},
		
		/**
		 * @desc 处理失焦事件
		 * @example
		 * // 处理失焦事件
		 * dropdown.handleBlur();
		 */
		handleBlur() {
			this.focusing = false
			this.hide()
		},
		
		/**
		 * @desc 移除聚焦状态
		 * @example
		 * // 移除聚焦状态
		 * dropdown.removeFocusing();
		 */
		removeFocusing() {
			this.focusing = false
		},

		/**
		 * @desc 添加下拉菜单类名
		 * @param {String} prev - 原始类名
		 * @returns {String} 返回处理后的类名
		 * @example
		 * // 添加下拉菜单类名
		 * const className = dropdown.addDropdownClass('original-class');
		 */
		addDropdownClass(prev) {
			if (!prev) {
				return `${prefix}-dropdown`
			} else {
				return `${prefix}-dropdown ` +
					prev.replace(`${prefix}-dropdown`, '')
			}
		},

		/**
		 * @desc 处理显示下拉菜单
		 * @example
		 * // 处理显示下拉菜单
		 * dropdown.handleShowPopper();
		 */
		handleShowPopper() {
			if (!this.expectedState || this.manual) return
			clearTimeout(this.timeout)
			this.timeout = setTimeout(() => {
				this.showPopper = true
			}, this.openDelay)

			if (this.hideAfter > 0) {
				this.timeoutPending = setTimeout(() => {
					this.showPopper = false
				}, this.hideAfter)
			}
		},

		/**
		 * @desc 处理关闭下拉菜单
		 * @example
		 * // 处理关闭下拉菜单
		 * dropdown.handleClosePopper();
		 */
		handleClosePopper() {
			if (this.enterable && this.expectedState || this.manual) return
			clearTimeout(this.timeout)

			if (this.timeoutPending) {
				clearTimeout(this.timeoutPending)
			}
			this.showPopper = false
		},

		/**
		 * @desc 设置期望的显示状态
		 * @param {Boolean} expectedState - 期望的显示状态
		 * @example
		 * // 设置期望的显示状态为 true
		 * dropdown.setExpectedState(true);
		 */
		setExpectedState(expectedState) {
			if (expectedState === false) {
				clearTimeout(this.timeoutPending)
			}
			this.expectedState = expectedState
		},

		/**
		 * @desc 获取期望的显示状态
		 * @returns {Boolean} 返回期望的显示状态
		 * @example
		 * // 获取期望的显示状态
		 * const state = dropdown.getExpectedState();
		 */
		getExpectedState() {
			return this.expectedState
		},

		/**
		 * @desc 获取第一个元素
		 * @returns {VNode|null} 返回第一个 VNode 元素或 null
		 * @example
		 * // 获取第一个元素
		 * const element = dropdown.getFirstElement();
		 */
		getFirstElement() {
			const slots = this.$slots.default
			if (!Array.isArray(slots)) return null
			let element = null
			for (let index = 0; index < slots.length; index++) {
				if (slots[index] && slots[index].tag) {
					element = slots[index]
				}

			}
			return element
		},
	},

	/**
	 * @desc 组件销毁前钩子
	 */
	beforeDestroy() {
		this.popperVM && this.popperVM.$destroy()
	},

	/**
	 * @desc 组件销毁后钩子
	 */
	destroyed() {
		const reference = this.referenceElm
		if (reference.nodeType === 1) {
			off(reference, 'mouseenter', this.show)
			off(reference, 'mouseleave', this.hide)
			off(reference, 'focus', this.handleFocus)
			off(reference, 'blur', this.handleBlur)
			off(reference, 'click', this.removeFocusing)
		}
	},
}