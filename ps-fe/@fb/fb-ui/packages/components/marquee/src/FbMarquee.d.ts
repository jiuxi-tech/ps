import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbMarquee 组件的 Props 类型
export interface FbMarqueeProps {
  /** 是否垂直滚动 */
  vertical?: boolean
  
  /** 滚动方向 */
  direction?: string
  
  /** 滚动持续时间 */
  duration?: number
  
  /** 延迟时间 */
  delay?: number
  
  /** 循环次数 */
  loop?: number
  
  /** 是否克隆 */
  clone?: boolean
  
  /** 是否显示渐变 */
  gradient?: boolean
  
  /** 渐变颜色 */
  gradientColor?: object | any[] | string
  
  /** 渐变宽度 */
  gradientWidth?: string
  
  /** 是否在悬停时暂停 */
  pauseOnHover?: boolean
  
  /** 是否在点击时暂停 */
  pauseOnClick?: boolean
}

// 定义 FbMarquee 组件的 Data 属性类型
export interface FbMarqueeData {
  /** 组件前缀 */
  prefix: string
  
  /** 克隆数量 */
  cloneAmount: number
  
  /** 最小宽度 */
  minWidth: string
  
  /** 最小高度 */
  minHeight: string
  
  /** 组件key */
  componentKey: number
  
  /** 是否暂停动画 */
  pauseAnimation: boolean
  
  /** 容器宽度 */
  containerWidth: number
  
  /** 内容宽度 */
  contentWidth: number
  
  /** 容器高度 */
  containerHeight: number
  
  /** 内容高度 */
  contentHeight: number
  
  /** 循环计数器 */
  loopCounter: number
  
  /** 循环间隔 */
  loopInterval: any
  
  /** 渐变长度 */
  gradientLength: string
  
  /** 是否准备就绪 */
  ready: boolean
}

// 定义 FbMarquee 组件的 Computed 属性类型
export interface FbMarqueeComputed {
  /** 当前样式 */
  getCurrentStyle: object
  
  /** 是否显示渐变 */
  showGradient: boolean
}

// 定义 FbMarquee 组件的 Slots 类型
export interface FbMarqueeSlots {
  /** 默认插槽 */
  default: VNode[]
}

// 定义 FbMarquee 组件实例类型
export interface FbMarquee extends Vue, FbMarqueeProps, FbMarqueeData, FbMarqueeComputed {
  $slots: FbMarqueeSlots
  
  /** 初始化 */
  init(): void
  
  /** 设置滚动 */
  setupMarquee(): void
  
  /** 强制更新 */
  ForcesUpdate(): void
  
  /** 检查克隆 */
  checkForClone(): void
  
  /** 悬停开始 */
  hoverStarted(): void
  
  /** 悬停结束 */
  hoverEnded(): void
  
  /** 鼠标按下 */
  mouseDown(): void
  
  /** 鼠标抬起 */
  mouseUp(): void
}

// 定义 FbMarquee 组件构造函数类型
export interface FbMarqueeConstructor extends VueConstructor<FbMarquee> {}

// 导出 FbMarquee 组件类型
export const FbMarquee: FbMarqueeConstructor

// 默认导出
export default FbMarquee