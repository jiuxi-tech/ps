/**
 *
 * @type {(v: PropertyKey) => boolean}
 */
const hasOwnProperty = Object.prototype.hasOwnProperty
/**
 *
 * @param obj
 * @param key
 * @returns {boolean}
 */
const hasOwn = function (obj, key) {
    return hasOwnProperty.call(obj, key)
}
const RE_NARGS = /(%|)\{([0-9a-zA-Z_]+)\}/g

/**
 * 格式化
 * @param string
 * @param args
 * @returns {*}
 */
const template = function (string, ...args) {
    if (args.length === 1 && typeof args[0] === 'object') {
        args = args[0]
    }

    if (!args || !args.hasOwnProperty) {
        args = {}
    }

    return string.replace(RE_NARGS, (match, prefix, i, index) => {
        if (string[index - 1] === '{' && string[index + match.length] === '}') {
            return i
        }
        const result = hasOwn(args, i) ? args[i] : null
        if (result === null || result === undefined) {
            return ''
        }

        return result
    })
}

export default template
