/**
 * 存储模块
 * @module storage/index
 */

/**
 * Cookie 操作对象
 */
export const cookies: {
  /**
   * 获取 Cookie 值
   * @param sKey - 键名
   * @returns Cookie 值
   */
  getItem(sKey: string): string | null;

  /**
   * 获取所有 Cookie
   * @returns 所有 Cookie 对象
   */
  getAll(): any;

  /**
   * 设置 Cookie 值
   * @param sKey - 键名
   * @param sValue - 值
   * @param vEnd - 过期时间
   * @param sPath - 路径
   * @param sDomain - 域名
   * @param bSecure - 是否安全
   * @returns 是否设置成功
   */
  setItem(sKey: string, sValue: string, vEnd?: any, sPath?: string, sDomain?: string, bSecure?: boolean): boolean;

  /**
   * 移除 Cookie
   * @param sKey - 键名
   * @param sPath - 路径
   * @param sDomain - 域名
   * @returns 是否移除成功
   */
  removeItem(sKey: string, sPath?: string, sDomain?: string): boolean;

  /**
   * 检查 Cookie 是否存在
   * @param sKey - 键名
   * @returns 是否存在
   */
  hasItem(sKey: string): boolean;

  /**
   * 获取所有 Cookie 键名
   * @returns 键名数组
   */
  keys(): Array<string>;
};

/**
 * 存储管理器
 */
export const storageManager: {
  /**
   * 设置存储值
   * @param key - 键名
   * @param value - 值
   * @param storage - 存储类型
   */
  set(key: string, value: any, storage: string): void;

  /**
   * 获取存储值
   * @param key - 键名
   * @param storage - 存储类型
   * @returns 存储值
   */
  get(key: string, storage: string): any;

  /**
   * 清空存储
   * @param storage - 存储类型
   */
  clear(storage: string): void;

  /**
   * 移除存储值
   * @param key - 键名
   * @param storage - 存储类型
   */
  remove(key: string, storage: string): void;

  /**
   * 获取所有存储值
   * @param storage - 存储类型
   * @returns 所有存储值对象
   */
  getAll(storage: string): any;
};

/**
 * Cookie 管理器
 */
export const cookieManager: {
  /**
   * 设置 Cookie 值
   * @param key - 键名
   * @param value - 值
   * @param expired - 过期时间
   */
  set(key: string, value: string, expired?: any): void;

  /**
   * 获取 Cookie 值
   * @param key - 键名
   * @returns Cookie 值
   */
  get(key: string): string | null;

  /**
   * 清空所有 Cookie
   */
  clear(): void;

  /**
   * 移除 Cookie
   * @param key - 键名
   */
  remove(key: string): void;

  /**
   * 获取所有 Cookie
   * @returns 所有 Cookie 对象
   */
  getAll(): any;
};

/**
 * 存储类型常量
 */
export const SESSION: string;
export const LOCAL: string;
export const COOKIE: string;

/**
 * 统一存储管理器类
 */
export class Manager {
  /**
   * 构造函数
   * @param category - 存储类型
   */
  constructor(category?: string);

  /**
   * 存储类型
   */
  category: string;

  /**
   * 设置存储值
   * @param key - 键名
   * @param value - 值
   * @param category - 存储类型
   * @param expired - 过期时间
   */
  set(key: string, value: any, category?: string, expired?: any): void;

  /**
   * 获取存储值
   * @param key - 键名
   * @param category - 存储类型
   * @returns 存储值
   */
  get(key: string, category?: string): any;

  /**
   * 获取所有存储值
   * @param category - 存储类型
   * @returns 所有存储值对象
   */
  getAll(category?: string): any;

  /**
   * 清空存储
   * @param category - 存储类型
   */
  clear(category?: string): void;

  /**
   * 移除存储值
   * @param key - 键名
   * @param category - 存储类型
   */
  remove(key: string, category?: string): void;

  /**
   * 映射存储类型
   * @param category - 存储类型
   * @returns 映射结果
   */
  _map(category: string): {
    isWebStorage: boolean;
    storage: string;
  };
}

/**
 * 默认导出对象
 */
declare const _default: {
  Manager: typeof Manager;
  storageManager: typeof storageManager;
  cookieManager: typeof cookieManager;
  cookies: typeof cookies;
};
export default _default;