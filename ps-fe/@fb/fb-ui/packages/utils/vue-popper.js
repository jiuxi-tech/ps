import Vue from 'vue';
import {
	PopupManager
} from './popup';

const PopperJS = Vue.prototype.$isServer ? function() {} : require('./popper');
const stop = e => e.stopPropagation();

/**
 * @namespace VuePopper
 * @desc Vue Popper 弹出层工具函数集合
 * @description 基于 Popper.js 的 Vue 弹出层组件工具
 */

/**
 * @desc Vue Popper 弹出层组件配置
 * @property {Boolean|String} transformOrigin - 是否启用变换原点，默认为 true
 * @property {String} placement - 弹出层位置，默认为 'bottom'
 * @property {Number} boundariesPadding - 边界内边距，默认为 5
 * @property {Element} reference - 参考元素
 * @property {Element} popper - 弹出层元素
 * @property {Number} offset - 弹出层偏移量，默认为 0
 * @property {Boolean} value - 弹出层可见性，默认为 false
 * @property {Boolean} visibleArrow - 是否显示箭头，默认为 false
 * @property {Number} arrowOffset - 箭头偏移量，默认为 35
 * @property {Boolean} appendToBody - 是否添加到 body，默认为 true
 * @property {Object} popperOptions - Popper.js 配置选项
 * @example
 * // 在 Vue 组件中使用
 * export default {
 *   mixins: [VuePopper],
 *   props: {
 *     placement: {
 *       type: String,
 *       default: 'top'
 *     }
 *   }
 * }
 */
