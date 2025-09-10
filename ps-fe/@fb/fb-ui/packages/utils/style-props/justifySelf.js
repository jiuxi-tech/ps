/*!
 * justifySelf
 * (c) 2024 lincong1987
 */

export const props = {

	justifySelf: {
		type: String,
		default: undefined,
	},

	jsAuto: {
		type: Boolean,
		default: false,
	},
	jsStart: {
		type: Boolean,
		default: false,
	},
	jsEnd: {
		type: Boolean,
		default: false,
	},
	jsCenter: {
		type: Boolean,
		default: false,
	},
	jsBaseline: {
		type: Boolean,
		default: false,
	},
	jsStretch: {
		type: Boolean,
		default: false,
	},

}

export const style = function () {

	let style = {}

	this.justifySelf && (style.justifySelf = this.justifySelf)
	this.jsAuto && (style.justifySelf = 'auto')
	this.jsStart && (style.justifySelf = 'start')
	this.jsEnd && (style.justifySelf = 'end')
	this.jsCenter && (style.justifySelf = 'center')
	this.jsBaseline && (style.justifySelf = 'baseline')
	this.jsStretch && (style.justifySelf = 'stretch')

	return style
}

export default {
	props,
	style,
}
