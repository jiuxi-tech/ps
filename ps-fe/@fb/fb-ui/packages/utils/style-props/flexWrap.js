/*!
 * flexWrap
 * (c) 2024 lincong1987
 */

export const props = {

	// ['nowrap', 'wrap']
	noWrap: {
		type: Boolean,
		default: true,
	},

	wrap: {
		type: Boolean,
		default: false,
	},

	wrapReverse: {
		type: Boolean,
		default: false,
	},

}

export const style = function () {

	let style = {}

	// 默认值：	nowrap
	// style.wrap = this.wrap || 'nowrap'
//			if (this.noWrap) {
//				style.flexWrap = 'nowrap'
//			}
	if (typeof this.wrap === 'boolean' && this.wrap === true) {
		style.flexWrap = 'wrap'
	}
	if (typeof this.wrap === 'string') {
		style.flexWrap = this.wrap
	}
	if (this.wrapReverse) {
		style.flexWrap = 'wrap-reverse'
	}

	return style
}

export default {
	props,
	style,
}
