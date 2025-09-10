/*!
 * lineHeight
 * (c) 2024 lincong1987
 */


export const props = {

	// 行高
	lh: {
		type: [String, Number],
		default: null,
	},
	lh1: {
		type: Boolean,
		default: false,
	},
	lh2: {
		type: Boolean,
		default: false,
	},
	lh3: {
		type: Boolean,
		default: false,
	},
	lh4: {
		type: Boolean,
		default: false,
	},
	lh5: {
		type: Boolean,
		default: false,
	},
	lh6: {
		type: Boolean,
		default: false,
	},
	lh7: {
		type: Boolean,
		default: false,
	},
	lh8: {
		type: Boolean,
		default: false,
	},
	lh9: {
		type: Boolean,
		default: false,
	},
	lh10: {
		type: Boolean,
		default: false,
	},


}

export const lineHeightMap = {

}
export const style = function () {

	let style = {}


	// 行高配置
	if (this.lh1) {
		style.lineHeight = 0.75
	}
	if (this.lh2) {
		style.lineHeight = 1
	}
	if (this.lh3) {
		style.lineHeight = 1.25
	}
	if (this.lh4) {
		style.lineHeight = 1.5
	}
	if (this.lh5) {
		style.lineHeight = 1.75
	}
	if (this.lh6) {
		style.lineHeight = 2
	}
	if (this.lh7) {
		style.lineHeight = 2.25
	}
	if (this.lh8) {
		style.lineHeight = 2.5
	}
	if (this.lh9) {
		style.lineHeight = 2.75
	}
	if (this.lh10) {
		style.lineHeight = 3
	}
	if (this.lh) {
		style.lineHeight = this.lh
	}

	return style
}

export default {
	props,
	style,
	lineHeightMap
}
