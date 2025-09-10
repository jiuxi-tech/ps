/*!
 * flexDirection
 * (c) 2024 lincong1987
 */


export const props = {


	// ['row','column','row-reverse','column-reverse']
	flexDirection: {
		type: String,
		default: undefined,
	},
	directionColumn: {
		type: Boolean,
		default: false,
	},
	directionColumnReverse: {
		type: Boolean,
		default: false,
	},
	directionRowReverse: {
		type: Boolean,
		default: false,
	},
}

export const style = function () {

	let style = {}

	if (this.flexDirection) {
		style.flexDirection = this.flexDirection
	}
	if (this.directionColumn) {
		style.flexDirection = 'column'
	}
	if (this.directionColumnReverse) {
		style.flexDirection = 'column-reverse'
	}
	if (this.directionRowReverse) {
		style.flexDirection = 'row-reverse'
	}

	return style
}

export default {
	props,
	style
}
