/**
 * 路由工具模块
 * @module util/routerTool
 */

/**
 * 自动路由排序
 * @param routes - 路由数组
 * @returns 排序后的路由数组
 */
export function autoRouter(routes: Array<any>): Array<any>;

/**
 * 注入路由
 * @param mainRouter - 主路由
 * @param child - 子路由
 */
export function injectRouter(mainRouter: any, child: any): void;

/**
 * 处理路由
 * @param systemRouter - 系统路由
 * @param files - 文件数组
 * @returns 处理后的路由数组
 */
export function handle(systemRouter: any, files: Array<any> | any): Array<any>;

/**
 * 初始化路由白名单
 * @param indexPath - 索引路径
 * @param files - 文件数组
 * @returns 路由数组
 */
export function initRoutesWhite(indexPath: string, files: Array<any> | any): Array<any>;

/**
 * 初始化路由
 * @param constantRoutes - 常量路由
 * @param flatRoutes - 平铺路由集合
 * @param mainPath - 主路径
 * @returns 路由数组
 */
export function initRoutes(constantRoutes: any, flatRoutes: Array<any>, mainPath: string): Array<any>;

/**
 * 平铺路由文件及获取主路由对象
 * @param indexPath - 索引路径
 * @param files - 文件数组
 * @returns 包含常量路由和平铺路由的对象
 */
export function flatFilesToRouteArr(indexPath: string, files: Array<any> | any): {
  constantRoutes: any;
  flatRoutes: Array<any>;
};

/**
 * 默认导出对象
 */
declare const _default: {
  handle: typeof handle;
  autoRouter: typeof autoRouter;
  injectRouter: typeof injectRouter;
  initRoutesWhite: typeof initRoutesWhite;
  initRoutes: typeof initRoutes;
  flatFilesToRouteArr: typeof flatFilesToRouteArr;
};
export default _default;