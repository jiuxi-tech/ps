/*!
 * utils - 通用工具函数集
 * (c) 2020 lincong1987
 */

import { random, now } from 'lodash-es'

/**
 * @namespace Utils
 * @desc 通用工具函数集合
 */

const inBrowser = typeof window !== 'undefined'
	&& Object.prototype.toString.call(window) !== '[object Object]'
const UA = inBrowser && window.navigator.userAgent.toLowerCase()
const isIE9 = UA && UA.indexOf('msie 9.0') > 0
const objectToString = Object.prototype.toString

/**
 * @desc 检测低版本 IE 浏览器
 * @param {Number} v - IE 版本号
 * @returns {Boolean} 如果是指定版本的 IE 浏览器则返回 true
 * @example
 * // 检测是否为 IE8 浏览器
 * if (isIELowVersion(8)) {
 *   console.log('当前是 IE8 浏览器');
 * }
 */
function isIELowVersion (v) {
	const b = document.createElement('b')
	b.innerHTML = `<!--[if IE ${v}]><i></i><![endif]-->`
	return b.getElementsByTagName('i').length === 1
}

/**
 * @desc 检测高版本 IE 浏览器
 * @param {Number} v - IE 版本号
 * @returns {Boolean} 如果是指定版本的 IE 浏览器则返回 true
 * @example
 * // 检测是否为 IE11 浏览器
 * if (isIEHightVersion(11)) {
 *   console.log('当前是 IE11 浏览器');
 * }
 */
function isIEHightVersion (v) {
	return RegExp(`msie${!isNaN(v) ? (`\\s${v}`) : ''}`, 'i').
		test(navigator.userAgent)
}

/**
 * @desc 检测 IE 浏览器版本
 * @param {Number} v - IE 版本号
 * @returns {Boolean} 如果是指定版本的 IE 浏览器则返回 true
 * @example
 * // 检测是否为 IE10 浏览器
 * if (isIEVersion(10)) {
 *   console.log('当前是 IE10 浏览器');
 * }
 */
export function isIEVersion (v) {
	return v > 9 ? isIEHightVersion(v) : isIELowVersion(v)
}

/**
 * @desc 获取元素的 class 属性值（兼容 IE9）
 * @param {Element} el - 要获取 class 的元素
 * @returns {String} 返回元素的 class 属性值
 * @example
 * // 获取元素的 class 属性值
 * const className = getClass(element);
 */
export function getClass (el) {
	let classname = el.className
	if (typeof classname === 'object') {
		classname = classname.baseVal || ''
	}
	return classname
}

/**
 * @desc 判断 DOM 节点是否有某样式
 * @param {Element} el - 要检查的元素
 * @param {String} name - 要检查的样式类名
 * @returns {Boolean} 如果元素包含指定的样式类则返回 true
 * @example
 * // 检查元素是否包含 'active' 类
 * if (hasClass(element, 'active')) {
 *   console.log('元素包含 active 类');
 * }
 */
export function hasClass (el, name) {
	if (!el) return
	const className = getClass(el)
	const classes = className.split(' ')
	return classes.indexOf(name) != -1
}

/**
 * @desc 设置元素的 class 属性值（兼容 IE 和 SVG）
 * @param {Element} el - 要设置 class 的元素
 * @param {String} cls - 要设置的 class 值
 * @example
 * // 设置元素的 class 属性值
 * setClass(element, 'new-class');
 */
export function setClass (el, cls) {
	/* istanbul ignore if */
	if (isIE9 && !/svg$/.test(el.namespaceURI)) {
		el.className = cls
	} else {
		el.setAttribute('class', cls)
	}
}

/**
 * @desc 添加 class（兼容 IE 和 SVG）
 * @param {Element} el - 要添加 class 的元素
 * @param {String} cls - 要添加的 class
 * @example
 * // 为元素添加 'active' 类
 * addClass(element, 'active');
 */
