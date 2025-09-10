/**
 * 自动高度指令模块
 * @module directive/autoheight
 */

/**
 * ResizeController 类
 */
export class ResizeController {
  /**
   * 构造函数
   */
  constructor();

  /**
   * 默认配置
   * @param binding - 指令绑定对象
   * @returns 配置对象
   */
  static defaults(binding: any): any;

  /**
   * 销毁方法
   */
  destroy(): void;

  /**
   * 添加任务
   * @param options - 任务选项
   */
  add(options: {
    el: HTMLElement | null;
    value: any;
    enable: boolean | null;
    uuid: string | null;
  }): void;

  /**
   * 添加事件监听
   */
  addEvent(): void;

  /**
   * 移除事件监听
   */
  removeEvent(): void;

  /**
   * 更新任务
   * @param uuid - 任务UUID
   * @param binding - 指令绑定对象
   */
  update(uuid: string, binding: any): void;

  /**
   * 移除任务
   * @param uuid - 任务UUID
   */
  remove(uuid: string): void;

  /**
   * 获取任务
   * @param uuid - 任务UUID
   * @returns 任务对象
   */
  get(uuid: string): any;

  /**
   * 执行调整大小
   * @param uuid - 任务UUID
   */
  doResize(uuid: string): void;
}

/**
 * 默认导出对象
 */
declare const _default: {
  inserted(el: HTMLElement, binding: any, vnode: any): void;
  unbind(el: HTMLElement, binding: any, vnode: any): void;
  update(el: HTMLElement, binding: any, vnode: any): void;
  componentUpdated(): void;
};
export default _default;