/**
 * aria-utils - ARIA 无障碍访问工具集
 * (c) 2020 lincong1987
 * @description 提供 ARIA 无障碍访问相关的工具函数，用于改善残障用户的网页体验
 */

var aria = aria || {};

/**
 * @namespace aria.Utils
 * @desc ARIA 工具函数集合
 */
aria.Utils = aria.Utils || {};

/**
 * @desc 设置焦点到后代节点，直到找到第一个可聚焦的元素
 * @param {Element} element - 要查找第一个可聚焦后代的 DOM 节点
 * @returns {boolean} 如果找到可聚焦元素并设置焦点则返回 true
 * @example
 * // 设置焦点到容器内的第一个可聚焦元素
 * aria.Utils.focusFirstDescendant(document.getElementById('container'));
 */
aria.Utils.focusFirstDescendant = function(element) {
  for (var i = 0; i < element.childNodes.length; i++) {
    var child = element.childNodes[i];
    if (aria.Utils.attemptFocus(child) || aria.Utils.focusFirstDescendant(child)) {
      return true;
    }
  }
  return false;
};

/**
 * @desc 查找最后一个可聚焦的后代节点
 * @param {Element} element - 要查找最后一个可聚焦后代的 DOM 节点
 * @returns {boolean} 如果找到可聚焦元素并设置焦点则返回 true
 * @example
 * // 设置焦点到容器内的最后一个可聚焦元素
 * aria.Utils.focusLastDescendant(document.getElementById('container'));
 */
aria.Utils.focusLastDescendant = function(element) {
  for (var i = element.childNodes.length - 1; i >= 0; i--) {
    var child = element.childNodes[i];
    if (aria.Utils.attemptFocus(child) || aria.Utils.focusLastDescendant(child)) {
      return true;
    }
  }
  return false;
};

/**
 * @desc 尝试设置焦点到当前节点
 * @param {Element} element - 要尝试设置焦点的节点
 * @returns {boolean} 如果元素获得焦点则返回 true
 * @example
 * // 尝试设置焦点到指定元素
 * aria.Utils.attemptFocus(document.getElementById('button'));
 */
aria.Utils.attemptFocus = function(element) {
  if (!aria.Utils.isFocusable(element)) {
    return false;
  }
  aria.Utils.IgnoreUtilFocusChanges = true;
  try {
    element.focus();
  } catch (e) {
  }
  aria.Utils.IgnoreUtilFocusChanges = false;
  return (document.activeElement === element);
};

/**
 * @desc 检查元素是否可聚焦
 * @param {Element} element - 要检查的元素
 * @returns {boolean} 如果元素可聚焦则返回 true
 * @example
 * // 检查元素是否可聚焦
 * if (aria.Utils.isFocusable(document.getElementById('input'))) {
 *   console.log('元素可聚焦');
 * }
 */
aria.Utils.isFocusable = function(element) {
  if (element.tabIndex > 0 || (element.tabIndex === 0 && element.getAttribute('tabIndex') !== null)) {
    return true;
  }

  if (element.disabled) {
    return false;
  }

  switch (element.nodeName) {
    case 'A':
      return !!element.href && element.rel !== 'ignore';
    case 'INPUT':
      return element.type !== 'hidden' && element.type !== 'file';
    case 'BUTTON':
    case 'SELECT':
    case 'TEXTAREA':
      return true;
    default:
      return false;
  }
};

/**
 * @desc 触发一个事件
 * @param {Element} elm - 要触发事件的元素
 * @param {String} name - 事件名称，如 mouseenter, mouseleave, mouseover, keyup, change, click 等
 * @param {...*} opts - 传递给事件的选项参数
 * @returns {Element} 返回触发事件的元素
 * @example
 * // 触发点击事件
 * aria.Utils.triggerEvent(button, 'click');
 * 
 * // 触发带有选项的键盘事件
 * aria.Utils.triggerEvent(input, 'keydown', false, true, false, 13);
 */
aria.Utils.triggerEvent = function(elm, name, ...opts) {
  let eventName;

  if (/^mouse|click/.test(name)) {
    eventName = 'MouseEvents';
  } else if (/^key/.test(name)) {
    eventName = 'KeyboardEvent';
  } else {
    eventName = 'HTMLEvents';
  }
  const evt = document.createEvent(eventName);

  evt.initEvent(name, ...opts);
  elm.dispatchEvent
    ? elm.dispatchEvent(evt)
    : elm.fireEvent('on' + name, evt);

  return elm;
};

/**
 * @desc 常用键盘按键码映射
 * @property {number} tab - Tab 键 (9)
 * @property {number} enter - Enter 键 (13)
 * @property {number} space - 空格键 (32)
 * @property {number} left - 左箭头键 (37)
 * @property {number} up - 上箭头键 (38)
 * @property {number} right - 右箭头键 (39)
 * @property {number} down - 下箭头键 (40)
 * @property {number} esc - Esc 键 (27)
 * @example
 * // 检查按键是否为 Enter
 * if (keyCode === aria.Utils.keys.enter) {
 *   // 处理 Enter 键按下事件
 * }
 */
aria.Utils.keys = {
  tab: 9,
  enter: 13,
  space: 32,
  left: 37,
  up: 38,
  right: 39,
  down: 40,
  esc: 27
};

export default aria.Utils;