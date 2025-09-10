/*!
* component
* (c) 2020 lincong1987
*/



import { typeOf } from './type'

/**
 * 向上查找组件
 * @param context
 * @param componentName
 * @returns {null|Vue}
 */
export function closest (context, componentName) {
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
	while (parent && (!name || componentNames.indexOf(name) < 0)) {
		parent = parent.$parent
		if (parent) name = parent.$options.name
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
