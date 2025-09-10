/**
 * utils - 通用工具函数集
 * (c) 2020 lincong1987
 */

import { random, now } from 'lodash-es'

/**
 * 通用工具函数集合
 */

/**
 * 检测低版本 IE 浏览器
 * 
 * @param v - IE 版本号
 * @returns 如果是指定版本的 IE 浏览器则返回 true
 * @example
 * // 检测是否为 IE8 浏览器
 * if (isIELowVersion(8)) {
 *   console.log('当前是 IE8 浏览器');
 * }
 */
export function isIELowVersion(v: number): boolean;

/**
 * 检测高版本 IE 浏览器
 * 
 * @param v - IE 版本号
 * @returns 如果是指定版本的 IE 浏览器则返回 true
 * @example
 * // 检测是否为 IE11 浏览器
 * if (isIEHightVersion(11)) {
 *   console.log('当前是 IE11 浏览器');
 * }
 */
export function isIEHightVersion(v: number): boolean;

/**
 * 检测 IE 浏览器版本
 * 
 * @param v - IE 版本号
 * @returns 如果是指定版本的 IE 浏览器则返回 true
 * @example
 * // 检测是否为 IE10 浏览器
 * if (isIEVersion(10)) {
 *   console.log('当前是 IE10 浏览器');
 * }
 */
export function isIEVersion(v: number): boolean;

/**
 * 获取元素的 class 属性值（兼容 IE9）
 * 
 * @param el - 要获取 class 的元素
 * @returns 返回元素的 class 属性值
 * @example
 * // 获取元素的 class 属性值
 * const className = getClass(element);
 */
export function getClass(el: Element): string;

/**
 * 判断 DOM 节点是否有某样式
 * 
 * @param el - 要检查的元素
 * @param name - 要检查的样式类名
 * @returns 如果元素包含指定的样式类则返回 true
 * @example
 * // 检查元素是否包含 'active' 类
 * if (hasClass(element, 'active')) {
 *   console.log('元素包含 active 类');
 * }
 */
export function hasClass(el: Element, name: string): boolean;

/**
 * 设置元素的 class 属性值（兼容 IE 和 SVG）
 * 
 * @param el - 要设置 class 的元素
 * @param cls - 要设置的 class 值
 * @example
 * // 设置元素的 class 属性值
 * setClass(element, 'new-class');
 */
export function setClass(el: Element, cls: string): void;

/**
 * 添加 class（兼容 IE 和 SVG）
 * 
 * @param el - 要添加 class 的元素
 * @param cls - 要添加的 class
 * @example
 * // 为元素添加 'active' 类
 * addClass(element, 'active');
 */
export function addClass(el: Element, cls: string): void;

/**
 * 移除 class（兼容 IE 和 SVG）
 * 
 * @param el - 要移除 class 的元素
 * @param cls - 要移除的 class
 * @example
 * // 从元素移除 'active' 类
 * removeClass(element, 'active');
 */
export function removeClass(el: Element, cls: string): void;

/**
 * 检查元素 a 是否包含元素 b（从 jQuery 中扣过来的，递归去算）
 * 
 * @param a - 父元素
 * @param b - 子元素
 * @returns 如果元素 a 包含元素 b 则返回 true
 * @example
 * // 检查父元素是否包含子元素
 * if (contains(parentElement, childElement)) {
 *   console.log('父元素包含子元素');
 * }
 */
export function contains(a: Element, b: Element): boolean;

/**
 * 合并对象（浅拷贝）
 * 
 * @param arguments - 要合并的对象
 * @returns 返回合并后的对象
 * @example
 * // 合并多个对象
 * const result = merge({a: 1}, {b: 2}, {c: 3});
 * // result = {a: 1, b: 2, c: 3}
 */
export function merge(...args: any[]): any;

/**
 * 扩展对象（只覆盖已存在的属性）
 * 
 * @param arguments - 要扩展的对象
 * @returns 返回扩展后的对象
 * @example
 * // 扩展对象（只覆盖已存在的属性）
 * const base = {a: 1, b: 2};
 * const result = extend(base, {b: 3, c: 4});
 * // result = {a: 1, b: 3} (c 不会被添加，因为 base 中不存在)
 */
export function extend(...args: any[]): any;

/**
 * 检查值是否为数字类型
 * 
 * @param value - 要检查的值
 * @returns 如果值为数字类型则返回 true
 * @example
 * // 检查值是否为数字
 * if (isNumber(123)) {
 *   console.log('123 是数字');
 * }
 */