export function addClass (el, cls) {
	if (el.classList) {
		el.classList.add(cls)
	} else {
		const cur = ` ${getClass(el)} `
		if (cur.indexOf(` ${cls} `) < 0) {
			setClass(el, (cur + cls).trim())
		}
	}
}

/**
 * @desc 移除 class（兼容 IE 和 SVG）
 * @param {Element} el - 要移除 class 的元素
 * @param {String} cls - 要移除的 class
 * @example
 * // 从元素移除 'active' 类
 * removeClass(element, 'active');
 */
export function removeClass (el, cls) {
	if (el.classList) {
		el.classList.remove(cls)
	} else {
		let cur = ` ${getClass(el)} `
		const tar = ` ${cls} `
		while (cur.indexOf(tar) >= 0) {
			cur = cur.replace(tar, ' ')
		}
		setClass(el, cur.trim())
	}
	if (!el.className) {
		el.removeAttribute('class')
	}
}

/**
 * @desc 检查元素 a 是否包含元素 b（从 jQuery 中扣过来的，递归去算）
 * @param {Element} a - 父元素
 * @param {Element} b - 子元素
 * @returns {Boolean} 如果元素 a 包含元素 b 则返回 true
 * @example
 * // 检查父元素是否包含子元素
 * if (contains(parentElement, childElement)) {
 *   console.log('父元素包含子元素');
 * }
 */
export function contains (a, b) {
	const adown = a.nodeType === 9 ? a.documentElement : a
	const bup = b && b.parentNode
	return a === bup || !!(bup && bup.nodeType === 1 && adown.contains(bup))
}

/**
 * @desc 合并对象（浅拷贝）
 * @param {...Object} arguments - 要合并的对象
 * @returns {Object} 返回合并后的对象
 * @example
 * // 合并多个对象
 * const result = merge({a: 1}, {b: 2}, {c: 3});
 * // result = {a: 1, b: 2, c: 3}
 */
export function merge () {
	const base = arguments[0]
	if (!base) return;
	[].forEach.call(arguments, (item, index) => {
		if (index > 0) {
			for (const attrname in item) {
				base[attrname] = item[attrname]
			}
		}
	})
	return base
}

/**
 * @desc 扩展对象（只覆盖已存在的属性）
 * @param {...Object} arguments - 要扩展的对象
 * @returns {Object} 返回扩展后的对象
 * @example
 * // 扩展对象（只覆盖已存在的属性）
 * const base = {a: 1, b: 2};
 * const result = extend(base, {b: 3, c: 4});
 * // result = {a: 1, b: 3} (c 不会被添加，因为 base 中不存在)
 */
export function extend () {
	const base = arguments[0]
	if (!base) return;
	[].forEach.call(arguments, (item, index) => {
		if (index > 0) {
			for (const attrname in item) {
				if (base[attrname] !== undefined) {
					base[attrname] = item[attrname]
				}
			}
		}
	})
	return base
}

/**
 * @desc 检查值是否为数字类型
 * @param {*} value - 要检查的值
 * @returns {Boolean} 如果值为数字类型则返回 true
 * @example
 * // 检查值是否为数字
 * if (isNumber(123)) {
 *   console.log('123 是数字');
 * }
 */
export function isNumber (value) {
	return typeof value === 'number'
}

/**
 * @desc 检查值是否为日期类型
 * @param {*} value - 要检查的值
 * @returns {Boolean} 如果值为日期类型则返回 true
 * @example
 * // 检查值是否为日期
 * if (isDate(new Date())) {
 *   console.log('当前值是日期类型');
 * }
 */
export function isDate (value) {
	return toString.call(value) === '[object Date]'
}

/**
 * @desc 检查值是否为函数类型
 * @param {*} value - 要检查的值
 * @returns {Boolean} 如果值为函数类型则返回 true
 * @example
 * // 检查值是否为函数
 * if (isFunction(function() {})) {
 *   console.log('当前值是函数类型');
 * }
 */
