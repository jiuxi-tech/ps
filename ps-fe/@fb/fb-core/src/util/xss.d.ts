/**
 * XSS过滤模块
 * @module util/xss
 */

import xss from 'xss';

/**
 * XSS过滤器
 */
export const xssFilter: any;

/**
 * 默认导出对象
 */
declare const _default: {
  whiteList: any;
  xssFilter: typeof xssFilter;
};
export default _default;