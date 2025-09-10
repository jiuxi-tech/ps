/**
 * emitter
 * Vue 组件间通信混入
 */

import { ComponentOptions } from 'vue';

export interface EmitterMethods {
  /**
   * 事件冒泡
   * @param componentName 组件名称
   * @param eventName 事件名称
   * @param params 事件参数
   */
  dispatch(componentName: string, eventName: string, params: any[]): void;
  
  /**
   * 广播事件
   * @param componentName 组件名称
   * @param eventName 事件名称
   * @param params 事件参数
   */
  broadcast(componentName: string, eventName: string, params: any[]): void;
}

export interface EmitterMixin {
  methods: EmitterMethods;
}

declare const emitter: EmitterMixin;

export default emitter;