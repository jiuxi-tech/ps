/**
 * focus
 * 焦点管理混入
 */

import { ComponentOptions } from 'vue';

export interface FocusMethods {
  /**
   * 聚焦到指定元素
   */
  focus(): void;
}

export interface FocusMixin {
  methods: FocusMethods;
}

/**
 * 创建一个焦点管理混入
 * @param ref 需要聚焦的元素的引用名称
 * @returns 包含focus方法的混入对象
 */
export default function focus(ref: string): FocusMixin;