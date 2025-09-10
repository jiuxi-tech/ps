/**
 * flex 工具模块
 * 提供 flex 相关的 Vue 组件属性和样式计算功能
 * @module flex
 */

/**
 * CSS 样式属性接口
 */
interface CSSProperties {
  flex?: string
}

/**
 * flex 组件属性定义
 */
export interface FlexProps {
  /**
   * 设置 flex 样式属性
   */
  flex?: string
  
  /**
   * 设置 flex 为 '1 1 0%'
   */
  flex1?: boolean
  
  /**
   * 设置 flex 为 '1 1 auto'
   */
  flexAuto?: boolean
  
  /**
   * 设置 flex 为 '0 1 auto'
   */
  flexInitial?: boolean
  
  /**
   * 设置 flex 为 'none'
   */
  noFlex?: boolean
}

/**
 * flex 样式计算函数
 * @returns 包含 flex 样式属性的对象
 */
export declare function style(this: FlexProps): Pick<CSSProperties, 'flex'>

/**
 * flex 模块默认导出
 */
interface FlexModule {
  props: FlexProps
  style: typeof style
}

export default FlexModule