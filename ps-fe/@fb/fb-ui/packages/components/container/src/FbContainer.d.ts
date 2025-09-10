import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbContainer 组件的 Props 类型
export interface FbContainerProps {
  /** 是否为块级元素 */
  block?: boolean
  
  /** 是否为弹性布局 */
  flex?: boolean
  
  /** 是否为内联元素 */
  inline?: boolean
  
  /** 是否为相对定位 */
  relative?: boolean
  
  /** 是否为绝对定位 */
  absolute?: boolean
  
  /** 是否为固定定位 */
  fixed?: boolean
  
  /** 是否为粘性定位 */
  sticky?: boolean
  
  /** 是否省略文本 */
  ellipsis?: boolean
  
  /** 宽度 */
  width?: string | number
  
  /** 高度 */
  height?: string | number
  
  /** 左外边距 */
  ml?: string | number
  
  /** 右外边距 */
  mr?: string | number
  
  /** 下外边距 */
  mb?: string | number
  
  /** 上外边距 */
  mt?: string | number
  
  /** 左内边距 */
  pl?: string | number
  
  /** 右内边距 */
  pr?: string | number
  
  /** 下内边距 */
  pb?: string | number
  
  /** 上内边距 */
  pt?: string | number
  
  /** 文本对齐方式 */
  align?: string
  
  /** 垂直对齐方式 */
  valign?: string
  
  /** 定位方式 */
  position?: string
  
  /** 显示方式 */
  display?: string
  
  /** 距顶部距离 */
  top?: string | number
  
  /** 距左侧距离 */
  left?: string | number
  
  /** 距右侧距离 */
  right?: string | number
  
  /** 距底部距离 */
  bottom?: string | number
  
  /** 圆角 */
  radius?: string
  
  /** 背景色 */
  background?: string
  
  /** 文字颜色 */
  color?: string
  
  /** 边框 */
  border?: string
  
  /** 上边框 */
  bt?: string
  
  /** 右边框 */
  br?: string
  
  /** 下边框 */
  bb?: string
  
  /** 左边框 */
  bl?: string
  
  /** 内边距 */
  padding?: string | number
  
  /** 外边距 */
  margin?: string | number
  
  /** 层级 */
  zIndex?: string | number
  
  /** 溢出处理 */
  overflow?: string
  
  /** 水平溢出处理 */
  overflowX?: string
  
  /** 垂直溢出处理 */
  overflowY?: string
  
  /** 最大高度 */
  maxHeight?: string | number
  
  /** 最小高度 */
  minHeight?: string | number
  
  /** 最大宽度 */
  maxWidth?: string | number
  
  /** 最小宽度 */
  minWidth?: string | number
  
  /** 鼠标样式 */
  cursor?: string
  
  /** 垂直对齐 */
  verticalAlign?: string
  
  /** 垂直对齐简写 */
  va?: string
  
  /** 行高 */
  lh?: string
  
  /** 弹性布局方向 */
  flexDirection?: string
  
  /** 弹性布局换行 */
  flexWrap?: string
  
  /** 内容对齐方式 */
  alignContent?: string
  
  /** 内容居中 */
  acCenter?: boolean
  
  /** 内容两端对齐 */
  acSpaceBetween?: boolean
  
  /** 内容环绕对齐 */
  acSpaceAround?: boolean
  
  /** 内容起始对齐 */
  acFlexStart?: boolean
  
  /** 内容结束对齐 */
  acFlexEnd?: boolean
  
  /** 主轴对齐方式 */
  justifyContent?: string
  
  /** 主轴居中 */
  jcCenter?: boolean
  
  /** 主轴起始对齐 */
  jcFlexStart?: boolean
  
  /** 主轴结束对齐 */
  jcFlexEnd?: boolean
  
  /** 主轴两端对齐 */
  jcSpaceBetween?: boolean
  
  /** 主轴环绕对齐 */
  jcSpaceAround?: boolean
  
  /** 主轴均匀对齐 */
  jcSpaceEvenly?: boolean
  
  /** 交叉轴对齐方式 */
  alignItems?: string
  
  /** 交叉轴居中 */
  aiCenter?: boolean
  
  /** 交叉轴起始对齐 */
  aiFlexStart?: boolean
  
  /** 交叉轴结束对齐 */
  aiFlexEnd?: boolean
  
  /** 交叉轴两端对齐 */
  aiSpaceBetween?: boolean
  
  /** 交叉轴环绕对齐 */
  aiSpaceAround?: boolean
  
  /** 交叉轴基线对齐 */
  aiBaseline?: boolean
}

// 定义 FbContainer 组件的 Data 属性类型
export interface FbContainerData {
  /** 组件前缀 */
  prefix: string
}

// 定义 FbContainer 组件的 Computed 属性类型
export interface FbContainerComputed {
  /** 类名 */
  getClass: string[]
  
  /** 样式 */
  getStyle: object
}

// 定义 FbContainer 组件的 Slots 类型
export interface FbContainerSlots {
  /** 默认插槽 */
  default: VNode[]
}

// 定义 FbContainer 组件实例类型
export interface FbContainer extends Vue, FbContainerProps, FbContainerData, FbContainerComputed {
  $slots: FbContainerSlots
  
  /** 点击事件处理 */
  handleClick(e: Event): void
  
  /** 单位转换 */
  unit(val: string | number): string
}

// 定义 FbContainer 组件构造函数类型
export interface FbContainerConstructor extends VueConstructor<FbContainer> {}

// 导出 FbContainer 组件类型
export const FbContainer: FbContainerConstructor

// 默认导出
export default FbContainer