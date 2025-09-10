/*!
 * filter
 * (c) 2024 lincong1987
 */

export const props = {

	noBlur: {
		type: Boolean,
		default: false,
	},
	blur: {
		type: [String, Number],
		default: null,
	},
	blurXs: {
		type: Boolean,
		default: false,
	},
	blurS: {
		type: Boolean,
		default: false,
	},
	blurM: {
		type: Boolean,
		default: false,
	},
	blurL: {
		type: Boolean,
		default: false,
	},
	blurXl: {
		type: Boolean,
		default: false,
	},
	blurXxl: {
		type: Boolean,
		default: false,
	},
	blurXxxl: {
		type: Boolean,
		default: false,
	},
	blurXxxxl: {
		type: Boolean,
		default: false,
	},

	dropShadow: {
		type: [Boolean, String],
		default: undefined,
	},
	noDropShadow: {
		type: Boolean,
		default: false,
	},
	dropShadowXs: {
		type: Boolean,
		default: false,
	},
	dropShadowS: {
		type: Boolean,
		default: false,
	},
	dropShadowM: {
		type: Boolean,
		default: false,
	},
	dropShadowL: {
		type: Boolean,
		default: false,
	},
	dropShadowXl: {
		type: Boolean,
		default: false,
	},
	dropShadowXxl: {
		type: Boolean,
		default: false,
	},

}
export const blurMap = {
	'blurXs': '4px',
	'blurS': '8px',
	'blurM': '12px',
	'blurL': '16px',
	'blurXl': '24px',
	'blurXxl': '32px',
	'blurXxxl': '48px',
	'blurXxxxl': '64px',
}
export const dropShadowMap = {
	'dropShadowXs': 'drop-shadow(0 1px 1px rgb(0 0 0 / 0.05))',
	'dropShadowS': 'drop-shadow(0 1px 2px rgb(0 0 0 / 0.1)) drop-shadow(0 1px 1px rgb(0 0 0 / 0.06))',
	'dropShadowM': 'drop-shadow(0 4px 3px rgb(0 0 0 / 0.07)) drop-shadow(0 2px 2px rgb(0 0 0 / 0.06))',
	'dropShadowL': 'drop-shadow(0 10px 8px rgb(0 0 0 / 0.04)) drop-shadow(0 4px 3px rgb(0 0 0 / 0.1))',
	'dropShadowXl': 'drop-shadow(0 20px 13px rgb(0 0 0 / 0.03)) drop-shadow(0 8px 5px rgb(0 0 0 / 0.08))',
	'dropShadowXxl': 'filter: drop-shadow(0 25px 25px rgb(0 0 0 / 0.15))',

}

export const style = function () {

	return {}


	let style = {
		filter: 'var(--jpx-filter)',
		'--jpx-filter': 'var(--jpx-blur) var(--jpx-drop-shadow)',
		'--jpx-drop-shadow': '',
		'--jpx-blur': '',
	}

	this.blur && (style['--jpx-blur'] = this.blur)
	this.noBlur && (style['--jpx-blur'] = '')
	this.blurXs && (style['--jpx-blur'] = blurMap.blurXs)
	this.blurS && (style['--jpx-blur'] = blurMap.blurS)
	this.blurM && (style['--jpx-blur'] = blurMap.blurM)
	this.blurL && (style['--jpx-blur'] = blurMap.blurL)
	this.blurXl && (style['--jpx-blur'] = blurMap.blurXl)
	this.blurXxl && (style['--jpx-blur'] = blurMap.blurXxl)
	this.blurXxxl && (style['--jpx-blur'] = blurMap.blurXxxl)
	this.blurXxxxl && (style['--jpx-blur'] = blurMap.blurXxxxl)

	this.dropShadow && (style['--jpx-drop-shadow'] = this.dropShadow)
	this.noDropShadow && (style['--jpx-drop-shadow'] = '')
	this.dropShadowXs &&
	(style['--jpx-drop-shadow'] = dropShadowMap.dropShadowXs)
	this.dropShadowS && (style['--jpx-drop-shadow'] = dropShadowMap.dropShadowS)
	this.dropShadowM && (style['--jpx-drop-shadow'] = dropShadowMap.dropShadowM)
	this.dropShadowL && (style['--jpx-drop-shadow'] = dropShadowMap.dropShadowL)
	this.dropShadowXl &&
	(style['--jpx-drop-shadow'] = dropShadowMap.dropShadowXl)
	this.dropShadowXxl &&
	(style['--jpx-drop-shadow'] = dropShadowMap.dropShadowXxl)

	return style
}

export default {
	props,
	style,
	dropShadowMap,
	blurMap,

}