export function isFunction (value) {
	return typeof value === 'function'
}

/**
 * @desc 检查值是否为对象类型
 * @param {*} value - 要检查的值
 * @returns {Boolean} 如果值为对象类型则返回 true
 * @example
 * // 检查值是否为对象
 * if (isObject({})) {
 *   console.log('当前值是对象类型');
 * }
 */
export function isObject (value) {
	const type = typeof value
	return !!value && (type == 'object' || type == 'function')
}

/**
 * @desc 检查值是否为数组类型
 * @param {*} value - 要检查的值
 * @returns {Boolean} 如果值为数组类型则返回 true
 * @example
 * // 检查值是否为数组
 * if (isArray([1, 2, 3])) {
 *   console.log('当前值是数组类型');
 * }
 */
export function isArray (value) {
	return Array.isArray(value)
}

/**
 * @desc 检查值是否为类对象类型
 * @param {*} value - 要检查的值
 * @returns {Boolean} 如果值为类对象类型则返回 true
 * @example
 * // 检查值是否为类对象
 * if (isObjectLike({})) {
 *   console.log('当前值是类对象类型');
 * }
 */
export function isObjectLike (value) {
	return !!value && typeof value === 'object'
}

/**
 * @desc 检查值是否为字符串类型
 * @param {*} value - 要检查的值
 * @returns {Boolean} 如果值为字符串类型则返回 true
 * @example
 * // 检查值是否为字符串
 * if (isString('hello')) {
 *   console.log('当前值是字符串类型');
 * }
 */
export function isString (value) {
	return (
		typeof value === 'string'
		|| (!isArray(value)
			&& isObjectLike(value)
			&& objectToString.call(value) == '[object String]')
	)
}

/**
 * @desc 通过 direction 和 align 计算元素的位置
 * @param {Element} brotherEle - 参考元素
 * @param {String} direction - 方向（top, bottom, left, right）
 * @param {String} align - 对齐方式
 * @param {Number} displacementX - X轴位移
 * @param {Number} displacementY - Y轴位移
 * @returns {Object} 返回包含 left、top 和 position 属性的对象
 * @example
 * // 计算元素位置
 * const position = getPositionWhenAfterBorther(element, 'bottom', 'left', 0, 0);
 */
export function getPositionWhenAfterBorther (
	brotherEle,
	direction,
	align,
	displacementX,
	displacementY,
) {
	const offset = {
		left: 0,
		top: 0,
		position: null,
	}
	displacementX = displacementX || 0
	displacementY = displacementY || 0
	direction = direction || 'bottom'
	align = align || 'left'

	const style = window.getComputedStyle(brotherEle, null)
	const rect = brotherEle.getBoundingClientRect()
	const height = rect.height || rect.bottom - rect.top
	const width = rect.width || rect.right - rect.left
	if (style.position === 'fixed' || style.position === 'absolute') {
		offset.position = style.position
		offset.left = Number(/^([0-9]*)/.exec(style.left)[0]) + displacementX
		offset.top = Number(/^([0-9]*)/.exec(style.top)[0]) + displacementY
	} else {
		offset.position = 'absolute'
		// 如果target元素不在任何相对定位下，则直接计算离屏幕的高度
		if (!brotherEle.offsetParent) {
			offset.left =
				rect.left + document.documentElement.scrollLeft + displacementX
			offset.top =
				rect.top + document.documentElement.scrollTop + displacementY
		} else {
			// offsetTop和offsetLeft表示该元素的左上角（边框外边缘）与已定位的父容器（offsetParent对象）左上角的距离
			offset.left = brotherEle.offsetLeft + displacementX
			offset.top = brotherEle.offsetTop + displacementY
		}
	}

	switch (direction) {
		case 'top':
			// offset.top = offset.top;
			if (align === 'left') {
				// offset.left = offset.left;
			} else if (align === 'center') {
				offset.left += width / 2
			} else if (align === 'right') {
				offset.left += width
			}
			break
		case 'bottom':
			offset.top += height
			if (align === 'left') {
				// offset.left = offset.left;
			} else if (align === 'center') {
				offset.left += width / 2
			} else if (align === 'right') {
				offset.left += width
			}
			break
		case 'left':
			// offset.left = offset.left;
			if (align === 'top') {
				// offset.top = offset.top;
			} else if (align === 'center') {
				offset.top += height / 2
			} else if (align === 'bottom') {
				offset.top += height
			}
			break
		case 'right':
			offset.left += width
			if (align === 'top') {
				// offset.top = offset.top;
			} else if (align === 'center') {
				offset.top += height / 2
			} else if (align === 'bottom') {
				offset.top += height
			}
			break
		default:
			break
	}

	offset.left += 'px'
	offset.top += 'px'

	return offset
}

