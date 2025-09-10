import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbListRoll 组件的 Props 类型
export interface FbListRollProps {
  /** 数据列表 */
  list?: any[]
  
  /** 父容器类名 */
  fatherClass?: string
  
  /** 主体类名 */
  bodyClass?: string
  
  /** 主体样式 */
  bodyStyle?: any[] | object | string
  
  /** 模式 */
  mode?: string
  
  /** 是否开启自动滚动 */
  scroll?: boolean
  
  /** 滚动速度 */
  speed?: number
  
  /** 滚动方向 */
  direction?: 'horizontal' | 'vertical'
}

// 定义 FbListRoll 组件的 Data 属性类型
export interface FbListRollData {
  /** 组件前缀 */
  prefix: string
  
  /** 定时器 */
  timer: any
}

// 定义 FbListRoll 组件的 Computed 属性类型
export interface FbListRollComputed {
  /** 父容器类名 */
  getFatherClass: string[]
  
  /** 主体类名 */
  getBodyClass: string[]
}

// 定义 FbListRoll 组件的 Slots 类型
export interface FbListRollSlots {
  /** 默认插槽 */
  default: VNode[]
}

// 定义 FbListRoll 组件实例类型
export interface FbListRoll extends Vue, FbListRollProps, FbListRollData, FbListRollComputed {
  $slots: FbListRollSlots
  
  /** 初始化滚动 */
  initScroll(): void
  
  /** 清除滚动 */
  clearScroll(): void
}

// 定义 FbListRoll 组件构造函数类型
export interface FbListRollConstructor extends VueConstructor<FbListRoll> {}

// 导出 FbListRoll 组件类型
export const FbListRoll: FbListRollConstructor

// 默认导出
export default FbListRoll