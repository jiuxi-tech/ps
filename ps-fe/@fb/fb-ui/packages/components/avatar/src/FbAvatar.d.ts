import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbAvatar 组件的 Props 类型
export interface FbAvatarProps {
  /** 头像尺寸 */
  size?: string | number
  
  /** 是否为圆形 */
  circle?: boolean
  
  /** 图标名称 */
  icon?: string
  
  /** 文本颜色 */
  color?: string
  
  /** 背景颜色 */
  backgroundColor?: string
  
  /** 显示的文本 */
  text?: string | number
  
  /** 图片源地址 */
  src?: string
  
  /** 图片替代文本 */
  alt?: string
  
  /** 图片加载错误回调 */
  loadError?: (event: Event) => void
}

// 定义 FbAvatar 组件的 Data 属性类型
export interface FbAvatarData {
  /** 组件前缀 */
  prefix: string
  
  /** 尺寸数值 */
  sizeNumber: number
  
  /** 字体大小 */
  fontSize: number
  
  /** 缩放比例 */
  scale: number
  
  /** 图片是否存在 */
  isImgExist: boolean
  
  /** 上次子元素宽度 */
  lastChildrenWidth?: number
  
  /** 上次节点宽度 */
  lastNodeWidth?: number
  
  /** 是否已挂载 */
  isMounted?: boolean
}

// 定义 FbAvatar 组件的 Computed 属性类型
export interface FbAvatarComputed {
  /** 文本样式 */
  getTextStyle: object
  
  /** 尺寸样式 */
  mySizeStyle: object
  
  /** 最终样式 */
  getStyle: object
  
  /** 类名 */
  getClass: string[]
}

// 定义 FbAvatar 组件的 Slots 类型
export interface FbAvatarSlots {
  /** 默认插槽 */
  default: VNode[]
}

// 定义 FbAvatar 组件实例类型
export interface FbAvatar extends Vue, FbAvatarProps, FbAvatarData, FbAvatarComputed {
  $slots: FbAvatarSlots
  
  /** 处理图片加载错误 */
  handleImageLoadError(event: Event): void
  
  /** 设置缩放比例 */
  setScale(): void
}

// 定义 FbAvatar 组件构造函数类型
export interface FbAvatarConstructor extends VueConstructor<FbAvatar> {}

// 导出 FbAvatar 组件类型
export const FbAvatar: FbAvatarConstructor

// 默认导出
export default FbAvatar