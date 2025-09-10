/*!
 * zIndex
 * (c) 2024 lincong1987
 */

export const props = {

	zIndex: {
		type: [String, Number],
		default: undefined,
	},
	z: {
		type: [String, Number],
		default: undefined,
	},
	z0: {
		type: Boolean,
		default: false,
	},
	z1: {
		type: Boolean,
		default: false,
	},
	z10: {
		type: Boolean,
		default: false,
	},
	z100: {
		type: Boolean,
		default: false,
	},
	z200: {
		type: Boolean,
		default: false,
	},
	z1000: {
		type: Boolean,
		default: false,
	},
	z2000: {
		type: Boolean,
		default: false,
	},
	z10000: {
		type: Boolean,
		default: false,
	},
	z20000: {
		type: Boolean,
		default: false,
	},
	z100000: {
		type: Boolean,
		default: false,
	},
	z1000000: {
		type: Boolean,
		default: false,
	},
	z10000000: {
		type: Boolean,
		default: false,
	},

	noZ: {
		type: Boolean,
		default: false,
	},

}

const zIndexMap = {
	'0': '0',
	'1': '1',
	'10': '10',
	'100': '100',
	'200': '200',
	'1000': '1000',
	'2000': '2000',
	'10000': '10000',
	'20000': '20000',
	'100000': '100000',
	'1000000': '1000000',
	'10000000': '10000000',
	noZ: '-1',
}

export const style = function () {

	let style = {}


	Object.keys(zIndexMap).forEach(key => {
		if (this[`z${key}`]) {
			style.zIndex = zIndexMap[key]
		}
	})

	if (this.zIndex) {
		style.zIndex = this.zIndex
	}
	if (this.z) {
		style.zIndex = this.z
	}

	return style
}

export default {
	props,
	style,
}
