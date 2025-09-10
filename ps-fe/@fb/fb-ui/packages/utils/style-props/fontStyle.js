/*!
 * fontStyle
 * (c) 2024 lincong1987
 */

export const props = {

	// 斜体
	italic: {
		type: Boolean,
		default: false,
	}, // 非斜体
	noItalic: {
		type: Boolean,
		default: false,
	}, // 脚标
	ordinal: {
		type: Boolean,
		default: false,
	}, // 文字间间距
	letterSpacing: {
		type: [String, Number],
		default: null,
	},
	'letterSpacing-3': {
		type: Boolean,
		default: false,
	},
	'letterSpacing-2': {
		type: Boolean,
		default: false,
	},
	'letterSpacing-1': {
		type: Boolean,
		default: false,
	},
	letterSpacing0: {
		type: Boolean,
		default: false,
	},
	letterSpacing1: {
		type: Boolean,
		default: false,
	},
	letterSpacing2: {
		type: Boolean,
		default: false,
	},
	letterSpacing3: {
		type: Boolean,
		default: false,
	},
	letterSpacing4: {
		type: Boolean,
		default: false,
	},

}

export const style = function () {

	let style = {}

	// 斜体
	this.italic && (style.fontStyle = 'italic')
	this.noItalic && (style.fontStyle = 'normal')

	this.letterSpacing && (style.letterSpacing = this.letterSpacing)
	this.letterSpacing0 && (style.letterSpacing = '0px')
	this.letterSpacing1 && (style.letterSpacing = '1px')
	this.letterSpacing2 && (style.letterSpacing = '2px')
	this.letterSpacing3 && (style.letterSpacing = '3px')
	this.letterSpacing3 && (style.letterSpacing = '3px')
	this.letterSpacing4 && (style.letterSpacing = '4px')
	this['letterSpacing-1'] && (style.letterSpacing = '-1px')
	this['letterSpacing-2'] && (style.letterSpacing = '-2px')
	this['letterSpacing-3'] && (style.letterSpacing = '-3px')

	// 脚标
	this.ordinal && (style.fontVariant = 'ordinal')

	return style
}

export default {
	props,
	style,
}
