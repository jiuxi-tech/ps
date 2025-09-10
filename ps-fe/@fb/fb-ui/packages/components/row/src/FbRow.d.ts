import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbRow 组件的 Props 类型
export interface FbRowProps {
  /** 栅格间隔 */
  gutter?: number | string
  
  /** 垂直栅格间隔 */
  verticalGutter?: number | string
  
  /** 是否启用 flex 布局 */
  flex?: boolean
  
  /** flex 布局下的水平排列方式 */
  justify?: string
  
  /** flex 布局下的垂直排列方式 */
  align?: string
  
  /** 行主键 */
  rowPk?: string | number
}

// 定义 FbRow 组件的 Data 属性类型
export interface FbRowData {
  /** 组件前缀 */
  prefix: string
  
  /** 水平间距 */
  myGutter: number | string
  
  /** 垂直间距 */
  myVerticalGutter: number | string
}

// 定义 FbRow 组件的 Computed 属性类型
export interface FbRowComputed {
  /** 类名 */
  getClass: string[]
  
  /** 样式 */
  getStyle: object
}

// 定义 FbRow 组件的 Slots 类型
export interface FbRowSlots {
  /** 默认插槽 */
  default: VNode[]
}

// 定义 FbRow 组件实例类型
export interface FbRow extends Vue, FbRowProps, FbRowData, FbRowComputed {
  $slots: FbRowSlots
}

// 定义 FbRow 组件构造函数类型
export interface FbRowConstructor extends VueConstructor<FbRow> {}

// 导出 FbRow 组件类型
export const FbRow: FbRowConstructor

// 默认导出
export default FbRow