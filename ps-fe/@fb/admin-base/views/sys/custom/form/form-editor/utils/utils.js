/*!
 * utils
 * (c) 2021 lincong1987
 */

import {
	toNumber,
	pad,
	unset,
	isArray,
	uniqWith,
	filter,
	each,
} from 'lodash-es'

export const getComponentNameByType = function () {
}

export function uniqAndCleanArray (arr = [], uniqKey = 'label') {

	let data = []

	if (isArray(arr)) {
		data = uniqWith(filter(arr, (row) => {
			return (row[uniqKey] + '').trim() !== ''
		}), (a, b) => {
			return a[uniqKey] === b[uniqKey]
		})
	}
	//console.log('uniqAndCleanArray', data)
	return data

}

export function arrayToObject (arr = [], label = 'label', value = 'value') {
	let param = {}

	if (isArray(arr)) {
		each(arr, (n, i) => {
			param[n[label]] = n[value]
		})
	}

	return param
}

export function stringToArray (str = '', splitPattern = ',') {
	if (typeof str === 'undefined') {
		return []
	}

	if (isArray(str)) {
		return str
	}

	if (typeof (str + '').trim() === 'string') {
		return str.split(splitPattern).map(n => n.trim())
	}
}

export function toRaw (obj) {
	return JSON.parse(JSON.stringify(obj))
}

export function uuid () {
	return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g,
		function (c) {
			var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8)
			return v.toString(16)
		})
}

export function computeCurrentCtx (exp) {

	let rs = null

	try {
		let fn = new Function('app', 'ctx', exp)
		let userInfo = this.$datax.get('userInfo')
		let ctx = {
			...toRaw(userInfo),
		}

		rs = fn.apply(this, [app || {}, ctx])

	} catch (e) {
		//console.log(e)
	}

	if (!['string', 'number', 'boolean'].includes(typeof rs)) {
		return '只允许返回字符串，数字或布尔值'
	}

	return rs ? rs : '空值'
}

export default {
	uuid,
	toRaw,
	toNumber,
	arrayToObject,
	uniqAndCleanArray,
	stringToArray,
	computeCurrentCtx
}
