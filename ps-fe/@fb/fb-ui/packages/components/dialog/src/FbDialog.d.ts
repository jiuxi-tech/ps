import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbDialog 组件的 Props 类型
export interface FbDialogProps {
  /** 是否显示对话框 */
  value?: boolean
  
  /** 标题 */
  title?: string
  
  /** 子标题 */
  subTitle?: string
  
  /** 宽度 */
  width?: number | string
  
  /** 高度 */
  height?: number | string
  
  /** 点击阴影部分关闭窗口 */
  closeOnClickShadow?: boolean
  
  /** 禁用ESC键关闭 */
  disableEsc?: boolean
  
  /** 是否显示最大化按钮 */
  canFullScreen?: boolean
  
  /** 是否显示关闭按钮 */
  showCloseBtn?: boolean
  
  /** 关闭前的回调函数 */
  beforeClose?: Function
  
  /** 是否自动显示 */
  autoShow?: boolean
  
  /** 是否锁屏 */
  lock?: boolean
  
  /** 加载器函数 */
  loader?: Function
  
  /** 是否无内边距 */
  noPadding?: boolean
  
  /** 距顶部距离 */
  top?: string | number
  
  /** 距左侧距离 */
  left?: string | number
  
  /** 溢出处理 */
  overflow?: string
  
  /** 对话框样式 */
  dialogStyle?: string | Array<any> | object
  
  /** 包装器样式 */
  wrapperStyle?: string | Array<any> | object
  
  /** 是否可拖拽 */
  drag?: boolean
  
  /** 是否可调整大小 */
  resize?: boolean
  
  /** 是否在body中 */
  inBody?: boolean
}

// 定义 FbDialog 组件的 Data 属性类型
export interface FbDialogData {
  /** 默认属性 */
  defaultProps: any
  
  /** 组件前缀 */
  prefix: string
  
  /** 组件 */
  isComponent: any
  
  /** URL */
  myUrl: string | null
  
  /** 组件实例 */
  myComponent: any
  
  /** 组件数据 */
  myComponentData: any
  
  /** 标题 */
  myTitle: string
  
  /** 宽度 */
  myWidth: number | string
  
  /** 高度 */
  myHeight: number | string
  
  /** 关闭前回调 */
  myBeforeClose: Function | undefined
  
  /** 是否显示 */
  isShow: boolean
  
  /** 是否滚动 */
  isScroll: boolean
  
  /** 是否全屏 */
  isFullScreen: boolean
  
  /** 是否可见 */
  visibility: boolean
  
  /** 距顶部距离 */
  myTop: string
  
  /** 距左侧距离 */
  myLeft: string
}

// 定义 FbDialog 组件的 Computed 属性类型
export interface FbDialogComputed {
  /** 样式 */
  getStyle: object
}

// 定义 FbDialog 组件的 Slots 类型
export interface FbDialogSlots {
  /** 标题插槽 */
  title: VNode[]
  
  /** 子标题插槽 */
  subTitle: VNode[]
  
  /** 默认插槽 */
  default: VNode[]
}

// 定义 FbDialog 组件实例类型
export interface FbDialog extends Vue, FbDialogProps, FbDialogData, FbDialogComputed {
  $slots: FbDialogSlots
  
  /** 计算滚动 */
  computeScroll(): void
  
  /** ESC键处理 */
  esc(event: KeyboardEvent): void
  
  /** 点击处理 */
  clickFn(event: Event): void
  
  /** 关闭处理 */
  handleClose(data?: any): Promise<void>
  
  /** 关闭 */
  close(): void
  
  /** 隐藏 */
  hide(data?: any): void
  
  /** 滚动处理 */
  scroll(event: Event): void
  
  /** 显示 */
  show(options?: any): Promise<void>
  
  /** 全屏切换 */
  fullScreen(): void
  
  /** 调整大小处理 */
  handleResize(): void
}

// 定义 FbDialog 组件构造函数类型
export interface FbDialogConstructor extends VueConstructor<FbDialog> {}

// 导出 FbDialog 组件类型
export const FbDialog: FbDialogConstructor

// 默认导出
export default FbDialog