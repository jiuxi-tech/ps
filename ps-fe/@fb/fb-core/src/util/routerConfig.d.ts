/**
 * 路由配置模块
 * @module util/routerConfig
 */

/**
 * 设置路由前置守卫
 * @param to - 目标路由
 * @param from - 来源路由
 * @param next - 下一步函数
 */
export function setRouterBefore(to: any, from: any, next: Function): void;