/*!
 * layout
 * (c) 2024 lincong1987
 */

import { unit } from './utils'
export const props = {

	// 宽度
	width: {
		type: [String, Number],
		default: undefined,
	},

	// 高度
	height: {
		type: [String, Number],
		default: undefined,
	},

	// 宽度 缩写
	w: {
		type: [String, Number],
		default: undefined,
	},

	// 高度 缩写
	h: {
		type: [String, Number],
		default: undefined,
	},

	// 最大高度
	maxHeight: {
		type: [String, Number],
		default: undefined,
	},
	// 最小高度
	minHeight: {
		type: [String, Number],
		default: undefined,
	},
	// 最大宽度
	maxWidth: {
		type: [String, Number],
		default: undefined,
	},
	// 最小宽度
	minWidth: {
		type: [String, Number],
		default: undefined,
	},

	// 最大高度 缩写
	hMax: {
		type: [String, Number],
		default: undefined,
	},
	// 最小高度 缩写
	hMin: {
		type: [String, Number],
		default: undefined,
	},
	// 最大宽度 缩写
	wMax: {
		type: [String, Number],
		default: undefined,
	},
	// 最小宽度 缩写
	wMin: {
		type: [String, Number],
		default: undefined,
	},


}

export const style = function () {

	let style = {}

	this.width && (style.width = unit(this.width))
	this.maxWidth && (style.maxWidth = unit(this.maxWidth))
	this.minWidth && (style.minWidth = unit(this.minWidth))
	this.w && (style.width = unit(this.w))
	this.wMax && (style.maxWidth = unit(this.wMax))
	this.wMin && (style.minWidth = unit(this.wMin))

	this.height && (style.height = unit(this.height))
	this.maxHeight && (style.maxHeight = unit(this.maxHeight))
	this.minHeight && (style.minHeight = unit(this.minHeight))
	this.h && (style.height = unit(this.h))
	this.hMax && (style.maxHeight = unit(this.hMax))
	this.hMin && (style.minHeight = unit(this.hMin))

	return style
}

export default {
	props,
	style,
}