/**
 * @desc 判断类型
 * @param {*} obj - 需要判断的对象
 * @returns {String} 返回对象的类型字符串
 * @example
 * // 判断对象类型
 * const type = typeOf([]); // 'array'
 * const type2 = typeOf({}); // 'object'
 * const type3 = typeOf(''); // 'string'
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
 * @desc 深度复制对象
 * @param {*} data - 原始数据
 * @returns {*} 返回深度复制后的数据
 * @example
 * // 深度复制对象
 * const original = {a: 1, b: {c: 2}};
 * const copy = deepCopy(original);
 * // 修改 copy 不会影响 original
 */
export function deepCopy (data) {
	const t = typeOf(data)
	let o

	if (t === 'array') {
		o = []
	} else if (t === 'object') {
		o = {}
	} else {
		return data
	}

	if (t === 'array') {
		for (let i = 0; i < data.length; i++) {
			o.push(deepCopy(data[i]))
		}
	} else if (t === 'object') {
		for (const i in data) {
			o[i] = deepCopy(data[i])
		}
	}
	return o
}

/**
 * @desc 空闲控制，返回函数连续调用时，空闲时间必须大于或等于 wait，func 才会执行
 * @param {Function} func - 传入函数
 * @param {Number} wait - 表示时间窗口的间隔（毫秒）
 * @param {Boolean} immediate - 设置为 true 时，调用触发于开始边界而不是结束边界
 * @returns {Function} 返回客户调用函数
 * @example
 * // 防抖函数，500ms 内只执行一次
 * const debouncedFn = debounce(function() {
 *   console.log('执行了');
 * }, 500);
 * 
 * // 立即执行的防抖函数
 * const immediateDebouncedFn = debounce(function() {
 *   console.log('立即执行');
 * }, 500, true);
 */
export var debounce = function (func, wait, immediate) {
	let timeout
	let args
	let context
	let timestamp
	let
		result

	var later = function () {
		// 据上一次触发时间间隔
		const last = new Date().getTime() - timestamp

		// 上次被包装函数被调用时间间隔 last 小于设定时间间隔 wait
		if (last < wait && last > 0) {
			timeout = setTimeout(later, wait - last)
		} else {
			timeout = null
			// 如果设定为 immediate === true，因为开始边界已经调用过了此处无需调用
			if (!immediate) {
				result = func.apply(context, args)
				if (!timeout) context = args = null
			}
		}
	}

	return function () {
		context = this
		args = arguments
		timestamp = new Date().getTime()
		const callNow = immediate && !timeout
		// 如果延时不存在，重新设定延时
		if (!timeout) timeout = setTimeout(later, wait)
		if (callNow) {
			result = func.apply(context, args)
			context = args = null
		}
		return result
	}
}

/**
 * @desc 获取滚动距离
 * @param {Window|Element} target - 滚动目标
 * @param {Boolean} top - 是否获取垂直滚动距离
 * @returns {Number} 返回滚动距离
 * @example
 * // 获取垂直滚动距离
 * const scrollTop = getScroll(window, true);
 * 
 * // 获取水平滚动距离
 * const scrollLeft = getScroll(window, false);
 */
