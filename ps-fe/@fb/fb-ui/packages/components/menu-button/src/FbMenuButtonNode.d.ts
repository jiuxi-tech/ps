import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbMenuButtonNode 组件的 Props 类型
export interface FbMenuButtonNodeProps {
  /** 图标名称 */
  icon?: string
  
  /** 索引 */
  index?: string | number
  
  /** 标签 */
  label?: string
  
  /** 颜色 */
  color?: string
  
  /** 值 */
  value?: string | number | boolean
  
  /** 是否禁用 */
  disabled?: boolean
  
  /** 子节点 */
  children?: any[]
}

// 定义 FbMenuButtonNode 组件的 Data 属性类型
export interface FbMenuButtonNodeData {
  /** 组件前缀 */
  prefix: string
  
  /** 是否显示菜单 */
  showMenu: boolean
  
  /** 菜单按钮组件 */
  fbMenuButton: any
}

// 定义 FbMenuButtonNode 组件的 Computed 属性类型
// 暂无计算属性

// 定义 FbMenuButtonNode 组件的 Slots 类型
export interface FbMenuButtonNodeSlots {
  /** 默认插槽 */
  default: VNode[]
}

// 定义 FbMenuButtonNode 组件实例类型
export interface FbMenuButtonNode extends Vue, FbMenuButtonNodeProps, FbMenuButtonNodeData {
  $slots: FbMenuButtonNodeSlots
  
  /** 处理点击 */
  handleClick(event: Event): void
}

// 定义 FbMenuButtonNode 组件构造函数类型
export interface FbMenuButtonNodeConstructor extends VueConstructor<FbMenuButtonNode> {}

// 导出 FbMenuButtonNode 组件类型
export const FbMenuButtonNode: FbMenuButtonNodeConstructor

// 默认导出
export default FbMenuButtonNode