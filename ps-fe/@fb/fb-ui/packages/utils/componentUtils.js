/*!
 * componentUtils - Vue 组件工具函数集
 * (c) 2020 lincong1987
 */

import { typeOf } from './commonUtils'

/**
 * @namespace ComponentUtils
 * @desc Vue 组件工具函数集合
 */

/**
 * @desc 向上查找组件
 * @param {VueComponent} context - 当前组件上下文
 * @param {String|Array} componentName - 要查找的组件名称或名称数组
 * @param {Number} level - 查找层级，默认为 0（查找所有父级）
 * @returns {VueComponent|null} 返回找到的组件实例，未找到则返回 null
 * @example
 * // 查找名为 'FbButton' 的父组件
 * const button = closest(this, 'FbButton');
 * 
 * // 查找名为 'FbButton' 或 'FbLink' 的父组件
 * const component = closest(this, ['FbButton', 'FbLink']);
 * 
 * // 只查找直接父级中名为 'FbButton' 的组件
 * const parentButton = closest(this, 'FbButton', 1);
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
 * @desc 向下查找组件
 * @param {VueComponent} context - 当前组件上下文
 * @param {String|Array} componentName - 要查找的组件名称或名称数组
 * @returns {VueComponent|null} 返回找到的组件实例，未找到则返回 null
 * @example
 * // 查找名为 'FbButton' 的子组件
 * const button = find(this, 'FbButton');
 * 
 * // 查找名为 'FbButton' 或 'FbLink' 的子组件
 * const component = find(this, ['FbButton', 'FbLink']);
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

	if (childrens && childrens.length) {
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
 * @desc 查找直接上级组件
 * @param {VueComponent} context - 当前组件上下文
 * @param {String} componentName - 要查找的组件名称
 * @returns {VueComponent|undefined} 返回找到的组件实例，未找到则返回 undefined
 * @example
 * // 查找直接父级中名为 'FbButton' 的组件
 * const parentButton = parent(this, 'FbButton');
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

/**
 * @desc 判断节点是否为空元素
 * @param {VNode} node - 虚拟节点
 * @returns {Boolean} 如果节点为空元素则返回 true
 * @example
 * // 判断节点是否为空
 * if (isEmptyElement(vnode)) {
 *   console.log('节点为空');
 * }
 */
export function isEmptyElement (node) {
	return !(node.tag || (node.text && node.text.trim() !== ''))
}

/**
 * @desc 过滤空元素
 * @param {Array} children - 子节点数组
 * @returns {Array} 返回过滤后的子节点数组
 * @example
 * // 过滤空元素
 * const filteredChildren = filterEmpty(this.$slots.default);
 */
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