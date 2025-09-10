
export const vueType = {
	string: {
		type: String,
		default: ''
	},
	boolean: {
		type: Boolean,
		default: false
	},
	number: {
		type: Number,
		default: ''
	},
	object: {
		type: Object,
		default: ''
	},
	array: {
		type: Array,
		default: ''
	},
	func: {
		type: Function,
		default: null
	},
	custom(type, def) {
		return {
			type: type || String,
			default: def || ''
		}
	}
}
/**
 * initDefaultProps 组件props生成器
 * @param {object} _props 初始化类型
 * @param {object} _defaultProps 默认值
 * */
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
