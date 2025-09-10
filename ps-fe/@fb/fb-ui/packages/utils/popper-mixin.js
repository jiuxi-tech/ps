import VuePopper from './vue-popper'

/**
 * @namespace PopperMixin
 * @desc Popper 弹出层混入工具
 * @description 提供 Popper 弹出层功能的 Vue 混入工具
 */

/**
 * @desc Popper 弹出层混入配置
 * @property {String} placement - 弹出层位置，默认为 'bottom-start'
 * @property {Boolean} appendToBody - 是否添加到 body
 * @property {Boolean} visibleArrow - 是否显示箭头，默认为 false
 * @property {Number} arrowOffset - 箭头偏移量
 * @property {Number} offset - 弹出层偏移量
 * @property {Number} boundariesPadding - 边界内边距
 * @property {Object} popperOptions - Popper.js 配置选项
 * @example
 * // 在 Vue 组件中使用
 * import PopperMixin from './popper-mixin';
 * 
 * export default {
 *   mixins: [PopperMixin],
 *   // 组件其他配置...
 * }
 */
const PopperMixin = {
	props: {
		/**
		 * @member {String} placement
		 * @desc 弹出层位置
		 * @default 'bottom-start'
		 */
		placement: {
			type: String,
			default: 'bottom-start',
		},
		
		/**
		 * @member {Boolean} appendToBody
		 * @desc 是否添加到 body
		 */
		appendToBody: VuePopper.props.appendToBody,
		
		/**
		 * @member {Boolean} visibleArrow
		 * @desc 是否显示箭头，影响弹窗初始化位置
		 * @default false
		 */
		visibleArrow: { // 影响弹窗初始化位置
			type: Boolean,
			default: false,
		},
		
		/**
		 * @member {Number} arrowOffset
		 * @desc 箭头偏移量
		 */
		arrowOffset: VuePopper.props.arrowOffset,
		
		/**
		 * @member {Number} offset
		 * @desc 弹出层偏移量
		 */
		offset: VuePopper.props.offset,
		
		/**
		 * @member {Number} boundariesPadding
		 * @desc 边界内边距
		 */
		boundariesPadding: VuePopper.props.boundariesPadding,
		
		/**
		 * @member {Object} popperOptions
		 * @desc Popper.js 配置选项
		 */
		popperOptions: VuePopper.props.popperOptions,
	},
	methods: VuePopper.methods,
	data: VuePopper.data,
	beforeDestroy: VuePopper.beforeDestroy,
}

export default PopperMixin