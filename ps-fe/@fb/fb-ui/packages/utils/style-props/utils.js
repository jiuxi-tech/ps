/*!
 * utils
 * (c) 2024 lincong1987
 */

export function unit (val) {
	return typeof val === 'number' ? `${val}px` : val
}

/*
export function applyStyle (styles) {
	try {
		return styles.reduce((acc, curr) => ({ ...acc, ...curr.style.apply(this) }), {});
	} catch (error) {
		console.error('Error applying styles:', error);
		return {};
	}
}
*/

export default {
	unit,
}
