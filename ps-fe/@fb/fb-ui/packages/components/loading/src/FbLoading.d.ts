import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbLoading 组件的 Props 类型
export interface FbLoadingProps {
  /** 颜色 */
  color?: string
  
  /** 文本颜色 */
  textColor?: string
  
  /** 大小 */
  size?: string
  
  /** 边框宽度 */
  borderWidth?: string
  
  /** 文本 */
  text?: string | number
  
  /** 位置 */
  position?: string
  
  /** 旋转器样式 */
  spinStyle?: object
  
  /** 是否显示点 */
  dot?: boolean
  
  /** 旋转器图标 */
  spinner?: string
  
  /** 类型 */
  type?: string
  
  /** SVG类型 */
  svg?: string
  
  /** CSS类型 */
  css?: string
}

// 定义 FbLoading 组件的 Data 属性类型
export interface FbLoadingData {
  /** 组件前缀 */
  prefix: string
}

// 定义 FbLoading 组件的 Computed 属性类型
export interface FbLoadingComputed {
  /** 大小 */
  getSize: string
  
  /** 样式 */
  getStyle: object
}

// 定义 FbLoading 组件的 Slots 类型
export interface FbLoadingSlots {
  /** 文本插槽 */
  text: VNode[]
  
  /** 默认插槽 */
  default: VNode[]
}

// 定义 FbLoading 组件实例类型
export interface FbLoading extends Vue, FbLoadingProps, FbLoadingData, FbLoadingComputed {
  $slots: FbLoadingSlots
}

// 定义 FbLoading 组件构造函数类型
export interface FbLoadingConstructor extends VueConstructor<FbLoading> {}

// 导出 FbLoading 组件类型
export const FbLoading: FbLoadingConstructor

// 默认导出
export default FbLoading