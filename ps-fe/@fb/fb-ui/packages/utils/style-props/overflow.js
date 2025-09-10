/*!
 * overflow
 * (c) 2024 lincong1987
 */

export const props = {

	overflow: {
		type: String,
		default: undefined,
	},
	overflowHidden: {
		type: Boolean,
		default: false,
	},
	overflowScroll: {
		type: Boolean,
		default: false,
	},
	overflowAuto: {
		type: Boolean,
		default: false,
	},
	overflowVisible: {
		type: Boolean,
		default: false,
	},


	overflowX: {
		type: String,
		default: undefined,
	},
	overflowXScroll: {
		type: Boolean,
		default: false,
	},
	overflowXAuto: {
		type: Boolean,
		default: false,
	},
	overflowXHidden: {
		type: Boolean,
		default: false,
	},
	overflowXVisible: {
		type: Boolean,
		default: false,
	},






	// 溢出宽度
	overflowY: {
		type: String,
		default: undefined,
	},
	overflowYScroll: {
		type: Boolean,
		default: false,
	},
	overflowYAuto: {
		type: Boolean,
		default: false,
	},
	overflowYHidden: {
		type: Boolean,
		default: false,
	},
	overflowYVisible: {
		type: Boolean,
		default: false,
	},

}

export const style = function () {

	let style = {}
	this.overflow && (style.overflow = this.overflow)
	this.overflowHidden && (style.overflow = 'hidden')
	this.overflowScroll && (style.overflow = 'scroll')
	this.overflowAuto && (style.overflow = 'auto')
	this.overflowVisible && (style.overflow = 'visible')

	this.overflowX && (style.overflowX = this.overflowX)
	this.overflowXHidden && (style.overflowX = 'hidden')
	this.overflowXScroll && (style.overflowX = 'scroll')
	this.overflowXAuto && (style.overflowX = 'auto')
	this.overflowXVisible && (style.overflowX = 'visible')

	this.overflowY && (style.overflowY = this.overflowY)
	this.overflowYHidden && (style.overflowY = 'hidden')
	this.overflowYScroll && (style.overflowY = 'scroll')
	this.overflowYAuto && (style.overflowY = 'auto')
	this.overflowYVisible && (style.overflowY = 'visible')

	return style
}

export default {
	props,
	style,
}
