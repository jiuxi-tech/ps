import Vue from 'vue';
import { PopupManager } from './popup';

/**
 * Vue Popper 弹出层工具函数集合
 * 
 * @description 基于 Popper.js 的 Vue 弹出层组件工具
 */

/**
 * Vue Popper 弹出层组件配置
 * 
 * @property transformOrigin - 是否启用变换原点，默认为 true
 * @property placement - 弹出层位置，默认为 'bottom'
 * @property boundariesPadding - 边界内边距，默认为 5
 * @property reference - 参考元素
 * @property popper - 弹出层元素
 * @property offset - 弹出层偏移量，默认为 0
 * @property value - 弹出层可见性，默认为 false
 * @property visibleArrow - 是否显示箭头，默认为 false
 * @property arrowOffset - 箭头偏移量，默认为 35
 * @property appendToBody - 是否添加到 body，默认为 true
 * @property popperOptions - Popper.js 配置选项
 * @example
 * // 在 Vue 组件中使用
 * export default {
 *   mixins: [VuePopper],
 *   props: {
 *     placement: {
 *       type: String,
 *       default: 'top'
 *     }
 *   }
 * }
 */
export interface VuePopper {
  /**
   * 组件属性
   */
  props: {
    /**
     * 是否启用变换原点
     * @default true
     */
    transformOrigin: boolean | string;
    
    /**
     * 弹出层位置
     * @default 'bottom'
     */
    placement: string;
    
    /**
     * 边界内边距
     * @default 5
     */
    boundariesPadding: number;
    
    /**
     * 参考元素
     */
    reference: any;
    
    /**
     * 弹出层元素
     */
    popper: any;
    
    /**
     * 弹出层偏移量
     * @default 0
     */
    offset: number;
    
    /**
     * 弹出层可见性
     * @default false
     */
    value: boolean;
    
    /**
     * 是否显示箭头
     * @default false
     */
    visibleArrow: boolean;
    
    /**
     * 箭头偏移量
     * @default 35
     */
    arrowOffset: number;
    
    /**
     * 是否添加到 body
     * @default true
     */
    appendToBody: boolean;
    
    /**
     * Popper.js 配置选项
     * @default { gpuAcceleration: false }
     */
    popperOptions: {
      gpuAcceleration: boolean;
      [key: string]: any;
    };
  };

  /**
   * 组件数据
   */
  data(): {
    /**
     * 弹出层显示状态
     */
    showPopper: boolean;
    
    /**
     * 当前弹出层位置
     */
    currentPlacement: string;
  };

  /**
   * 组件方法
   */
  methods: {
    /**
     * 创建弹出层
     * @example
     * // 创建弹出层
     * this.createPopper();
     */
    createPopper(): void;

    /**
     * 更新弹出层
     * @example
     * // 更新弹出层
     * this.updatePopper();
     */
    updatePopper(): void;

    /**
     * 销毁弹出层
     * @param forceDestroy - 是否强制销毁
     * @example
     * // 销毁弹出层
     * this.doDestroy();
     * 
     * // 强制销毁弹出层
     * this.doDestroy(true);
     */
    doDestroy(forceDestroy?: boolean): void;

    /**
     * 销毁弹出层元素
     * @example
     * // 销毁弹出层元素
     * this.destroyPopper();
     */
    destroyPopper(): void;

    /**
     * 重置变换原点
     * @example
     * // 重置变换原点
     * this.resetTransformOrigin();
     */
    resetTransformOrigin(): void;

    /**
     * 添加箭头元素
     * @param element - 要添加箭头的元素
     * @example
     * // 添加箭头元素
     * this.appendArrow(popperElement);
     */
    appendArrow(element: HTMLElement): void;
  };

  /**
   * 组件监听器
   */
  watch: {
    value: {
      immediate: boolean;
      handler(val: boolean): void;
    };
    showPopper(val: boolean): void;
  };

  /**
   * 生命周期钩子
   */
  beforeDestroy(): void;
  deactivated(): void;
}

/**
 * VuePopper 默认导出
 */
declare const vuePopper: VuePopper;

export default vuePopper;