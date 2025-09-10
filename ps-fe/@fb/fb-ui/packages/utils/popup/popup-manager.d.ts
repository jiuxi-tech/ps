import Vue from 'vue';
import { addClass, removeClass } from '../dom';

/**
 * 弹出层管理器
 * 
 * @description 管理弹出层实例和遮罩层的工具
 */

/**
 * 弹出层实例接口
 */
export interface PopupInstance {
  closeOnClickModal?: boolean;
  closeOnPressEscape?: boolean;
  close?: () => void;
  handleClose?: () => void;
  handleAction?: (action: string) => void;
  [key: string]: any;
}

/**
 * 遮罩层堆栈项
 */
export interface ModalStackItem {
  id: string;
  zIndex: number;
  modalClass?: string;
}

/**
 * PopupManager 弹出层管理器
 */
export interface PopupManager {
  /**
   * 遮罩层是否淡入淡出
   * @default true
   */
  modalFade: boolean;

  /**
   * 遮罩层堆栈
   */
  modalStack: ModalStackItem[];

  /**
   * 遮罩层 DOM 元素
   */
  modalDom?: HTMLElement;

  /**
   * z-index 值
   */
  zIndex: number;

  /**
   * 获取弹出层实例
   * 
   * @param id - 实例 ID
   * @returns 返回弹出层实例
   * @example
   * // 获取弹出层实例
   * const instance = PopupManager.getInstance('popup-1');
   */
  getInstance(id: string): PopupInstance | undefined;

  /**
   * 注册弹出层实例
   * 
   * @param id - 实例 ID
   * @param instance - 弹出层实例
   * @example
   * // 注册弹出层实例
   * PopupManager.register('popup-1', popupInstance);
   */
  register(id: string, instance: PopupInstance): void;

  /**
   * 注销弹出层实例
   * 
   * @param id - 实例 ID
   * @example
   * // 注销弹出层实例
   * PopupManager.deregister('popup-1');
   */
  deregister(id: string): void;

  /**
   * 获取下一个 z-index 值
   * 
   * @returns 返回下一个 z-index 值
   * @example
   * // 获取下一个 z-index 值
   * const zIndex = PopupManager.nextZIndex();
   */
  nextZIndex(): number;

  /**
   * 处理遮罩层点击事件
   * @example
   * // 处理遮罩层点击事件
   * PopupManager.doOnModalClick();
   */
  doOnModalClick(): void;

  /**
   * 打开遮罩层
   * 
   * @param id - 实例 ID
   * @param zIndex - z-index 值
   * @param dom - DOM 元素
   * @param modalClass - 遮罩层类名
   * @param modalFade - 是否淡入淡出
   * @example
   * // 打开遮罩层
   * PopupManager.openModal('popup-1', 2000, element, 'custom-class', true);
   */
  openModal(
    id: string,
    zIndex: number,
    dom?: HTMLElement,
    modalClass?: string,
    modalFade?: boolean
  ): void;

  /**
   * 关闭遮罩层
   * 
   * @param id - 实例 ID
   * @example
   * // 关闭遮罩层
   * PopupManager.closeModal('popup-1');
   */
  closeModal(id: string): void;
}

/**
 * PopupManager 默认导出
 */
declare const popupManager: PopupManager;

export default popupManager;