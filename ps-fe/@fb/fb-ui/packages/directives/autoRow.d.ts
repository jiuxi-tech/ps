/**
 * autoRow
 * (c) 2020 lincong1987
 */

import { VNode } from 'vue';

export interface AutoRowBinding {
  value: boolean | {
    min: number;
    max: number;
  };
  oldValue: any;
}

export interface AutoRowDirective {
  inserted(el: HTMLElement, binding: AutoRowBinding, vnode: VNode, oldVnode: VNode): void;
  update(el: HTMLElement, binding: AutoRowBinding, vnode: VNode, oldVnode: VNode): void;
  unbind(el: HTMLElement, binding: AutoRowBinding, vnode: VNode, oldVnode: VNode): void;
}

declare const autoRow: AutoRowDirective;

export default autoRow;