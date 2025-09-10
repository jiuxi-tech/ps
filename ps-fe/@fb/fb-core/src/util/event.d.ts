/**
 * 事件代理模块
 * @module util/event
 */

/**
 * 事件代理对象
 */
export const eventProxy: {
  /**
   * 持续监听事件的对象
   */
  onObj: any;

  /**
   * 单次监听事件的对象
   */
  oneObj: any;

  /**
   * 添加持续监听事件
   * @param key - 事件键名
   * @param fn - 回调函数
   */
  on(key: string, fn: Function): void;

  /**
   * 添加单次监听事件
   * @param key - 事件键名
   * @param fn - 回调函数
   */
  one(key: string, fn: Function): void;

  /**
   * 移除事件监听
   * @param key - 事件键名
   */
  off(key: string): void;

  /**
   * 触发事件
   * @param args - 参数数组
   * @returns null
   */
  trigger(...args: any[]): any;
};

/**
 * 默认导出事件代理对象
 */
export default eventProxy;