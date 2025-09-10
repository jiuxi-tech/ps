/*!
 * boxSizing
 * (c) 2024 lincong1987
 */
export const props = {

	//  标准盒子模型。width 与 height 只包括内容的宽和高，不包括边框（border），内边距（padding），外边距（margin）。
	boxContent: {
		type: Boolean,
		default: false,
	}, // width 和 height 属性包括内容，内边距和边框，但不包括外边距。
	boxBorder: {
		type: Boolean,
		default: false,
	}, // 盒模型
	boxSizing: {
		type: String,
		default: undefined,
	},

}

export const style = function () {

	let style = {}

	this.boxSizing && (style.boxSizing = this.boxSizing)
	this.boxContent && (style.boxSizing = 'content-box')
	this.boxBorder && (style.boxSizing = 'border-box')

	return style
}

export default {
	props,
	style,
}

