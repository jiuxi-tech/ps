/**
 * alignItems 工具模块
 * 提供 align-items 相关的 Vue 组件属性和样式计算功能
 * @module alignItems
 */

/**
 * CSS 样式属性接口
 */
interface CSSProperties {
  alignItems?: string
}

/**
 * alignItems 组件属性定义
 */
export interface AlignItemsProps {
  /**
   * 设置 align-items 样式属性
   * 可选值: 'center' | 'flex-start' | 'flex-end' | 'stretch' | 'baseline'
   */
  alignItems?: 'center' | 'flex-start' | 'flex-end' | 'stretch' | 'baseline'
  
  /**
   * 设置 align-items 为 'center'
   */
  aiCenter?: boolean
  
  /**
   * 设置 align-items 为 'flex-start'
   */
  aiStart?: boolean
  
  /**
   * 设置 align-items 为 'flex-end'
   */
  aiEnd?: boolean
  
  /**
   * 设置 align-items 为 'space-between'
   */
  aiSpaceBetween?: boolean
  
  /**
   * 设置 align-items 为 'space-around'
   */
  aiSpaceAround?: boolean
  
  /**
   * 设置 align-items 为 'baseline'
   */
  aiBaseline?: boolean
}

/**
 * alignItems 样式计算函数
 * @returns 包含 alignItems 样式属性的对象
 */
export declare function style(this: AlignItemsProps): Pick<CSSProperties, 'alignItems'>

/**
 * alignItems 模块默认导出
 */
interface AlignItemsModule {
  props: AlignItemsProps
  style: typeof style
}

export default AlignItemsModule