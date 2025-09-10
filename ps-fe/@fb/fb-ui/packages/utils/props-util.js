/**
 * props-util - Vue 组件属性工具函数集
 * @description 提供 Vue 组件属性相关的工具函数
 */

/**
 * @namespace PropsUtil
 * @desc Vue 组件属性工具函数集合
 */

/**
 * @desc Vue 属性类型定义
 * @property {Object} string - 字符串类型定义
 * @property {Object} boolean - 布尔类型定义
 * @property {Object} number - 数字类型定义
 * @property {Object} object - 对象类型定义
 * @property {Object} array - 数组类型定义
 * @property {Object} func - 函数类型定义
 * @property {Function} custom - 自定义类型定义函数
 * @example
 * // 使用预定义的类型
 * props: {
 *   title: vueType.string,
 *   visible: vueType.boolean,
 *   count: vueType.number
 * }
 * 
 * // 使用自定义类型
 * props: {
 *   customProp: vueType.custom(String, 'default')
 * }
 */
export const vueType = {
	/**
	 * @member {Object} string
	 * @desc 字符串类型定义
	 * @property {Function} type - 数据类型为 String
	 * @property {String} default - 默认值为空字符串
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
	 */
	boolean: {
		type: Boolean,
		default: false
	},
	
	/**
	 * @member {Object} number
	 * @desc 数字类型定义
	 * @property {Function} type - 数据类型为 Number
	 * @property {String} default - 默认值为空字符串
	 */
	number: {
		type: Number,
		default: ''
	},
	
	/**
	 * @member {Object} object
	 * @desc 对象类型定义
	 * @property {Function} type - 数据类型为 Object
	 * @property {String} default - 默认值为空字符串
	 */
	object: {
		type: Object,
		default: ''
	},
	
	/**
	 * @member {Object} array
	 * @desc 数组类型定义
	 * @property {Function} type - 数据类型为 Array
	 * @property {String} default - 默认值为空字符串
	 */
	array: {
		type: Array,
		default: ''
	},
	
	/**
	 * @member {Object} func
	 * @desc 函数类型定义
	 * @property {Function} type - 数据类型为 Function
	 * @property {null} default - 默认值为 null
	 */
	func: {
		type: Function,
		default: null
	},
	
	/**
	 * @method custom
	 * @desc 自定义类型定义函数
	 * @param {Function} type - 自定义数据类型
	 * @param {*} def - 自定义默认值
	 * @returns {Object} 返回包含 type 和 default 属性的对象
	 * @example
	 * // 自定义类型，默认值为 'default'
	 * const customProp = vueType.custom(String, 'default');
	 * 
	 * // 自定义类型，默认值为 null
	 * const customProp2 = vueType.custom(Object, null);
	 */
	custom(type, def) {
		return {
			type: type || String,
			default: def || ''
		}
	}
}

/**
 * @desc 初始化默认属性
 * @param {Object} _props - 初始化类型
 * @param {Object} _defaultProps - 默认值
 * @returns {Object} 返回处理后的属性对象
 * @example
 * // 初始化组件属性
 * const props = {
 *   title: vueType.string,
 *   visible: vueType.boolean
 * };
 * 
 * const defaultProps = {
 *   title: '默认标题',
 *   visible: true
 * };
 * 
 * const initializedProps = initDefaultProps(props, defaultProps);
 */
export function initDefaultProps(_props, _defaultProps) {
	let props = _props

	if (_defaultProps) {
		for (let i in props) {
			let item = props[i]

			if (!item) {
				break;
			}

			if (Object.prototype.toString.call(_defaultProps[i]) === '[object Array]') {
				item.default = () => _defaultProps[i]
			} else if (Object.prototype.toString.call(_defaultProps[i]) === '[object Object]') {
				item.default = () => (_defaultProps[i])
			} else if (Object.prototype.toString.call(_defaultProps[i]) === '[object Boolean]') {
				item.default = _defaultProps[i]
			} else if (item.default || item.default === null) {
				item.default = _defaultProps[i] || item.default || null
			}

		}
	}

	return props
}

export default {
	vueType,
	initDefaultProps
}