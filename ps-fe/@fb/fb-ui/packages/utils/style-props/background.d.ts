/**
 * background 工具模块
 * 提供 background 相关的 Vue 组件属性和样式计算功能
 * @module background
 */

/**
 * CSS 样式属性接口
 */
interface CSSProperties {
  background?: string
}

/**
 * background 组件属性定义
 */
export interface BackgroundProps {
  /**
   * 设置 background 样式属性
   */
  background?: string
  
  /**
   * 设置 background 样式属性（缩写）
   */
  bg?: string
}

/**
 * background 样式计算函数
 * @returns 包含 background 样式属性的对象
 */
export declare function style(this: BackgroundProps): Pick<CSSProperties, 'background'>

/**
 * background 模块默认导出
 */
interface BackgroundModule {
  props: BackgroundProps
  style: typeof style
}

export default BackgroundModule