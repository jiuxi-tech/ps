/**
 * commonUtils - 通用工具函数集
 * (c) 2020 lincong1987
 */

/**
 * 通用工具函数集合
 */

/**
 * 类型检测
 * 
 * @param obj - 要检测类型的对象
 * @returns 返回对象的类型字符串
 * @example
 * // 检测不同类型
 * typeOf([]); // 'array'
 * typeOf({}); // 'object'
 * typeOf(''); // 'string'
 * typeOf(123); // 'number'
 * typeOf(true); // 'boolean'
 * typeOf(function() {}); // 'function'
 * typeOf(new Date()); // 'date'
 * typeOf(/regexp/); // 'regExp'
 * typeOf(undefined); // 'undefined'
 * typeOf(null); // 'null'
 */
export function typeOf(obj: any): string;