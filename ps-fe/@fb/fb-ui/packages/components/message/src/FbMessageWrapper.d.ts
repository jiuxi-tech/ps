import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbMessageWrapper 组件的 Props 类型
export interface FbMessageWrapperProps {
  /** 对齐方式 */
  align?: string
}

// 定义 FbMessageWrapper 组件的 Data 属性类型
export interface FbMessageWrapperData {
  /** 组件前缀 */
  prefix: string
}

// 定义 FbMessageWrapper 组件的 Computed 属性类型
export interface FbMessageWrapperComputed {
  /** 类名 */
  getClass: string[]
}

// 定义 FbMessageWrapper 组件的 Slots 类型
// 暂无插槽

// 定义 FbMessageWrapper 组件实例类型
export interface FbMessageWrapper extends Vue, FbMessageWrapperProps, FbMessageWrapperData, FbMessageWrapperComputed {
  $slots: object
}

// 定义 FbMessageWrapper 组件构造函数类型
export interface FbMessageWrapperConstructor extends VueConstructor<FbMessageWrapper> {}

// 导出 FbMessageWrapper 组件类型
export const FbMessageWrapper: FbMessageWrapperConstructor

// 默认导出
export default FbMessageWrapper