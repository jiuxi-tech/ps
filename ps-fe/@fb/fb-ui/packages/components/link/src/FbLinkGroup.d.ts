import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbLinkGroup 组件的 Props 类型
export interface FbLinkGroupProps {
  /** 尺寸 */
  size?: 's' | 'm' | 'l' | 'xl'
  
  /** 分隔符 */
  separator?: string
  
  /** 数据 */
  data?: any[]
  
  /** 路由 */
  routes?: any[]
  
  /** 颜色 */
  color?: string
  
  /** 下划线 */
  underline?: 'on' | 'off' | 'hover'
  
  /** 是否追加 */
  append?: boolean
}

// 定义 FbLinkGroup 组件的 Data 属性类型
export interface FbLinkGroupData {
  /** 组件前缀 */
  prefix: string
}

// 定义 FbLinkGroup 组件的 Computed 属性类型
// 暂无计算属性

// 定义 FbLinkGroup 组件的 Slots 类型
export interface FbLinkGroupSlots {
  /** 默认插槽 */
  default: VNode[]
}

// 定义 FbLinkGroup 组件实例类型
export interface FbLinkGroup extends Vue, FbLinkGroupProps, FbLinkGroupData {
  $slots: FbLinkGroupSlots
}

// 定义 FbLinkGroup 组件构造函数类型
export interface FbLinkGroupConstructor extends VueConstructor<FbLinkGroup> {}

// 导出 FbLinkGroup 组件类型
export const FbLinkGroup: FbLinkGroupConstructor

// 默认导出
export default FbLinkGroup