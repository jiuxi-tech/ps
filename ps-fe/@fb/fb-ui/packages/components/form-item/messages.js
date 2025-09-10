/*!
 * messages
 * (c) 2020 lincong1987
 */

export function newMessages (msg) {
	return {
		default: '%s 校验失败',
		required: '请输入 %s',
		enum: '%s 必须是 %s 中的一个',
		whitespace: '%s 不能为空',
		date: {
			format: '%s 日期对象 %s 无效，当转换 %s时',
			parse: '%s 日期对象不能被解析, %s 无效',
			invalid: '%s 日期对象 %s 无效',
		},
		types: {
			string: '%s 不是一个 字符串',
			method: '%s 不是一个 方法',
			array: '%s 不是一个 数组',
			object: '%s 不是一个 对象',
			date: '%s 不是一个 日期',
			boolean: '%s 不是一个 布尔值',
			number: '%s 不是一个 数字',
			integer: '%s 不是一个 整数',
			float: '%s 不是一个 浮点数',
			regexp: '%s 不是一个有效的 表达式',
			email: '%s 不是一个有效的 邮件地址',
			url: '%s 不是一个有效的 请求地址',
			hex: '%s 不是一个有效的 十六进制',
		},
		string: {
			len: '%s 须包含 %s 个字符',
			min: '%s 须大于 %s 个字符',
			max: '%s 须小于 %s 个字符',
			range: '%s 须小于 %s 到 %s 个字符',
		},
		number: {
			len: '%s 须等于 %s',
			min: '%s 不小于 %s',
			max: '%s 不大于 %s',
			range: '%s 须介于 %s 、 %s 之间',
		},
		array: {
			len: '%s 的长度须等于 %s',
			min: '%s 的长度须大于 %s',
			max: '%s 的长度须小于 %s',
			range: '%s 的长度须介于 %s 、 %s 之间',
		},
		pattern: {
			mismatch: '%s 的值 %s 不匹配正则 %s',
		},
		clone () {
			const cloned = JSON.parse(JSON.stringify(this))
			cloned.clone = this.clone
			return cloned
		},
	}
}

const messages = newMessages()

export default messages
