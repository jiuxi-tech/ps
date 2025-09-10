/*!
 * componentUtils
 * (c) 2020 lincong1987
 */

export function typeOf (obj) {
	const toString = Object.prototype.toString
	const map = {
		'[object Boolean]': 'boolean',
		'[object Number]': 'number',
		'[object String]': 'string',
		'[object Function]': 'function',
		'[object Array]': 'array',
		'[object Date]': 'date',
		'[object RegExp]': 'regExp',
		'[object Undefined]': 'undefined',
		'[object Null]': 'null',
		'[object Object]': 'object',
	}
	return map[toString.call(obj)]
}

/**
 * 向上查找组件
 * @param context
 * @param componentName
 * @returns {null|parent}
 */
export function closest (context, componentName, level = 0) {
	let componentNames
	if (typeOf(componentName) === 'string') {
		componentNames = [componentName]
	} else if (typeOf(componentName) === 'array') {
		componentNames = componentName
	} else {
		return null
	}

	let parent = context.$parent
	let name = parent.$options.name

	if (level === 1 &&
		(name === componentNames || (componentNames.indexOf(name) != -1))) {
		return parent
	}

	while (parent && (!name || componentNames.indexOf(name) < 0)) {
		parent = parent.$parent
		if (parent) {
			name = parent.$options.name
		}
	}

	return parent
}

/**
 * 向下查找组件
 * @param context
 * @param componentName
 * @returns {null}
 */
export function find (context, componentName) {
	let componentNames
	if (typeOf(componentName) === 'string') {
		componentNames = [componentName]
	} else if (typeOf(componentName) === 'array') {
		componentNames = componentName
	} else {
		return null
	}

	const childrens = context.$children
	let children = null

	if (childrens.length) {
		childrens.forEach((child) => {
			const name = child.$options.name
			if (componentNames.indexOf(name) != -1) {
				children = child
			}
		})

		for (let i = 0; i < childrens.length; i++) {
			const child = childrens[i]
			const name = child.$options.name
			if (componentNames.indexOf(name) != -1) {
				children = child
				break
			} else {
				children = find(child, componentNames)
				if (children) break
			}
		}
	}
	return children
}

/**
 * 查找直接上级
 * @param context
 * @param componentName
 * @returns {null|Vue}
 */
export function parent (context, componentName) {
	if (typeOf(componentName) !== 'string') {
		return null
	}

	let parent = context.$parent
	if (componentName === parent.$options.name) {
		return parent
	}
}

export function isEmptyElement (node) {
	return !(node.tag || (node.text && node.text.trim() !== ''))
}

export function filterEmpty (children = []) {
	return children.filter(child => !isEmptyElement(child))
}

export default {
	filterEmpty,
	parent,
	closest,
	find,
	typeOf,
	isEmptyElement,
}
