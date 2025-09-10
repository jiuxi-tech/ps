/*!
 * gridTemplateRows
 * (c) 2024 lincong1987
 */

export const props = {

	gridTemplate: {
		type: String,
		default: undefined,
	},

	gridRows: {
		type: String,
		default: undefined,
	},

	rows1: {
		type: Boolean,
		default: false,
	},
	rows2: {
		type: Boolean,
		default: false,
	},
	rows3: {
		type: Boolean,
		default: false,
	},
	rows4: {
		type: Boolean,
		default: false,
	},
	rows5: {
		type: Boolean,
		default: false,
	},
	rows6: {
		type: Boolean,
		default: false,
	},
	rows7: {
		type: Boolean,
		default: false,
	},
	rows8: {
		type: Boolean,
		default: false,
	},
	rows9: {
		type: Boolean,
		default: false,
	},
	rows10: {
		type: Boolean,
		default: false,
	},
	rows11: {
		type: Boolean,
		default: false,
	},
	rows12: {
		type: Boolean,
		default: false,
	},

	noRows: {
		type: Boolean,
		default: false,
	},

	rowsSubgrid: {
		type: Boolean,
		default: false,
	},

	gridCols: {
		type: String,
		default: undefined,
	},

	cols1: {
		type: Boolean,
		default: false,
	},
	cols2: {
		type: Boolean,
		default: false,
	},
	cols3: {
		type: Boolean,
		default: false,
	},
	cols4: {
		type: Boolean,
		default: false,
	},
	cols5: {
		type: Boolean,
		default: false,
	},
	cols6: {
		type: Boolean,
		default: false,
	},
	cols7: {
		type: Boolean,
		default: false,
	},
	cols8: {
		type: Boolean,
		default: false,
	},
	cols9: {
		type: Boolean,
		default: false,
	},
	cols10: {
		type: Boolean,
		default: false,
	},
	cols11: {
		type: Boolean,
		default: false,
	},
	cols12: {
		type: Boolean,
		default: false,
	},

	noCols: {
		type: Boolean,
		default: false,
	},

	colsSubgrid: {
		type: Boolean,
		default: false,
	}   ,


	gridTemplateAreas: {
		type: String,
		default: undefined,
	},

}

export const style = function () {

	let style = {}

	//
	if (this.gridCols) {
		style.gridTemplateColumns = this.gridCols
	}
	this.cols1 && (style.gridTemplateColumns = 'repeat(1, minmax(0, 1fr))')
	this.cols2 && (style.gridTemplateColumns = 'repeat(2, minmax(0, 1fr))')
	this.cols3 && (style.gridTemplateColumns = 'repeat(3, minmax(0, 1fr))')
	this.cols4 && (style.gridTemplateColumns = 'repeat(4, minmax(0, 1fr))')
	this.cols5 && (style.gridTemplateColumns = 'repeat(5, minmax(0, 1fr))')
	this.cols6 && (style.gridTemplateColumns = 'repeat(6, minmax(0, 1fr))')
	this.cols7 && (style.gridTemplateColumns = 'repeat(7, minmax(0, 1fr))')
	this.cols8 && (style.gridTemplateColumns = 'repeat(8, minmax(0, 1fr))')
	this.cols9 && (style.gridTemplateColumns = 'repeat(9, minmax(0, 1fr))')
	this.cols10 && (style.gridTemplateColumns = 'repeat(10, minmax(0, 1fr))')
	this.cols11 && (style.gridTemplateColumns = 'repeat(11, minmax(0, 1fr))')
	this.cols12 && (style.gridTemplateColumns = 'repeat(12, minmax(0, 1fr))')
	this.noCols && (style.gridTemplateColumns = 'none')
	this.colsSubgrid && (style.gridTemplateColumns = 'subgrid')

	if (this.gridRows) {
		style.gridTemplateRows = this.gridRows
	}

	this.rows1 && (style.gridTemplateRows = 'repeat(1, minmax(0, 1fr))')
	this.rows2 && (style.gridTemplateRows = 'repeat(2, minmax(0, 1fr))')
	this.rows3 && (style.gridTemplateRows = 'repeat(3, minmax(0, 1fr))')
	this.rows4 && (style.gridTemplateRows = 'repeat(4, minmax(0, 1fr))')
	this.rows5 && (style.gridTemplateRows = 'repeat(5, minmax(0, 1fr))')
	this.rows6 && (style.gridTemplateRows = 'repeat(6, minmax(0, 1fr))')
	this.rows7 && (style.gridTemplateRows = 'repeat(7, minmax(0, 1fr))')
	this.rows8 && (style.gridTemplateRows = 'repeat(8, minmax(0, 1fr))')
	this.rows9 && (style.gridTemplateRows = 'repeat(9, minmax(0, 1fr))')
	this.rows10 && (style.gridTemplateRows = 'repeat(10, minmax(0, 1fr))')
	this.rows11 && (style.gridTemplateRows = 'repeat(11, minmax(0, 1fr))')
	this.rows12 && (style.gridTemplateRows = 'repeat(12, minmax(0, 1fr))')
	this.noRows && (style.gridTemplateRows = 'none')
	this.rowsSubgrid && (style.gridTemplateRows = 'subgrid')


	if (this.gridTemplateAreas) {
		style.gridTemplateAreas = this.gridTemplateAreas
	}

	if (this.gridTemplate) {
		style.gridTemplate = this.gridTemplate
	}

	return style
}

export default {
	props,
	style,
}
