import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbLoadingBar 组件的 Props 类型
export interface FbLoadingBarProps {
  /** 类型 */
  type?: 'primary' | 'info' | 'success' | 'warn' | 'danger'
  
  /** 高度 */
  height?: number
  
  /** 进度 */
  process?: number
  
  /** 颜色 */
  color?: string
}

// 定义 FbLoadingBar 组件的 Data 属性类型
export interface FbLoadingBarData {
  /** 组件前缀 */
  prefix: string
}

// 定义 FbLoadingBar 组件的 Computed 属性类型
export interface FbLoadingBarComputed {
  /** 类名 */
  getClass: string[]
  
  /** 进度条样式 */
  getProcessStyle: object
}

// 定义 FbLoadingBar 组件的 Slots 类型
// 暂无插槽

// 定义 FbLoadingBar 组件实例类型
export interface FbLoadingBar extends Vue, FbLoadingBarProps, FbLoadingBarData, FbLoadingBarComputed {
  $slots: object
}

// 定义 FbLoadingBar 组件构造函数类型
export interface FbLoadingBarConstructor extends VueConstructor<FbLoadingBar> {}

// 导出 FbLoadingBar 组件类型
export const FbLoadingBar: FbLoadingBarConstructor

// 默认导出
export default FbLoadingBar