export function getScroll (target, top) {
	if (typeof window === 'undefined') return 0

	const prop = top ? 'pageYOffset' : 'pageXOffset'
	const method = top ? 'scrollTop' : 'scrollLeft'
	const isWindow = target === window

	let ret = isWindow ? target[prop] : target[method]
	if (isWindow && typeof ret !== 'number') {
		const d = window.document
		// ie6,7,8 standard mode
		if (document.compatMode === 'CSS1Compat') {
			ret = d.documentElement[method]
		} else {
			// quirks mode
			ret = d.body[method]
		}
	}
	return ret
}

/**
 * @desc 获取元素 top, left, bottom 的绝对位置
 * @param {Element} element - 要获取位置的元素
 * @param {Window|Element} target - 参考元素
 * @returns {Object} 返回包含 top、left、bottom 属性的对象
 * @example
 * // 获取元素相对于窗口的绝对位置
 * const offset = getOffset(element, window);
 */
export function getOffset (element, target) {
	const el_rect = element.getBoundingClientRect()
	const target_rect = target === window
		? {
			top: 0,
			left: 0,
			bottom: window.innerHeight,
		}
		: target.getBoundingClientRect()
	const clientTop = element.clientTop || 0
	const clientLeft = element.clientLeft || 0

	const scrollTop = getScroll(target, true)
	const scrollLeft = getScroll(target, false)
	const topOffset = scrollTop - target_rect.top
	const leftOffset = scrollLeft - target_rect.left
	return {
		top: el_rect.top + topOffset - clientTop,
		left: el_rect.left + leftOffset - clientLeft,
		bottom: el_rect.bottom + topOffset - clientTop,
	}
}

/**
 * @desc 获取视口尺寸
 * @param {Element} [el] - 要获取视口尺寸的元素
 * @returns {Object} 返回包含 width 和 height 属性的对象
 * @example
 * // 获取窗口视口尺寸
 * const viewport = getViewport();
 * 
 * // 获取元素视口尺寸
 * const elementViewport = getViewport(element);
 */
export function getViewport (el) {

	if ((el || document).compatMode == 'BackCompat') {
		return {
			width: (el || document.body).clientWidth,
			height:(el || document.body).clientHeight,
		}
	}
	return {
		width: (el || document.documentElement).clientWidth,
		height: (el || document.documentElement).clientHeight,
	}
}

/**
 * @desc 判断参数是否是其中之一
 * @param {*} value - 要检查的值
 * @param {Array} validList - 有效值列表
 * @returns {Boolean} 如果值在有效值列表中则返回 true
 * @example
 * // 检查值是否在有效列表中
 * if (oneOf('apple', ['apple', 'banana', 'orange'])) {
 *   console.log('值有效');
 * }
 */
export function oneOf (value, validList) {
	for (let i = 0; i < validList.length; i++) {
		if (value === validList[i]) {
			return true
		}
	}
	return false
}

const hasOwnProperty = Object.prototype.hasOwnProperty

/**
 * @desc 检查对象是否有指定属性
 * @param {Object} obj - 要检查的对象
 * @param {String} key - 要检查的属性名
 * @returns {Boolean} 如果对象有指定属性则返回 true
 * @example
 * // 检查对象是否有指定属性
 * if (hasOwn({a: 1}, 'a')) {
 *   console.log('对象有属性 a');
 * }
 */
export function hasOwn (obj, key) {
	return hasOwnProperty.call(obj, key)
}

/**
 * @desc 将元素滚动到指定位置
 * @param {Element} element - 要滚动的元素
 * @param {Number} to - 目标位置
 * @param {Number} duration - 滚动持续时间（毫秒）
 * @example
 * // 将元素滚动到顶部
 * scrollTo(element, 0, 300);
 * 
 * // 将元素滚动到指定位置
 * scrollTo(element, 500, 500);
 */
