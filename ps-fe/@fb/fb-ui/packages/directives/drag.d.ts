/**
 * drag
 * (c) 2022 lincong1987
 */

import { VNode } from 'vue';

export interface DragValue {
  handle?: string;
  el?: string;
}

export interface DragBinding {
  value: DragValue;
  oldValue: any;
}

export interface DragDirective {
  bind(el: HTMLElement, binding: DragBinding, vnode: VNode, oldVnode: VNode): void;
}

declare const drag: DragDirective;

export default drag;