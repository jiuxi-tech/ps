/*!
 * dom - DOM 操作工具集
 * (c) 2020 lincong1987
 * @description 提供常用的 DOM 操作工具函数，兼容不同浏览器
 */

/* istanbul ignore next */

const SPECIAL_CHARS_REGEXP = /([\:\-\_]+(.))/g
const MOZ_HACK_REGEXP = /^moz([A-Z])/
const ieVersion = Number(document.documentMode)

/* istanbul ignore next */
/**
 * @desc 去除字符串首尾空格
 * @param {String} string - 要处理的字符串
 * @returns {String} 返回去除首尾空格的字符串
 * @example
 * // 去除字符串首尾空格
 * const result = trim('  hello world  '); // 'hello world'
 */
const trim = function (string) {
	return (string || '').replace(/^[\s\uFEFF]+|[\s\uFEFF]+$/g, '')
}

/* istanbul ignore next */
/**
 * @desc 将字符串转换为驼峰命名格式
 * @param {String} name - 要转换的字符串
 * @returns {String} 返回驼峰命名格式的字符串
 * @example
 * // 转换为驼峰命名
 * const result = camelCase('font-size'); // 'fontSize'
 * const result2 = camelCase('background-color'); // 'backgroundColor'
 */
const camelCase = function (name) {
	return name.replace(SPECIAL_CHARS_REGEXP,
		function (_, separator, letter, offset) {
			return offset ? letter.toUpperCase() : letter
		}).replace(MOZ_HACK_REGEXP, 'Moz$1')
}

/* istanbul ignore next */
/**
 * @desc 添加事件监听器
 * @param {Element} element - 要添加事件监听的元素
 * @param {String} event - 事件名称
 * @param {Function} handler - 事件处理函数
 * @example
 * // 添加点击事件监听
 * on(button, 'click', function() {
 *   console.log('按钮被点击了');
 * });
 */
export const on = (function () {
	if (document.addEventListener) {
		return function (element, event, handler) {
			if (element && event && handler) {
				element.addEventListener(event, handler, false)
			}
		}
	} else {
		return function (element, event, handler) {
			if (element && event && handler) {
				element.attachEvent('on' + event, handler)
			}
		}
	}
})()

/* istanbul ignore next */
/**
 * @desc 移除事件监听器
 * @param {Element} element - 要移除事件监听的元素
 * @param {String} event - 事件名称
 * @param {Function} handler - 事件处理函数
 * @example
 * // 移除点击事件监听
 * off(button, 'click', handleClick);
 */
export const off = (function () {
	if (document.removeEventListener) {
		return function (element, event, handler) {
			if (element && event) {
				element.removeEventListener(event, handler, false)
			}
		}
	} else {
		return function (element, event, handler) {
			if (element && event) {
				element.detachEvent('on' + event, handler)
			}
		}
	}
})()

/* istanbul ignore next */
/**
 * @desc 添加一次性事件监听器，触发后自动移除
 * @param {Element} el - 要添加事件监听的元素
 * @param {String} event - 事件名称
 * @param {Function} fn - 事件处理函数
 * @example
 * // 添加一次性点击事件监听
 * once(button, 'click', function() {
 *   console.log('按钮只会响应一次点击');
 * });
 */
export const once = function (el, event, fn) {
	var listener = function () {
		if (fn) {
			fn.apply(this, arguments)
		}
		off(el, event, listener)
	}
	on(el, event, listener)
}

/* istanbul ignore next */
/**
 * @desc 检查元素是否包含指定的 CSS 类
 * @param {Element} el - 要检查的元素
 * @param {String} cls - 要检查的 CSS 类名
 * @returns {Boolean} 如果元素包含指定的 CSS 类则返回 true
 * @example
 * // 检查元素是否包含 'active' 类
 * if (hasClass(element, 'active')) {
 *   console.log('元素包含 active 类');
 * }
 */
export function hasClass (el, cls) {
	if (!el || !cls) return false
	if (cls.indexOf(' ') !== -1) throw new Error(
		'className should not contain space.')
	if (el.classList) {
		return el.classList.contains(cls)
	} else {
		return (' ' + el.className + ' ').indexOf(' ' + cls + ' ') > -1
	}
}

