/**
 * RsBuild 配置模块
 * @module util/RsConfig
 */

import { RsbuildConfig } from '@rsbuild/core';

/**
 * 解析别名
 * @param pages - 页面配置对象
 * @param __dirname - 当前目录路径
 * @returns 别名对象
 */
export function parseAlias(pages: any, __dirname: string): any;

/**
 * 解析页面配置
 * @param pages - 页面配置对象
 * @returns 解析后的页面配置对象
 */
export function parsePages(pages: any): {
  entry: any;
  template: any;
  title: any;
};

/**
 * 解析代理配置
 * @param services - 服务配置数组
 * @returns 代理配置对象
 */
export function parseProxy(services: Array<any>): any;

/**
 * RsBuild 配置生成器
 * @param __dirname - 当前目录路径
 * @param pkg - package.json 对象
 * @param projectConfig - 项目配置对象
 * @param output - 输出配置对象
 * @param plugins - 插件数组
 * @param option - 配置选项对象
 * @returns Rsbuild 配置对象
 */
export function RsDefineConfig(
  __dirname: string,
  pkg: any,
  projectConfig?: any,
  output?: any,
  plugins?: Array<any>,
  option?: any
): RsbuildConfig;