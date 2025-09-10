/**
 * 存储工具模块
 * @module util/storeTool
 */

/**
 * 修复模块数组，将 index.js 文件优先排列
 * @param modules - 模块数组
 * @returns 修复后的模块数组
 */
export function fixModules(modules: Array<any>): Array<any>;

/**
 * 处理存储
 * @param store - 存储对象
 * @param files - 文件数组
 * @returns 存储对象
 */
export function handle(store: any, files: Array<any> | any): any;

/**
 * 默认导出对象
 */
declare const _default: {
  handle: typeof handle;
  fixModules: typeof fixModules;
};
export default _default;