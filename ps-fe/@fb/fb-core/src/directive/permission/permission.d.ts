/**
 * 权限指令模块
 * @module directive/permission/permission
 */

/**
 * 检查是否有权限
 * @param value - 权限值
 * @returns Promise<boolean> 是否有权限
 */
export function hasPermission(value: string): Promise<boolean>;

/**
 * 同步检查是否有权限
 * @param value - 权限值
 * @returns boolean 是否有权限
 */
export function hasPermissionSync(value: string): boolean;

/**
 * 默认导出对象
 */
declare const _default: {
  inserted(el: HTMLElement, binding: any, vnode: any): void;
  update(el: HTMLElement, binding: any, vnode: any): void;
  unbind(el: HTMLElement, binding: any): void;
};
export default _default;