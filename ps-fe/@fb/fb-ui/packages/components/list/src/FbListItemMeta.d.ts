import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbListItemMeta 组件的 Props 类型
export interface FbListItemMetaProps {
  /** 标题 */
  title?: string
  
  /** 描述 */
  description?: string
}

// 定义 FbListItemMeta 组件的 Data 属性类型
export interface FbListItemMetaData {
  /** 组件前缀 */
  prefix: string
}

// 定义 FbListItemMeta 组件的 Computed 属性类型
export interface FbListItemMetaComputed {
  /** 类名 */
  getClass: string[]
}

// 定义 FbListItemMeta 组件的 Slots 类型
export interface FbListItemMetaSlots {
  /** 头像插槽 */
  avatar: VNode[]
  
  /** 标题插槽 */
  title: VNode[]
  
  /** 描述插槽 */
  description: VNode[]
  
  /** 操作插槽 */
  actions: VNode[]
}

// 定义 FbListItemMeta 组件实例类型
export interface FbListItemMeta extends Vue, FbListItemMetaProps, FbListItemMetaData, FbListItemMetaComputed {
  $slots: FbListItemMetaSlots
}

// 定义 FbListItemMeta 组件构造函数类型
export interface FbListItemMetaConstructor extends VueConstructor<FbListItemMeta> {}

// 导出 FbListItemMeta 组件类型
export const FbListItemMeta: FbListItemMetaConstructor

// 默认导出
export default FbListItemMeta