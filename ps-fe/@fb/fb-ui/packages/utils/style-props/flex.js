/*!
 * flex
 * (c) 2024 lincong1987
 */

export const props = {
	// flex 值
	flex: {
		type: String,
		default: undefined,
	},
	flex1: {
		type: Boolean,
		default: false,
	},
	flexAuto: {
		type: Boolean,
		default: false,
	},
	flexInitial: {
		type: Boolean,
		default: false,
	},
	noFlex: {
		type: Boolean,
		default: false,
	},

}

export const style = function () {

	let style = {}

	this.flex1 && (style.flex = '1 1 0%')
	this.flexAuto && (style.flex = '1 1 auto')
	this.flexInitial && (style.flex = '0 1 auto')
	this.noFlex && (style.flex = 'none')

	this.flex && (style.flex = this.flex)

	return style
}

export default {
	props,
	style,
}
