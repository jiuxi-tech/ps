/**
 * dialogDnd
 * (c) 2022 lincong1987
 */

import { VNode } from 'vue';

export interface DialogDndBinding {
  value: boolean | {
    enable: boolean;
  };
  oldValue: any;
}

export interface DialogDndDirective {
  bind(el: HTMLElement, binding: DialogDndBinding, vnode: VNode, oldVnode: VNode): void;
}

declare const dialogDnd: DialogDndDirective;

export default dialogDnd;