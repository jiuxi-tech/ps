import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbMenuButton 组件的 Props 类型
export interface FbMenuButtonProps {
  /** 标签 */
  label?: string
  
  /** 值 */
  value?: string
  
  /** 宽度 */
  width?: string | number
  
  /** 类型 */
  type?: string
  
  /** 尺寸 */
  size?: string
  
  /** 是否100%宽度 */
  long?: boolean
  
  /** 是否加载中 */
  loading?: boolean
  
  /** 是否危险按钮 */
  danger?: boolean
  
  /** 是否编辑器按钮 */
  editor?: boolean
  
  /** 是否警告按钮 */
  warning?: boolean
  
  /** 是否成功按钮 */
  success?: boolean
  
  /** 是否禁用 */
  disabled?: boolean
  
  /** 是否清爽按钮 */
  plain?: boolean
  
  /** 是否圆角 */
  round?: boolean
  
  /** 是否圆形按钮 */
  circle?: boolean
  
  /** 图标名称 */
  icon?: string
  
  /** 后置图标 */
  appendIcon?: string
  
  /** 数据 */
  data?: any[]
  
  /** 点击后是否隐藏菜单 */
  hideMenuAfterClick?: boolean
  
  /** 触发类型 */
  triggerType?: string
}

// 定义 FbMenuButton 组件的 Data 属性类型
export interface FbMenuButtonData {
  /** 组件前缀 */
  prefix: string
  
  /** 过渡动画 */
  transitions: object
  
  /** 是否显示菜单 */
  showMenu: boolean
  
  /** 数据 */
  myData: any[]
  
  /** 列表高度 */
  listHeight: number
}

// 定义 FbMenuButton 组件的 Computed 属性类型
// 暂无计算属性

// 定义 FbMenuButton 组件的 Slots 类型
export interface FbMenuButtonSlots {
  /** 默认插槽 */
  default: VNode[]
  
  /** 菜单插槽 */
  menu: VNode[]
}

// 定义 FbMenuButton 组件实例类型
export interface FbMenuButton extends Vue, FbMenuButtonProps, FbMenuButtonData {
  $slots: FbMenuButtonSlots
  
  /** 关闭 */
  close(): void
  
  /** 失去焦点 */
  blur(): void
  
  /** 获得焦点 */
  focus(): void
  
  /** 切换 */
  toggle(): void
  
  /** 更新位置 */
  updatePosition(): void
  
  /** 处理点击 */
  handleClick(event: Event): void
  
  /** 处理菜单点击 */
  handleMenuClick(value: any, node: object): void
}

// 定义 FbMenuButton 组件构造函数类型
export interface FbMenuButtonConstructor extends VueConstructor<FbMenuButton> {}

// 导出 FbMenuButton 组件类型
export const FbMenuButton: FbMenuButtonConstructor

// 默认导出
export default FbMenuButton