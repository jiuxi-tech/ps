/*!
 * indent
 * (c) 2024 lincong1987
 */
export const props = {

// 文本缩进
	indent: {
		type: [String, Number],
		default: null,
	}, // 缩进1 = 1em
	indent1: {
		type: Boolean,
		default: false,
	}, // 缩进2 = 2em
	indent2: {
		type: Boolean,
		default: false,
	}, // 缩进3 = 3em
	indent3: {
		type: Boolean,
		default: false,
	}, // 缩进4 = 4em
	indent4: {
		type: Boolean,
		default: false,
	}, // 缩进5 = 5em
	indent5: {
		type: Boolean,
		default: false,
	}, // 缩进6 = 6em
	indent6: {
		type: Boolean,
		default: false,
	},

}

export const style = function () {

	let style = {}

	// 文本缩进
	this.indent1 && (style.textIndent = '1em')
	this.indent2 && (style.textIndent = '2em')
	this.indent3 && (style.textIndent = '3em')
	this.indent4 && (style.textIndent = '4em')
	this.indent5 && (style.textIndent = '5em')
	this.indent6 && (style.textIndent = '6em')

	if (this.indent) {
		style.textIndent = this.indent
	}

	return style
}

export default {
	props,
	style,
}

