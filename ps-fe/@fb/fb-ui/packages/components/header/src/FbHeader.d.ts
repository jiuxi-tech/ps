import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbHeader 组件的 Props 类型
export interface FbHeaderProps {
  /** 高度 */
  height?: string | number
}

// 定义 FbHeader 组件的 Data 属性类型
export interface FbHeaderData {
  /** 组件前缀 */
  prefix: string
}

// 定义 FbHeader 组件的 Computed 属性类型
// 暂无计算属性

// 定义 FbHeader 组件的 Slots 类型
export interface FbHeaderSlots {
  /** 默认插槽 */
  default: VNode[]
}

// 定义 FbHeader 组件实例类型
export interface FbHeader extends Vue, FbHeaderProps, FbHeaderData {
  $slots: FbHeaderSlots
}

// 定义 FbHeader 组件构造函数类型
export interface FbHeaderConstructor extends VueConstructor<FbHeader> {}

// 导出 FbHeader 组件类型
export const FbHeader: FbHeaderConstructor

// 默认导出
export default FbHeader