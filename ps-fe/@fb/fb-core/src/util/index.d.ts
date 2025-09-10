/**
 * util/index.js
 */

interface Utils {
  /**
   * 验证一个path是否可以访问
   * @param path 
   * @param allowPage 
   */
  checkRoute(path: string, allowPage: string[]): boolean;
  
  /**
   * 
   * @param name 
   */
  getParam(name: string): string | null;
  
  removeParam(name: string, content: string): string | boolean;
  
  proxy(proxy: any, prop: string, apiArr: string[]): any;
}

declare const utils: Utils;

export {
  Utils,
  utils
};

export default utils;