/* istanbul ignore next */
/**
 * @desc 为元素添加 CSS 类
 * @param {Element} el - 要添加 CSS 类的元素
 * @param {String} cls - 要添加的 CSS 类名
 * @example
 * // 为元素添加 'active' 类
 * addClass(element, 'active');
 */
export function addClass (el, cls) {
	if (!el) return
	var curClass = el.className
	var classes = (cls || '').split(' ')

	for (var i = 0, j = classes.length; i < j; i++) {
		var clsName = classes[i]
		if (!clsName) continue

		if (el.classList) {
			el.classList.add(clsName)
		} else if (!hasClass(el, clsName)) {
			curClass += ' ' + clsName
		}
	}
	if (!el.classList) {
		el.className = curClass
	}
}

/* istanbul ignore next */
/**
 * @desc 从元素移除 CSS 类
 * @param {Element} el - 要移除 CSS 类的元素
 * @param {String} cls - 要移除的 CSS 类名
 * @example
 * // 从元素移除 'active' 类
 * removeClass(element, 'active');
 */
export function removeClass (el, cls) {
	if (!el || !cls) return
	var classes = cls.split(' ')
	var curClass = ' ' + el.className + ' '

	for (var i = 0, j = classes.length; i < j; i++) {
		var clsName = classes[i]
		if (!clsName) continue

		if (el.classList) {
			el.classList.remove(clsName)
		} else if (hasClass(el, clsName)) {
			curClass = curClass.replace(' ' + clsName + ' ', ' ')
		}
	}
	if (!el.classList) {
		el.className = trim(curClass)
	}
}

/* istanbul ignore next */
/**
 * @desc 获取元素的样式值
 * @param {Element} element - 要获取样式的元素
 * @param {String} styleName - 要获取的样式名称
 * @returns {String|null} 返回样式值，如果获取失败则返回 null
 * @example
 * // 获取元素的背景颜色
 * const bgColor = getStyle(element, 'backgroundColor');
 * 
 * // 获取元素的宽度
 * const width = getStyle(element, 'width');
 */
export const getStyle = ieVersion < 9 ? function (element, styleName) {
	if (!element || !styleName) return null
	styleName = camelCase(styleName)
	if (styleName === 'float') {
		styleName = 'styleFloat'
	}
	try {
		switch (styleName) {
			case 'opacity':
				try {
					return element.filters.item('alpha').opacity / 100
				} catch (e) {
					return 1.0
				}
			default:
				return (element.style[styleName] || element.currentStyle
					? element.currentStyle[styleName]
					: null)
		}
	} catch (e) {
		return element.style[styleName]
	}
} : function (element, styleName) {
	if (!element || !styleName) return null
	styleName = camelCase(styleName)
	if (styleName === 'float') {
		styleName = 'cssFloat'
	}
	try {
		var computed = document.defaultView.getComputedStyle(element, '')
		return element.style[styleName] || computed ? computed[styleName] : null
	} catch (e) {
		return element.style[styleName]
	}
}

/* istanbul ignore next */
/**
 * @desc 设置元素的样式
 * @param {Element} element - 要设置样式的元素
 * @param {String|Object} styleName - 要设置的样式名称或样式对象
 * @param {String|Number} [value] - 要设置的样式值（当 styleName 为字符串时使用）
 * @example
 * // 设置单个样式
 * setStyle(element, 'backgroundColor', 'red');
 * 
 * // 设置多个样式
 * setStyle(element, {
 *   backgroundColor: 'red',
 *   fontSize: '14px'
 * });
 */
export function setStyle (element, styleName, value) {
	if (!element || !styleName) return

	if (typeof styleName === 'object') {
		for (var prop in styleName) {
			if (styleName.hasOwnProperty(prop)) {
				setStyle(element, prop, styleName[prop])
			}
		}
	} else {
		styleName = camelCase(styleName)
		if (styleName === 'opacity' && ieVersion < 9) {
			element.style.filter =
				isNaN(value) ? '' : 'alpha(opacity=' + value * 100 + ')'
		} else {
			element.style[styleName] = value
		}
	}
}

/**
 * @desc 检查元素是否具有滚动条
 * @param {Element} el - 要检查的元素
 * @param {Boolean} [vertical] - 是否检查垂直滚动条，默认为 undefined
 * @returns {Boolean} 如果元素具有滚动条则返回 true
 * @example
 * // 检查元素是否具有垂直滚动条
 * if (isScroll(element, true)) {
 *   console.log('元素具有垂直滚动条');
 * }
 * 
 * // 检查元素是否具有任意滚动条
 * if (isScroll(element)) {
 *   console.log('元素具有滚动条');
 * }
 */
