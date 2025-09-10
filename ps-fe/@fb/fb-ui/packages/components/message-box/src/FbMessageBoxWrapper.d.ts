import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbMessageBoxWrapper 组件的 Props 类型
export interface FbMessageBoxWrapperProps {
  /** 是否锁定 */
  lock?: boolean
}

// 定义 FbMessageBoxWrapper 组件的 Data 属性类型
export interface FbMessageBoxWrapperData {
  /** 组件前缀 */
  prefix: string
  
  /** 是否显示 */
  show: boolean
}

// 定义 FbMessageBoxWrapper 组件的 Computed 属性类型
// 暂无计算属性

// 定义 FbMessageBoxWrapper 组件的 Slots 类型
export interface FbMessageBoxWrapperSlots {
  /** 默认插槽 */
  default: VNode[]
}

// 定义 FbMessageBoxWrapper 组件实例类型
export interface FbMessageBoxWrapper extends Vue, FbMessageBoxWrapperProps, FbMessageBoxWrapperData {
  $slots: FbMessageBoxWrapperSlots
  
  /** 隐藏 */
  hide(): void
}

// 定义 FbMessageBoxWrapper 组件构造函数类型
export interface FbMessageBoxWrapperConstructor extends VueConstructor<FbMessageBoxWrapper> {}

// 导出 FbMessageBoxWrapper 组件类型
export const FbMessageBoxWrapper: FbMessageBoxWrapperConstructor

// 默认导出
export default FbMessageBoxWrapper