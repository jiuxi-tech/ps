/**
 * resize - 元素尺寸变化监听工具
 * (c) 2020 lincong1987
 */

/**
 * 元素尺寸变化监听工具函数集合
 */

/**
 * 监听元素尺寸变化
 * 
 * @param element - 要监听的元素
 * @param fn - 尺寸变化回调函数
 * @example
 * // 监听元素尺寸变化
 * resize(element, function(e) {
 *   console.log('元素尺寸发生了变化');
 * });
 */
declare function resize(element: Element, fn: Function): void;

/**
 * 解除元素尺寸变化监听
 * 
 * @param element - 要解除监听的元素
 * @param fn - 要解除的回调函数，如果不传则解除所有监听
 * @example
 * // 解除特定回调函数的监听
 * resize.unbind(element, callback);
 * 
 * // 解除所有监听
 * resize.unbind(element);
 */
declare namespace resize {
  function unbind(element: Element, fn?: Function): void;
}

export default resize;