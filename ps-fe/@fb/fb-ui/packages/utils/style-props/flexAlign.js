/*!
 * flexAlign
 * (c) 2024 lincong1987
 */


import { unit } from './utils'

export const props = {
	// 中心对齐
	alignCenter: {
		type: Boolean,
		default: false,
	},

	align: {
		type: String,
		default: undefined,
	},

}

export const style = function () {

	let style = {}


	if (this.alignCenter) {
		style.alignItems = 'center'
		style.justifyContent = 'center'
	}

	if (this.align === 'top start') {
		style.justifyContent = 'flex-start'
		style.alignItems = 'flex-start'
	}
	if (this.align === 'top center') {
		style.justifyContent = 'center'
		style.alignItems = 'flex-start'
	}
	if (this.align === 'top end') {
		style.justifyContent = 'flex-end'
		style.alignItems = 'flex-start'
	}
	if (this.align === 'left start') {
		style.justifyContent = 'flex-start'
		style.alignItems = 'center'
	}
	if (this.align === 'left center') {
		style.justifyContent = 'flex-start'
		style.alignItems = 'center'
	}
	if (this.align === 'left end') {
		style.justifyContent = 'flex-start'
		style.alignItems = 'center'
	}
	if (this.align === 'right start') {
		style.justifyContent = 'flex-end'
		style.alignItems = 'center'
	}
	if (this.align === 'right center') {
		style.justifyContent = 'flex-end'
		style.alignItems = 'center'
	}
	if (this.align === 'right end') {
		style.justifyContent = 'flex-end'
		style.alignItems = 'center'
	}
	if (this.align === 'bottom start') {
		style.justifyContent = 'flex-start'
		style.alignItems = 'flex-end'
	}
	if (this.align === 'bottom center') {
		style.justifyContent = 'center'
		style.alignItems = 'flex-end'
	}
	if (this.align === 'bottom end') {
		style.justifyContent = 'flex-end'
		style.alignItems = 'flex-end'
	}
	if (this.align === 'center center') {
		style.justifyContent = 'center'
		style.alignItems = 'center'
	}



	return style
}

export default {
	props,
	style
}
