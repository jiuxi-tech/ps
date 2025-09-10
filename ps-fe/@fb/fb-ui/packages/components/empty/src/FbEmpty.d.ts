import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbEmpty 组件的 Props 类型
export interface FbEmptyProps {
  /** 尺寸 */
  size?: string
  
  /** 类型 */
  type?: 'data' | 'notice' | 'todo'
  
  /** 文本 */
  text?: string
}

// 定义 FbEmpty 组件的 Data 属性类型
export interface FbEmptyData {
  /** 组件前缀 */
  prefix: string
}

// 定义 FbEmpty 组件的 Computed 属性类型
export interface FbEmptyComputed {
  /** 类名 */
  getClass: string[]
}

// 定义 FbEmpty 组件的 Slots 类型
export interface FbEmptySlots {
  /** 默认插槽 */
  default: VNode[]
  
  /** 文本插槽 */
  text: VNode[]
}

// 定义 FbEmpty 组件实例类型
export interface FbEmpty extends Vue, FbEmptyProps, FbEmptyData, FbEmptyComputed {
  $slots: FbEmptySlots
}

// 定义 FbEmpty 组件构造函数类型
export interface FbEmptyConstructor extends VueConstructor<FbEmpty> {}

// 导出 FbEmpty 组件类型
export const FbEmpty: FbEmptyConstructor

// 默认导出
export default FbEmpty