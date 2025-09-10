import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbNotification 组件的 Props 类型
export interface FbNotificationProps {
  /** 标题 */
  title?: string
  
  /** 消息内容 */
  message?: string | number
  
  /** 宽度 */
  width?: string | number
  
  /** 类型 */
  type?: string
  
  /** 是否显示确定按钮 */
  showOkButton?: boolean
  
  /** 确定按钮文字 */
  okButtonText?: string
  
  /** 确定回调 */
  callback?: Function
  
  /** 图标 */
  icon?: string
  
  /** 图标颜色 */
  iconColor?: string
  
  /** 是否显示图标 */
  iconShow?: boolean
}

// 定义 FbNotification 组件的 Data 属性类型
export interface FbNotificationData {
  /** 组件前缀 */
  prefix: string
  
  /** 操作 */
  action: string
  
  /** 是否可见 */
  visible: boolean
  
  /** 包装器 */
  wrapper: any
  
  /** 图标映射 */
  icons: object
}

// 定义 FbNotification 组件的 Computed 属性类型
export interface FbNotificationComputed {
  /** 图标类型 */
  myType: string
  
  /** 类名 */
  getClass: string[]
}

// 定义 FbNotification 组件的 Slots 类型
// 暂无插槽

// 定义 FbNotification 组件实例类型
export interface FbNotification extends Vue, FbNotificationProps, FbNotificationData, FbNotificationComputed {
  $slots: object
  
  /** 获取尺寸样式 */
  getSizeStyle(value: string | number): string
  
  /** 处理操作 */
  handleAction(action: string): void
  
  /** 关闭 */
  doClose(): void
  
  /** 处理关闭图标点击 */
  handleCloseIconClick(): void
}

// 定义 FbNotification 组件构造函数类型
export interface FbNotificationConstructor extends VueConstructor<FbNotification> {}

// 导出 FbNotification 组件类型
export const FbNotification: FbNotificationConstructor

// 默认导出
export default FbNotification