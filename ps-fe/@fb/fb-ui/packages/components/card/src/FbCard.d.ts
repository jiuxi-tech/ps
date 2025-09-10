import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbCard 组件的 Props 类型
export interface FbCardProps {
  /** 卡片头部标题 */
  header?: string | number
  
  /** 阴影显示方式 */
  shadow?: 'on' | 'off' | 'hover'
  
  /** 外边距 */
  margin?: string | number
  
  /** 卡片高度 */
  height?: string | number
  
  /** 是否不显示边框 */
  noBorder?: boolean
  
  /** 是否不显示内边距 */
  noPadding?: boolean
  
  /** 内边距 */
  padding?: string | number
  
  /** body 样式 */
  bodyStyle?: string | object | object[]
  
  /** body 区域滚动设置 */
  bodyOverflow?: 'x' | 'y' | 'auto' | ''
  
  /** 标题样式 */
  titleStyle?: string | object | object[]
}

// 定义 FbCard 组件的 Computed 属性类型
export interface FbCardComputed {
  /** 卡片样式 */
  getCardStyle: object[]
  
  /** body 样式 */
  getBodyStyle: object[]
  
  /** 是否显示头部 */
  showHeader: boolean
}

// 定义 FbCard 组件的 Slots 类型
export interface FbCardSlots {
  /** 默认插槽 */
  default: VNode[]
  
  /** 标题插槽 */
  title: VNode[]
  
  /** 头部操作区域插槽 */
  actions: VNode[]
  
  /** 头部插槽 */
  header: VNode[]
  
  /** 底部插槽 */
  footer: VNode[]
}

// 定义 FbCard 组件实例类型
export interface FbCard extends Vue, FbCardProps, FbCardComputed {
  $slots: FbCardSlots
}

// 定义 FbCard 组件构造函数类型
export interface FbCardConstructor extends VueConstructor<FbCard> {}

// 导出 FbCard 组件类型
export const FbCard: FbCardConstructor

// 默认导出
export default FbCard