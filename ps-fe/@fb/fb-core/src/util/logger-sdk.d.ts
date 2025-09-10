/**
 * 日志SDK模块
 * @module util/logger-sdk
 */

/**
 * Web日志类
 */
export class WebLogger {
  /**
   * 构造函数
   * @param data - 配置数据
   */
  constructor(data?: any);

  /**
   * 日志服务地址
   */
  url: string;

  /**
   * 当前项目名
   */
  project: string;

  /**
   * 发送埋点数据
   * @param data - 埋点数据
   */
  send(data: {
    moduleCode: string;
    moduleName?: string;
    operateType: string;
    operterId: string;
    operterName: string;
  }): void;

  /**
   * 统计当前登录人数-心跳
   * @param token - 令牌
   * @param data - 数据
   * @param time - 定时时间
   */
  session(token: string, data: { operterName: string }, time?: number): void;

  /**
   * 获取当前登录人数
   * @param callback - 回调方法
   * @param time - 定时时间
   */
  online(callback: (data: any) => void, time?: number): void;
}

/**
 * 安装函数
 * @param adapter - 适配器
 */
export const install: (adapter: any) => void;

/**
 * 默认导出 WebLogger 类
 */
export default WebLogger;