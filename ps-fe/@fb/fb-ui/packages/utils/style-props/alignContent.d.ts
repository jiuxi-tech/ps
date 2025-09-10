/**
 * alignContent 工具模块
 * 提供 align-content 相关的 Vue 组件属性和样式计算功能
 * @module alignContent
 */

/**
 * CSS 样式属性接口
 */
interface CSSProperties {
  alignContent?: string
}

/**
 * alignContent 组件属性定义
 */
export interface AlignContentProps {
  /**
   * 设置 align-content 样式属性
   * 可选值: 'center' | 'flex-start' | 'flex-end' | 'space-around' | 'space-between' | 'stretch'
   */
  alignContent?: 'center' | 'flex-start' | 'flex-end' | 'space-around' | 'space-between' | 'stretch'
  
  /**
   * 设置 align-content 为 'center'
   */
  acCenter?: boolean
  
  /**
   * 设置 align-content 为 'space-between'
   */
  acSpaceBetween?: boolean
  
  /**
   * 设置 align-content 为 'space-around'
   */
  acSpaceAround?: boolean
  
  /**
   * 设置 align-content 为 'space-evenly'
   */
  acSpaceEvenly?: boolean
  
  /**
   * 设置 align-content 为 'flex-start'
   */
  acStart?: boolean
  
  /**
   * 设置 align-content 为 'flex-end'
   */
  acEnd?: boolean
}

/**
 * alignContent 样式计算函数
 * @returns 包含 alignContent 样式属性的对象
 */
export declare function style(this: AlignContentProps): Pick<CSSProperties, 'alignContent'>

/**
 * alignContent 模块默认导出
 */
interface AlignContentModule {
  props: AlignContentProps
  style: typeof style
}

export default AlignContentModule