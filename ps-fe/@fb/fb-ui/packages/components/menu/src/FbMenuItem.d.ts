import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbMenuItem 组件的 Props 类型
export interface FbMenuItemProps {
  /** 索引 */
  index?: string
  
  /** 路由 */
  route?: string | object
  
  /** 是否禁用 */
  disabled?: boolean
}

// 定义 FbMenuItem 组件的 Data 属性类型
export interface FbMenuItemData {
  /** 组件前缀 */
  prefix: string
}

// 定义 FbMenuItem 组件的 Computed 属性类型
export interface FbMenuItemComputed {
  /** 是否激活 */
  active: boolean
  
  /** 悬停背景色 */
  hoverBackground: string
  
  /** 背景色 */
  backgroundColor: string
  
  /** 激活文字颜色 */
  activeTextColor: string
  
  /** 文字颜色 */
  textColor: string
  
  /** 模式 */
  mode: string
  
  /** 项目样式 */
  itemStyle: object
  
  /** 是否嵌套 */
  isNested: boolean
}

// 定义 FbMenuItem 组件的 Slots 类型
export interface FbMenuItemSlots {
  /** 默认插槽 */
  default: VNode[]
  
  /** 标题插槽 */
  title: VNode[]
}

// 定义 FbMenuItem 组件实例类型
export interface FbMenuItem extends Vue, FbMenuItemProps, FbMenuItemData, FbMenuItemComputed {
  $slots: FbMenuItemSlots
  
  /** 鼠标进入 */
  onMouseEnter(): void
  
  /** 鼠标离开 */
  onMouseLeave(): void
  
  /** 处理点击 */
  handleClick(): void
}

// 定义 FbMenuItem 组件构造函数类型
export interface FbMenuItemConstructor extends VueConstructor<FbMenuItem> {}

// 导出 FbMenuItem 组件类型
export const FbMenuItem: FbMenuItemConstructor

// 默认导出
export default FbMenuItem