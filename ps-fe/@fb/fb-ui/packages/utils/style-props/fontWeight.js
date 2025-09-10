/*!
 * fontWeight 文本粗细
 * (c) 2024 lincong1987
 */

export const props = {
	// 文本粗体
	bold: {
		type: Boolean,
		default: false,
	},
	// 文本粗细
	weight: {
		type: [String, Number],
		default: null,
	},
	// 文本粗细1 = 100
	weight1: {
		type: Boolean,
		default: false,
	},

	// 文本粗细2 = 200
	weight2: {
		type: Boolean,
		default: false,
	},
	// 文本粗细3 = 300
	weight3: {
		type: Boolean,
		default: false,
	},
	// 文本粗细4 = 400
	weight4: {
		type: Boolean,
		default: false,
	},

	// 文本粗细5 = 500
	weight5: {
		type: Boolean,
		default: false,
	},
	// 文本粗细6 = 600
	weight6: {
		type: Boolean,
		default: false,
	},
	// 文本粗细7 = 700
	weight7: {
		type: Boolean,
		default: false,
	},
	// 文本粗细8 = 800
	weight8: {
		type: Boolean,
		default: false,
	},
	// 文本粗细9 = 900
	weight9: {
		type: Boolean,
		default: false,
	},
	// 文本粗细    lighter
	weightLighter: {
		type: Boolean,
		default: false,
	},
	// 	文本粗细    bolder
	weightBolder: {
		type: Boolean,
		default: false,
	},
	weightNormal: {
		type: Boolean,
		default: false,
	},

}

/**
 * 应用样式
 * @returns {{}}
 */
export const style = function () {

	let style = {}

	let fontWeightMap = {
		weight1: 100,
		weight2: 200,
		weight3: 300,
		weight4: 400,
		weight5: 500,
		weight6: 600,
		weight7: 700,
		weight8: 800,
		weight9: 900,
		weightBold: 'bold',
		weightLighter: 'lighter',
		weightNormal: 'normal',
		weightBolder: 'bolder',
	}

	// 粗体
	this.bold && (style.fontWeight = fontWeightMap.weightBold)
	// 字重
	this.weight1 === true && (style.fontWeight = fontWeightMap.weight1)
	this.weight2 === true && (style.fontWeight = fontWeightMap.weight2)
	this.weight3 === true && (style.fontWeight = fontWeightMap.weight3)
	this.weight4 === true && (style.fontWeight = fontWeightMap.weight4)
	this.weight5 === true && (style.fontWeight = fontWeightMap.weight5)
	this.weight6 === true && (style.fontWeight = fontWeightMap.weight6)
	this.weight7 === true && (style.fontWeight = fontWeightMap.weight7)
	this.weight8 === true && (style.fontWeight = fontWeightMap.weight8)
	this.weight9 === true && (style.fontWeight = fontWeightMap.weight9)
	this.weightBolder === true &&
	(style.fontWeight = fontWeightMap.weightBolder)
	this.weightLighter === true &&
	(style.fontWeight = fontWeightMap.weightLighter)
	this.weightNormal && (style.fontWeight = fontWeightMap.weightNormal)

	if (this.weight) {
		style.fontWeight = this.weight
	}

	return style

}

export default {
	props,
	style,
}