export function isNumber(value: any): boolean;

/**
 * 检查值是否为日期类型
 * 
 * @param value - 要检查的值
 * @returns 如果值为日期类型则返回 true
 * @example
 * // 检查值是否为日期
 * if (isDate(new Date())) {
 *   console.log('当前值是日期类型');
 * }
 */
export function isDate(value: any): boolean;

/**
 * 检查值是否为函数类型
 * 
 * @param value - 要检查的值
 * @returns 如果值为函数类型则返回 true
 * @example
 * // 检查值是否为函数
 * if (isFunction(function() {})) {
 *   console.log('当前值是函数类型');
 * }
 */
export function isFunction(value: any): boolean;

/**
 * 检查值是否为对象类型
 * 
 * @param value - 要检查的值
 * @returns 如果值为对象类型则返回 true
 * @example
 * // 检查值是否为对象
 * if (isObject({})) {
 *   console.log('当前值是对象类型');
 * }
 */
export function isObject(value: any): boolean;

/**
 * 检查值是否为数组类型
 * 
 * @param value - 要检查的值
 * @returns 如果值为数组类型则返回 true
 * @example
 * // 检查值是否为数组
 * if (isArray([1, 2, 3])) {
 *   console.log('当前值是数组类型');
 * }
 */
export function isArray(value: any): boolean;

/**
 * 检查值是否为类对象类型
 * 
 * @param value - 要检查的值
 * @returns 如果值为类对象类型则返回 true
 * @example
 * // 检查值是否为类对象
 * if (isObjectLike({})) {
 *   console.log('当前值是类对象类型');
 * }
 */
export function isObjectLike(value: any): boolean;

/**
 * 检查值是否为字符串类型
 * 
 * @param value - 要检查的值
 * @returns 如果值为字符串类型则返回 true
 * @example
 * // 检查值是否为字符串
 * if (isString('hello')) {
 *   console.log('当前值是字符串类型');
 * }
 */
export function isString(value: any): boolean;

/**
 * 通过 direction 和 align 计算元素的位置
 * 
 * @param brotherEle - 参考元素
 * @param direction - 方向（top, bottom, left, right）
 * @param align - 对齐方式
 * @param displacementX - X轴位移
 * @param displacementY - Y轴位移
 * @returns 返回包含 left、top 和 position 属性的对象
 * @example
 * // 计算元素位置
 * const position = getPositionWhenAfterBorther(element, 'bottom', 'left', 0, 0);
 */
export function getPositionWhenAfterBorther(
  brotherEle: Element,
  direction: string,
  align: string,
  displacementX: number,
  displacementY: number
): { left: string; top: string; position: string | null };

/**
 * 判断类型
 * 
 * @param obj - 需要判断的对象
 * @returns 返回对象的类型字符串
 * @example
 * // 判断对象类型
 * const type = typeOf([]); // 'array'
 * const type2 = typeOf({}); // 'object'
 * const type3 = typeOf(''); // 'string'
 */
export function typeOf(obj: any): string;

/**
 * 深度复制对象
 * 
 * @param data - 原始数据
 * @returns 返回深度复制后的数据
 * @example
 * // 深度复制对象
 * const original = {a: 1, b: {c: 2}};
 * const copy = deepCopy(original);
 * // 修改 copy 不会影响 original
 */
export function deepCopy(data: any): any;

/**
 * 空闲控制，返回函数连续调用时，空闲时间必须大于或等于 wait，func 才会执行
 * 
 * @param func - 传入函数
 * @param wait - 表示时间窗口的间隔（毫秒）
 * @param immediate - 设置为 true 时，调用触发于开始边界而不是结束边界
 * @returns 返回客户调用函数
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
export function debounce<T extends (...args: any[]) => any>(
  func: T,
  wait: number,
  immediate?: boolean
): T;

/**
 * 获取滚动距离
 * 
 * @param target - 滚动目标
 * @param top - 是否获取垂直滚动距离
 * @returns 返回滚动距离
 * @example
 * // 获取垂直滚动距离
 * const scrollTop = getScroll(window, true);
 * 
 * // 获取水平滚动距离
 * const scrollLeft = getScroll(window, false);
 */
export function getScroll(target: Window | Element, top: boolean): number;

