import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbLayoutFooter 组件的 Props 类型
export interface FbLayoutFooterProps {
  // 暂无属性
}

// 定义 FbLayoutFooter 组件的 Data 属性类型
export interface FbLayoutFooterData {
  /** 组件前缀 */
  prefix: string
}

// 定义 FbLayoutFooter 组件的 Computed 属性类型
// 暂无计算属性

// 定义 FbLayoutFooter 组件的 Slots 类型
export interface FbLayoutFooterSlots {
  /** 默认插槽 */
  default: VNode[]
}

// 定义 FbLayoutFooter 组件实例类型
export interface FbLayoutFooter extends Vue, FbLayoutFooterProps, FbLayoutFooterData {
  $slots: FbLayoutFooterSlots
}

// 定义 FbLayoutFooter 组件构造函数类型
export interface FbLayoutFooterConstructor extends VueConstructor<FbLayoutFooter> {}

// 导出 FbLayoutFooter 组件类型
export const FbLayoutFooter: FbLayoutFooterConstructor

// 默认导出
export default FbLayoutFooter