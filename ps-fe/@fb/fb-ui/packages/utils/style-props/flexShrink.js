/*!
 * shrink
 * (c) 2024 lincong1987
 */


export const props = {

	//
	shrink: {
		type: [Boolean, String],
		default: undefined,
	},

}

export const style = function () {

	let style = {}
	if (this.shrink) {
		style.flexShrink = this.shrink
	}

	return style
}

export default {
	props,
	style
}
