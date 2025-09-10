import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbBreadcrumb 组件的 Props 类型
export interface FbBreadcrumbProps {
  /** 尺寸 */
  size?: string
  
  /** 分割线 */
  separator?: string
  
  /** 数据 */
  data?: Array<any>
  
  /** 路由 */
  routes?: Array<any>
  
  /** 颜色 */
  color?: string
  
  /** 下划线 */
  underline?: string
  
  /** 是否追加 */
  append?: boolean
}

// 定义 FbBreadcrumb 组件的 Data 属性类型
export interface FbBreadcrumbData {
  /** 组件前缀 */
  prefix: string
  
  /** 数据 */
  myData: Array<any>
}

// 定义 FbBreadcrumb 组件的 Computed 属性类型
// 暂无计算属性

// 定义 FbBreadcrumb 组件的 Slots 类型
export interface FbBreadcrumbSlots {
  /** 默认插槽 */
  default: VNode[]
}

// 定义 FbBreadcrumb 组件实例类型
export interface FbBreadcrumb extends Vue, FbBreadcrumbProps, FbBreadcrumbData {
  $slots: FbBreadcrumbSlots
}

// 定义 FbBreadcrumb 组件构造函数类型
export interface FbBreadcrumbConstructor extends VueConstructor<FbBreadcrumb> {}

// 导出 FbBreadcrumb 组件类型
export const FbBreadcrumb: FbBreadcrumbConstructor

// 默认导出
export default FbBreadcrumb