export const isScroll = (el, vertical) => {

	const determinedDirection = vertical !== null || vertical !== undefined
	const overflow = determinedDirection ? vertical
		? getStyle(el, 'overflow-y')
		: getStyle(el, 'overflow-x') : getStyle(el, 'overflow')

	return overflow.match(/(scroll|auto)/)
}

/**
 * @desc 获取元素的滚动容器
 * @param {Element} el - 要查找滚动容器的元素
 * @param {Boolean} [vertical] - 是否查找垂直滚动容器，默认为 undefined
 * @returns {Window|Element} 返回滚动容器元素或 window 对象
 * @example
 * // 获取元素的垂直滚动容器
 * const container = getScrollContainer(element, true);
 */
export const getScrollContainer = (el, vertical) => {

	let parent = el
	while (parent) {
		if ([window, document, document.documentElement].includes(parent)) {
			return window
		}
		if (isScroll(parent, vertical)) {
			return parent
		}
		parent = parent.parentNode
	}

	return parent
}

/**
 * @desc 检查元素是否在容器内
 * @param {Element} el - 要检查的元素
 * @param {Element|Window} container - 容器元素
 * @returns {Boolean} 如果元素在容器内则返回 true
 * @example
 * // 检查元素是否在指定容器内
 * if (isInContainer(element, container)) {
 *   console.log('元素在容器内');
 * }
 */
export const isInContainer = (el, container) => {
	if (!el || !container) return false

	const elRect = el.getBoundingClientRect()
	let containerRect

	if ([window, document, document.documentElement, null, undefined].includes(
		container)) {
		containerRect = {
			top: 0,
			right: window.innerWidth,
			bottom: window.innerHeight,
			left: 0,
		}
	} else {
		containerRect = container.getBoundingClientRect()
	}

	return elRect.top < containerRect.bottom && elRect.bottom >
		containerRect.top && elRect.right > containerRect.left && elRect.left <
		containerRect.right
}

/**
 * @desc 获取元素的 innerWidth（包含 padding）
 * @param {Element} el - 要获取宽度的元素
 * @returns {Number} 返回元素的 innerWidth
 * @example
 * // 获取元素的 innerWidth
 * const width = innerWidth(element);
 */
export function innerWidth (el) {
	let width = el.offsetWidth
	let style = getComputedStyle(el)

	width += parseFloat(style.paddingLeft) + parseFloat(style.paddingRight)
	return width
}

/**
 * @desc 获取元素的 width（不包含 padding）
 * @param {Element} el - 要获取宽度的元素
 * @returns {Number} 返回元素的 width
 * @example
 * // 获取元素的 width
 * const width = width(element);
 */
export function width (el) {
	let width = el.offsetWidth
	let style = getComputedStyle(el)

	width -= parseFloat(style.paddingLeft) + parseFloat(style.paddingRight)
	return width
}

/**
 * @desc 获取窗口垂直滚动距离
 * @returns {Number} 返回窗口垂直滚动距离
 * @example
 * // 获取窗口垂直滚动距离
 * const scrollTop = getWindowScrollTop();
 */
export function getWindowScrollTop () {
	let doc = document.documentElement
	return (window.pageYOffset || doc.scrollTop) - (doc.clientTop || 0)
}

/**
 * @desc 获取窗口水平滚动距离
 * @returns {Number} 返回窗口水平滚动距离
 * @example
 * // 获取窗口水平滚动距离
 * const scrollLeft = getWindowScrollLeft();
 */
export function getWindowScrollLeft () {
	let doc = document.documentElement
	return (window.pageXOffset || doc.scrollLeft) - (doc.clientLeft || 0)
}

/**
 * @desc 获取元素的 outerWidth（包含 margin）
 * @param {Element} el - 要获取宽度的元素
 * @param {Boolean} [margin] - 是否包含 margin，默认为 false
 * @returns {Number} 返回元素的 outerWidth
 * @example
 * // 获取元素的 outerWidth（不包含 margin）
 * const width = getOuterWidth(element);
 * 
 * // 获取元素的 outerWidth（包含 margin）
 * const widthWithMargin = getOuterWidth(element, true);
 */
