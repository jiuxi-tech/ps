/*!
 * textAlign
 * (c) 2024 lincong1987
 */

export const props = {
	// 文本对齐
	align: {
		type: String,
		default: null,
	},

	// 文本居中对齐
	alignCenter: {
		type: Boolean,
		default: false,
	},
	// 文本右对齐
	alignRight: {
		type: Boolean,
		default: false,
	},
	// 文本左对齐
	alignLeft: {
		type: Boolean,
		default: false,
	},
	// 文本两端对齐
	alignJustify: {
		type: Boolean,
		default: false,
	},
}

export const textAlignMap = {
	center: 'center',
	right: 'right',
	left: 'left',
	justify: 'justify',
}

export const style = function () {

	let style = {}

	// 文本对齐
	this.alignCenter && (style.textAlign = textAlignMap.center)
	this.alignRight && (style.textAlign = textAlignMap.right)
	this.alignLeft && (style.textAlign = textAlignMap.left)
	this.alignJustify && (style.textAlign = textAlignMap.justify)
	this.align && (style.textAlign = this.align)

	return style
}

export default {
	textAlignMap,
	props,
	style,
}
