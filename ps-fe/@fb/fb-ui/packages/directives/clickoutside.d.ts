/**
 * clickoutside
 * (c) 2020 lincong1987
 */

import { VNode } from 'vue';

export interface ClickOutsideBinding {
  value: () => void;
  oldValue: any;
}

export interface ClickOutsideDirective {
  inserted(el: HTMLElement, binding: ClickOutsideBinding, vnode: VNode, oldVnode: VNode): void;
  unbind(el: HTMLElement, binding: ClickOutsideBinding, vnode: VNode, oldVnode: VNode): void;
}

declare const clickoutside: ClickOutsideDirective;

export default clickoutside;