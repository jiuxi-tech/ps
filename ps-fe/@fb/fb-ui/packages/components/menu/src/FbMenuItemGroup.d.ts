import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbMenuItemGroup 组件的 Props 类型
export interface FbMenuItemGroupProps {
  /** 标题 */
  title?: string
}

// 定义 FbMenuItemGroup 组件的 Data 属性类型
export interface FbMenuItemGroupData {
  /** 组件前缀 */
  prefix: string
  
  /** 左边距 */
  paddingLeft: number
}

// 定义 FbMenuItemGroup 组件的 Computed 属性类型
export interface FbMenuItemGroupComputed {
  /** 背景色 */
  backgroundColor: string
  
  /** 层级左边距 */
  levelPadding: number
}

// 定义 FbMenuItemGroup 组件的 Slots 类型
export interface FbMenuItemGroupSlots {
  /** 标题插槽 */
  title: VNode[]
  
  /** 默认插槽 */
  default: VNode[]
}

// 定义 FbMenuItemGroup 组件实例类型
export interface FbMenuItemGroup extends Vue, FbMenuItemGroupProps, FbMenuItemGroupData, FbMenuItemGroupComputed {
  $slots: FbMenuItemGroupSlots
}

// 定义 FbMenuItemGroup 组件构造函数类型
export interface FbMenuItemGroupConstructor extends VueConstructor<FbMenuItemGroup> {}

// 导出 FbMenuItemGroup 组件类型
export const FbMenuItemGroup: FbMenuItemGroupConstructor

// 默认导出
export default FbMenuItemGroup