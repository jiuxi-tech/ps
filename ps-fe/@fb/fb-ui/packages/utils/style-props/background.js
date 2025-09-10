/*!
 * background
 * (c) 2024 lincong1987
 */

//import { colors } from '../../theme/pc3/src/common/colors.js'
//import { upperFirst, camelCase } from 'lodash-es'
//
//let colorMap = {}
//Object.keys(colors).forEach((color) => {
//	let prop = `bg${upperFirst(camelCase(color))}`
//	colorMap[prop] = colors[color]
//})

export const props = {
	// 背景
	background: {
		type: String,
		default: undefined,
	},

	// 背景 缩写
	bg: {
		type: String,
		default: undefined,
	},

//
//	bgRed1: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgRed2: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgRed3: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgRed4: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgRed5: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgRed6: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgRed7: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgRed8: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgRed9: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgRed10: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTomato1: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTomato2: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTomato3: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTomato4: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTomato5: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTomato6: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTomato7: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTomato8: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTomato9: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTomato10: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgCarrot1: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgCarrot2: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgCarrot3: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgCarrot4: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgCarrot5: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgCarrot6: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgCarrot7: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgCarrot8: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgCarrot9: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgCarrot10: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgOrange1: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgOrange2: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgOrange3: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgOrange4: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgOrange5: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgOrange6: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgOrange7: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgOrange8: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgOrange9: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgOrange10: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgYellow1: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgYellow2: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgYellow3: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgYellow4: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgYellow5: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgYellow6: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgYellow7: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgYellow8: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgYellow9: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgYellow10: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgLime1: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgLime2: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgLime3: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgLime4: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgLime5: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgLime6: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgLime7: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgLime8: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgLime9: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgLime10: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgGreen1: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgGreen2: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgGreen3: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgGreen4: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgGreen5: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgGreen6: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgGreen7: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgGreen8: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgGreen9: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgGreen10: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgCyan1: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgCyan2: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgCyan3: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgCyan4: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgCyan5: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgCyan6: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgCyan7: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgCyan8: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgCyan9: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgCyan10: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgBlue1: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgBlue2: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgBlue3: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgBlue4: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgBlue5: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgBlue6: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgBlue7: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgBlue8: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgBlue9: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgBlue10: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgUltramarine1: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgUltramarine2: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgUltramarine3: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgUltramarine4: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgUltramarine5: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgUltramarine6: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgUltramarine7: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgUltramarine8: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgUltramarine9: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgUltramarine10: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgPurple1: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgPurple2: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgPurple3: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgPurple4: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgPurple5: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgPurple6: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgPurple7: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgPurple8: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgPurple9: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgPurple10: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgMagenta1: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgMagenta2: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgMagenta3: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgMagenta4: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgMagenta5: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgMagenta6: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgMagenta7: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgMagenta8: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgMagenta9: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgMagenta10: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgGrey1: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgGrey2: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgGrey3: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgGrey4: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgGrey5: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgGrey6: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgGrey7: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgGrey8: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgGrey9: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgRed: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTomato: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgCarrot: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgOrange: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgYellow: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgLime: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgGreen: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgCyan: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgBlue: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgUltramarine: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgMagenta: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgGrey: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgBrand: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgPrimary: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgWhite: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgBlack: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTransparent: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgSuccess: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgWarning: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgDanger: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgInfo: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTextTitle: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTextEmphasize: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTextBody: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTextSecondary: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTextDisabled: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTextLink: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTextSuccess: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTextWarning: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTextDanger: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTextRegular: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTextPlaceholder: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTextDarkTitle: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTextDarkEmphasize: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTextDarkBody: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTextDarkSecondary: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTextDarkDisabled: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTextDarkLink: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTextDarkSuccess: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTextDarkWarning: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTextDarkDanger: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTextDarkRegular: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgTextDarkPlaceholder: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgBackgroundBase: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgBackgroundColorBase: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgLinkColor: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	bgLinkHoverColor: {
//		type: [Boolean, String],
//		default: undefined,
//	},

}

export const style = function () {

	let style = {}

//	Object.keys(colorMap).forEach((color) => {
//		if (this[color] === true) {
//			style.background = colorMap[color]
//		}
//		if (typeof this[color] === 'string') {
//			style.background = this[color]
//		}
//	})


	this.background && (style.background = this.background)
	this.bg && (style.background = this.bg)

	return style
}

export default {
	props,
	style,
}