export function scrollTo (element, to, duration) {
	const requestAnimationFrame = window.requestAnimationFrame ||
		function (func) {
			return setTimeout(() => {
				func && func()
			}, 10)
		}
	if (duration <= 0) {
		element.scrollTop = to
		return
	}
	const difference = to - element.scrollTop
	const perTick = (difference / duration) * 10
	requestAnimationFrame(() => {
		element.scrollTop += perTick
		if (element.scrollTop === to) {
			return
		}
		scrollTo(element, to, duration - 10)
	})
}

/**
 * @desc 空函数
 * @example
 * // 用作默认回调函数
 * const callback = noop;
 */
export const noop = function () {
	// 空函数
}

/**
 * @desc 生成随机 ID
 * @returns {Number} 返回随机 ID
 * @example
 * // 生成随机 ID
 * const id = generateId();
 */
export const generateId = function () {
	return Math.floor(Math.random() * 10000)
}

/**
 * @desc 判断两个简单数组的元素是否一致
 * @param {Array} arr1 - 第一个数组
 * @param {Array} arr2 - 第二个数组
 * @returns {Boolean} 如果两个数组元素一致则返回 true
 * @example
 * // 比较两个数组是否相等
 * const result = isArrayEqual([1, 2, 3], [3, 2, 1]); // true
 * const result2 = isArrayEqual([1, 2, 3], [1, 2, 4]); // false
 */
export const isArrayEqual = function (arr1, arr2) {

	// console.log(arr1, arr2)

	if ((typeof arr1 === 'undefined' && typeof arr2 === 'undefined')
		|| (typeof arr1 === 'undefined' && typeof arr2 === 'object')
		|| (typeof arr1 === 'object' && typeof arr2 === 'undefined')
		|| (!arr1 && arr2)
		|| (!arr2 && arr1)

	) {
		return false
	}

	if (!arr1 && !arr2) {
		return true
	}

	if (arr1.length !== arr2.length) {
		return false
	}

	if (!arr1.every(el => arr2.indexOf(el) !== -1) ||
		!arr2.every(el => arr1.indexOf(el) !== -1)) {
		return false
	}

	return true

}

/**
 * @desc 扁平化数组（处理树形结构数据）
 * @param {Array} data - 要扁平化的数据
 * @param {String} childrenName - 子节点属性名，默认为 'children'
 * @returns {Array} 返回扁平化后的数组
 * @example
 * // 扁平化树形数据
 * const tree = [
 *   {
 *     id: 1,
 *     name: '节点1',
 *     children: [
 *       { id: 2, name: '节点2' }
 *     ]
 *   }
 * ];
 * const flat = flatArray(tree);
 */
export function flatArray (data = [], childrenName = 'children') {
	const result = []
	const loop = array => {
		array.forEach(item => {
			if (item[childrenName]) {
				const newItem = {...item}
				delete newItem[childrenName]
				result.push(newItem)
				if (item[childrenName].length > 0) {
					loop(item[childrenName])
				}
			} else {
				result.push(item)
			}
		})
	}
	loop(data)
	return result
}

/**
 * @desc 映射树形结构数据
 * @param {Array} tree - 要映射的树形数据
 * @param {Function} mapper - 映射函数
 * @param {String} childrenName - 子节点属性名，默认为 'children'
 * @returns {Array} 返回映射后的树形数据
 * @example
 * // 映射树形数据
 * const tree = [{ id: 1, name: '节点1' }];
 * const mapped = treeMap(tree, (node) => ({ ...node, selected: false }));
 */
export function treeMap (tree, mapper, childrenName = 'children') {
	return tree.map((node, index) => {
		const extra = {}
		if (node[childrenName]) {
			extra[childrenName] =
				treeMap(node[childrenName], mapper, childrenName)
		}
		return {
			...mapper(node, index),
			...extra,
		}
	})
}

