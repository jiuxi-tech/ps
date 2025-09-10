/**
 * ripple
 * (c) 2022 lincong1987
 */

import { VNode } from 'vue';

export interface RippleBinding {
  value: any;
  oldValue: any;
}

export interface RippleDirective {
  inserted(el: HTMLElement, binding: RippleBinding, vnode: VNode, oldVnode: VNode): void;
  update(el: HTMLElement, binding: RippleBinding, vnode: VNode, oldVnode: VNode): void;
  unbind(el: HTMLElement, binding: RippleBinding, vnode: VNode, oldVnode: VNode): void;
}

declare const ripple: RippleDirective;

export default ripple;