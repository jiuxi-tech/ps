/*!
 * color 色板
 * (c) 2024 lincong1987
 */

//import { colors } from '../../theme/pc3/src/common/colors.js'
//import { upperFirst, camelCase } from 'lodash-es'
//
//let colorMap = {}
//let str = ''
//Object.keys(colors).forEach((color) => {
//	let prop = `color${upperFirst(camelCase(color))}`
//	colorMap[prop] = colors[color]
//})

export const props = {

	// 颜色
	color: {
		type: [String],
		default: undefined,
	},
//
//	colorRed1: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorRed2: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorRed3: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorRed4: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorRed5: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorRed6: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorRed7: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorRed8: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorRed9: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorRed10: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTomato1: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTomato2: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTomato3: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTomato4: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTomato5: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTomato6: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTomato7: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTomato8: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTomato9: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTomato10: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorCarrot1: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorCarrot2: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorCarrot3: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorCarrot4: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorCarrot5: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorCarrot6: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorCarrot7: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorCarrot8: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorCarrot9: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorCarrot10: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorOrange1: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorOrange2: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorOrange3: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorOrange4: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorOrange5: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorOrange6: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorOrange7: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorOrange8: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorOrange9: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorOrange10: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorYellow1: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorYellow2: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorYellow3: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorYellow4: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorYellow5: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorYellow6: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorYellow7: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorYellow8: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorYellow9: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorYellow10: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorLime1: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorLime2: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorLime3: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorLime4: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorLime5: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorLime6: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorLime7: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorLime8: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorLime9: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorLime10: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorGreen1: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorGreen2: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorGreen3: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorGreen4: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorGreen5: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorGreen6: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorGreen7: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorGreen8: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorGreen9: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorGreen10: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorCyan1: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorCyan2: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorCyan3: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorCyan4: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorCyan5: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorCyan6: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorCyan7: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorCyan8: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorCyan9: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorCyan10: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorBlue1: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorBlue2: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorBlue3: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorBlue4: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorBlue5: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorBlue6: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorBlue7: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorBlue8: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorBlue9: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorBlue10: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorUltramarine1: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorUltramarine2: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorUltramarine3: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorUltramarine4: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorUltramarine5: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorUltramarine6: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorUltramarine7: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorUltramarine8: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorUltramarine9: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorUltramarine10: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorPurple1: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorPurple2: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorPurple3: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorPurple4: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorPurple5: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorPurple6: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorPurple7: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorPurple8: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorPurple9: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorPurple10: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorMagenta1: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorMagenta2: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorMagenta3: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorMagenta4: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorMagenta5: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorMagenta6: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorMagenta7: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorMagenta8: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorMagenta9: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorMagenta10: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorGrey1: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorGrey2: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorGrey3: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorGrey4: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorGrey5: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorGrey6: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorGrey7: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorGrey8: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorGrey9: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorRed: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTomato: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorCarrot: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorOrange: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorYellow: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorLime: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorGreen: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorCyan: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorBlue: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorUltramarine: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorMagenta: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorGrey: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorBrand: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorPrimary: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorWhite: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorBlack: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTransparent: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorSuccess: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorWarning: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorDanger: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorInfo: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTextTitle: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTextEmphasize: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTextBody: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTextSecondary: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTextDisabled: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTextLink: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTextSuccess: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTextWarning: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTextDanger: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTextRegular: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTextPlaceholder: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTextDarkTitle: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTextDarkEmphasize: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTextDarkBody: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTextDarkSecondary: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTextDarkDisabled: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTextDarkLink: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTextDarkSuccess: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTextDarkWarning: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTextDarkDanger: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTextDarkRegular: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorTextDarkPlaceholder: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorBackgroundBase: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorBackgroundColorBase: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorLinkColor: {
//		type: [Boolean, String],
//		default: undefined,
//	},
//
//	colorLinkHoverColor: {
//		type: [Boolean, String],
//		default: undefined,
//	},
}

export const style = function () {

	let style = {}

//	Object.keys(colorMap).forEach((color) => {
//		if (this[color] === true) {
//			style.color = colorMap[color]
//		}
//		if (typeof this[color] === 'string') {
//			style.color = this[color]
//		}
//	})


	this.color && (style.color = this.color)

	return style
}

export default {
	props,
	style,
}
