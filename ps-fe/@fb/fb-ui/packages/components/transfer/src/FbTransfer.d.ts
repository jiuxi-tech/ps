import { VNode } from 'vue'
import { FbButton } from '../../button/src/FbButton'
import { FbCheckbox } from '../../checkbox/src/FbCheckbox'
import { FbEmpty } from '../../empty/src/FbEmpty'
import { FbCheckboxGroup } from '../../checkbox-group/src/FbCheckboxGroup'

export interface FbTransferProps {
  /**
   * 绑定值
   */
  value?: any[]
  
  /**
   * 数据读取配置
   */
  reader?: {
    value: string
    label: string
  }
  
  /**
   * 数据源
   */
  data?: any[]
  
  /**
   * 标题
   */
  titles?: [string | null, string | null]
  
  /**
   * 按钮文本
   */
  buttons?: [string | null, string | null]
  
  /**
   * 列表样式
   */
  listStyle?: object
  
  /**
   * 目标键值
   */
  targetKeys?: any[]
  
  /**
   * 选中键值
   */
  selectedKeys?: any[]
  
  /**
   * 是否显示快速操作按钮
   */
  showFastButtons?: boolean
  
  /**
   * 源列表空状态文本
   */
  sourceEmptyText?: string
  
  /**
   * 源列表空状态类型
   */
  sourceEmptyType?: string
  
  /**
   * 目标列表空状态文本
   */
  targetEmptyText?: string
  
  /**
   * 目标列表空状态类型
   */
  targetEmptyType?: string
}

export interface FbTransferData {
  /**
   * 左侧列表数据
   */
  mySourceList: any[]
  
  /**
   * 左侧选中项
   */
  sourceChecked: any[]
  
  /**
   * 左侧全选状态
   */
  sourceAllChecked: boolean
  
  /**
   * 右侧列表数据
   */
  myTargetList: any[]
  
  /**
   * 右侧选中项
   */
  targetChecked: any[]
  
  /**
   * 右侧全选状态
   */
  targetAllChecked: boolean
  
  /**
   * 向左按钮禁用状态
   */
  toLeftButtonDisabled: boolean
  
  /**
   * 向左按钮类型
   */
  toLeftButtonType: string
  
  /**
   * 向左按钮文本
   */
  toLeftButtonText: string
  
  /**
   * 向右按钮禁用状态
   */
  toRightButtonDisabled: boolean
  
  /**
   * 向右按钮类型
   */
  toRightButtonType: string
  
  /**
   * 向右按钮文本
   */
  toRightButtonText: string
}

export interface FbTransferComputed {
  /**
   * 组件类名
   */
  getClass: string[]
  
  /**
   * 源列表选中文本
   */
  sourceHasCheckedText: string
  
  /**
   * 目标列表选中文本
   */
  targetHasCheckedText: string
  
  /**
   * 目标键值数组
   */
  myTargetKeys: any[]
  
  /**
   * 源键值数组
   */
  mySourceKeys: any[]
}

export interface FbTransferMethods {
  /**
   * 获取源列表渲染
   */
  getSourceRender(h: any): VNode
  
  /**
   * 获取目标列表渲染
   */
  getTargetRender(h: any): VNode
  
  /**
   * 获取操作区域渲染
   */
  getOperationRender(h: any): VNode
  
  /**
   * 处理源列表状态变化
   */
  handleSourceStatusChange(): void
  
  /**
   * 处理源列表全选
   */
  handelSourceAllCheck(checked: boolean, e: Event): void
  
  /**
   * 向右转移
   */
  handleTransToRight(): void
  
  /**
   * 全部向右转移
   */
  handleTransAllToRight(): void
  
  /**
   * 处理目标列表状态变化
   */
  handleTargetStatusChange(): void
  
  /**
   * 处理目标列表全选
   */
  handelTargetAllCheck(checked: boolean, e: Event): void
  
  /**
   * 向左转移
   */
  handleTransToLeft(): void
  
  /**
   * 全部向左转移
   */
  handleTransAllToLeft(): void
  
  /**
   * 处理状态变化
   */
  handleStatusChange(): void
}

export interface FbTransferSlots {
  /**
   * 源列表项插槽
   */
  source: VNode[]
  
  /**
   * 源列表复选框插槽
   */
  'source-checkbox': VNode[]
  
  /**
   * 目标列表项插槽
   */
  target: VNode[]
  
  /**
   * 目标列表复选框插槽
   */
  'target-checkbox': VNode[]
}

export declare class FbTransfer extends Vue implements FbTransferProps, FbTransferData, FbTransferComputed, FbTransferMethods {
  static readonly componentName: 'FbTransfer'
  
  // Props
  value: any[]
  reader: {
    value: string
    label: string
  }
  data: any[]
  titles: [string | null, string | null]
  buttons: [string | null, string | null]
  listStyle: object
  targetKeys: any[]
  selectedKeys: any[]
  showFastButtons: boolean
  sourceEmptyText: string
  sourceEmptyType: string
  targetEmptyText: string
  targetEmptyType: string
  
  // Data
  mySourceList: any[]
  sourceChecked: any[]
  sourceAllChecked: boolean
  myTargetList: any[]
  targetChecked: any[]
  targetAllChecked: boolean
  toLeftButtonDisabled: boolean
  toLeftButtonType: string
  toLeftButtonText: string
  toRightButtonDisabled: boolean
  toRightButtonType: string
  toRightButtonText: string
  
  // Computed
  getClass: string[]
  sourceHasCheckedText: string
  targetHasCheckedText: string
  myTargetKeys: any[]
  mySourceKeys: any[]
  
  // Methods
  getSourceRender(h: any): VNode
  getTargetRender(h: any): VNode
  getOperationRender(h: any): VNode
  handleSourceStatusChange(): void
  handelSourceAllCheck(checked: boolean, e: Event): void
  handleTransToRight(): void
  handleTransAllToRight(): void
  handleTargetStatusChange(): void
  handelTargetAllCheck(checked: boolean, e: Event): void
  handleTransToLeft(): void
  handleTransAllToLeft(): void
  handleStatusChange(): void
}

export default FbTransfer