/**
 * @desc 扁平化过滤树形结构数据
 * @param {Array} tree - 要过滤的树形数据
 * @param {Function} callback - 过滤回调函数
 * @returns {Array} 返回过滤后的扁平化数组
 * @example
 * // 过滤树形数据
 * const tree = [{ id: 1, name: '节点1' }];
 * const filtered = flatFilter(tree, (node) => node.id > 0);
 */
export function flatFilter (tree, callback) {
	return tree.reduce((acc, node) => {
		if (callback(node)) {
			acc.push(node)
		}
		if (node.children) {
			const children = flatFilter(node.children, callback)
			acc.push(...children)
		}
		return acc
	}, [])
}

let uidIndex = 0

/**
 * @desc 生成唯一 ID（带前缀）
 * @param {String} prefix - ID 前缀，默认为 'uuid'
 * @returns {String} 返回唯一 ID
 * @example
 * // 生成带前缀的唯一 ID
 * const id = uid('user'); // 'user-时间戳-索引'
 */
export function uid (prefix = 'uuid') {
	let now = +new Date()
	return `${prefix}-${now}-${++uidIndex}`
}

/**
 * @desc 生成 UUID
 * @returns {String} 返回 UUID
 * @example
 * // 生成 UUID
 * const uuid = uuid(); // 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'
 */
export function uuid () {
	let d = now()
	return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g,
		function (c) {
			let r = (d + random(16)) % 16 | 0
			d = Math.floor(d / 16)
			return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16)
		})
}

/**
 * @desc 将字符串转换为短横线命名格式
 * @param {String} str - 要转换的字符串
 * @returns {String} 返回短横线命名格式的字符串
 * @example
 * // 转换为短横线命名
 * const result = kebabCase('camelCase'); // 'camel-case'
 * const result2 = kebabCase('PascalCase'); // 'pascal-case'
 */
export const kebabCase = function(str) {
	const hyphenateRE = /([^-])([A-Z])/g;
	return str
		.replace(hyphenateRE, '$1-$2')
		.replace(hyphenateRE, '$1-$2')
		.toLowerCase();
};

/**
 * @desc 检查值是否已定义（不为 undefined 且不为 null）
 * @param {*} val - 要检查的值
 * @returns {Boolean} 如果值已定义则返回 true
 * @example
 * // 检查值是否已定义
 * if (isDef(0)) {
 *   console.log('0 已定义');
 * }
 * if (isDef(null)) {
 *   console.log('不会执行，null 不算已定义');
 * }
 */
export function isDef(val) {
	return val !== undefined && val !== null;
}

/**
 * @desc 宽松相等比较
 * @param {*} a - 要比较的第一个值
 * @param {*} b - 要比较的第二个值
 * @returns {Boolean} 如果两个值相等则返回 true
 * @example
 * // 宽松相等比较
 * const result = looseEqual(1, '1'); // true
 * const result2 = looseEqual({}, {}); // true (内容相同)
 */
export const looseEqual = function(a, b) {
	const isObjectA = isObject(a);
	const isObjectB = isObject(b);
	if (isObjectA && isObjectB) {
		return JSON.stringify(a) === JSON.stringify(b);
	} else if (!isObjectA && !isObjectB) {
		return String(a) === String(b);
	} else {
		return false;
	}
};

/**
 * @desc 数组相等比较
 * @param {Array} arrayA - 要比较的第一个数组
 * @param {Array} arrayB - 要比较的第二个数组
 * @returns {Boolean} 如果两个数组相等则返回 true
 * @example
 * // 数组相等比较
 * const result = arrayEquals([1, 2, 3], [1, 2, 3]); // true
 * const result2 = arrayEquals([1, 2, 3], [3, 2, 1]); // false
 */
