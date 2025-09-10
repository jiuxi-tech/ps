/*!
 * cursor
 * (c) 2024 lincong1987
 */

export const props = {

// 鼠标样式
	cursor: {
		type: String,
		default: undefined,
	},
// 手型
	pointer: {
		type: Boolean,
		default: false,
	},

}

export const style = function () {

	let style = {}

	this.cursor && (style.cursor = this.cursor)
	this.pointer && (style.cursor = 'pointer')


	return style
}

export default {
	props,
	style,
}
