/**
 * 视图工具模块
 * @module util/viewTool
 */

/**
 * 构建路由
 * @param rotuer - 路由对象
 * @param context - 上下文
 * @param filepath - 文件路径
 * @param component - 组件
 */
export function buildRouter(rotuer: any, context: any, filepath: string, component: any): void;

/**
 * 获取路由名称
 * @param parentRoutePath - 父路由路径
 * @param fileName - 文件名
 * @returns 路由名称
 */
export function getRouteName(parentRoutePath: string, fileName: string): string;

/**
 * 获取路由路径
 * @param parentRoutePath - 父路由路径
 * @param fileName - 文件名
 * @returns 路由路径
 */
export function getRoutePath(parentRoutePath: string, fileName: string): string;

/**
 * 处理路由
 * @param rotuer - 路由对象
 * @param files - 文件数组
 */
export function handle(rotuer: any, files: Array<any> | any): void;

/**
 * 自动处理
 */
export function auto(): void;

/**
 * 构建路由数组
 * @param views - 视图数组
 * @param $rotuer - 路由对象
 * @param routers - 路由数组
 */
export function buildRouters(views: Array<any>, $rotuer: any, routers: Array<any>): void;

/**
 * 自动处理路由
 * @param files - 文件数组
 * @returns 路由数组
 */
export function handleAuto(files: Array<any> | any): Array<any>;

/**
 * 默认导出对象
 */
declare const _default: {
  handle: typeof handle;
  handleAuto: typeof handleAuto;
};
export default _default;