import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbFormItem 组件的 Props 类型
export interface FbFormItemProps {
  /** 字段名 */
  prop?: string
  
  /** 标签 */
  label?: string
  
  /** 是否显示标签 */
  showLabel?: boolean
  
  /** 标签宽度 */
  labelWidth?: number | string
  
  /** 标签位置 */
  labelPosition?: string
  
  /** 标签是否固定 */
  labelFixed?: boolean
  
  /** 是否显示校验信息 */
  showMessage?: boolean
  
  /** 校验规则 */
  rule?: Array<any> | object
  
  /** 值 */
  value?: string | number | Array<any> | boolean | object
  
  /** 标签样式 */
  labelStyle?: object | Array<any> | string
  
  /** 标签文本样式 */
  labelTextStyle?: object | Array<any> | string
  
  /** 标签额外样式 */
  labelExtraStyle?: object | Array<any> | string
  
  /** 内容样式 */
  contentStyle?: object | Array<any> | string
  
  /** 是否省略 */
  ellipsis?: boolean
  
  /** 是否取消表单项下方的留白 */
  noPaddingBottom?: boolean
}

// 定义 FbFormItem 组件的 Data 属性类型
export interface FbFormItemData {
  /** 组件前缀 */
  prefix: string
  
  /** 校验状态 */
  validateState: string
  
  /** 校验消息 */
  validateMessage: string
  
  /** 校验消息数组 */
  validateMessageArray: Array<string>
  
  /** 校验禁用 */
  validateDisabled: boolean
  
  /** 标签是否固定 */
  myLabelFixed: boolean
  
  /** 初始值 */
  initialValue: any
}

// 定义 FbFormItem 组件的 Computed 属性类型
export interface FbFormItemComputed {
  /** 当前规则 */
  currentRule: Array<any>
  
  /** 是否必填 */
  required: boolean
  
  /** 类名 */
  getClass: string[]
  
  /** 标签样式 */
  getLabelStyle: object
  
  /** 内容样式 */
  getContentStyle: object
}

// 定义 FbFormItem 组件的 Slots 类型
export interface FbFormItemSlots {
  /** 默认插槽 */
  default: VNode[]
  
  /** 标签插槽 */
  label: VNode[]
  
  /** 标签额外插槽 */
  'label-extra': VNode[]
}

// 定义 FbFormItem 组件实例类型
export interface FbFormItem extends Vue, FbFormItemProps, FbFormItemData, FbFormItemComputed {
  $slots: FbFormItemSlots
  
  /** 获取字段值 */
  getFieldValue(): any
  
  /** 校验 */
  validate(trigger: string, callback?: Function, currentValue?: any): boolean | void
  
  /** 重置字段 */
  resetField(): void
  
  /** 添加校验事件 */
  addValidateEvents(): void
  
  /** 字段失焦处理 */
  onFieldBlur(currentValue: any): void
  
  /** 字段变更处理 */
  onFieldChange(currentValue: any): void
}

// 定义 FbFormItem 组件构造函数类型
export interface FbFormItemConstructor extends VueConstructor<FbFormItem> {}

// 导出 FbFormItem 组件类型
export const FbFormItem: FbFormItemConstructor

// 默认导出
export default FbFormItem