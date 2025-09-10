/**
 * textAlign 工具模块
 * 提供 text-align 相关的 Vue 组件属性和样式计算功能
 * @module textAlign
 */

/**
 * CSS 样式属性接口
 */
interface CSSProperties {
  textAlign?: string
}

/**
 * textAlign 预定义映射
 */
export const textAlignMap: {
  center: string
  right: string
  left: string
  justify: string
}

/**
 * textAlign 组件属性定义
 */
export interface TextAlignProps {
  /**
   * 设置 text-align 样式属性
   */
  align?: string
  
  /**
   * 设置 text-align 为 'center'
   */
  alignCenter?: boolean
  
  /**
   * 设置 text-align 为 'right'
   */
  alignRight?: boolean
  
  /**
   * 设置 text-align 为 'left'
   */
  alignLeft?: boolean
  
  /**
   * 设置 text-align 为 'justify'
   */
  alignJustify?: boolean
}

/**
 * textAlign 样式计算函数
 * @returns 包含 textAlign 样式属性的对象
 */
export declare function style(this: TextAlignProps): Pick<CSSProperties, 'textAlign'>

/**
 * textAlign 模块默认导出
 */
interface TextAlignModule {
  textAlignMap: typeof textAlignMap
  props: TextAlignProps
  style: typeof style
}

export default TextAlignModule