export const arrayEquals = function(arrayA, arrayB) {
	arrayA = arrayA || [];
	arrayB = arrayB || [];

	if (arrayA.length !== arrayB.length) {
		return false;
	}

	for (let i = 0; i < arrayA.length; i++) {
		if (!looseEqual(arrayA[i], arrayB[i])) {
			return false;
		}
	}

	return true;
};

/**
 * @desc 判断值是否相等（数组或普通值）
 * @param {*} value1 - 要比较的第一个值
 * @param {*} value2 - 要比较的第二个值
 * @returns {Boolean} 如果两个值相等则返回 true
 * @example
 * // 判断值是否相等
 * const result = isEqual([1, 2, 3], [1, 2, 3]); // true (数组比较)
 * const result2 = isEqual('hello', 'hello'); // true (字符串比较)
 */
export const isEqual = function(value1, value2) {
	if (Array.isArray(value1) && Array.isArray(value2)) {
		return arrayEquals(value1, value2);
	}
	return looseEqual(value1, value2);
}

/**
 * @desc 将字符串首字母大写
 * @param {String} str - 要处理的字符串
 * @returns {String} 返回首字母大写的字符串
 * @example
 * // 首字母大写
 * const result = capitalize('hello'); // 'Hello'
 * const result2 = capitalize('world'); // 'World'
 */
export const capitalize = function(str) {
	if (!isString(str)) return str;
	return str.charAt(0).toUpperCase() + str.slice(1);
}

/**
 * @desc 将真值转换为数组
 * @param {*} val - 要转换的值
 * @returns {Array} 返回数组
 * @example
 * // 将真值转换为数组
 * const result = coerceTruthyValueToArray([1, 2, 3]); // [1, 2, 3]
 * const result2 = coerceTruthyValueToArray('hello'); // ['hello']
 * const result3 = coerceTruthyValueToArray(null); // []
 */
export const coerceTruthyValueToArray = function(val) {
	if (Array.isArray(val)) {
		return val;
	} else if (val) {
		return [val];
	} else {
		return [];
	}
};

/**
 * @desc 值相等比较（数组）
 * @param {Array} a - 要比较的第一个数组
 * @param {Array} b - 要比较的第二个数组
 * @returns {Boolean} 如果两个数组相等则返回 true
 * @example
 * // 数组值相等比较
 * const result = valueEquals([1, 2, 3], [1, 2, 3]); // true
 * const result2 = valueEquals([1, 2, 3], [3, 2, 1]); // false
 */
export const valueEquals = (a, b) => {
	// see: https://stackoverflow.com/questions/3115982/how-to-check-if-two-arrays-are-equal-with-javascript
	if (a === b) return true;
	if (!(a instanceof Array)) return false;
	if (!(b instanceof Array)) return false;
	if (a.length !== b.length) return false;
	for (let i = 0; i !== a.length; ++i) {
		if (a[i] !== b[i]) return false;
	}
	return true;
};

/**
 * @desc 检查值是否为空
 * @param {*} val - 要检查的值
 * @returns {Boolean} 如果值为空则返回 true
 * @example
 * // 检查值是否为空
 * const result = isEmpty(null); // true
 * const result2 = isEmpty(''); // true
 * const result3 = isEmpty([]); // true
 * const result4 = isEmpty({}); // true
 * const result5 = isEmpty(0); // false
 */
export const isEmpty = function(val) {
	// null or undefined
	if (val == null) return true;

	if (typeof val === 'boolean') return false;

	if (typeof val === 'number') return !val;

	if (val instanceof Error) return val.message === '';

	switch (Object.prototype.toString.call(val)) {
		// String or Array
		case '[object String]':
		case '[object Array]':
			return !val.length;

		// Map or Set or File
		case '[object File]':
		case '[object Map]':
		case '[object Set]': {
			return !val.size;
		}
		// Plain Object
		case '[object Object]': {
			return !Object.keys(val).length;
		}
	}

	return false;
};