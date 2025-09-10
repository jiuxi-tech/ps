/*!
 * utils
 * (c) 2021 lincong1987
 */

import { isArray, isObject, isString } from '../../../utils/utils'

/**
 *
 * @param columns
 * @param currentRow
 * @param rows
 * @param isLast
 * @returns {*[]}
 */
export const renderHeaderRows = function ({
	                                          columns = [],
	                                          currentRow = 0,
	                                          rows = [],
	                                          isLast = true,
                                          }) {
	rows = rows || []
	rows[currentRow] = rows[currentRow] || []

	columns.forEach((column, i) => {

		if (column.rowSpan && rows.length < column.rowSpan) {
			while (rows.length < column.rowSpan) {
				rows.push([])
			}
		}

		const cellIsLast = isLast && i === columns.length - 1
//				const cell = {
//					label: column.label,
//					className: column.className || column.class || '',
//					children: column.label,
//					isLast: cellIsLast,
//					column,
//				};

		column.isLast = cellIsLast

		if (!column.type) {
			column.type = 'normal'
			// if(!column.width) {}
		}

		if (['rownum', 'subrow', 'multiple', 'radio'].indexOf(column.type) !==
			-1) {
			column.freeze = column.freeze || 'left'
			column.width = column.width || 40
		}

		if (column.children) {
			renderHeaderRows({
				columns: column.children,
				currentRow: currentRow + 1,
				rows,
				isLast: cellIsLast,
			})
		}
//		if ('colSpan' in column) {
//			column.colSpan = column.colSpan
//		}
//		if ('rowSpan' in column) {
//			column.rowSpan = column.rowSpan
//		}
		if (column.colSpan !== 0) {
			rows[currentRow].push(column)
		}
	})
	return rows.filter(row => row.length > 0)
}

export const classNames = function () {
	let classes = []
	for (let i = 0; i < arguments.length; i++) {
		const value = arguments[i]
		if (!value) continue
		if (isString(value)) {
			classes.push(value)
		} else if (isArray(value)) {
			for (let i = 0; i < value.length; i++) {
				const inner = classNames(value[i])
				if (inner) {
					classes.push(inner)
				}
			}
		} else if (isObject(value)) {
			for (const name in value) {
				if (value[name]) {
					classes.push(name)
				}
			}
		}
	}
	return classes.join(' ')
}

export const indexOf = function (arr, obj) {
	if (arr.indexOf) return arr.indexOf(obj)
	for (let i = 0; i < arr.length; ++i) {
		if (arr[i] === obj) return i
	}
	return -1
}

let scrollbarVerticalSize
let scrollbarHorizontalSize

// Measure scrollbar width for padding body during modal show/hide
const scrollbarMeasure = {
	position: 'absolute',
	top: '-9999px',
	width: '50px',
	height: '50px',
}

export const INTERNAL_COL_DEFINE = 'RC_TABLE_INTERNAL_COL_DEFINE'

export function measureScrollbar ({
	                                  direction = 'vertical',
	                                  prefix = '',
                                  }) {
	if (typeof document === 'undefined' || typeof window === 'undefined') {
		return 0
	}
	const isVertical = direction === 'vertical'
	if (isVertical && scrollbarVerticalSize) {
		return scrollbarVerticalSize
	}
	if (!isVertical && scrollbarHorizontalSize) {
		return scrollbarHorizontalSize
	}
	const scrollDiv = document.createElement('div')
	Object.keys(scrollbarMeasure).forEach(scrollProp => {
		scrollDiv.style[scrollProp] = scrollbarMeasure[scrollProp]
	})
	// apply hide scrollbar className ahead
	scrollDiv.className = `${prefix}-hide-scrollbar scroll-div-append-to-body`

	// Append related overflow style
	if (isVertical) {
		scrollDiv.style.overflowY = 'scroll'
	} else {
		scrollDiv.style.overflowX = 'scroll'
	}
	document.body.appendChild(scrollDiv)
	let size = 0
	if (isVertical) {
		size = scrollDiv.offsetWidth - scrollDiv.clientWidth
		scrollbarVerticalSize = size
	} else {
		size = scrollDiv.offsetHeight - scrollDiv.clientHeight
		scrollbarHorizontalSize = size
	}

	document.body.removeChild(scrollDiv)
	return size
}

export function debounce (func, wait, immediate) {
	let timeout

	function debounceFunc (...args) {
		const context = this
		// https://fb.me/react-event-pooling
		if (args[0] && args[0].persist) {
			args[0].persist()
		}
		const later = () => {
			timeout = null
			if (!immediate) {
				func.apply(context, args)
			}
		}
		const callNow = immediate && !timeout
		clearTimeout(timeout)
		timeout = setTimeout(later, wait)
		if (callNow) {
			func.apply(context, args)
		}
	}

	debounceFunc.cancel = function cancel () {
		if (timeout) {
			clearTimeout(timeout)
			timeout = null
		}
	}
	return debounceFunc
}

export function remove (array, item) {
	const index = array.indexOf(item)
	const front = array.slice(0, index)
	const last = array.slice(index + 1, array.length)
	return front.concat(last)
}

/**
 * Returns only data- and aria- key/value pairs
 * @param {object} props
 */
export function getDataAndAriaProps (props) {
	return Object.keys(props).reduce((memo, key) => {
		if (key.substr(0, 5) === 'data-' || key.substr(0, 5) === 'aria-') {
			memo[key] = props[key]
		}
		return memo
	}, {})
}

let uidIndex = 0

export function uid (prefix = 'uuid') {
	let now = +new Date()
	return `${prefix}-${now}-${++uidIndex}`
}

export default {
	uid,
	renderHeaderRows,
	classNames,
	indexOf,
	measureScrollbar,
	scrollbarMeasure,
	scrollbarHorizontalSize,
	scrollbarVerticalSize,
	remove,
	getDataAndAriaProps,
	debounce,
}
