/**
 * clickoutside - 点击元素外部指令
 * @description Vue 自定义指令，用于检测用户是否点击了元素外部区域
 */

import Vue from 'vue';
import { on } from './dom';

/**
 * 点击元素外部处理工具
 */
export interface ClickOutside {
  /**
   * 指令绑定时调用
   * 
   * @param el - 指令绑定的元素
   * @param binding - 指令绑定对象
   * @param vnode - Vue 虚拟节点
   */
  bind(
    el: HTMLElement,
    binding: Vue.DirectiveBinding,
    vnode: Vue.VNode
  ): void;

  /**
   * 指令更新时调用
   * 
   * @param el - 指令绑定的元素
   * @param binding - 指令绑定对象
   * @param vnode - Vue 虚拟节点
   */
  update(
    el: HTMLElement,
    binding: Vue.DirectiveBinding,
    vnode: Vue.VNode
  ): void;

  /**
   * 指令解绑时调用
   * 
   * @param el - 指令绑定的元素
   */
  unbind(el: HTMLElement): void;
}

/**
 * 点击元素外部上下文信息
 */
interface ClickOutsideContext {
  id: number;
  documentHandler: (mouseup?: MouseEvent, mousedown?: MouseEvent) => void;
  methodName: string;
  bindingFn: Function;
}

/**
 * v-clickoutside - 点击元素外面才会触发的事件
 * 
 * @desc Vue 自定义指令，当用户点击元素外部时触发指定方法
 * @example
 * ```vue
 * <div v-clickoutside="handleClose">
 * ```
 */
declare const clickOutside: ClickOutside;

export default clickOutside;