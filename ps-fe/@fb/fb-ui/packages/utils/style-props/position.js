/*!
 * position
 * (c) 2024 lincong1987
 */

import { unit } from './utils'
export const props = {
	// 相对定位
	relative: {
		type: Boolean,
		default: false,
	},
	// 绝对定位
	absolute: {
		type: Boolean,
		default: false,
	},
	//   固定定位
	fixed: {
		type: Boolean,
		default: false,
	},
	//   粘性定位
	sticky: {
		type: Boolean,
		default: false,
	},

	position: {
		type: String,
		default: undefined,
	},



	top: {
		type: [String, Number],
		default: undefined,
	},
	left: {
		type: [String, Number],
		default: undefined,
	},
	right: {
		type: [String, Number],
		default: undefined,
	},
	bottom: {
		type: [String, Number],
		default: undefined,
	},
}

export const style = function () {

	let style = {}

	this.relative && (style.position = 'relative')
	this.absolute && (style.position = 'absolute')
	this.fixed && (style.position = 'fixed')
	this.sticky && (style.position = 'sticky')
	this.position && (style.position = this.position)

	this.top && (style.top = unit(this.top))
	this.right && (style.right = unit(this.right))
	this.bottom && (style.bottom = unit(this.bottom))
	this.left && (style.left = unit(this.left))

	return style
}

export default {
	props,
	style,
}
