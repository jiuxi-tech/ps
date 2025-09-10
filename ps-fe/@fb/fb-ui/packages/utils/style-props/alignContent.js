/*!
 * alignContent
 * (c) 2024 lincong1987
 * 
 * 提供 align-content 相关的 Vue 组件属性和样式计算功能
 * 用于设置 Flexbox 容器中行的对齐方式
 */

/**
 * alignContent 组件属性定义
 * @property {string} [alignContent] - 设置 align-content 样式属性，可选值: 'center' | 'flex-start' | 'flex-end' | 'space-around' | 'space-between' | 'stretch'
 * @property {boolean} [acCenter=false] - 设置 align-content 为 'center'
 * @property {boolean} [acSpaceBetween=false] - 设置 align-content 为 'space-between'
 * @property {boolean} [acSpaceAround=false] - 设置 align-content 为 'space-around'
 * @property {boolean} [acSpaceEvenly=false] - 设置 align-content 为 'space-evenly'
 * @property {boolean} [acStart=false] - 设置 align-content 为 'flex-start'
 * @property {boolean} [acEnd=false] - 设置 align-content 为 'flex-end'
 */
export const props = {

	// alignContent = ['center','flex-start','flex-end','space-around','space-between','stretch']
	alignContent: {
		type: String,
		default: undefined,
	},
	acCenter: {
		type: Boolean,
		default: false,
	},
	acSpaceBetween: {
		type: Boolean,
		default: false,
	},
	acSpaceAround: {
		type: Boolean,
		default: false,
	},
	acSpaceEvenly: {
		type: Boolean,
		default: false,
	},
	acStart: {
		type: Boolean,
		default: false,
	},
	acEnd: {
		type: Boolean,
		default: false,
	},
}

/**
 * alignContent 样式计算函数
 * 根据组件属性计算对应的 CSS align-content 样式
 * @returns {Object} 包含 alignContent 样式属性的对象
 */
export const style = function () {

	let style = {}

	if (this.alignContent) {

		style.alignContent = this.alignContent
	}
	if (this.acCenter) {
		style.alignContent = 'center'
	}
	if (this.acStart) {
		style.alignContent = 'flex-start'
	}
	if (this.acEnd) {
		style.alignContent = 'flex-end'
	}
	if (this.acSpaceAround) {
		style.alignContent = 'space-around'
	}
	if (this.acSpaceBetween) {
		style.alignContent = 'space-between'
	}
	if (this.acSpaceEvenly) {
		style.alignContent = 'space-evenly'
	}

	return style
}

/**
 * alignContent 模块默认导出
 * @property {Object} props - 组件属性定义
 * @property {Function} style - 样式计算函数
 */
export default {
	props,
	style
}