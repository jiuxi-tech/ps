import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbButton 组件的 Props 类型
export interface FbButtonProps {
  /** 按钮的值 */
  value?: string
  
  /** 复制的文本 */
  copy?: string
  
  /** 按钮名称 */
  name?: string
  
  /** 类型 */
  type?: string
  
  /** 尺寸 */
  size?: string
  
  /** 是否100%宽度 */
  long?: boolean
  
  /** 是否悬停状态 */
  hover?: boolean
  
  /** 按钮元素的类型 */
  elType?: string
  
  /** 是否加载中 */
  loading?: boolean
  
  /** 是否危险按钮 */
  danger?: boolean
  
  /** 是否编辑器按钮 */
  editor?: boolean
  
  /** 是否警告按钮 */
  warning?: boolean
  
  /** 是否成功按钮 */
  success?: boolean
  
  /** 是否禁用 */
  disabled?: boolean
  
  /** 是否清爽按钮 */
  plain?: boolean
  
  /** 是否自动获取焦点 */
  autofocus?: boolean
  
  /** 是否圆角 */
  round?: boolean
  
  /** 是否圆形按钮 */
  circle?: boolean
  
  /** 图标名称 */
  icon?: string
  
  /** 后置图标 */
  appendIcon?: string
}

// 定义 FbButton 组件的 Data 属性类型
export interface FbButtonData {
  /** 组件前缀 */
  prefix: string
  
  /** 是否鼠标按下 */
  mousedown: boolean
  
  /** 是否复制中 */
  copying: boolean
  
  /** 图标 */
  myIcon: string
  
  /** 定时器 */
  timer: any
}

// 定义 FbButton 组件的 Computed 属性类型
// 暂无计算属性

// 定义 FbButton 组件的 Slots 类型
export interface FbButtonSlots {
  /** 默认插槽 */
  default: VNode[]
}

// 定义 FbButton 组件实例类型
export interface FbButton extends Vue, FbButtonProps, FbButtonData {
  $slots: FbButtonSlots
  
  /** 点击事件处理 */
  handleClick(event: Event): void
  
  /** 执行复制 */
  doCopy(): void
  
  /** 显示复制动画 */
  showCopyAnimation(): void
}

// 定义 FbButton 组件构造函数类型
export interface FbButtonConstructor extends VueConstructor<FbButton> {}

// 导出 FbButton 组件类型
export const FbButton: FbButtonConstructor

// 默认导出
export default FbButton