/**
 * color 工具模块
 * 提供 color 相关的 Vue 组件属性和样式计算功能
 * @module color
 */

/**
 * CSS 样式属性接口
 */
interface CSSProperties {
  color?: string
}

/**
 * color 组件属性定义
 */
export interface ColorProps {
  /**
   * 设置 color 样式属性
   */
  color?: string
}

/**
 * color 样式计算函数
 * @returns 包含 color 样式属性的对象
 */
export declare function style(this: ColorProps): Pick<CSSProperties, 'color'>

/**
 * color 模块默认导出
 */
interface ColorModule {
  props: ColorProps
  style: typeof style
}

export default ColorModule