/**
 * 获取元素 top, left, bottom 的绝对位置
 * 
 * @param element - 要获取位置的元素
 * @param target - 参考元素
 * @returns 返回包含 top、left、bottom 属性的对象
 * @example
 * // 获取元素相对于窗口的绝对位置
 * const offset = getOffset(element, window);
 */
export function getOffset(
  element: Element,
  target: Window | Element
): { top: number; left: number; bottom: number };

/**
 * 获取视口尺寸
 * 
 * @param el - 要获取视口尺寸的元素
 * @returns 返回包含 width 和 height 属性的对象
 * @example
 * // 获取窗口视口尺寸
 * const viewport = getViewport();
 * 
 * // 获取元素视口尺寸
 * const elementViewport = getViewport(element);
 */
export function getViewport(el?: Element): { width: number; height: number };

/**
 * 判断参数是否是其中之一
 * 
 * @param value - 要检查的值
 * @param validList - 有效值列表
 * @returns 如果值在有效值列表中则返回 true
 * @example
 * // 检查值是否在有效列表中
 * if (oneOf('apple', ['apple', 'banana', 'orange'])) {
 *   console.log('值有效');
 * }
 */
export function oneOf<T>(value: T, validList: T[]): boolean;

/**
 * 检查对象是否有指定属性
 * 
 * @param obj - 要检查的对象
 * @param key - 要检查的属性名
 * @returns 如果对象有指定属性则返回 true
 * @example
 * // 检查对象是否有指定属性
 * if (hasOwn({a: 1}, 'a')) {
 *   console.log('对象有属性 a');
 * }
 */
export function hasOwn(obj: Record<string, any>, key: string): boolean;

/**
 * 将元素滚动到指定位置
 * 
 * @param element - 要滚动的元素
 * @param to - 目标位置
 * @param duration - 滚动持续时间（毫秒）
 * @example
 * // 将元素滚动到顶部
 * scrollTo(element, 0, 300);
 * 
 * // 将元素滚动到指定位置
 * scrollTo(element, 500, 500);
 */
export function scrollTo(element: Element, to: number, duration: number): void;

/**
 * 空函数
 * @example
 * // 用作默认回调函数
 * const callback = noop;
 */
export const noop: () => void;

/**
 * 生成随机 ID
 * 
 * @returns 返回随机 ID
 * @example
 * // 生成随机 ID
 * const id = generateId();
 */
export const generateId: () => number;

/**
 * 判断两个简单数组的元素是否一致
 * 
 * @param arr1 - 第一个数组
 * @param arr2 - 第二个数组
 * @returns 如果两个数组元素一致则返回 true
 * @example
 * // 比较两个数组是否相等
 * const result = isArrayEqual([1, 2, 3], [3, 2, 1]); // true
 * const result2 = isArrayEqual([1, 2, 3], [1, 2, 4]); // false
 */
export const isArrayEqual: (arr1: any[], arr2: any[]) => boolean;

/**
 * 扁平化数组（处理树形结构数据）
 * 
 * @param data - 要扁平化的数据
 * @param childrenName - 子节点属性名，默认为 'children'
 * @returns 返回扁平化后的数组
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
export function flatArray(data?: any[], childrenName?: string): any[];

/**
 * 映射树形结构数据
 * 
 * @param tree - 要映射的树形数据
 * @param mapper - 映射函数
 * @param childrenName - 子节点属性名，默认为 'children'
 * @returns 返回映射后的树形数据
 * @example
 * // 映射树形数据
 * const tree = [{ id: 1, name: '节点1' }];
 * const mapped = treeMap(tree, (node) => ({ ...node, selected: false }));
 */
export function treeMap(
  tree: any[],
  mapper: (node: any, index: number) => any,
  childrenName?: string
): any[];

/**
 * 扁平化过滤树形结构数据
 * 
 * @param tree - 要过滤的树形数据
 * @param callback - 过滤回调函数
 * @returns 返回过滤后的扁平化数组
 * @example
 * // 过滤树形数据
 * const tree = [{ id: 1, name: '节点1' }];
 * const filtered = flatFilter(tree, (node) => node.id > 0);
 */
export function flatFilter(
  tree: any[],
  callback: (node: any) => boolean
): any[];

/**
 * 生成唯一 ID（带前缀）
 * 
 * @param prefix - ID 前缀，默认为 'uuid'
 * @returns 返回唯一 ID
 * @example
 * // 生成带前缀的唯一 ID
 * const id = uid('user'); // 'user-时间戳-索引'
 */
export function uid(prefix?: string): string;

