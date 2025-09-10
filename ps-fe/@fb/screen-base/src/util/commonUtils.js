/*!
* commonUtils
* (c) 2020 lincong1987
*/

/**
 * 类型检测
 * @param obj {any}
 * @returns {String}
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
