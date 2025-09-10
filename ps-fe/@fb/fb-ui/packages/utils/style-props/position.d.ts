/**
 * position 工具模块
 * 提供 position 相关的 Vue 组件属性和样式计算功能
 * @module position
 */

/**
 * CSS 样式属性接口
 */
interface CSSProperties {
  position?: string
  top?: string
  left?: string
  right?: string
  bottom?: string
}

/**
 * position 组件属性定义
 */
export interface PositionProps {
  /**
   * 设置 position 为 'relative'
   */
  relative?: boolean
  
  /**
   * 设置 position 为 'absolute'
   */
  absolute?: boolean
  
  /**
   * 设置 position 为 'fixed'
   */
  fixed?: boolean
  
  /**
   * 设置 position 为 'sticky'
   */
  sticky?: boolean
  
  /**
   * 设置 position 样式属性
   */
  position?: string
  
  /**
   * 设置 top 样式属性
   */
  top?: string | number
  
  /**
   * 设置 left 样式属性
   */
  left?: string | number
  
  /**
   * 设置 right 样式属性
   */
  right?: string | number
  
  /**
   * 设置 bottom 样式属性
   */
  bottom?: string | number
}

/**
 * position 样式计算函数
 * @returns 包含 position 相关样式属性的对象
 */
export declare function style(this: PositionProps): Pick<CSSProperties, 'position' | 'top' | 'left' | 'right' | 'bottom'>

/**
 * position 模块默认导出
 */
interface PositionModule {
  props: PositionProps
  style: typeof style
}

export default PositionModule