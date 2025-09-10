import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbLayoutSider 组件的 Props 类型
export interface FbLayoutSiderProps {
  /** 侧边栏宽度 */
  width?: string | number
}

// 定义 FbLayoutSider 组件的 Data 属性类型
export interface FbLayoutSiderData {
  /** 组件前缀 */
  prefix: string
  
  /** 布局组件 */
  layout: any
}

// 定义 FbLayoutSider 组件的 Computed 属性类型
export interface FbLayoutSiderComputed {
  /** 样式 */
  getStyle: object
}

// 定义 FbLayoutSider 组件的 Slots 类型
export interface FbLayoutSiderSlots {
  /** 默认插槽 */
  default: VNode[]
}

// 定义 FbLayoutSider 组件实例类型
export interface FbLayoutSider extends Vue, FbLayoutSiderProps, FbLayoutSiderData, FbLayoutSiderComputed {
  $slots: FbLayoutSiderSlots
}

// 定义 FbLayoutSider 组件构造函数类型
export interface FbLayoutSiderConstructor extends VueConstructor<FbLayoutSider> {}

// 导出 FbLayoutSider 组件类型
export const FbLayoutSider: FbLayoutSiderConstructor

// 默认导出
export default FbLayoutSider