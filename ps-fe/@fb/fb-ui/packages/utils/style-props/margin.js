/*!
 * margin
 * (c) 2024 lincong1987
 */

import { unit } from './utils'
export const props = {
	// 左外边距
	ml: {
		type: [String, Number],
		default: undefined,
	}, // 右外边距
	mr: {
		type: [String, Number],
		default: undefined,
	}, // 底外边距
	mb: {
		type: [String, Number],
		default: undefined,
	}, // 上外边距
	mt: {
		type: [String, Number],
		default: undefined,
	},
	/**
	 * 水平外边距
	 * mx='8px' 等同于 margin-left: 8px; margin-right: 8px;
	 */
	mx: {
		type: [String, Number],
		default: undefined,
	}, // 垂直外边距 my='8px' 等同于 margin-top: 8px; margin-bottom: 8px;
	my: {
		type: [String, Number],
		default: undefined,
	},
	margin: {
		type: [String, Number],
		default: undefined,
	},
}

export const style = function () {

	let style = {}

	this.margin && (style.margin = unit(this.margin))
	if (this.mx) {
		style.marginLeft = unit(this.mx)
		style.marginRight = unit(this.mx)
	}
	if (this.my) {
		style.marginTop = unit(this.my)
		style.marginBottom = unit(this.my)
	}
	this.mt && (style.marginTop = unit(this.mt))
	this.mr && (style.marginRight = unit(this.mr))
	this.mb && (style.marginBottom = unit(this.mb))
	this.ml && (style.marginLeft = unit(this.ml))

	return style
}

export default {
	props,
	style,
}