export function getOuterWidth (el, margin) {
	if (el) {
		let width = el.offsetWidth

		if (margin) {
			let style = getComputedStyle(el)
			width +=
				parseFloat(style.marginLeft) + parseFloat(style.marginRight)
		}

		return width
	} else {
		return 0
	}
}

/**
 * @desc 获取元素的 outerHeight（包含 margin）
 * @param {Element} el - 要获取高度的元素
 * @param {Boolean} [margin] - 是否包含 margin，默认为 false
 * @returns {Number} 返回元素的 outerHeight
 * @example
 * // 获取元素的 outerHeight（不包含 margin）
 * const height = getOuterHeight(element);
 * 
 * // 获取元素的 outerHeight（包含 margin）
 * const heightWithMargin = getOuterHeight(element, true);
 */
export function getOuterHeight (el, margin) {
	if (el) {
		let height = el.offsetHeight

		if (margin) {
			let style = getComputedStyle(el)
			height +=
				parseFloat(style.marginTop) + parseFloat(style.marginBottom)
		}

		return height
	} else {
		return 0
	}
}

/**
 * @desc 获取元素的 clientHeight（可包含 margin）
 * @param {Element} el - 要获取高度的元素
 * @param {Boolean} [margin] - 是否包含 margin，默认为 false
 * @returns {Number} 返回元素的 clientHeight
 * @example
 * // 获取元素的 clientHeight（不包含 margin）
 * const height = getClientHeight(element);
 * 
 * // 获取元素的 clientHeight（包含 margin）
 * const heightWithMargin = getClientHeight(element, true);
 */
export function getClientHeight (el, margin) {
	if (el) {
		let height = el.clientHeight

		if (margin) {
			let style = getComputedStyle(el)
			height +=
				parseFloat(style.marginTop) + parseFloat(style.marginBottom)
		}

		return height
	} else {
		return 0
	}
}

/**
 * @desc 获取视口尺寸
 * @returns {Object} 返回包含 width 和 height 属性的对象
 * @example
 * // 获取视口尺寸
 * const viewport = getViewport();
 * console.log(viewport.width, viewport.height);
 */
export function getViewport () {
	let win = window,
		d = document,
		e = d.documentElement,
		g = d.getElementsByTagName('body')[0],
		w = win.innerWidth || e.clientWidth || g.clientWidth,
		h = win.innerHeight || e.clientHeight || g.clientHeight

	return {
		width: w,
		height: h,
	}
}

/**
 * @desc 获取元素相对于文档的偏移量
 * @param {Element} el - 要获取偏移量的元素
 * @returns {Object} 返回包含 top 和 left 属性的对象
 * @example
 * // 获取元素相对于文档的偏移量
 * const offset = getOffset(element);
 * console.log(offset.top, offset.left);
 */
export function getOffset (el) {
	var rect = el.getBoundingClientRect()

	return {
		top: rect.top +
			(window.pageYOffset || document.documentElement.scrollTop ||
				document.body.scrollTop || 0),
		left: rect.left +
			(window.pageXOffset || document.documentElement.scrollLeft ||
				document.body.scrollLeft || 0),
	}
}

/**
 * @desc 获取元素的高度（不包含 padding、border）
 * @param {Element} el - 要获取高度的元素
 * @returns {Number} 返回元素的高度
 * @example
 * // 获取元素的高度
 * const height = getHeight(element);
 */
export function getHeight (el) {
	let height = el.offsetHeight
	let style = getComputedStyle(el)

	height -= parseFloat(style.paddingTop) + parseFloat(style.paddingBottom) +
		parseFloat(style.borderTopWidth) + parseFloat(style.borderBottomWidth)

	return height
}

/**
 * @desc 获取元素的宽度（不包含 padding、border）
 * @param {Element} el - 要获取宽度的元素
 * @returns {Number} 返回元素的宽度
 * @example
 * // 获取元素的宽度
 * const width = getWidth(element);
 */
export function getWidth (el) {
	let width = el.offsetWidth
	let style = getComputedStyle(el)

	width -= parseFloat(style.paddingLeft) + parseFloat(style.paddingRight) +
		parseFloat(style.borderLeftWidth) + parseFloat(style.borderRightWidth)

	return width
}