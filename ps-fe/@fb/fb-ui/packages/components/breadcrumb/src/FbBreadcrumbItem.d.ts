import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbBreadcrumbItem 组件的 Props 类型
export interface FbBreadcrumbItemProps {
  /** 尺寸 */
  size?: string
  
  /** 分割符 */
  separator?: string
  
  /** 链接地址 */
  href?: string
  
  /** 路由地址 */
  to?: string | object
  
  /** 是否替换 */
  replace?: boolean
  
  /** 是否追加 */
  append?: boolean
  
  /** 目标 */
  target?: string
  
  /** 图标 */
  icon?: string
  
  /** 标签 */
  label?: string
  
  /** 标题 */
  title?: string
  
  /** 颜色 */
  color?: string
  
  /** 下划线 */
  underline?: string
}

// 定义 FbBreadcrumbItem 组件的 Data 属性类型
export interface FbBreadcrumbItemData {
  /** 组件前缀 */
  prefix: string
  
  /** 分割符 */
  mySeparator: string
  
  /** 下划线 */
  myUnderline: string
  
  /** 面包屑组件 */
  breadcrumb: any
}

// 定义 FbBreadcrumbItem 组件的 Computed 属性类型
export interface FbBreadcrumbItemComputed {
  /** 链接类名 */
  getLinkClass: string[]
}

// 定义 FbBreadcrumbItem 组件的 Slots 类型
export interface FbBreadcrumbItemSlots {
  /** 默认插槽 */
  default: VNode[]
  
  /** 分割符插槽 */
  separator: VNode[]
}

// 定义 FbBreadcrumbItem 组件实例类型
export interface FbBreadcrumbItem extends Vue, FbBreadcrumbItemProps, FbBreadcrumbItemData, FbBreadcrumbItemComputed {
  $slots: FbBreadcrumbItemSlots
}

// 定义 FbBreadcrumbItem 组件构造函数类型
export interface FbBreadcrumbItemConstructor extends VueConstructor<FbBreadcrumbItem> {}

// 导出 FbBreadcrumbItem 组件类型
export const FbBreadcrumbItem: FbBreadcrumbItemConstructor

// 默认导出
export default FbBreadcrumbItem