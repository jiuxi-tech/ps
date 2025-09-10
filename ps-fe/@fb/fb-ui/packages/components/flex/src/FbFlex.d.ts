import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbFlex 组件的 Props 类型
export interface FbFlexProps {
  /** 是否为内联元素 */
  inline?: boolean
  
  /** 定位 */
  position?: string
  
  /** 布局相关属性 */
  layout?: object
  
  /** 字体族 */
  fontFamily?: string
  
  /** 字体样式 */
  fontStyle?: string
  
  /** 字体粗细 */
  fontWeight?: string | number
  
  /** 颜色 */
  color?: string
  
  /** 背景色 */
  background?: string
  
  /** 省略号 */
  ellipsis?: boolean
  
  /** 盒子阴影 */
  boxShadow?: string
  
  /** 边框 */
  border?: string
  
  /** 盒模型 */
  boxSizing?: string
  
  /** 外边距 */
  margin?: string | number
  
  /** 内边距 */
  padding?: string | number
  
  /** 滤镜 */
  filter?: string
  
  /** 显示方式 */
  display?: string
  
  /** 轮廓 */
  outline?: string
  
  /** 鼠标样式 */
  cursor?: string
  
  /** 垂直对齐 */
  verticalAlign?: string
  
  /** 行高 */
  lineHeight?: string | number
  
  /** 间隙 */
  gap?: string | number
  
  /** 弹性 */
  flex?: string | number
  
  /** 弹性增长 */
  flexGrow?: string | number
  
  /** 弹性收缩 */
  flexShrink?: string | number
  
  /** 弹性基础 */
  flexBasis?: string | number
  
  /** 弹性换行 */
  flexWrap?: string
  
  /** 弹性方向 */
  flexDirection?: string
  
  /** 弹性流 */
  flexFlow?: string
  
  /** 内容放置 */
  placeContent?: string
  
  /** 内容对齐 */
  alignContent?: string
  
  /** 主轴对齐 */
  justifyContent?: string
  
  /** 交叉轴对齐 */
  alignItems?: string
  
  /** 自身对齐 */
  justifySelf?: string
  
  /** 弹性对齐 */
  flexAlign?: string
  
  /** 网格 */
  grid?: boolean | string
  
  /** 网格区域 */
  gridArea?: string
  
  /** 网格模板 */
  gridTemplate?: string
  
  /** 顺序 */
  order?: number
  
  /** 溢出处理 */
  overflow?: string
  
  /** 垂直对齐方式 */
  valign?: string
  
  /** 文本渐变 */
  textGradient?: string | string[]
  
  /** 遮罩 */
  mask?: string
  
  /** 文本左对齐 */
  textLeft?: boolean
  
  /** 文本居中对齐 */
  textCenter?: boolean
  
  /** 文本右对齐 */
  textRight?: boolean
  
  /** 文本两端对齐 */
  textJustify?: boolean
  
  /** 字体大小 */
  size?: string | number
}

// 定义 FbFlex 组件的 Data 属性类型
export interface FbFlexData {
  /** 组件前缀 */
  prefix: string
}

// 定义 FbFlex 组件的 Computed 属性类型
export interface FbFlexComputed {
  /** 类名 */
  getClass: string[]
  
  /** 样式 */
  getStyle: object
}

// 定义 FbFlex 组件的 Slots 类型
export interface FbFlexSlots {
  /** 默认插槽 */
  default: VNode[]
}

// 定义 FbFlex 组件实例类型
export interface FbFlex extends Vue, FbFlexProps, FbFlexData, FbFlexComputed {
  $slots: FbFlexSlots
  
  /** 点击事件处理 */
  handleClick(e: Event): void
  
  /** 单位转换 */
  unit(val: string | number): string
}

// 定义 FbFlex 组件构造函数类型
export interface FbFlexConstructor extends VueConstructor<FbFlex> {}

// 导出 FbFlex 组件类型
export const FbFlex: FbFlexConstructor

// 默认导出
export default FbFlex