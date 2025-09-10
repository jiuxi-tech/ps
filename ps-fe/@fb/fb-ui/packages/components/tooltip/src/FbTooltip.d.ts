import { VNode } from 'vue'
import { FbPopper } from '../../../utils/vue-popper'

export interface FbTooltipProps {
  /**
   * 延迟显示(毫秒)
   */
  openDelay?: number
  
  /**
   * 是否禁用
   */
  disabled?: boolean
  
  /**
   * 是否手动控制
   */
  manual?: boolean
  
  /**
   * 主题样式
   */
  effect?: 'dark' | 'light'
  
  /**
   * 箭头偏移量
   */
  arrowOffset?: number
  
  /**
   * 自定义类名
   */
  popperClass?: string
  
  /**
   * 提示内容
   */
  content?: string
  
  /**
   * 是否显示箭头
   */
  visibleArrow?: boolean
  
  /**
   * 动画名称
   */
  transition?: string
  
  /**
   * popper.js 参数
   */
  popperOptions?: any
  
  /**
   * 鼠标是否可进入
   */
  enterable?: boolean
  
  /**
   * 延迟隐藏(毫秒)
   */
  hideAfter?: number
  
  /**
   * tabindex
   */
  tabindex?: number
  
  /**
   * 触发方式
   */
  trigger?: 'hover' | 'click' | 'focus'
  
  /**
   * 提示框样式
   */
  tipStyle?: string | object | any[]
  
  /**
   * 聚焦时的类名
   */
  focusClass?: string
}

export interface FbTooltipData {
  /**
   * tooltip ID
   */
  tooltipId: string
  
  /**
   * 超时句柄
   */
  timeoutPending: number | null
  
  /**
   * 是否聚焦
   */
  focusing: boolean
  
  /**
   * 期望状态
   */
  expectedState: boolean
}

export interface FbTooltipMethods {
  /**
   * 显示 tooltip
   */
  show(): void
  
  /**
   * 隐藏 tooltip
   */
  hide(): void
  
  /**
   * 切换显示状态
   */
  toggle(): void
  
  /**
   * 处理聚焦
   */
  handleFocus(): void
  
  /**
   * 处理失焦
   */
  handleBlur(): void
  
  /**
   * 移除聚焦状态
   */
  removeFocusing(): void
  
  /**
   * 添加 tooltip 类名
   */
  addTooltipClass(prev: string): string
  
  /**
   * 处理显示
   */
  handleShowPopper(): void
  
  /**
   * 处理隐藏
   */
  handleClosePopper(): void
  
  /**
   * 设置期望状态
   */
  setExpectedState(expectedState: boolean): void
  
  /**
   * 获取期望状态
   */
  getExpectedState(): boolean
  
  /**
   * 获取第一个元素
   */
  getFirstElement(): VNode | null
}

export interface FbTooltipSlots {
  /**
   * 内容插槽
   */
  content: VNode[]
  
  /**
   * 默认插槽
   */
  default: VNode[]
}

export declare class FbTooltip extends FbPopper implements FbTooltipProps, FbTooltipData, FbTooltipMethods {
  static readonly componentName: 'FbTooltip'
  
  // Props
  openDelay: number
  disabled: boolean
  manual: boolean
  effect: 'dark' | 'light'
  arrowOffset: number
  popperClass: string
  content: string
  visibleArrow: boolean
  transition: string
  popperOptions: any
  enterable: boolean
  hideAfter: number
  tabindex: number
  trigger: 'hover' | 'click' | 'focus'
  tipStyle: string | object | any[]
  focusClass: string
  
  // Data
  tooltipId: string
  timeoutPending: number | null
  focusing: boolean
  expectedState: boolean
  
  // Methods
  show(): void
  hide(): void
  toggle(): void
  handleFocus(): void
  handleBlur(): void
  removeFocusing(): void
  addTooltipClass(prev: string): string
  handleShowPopper(): void
  handleClosePopper(): void
  setExpectedState(expectedState: boolean): void
  getExpectedState(): boolean
  getFirstElement(): VNode | null
}

export default FbTooltip