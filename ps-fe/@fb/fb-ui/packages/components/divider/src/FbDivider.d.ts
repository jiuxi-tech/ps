import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbDivider 组件的 Props 类型
export interface FbDividerProps {
  /** 标签文本 */
  label?: string
  
  /** 标签位置 */
  position?: 'left' | 'center' | 'right'
  
  /** 分割线方向 */
  direction?: 'horizontal' | 'vertical'
  
  /** 是否为虚线 */
  dashed?: boolean
  
  /** 是否无内边距 */
  noPadding?: boolean
  
  /** 是否无外边距 */
  noMargin?: boolean
  
  /** 外边距大小 */
  marginSize?: 'xs' | 's' | 'm' | 'l' | 'xl' | 'xxl'
}

// 定义 FbDivider 组件的 Data 属性类型
export interface FbDividerData {
  /** 组件前缀 */
  prefix: string
}

// 定义 FbDivider 组件的 Computed 属性类型
export interface FbDividerComputed {
  /** 样式 */
  getStyle: object
}

// 定义 FbDivider 组件的 Slots 类型
export interface FbDividerSlots {
  /** 默认插槽 */
  default: VNode[]
}

// 定义 FbDivider 组件实例类型
export interface FbDivider extends Vue, FbDividerProps, FbDividerData, FbDividerComputed {
  $slots: FbDividerSlots
}

// 定义 FbDivider 组件构造函数类型
export interface FbDividerConstructor extends VueConstructor<FbDivider> {}

// 导出 FbDivider 组件类型
export const FbDivider: FbDividerConstructor

// 默认导出
export default FbDivider