/*!
 * textDecoration
 * (c) 2024 lincong1987
 */

export const props = {
	// 没有 文本装饰
	noDecoration: {
		type: Boolean,
		default: false,
	},
	// 下划线
	underline: {
		type: Boolean,
		default: false,
	},
	// 上划线
	overline: {
		type: Boolean,
		default: false,
	},

	// 删除线
	lineThrough: {
		type: Boolean,
		default: false,
	},

	// 文本装饰线 Solid
	decorationSolid: {
		type: Boolean,
		default: false,
	},
	// 双线
	decorationDouble: {
		type: Boolean,
		default: false,
	},
	// 点状
	decorationDotted: {
		type: Boolean,
		default: false,
	},
	// 虚线
	decorationDashed: {
		type: Boolean,
		default: false,
	},
	// 波浪线
	decorationWavy: {
		type: Boolean,
		default: false,
	},
	// 颜色
	decorationColor: {
		type: String,
		default: null,
	},
	// 线条宽度
	decorationWidth: {
		type: [String, Number],
		default: null,
	},
	// 线条宽度 0
	decorationWidth0: {
		type: Boolean,
		default: false,
	},
	// 线条宽度 1
	decorationWidth1: {
		type: Boolean,
		default: false,
	},
	// 线条宽度 2
	decorationWidth2: {
		type: Boolean,
		default: false,
	},
	// 线条宽度 3
	decorationWidth3: {
		type: Boolean,
		default: false,
	},
	// 线条宽度 4
	decorationWidth4: {
		type: Boolean,
		default: false,
	},
	// 线条宽度 5
	decorationWidth5: {
		type: Boolean,
		default: false,
	},
	// 线条宽度 6
	decorationWidth6: {
		type: Boolean,
		default: false,
	},
	// 线条宽度 8
	decorationWidth8: {
		type: Boolean,
		default: false,
	},
	// 线条偏移
	decorationOffset: {
		type: [String, Number],
		default: null,
	},
	// 线条偏移 0
	decorationOffset0: {
		type: Boolean,
		default: false,
	},
	// 线条偏移 1
	decorationOffset1: {
		type: Boolean,
		default: false,
	},
	// 线条偏移 2
	decorationOffset2: {
		type: Boolean,
		default: false,
	},
	// 线条偏移 3
	decorationOffset3: {
		type: Boolean,
		default: false,
	},
	// 线条偏移 4
	decorationOffset4: {
		type: Boolean,
		default: false,
	},
	// 线条偏移 5
	decorationOffset5: {
		type: Boolean,
		default: false,
	},
	// 线条偏移 6
	decorationOffset6: {
		type: Boolean,
		default: false,
	},
	// 线条偏移 8
	decorationOffset8: {
		type: Boolean,
		default: false,
	},

}

export const textDecorationMap = {
	// 下划线
	underline: 'underline',
	// 上划线
	overline: 'overline',
	// 删除线
	lineThrough: 'line-through',
	// 文本装饰线 Solid
	decorationSolid: 'solid',
	// 双线
	decorationDouble: 'double',
	// 点状
	decorationDotted: 'dotted',
	// 虚线
	decorationDashed: 'dashed',
	// 波浪线
	decorationWavy: 'wavy',
	none: 'none',
}

export const style = function () {

	let style = {}
	// 下划线、删除线
	this.underline && (style.textDecorationLine = textDecorationMap.underline)
	this.lineThrough &&
	(style.textDecorationLine = textDecorationMap.lineThrough)
	this.overline && (style.textDecorationLine = textDecorationMap.overline)

	this.decorationSolid &&
	(style.textDecorationStyle = textDecorationMap.decorationSolid)
	this.decorationDouble &&
	(style.textDecorationStyle = textDecorationMap.decorationDouble)
	this.decorationDotted &&
	(style.textDecorationStyle = textDecorationMap.decorationDotted)
	this.decorationDashed &&
	(style.textDecorationStyle = textDecorationMap.decorationDashed)
	this.decorationWavy &&
	(style.textDecorationStyle = textDecorationMap.decorationWavy)

	this.decorationColor && (style.textDecorationColor = this.decorationColor)
	this.decorationWidth &&
	(style.textDecorationThickness = this.decorationWidth)
	this.decorationWidth0 && (style.textDecorationThickness = '0px')
	this.decorationWidth1 && (style.textDecorationThickness = '1px')
	this.decorationWidth2 && (style.textDecorationThickness = '2px')
	this.decorationWidth3 && (style.textDecorationThickness = '3px')
	this.decorationWidth4 && (style.textDecorationThickness = '4px')
	this.decorationWidth5 && (style.textDecorationThickness = '5px')
	this.decorationWidth6 && (style.textDecorationThickness = '6px')
	this.decorationWidth8 && (style.textDecorationThickness = '8px')

	this.decorationOffset &&
	(style.textUnderlineOffset = this.decorationOffset)
	this.decorationOffset0 && (style.textUnderlineOffset = '0px')
	this.decorationOffset1 && (style.textUnderlineOffset = '1px')
	this.decorationOffset2 && (style.textUnderlineOffset = '2px')
	this.decorationOffset3 && (style.textUnderlineOffset = '3px')
	this.decorationOffset4 && (style.textUnderlineOffset = '4px')
	this.decorationOffset5 && (style.textUnderlineOffset = '5px')
	this.decorationOffset6 && (style.textUnderlineOffset = '6px')
	this.decorationOffset8 && (style.textUnderlineOffset = '8px')

	this.noDecoration && (style.textDecoration = textDecorationMap.none)

	return style
}

export default {
	props,
	style,
}
