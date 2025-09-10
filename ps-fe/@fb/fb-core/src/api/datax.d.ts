/**
 * datax
 * (c) 2020 lincong1987
 */

import { Manager } from '../storage';

interface DataxOptions {
  storage?: Manager;
  Vue?: any;
  name?: string;
}

export default class Datax {
  constructor(storage?: Manager, Vue?: any, name?: string);
  
  storage: Manager;
  name: string;
  pre: string;
  _map: any;
  
  /**
   * 获取存储内容
   * @param prop 
   * @param prefix 
   */
  get(prop: string, prefix?: string): any;
  
  /**
   * 获取全部
   * @param prefix 
   */
  getAll(prefix?: string): any;
  
  /**
   * 设置存储内容
   * @param prop 
   * @param value 
   * @param prefix 
   */
  set(prop: string | any, value?: any, prefix?: string): this;
  
  /**
   * 移除存储内容
   * @param prop 
   * @param prefix 
   */
  remove(prop: string, prefix?: string): this;
  
  /**
   * 清空存储内容
   * @param prefix 
   */
  clear(prefix?: string): void;
  
  /**
   * 销毁实例
   * @param prefix 
   */
  destroy(prefix?: string): void;
}