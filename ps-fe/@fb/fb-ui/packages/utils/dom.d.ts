/**
 * dom - DOM 操作工具集
 * (c) 2020 lincong1987
 * @description 提供常用的 DOM 操作工具函数，兼容不同浏览器
 */

/**
 * 去除字符串首尾空格
 * 
 * @param string - 要处理的字符串
 * @returns 返回去除首尾空格的字符串
 * @example
 * // 去除字符串首尾空格
 * const result = trim('  hello world  '); // 'hello world'
 */
export function trim(string: string): string;

/**
 * 将字符串转换为驼峰命名格式
 * 
 * @param name - 要转换的字符串
 * @returns 返回驼峰命名格式的字符串
 * @example
 * // 转换为驼峰命名
 * const result = camelCase('font-size'); // 'fontSize'
 * const result2 = camelCase('background-color'); // 'backgroundColor'
 */
export function camelCase(name: string): string;

/**
 * 添加事件监听器
 * 
 * @param element - 要添加事件监听的元素
 * @param event - 事件名称
 * @param handler - 事件处理函数
 * @example
 * // 添加点击事件监听
 * on(button, 'click', function() {
 *   console.log('按钮被点击了');
 * });
 */
export function on(element: EventTarget, event: string, handler: EventListener): void;

/**
 * 移除事件监听器
 * 
 * @param element - 要移除事件监听的元素
 * @param event - 事件名称
 * @param handler - 事件处理函数
 * @example
 * // 移除点击事件监听
 * off(button, 'click', handleClick);
 */
export function off(element: EventTarget, event: string, handler: EventListener): void;

/**
 * 添加一次性事件监听器，触发后自动移除
 * 
 * @param el - 要添加事件监听的元素
 * @param event - 事件名称
 * @param fn - 事件处理函数
 * @example
 * // 添加一次性点击事件监听
 * once(button, 'click', function() {
 *   console.log('按钮只会响应一次点击');
 * });
 */
export function once(el: EventTarget, event: string, fn: EventListener): void;

/**
 * 检查元素是否包含指定的 CSS 类
 * 
 * @param el - 要检查的元素
 * @param cls - 要检查的 CSS 类名
 * @returns 如果元素包含指定的 CSS 类则返回 true
 * @example
 * // 检查元素是否包含 'active' 类
 * if (hasClass(element, 'active')) {
 *   console.log('元素包含 active 类');
 * }
 */
export function hasClass(el: Element, cls: string): boolean;

/**
 * 为元素添加 CSS 类
 * 
 * @param el - 要添加 CSS 类的元素
 * @param cls - 要添加的 CSS 类名
 * @example
 * // 为元素添加 'active' 类
 * addClass(element, 'active');
 */
export function addClass(el: Element, cls: string): void;

/**
 * 从元素移除 CSS 类
 * 
 * @param el - 要移除 CSS 类的元素
 * @param cls - 要移除的 CSS 类名
 * @example
 * // 从元素移除 'active' 类
 * removeClass(element, 'active');
 */
export function removeClass(el: Element, cls: string): void;

/**
 * 获取元素的样式值
 * 
 * @param element - 要获取样式的元素
 * @param styleName - 要获取的样式名称
 * @returns 返回样式值，如果获取失败则返回 null
 * @example
 * // 获取元素的背景颜色
 * const bgColor = getStyle(element, 'backgroundColor');
 * 
 * // 获取元素的宽度
 * const width = getStyle(element, 'width');
 */
export function getStyle(element: HTMLElement, styleName: string): string | null;

/**
 * 设置元素的样式
 * 
 * @param element - 要设置样式的元素
 * @param styleName - 要设置的样式名称或样式对象
 * @param value - 要设置的样式值（当 styleName 为字符串时使用）
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
export function setStyle(element: HTMLElement, styleName: string | Partial<CSSStyleDeclaration>, value?: string | number): void;

/**
 * 检查元素是否具有滚动条
 * 
 * @param el - 要检查的元素
 * @param vertical - 是否检查垂直滚动条，默认为 undefined
 * @returns 如果元素具有滚动条则返回 true
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
export function isScroll(el: Element, vertical?: boolean): boolean;

/**
 * 获取元素的滚动容器
 * 
 * @param el - 要查找滚动容器的元素
 * @param vertical - 是否查找垂直滚动容器，默认为 undefined
 * @returns 返回滚动容器元素或 window 对象
 * @example
 * // 获取元素的垂直滚动容器
 * const container = getScrollContainer(element, true);
 */
