/*!
* PropTypes - Vue 组件属性类型定义工具
* (c) 2020 lincong1987
* @description 用于定义 Vue 组件的属性类型，提供更严格的类型检查
*/

/**
 * @namespace PropTypes
 * @desc Vue 组件属性类型定义工具集合
 * @example
 * // 在组件中使用
 * import { PropTypes } from '@fb/fb-ui/packages/utils/PropTypes'
 * 
 * export default {
 *   props: {
 *     // 使用预定义的类型
 *     title: PropTypes.string,
 *     visible: PropTypes.boolean,
 *     count: PropTypes.number,
 *     items: PropTypes.array,
 *     onChange: PropTypes.func,
 *     config: PropTypes.object
 *   }
 * }
 */
var PropTypes = {

	/**
	 * @member {Object} string
	 * @desc 字符串类型定义
	 * @property {Function} type - 数据类型为 String
	 * @property {String} default - 默认值为空字符串
	 * @example
	 * props: {
	 *   title: PropTypes.string
	 * }
	 */
	string: {
		type: String,
		default: ''
	},

	/**
	 * @member {Object} boolean
	 * @desc 布尔类型定义
	 * @property {Function} type - 数据类型为 Boolean
	 * @property {Boolean} default - 默认值为 false
	 * @example
	 * props: {
	 *   visible: PropTypes.boolean
	 * }
	 */
	boolean: {
		type: Boolean,
		default: false
	},

	/**
	 * @member {Object} number
	 * @desc 数字类型定义
	 * @property {Function} type - 数据类型为 Number
	 * @property {Number} default - 默认值为 0
	 * @example
	 * props: {
	 *   count: PropTypes.number
	 * }
	 */
	number: {
		type: Number,
		default: 0
	},

	/**
	 * @member {Object} object
	 * @desc 对象类型定义
	 * @property {Function} type - 数据类型为 Object
	 * @property {Object} default - 默认值为 null
	 * @example
	 * props: {
	 *   config: PropTypes.object
	 * }
	 */
	object: {
		type: Object,
		default: null
	},

	/**
	 * @member {Object} array
	 * @desc 数组类型定义
	 * @property {Function} type - 数据类型为 Array
	 * @property {Array} default - 默认值为 null
	 * @example
	 * props: {
	 *   items: PropTypes.array
	 * }
	 */
	array: {
		type: Array,
		default: null
	},

	/**
	 * @member {Object} func
	 * @desc 函数类型定义
	 * @property {Function} type - 数据类型为 Function
	 * @property {null} default - 默认值为 null
	 * @example
	 * props: {
	 *   onChange: PropTypes.func
	 * }
	 */
	func: {
		type: Function,
		default: null
	},

	/**
	 * @method custom
	 * @desc 自定义类型定义
	 * @param {Function} type - 自定义数据类型
	 * @param {*} def - 自定义默认值
	 * @returns {Object} 返回包含 type 和 default 属性的对象
	 * @example
	 * props: {
	 *   // 自定义类型，默认值为 'default'
	 *   customProp: PropTypes.custom(String, 'default'),
	 *   
	 *   // 自定义类型，默认值为 null
	 *   customProp2: PropTypes.custom(Object, null)
	 * }
	 */
	custom: function(type, def) {
		return {
			type: type || String,
			default: def || null
		}
	}
}

export default PropTypes