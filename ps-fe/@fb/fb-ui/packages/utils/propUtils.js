/*!
 * propUtils - Vue 组件属性工具函数集
 * (c) 2020 lincong1987
 */

import {
	endsWith,
	includes, isNumber,
	isString,
	isUndefined,
	keysIn,
	parseInt,
} from 'lodash-es'

/**
 * @namespace PropUtils
 * @desc Vue 组件属性工具函数集合
 */

const nestRE = /^(attrs|props|on|nativeOn|class|style|hook)$/

/**
 * @desc 合并 JSX 属性
 * @param {Array} objs - 要合并的属性对象数组
 * @returns {Object} 返回合并后的属性对象
 * @example
 * // 合并 JSX 属性
 * const mergedProps = mergeJSXProps([
 *   { class: 'btn' },
 *   { style: { color: 'red' } },
 *   { onClick: handleClick }
 * ]);
 */
export function mergeJSXProps (objs) {
	return objs.reduce(function (a, b) {
		var aa, bb, key, nestedKey, temp
		for (key in b) {
			aa = a[key]
			bb = b[key]
			if (aa && nestRE.test(key)) {
				// normalize class
				if (key === 'class') {
					if (typeof aa === 'string') {
						temp = aa
						a[key] = aa = {}
						aa[temp] = true
					}
					if (typeof bb === 'string') {
						temp = bb
						b[key] = bb = {}
						bb[temp] = true
					}
				}
				if (key === 'on' || key === 'nativeOn' || key === 'hook') {
					// merge functions
					for (nestedKey in bb) {
						aa[nestedKey] = mergeFn(aa[nestedKey], bb[nestedKey])
					}
				} else if (Array.isArray(aa)) {
					a[key] = aa.concat(bb)
				} else if (Array.isArray(bb)) {
					a[key] = [aa].concat(bb)
				} else {
					for (nestedKey in bb) {
						aa[nestedKey] = bb[nestedKey]
					}
				}
			} else {
				a[key] = b[key]
			}
		}
		return a
	}, {})
}

/**
 * @desc 合并函数
 * @param {Function} a - 第一个函数
 * @param {Function} b - 第二个函数
 * @returns {Function} 返回合并后的函数
 * @example
 * // 合并两个函数
 * const fn1 = () => console.log('fn1');
 * const fn2 = () => console.log('fn2');
 * const mergedFn = mergeFn(fn1, fn2);
 * mergedFn(); // 会依次执行 fn1 和 fn2
 */
export function mergeFn (a, b) {
	return function () {
		a && a.apply(this, arguments)
		b && b.apply(this, arguments)
	}
}

/**
 * @desc 智能获取尺寸
 * @param {String|Number} size - 尺寸值
 * @param {Object} sizeMap - 尺寸码表
 * @returns {Number|String|undefined} 返回处理后的尺寸值
 * @example
 * // 获取像素尺寸
 * getSizeStyle(10); // '10px'
 * getSizeStyle('10px'); // '10px'
 * getSizeStyle('50%'); // '50%'
 * 
 * // 使用尺寸码表
 * getSizeStyle('large', { large: 100 }); // '100px'
 * getSizeStyle('small', { small: '50px' }); // '50px'
 */
export function getSizeStyle (size, sizeMap = {}) {

	if (isUndefined(size)) {
		return 0
	}

	if (isString(size)) {
		if (endsWith(size, 'px')) {
			return size
		}

		if (endsWith(size, '%')) {
			return size
		}

		if (includes(keysIn(sizeMap), size)) {
			return getSizeStyle(sizeMap[size])
		}
	}

	let _size = parseInt(size, 10)

	if (_size.toFixed() == 'NaN') {
		return 0
	}

	if (isNumber(_size)) {
		return `${size}px`
	}

}

export default {
	mergeJSXProps,
	mergeFn,
}