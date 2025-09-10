import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbDrawer 组件的 Props 类型
export interface FbDrawerProps {
  /** 是否显示抽屉 */
  value?: boolean
  
  /** 标题 */
  title?: string
  
  /** 位置 */
  position?: 'top' | 'right' | 'bottom' | 'left'
  
  /** 是否自动显示 */
  autoShow?: boolean
  
  /** 宽度 */
  width?: string | number
  
  /** 高度 */
  height?: string | number
  
  /** 是否支持ESC键关闭 */
  esc?: boolean
  
  /** 是否锁屏 */
  lock?: boolean
  
  /** 是否在body中 */
  inBody?: boolean
  
  /** 是否显示关闭按钮 */
  showCloseBtn?: boolean
  
  /** 关闭前的回调函数 */
  beforeClose?: Function
  
  /** 打开前的回调函数 */
  beforeOpen?: Function
}

// 定义 FbDrawer 组件的 Data 属性类型
export interface FbDrawerData {
  /** 组件前缀 */
  prefix: string
  
  /** 是否显示 */
  isShow: boolean
  
  /** 是否显示内容 */
  showContent: boolean
}

// 定义 FbDrawer 组件的 Computed 属性类型
export interface FbDrawerComputed {
  /** 抽屉样式 */
  getDrawerStyle: object
  
  /** 类名 */
  getClass: string[]
}

// 定义 FbDrawer 组件的 Slots 类型
export interface FbDrawerSlots {
  /** 默认插槽 */
  default: VNode[]
  
  /** 底部插槽 */
  footer: VNode[]
}

// 定义 FbDrawer 组件实例类型
export interface FbDrawer extends Vue, FbDrawerProps, FbDrawerData, FbDrawerComputed {
  $slots: FbDrawerSlots
  
  /** 打开后回调 */
  afterEnter(): void
  
  /** 关闭后回调 */
  afterLeave(): void
  
  /** 隐藏 */
  hide(): void
  
  /** 显示 */
  show(): void
  
  /** 打开 */
  open(): void
  
  /** 关闭 */
  close(): void
  
  /** ESC键处理 */
  handleEsc(event: KeyboardEvent): void
}

// 定义 FbDrawer 组件构造函数类型
export interface FbDrawerConstructor extends VueConstructor<FbDrawer> {}

// 导出 FbDrawer 组件类型
export const FbDrawer: FbDrawerConstructor

// 默认导出
export default FbDrawer