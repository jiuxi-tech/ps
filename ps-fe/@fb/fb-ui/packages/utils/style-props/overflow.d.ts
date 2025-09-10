/**
 * overflow 工具模块
 * 提供 overflow 相关的 Vue 组件属性和样式计算功能
 * @module overflow
 */

/**
 * CSS 样式属性接口
 */
interface CSSProperties {
  overflow?: string
  overflowX?: string
  overflowY?: string
}

/**
 * overflow 组件属性定义
 */
export interface OverflowProps {
  /**
   * 设置 overflow 样式属性
   */
  overflow?: string
  
  /**
   * 设置 overflow 为 'hidden'
   */
  overflowHidden?: boolean
  
  /**
   * 设置 overflow 为 'scroll'
   */
  overflowScroll?: boolean
  
  /**
   * 设置 overflow 为 'auto'
   */
  overflowAuto?: boolean
  
  /**
   * 设置 overflow 为 'visible'
   */
  overflowVisible?: boolean
  
  /**
   * 设置 overflow-x 样式属性
   */
  overflowX?: string
  
  /**
   * 设置 overflow-x 为 'hidden'
   */
  overflowXHidden?: boolean
  
  /**
   * 设置 overflow-x 为 'scroll'
   */
  overflowXScroll?: boolean
  
  /**
   * 设置 overflow-x 为 'auto'
   */
  overflowXAuto?: boolean
  
  /**
   * 设置 overflow-x 为 'visible'
   */
  overflowXVisible?: boolean
  
  /**
   * 设置 overflow-y 样式属性
   */
  overflowY?: string
  
  /**
   * 设置 overflow-y 为 'hidden'
   */
  overflowYHidden?: boolean
  
  /**
   * 设置 overflow-y 为 'scroll'
   */
  overflowYScroll?: boolean
  
  /**
   * 设置 overflow-y 为 'auto'
   */
  overflowYAuto?: boolean
  
  /**
   * 设置 overflow-y 为 'visible'
   */
  overflowYVisible?: boolean
}

/**
 * overflow 样式计算函数
 * @returns 包含 overflow 相关样式属性的对象
 */
export declare function style(this: OverflowProps): Pick<CSSProperties, 'overflow' | 'overflowX' | 'overflowY'>

/**
 * overflow 模块默认导出
 */
interface OverflowModule {
  props: OverflowProps
  style: typeof style
}

export default OverflowModule