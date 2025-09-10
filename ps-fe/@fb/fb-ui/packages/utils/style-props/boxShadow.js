/*!
 * boxShadow
 * (c) 2024 lincong1987
 */

export const props = {

	// 阴影
	shadow: {
		type: [Boolean, String],
		default: undefined,
	},
	noShadow: {
		type: Boolean,
		default: false,
	},
	shadowXs: {
		type: Boolean,
		default: false,
	},
	shadowS: {
		type: Boolean,
		default: false,
	},
	shadowM: {
		type: Boolean,
		default: false,
	},
	shadowL: {
		type: Boolean,
		default: false,
	},
	shadowXl: {
		type: Boolean,
		default: false,
	},
	shadowXxl: {
		type: Boolean,
		default: false,
	},
	shadowInner: {
		type: Boolean,
		default: false,
	},

	boxShadow: {
		type: String,
		default: undefined,
	},
}

export const boxShadowMap = {
	shadow: '0 1px 3px 0 rgb(0 0 0 / 0.1), 0 1px 2px -1px rgb(0 0 0 / 0.1)',
	shadowXs: '0 1px 2px 0 rgb(0 0 0 / 0.05)',
	shadowS: '0 1px 2px 0 rgb(0 0 0 / 0.05)',
	shadowM: '0 4px 6px -1px rgb(0 0 0 / 0.1), 0 2px 4px -2px rgb(0 0 0 / 0.1)',
	shadowL: '0 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1)',
	shadowXl: '0 20px 25px -5px rgb(0 0 0 / 0.1), 0 8px 10px -6px rgb(0 0 0 / 0.1)',
	shadowXxl: '0 25px 50px -12px rgb(0 0 0 / 0.25)',
	shadowInner: 'inset 0 2px 4px 0 rgb(0 0 0 / 0.05)',
	noShadow: 'none',
}

export const style = function () {

	let style = {}

	// boxShadow 阴影

	if (this.shadow) {
		if (typeof this.shadow === 'boolean') {
			style.boxShadow = boxShadowMap.shadow
		}
		if (typeof this.shadow === 'string') {
			style.boxShadow = this.shadow
		}
	}
	this.shadowXs && (style.boxShadow = boxShadowMap.shadowXs)
	this.shadowS && (style.boxShadow = boxShadowMap.shadowS)
	this.shadowM && (style.boxShadow = boxShadowMap.shadowM)
	this.shadowL && (style.boxShadow = boxShadowMap.shadowL)
	this.shadowXl && (style.boxShadow = boxShadowMap.shadowXl)
	this.shadowXxl && (style.boxShadow = boxShadowMap.shadowXxl)
	this.shadowInner && (style.boxShadow = boxShadowMap.shadowInner)
	this.noShadow && (style.boxShadow = boxShadowMap.noShadow)

	this.boxShadow && (style.boxShadow = this.boxShadow)

	return style
}

export default {
	props,
	style,
	boxShadowMap
}
