/**
 * 服务工具模块
 * @module util/serviceTool
 */

/**
 * 注入服务提供者
 * @param serviceProvide - 服务提供者对象
 * @param serviceRoot - 服务根对象
 * @param path - 路径
 */
export function injectServiceProvide(serviceProvide: any, serviceRoot: any, path: string): void;

/**
 * 解析路径到命名空间
 * @param serviceRoot - 服务根对象
 * @param path - 路径
 * @param serviceProvide - 服务提供者对象
 */
export function parsePathToNamespace(serviceRoot: any, path: string, serviceProvide: any): void;

/**
 * 处理服务
 * @param service - 服务对象
 * @param files - 文件数组
 */
export function handle(service: any, files: Array<any> | any): void;

/**
 * 默认导出对象
 */
declare const _default: {
  handle: typeof handle;
  parsePathToNamespace: typeof parsePathToNamespace;
  injectServiceProvide: typeof injectServiceProvide;
};
export default _default;