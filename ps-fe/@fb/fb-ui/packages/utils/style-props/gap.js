/*!
 * gap
 * (c) 2024 lincong1987
 */

export const props = {
	gap: {
		type: [String, Number],
		default: undefined,
	},

	columnGap: {
		type: [String, Number],
		default: undefined,
	},
	rowGap: {
		type: [String, Number],
		default: undefined,
	},

}

export const style = function () {

	let style = {}

	if (this.gap) {
		style.gap = this.gap
	}
	if (this.columnGap) {
		style.columnGap = this.columnGap
	}
	if (this.rowGap) {
		style.rowGap = this.rowGap
	}

	return style
}

export default {
	props,
	style,
}
