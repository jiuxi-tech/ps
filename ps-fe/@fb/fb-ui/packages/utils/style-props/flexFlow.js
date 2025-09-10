/*!
 * flexFlow
 * (c) 2024 lincong1987
 */

export const props = {
	// flex 值
	flexFlow: {
		type: String,
		default: undefined,
	},
}

export const style = function () {

	let style = {}

	if (this.flow) {
		style.flexFlow = this.flow
	}

	return style
}

export default {
	props,
	style
}
