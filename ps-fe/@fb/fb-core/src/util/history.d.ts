/**
 * 历史记录模块
 * @module util/history
 */

/**
 * 历史记录对象
 */
export const history: {
  /**
   * 历史数据数组
   */
  data: Array<any>;

  /**
   * 当前历史记录
   */
  current: any;

  /**
   * 记录历史
   * @param href - 链接地址
   */
  record(href: string): void;
};

/**
 * 默认导出历史记录对象
 */
export default history;