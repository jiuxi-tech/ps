import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbMessage 组件的 Props 类型
export interface FbMessageProps {
  /** 消息内容 */
  message?: string | number | Error
  
  /** 类型 */
  type?: string
}

// 定义 FbMessage 组件的 Data 属性类型
export interface FbMessageData {
  /** 组件前缀 */
  prefix: string
  
  /** 图标映射 */
  icons: object
}

// 定义 FbMessage 组件的 Computed 属性类型
export interface FbMessageComputed {
  /** 图标类型 */
  myType: string
}

// 定义 FbMessage 组件的 Slots 类型
// 暂无插槽

// 定义 FbMessage 组件实例类型
export interface FbMessage extends Vue, FbMessageProps, FbMessageData, FbMessageComputed {
  $slots: object
}

// 定义 FbMessage 组件构造函数类型
export interface FbMessageConstructor extends VueConstructor<FbMessage> {}

// 导出 FbMessage 组件类型
export const FbMessage: FbMessageConstructor

// 默认导出
export default FbMessage