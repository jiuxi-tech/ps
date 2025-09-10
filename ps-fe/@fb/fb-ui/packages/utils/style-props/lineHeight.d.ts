/**
 * lineHeight 工具模块
 * 提供 line-height 相关的 Vue 组件属性和样式计算功能
 * @module lineHeight
 */

/**
 * CSS 样式属性接口
 */
interface CSSProperties {
  lineHeight?: string | number
}

/**
 * lineHeight 组件属性定义
 */
export interface LineHeightProps {
  /**
   * 设置 line-height 样式属性
   */
  lh?: string | number
  
  /**
   * 设置 line-height 为 0.75
   */
  lh1?: boolean
  
  /**
   * 设置 line-height 为 1
   */
  lh2?: boolean
  
  /**
   * 设置 line-height 为 1.25
   */
  lh3?: boolean
  
  /**
   * 设置 line-height 为 1.5
   */
  lh4?: boolean
  
  /**
   * 设置 line-height 为 1.75
   */
  lh5?: boolean
  
  /**
   * 设置 line-height 为 2
   */
  lh6?: boolean
  
  /**
   * 设置 line-height 为 2.25
   */
  lh7?: boolean
  
  /**
   * 设置 line-height 为 2.5
   */
  lh8?: boolean
  
  /**
   * 设置 line-height 为 2.75
   */
  lh9?: boolean
  
  /**
   * 设置 line-height 为 3
   */
  lh10?: boolean
}

/**
 * lineHeight 样式计算函数
 * @returns 包含 lineHeight 样式属性的对象
 */
export declare function style(this: LineHeightProps): Pick<CSSProperties, 'lineHeight'>

/**
 * lineHeight 模块默认导出
 */
interface LineHeightModule {
  props: LineHeightProps
  style: typeof style
  lineHeightMap: Record<string, any>
}

export default LineHeightModule