/*!
 * display
 * (c) 2024 lincong1987
 */

export const props = {
	display: {
		type: String,
		default: undefined,
	},
	inline: {
		type: Boolean,
		default: false,
	},
	block: {
		type: Boolean,
		default: false,
	},
}

export const style = function () {

	let style = {}

	this.display && (style.display = this.display)

	if (this.inline) {
		style.display = 'inline'
	}
	if (this.block) {
		style.display = 'block'
	}

	return style
}

export default {
	props,
	style,
}
