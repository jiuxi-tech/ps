/**
 * aria-utils - ARIA 无障碍访问工具集
 * (c) 2020 lincong1987
 * @description 提供 ARIA 无障碍访问相关的工具函数，用于改善残障用户的网页体验
 */

/**
 * ARIA 工具函数集合
 */
export interface AriaUtils {
  /**
   * 设置焦点到后代节点，直到找到第一个可聚焦的元素
   * 
   * @param element - 要查找第一个可聚焦后代的 DOM 节点
   * @returns 如果找到可聚焦元素并设置焦点则返回 true
   * @example
   * // 设置焦点到容器内的第一个可聚焦元素
   * aria.Utils.focusFirstDescendant(document.getElementById('container'));
   */
  focusFirstDescendant(element: Element): boolean;

  /**
   * 查找最后一个可聚焦的后代节点
   * 
   * @param element - 要查找最后一个可聚焦后代的 DOM 节点
   * @returns 如果找到可聚焦元素并设置焦点则返回 true
   * @example
   * // 设置焦点到容器内的最后一个可聚焦元素
   * aria.Utils.focusLastDescendant(document.getElementById('container'));
   */
  focusLastDescendant(element: Element): boolean;

  /**
   * 尝试设置焦点到当前节点
   * 
   * @param element - 要尝试设置焦点的节点
   * @returns 如果元素获得焦点则返回 true
   * @example
   * // 尝试设置焦点到指定元素
   * aria.Utils.attemptFocus(document.getElementById('button'));
   */
  attemptFocus(element: Element): boolean;

  /**
   * 检查元素是否可聚焦
   * 
   * @param element - 要检查的元素
   * @returns 如果元素可聚焦则返回 true
   * @example
   * // 检查元素是否可聚焦
   * if (aria.Utils.isFocusable(document.getElementById('input'))) {
   *   console.log('元素可聚焦');
   * }
   */
  isFocusable(element: Element): boolean;

  /**
   * 触发一个事件
   * 
   * @param elm - 要触发事件的元素
   * @param name - 事件名称，如 mouseenter, mouseleave, mouseover, keyup, change, click 等
   * @param opts - 传递给事件的选项参数
   * @returns 返回触发事件的元素
   * @example
   * // 触发点击事件
   * aria.Utils.triggerEvent(button, 'click');
   * 
   * // 触发带有选项的键盘事件
   * aria.Utils.triggerEvent(input, 'keydown', false, true, false, 13);
   */
  triggerEvent(elm: Element, name: string, ...opts: any[]): Element;

  /**
   * 常用键盘按键码映射
   * 
   * @property tab - Tab 键 (9)
   * @property enter - Enter 键 (13)
   * @property space - 空格键 (32)
   * @property left - 左箭头键 (37)
   * @property up - 上箭头键 (38)
   * @property right - 右箭头键 (39)
   * @property down - 下箭头键 (40)
   * @property esc - Esc 键 (27)
   * @example
   * // 检查按键是否为 Enter
   * if (keyCode === aria.Utils.keys.enter) {
   *   // 处理 Enter 键按下事件
   * }
   */
  keys: {
    tab: number;
    enter: number;
    space: number;
    left: number;
    up: number;
    right: number;
    down: number;
    esc: number;
  };

  /**
   * 用于忽略焦点变化的标志
   */
  IgnoreUtilFocusChanges?: boolean;
}

/**
 * ARIA 工具函数默认导出
 */
declare const ariaUtils: AriaUtils;

export default ariaUtils;