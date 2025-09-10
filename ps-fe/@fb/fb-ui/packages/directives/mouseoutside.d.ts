/**
 * mouseoutside
 * (c) 2020 lincong1987
 */

import { VNode } from 'vue';

export interface MouseOutsideBinding {
  value: () => void;
  oldValue: any;
}

export interface MouseOutsideDirective {
  inserted(el: HTMLElement, binding: MouseOutsideBinding, vnode: VNode, oldVnode: VNode): void;
  unbind(el: HTMLElement, binding: MouseOutsideBinding, vnode: VNode, oldVnode: VNode): void;
}

declare const mouseoutside: MouseOutsideDirective;

export default mouseoutside;