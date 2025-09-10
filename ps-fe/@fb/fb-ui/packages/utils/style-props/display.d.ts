/**
 * display 工具模块
 * 提供 display 相关的 Vue 组件属性和样式计算功能
 * @module display
 */

/**
 * CSS 样式属性接口
 */
interface CSSProperties {
  display?: string
}

/**
 * display 组件属性定义
 */
export interface DisplayProps {
  /**
   * 设置 display 样式属性
   */
  display?: string
  
  /**
   * 设置 display 为 'inline'
   */
  inline?: boolean
  
  /**
   * 设置 display 为 'block'
   */
  block?: boolean
}

/**
 * display 样式计算函数
 * @returns 包含 display 样式属性的对象
 */
export declare function style(this: DisplayProps): Pick<CSSProperties, 'display'>

/**
 * display 模块默认导出
 */
interface DisplayModule {
  props: DisplayProps
  style: typeof style
}

export default DisplayModule