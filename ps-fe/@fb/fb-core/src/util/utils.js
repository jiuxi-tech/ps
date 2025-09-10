/*!
 * utils
 * (c) 2022 lincong1987
 */

export function toRaw (obj) {
	return JSON.parse(JSON.stringify(obj))
}

export function uuid () {
	return 'jpxid-' + 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g,
		function (c) {
			var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8)
			return v.toString(16)
		})
}

export default {
	uuid,
	toRaw,
}
