import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbDot 组件的 Props 类型
export interface FbDotProps {
  /** 显示文本 */
  text?: string
  
  /** 分割符 */
  separator?: string
  
  /** 间隔时间(毫秒) */
  interval?: number
}

// 定义 FbDot 组件的 Data 属性类型
export interface FbDotData {
  /** 组件前缀 */
  prefix: string
  
  /** 显示文本 */
  myText: string
  
  /** 定时器 */
  timer: any
  
  /** 文本数组 */
  textArray: string[]
  
  /** 索引 */
  index: number
}

// 定义 FbDot 组件的 Computed 属性类型
export interface FbDotComputed {
  // 暂无计算属性
}

// 定义 FbDot 组件的 Slots 类型
export interface FbDotSlots {
  /** 默认插槽 */
  default: VNode[]
}

// 定义 FbDot 组件实例类型
export interface FbDot extends Vue, FbDotProps, FbDotData, FbDotComputed {
  $slots: FbDotSlots
  
  /** 初始化 */
  init(): void
}

// 定义 FbDot 组件构造函数类型
export interface FbDotConstructor extends VueConstructor<FbDot> {}

// 导出 FbDot 组件类型
export const FbDot: FbDotConstructor

// 默认导出
export default FbDot