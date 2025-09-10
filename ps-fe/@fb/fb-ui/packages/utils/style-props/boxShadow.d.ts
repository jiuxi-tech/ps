/**
 * boxShadow 工具模块
 * 提供 box-shadow 相关的 Vue 组件属性和样式计算功能
 * @module boxShadow
 */

/**
 * CSS 样式属性接口
 */
interface CSSProperties {
  boxShadow?: string
}

/**
 * boxShadow 预定义映射
 */
export const boxShadowMap: {
  shadow: string
  shadowXs: string
  shadowS: string
  shadowM: string
  shadowL: string
  shadowXl: string
  shadowXxl: string
  shadowInner: string
  noShadow: string
}

/**
 * boxShadow 组件属性定义
 */
export interface BoxShadowProps {
  /**
   * 设置 box-shadow 样式属性
   */
  shadow?: boolean | string
  
  /**
   * 无阴影
   */
  noShadow?: boolean
  
  /**
   * 超小阴影
   */
  shadowXs?: boolean
  
  /**
   * 小阴影
   */
  shadowS?: boolean
  
  /**
   * 中等阴影
   */
  shadowM?: boolean
  
  /**
   * 大阴影
   */
  shadowL?: boolean
  
  /**
   * 超大阴影
   */
  shadowXl?: boolean
  
  /**
   * 超超大阴影
   */
  shadowXxl?: boolean
  
  /**
   * 内阴影
   */
  shadowInner?: boolean
  
  /**
   * 自定义 box-shadow 样式属性
   */
  boxShadow?: string
}

/**
 * boxShadow 样式计算函数
 * @returns 包含 boxShadow 样式属性的对象
 */
export declare function style(this: BoxShadowProps): Pick<CSSProperties, 'boxShadow'>

/**
 * boxShadow 模块默认导出
 */
interface BoxShadowModule {
  props: BoxShadowProps
  style: typeof style
  boxShadowMap: typeof boxShadowMap
}

export default BoxShadowModule