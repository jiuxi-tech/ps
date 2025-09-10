/**
 * padding 工具模块
 * 提供 padding 相关的 Vue 组件属性和样式计算功能
 * @module padding
 */

/**
 * CSS 样式属性接口
 */
interface CSSProperties {
  padding?: string
  paddingTop?: string
  paddingRight?: string
  paddingBottom?: string
  paddingLeft?: string
}

/**
 * padding 组件属性定义
 */
export interface PaddingProps {
  /**
   * 左内边距
   */
  pl?: string | number
  
  /**
   * 右内边距
   */
  pr?: string | number
  
  /**
   * 上内边距
   */
  pt?: string | number
  
  /**
   * 底内边距
   */
  pb?: string | number
  
  /**
   * 水平内边距
   * px='8px' 等同于 padding-left: 8px; padding-right: 8px;
   */
  px?: string | number
  
  /**
   * 垂直内边距
   * py='8px' 等同于 padding-top: 8px; padding-bottom: 8px;
   */
  py?: string | number
  
  /**
   * 内边距
   */
  padding?: string | number
}

/**
 * padding 样式计算函数
 * @returns 包含 padding 相关样式属性的对象
 */
export declare function style(this: PaddingProps): Pick<CSSProperties, 'padding' | 'paddingTop' | 'paddingRight' | 'paddingBottom' | 'paddingLeft'>

/**
 * padding 模块默认导出
 */
interface PaddingModule {
  props: PaddingProps
  style: typeof style
}

export default PaddingModule