/*!
 * padding
 * (c) 2024 lincong1987
 */

import { unit } from './utils'

export const props = {

	pl: {
		type: [String, Number],
		default: null,
	},
	pr: {
		type: [String, Number],
		default: null,
	},
	pt: {
		type: [String, Number],
		default: null,
	},
	pb: {
		type: [String, Number],
		default: null,
	},

	/**
	 * 水平内边距
	 * px='8px' 等同于 padding-left: 8px; padding-right: 8px;
	 */
	px: {
		type: [String, Number],
		default: undefined,
	},
	// 垂直内边距 py='8px' 等同于 padding-top: 8px; padding-bottom: 8px;
	py: {
		type: [String, Number],
		default: undefined,
	},
	padding: {
		type: [String, Number],
		default: undefined,
	},

}

export const style = function () {

	let style = {}

	this.padding && (style.padding = unit(this.padding))
	if (this.px) {
		style.paddingLeft = style.paddingRight = unit(this.px)
	}
	if (this.py) {
		style.paddingTop = style.paddingBottom = unit(this.py)
	}
	this.pt && (style.paddingTop = unit(this.pt))
	this.pr && (style.paddingRight = unit(this.pr))
	this.pb && (style.paddingBottom = unit(this.pb))
	this.pl && (style.paddingLeft = unit(this.pl))

	return style
}

export default {
	props,
	style,
}
