import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbForm 组件的 Props 类型
export interface FbFormProps {
  /** 表单数据对象 */
  model?: object
  
  /** 标签显示位置 */
  labelPosition?: string
  
  /** 标签宽度 */
  labelWidth?: number | string
  
  /** 校验规则 */
  rule?: object | Array<any>
  
  /** 模式 */
  mode?: string
  
  /** 是否显示校验信息 */
  showMessage?: boolean
  
  /** 自动完成 */
  autocomplete?: string
  
  /** 表单标题 */
  caption?: string | Array<any> | object
  
  /** 标签样式 */
  labelStyle?: object
  
  /** 内容样式 */
  contentStyle?: object
}

// 定义 FbForm 组件的 Data 属性类型
export interface FbFormData {
  /** 组件前缀 */
  prefix: string
  
  /** 需要校验的表单项 */
  validateFormItems: Array<any>
}

// 定义 FbForm 组件的 Computed 属性类型
export interface FbFormComputed {
  /** 类名 */
  getClass: string[]
  
  /** 当前是否显示消息 */
  currentShowMessage: boolean
}

// 定义 FbForm 组件的 Slots 类型
export interface FbFormSlots {
  /** 默认插槽 */
  default: VNode[]
}

// 定义 FbForm 组件实例类型
export interface FbForm extends Vue, FbFormProps, FbFormData, FbFormComputed {
  $slots: FbFormSlots
  
  /** 添加表单项 */
  addItem(item: any): void
  
  /** 移除表单项 */
  removeItem(item: any): void
  
  /** 校验表单 */
  validate(callback?: Function): Promise<boolean> | void
  
  /** 校验表单中的某个字段 */
  validateField(filed: string, callback?: Function): Promise<boolean> | void
  
  /** 重置表单 */
  resetFields(filed?: string): void
  
  /** 处理提交事件 */
  handleSubmit(): void
}

// 定义 FbForm 组件构造函数类型
export interface FbFormConstructor extends VueConstructor<FbForm> {}

// 导出 FbForm 组件类型
export const FbForm: FbFormConstructor

// 默认导出
export default FbForm