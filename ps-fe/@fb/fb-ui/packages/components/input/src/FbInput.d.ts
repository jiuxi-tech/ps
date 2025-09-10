import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbInput 组件的 Props 类型
export interface FbInputProps {
  /** 输入框的值 */
  value?: string | number
  
  /** 类型 */
  type?: string
  
  /** 尺寸 */
  size?: string
  
  /** 占位提示 */
  placeholder?: string
  
  /** 是否禁用 */
  disabled?: boolean
  
  /** 是否只读 */
  readonly?: boolean
  
  /** 是否可清除 */
  clearable?: boolean
  
  /** 最大长度 */
  maxlength?: number | string
  
  /** 前置图标 */
  prependIcon?: string
  
  /** 后置图标 */
  icon?: string
  
  /** 宽度 */
  width?: string | number
  
  /** 名称 */
  name?: string
  
  /** 自动完成 */
  autocomplete?: string
  
  /** 是否圆角 */
  round?: boolean
  
  /** 输入框样式 */
  elStyle?: string | Array<any> | object
  
  /** 前置文本 */
  prepend?: string
  
  /** 后置文本 */
  append?: string
  
  /** 是否显示后置 */
  appendShow?: boolean
}

// 定义 FbInput 组件的 Data 属性类型
export interface FbInputData {
  /** 组件前缀 */
  prefix: string
  
  /** 当前值 */
  currentValue: string | number
  
  /** 是否为密码框 */
  password: boolean
  
  /** 是否显示密码 */
  showPassword: boolean
  
  /** 当前类型 */
  currentType: string
  
  /** 是否悬停 */
  hovering: boolean
  
  /** 是否有前置按钮 */
  prependButton: boolean
  
  /** 是否有后置按钮 */
  appendButton: boolean
  
  /** 表单项 */
  fbFormItem: any
  
  /** 插槽是否准备就绪 */
  slotReady: boolean
}

// 定义 FbInput 组件的 Computed 属性类型
export interface FbInputComputed {
  /** 宽度 */
  myWidth: string
  
  /** 是否有前置 */
  hasPrepend: boolean
  
  /** 是否有后置 */
  hasAppend: boolean
}

// 定义 FbInput 组件的 Slots 类型
export interface FbInputSlots {
  /** 前置插槽 */
  prepend: VNode[]
  
  /** 前置按钮插槽 */
  'prepend-button': VNode[]
  
  /** 后置插槽 */
  append: VNode[]
  
  /** 后置按钮插槽 */
  'append-button': VNode[]
  
  /** 后缀插槽 */
  suffix: VNode[]
  
  /** 默认插槽 */
  default: VNode[]
}

// 定义 FbInput 组件实例类型
export interface FbInput extends Vue, FbInputProps, FbInputData, FbInputComputed {
  $slots: FbInputSlots
  
  /** 处理删除事件 */
  handleDelete(event: Event): void
  
  /** 处理前置图标点击 */
  handlePrependIconClick(event: Event): void
  
  /** 处理图标点击 */
  handleIconClick(event: Event): void
  
  /** 处理回车事件 */
  handleEnter(event: Event): void
  
  /** 处理聚焦事件 */
  handleFocus(event: Event): void
  
  /** 处理失焦事件 */
  handleBlur(event: Event): void
  
  /** 处理输入事件 */
  handleInput(event: Event): void
  
  /** 处理变更事件 */
  handleChange(event: Event): void
  
  /** 切换输入类型 */
  changeInputType(): void
  
  /** 设置当前值 */
  setCurrentValue(value: string | number): void
  
  /** 处理点击事件 */
  handleClick(e: Event): void
  
  /** 获取长度 */
  getLength(): number
  
  /** 处理清除 */
  handleClear(): void
}

// 定义 FbInput 组件构造函数类型
export interface FbInputConstructor extends VueConstructor<FbInput> {}

// 导出 FbInput 组件类型
export const FbInput: FbInputConstructor

// 默认导出
export default FbInput