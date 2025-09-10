/**
 * justifyContent 工具模块
 * 提供 justify-content 相关的 Vue 组件属性和样式计算功能
 * @module justifyContent
 */

/**
 * CSS 样式属性接口
 */
interface CSSProperties {
  justifyContent?: string
}

/**
 * justifyContent 组件属性定义
 */
export interface JustifyContentProps {
  /**
   * 设置 justify-content 样式属性
   * 可选值: 'center' | 'flex-start' | 'flex-end' | 'space-around' | 'space-between' | 'space-evenly'
   */
  justifyContent?: 'center' | 'flex-start' | 'flex-end' | 'space-around' | 'space-between' | 'space-evenly'
  
  /**
   * 设置 justify-content 为 'center'
   */
  jcCenter?: boolean
  
  /**
   * 设置 justify-content 为 'flex-start'
   */
  jcStart?: boolean
  
  /**
   * 设置 justify-content 为 'flex-end'
   */
  jcEnd?: boolean
  
  /**
   * 设置 justify-content 为 'space-between'
   */
  jcSpaceBetween?: boolean
  
  /**
   * 设置 justify-content 为 'space-around'
   */
  jcSpaceAround?: boolean
  
  /**
   * 设置 justify-content 为 'space-evenly'
   */
  jcSpaceEvenly?: boolean
}

/**
 * justifyContent 样式计算函数
 * @returns 包含 justifyContent 样式属性的对象
 */
export declare function style(this: JustifyContentProps): Pick<CSSProperties, 'justifyContent'>

/**
 * justifyContent 模块默认导出
 */
interface JustifyContentModule {
  props: JustifyContentProps
  style: typeof style
}

export default JustifyContentModule