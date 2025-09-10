/*!
 * family
 * (c) 2024 lincong1987
 */

export const props = {

	//  字体
	family: {
		type: String,
		default: null,
	},

	familyYahei: {
		type: Boolean,
		default: false,
	},

	familySans: {
		type: Boolean,
		default: false,
	},

	familySong: {
		type: Boolean,
		default: false,
	},

	familySerif: {
		type: Boolean,
		default: false,
	},

	familyMono: {
		type: Boolean,
		default: false,
	},

	familyCode: {
		type: Boolean,
		default: false,
	},

	familyEmoji: {
		type: Boolean,
		default: false,
	},

	familyFallback: {
		type: Boolean,
		default: false,
	},

	familyDingtalkJinbuti: {
		type: Boolean,
		default: false,
	},
	familyJinbuti: {
		type: Boolean,
		default: false,
	},
	familyFzzchjw: {
		type: Boolean,
		default: false,
	},
	familySansationLight: {
		type: Boolean,
		default: false,
	},
	familyYoushebiaotihei: {
		type: Boolean,
		default: false,
	},
}

export const style = function () {
	let style = {}

	this.familyYahei && (style.fontFamily = 'Microsoft YaHei')
	this.familySans && (style.fontFamily = 'sans-serif')
	this.familySong && (style.fontFamily = 'SimSun')
	this.familySerif && (style.fontFamily = 'serif')
	this.familyMono && (style.fontFamily = 'monospace')
	this.familyCode && (style.fontFamily = 'monospace')
	this.familyEmoji && (style.fontFamily = 'EmojiSymbols')
	this.familyFallback && (style.fontFamily = 'sans-serif')
	this.familyDingtalkJinbuti && (style.fontFamily = 'DingTalk-JinBuTi')
	this.familyJinbuti && (style.fontFamily = 'DingTalk-JinBuTi')
	this.familyFzzchjw && (style.fontFamily = 'FZZCHJW')
	this.familySansationLight && (style.fontFamily = 'Sansation_Light')
	this.familyYoushebiaotihei && (style.fontFamily = 'YouSheBiaoTiHei')

	this.family && (style.fontFamily = this.family)

	return style
}

export default {
	props,
	style,
}
