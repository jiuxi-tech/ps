/**
 * border 工具模块
 * 提供 border 相关的 Vue 组件属性和样式计算功能
 * @module border
 */

/**
 * CSS 样式属性接口
 */
interface CSSProperties {
  border?: string
  borderTop?: string
  borderRight?: string
  borderBottom?: string
  borderLeft?: string
  borderRadius?: string
}

/**
 * border 组件属性定义
 */
export interface BorderProps {
  /**
   * 设置 border 样式属性
   */
  border?: boolean | string
  
  /**
   * 上边框
   */
  bt?: string
  
  /**
   * 右边框
   */
  br?: string
  
  /**
   * 下边框
   */
  bb?: string
  
  /**
   * 左边框
   */
  bl?: string
  
  /**
   * 圆角
   */
  radius?: boolean | string | number
  
  /**
   * 小圆角 (2px)
   */
  radiusXs?: boolean
  
  /**
   * 小圆角 (4px)
   */
  radiusS?: boolean
  
  /**
   * 中等圆角 (6px)
   */
  radiusM?: boolean
  
  /**
   * 大圆角 (8px)
   */
  radiusL?: boolean
  
  /**
   * 超大圆角 (12px)
   */
  radiusXl?: boolean
  
  /**
   * 超超大圆角 (16px)
   */
  radiusXxl?: boolean
}

/**
 * border 样式计算函数
 * @returns 包含 border 相关样式属性的对象
 */
export declare function style(this: BorderProps): Pick<CSSProperties, 'border' | 'borderTop' | 'borderRight' | 'borderBottom' | 'borderLeft' | 'borderRadius'>

/**
 * border 模块默认导出
 */
interface BorderModule {
  props: BorderProps
  style: typeof style
}

export default BorderModule