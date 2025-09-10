import Vue from 'vue';
import {merge} from 'lodash-es';
import PopupManager from './popup-manager';
// import getScrollBarWidth from '../scrollbar-width';
import { getStyle, addClass, removeClass, hasClass } from '../dom';

/**
 * 弹出层组件
 * 
 * @description 提供弹出层功能的 Vue 组件
 */

export interface Popup {
  /**
   * 组件属性
   */
  props: {
    /**
     * 弹出层是否可见
     * @default false
     */
    visible: boolean;
    
    /**
     * 打开延迟时间（毫秒）
     */
    openDelay: number;
    
    /**
     * 关闭延迟时间（毫秒）
     */
    closeDelay: number;
    
    /**
     * z-index 层级
     */
    zIndex: number;
    
    /**
     * 是否显示遮罩层
     * @default false
     */
    modal: boolean;
    
    /**
     * 遮罩层是否淡入淡出
     * @default true
     */
    modalFade: boolean;
    
    /**
     * 遮罩层自定义类名
     */
    modalClass: string;
    
    /**
     * 遮罩层是否添加到 body
     * @default false
     */
    modalAppendToBody: boolean;
    
    /**
     * 是否锁定滚动
     * @default true
     */
    lockScroll: boolean;
    
    /**
     * 是否按下 ESC 键关闭
     * @default false
     */
    closeOnPressEscape: boolean;
    
    /**
     * 是否点击遮罩层关闭
     * @default false
     */
    closeOnClickModal: boolean;
  };

  /**
   * 组件数据
   */
  data(): {
    /**
     * 弹出层是否已打开
     */
    opened: boolean;
    
    /**
     * body 的 padding-right 值
     */
    bodyPaddingRight: string | null;
    
    /**
     * 计算后的 body padding-right 值
     */
    computedBodyPaddingRight: number;
    
    /**
     * 是否没有隐藏类
     */
    withoutHiddenClass: boolean;
    
    /**
     * 是否已渲染
     */
    rendered: boolean;
  };

  /**
   * 组件生命周期钩子
   */
  beforeMount(): void;
  beforeDestroy(): void;

  /**
   * 组件监听器
   */
  watch: {
    /**
     * 监听 visible 属性变化
     * @param val - 可见性值
     */
    visible(val: boolean): void;
  };

  /**
   * 组件方法
   */
  methods: {
    /**
     * 打开弹出层
     * 
     * @param options - 打开选项
     * @example
     * // 打开弹出层
     * popup.open();
     * 
     * // 带选项打开弹出层
     * popup.open({ modal: true });
     */
    open(options?: Record<string, any>): void;

    /**
     * 执行打开弹出层
     * 
     * @param props - 属性配置
     */
    doOpen(props: Record<string, any>): void;

    /**
     * 打开后处理
     */
    doAfterOpen(): void;

    /**
     * 关闭弹出层
     * @example
     * // 关闭弹出层
     * popup.close();
     */
    close(): void;

    /**
     * 执行关闭弹出层
     */
    doClose(): void;

    /**
     * 关闭后处理
     */
    doAfterClose(): void;

    /**
     * 恢复 body 样式
     */
    restoreBodyStyle(): void;
  };
}

/**
 * Popup 默认导出
 */
declare const popup: Popup;

export default popup;

/**
 * 导出 PopupManager
 */
export { PopupManager };