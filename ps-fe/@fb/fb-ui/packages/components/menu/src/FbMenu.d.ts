import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbMenu 组件的 Props 类型
export interface FbMenuProps {
  /** 模式 */
  mode?: 'horizontal' | 'vertical' | 'inline'
  
  /** 主题 */
  theme?: string
  
  /** 默认激活的菜单项 */
  defaultActive?: string
  
  /** 当前激活的菜单项 */
  value?: string | any[]
  
  /** 菜单触发方式 */
  menuTrigger?: string
  
  /** 是否只保持一个子菜单展开 */
  uniqueOpened?: boolean
  
  /** 默认展开的菜单项 */
  defaultOpeneds?: any[]
  
  /** 是否水平折叠 */
  inlineCollapse?: boolean
  
  /** 是否开启折叠动画 */
  collapseTransition?: boolean
  
  /** 左边距 */
  paddingLeftNum?: number
  
  /** 背景颜色 */
  backgroundColor?: string
  
  /** 文字颜色 */
  textColor?: string
  
  /** 激活文字颜色 */
  activeTextColor?: string
}

// 定义 FbMenu 组件的 Data 属性类型
export interface FbMenuData {
  /** 组件前缀 */
  prefix: string
  
  /** 激活的索引 */
  activeIndex: string | any[]
  
  /** 展开的菜单 */
  openedMenus: any[]
  
  /** 菜单项 */
  items: object
  
  /** 子菜单 */
  submenus: object
}

// 定义 FbMenu 组件的 Computed 属性类型
export interface FbMenuComputed {
  /** 悬停背景色 */
  hoverBackground: string
  
  /** 是否为弹出菜单 */
  isMenuPopup: boolean
}

// 定义 FbMenu 组件的 Slots 类型
export interface FbMenuSlots {
  /** 默认插槽 */
  default: VNode[]
}

// 定义 FbMenu 组件实例类型
export interface FbMenu extends Vue, FbMenuProps, FbMenuData, FbMenuComputed {
  $slots: FbMenuSlots
  
  /** 更新激活索引 */
  updateActiveIndex(val: string | any[]): void
  
  /** 获取迁移配置 */
  getMigratingConfig(): object
  
  /** 获取颜色通道 */
  getColorChannels(color: string): object
  
  /** 混合颜色 */
  mixColor(color: string, percent: number): string
  
  /** 添加菜单项 */
  addItem(item: object): void
  
  /** 移除菜单项 */
  removeItem(item: object): void
  
  /** 添加子菜单 */
  addSubmenu(item: object): void
  
  /** 移除子菜单 */
  removeSubmenu(item: object): void
  
  /** 展开菜单 */
  openMenu(index: string, indexPath: any[]): void
  
  /** 关闭菜单 */
  closeMenu(index: string): void
  
  /** 处理子菜单点击 */
  handleSubmenuClick(submenu: object): void
  
  /** 处理菜单项点击 */
  handleItemClick(item: object): void
  
  /** 初始化展开菜单 */
  initOpenedMenu(): void
  
  /** 路由跳转 */
  routeToItem(item: object, onError: Function): void
  
  /** 展开菜单 */
  open(index: string): void
  
  /** 关闭菜单 */
  close(index: string): void
}

// 定义 FbMenu 组件构造函数类型
export interface FbMenuConstructor extends VueConstructor<FbMenu> {}

// 导出 FbMenu 组件类型
export const FbMenu: FbMenuConstructor

// 默认导出
export default FbMenu