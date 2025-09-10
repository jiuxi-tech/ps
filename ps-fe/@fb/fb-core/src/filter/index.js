/**
 * 添加常用的过滤器
 */
//import util from '../util/index.js'

//import * as lodash from 'lodash-es'
//
//export default lodash

import { isArray, isNumber } from 'lodash-es'

/**
 * 日期格式化
 * @param _date
 * @param format
 * @returns {*}
 */
export function date (timestamp, format) {
	if (!timestamp) return ''
	format = format || 'yyyy-MM-dd hh:mm:ss'
	timestamp = Number(timestamp)
	const time = new Date(timestamp)
	const obj = {
		'y+': time.getFullYear(),
		'M+': time.getMonth() + 1,
		'd+': time.getDate(),
		'h+': time.getHours(),
		'm+': time.getMinutes(),
		's+': time.getSeconds(),
	}

	if (new RegExp('(y+)').test(format)) {
		format = format.replace(RegExp.$1, obj['y+'])
	}
	Object.keys(obj).forEach((j) => {
		if (new RegExp(`(${j})`).test(format)) {
			format = format.replace(RegExp.$1,
				(RegExp.$1.length === 1) ? (obj[j]) : ((`00${obj[j]}`).substr(
					(`${obj[j]}`).length)))
		}
	})
	return format
}

/**
 * 资金格式化插件
 * @param value
 * @returns {string|*}
 */
export function money (value) {
	const m = []
	value = Number(value).toFixed(2)
	// 获取小数部分
	const decimals = value.match(/\.[0-9]*/g)
	// 获取整数部分
	const integer = parseInt(value, 10).toString()
	const temp = integer.split('')
	const length = temp.length

	// 添加","分隔符
	function format () {
		let count = 0
		for (let n = length; n > 0; n--, count++) {
			if (count && count % 3 === 0) {
				m.unshift(',')
				count = 0
			}
			m.unshift(temp.pop())
		}
		const result = m.join('')
		return decimals ? result.concat(decimals) : result
	}

	return length > 3 ? format() : value
}

/**
 * 银行卡，四位加一空格
 * @param value
 * @returns {*}
 */
export function card (value) {
	value = `${value}`
	const reg = /([0-9]{4})/g
	if (value) {
		value = value.replace(reg, '$1 ')
	}
	return value
}

/**
 * 给字符串中间加***
 * @param value
 * @param frontLen
 * @param backLen
 * @returns {*}
 */
export function safety (value, frontLen, backLen) {
	if (value) {
		const len = value.length
		let front = ''
		let back = ''
		if (frontLen && len > frontLen) {
			front = value.slice(0, frontLen)
		}
		if (backLen && len > (frontLen + backLen)) {
			back = value.slice(len - backLen)
		}
		return `${front}***${back}`
	}
	return ''
}

/**
 * 把数据字典中的值转换成text
 * @param value
 * @param arr
 * @returns {string}
 */
export function map (value, arr) {
	let name = ''
	if (arr && lodash.isArray(arr)) {
		arr.forEach((item) => {
			if (item.value === value) {
				name = item.text
			}
		})
	}
	return name
}

/**
 * 过滤掉数据中的值
 * @param value
 * @param arr
 * @returns {string}
 */
export function allow (value, arr) {
	const _arr = []
	if (isArray(value)) {
		value.forEach((obj) => {
			if (isArray(arr)) {
				if (arr.indexOf(obj.value) !== -1) {
					_arr.push(obj)
				}
			}
		})
	}
	return _arr
}

/**
 * capitalize
 * @param text
 * @returns {string}
 */
export function capitalize (text) {
	return text[0].toUpperCase() + text.slice(1)
}

export function uppercase (text) {
	return text.toUpperCase()
}

export function lowercase (text) {
	return text.toLowerCase()
}

export function defaults (value, def, blank) {
	blank = blank || true
	if (typeof blank === 'boolean' && blank === true) {
		return (value == null || value == '') ? def : value
	} else {
		return (value == null) ? def : value
	}
}

export function decodeHTML (value) {
	var s = ''
	if (value.length == 0) return ''
	s = value.replace(/&amp;/g, '&')
	s = s.replace(/&lt;/g, '<')
	s = s.replace(/&gt;/g, '>')
	s = s.replace(/&nbsp;/g, ' ')
	s = s.replace(/&#39;/g, '\'')
	s = s.replace(/&quot;/g, '"')
	return s
}

export function encodeHTML (value) {
	return String(value).
		replace(/&/g, '&amp;').
		replace(/</g, '&lt;').
		replace(/>/g, '&gt;').
		replace(/"/g, '&quot;').
		replace(/'/g, '&#39;')
}

export function toLowerLine (str, line = '-') {

	let temp = str.replace(/[A-Z]/g, function (match) {
		return line + match.toLowerCase()
	})
	if (temp.slice(0, line.length) === line) { //如果首字母是大写，执行replace时会多一个_，这里需要去掉
		temp = temp.slice(line.length)
	}
	return temp
}

export function toCamel (str, line = '-') {
	return str.replace(
		new RegExp('([^' + line + '])(?:' + line + '+([^' + line + ']))', 'g'),
		function ($0, $1, $2) {
			return $1 + $2.toUpperCase()
		})
}

export function oneOf (value, defaults, array = []) {
	if (array.includes(value)) {
		return defaults
	}
	return value
}

export function trim (value) {
	return (value + '').trim()

}

export function cat (value, catValue) {
	return value + '' + catValue
}

export function lower (value) {
	return value.toLowerCase()
}

export function upper (value) {
	return value.toUpperCase()
}

export function nl2br (value) {
	return value.replace(/\n/ig, '<br>')
}

export function n2p (value) {
	return '<p>' + value.replace(/\n/ig, '</p><p>') + '</p>'
}

export function join (value, string) {
	return value.join(string)
}

export function timeDiff (time) {
	if (!time) {
		return ''
	} else if (time instanceof Date) {
		return repTime((new Date()).getTime() - time.getTime(), time)
	} else if (isNumber(time)) {
		//if (export function isString(time)) {
		time = new Date(time)
		//}
		return repTime((new Date()).getTime() - time.getTime(), time)
	} else {
		return repTime((new Date()).getTime() -
			(new Date(Date.parse(time.replace(/-/g, '/')))).getTime(),
			new Date(Date.parse(time.replace(/-/g, '/'))))
	}

	function repTime (differential, time) {
		if (differential <= 0) return '刚刚'
		differential = differential / 1000
		//if(differential>86400)
		//return parseInt(differential/86400)+"天前";
		if (differential > 86400) {
			return time.getFullYear() + '-' + (time.getMonth() + 1) + '-' +
				time.getDate()
		}
		//return parseInt(differential/86400)+"天前";
		if (differential > 3600) {
			return parseInt(differential / 3600) + '小时前'
		}

		if (differential > 60) {
			return parseInt(differential / 60) + '分钟前'
		} else {
			return parseInt(differential) + '秒前'
		}
	}
}
