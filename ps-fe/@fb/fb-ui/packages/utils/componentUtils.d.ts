/**
 * componentUtils - Vue 组件工具函数集
 * (c) 2020 lincong1987
 */

import { typeOf } from './commonUtils'
import Vue from 'vue'

/**
 * Vue 组件工具函数集合
 */

/**
 * 向上查找组件
 * 
 * @param context - 当前组件上下文
 * @param componentName - 要查找的组件名称或名称数组
 * @param level - 查找层级，默认为 0（查找所有父级）
 * @returns 返回找到的组件实例，未找到则返回 null
 * @example
 * // 查找名为 'FbButton' 的父组件
 * const button = closest(this, 'FbButton');
 * 
 * // 查找名为 'FbButton' 或 'FbLink' 的父组件
 * const component = closest(this, ['FbButton', 'FbLink']);
 * 
 * // 只查找直接父级中名为 'FbButton' 的组件
 * const parentButton = closest(this, 'FbButton', 1);
 */
export function closest(
  context: Vue,
  componentName: string | string[],
  level?: number
): Vue | null;

/**
 * 向下查找组件
 * 
 * @param context - 当前组件上下文
 * @param componentName - 要查找的组件名称或名称数组
 * @returns 返回找到的组件实例，未找到则返回 null
 * @example
 * // 查找名为 'FbButton' 的子组件
 * const button = find(this, 'FbButton');
 * 
 * // 查找名为 'FbButton' 或 'FbLink' 的子组件
 * const component = find(this, ['FbButton', 'FbLink']);
 */
export function find(
  context: Vue,
  componentName: string | string[]
): Vue | null;

/**
 * 查找直接上级组件
 * 
 * @param context - 当前组件上下文
 * @param componentName - 要查找的组件名称
 * @returns 返回找到的组件实例，未找到则返回 undefined
 * @example
 * // 查找直接父级中名为 'FbButton' 的组件
 * const parentButton = parent(this, 'FbButton');
 */
export function parent(
  context: Vue,
  componentName: string
): Vue | undefined;

/**
 * 判断节点是否为空元素
 * 
 * @param node - 虚拟节点
 * @returns 如果节点为空元素则返回 true
 * @example
 * // 判断节点是否为空
 * if (isEmptyElement(vnode)) {
 *   console.log('节点为空');
 * }
 */
export function isEmptyElement(node: Vue.VNode): boolean;

/**
 * 过滤空元素
 * 
 * @param children - 子节点数组
 * @returns 返回过滤后的子节点数组
 * @example
 * // 过滤空元素
 * const filteredChildren = filterEmpty(this.$slots.default);
 */
export function filterEmpty(children?: Vue.VNode[]): Vue.VNode[];

/**
 * ComponentUtils 默认导出
 */
export interface ComponentUtils {
  filterEmpty: typeof filterEmpty;
  parent: typeof parent;
  closest: typeof closest;
  find: typeof find;
  typeOf: typeof typeOf;
  isEmptyElement: typeof isEmptyElement;
}

declare const componentUtils: ComponentUtils;

export default componentUtils;