import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbSubMenu 组件的 Props 类型
export interface FbSubMenuProps {
  /** 索引 */
  index: string
  
  /** 显示超时时间 */
  showTimeout?: number
  
  /** 隐藏超时时间 */
  hideTimeout?: number
  
  /** 弹出层类名 */
  popperClass?: string
  
  /** 是否禁用 */
  disabled?: boolean
  
  /** 是否追加到body */
  popperAppendToBody?: boolean
  
  /** 是否使用transform-origin */
  transformOrigin?: boolean | string
  
  /** 偏移量 */
  offset?: any
  
  /** 边界填充 */
  boundariesPadding?: any
  
  /** popper选项 */
  popperOptions?: any
}

// 定义 FbSubMenu 组件的 Data 属性类型
export interface FbSubMenuData {
  /** 组件前缀 */
  prefix: string
  
  /** popper实例 */
  popperJS: any
  
  /** 定时器 */
  timeout: any
  
  /** 菜单项 */
  items: object
  
  /** 子菜单 */
  submenus: object
  
  /** 鼠标是否在子元素内 */
  mouseInChild: boolean
}

// 定义 FbSubMenu 组件的 Computed 属性类型
export interface FbSubMenuComputed {
  /** 是否追加到body */
  appendToBody: boolean
  
  /** 菜单过渡名称 */
  menuTransitionName: string
  
  /** 是否展开 */
  opened: boolean
  
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
  
  /** 是否为弹出菜单 */
  isMenuPopup: boolean
  
  /** 标题样式 */
  titleStyle: object
  
  /** 子菜单样式 */
  subMenuStyle: object
  
  /** 是否为第一级 */
  isFirstLevel: boolean
}

// 定义 FbSubMenu 组件的 Slots 类型
export interface FbSubMenuSlots {
  /** 标题插槽 */
  title: VNode[]
  
  /** 默认插槽 */
  default: VNode[]
}

// 定义 FbSubMenu 组件实例类型
export interface FbSubMenu extends Vue, FbSubMenuProps, FbSubMenuData, FbSubMenuComputed {
  $slots: FbSubMenuSlots
  
  /** 处理折叠切换 */
  handleCollapseToggle(value: boolean): void
  
  /** 添加菜单项 */
  addItem(item: object): void
  
  /** 移除菜单项 */
  removeItem(item: object): void
  
  /** 添加子菜单 */
  addSubmenu(item: object): void
  
  /** 移除子菜单 */
  removeSubmenu(item: object): void
  
  /** 处理点击 */
  handleClick(): void
  
  /** 处理鼠标进入 */
  handleMouseenter(event: Event, showTimeout?: number): void
  
  /** 处理鼠标离开 */
  handleMouseleave(deepDispatch?: boolean): void
  
  /** 处理标题鼠标进入 */
  handleTitleMouseenter(): void
  
  /** 处理标题鼠标离开 */
  handleTitleMouseleave(): void
  
  /** 更新位置 */
  updatePlacement(): void
  
  /** 初始化popper */
  initPopper(): void
}

// 定义 FbSubMenu 组件构造函数类型
export interface FbSubMenuConstructor extends VueConstructor<FbSubMenu> {}

// 导出 FbSubMenu 组件类型
export const FbSubMenu: FbSubMenuConstructor

// 默认导出
export default FbSubMenu