/**
 * 生成 UUID
 * 
 * @returns 返回 UUID
 * @example
 * // 生成 UUID
 * const uuid = uuid(); // 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'
 */
export function uuid(): string;

/**
 * 将字符串转换为短横线命名格式
 * 
 * @param str - 要转换的字符串
 * @returns 返回短横线命名格式的字符串
 * @example
 * // 转换为短横线命名
 * const result = kebabCase('camelCase'); // 'camel-case'
 * const result2 = kebabCase('PascalCase'); // 'pascal-case'
 */
export const kebabCase: (str: string) => string;

/**
 * 检查值是否已定义（不为 undefined 且不为 null）
 * 
 * @param val - 要检查的值
 * @returns 如果值已定义则返回 true
 * @example
 * // 检查值是否已定义
 * if (isDef(0)) {
 *   console.log('0 已定义');
 * }
 * if (isDef(null)) {
 *   console.log('不会执行，null 不算已定义');
 * }
 */
export function isDef<T>(val: T): val is NonNullable<T>;

/**
 * 宽松相等比较
 * 
 * @param a - 要比较的第一个值
 * @param b - 要比较的第二个值
 * @returns 如果两个值相等则返回 true
 * @example
 * // 宽松相等比较
 * const result = looseEqual(1, '1'); // true
 * const result2 = looseEqual({}, {}); // true (内容相同)
 */
export const looseEqual: (a: any, b: any) => boolean;

/**
 * 数组相等比较
 * 
 * @param arrayA - 要比较的第一个数组
 * @param arrayB - 要比较的第二个数组
 * @returns 如果两个数组相等则返回 true
 * @example
 * // 数组相等比较
 * const result = arrayEquals([1, 2, 3], [1, 2, 3]); // true
 * const result2 = arrayEquals([1, 2, 3], [3, 2, 1]); // false
 */
export const arrayEquals: (arrayA: any[], arrayB: any[]) => boolean;

/**
 * 判断值是否相等（数组或普通值）
 * 
 * @param value1 - 要比较的第一个值
 * @param value2 - 要比较的第二个值
 * @returns 如果两个值相等则返回 true
 * @example
 * // 判断值是否相等
 * const result = isEqual([1, 2, 3], [1, 2, 3]); // true (数组比较)
 * const result2 = isEqual('hello', 'hello'); // true (字符串比较)
 */
export const isEqual: (value1: any, value2: any) => boolean;

/**
 * 将字符串首字母大写
 * 
 * @param str - 要处理的字符串
 * @returns 返回首字母大写的字符串
 * @example
 * // 首字母大写
 * const result = capitalize('hello'); // 'Hello'
 * const result2 = capitalize('world'); // 'World'
 */
export const capitalize: (str: string) => string;

/**
 * 将真值转换为数组
 * 
 * @param val - 要转换的值
 * @returns 返回数组
 * @example
 * // 将真值转换为数组
 * const result = coerceTruthyValueToArray([1, 2, 3]); // [1, 2, 3]
 * const result2 = coerceTruthyValueToArray('hello'); // ['hello']
 * const result3 = coerceTruthyValueToArray(null); // []
 */
export const coerceTruthyValueToArray: <T>(val: T) => T extends any[] ? T : T extends null | undefined ? [] : [T];

/**
 * 值相等比较（数组）
 * 
 * @param a - 要比较的第一个数组
 * @param b - 要比较的第二个数组
 * @returns 如果两个数组相等则返回 true
 * @example
 * // 数组值相等比较
 * const result = valueEquals([1, 2, 3], [1, 2, 3]); // true
 * const result2 = valueEquals([1, 2, 3], [3, 2, 1]); // false
 */
export const valueEquals: (a: any[], b: any[]) => boolean;

/**
 * 检查值是否为空
 * 
 * @param val - 要检查的值
 * @returns 如果值为空则返回 true
 * @example
 * // 检查值是否为空
 * const result = isEmpty(null); // true
 * const result2 = isEmpty(''); // true
 * const result3 = isEmpty([]); // true
 * const result4 = isEmpty({}); // true
 * const result5 = isEmpty(0); // false
 */
export const isEmpty: (val: any) => boolean;

/**
 * 浏览器环境标识
 */
export const inBrowser: boolean;

/**
 * 用户代理字符串
 */
export const UA: string | undefined;

/**
 * 是否为 IE9 浏览器
 */
export const isIE9: boolean;