/*!
 * alignItems
 * (c) 2024 lincong1987
 */


export const props = {


	// alignItems = ['center','flex-start','flex-end','stretch', 'baseline']
	alignItems: {
		type: String,
		default: undefined,
	},
	aiCenter: {
		type: Boolean,
		default: false,
	},
	aiStart: {
		type: Boolean,
		default: false,
	},
	aiEnd: {
		type: Boolean,
		default: false,
	},
	aiSpaceBetween: {
		type: Boolean,
		default: false,
	},
	aiSpaceAround: {
		type: Boolean,
		default: false,
	},
	aiBaseline: {
		type: Boolean,
		default: false,
	},
}

export const style = function () {

	let style = {}

	if (this.alignItems) {
		style.alignItems = this.alignItems
	}
	if (this.aiCenter) {
		style.alignItems = 'center'
	}
	if (this.aiStart) {
		style.alignItems = 'flex-start'
	}
	if (this.aiEnd) {
		style.alignItems = 'flex-end'
	}
	if (this.aiFlexStart) {
		style.alignItems = 'space-around'
	}
	if (this.aiSpaceBetween) {
		style.alignItems = 'space-between'
	}
	if (this.aiBaseline) {
		style.alignItems = 'baseline'
	}


	return style
}

export default {
	props,
	style
}
