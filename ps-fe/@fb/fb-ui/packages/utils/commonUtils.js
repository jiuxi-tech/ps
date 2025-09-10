/*!
* commonUtils - 通用工具函数集
* (c) 2020 lincong1987
*/

/**
 * @namespace CommonUtils
 * @desc 通用工具函数集合
 */

/**
 * @desc 类型检测
 * @param {any} obj - 要检测类型的对象
 * @returns {String} 返回对象的类型字符串
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