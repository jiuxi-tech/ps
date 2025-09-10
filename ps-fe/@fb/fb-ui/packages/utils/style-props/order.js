/*!
 * order
 * (c) 2024 lincong1987
 */

export const props = {

	order: {
		type: [String, Number],
		default: undefined,
	}
}

export const style = function () {

	let style = {}

	this.order && (style['order'] = this.order)

	return style
}

export default {
	props,
	style,
}
