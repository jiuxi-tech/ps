/**
 * 工具模块
 * @module util/utils
 */

/**
 * 将对象转换为原始对象（深拷贝）
 * @param obj - 要转换的对象
 * @returns 转换后的对象
 */
export function toRaw(obj: any): any;

/**
 * 生成UUID
 * @returns UUID字符串
 */
export function uuid(): string;

/**
 * 默认导出对象
 */
declare const _default: {
  uuid: typeof uuid;
  toRaw: typeof toRaw;
};
export default _default;