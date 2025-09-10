/**
 * repeat-click
 * (c) 2022 lincong1987
 */

import { VNode } from 'vue';

export interface RepeatClickBinding {
  expression: string;
  value: any;
  oldValue: any;
}

export interface RepeatClickDirective {
  bind(el: HTMLElement, binding: RepeatClickBinding, vnode: VNode, oldVnode: VNode): void;
}

declare const repeatClick: RepeatClickDirective;

export default repeatClick;