import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbListItem 组件的 Props 类型
export interface FbListItemProps {
  // 暂无属性
}

// 定义 FbListItem 组件的 Data 属性类型
export interface FbListItemData {
  /** 组件前缀 */
  prefix: string
}

// 定义 FbListItem 组件的 Computed 属性类型
export interface FbListItemComputed {
  /** 类名 */
  getClass: string[]
}

// 定义 FbListItem 组件的 Slots 类型
export interface FbListItemSlots {
  /** 默认插槽 */
  default: VNode[]
  
  /** 额外内容插槽 */
  extra: VNode[]
}

// 定义 FbListItem 组件实例类型
export interface FbListItem extends Vue, FbListItemProps, FbListItemData, FbListItemComputed {
  $slots: FbListItemSlots
}

// 定义 FbListItem 组件构造函数类型
export interface FbListItemConstructor extends VueConstructor<FbListItem> {}

// 导出 FbListItem 组件类型
export const FbListItem: FbListItemConstructor

// 默认导出
export default FbListItem