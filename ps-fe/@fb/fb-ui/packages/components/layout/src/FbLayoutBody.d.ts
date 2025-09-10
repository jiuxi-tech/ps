import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbLayoutBody 组件的 Props 类型
export interface FbLayoutBodyProps {
  // 暂无属性
}

// 定义 FbLayoutBody 组件的 Data 属性类型
export interface FbLayoutBodyData {
  /** 组件前缀 */
  prefix: string
}

// 定义 FbLayoutBody 组件的 Computed 属性类型
// 暂无计算属性

// 定义 FbLayoutBody 组件的 Slots 类型
export interface FbLayoutBodySlots {
  /** 默认插槽 */
  default: VNode[]
}

// 定义 FbLayoutBody 组件实例类型
export interface FbLayoutBody extends Vue, FbLayoutBodyProps, FbLayoutBodyData {
  $slots: FbLayoutBodySlots
}

// 定义 FbLayoutBody 组件构造函数类型
export interface FbLayoutBodyConstructor extends VueConstructor<FbLayoutBody> {}

// 导出 FbLayoutBody 组件类型
export const FbLayoutBody: FbLayoutBodyConstructor

// 默认导出
export default FbLayoutBody