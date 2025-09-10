import { Component, Vue } from 'vue-property-decorator'
import { FbIcon } from '../../icon/src/FbIcon'
import { FbButton } from '../../button/src/FbButton'

export declare class FbNumberInputProps {
  value?: string | number
  // 类型，可选 text password
  type?: string
  // 尺寸
  size?: string
  // 占位提示
  placeholder?: string
  // 禁用
  disabled?: boolean
  // 只读
  readonly?: boolean
  // 可清除
  clearable?: boolean
  // 最大长度
  maxlength?: number | string
  // 前置图标
  prependIcon?: string
  // 后置图标
  icon?: string
  // 宽度
  width?: string | number
  name?: string
  // 自动完成
  autocomplete?: string
  // 圆角
  round?: boolean
  formatter?: Function
  max?: number
  min?: number
  parser?: Function
  // 数值精度
  precision?: number
  // 小数点
  decimalSeparator?: number
  // 步数
  step?: number
  // 快速模式，按住鼠标或按住键盘
  fastMode?: boolean
  // 按住ctrl点按键盘 步进*10
  ctrl?: boolean
}

export declare class FbNumberInputData {
  prefix: string
  currentValue: string | number
  password: boolean
  showPassword: boolean
  prepend: boolean
  append: boolean
  currentType: string
  hovering: boolean
  prependButton: boolean
  appendButton: boolean
  formItem: any
}

export declare class FbNumberInputComputed {
  myWidth: string
}

export declare class FbNumberInputMethods {
  getValidValue(): void
  handleDelete(event: Event): void
  handlePrependIconClick(event: Event): void
  handleIconClick(event: Event): void
  handleEnter(event: Event): void
  handleFocus(event: Event): void
  handleBlur(event: Event): void
  handleInput(event: Event): void
  changeInputType(): void
  setCurrentValue(value: string | number): void
  click(e: Event): void
  getLength(): number
  handleClear(): void
  handleUp(): void
  add(arg1: number, arg2: number): number
  subtraction(arg1: number, arg2: number): string
  multiplication(arg1: number, arg2: number): number
  division(arg1: number, arg2: number): number
}

declare type FbNumberInputSlots = {
  'append-button': void
  'append': void
}

declare class FbNumberInput extends Vue {
  // Props
  value: string | number
  type: string
  size: string
  placeholder: string
  disabled: boolean
  readonly: boolean
  clearable: boolean
  maxlength: number | string
  prependIcon: string
  icon: string
  width: string | number
  name: string
  autocomplete: string
  round: boolean
  formatter: Function
  max: number
  min: number
  parser: Function
  precision: number
  decimalSeparator: number
  step: number
  fastMode: boolean
  ctrl: boolean

  // Data
  prefix: string
  currentValue: string | number
  password: boolean
  showPassword: boolean
  prepend: boolean
  append: boolean
  currentType: string
  hovering: boolean
  prependButton: boolean
  appendButton: boolean
  formItem: any

  // Computed
  myWidth: string

  // Methods
  getValidValue(): void
  handleDelete(event: Event): void
  handlePrependIconClick(event: Event): void
  handleIconClick(event: Event): void
  handleEnter(event: Event): void
  handleFocus(event: Event): void
  handleBlur(event: Event): void
  handleInput(event: Event): void
  changeInputType(): void
  setCurrentValue(value: string | number): void
  click(e: Event): void
  getLength(): number
  handleClear(): void
  handleUp(): void
  add(arg1: number, arg2: number): number
  subtraction(arg1: number, arg2: number): string
  multiplication(arg1: number, arg2: number): number
  division(arg1: number, arg2: number): number

  // Events
  $emit(event: 'input', value: string | number): void
  $emit(event: 'on-enter', e: Event): void
  $emit(event: 'on-focus', e: Event): void
  $emit(event: 'on-blur', e: Event): void
  $emit(event: 'on-input', value: string | number, e: Event): void
  $emit(event: 'on-change', value: string | number, e: Event): void
  $emit(event: 'on-prepend-icon-click', e: Event): void
  $emit(event: 'on-icon-click', e: Event): void
  $emit(event: 'on-click', e: Event): void
}

export { FbNumberInput }
export default FbNumberInput