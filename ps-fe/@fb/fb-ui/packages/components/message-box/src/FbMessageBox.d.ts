import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbMessageBox 组件的 Props 类型
export interface FbMessageBoxProps {
  /** 标题 */
  title?: string
  
  /** 标题样式 */
  titleStyle?: string | object | any[]
  
  /** 对齐方式 */
  align?: string
  
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
  
  /** 是否显示确认按钮 */
  showConfirmButton?: boolean
  
  /** 确认按钮文字 */
  confirmButtonText?: string
  
  /** 是否显示取消按钮 */
  showCancelButton?: boolean
  
  /** 取消按钮文字 */
  cancelButtonText?: string
  
  /** 是否显示关闭图标 */
  showClose?: boolean
  
  /** 取消回调 */
  fallback?: Function
  
  /** 确认回调 */
  callback?: Function
  
  /** 图标 */
  icon?: string
}

// 定义 FbMessageBox 组件的 Data 属性类型
export interface FbMessageBoxData {
  /** 组件前缀 */
  prefix: string
  
  /** 操作 */
  action: string
  
  /** 是否可见 */
  visible: boolean
  
  /** 图标映射 */
  icons: object
}

// 定义 FbMessageBox 组件的 Computed 属性类型
export interface FbMessageBoxComputed {
  /** 图标类型 */
  myType: string
}

// 定义 FbMessageBox 组件的 Slots 类型
// 暂无插槽

// 定义 FbMessageBox 组件实例类型
export interface FbMessageBox extends Vue, FbMessageBoxProps, FbMessageBoxData, FbMessageBoxComputed {
  $slots: object
  
  /** 获取尺寸样式 */
  getSizeStyle(value: string | number): string
  
  /** 处理操作 */
  handleAction(action: string): void
  
  /** 关闭 */
  doClose(): void
  
  /** ESC键处理 */
  esc(event: KeyboardEvent): void
}

// 定义 FbMessageBox 组件构造函数类型
export interface FbMessageBoxConstructor extends VueConstructor<FbMessageBox> {}

// 导出 FbMessageBox 组件类型
export const FbMessageBox: FbMessageBoxConstructor

// 默认导出
export default FbMessageBox