export function getScrollContainer(el: Element, vertical?: boolean): Window | Element | undefined;

/**
 * 检查元素是否在容器内
 * 
 * @param el - 要检查的元素
 * @param container - 容器元素
 * @returns 如果元素在容器内则返回 true
 * @example
 * // 检查元素是否在指定容器内
 * if (isInContainer(element, container)) {
 *   console.log('元素在容器内');
 * }
 */
export function isInContainer(el: Element, container: Element | Window): boolean;

/**
 * 获取元素的 innerWidth（包含 padding）
 * 
 * @param el - 要获取宽度的元素
 * @returns 返回元素的 innerWidth
 * @example
 * // 获取元素的 innerWidth
 * const width = innerWidth(element);
 */
export function innerWidth(el: HTMLElement): number;

/**
 * 获取元素的 width（不包含 padding）
 * 
 * @param el - 要获取宽度的元素
 * @returns 返回元素的 width
 * @example
 * // 获取元素的 width
 * const width = width(element);
 */
export function width(el: HTMLElement): number;

/**
 * 获取窗口垂直滚动距离
 * 
 * @returns 返回窗口垂直滚动距离
 * @example
 * // 获取窗口垂直滚动距离
 * const scrollTop = getWindowScrollTop();
 */
export function getWindowScrollTop(): number;

/**
 * 获取窗口水平滚动距离
 * 
 * @returns 返回窗口水平滚动距离
 * @example
 * // 获取窗口水平滚动距离
 * const scrollLeft = getWindowScrollLeft();
 */
export function getWindowScrollLeft(): number;

/**
 * 获取元素的 outerWidth（包含 margin）
 * 
 * @param el - 要获取宽度的元素
 * @param margin - 是否包含 margin，默认为 false
 * @returns 返回元素的 outerWidth
 * @example
 * // 获取元素的 outerWidth（不包含 margin）
 * const width = getOuterWidth(element);
 * 
 * // 获取元素的 outerWidth（包含 margin）
 * const widthWithMargin = getOuterWidth(element, true);
 */
export function getOuterWidth(el: HTMLElement, margin?: boolean): number;

/**
 * 获取元素的 outerHeight（包含 margin）
 * 
 * @param el - 要获取高度的元素
 * @param margin - 是否包含 margin，默认为 false
 * @returns 返回元素的 outerHeight
 * @example
 * // 获取元素的 outerHeight（不包含 margin）
 * const height = getOuterHeight(element);
 * 
 * // 获取元素的 outerHeight（包含 margin）
 * const heightWithMargin = getOuterHeight(element, true);
 */
export function getOuterHeight(el: HTMLElement, margin?: boolean): number;

/**
 * 获取元素的 clientHeight（可包含 margin）
 * 
 * @param el - 要获取高度的元素
 * @param margin - 是否包含 margin，默认为 false
 * @returns 返回元素的 clientHeight
 * @example
 * // 获取元素的 clientHeight（不包含 margin）
 * const height = getClientHeight(element);
 * 
 * // 获取元素的 clientHeight（包含 margin）
 * const heightWithMargin = getClientHeight(element, true);
 */
export function getClientHeight(el: HTMLElement, margin?: boolean): number;

/**
 * 获取视口尺寸
 * 
 * @returns 返回包含 width 和 height 属性的对象
 * @example
 * // 获取视口尺寸
 * const viewport = getViewport();
 * console.log(viewport.width, viewport.height);
 */
export function getViewport(): { width: number; height: number };

/**
 * 获取元素相对于文档的偏移量
 * 
 * @param el - 要获取偏移量的元素
 * @returns 返回包含 top 和 left 属性的对象
 * @example
 * // 获取元素相对于文档的偏移量
 * const offset = getOffset(element);
 * console.log(offset.top, offset.left);
 */
export function getOffset(el: Element): { top: number; left: number };

/**
 * 获取元素的高度（不包含 padding、border）
 * 
 * @param el - 要获取高度的元素
 * @returns 返回元素的高度
 * @example
 * // 获取元素的高度
 * const height = getHeight(element);
 */
export function getHeight(el: HTMLElement): number;

/**
 * 获取元素的宽度（不包含 padding、border）
 * 
 * @param el - 要获取宽度的元素
 * @returns 返回元素的宽度
 * @example
 * // 获取元素的宽度
 * const width = getWidth(element);
 */
export function getWidth(el: HTMLElement): number;

/**
 * IE 版本号
 */
export const ieVersion: number;