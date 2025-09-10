import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbCol 组件的 Props 类型
export interface FbColProps {
  /** 栅格占据的列数 */
  span?: string | number
  
  /** 栅格左侧的间隔格数 */
  offset?: string | number
  
  /** 栅格的排序 */
  order?: string | number
  
  /** flex 布局属性 */
  flex?: string | number
  
  /** 小屏幕下的栅格占据的列数 */
  s?: string | number
  
  /** 中等屏幕下的栅格占据的列数 */
  m?: string | number
  
  /** 大屏幕下的栅格占据的列数 */
  l?: string | number
  
  /** 超大屏幕下的栅格占据的列数 */
  xl?: string | number
}

// 定义 FbCol 组件的 Data 属性类型
export interface FbColData {
  /** 组件前缀 */
  prefix: string
  
  /** 水平间距 */
  myGutter: number
  
  /** 垂直间距 */
  myVerticalGutter: number
  
  /** 行组件 */
  fbRow: any
}

// 定义 FbCol 组件的 Computed 属性类型
export interface FbColComputed {
  /** 样式 */
  getStyle: object
  
  /** 类名 */
  getClass: string[]
}

// 定义 FbCol 组件的 Slots 类型
export interface FbColSlots {
  /** 默认插槽 */
  default: VNode[]
}

// 定义 FbCol 组件实例类型
export interface FbCol extends Vue, FbColProps, FbColData, FbColComputed {
  $slots: FbColSlots
}

// 定义 FbCol 组件构造函数类型
export interface FbColConstructor extends VueConstructor<FbCol> {}

// 导出 FbCol 组件类型
export const FbCol: FbColConstructor

// 默认导出
export default FbCol