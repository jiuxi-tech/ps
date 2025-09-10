/**
 * zIndex 工具模块
 * 提供 z-index 相关的 Vue 组件属性和样式计算功能
 * @module zIndex
 */

/**
 * CSS 样式属性接口
 */
interface CSSProperties {
  zIndex?: string
}

/**
 * zIndex 预定义映射
 */
export const zIndexMap: {
  '0': string
  '1': string
  '10': string
  '100': string
  '200': string
  '1000': string
  '2000': string
  '10000': string
  '20000': string
  '100000': string
  '1000000': string
  '10000000': string
  noZ: string
}

/**
 * zIndex 组件属性定义
 */
export interface ZIndexProps {
  /**
   * 设置 z-index 样式属性
   */
  zIndex?: string | number
  
  /**
   * 设置 z-index 样式属性（缩写）
   */
  z?: string | number
  
  /**
   * 设置 z-index 为 0
   */
  z0?: boolean
  
  /**
   * 设置 z-index 为 1
   */
  z1?: boolean
  
  /**
   * 设置 z-index 为 10
   */
  z10?: boolean
  
  /**
   * 设置 z-index 为 100
   */
  z100?: boolean
  
  /**
   * 设置 z-index 为 200
   */
  z200?: boolean
  
  /**
   * 设置 z-index 为 1000
   */
  z1000?: boolean
  
  /**
   * 设置 z-index 为 2000
   */
  z2000?: boolean
  
  /**
   * 设置 z-index 为 10000
   */
  z10000?: boolean
  
  /**
   * 设置 z-index 为 20000
   */
  z20000?: boolean
  
  /**
   * 设置 z-index 为 100000
   */
  z100000?: boolean
  
  /**
   * 设置 z-index 为 1000000
   */
  z1000000?: boolean
  
  /**
   * 设置 z-index 为 10000000
   */
  z10000000?: boolean
  
  /**
   * 设置 z-index 为 -1
   */
  noZ?: boolean
}

/**
 * zIndex 样式计算函数
 * @returns 包含 zIndex 样式属性的对象
 */
export declare function style(this: ZIndexProps): Pick<CSSProperties, 'zIndex'>

/**
 * zIndex 模块默认导出
 */
interface ZIndexModule {
  props: ZIndexProps
  style: typeof style
}

export default ZIndexModule