export default {
	props: {
		/**
		 * @member {Boolean|String} transformOrigin
		 * @desc 是否启用变换原点
		 * @default true
		 */
		transformOrigin: {
			type: [Boolean, String],
			default: true
		},
		
		/**
		 * @member {String} placement
		 * @desc 弹出层位置
		 * @default 'bottom'
		 */
		placement: {
			type: String,
			default: 'bottom'
		},
		
		/**
		 * @member {Number} boundariesPadding
		 * @desc 边界内边距
		 * @default 5
		 */
		boundariesPadding: {
			type: Number,
			default: 5
		},
		
		/**
		 * @member {Element} reference
		 * @desc 参考元素
		 */
		reference: {},
		
		/**
		 * @member {Element} popper
		 * @desc 弹出层元素
		 */
		popper: {},
		
		/**
		 * @member {Number} offset
		 * @desc 弹出层偏移量
		 * @default 0
		 */
		offset: {
			default: 0
		},
		
		/**
		 * @member {Boolean} value
		 * @desc 弹出层可见性
		 * @default false
		 */
		value: Boolean,
		
		/**
		 * @member {Boolean} visibleArrow
		 * @desc 是否显示箭头
		 * @default false
		 */
		visibleArrow: Boolean,
		
		/**
		 * @member {Number} arrowOffset
		 * @desc 箭头偏移量
		 * @default 35
		 */
		arrowOffset: {
			type: Number,
			default: 35
		},
		
		/**
		 * @member {Boolean} appendToBody
		 * @desc 是否添加到 body
		 * @default true
		 */
		appendToBody: {
			type: Boolean,
			default: true
		},
		
		/**
		 * @member {Object} popperOptions
		 * @desc Popper.js 配置选项
		 * @default { gpuAcceleration: false }
		 */
		popperOptions: {
			type: Object,
			default() {
				return {
					gpuAcceleration: false
				};
			}
		}
	},

	data() {
		return {
			/**
			 * @member {Boolean} showPopper
			 * @desc 弹出层显示状态
			 */
			showPopper: false,
			
			/**
			 * @member {String} currentPlacement
			 * @desc 当前弹出层位置
			 */
			currentPlacement: ''
		};
	},

	watch: {
		value: {
			immediate: true,
			handler(val) {
				this.showPopper = val;
				this.$emit('input', val);
			}
		},

		showPopper(val) {
			if (this.disabled) return;
			val ? this.updatePopper() : this.destroyPopper();
			this.$emit('input', val);
		}
	},

	methods: {
		/**
		 * @desc 创建弹出层
		 * @example
		 * // 创建弹出层
		 * this.createPopper();
		 */
		createPopper() {
			if (this.$isServer) return;
			this.currentPlacement = this.currentPlacement || this.placement;
			if (!/^(top|bottom|left|right)(-start|-end)?$/g.test(this.currentPlacement)) {
				return;
			}

			const options = this.popperOptions;
			const popper = this.popperElm = this.popperElm || this.popper || this.$refs.popper || this.popperVM.node.elm;
			let reference = this.referenceElm = this.referenceElm || this.reference || this.$refs.reference;

			if (!reference &&
				this.$slots.reference &&
				this.$slots.reference[0]) {
				reference = this.referenceElm = this.$slots.reference[0].elm;
			}

			if (!popper || !reference) return;
			if (this.visibleArrow) this.appendArrow(popper);
			if (this.appendToBody) document.body.appendChild(this.popperElm);
			if (this.popperJS && this.popperJS.destroy) {
				this.popperJS.destroy();
			}

			options.placement = this.currentPlacement;
			options.offset = this.offset;
			options.arrowOffset = this.arrowOffset;
			this.popperJS = new PopperJS(reference, popper, options);
			this.popperJS.onCreate(_ => {
				this.$emit('created', this);
				this.resetTransformOrigin();
				this.$nextTick(this.updatePopper);
			});
			if (typeof options.onUpdate === 'function') {
				this.popperJS.onUpdate(options.onUpdate);
			}
			this.popperJS._popper.style.zIndex = PopupManager.nextZIndex();
			this.popperElm.addEventListener('click', stop);
		},

		/**
		 * @desc 更新弹出层
		 * @example
		 * // 更新弹出层
		 * this.updatePopper();
		 */
		updatePopper() {
			const popperJS = this.popperJS;
			if (popperJS) {
				popperJS.update();
				if (popperJS._popper) {
					popperJS._popper.style.zIndex = PopupManager.nextZIndex();
				}
			} else {
				this.createPopper();
			}
		},

		/**
		 * @desc 销毁弹出层
		 * @param {Boolean} forceDestroy - 是否强制销毁
		 * @example
		 * // 销毁弹出层
		 * this.doDestroy();
		 * 
		 * // 强制销毁弹出层
		 * this.doDestroy(true);
		 */
		doDestroy(forceDestroy) {
			/* istanbul ignore if */
			if (!this.popperJS || (this.showPopper && !forceDestroy)) return;
			this.popperJS.destroy();
			this.popperJS = null;
		},

		/**
		 * @desc 销毁弹出层元素
		 * @example
		 * // 销毁弹出层元素
		 * this.destroyPopper();
		 */
		destroyPopper() {
			if (this.popperJS) {
				this.resetTransformOrigin();
			}
		},

		/**
		 * @desc 重置变换原点
		 * @example
		 * // 重置变换原点
		 * this.resetTransformOrigin();
		 */
		resetTransformOrigin() {
			if (!this.transformOrigin) return;
			let placementMap = {
				top: 'bottom',
				bottom: 'top',
				left: 'right',
				right: 'left'
			};
			let placement = this.popperJS._popper.getAttribute('x-placement').split('-')[0];
			let origin = placementMap[placement];
			this.popperJS._popper.style.transformOrigin = typeof this.transformOrigin === 'string'
				? this.transformOrigin
				: ['top', 'bottom'].indexOf(placement) > -1 ? `center ${ origin }` : `${ origin } center`;
		},

		/**
		 * @desc 添加箭头元素
		 * @param {Element} element - 要添加箭头的元素
		 * @example
		 * // 添加箭头元素
		 * this.appendArrow(popperElement);
		 */
		appendArrow(element) {
			let hash;
			if (this.appended) {
				return;
			}

			this.appended = true;

			for (let item in element.attributes) {
				if (/^_v-/.test(element.attributes[item].name)) {
					hash = element.attributes[item].name;
					break;
				}
			}

			const arrow = document.createElement('div');

			if (hash) {
				arrow.setAttribute(hash, '');
			}
			arrow.setAttribute('x-arrow', '');
			arrow.className = 'popper__arrow';
			element.appendChild(arrow);
		}
	},

	beforeDestroy() {
		this.doDestroy(true);
		if (this.popperElm && this.popperElm.parentNode === document.body) {
			this.popperElm.removeEventListener('click', stop);
			document.body.removeChild(this.popperElm);
		}
	},

	// call destroy in keep-alive mode
	deactivated() {
		this.$options.beforeDestroy[0].call(this);
	}
};