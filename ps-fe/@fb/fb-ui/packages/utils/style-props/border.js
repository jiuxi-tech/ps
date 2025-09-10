/*!
 * border
 * (c) 2024 lincong1987
 */

import { unit } from './utils'

export const props = {

	// 边框
	border: {
		type: [Boolean, String],
		default: undefined,
	},

	// 上边框
	bt: {
		type: String,
		default: undefined,
	},
	// 右边框
	br: {
		type: String,
		default: undefined,
	},
	// 下边框
	bb: {
		type: String,
		default: undefined,
	},
	// 左边框
	bl: {
		type: String,
		default: undefined,
	},

	// 圆角
	radius: {
		type: [Boolean, String, Number],
		default: undefined,
	},

	radiusXs: {
		type: Boolean,
		default: false,
	},
	radiusS: {
		type: Boolean,
		default: false,
	},
	radiusM: {
		type: Boolean,
		default: false,
	},
	radiusL: {
		type: Boolean,
		default: false,
	},
	radiusXl: {
		type: Boolean,
		default: false,
	},
	radiusXxl: {
		type: Boolean,
		default: false,
	},

}

export const style = function () {

	let style = {}

	this.border && (style.border = this.border)
	this.bt && (style.borderTop = this.bt)
	this.br && (style.borderRight = this.br)
	this.bb && (style.borderBottom = this.bb)
	this.bl && (style.borderLeft = this.bl)


	if (typeof this.radius === 'boolean' && this.radius === true) {
		style.borderRadius = '4px'
	}
	if (typeof this.radius === 'string' || typeof this.radius === 'number') {
		style.borderRadius = unit(this.radius)
	}
	this.radiusXs && (style.borderRadius = '2px')
	this.radiusS && (style.borderRadius = '4px')
	this.radiusM && (style.borderRadius = '6px')
	this.radiusL && (style.borderRadius = '8px')
	this.radiusXl && (style.borderRadius = '12px')
	this.radiusXxl && (style.borderRadius = '16px')

	return style
}

export default {
	props,
	style,
}
