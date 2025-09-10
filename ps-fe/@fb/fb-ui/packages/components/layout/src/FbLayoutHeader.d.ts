import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbLayoutHeader 组件的 Props 类型
export interface FbLayoutHeaderProps {
  // 暂无属性
}

// 定义 FbLayoutHeader 组件的 Data 属性类型
export interface FbLayoutHeaderData {
  /** 组件前缀 */
  prefix: string
}

// 定义 FbLayoutHeader 组件的 Computed 属性类型
// 暂无计算属性

// 定义 FbLayoutHeader 组件的 Slots 类型
export interface FbLayoutHeaderSlots {
  /** 默认插槽 */
  default: VNode[]
}

// 定义 FbLayoutHeader 组件实例类型
export interface FbLayoutHeader extends Vue, FbLayoutHeaderProps, FbLayoutHeaderData {
  $slots: FbLayoutHeaderSlots
}

// 定义 FbLayoutHeader 组件构造函数类型
export interface FbLayoutHeaderConstructor extends VueConstructor<FbLayoutHeader> {}

// 导出 FbLayoutHeader 组件类型
export const FbLayoutHeader: FbLayoutHeaderConstructor

// 默认导出
export default FbLayoutHeader