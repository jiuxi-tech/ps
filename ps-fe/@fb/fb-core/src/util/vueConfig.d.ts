/**
 * Vue配置模块
 * @module util/vueConfig
 */

/**
 * 解析别名
 * @param pkg - package.json对象
 * @param config - 配置对象
 * @returns 别名对象
 */
export function parseAlias(pkg: any, config: any): any;

/**
 * 解析代理配置
 * @param config - 配置对象
 * @returns 代理配置对象
 */
export function parseProxy(config: any): any;

/**
 * 解析页面配置
 * @param config - 配置对象
 * @returns 页面配置对象
 */
export function parsePages(config: any): any;

/**
 * 解析复制配置
 * @param pkg - package.json对象
 * @param config - 配置对象
 * @returns 复制配置数组
 */
export function parseCopy(pkg: any, config: any): Array<any>;

/**
 * 获取插件
 * @param pkg - package.json对象
 * @param config - 配置对象
 * @returns 插件数组
 */
export function getPlugin(pkg: any, config: any): Array<any>;

/**
 * 检查是否有多核
 * @returns 是否有多核
 */
export function hasMultipleCores(): boolean;

/**
 * Vue配置函数
 * @param path - 项目路径
 * @param pkg - package.json对象
 * @param fbconfig - fb配置对象
 * @param config - 配置对象
 * @returns Vue配置对象
 */
export function VueConfig(path: string, pkg: any, fbconfig: any, config?: any): any;

/**
 * 默认导出对象
 */
declare const _default: {
  VueConfig: typeof VueConfig;
};
export default _default;