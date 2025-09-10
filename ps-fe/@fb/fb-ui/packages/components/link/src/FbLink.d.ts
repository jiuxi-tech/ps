import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbLink 组件的 Props 类型
export interface FbLinkProps {
  /** 尺寸 */
  size?: 'xs' | 's' | 'm' | 'l' | 'xl' | 'xxl'
  
  /** 点击事件 */
  click?: Function
  
  /** 分隔符 */
  separator?: string
  
  /** 类型 */
  type?: string
  
  /** 标签 */
  tag?: string
  
  /** 链接地址 */
  href?: string
  
  /** 动作 */
  action?: string
  
  /** 路由地址 */
  to?: string | object
  
  /** 是否替换 */
  replace?: boolean
  
  /** 是否追加 */
  append?: boolean
  
  /** 打开方式 */
  target?: string
  
  /** 图标 */
  icon?: string
  
  /** 标签文本 */
  label?: string | number
  
  /** 标题 */
  title?: string | number
  
  /** 颜色 */
  color?: string
  
  /** 下划线 */
  underline?: 'on' | 'off' | 'hover'
  
  /** 是否禁用 */
  disabled?: boolean
}

// 定义 FbLink 组件的 Data 属性类型
export interface FbLinkData {
  /** 组件前缀 */
  prefix: string
  
  /** 分隔符 */
  mySeparator: string
  
  /** 下划线 */
  myUnderline: string
  
  /** 链接组 */
  linkGroup: any
}

// 定义 FbLink 组件的 Computed 属性类型
export interface FbLinkComputed {
  /** 链接类名 */
  getLinkClass: string[]
}

// 定义 FbLink 组件的 Slots 类型
export interface FbLinkSlots {
  /** 默认插槽 */
  default: VNode[]
  
  /** 分隔符插槽 */
  separator: VNode[]
}

// 定义 FbLink 组件实例类型
export interface FbLink extends Vue, FbLinkProps, FbLinkData, FbLinkComputed {
  $slots: FbLinkSlots
  
  /** 判断是否未定义 */
  isUndefined(value: any): boolean
  
  /** 处理点击事件 */
  handleClick(): void
}

// 定义 FbLink 组件构造函数类型
export interface FbLinkConstructor extends VueConstructor<FbLink> {}

// 导出 FbLink 组件类型
export const FbLink: FbLinkConstructor

// 默认导出
export default FbLink