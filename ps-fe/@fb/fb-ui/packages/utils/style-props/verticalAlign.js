/*!
 * verticalAlign
 * (c) 2024 lincong1987
 */

export const props = {
	// 垂直对齐
	va: {
		type: [String],
		default: null,
	}, // 基线对齐
	vaBaseline: {
		type: Boolean,
		default: false,
	}, // 中间对齐
	vaMiddle: {
		type: Boolean,
		default: false,
	}, // 下标对齐
	vaSub: {
		type: Boolean,
		default: false,
	}, // 上标对齐
	vaSuper: {
		type: Boolean,
		default: false,
	}, // 顶部对齐
	vaTop: {
		type: Boolean,
		default: false,
	}, // 底部对齐
	vaBottom: {
		type: Boolean,
		default: false,
	}, // 文本顶部对齐
	vaTextTop: {
		type: Boolean,
		default: false,
	}, // 文本底部对齐
	vaTextBottom: {
		type: Boolean,
		default: false,
	},
}

export const style = function () {

	let style = {}

	// 垂直对齐
	if (this.vaBaseline) {
		style.verticalAlign = 'baseline'
	}
	if (this.vaSub) {
		style.verticalAlign = 'sub'
	}
	if (this.vaSuper) {
		style.verticalAlign = 'super'
	}
	if (this.vaTextTop) {
		style.verticalAlign = 'text-top'
	}
	if (this.vaTextBottom) {
		style.verticalAlign = 'text-bottom'
	}
	if (this.vaMiddle) {
		style.verticalAlign = 'middle'
	}
	if (this.vaTop) {
		style.verticalAlign = 'top'
	}
	if (this.vaBottom) {
		style.verticalAlign = 'bottom'
	}
	if (this.va) {
		style.verticalAlign = this.va
	}

	return style
}

export default {
	props,
	style,
}
