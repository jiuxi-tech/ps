/**
 * fontWeight 工具模块
 * 提供 font-weight 相关的 Vue 组件属性和样式计算功能
 * @module fontWeight
 */

/**
 * CSS 样式属性接口
 */
interface CSSProperties {
  fontWeight?: string | number
}

/**
 * fontWeight 组件属性定义
 */
export interface FontWeightProps {
  /**
   * 设置 font-weight 为 'bold'
   */
  bold?: boolean
  
  /**
   * 设置 font-weight 样式属性
   */
  weight?: string | number
  
  /**
   * 设置 font-weight 为 100
   */
  weight1?: boolean
  
  /**
   * 设置 font-weight 为 200
   */
  weight2?: boolean
  
  /**
   * 设置 font-weight 为 300
   */
  weight3?: boolean
  
  /**
   * 设置 font-weight 为 400
   */
  weight4?: boolean
  
  /**
   * 设置 font-weight 为 500
   */
  weight5?: boolean
  
  /**
   * 设置 font-weight 为 600
   */
  weight6?: boolean
  
  /**
   * 设置 font-weight 为 700
   */
  weight7?: boolean
  
  /**
   * 设置 font-weight 为 800
   */
  weight8?: boolean
  
  /**
   * 设置 font-weight 为 900
   */
  weight9?: boolean
  
  /**
   * 设置 font-weight 为 'lighter'
   */
  weightLighter?: boolean
  
  /**
   * 设置 font-weight 为 'bolder'
   */
  weightBolder?: boolean
  
  /**
   * 设置 font-weight 为 'normal'
   */
  weightNormal?: boolean
}

/**
 * fontWeight 样式计算函数
 * @returns 包含 fontWeight 样式属性的对象
 */
export declare function style(this: FontWeightProps): Pick<CSSProperties, 'fontWeight'>

/**
 * fontWeight 模块默认导出
 */
interface FontWeightModule {
  props: FontWeightProps
  style: typeof style
}

export default FontWeightModule