import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbBadge 组件的 Props 类型
export interface FbBadgeProps {
  /** 自定义小圆点的颜色 */
  dotColor?: string
  
  /** 展示的数字 */
  count?: number | string
  
  /** 展示的数字型号 */
  countSize?: string
  
  /** 展示的小圆点 */
  dot?: boolean
  
  /** 小圆点大小 */
  dotSize?: number | string
  
  /** 垂直对齐 */
  vertical?: string
  
  /** 设置状态点的位置偏移，格式为 [x, y] */
  offset?: Array<any>
  
  /** 展示封顶的数字值 */
  overflowCount?: number | string
  
  /** 当数值为 0 时，是否展示 Badge */
  showZero?: boolean
  
  /** 设置 Badge 为状态点 */
  type?: string
  
  /** 设置状态点的样式 */
  numberStyle?: object
  
  /** 设置鼠标放在状态点上时显示的文字 */
  title?: string
}

// 定义 FbBadge 组件的 Data 属性类型
export interface FbBadgeData {
  /** 组件前缀 */
  prefix: string
  
  /** 徽章文本 */
  badgeText: number | string
  
  /** 是否显示计数 */
  showCount: boolean
}

// 定义 FbBadge 组件的 Computed 属性类型
export interface FbBadgeComputed {
  /** 徽章类名 */
  getBadgeClass: string[]
  
  /** 徽章样式 */
  getBadgeStyle: object
  
  /** 计数类名 */
  getCountClass: string[]
  
  /** 计数样式 */
  getCountStyle: object
  
  /** 圆点类名 */
  getDotClass: string[]
  
  /** 圆点样式 */
  getDotStyle: object
}

// 定义 FbBadge 组件的 Slots 类型
export interface FbBadgeSlots {
  /** 默认插槽 */
  default: VNode[]
  
  /** 计数插槽 */
  count: VNode[]
}

// 定义 FbBadge 组件实例类型
export interface FbBadge extends Vue, FbBadgeProps, FbBadgeData, FbBadgeComputed {
  $slots: FbBadgeSlots
  
  /** 初始化徽章文本 */
  initBadgeText(): void
  
  /** 计算偏移 */
  calcOffset(obj: object): object
}

// 定义 FbBadge 组件构造函数类型
export interface FbBadgeConstructor extends VueConstructor<FbBadge> {}

// 导出 FbBadge 组件类型
export const FbBadge: FbBadgeConstructor

// 默认导出
export default FbBadge