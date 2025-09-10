import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbNotificationWrapper 组件的 Props 类型
export interface FbNotificationWrapperProps {
  /** 位置 */
  placement?: string
}

// 定义 FbNotificationWrapper 组件的 Data 属性类型
export interface FbNotificationWrapperData {
  /** 组件前缀 */
  prefix: string
  
  /** 是否显示 */
  show: boolean
}

// 定义 FbNotificationWrapper 组件的 Computed 属性类型
// 暂无计算属性

// 定义 FbNotificationWrapper 组件的 Slots 类型
export interface FbNotificationWrapperSlots {
  /** 默认插槽 */
  default: VNode[]
}

// 定义 FbNotificationWrapper 组件实例类型
export interface FbNotificationWrapper extends Vue, FbNotificationWrapperProps, FbNotificationWrapperData {
  $slots: FbNotificationWrapperSlots
  
  /** 隐藏 */
  hide(): void
}

// 定义 FbNotificationWrapper 组件构造函数类型
export interface FbNotificationWrapperConstructor extends VueConstructor<FbNotificationWrapper> {}

// 导出 FbNotificationWrapper 组件类型
export const FbNotificationWrapper: FbNotificationWrapperConstructor

// 默认导出
export default FbNotificationWrapper