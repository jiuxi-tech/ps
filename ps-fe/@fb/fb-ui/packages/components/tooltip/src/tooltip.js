// vue-popper 根据 https://popper.js.org/ 处理而来
// 整体改造与 el-tooltip 一致
import Popper from '../../../utils/vue-popper'
import { debounce } from 'lodash-es'
import { addClass, removeClass, on, off } from '../../../utils/dom'
import { generateId } from '../../../utils/utils'
import { prefix } from '../../../../src/config'
import Vue from 'vue'

export default {
	name: 'FbTooltip',

	mixins: [Popper],

	props: {

		openDelay: {
			type: Number,
			default: 0,
		},
		disabled: Boolean,
		manual: Boolean,
		effect: {
			type: String,
			default: 'dark',
		},
		arrowOffset: {
			type: Number,
			default: 0,
		},
		popperClass: String,
		content: String,
		visibleArrow: {
			default: true,
		},
		transition: {
			type: String,
			default: 'fade',
		},
		popperOptions: {
			default () {
				return {
					boundariesPadding: 10,
					gpuAcceleration: false,
				}
			},
		},
		enterable: {
			type: Boolean,
			default: true,
		},
		hideAfter: {
			type: Number,
			default: 0,
		},
		tabindex: {
			type: Number,
			default: 0,
		},
		trigger: {
			type: String,
			default: 'hover',
		},
		tipStyle: {
			type: [String, Object, Array],
			default () {
				return []
			},
		},
		focusClass: {
			type: String,
			default: 'focusing',
		},
	},

	data () {
		return {
			tooltipId: `${prefix}-tooltip-${generateId()}`,
			timeoutPending: null,
			focusing: false,
			expectedState: false,
		}
	},
	beforeCreate () {
		if (this.$isServer) return

		this.popperVM = new Vue({
			data: {node: ''},
			render (h) {
				return this.node
			},
		}).$mount()

		this.debounceClose = debounce(() => this.handleClosePopper(), 200)
	},

	render (h) {
		if (this.popperVM) {
			// 	this.popperVM.node = (
			// 		<transition
			// 	name={ this.transition }
			// 	onAfterLeave={ this.doDestroy }>
			// 		<div
			// 	onMouseleave={ () => { this.setExpectedState(false);
			// this.debounceClose(); } } onMouseenter= { () => {
			// this.setExpectedState(true); } } ref="popper" role="tooltip"
			// id={this.tooltipId} aria-hidden={ (this.disabled ||
			// !this.showPopper) ? 'true' : 'false' } v-show={!this.disabled &&
			// this.showPopper} class={ [`${prefix}-tooltip__popper`, 'is-' +
			// this.effect, this.popperClass] }> { this.$slots.content ||
			// this.content } </div> </transition>);

			// createElement 的调用位置，会当作当前的当前组件的引用位置
			// 详情可见 ref
			let events = {}

			if (this.trigger === 'click') {
				events.click = () => {
					//this.setExpectedState(!this.disabled && this.showPopper)
				}
				events.mouseenter = () => {
				}
				events.mouseleave = () => {
				}
			}

			if (this.trigger === 'hover') {

				events.mouseenter = () => {
					this.setExpectedState(true)
				}

				events.mouseleave = () => {
					this.setExpectedState(false)
					this.debounceClose()
				}
			}

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
						on: events,
						ref: 'popper',
						attrs: {
							role: 'tooltip',
							// id: this.tooltipId,
							'aria-hidden': this.disabled || !this.showPopper
								? 'true'
								: 'false',
						},
						directives: [
							{
								name: 'show',
								value: !this.disabled && this.showPopper,
							},
						],
						class: [
							`${prefix}-tooltip__popper`,
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
		data.staticClass = this.addTooltipClass(data.staticClass)

		return firstElement
	},

	mounted () {

		this.referenceElm = this.$el
		if (this.$el.nodeType === 1) {
			this.$el.setAttribute('aria-describedby', this.tooltipId)
			this.$el.setAttribute('tabindex', this.tabindex)

			if (this.trigger === 'hover') {
				on(this.referenceElm, 'mouseenter', this.show)
				on(this.referenceElm, 'mouseleave', this.hide)

				on(this.referenceElm, 'focus', () => {
					if (!this.$slots.default || !this.$slots.default.length) {
						this.handleFocus()
						return
					}
					const instance = this.$slots.default[0].componentInstance
					if (instance && instance.focus) {
						instance.focus()
					} else {
						this.handleFocus()
					}
				})
				on(this.referenceElm, 'blur', this.handleBlur)

				on(this.referenceElm, 'click', this.removeFocusing)
			}

			if (this.trigger === 'click') {
				on(this.referenceElm, 'click', () => {
					this.setExpectedState(!this.expectedState)
					this.toggle()
				})
			}

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
	watch: {
		focusing (val) {
			if (val) {
				addClass(this.referenceElm, this.focusClass)
			} else {
				removeClass(this.referenceElm, this.focusClass)
			}
		},
		expectedState (val) {
			// console.log('expectedState', val)
		},
	},
	methods: {
		show () {
			this.setExpectedState(true)
			this.handleShowPopper()
		},

		hide () {
			this.setExpectedState(false)
			this.debounceClose()
		},
		toggle () {
			if (this.getExpectedState() === false) {
				this.hide()
				this.handleClosePopper()
			} else {
				this.show()
			}
		},
		handleFocus () {
			this.focusing = true
			this.show()
		},
		handleBlur () {
			this.focusing = false
			this.hide()
		},
		removeFocusing () {
			this.focusing = false
		},

		addTooltipClass (prev) {
			if (!prev) {
				return `${prefix}-tooltip`
			} else {
				return `${prefix}-tooltip ` +
					prev.replace(`${prefix}-tooltip`, '')
			}
		},

		handleShowPopper () {
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

		handleClosePopper () {
			if (this.enterable && this.expectedState || this.manual) return
			clearTimeout(this.timeout)

			if (this.timeoutPending) {
				clearTimeout(this.timeoutPending)
			}
			this.showPopper = false

			if (this.disabled) {
				this.doDestroy()
			}
		},

		setExpectedState (expectedState) {
			if (expectedState === false) {
				clearTimeout(this.timeoutPending)
			}
			this.expectedState = expectedState
		},

		getExpectedState () {
			return this.expectedState
		},

		getFirstElement () {
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

	beforeDestroy () {
		this.popperVM && this.popperVM.$destroy()
	},

	destroyed () {
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
