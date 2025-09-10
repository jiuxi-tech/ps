// vue-popper 根据 https://popper.js.org/ 处理而来
// 整体改造与 el-tooltip 一致
import Popper from './vue-popper'
import {debounce} from 'lodash-es'
import {addClass, removeClass, on, off} from './dom'
import {generateId} from './utils'
import {prefix} from '../../src/config'
import Vue from 'vue'

/**
 * 下拉弹出层组件
 * 
 * @description 基于 Popper 的下拉弹出层组件
 */

export interface DropdownPopper {
  /**
   * 组件名称
   */
  name: string;

  /**
   * 组件混入
   */
  mixins: any[];

  /**
   * 组件属性
   */
  props: {
    /**
     * 打开延迟时间（毫秒）
     * @default 0
     */
    openDelay: number;
    
    /**
     * 是否手动控制显示
     * @default false
     */
    manual: boolean;
    
    /**
     * 弹出层效果主题
     * @default 'dark'
     */
    effect: string;
    
    /**
     * 箭头偏移量
     * @default 0
     */
    arrowOffset: number;
    
    /**
     * 弹出层自定义类名
     */
    popperClass: string;
    
    /**
     * 弹出层内容
     */
    content: string;
    
    /**
     * 是否显示箭头
     * @default false
     */
    visibleArrow: boolean;
    
    /**
     * 过渡动画名称
     * @default 'fade'
     */
    transition: string;
    
    /**
     * Popper.js 配置选项
     * @default { boundariesPadding: 10, gpuAcceleration: false }
     */
    popperOptions: {
      boundariesPadding: number;
      gpuAcceleration: boolean;
      [key: string]: any;
    };
    
    /**
     * 鼠标是否可进入弹出层
     * @default true
     */
    enterable: boolean;
    
    /**
     * 自动隐藏延迟时间（毫秒）
     * @default 0
     */
    hideAfter: number;
    
    /**
     * 元素 tabindex 属性
     * @default 0
     */
    tabindex: number;
    
    /**
     * 弹出层样式
     * @default [{ border: 'none', padding: 0, margin: 0, background: 'transparent' }]
     */
    tipStyle: string | object | any[];
    
    /**
     * 聚焦时的类名
     * @default ''
     */
    focusClass: string;
  };

  /**
   * 组件数据
   */
  data(): {
    /**
     * 下拉菜单 ID
     */
    dropdownId: string;
    
    /**
     * 延迟定时器 ID
     */
    timeoutPending: number | null;
    
    /**
     * 是否聚焦状态
     */
    focusing: boolean;
    
    /**
     * 期望的显示状态
     */
    expectedState: boolean;
  };

  /**
   * 组件生命周期钩子
   */
  beforeCreate(): void;
  mounted(): void;
  beforeDestroy(): void;
  destroyed(): void;

  /**
   * 组件监听器
   */
  watch: {
    focusing(val: boolean): void;
    expectedState(val: boolean): void;
  };

  /**
   * 组件方法
   */
  methods: {
    /**
     * 显示下拉菜单
     * @example
     * // 显示下拉菜单
     * dropdown.show();
     */
    show(): void;

    /**
     * 隐藏下拉菜单
     * @example
     * // 隐藏下拉菜单
     * dropdown.hide();
     */
    hide(): void;
    
    /**
     * 切换下拉菜单显示状态
     * @example
     * // 切换下拉菜单显示状态
     * dropdown.toggle();
     */
    toggle(): void;
    
    /**
     * 处理聚焦事件
     * @example
     * // 处理聚焦事件
     * dropdown.handleFocus();
     */
    handleFocus(): void;
    
    /**
     * 处理失焦事件
     * @example
     * // 处理失焦事件
     * dropdown.handleBlur();
     */
    handleBlur(): void;
    
    /**
     * 移除聚焦状态
     * @example
     * // 移除聚焦状态
     * dropdown.removeFocusing();
     */
    removeFocusing(): void;

    /**
     * 添加下拉菜单类名
     * 
     * @param prev - 原始类名
     * @returns 返回处理后的类名
     * @example
     * // 添加下拉菜单类名
     * const className = dropdown.addDropdownClass('original-class');
     */
    addDropdownClass(prev: string): string;

    /**
     * 处理显示下拉菜单
     * @example
     * // 处理显示下拉菜单
     * dropdown.handleShowPopper();
     */
    handleShowPopper(): void;

    /**
     * 处理关闭下拉菜单
     * @example
     * // 处理关闭下拉菜单
     * dropdown.handleClosePopper();
     */
    handleClosePopper(): void;

    /**
     * 设置期望的显示状态
     * 
     * @param expectedState - 期望的显示状态
     * @example
     * // 设置期望的显示状态为 true
     * dropdown.setExpectedState(true);
     */
    setExpectedState(expectedState: boolean): void;

    /**
     * 获取期望的显示状态
     * 
     * @returns 返回期望的显示状态
     * @example
     * // 获取期望的显示状态
     * const state = dropdown.getExpectedState();
     */
    getExpectedState(): boolean;

    /**
     * 获取第一个元素
     * 
     * @returns 返回第一个 VNode 元素或 null
     * @example
     * // 获取第一个元素
     * const element = dropdown.getFirstElement();
     */
    getFirstElement(): Vue.VNode | null;
    
    /**
     * 渲染函数
     */
    render(h: Vue.CreateElement): Vue.VNode | null;
    
    /**
     * 更新 Popper
     */
    updatePopper(): void;
    
    /**
     * 销毁 Popper
     */
    doDestroy(force?: boolean): void;
  };
}

/**
 * DropdownPopper 默认导出
 */
declare const dropdownPopper: DropdownPopper;

export default dropdownPopper;