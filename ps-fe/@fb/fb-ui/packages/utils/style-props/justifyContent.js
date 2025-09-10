/*!
 * justifyContent
 * (c) 2024 lincong1987
 */


export const props = {


	// justifyContent = ['center','flex-start','flex-end', 'space-around','space-between', 'space-evenly', ]
	justifyContent: {
		type: String,
		default: undefined,
	},
	jcCenter: {
		type: Boolean,
		default: false,
	},
	jcStart: {
		type: Boolean,
		default: false,
	},
	jcEnd: {
		type: Boolean,
		default: false,
	},
	jcSpaceBetween: {
		type: Boolean,
		default: false,
	},
	jcSpaceAround: {
		type: Boolean,
		default: false,
	},
	jcSpaceEvenly: {
		type: Boolean,
		default: false,
	},
}

export const style = function () {

	let style = {}


	if (this.justifyContent) {
		style.justifyContent = this.justifyContent
	}
	if (this.jcCenter) {
		style.justifyContent = 'center'
	}
	if (this.jcStart) {
		style.justifyContent = 'flex-start'
	}
	if (this.jcEnd) {
		style.justifyContent = 'flex-end'
	}
	if (this.jcSpaceAround) {
		style.justifyContent = 'space-around'
	}
	if (this.jcSpaceBetween) {
		style.justifyContent = 'space-between'
	}
	if (this.jcSpaceEvenly) {
		style.justifyContent = 'space-evenly'
	}

	return style
}

export default {
	props,
	style,
}
