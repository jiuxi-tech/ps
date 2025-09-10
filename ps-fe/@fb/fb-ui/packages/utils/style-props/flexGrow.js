/*!
 * grow
 * (c) 2024 lincong1987
 */


export const props = {
	glow: {
		type: Boolean,
		default: false,
	},
}

export const style = function () {

	let style = {}

	if (this.glow) {
		style.flexGrow = this.glow
	}

	return style
}

export default {
	props,
	style
}
