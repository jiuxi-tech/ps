/**
 * Vue 配置模块
 * @module util/defineVueConfig
 */

/**
 * 解析页面配置
 * @param projectConfig - 项目配置对象
 * @returns 页面配置对象
 */
export function getPages(projectConfig: any): any;

/**
 * 查找模块
 * @param pkg - package.json 对象
 * @param projectConfig - 项目配置对象
 * @returns 模块对象
 */
export function findModules(pkg: any, projectConfig: any): any;

/**
 * 解析别名
 * @param pkg - package.json 对象
 * @param projectConfig - 项目配置对象
 * @returns 别名对象
 */
export function parseAlias(pkg: any, projectConfig: any): any;

/**
 * 解析代理配置
 * @param projectConfig - 项目配置对象
 * @returns 代理配置对象
 */
export function parseProxy(projectConfig: any): any;

/**
 * 解析页面入口配置
 * @param projectConfig - 项目配置对象
 * @returns 页面入口配置对象
 */
export function parsePages(projectConfig: any): any;

/**
 * 解析复制配置
 * @param pkg - package.json 对象
 * @param projectConfig - 项目配置对象
 * @returns 复制配置数组
 */
export function parseCopy(pkg: any, projectConfig: any): Array<any>;

/**
 * 获取插件
 * @param pkg - package.json 对象
 * @param projectConfig - 项目配置对象
 * @returns 插件数组
 */
export function getPlugin(pkg: any, projectConfig: any): Array<any>;

/**
 * 检查是否有多核
 * @returns 是否有多核
 */
export function hasMultipleCores(): boolean;

/**
 * Vue 配置生成器
 * @param path - 路径
 * @param pkg - package.json 对象
 * @param projectConfig - 项目配置对象
 * @param options - 配置选项对象
 * @returns Vue 配置对象
 */
export function DefineVueConfig(path: string, pkg: any, projectConfig: any, options?: any): any;