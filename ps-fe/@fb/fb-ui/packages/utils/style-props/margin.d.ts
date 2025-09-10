/**
 * margin 工具模块
 * 提供 margin 相关的 Vue 组件属性和样式计算功能
 * @module margin
 */

/**
 * CSS 样式属性接口
 */
interface CSSProperties {
  margin?: string
  marginTop?: string
  marginRight?: string
  marginBottom?: string
  marginLeft?: string
}

/**
 * margin 组件属性定义
 */
export interface MarginProps {
  /**
   * 左外边距
   */
  ml?: string | number
  
  /**
   * 右外边距
   */
  mr?: string | number
  
  /**
   * 底外边距
   */
  mb?: string | number
  
  /**
   * 上外边距
   */
  mt?: string | number
  
  /**
   * 水平外边距
   * mx='8px' 等同于 margin-left: 8px; margin-right: 8px;
   */
  mx?: string | number
  
  /**
   * 垂直外边距
   * my='8px' 等同于 margin-top: 8px; margin-bottom: 8px;
   */
  my?: string | number
  
  /**
   * 外边距
   */
  margin?: string | number
}

/**
 * margin 样式计算函数
 * @returns 包含 margin 相关样式属性的对象
 */
export declare function style(this: MarginProps): Pick<CSSProperties, 'margin' | 'marginTop' | 'marginRight' | 'marginBottom' | 'marginLeft'>

/**
 * margin 模块默认导出
 */
interface MarginModule {
  props: MarginProps
  style: typeof style
}

export default MarginModule