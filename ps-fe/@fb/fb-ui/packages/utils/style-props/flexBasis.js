/*!
 * flexBasis
 * (c) 2024 lincong1987
 */


export const props = {

	// 宽度百分比
	basis: {
		type: [String, Number],
		default: undefined,
	},
}

export const style = function () {

	let style = {}
	if (this.basis) {
		style.flexBasis = this.basis
	}

	return style
}

export default {
	props,
	style
}
