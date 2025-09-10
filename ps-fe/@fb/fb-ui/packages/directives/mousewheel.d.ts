/**
 * mousewheel
 * (c) 2022 lincong1987
 */

import { VNode } from 'vue';
import { NormalizedWheelEvent } from 'normalize-wheel';

export interface MousewheelBinding {
  value: (event: Event, normalized: NormalizedWheelEvent) => void;
  oldValue: any;
}

export interface MousewheelDirective {
  bind(el: HTMLElement, binding: MousewheelBinding, vnode: VNode, oldVnode: VNode): void;
  unbind(el: HTMLElement, binding: MousewheelBinding, vnode: VNode, oldVnode: VNode): void;
}

declare const mousewheel: MousewheelDirective;

export default mousewheel;