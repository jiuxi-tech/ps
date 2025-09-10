/**
 * 字符串格式化模块
 * @module util/format
 */

/**
 * 检查对象是否有指定属性
 * @param obj - 对象
 * @param key - 属性键名
 * @returns 是否有指定属性
 */
export const hasOwn: (obj: any, key: PropertyKey) => boolean;

/**
 * 字符串格式化函数
 * @param string - 模板字符串
 * @param args - 参数数组或对象
 * @returns 格式化后的字符串
 */
export const template: (string: string, ...args: any[]) => string;

/**
 * 默认导出格式化函数
 */
export default template;