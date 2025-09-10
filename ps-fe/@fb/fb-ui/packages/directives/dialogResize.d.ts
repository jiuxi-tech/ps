/**
 * dialogResize
 * (c) 2022 lincong1987
 */

import { VNode } from 'vue';

export interface DialogResizeValue {
  $refs: {
    dialog: HTMLElement;
  };
  width: string | number;
  height: string | number;
  myWidth: string | number;
  myHeight: string | number;
}

export interface DialogResizeBinding {
  value: DialogResizeValue;
  oldValue: any;
}

export interface DialogResizeDirective {
  update(el: HTMLElement, binding: DialogResizeBinding, vnode: VNode, oldVnode: VNode): void;
}

declare const dialogResize: DialogResizeDirective;

export default dialogResize;