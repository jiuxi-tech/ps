import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbLayout 组件的 Props 类型
export interface FbLayoutProps {
  /** 布局样式: default 默认样式, blank 空白，骨架 */
  theme?: string
}

// 定义 FbLayout 组件的 Data 属性类型
export interface FbLayoutData {
  /** 组件前缀 */
  prefix: string
  
  /** 侧边栏组件 */
  sider: any
}

// 定义 FbLayout 组件的 Computed 属性类型
export interface FbLayoutComputed {
  /** 类名 */
  getClass: string[]
}

// 定义 FbLayout 组件的 Slots 类型
export interface FbLayoutSlots {
  /** 默认插槽 */
  default: VNode[]
}

// 定义 FbLayout 组件实例类型
export interface FbLayout extends Vue, FbLayoutProps, FbLayoutData, FbLayoutComputed {
  $slots: FbLayoutSlots
  
  /** 设置侧边栏 */
  setSider(sider: any): void
}

// 定义 FbLayout 组件构造函数类型
export interface FbLayoutConstructor extends VueConstructor<FbLayout> {}

// 导出 FbLayout 组件类型
export const FbLayout: FbLayoutConstructor

// 默认导出
export default FbLayout