/**
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
 * Vue 组件属性工具函数集合
 */

/**
 * 合并 JSX 属性
 * 
 * @param objs - 要合并的属性对象数组
 * @returns 返回合并后的属性对象
 * @example
 * // 合并 JSX 属性
 * const mergedProps = mergeJSXProps([
 *   { class: 'btn' },
 *   { style: { color: 'red' } },
 *   { onClick: handleClick }
 * ]);
 */
export function mergeJSXProps(objs: any[]): Record<string, any>;

/**
 * 合并函数
 * 
 * @param a - 第一个函数
 * @param b - 第二个函数
 * @returns 返回合并后的函数
 * @example
 * // 合并两个函数
 * const fn1 = () => console.log('fn1');
 * const fn2 = () => console.log('fn2');
 * const mergedFn = mergeFn(fn1, fn2);
 * mergedFn(); // 会依次执行 fn1 和 fn2
 */
export function mergeFn<T extends Function>(a: T, b: T): T;

/**
 * 智能获取尺寸
 * 
 * @param size - 尺寸值
 * @param sizeMap - 尺寸码表
 * @returns 返回处理后的尺寸值
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
export function getSizeStyle(
  size: string | number,
  sizeMap?: Record<string, string | number>
): number | string | undefined;

/**
 * PropUtils 默认导出
 */
export interface PropUtils {
  mergeJSXProps: typeof mergeJSXProps;
  mergeFn: typeof mergeFn;
}

declare const propUtils: PropUtils;

export default propUtils;