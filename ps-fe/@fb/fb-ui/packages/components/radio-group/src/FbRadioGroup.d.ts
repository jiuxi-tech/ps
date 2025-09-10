import { Component, Vue } from 'vue-property-decorator'
import { FbRadio } from '../../radio/src/FbRadio'
import { FbLoading } from '../../loading/src/FbLoading'
import { FbEmpty } from '../../empty/src/FbEmpty'

export declare class FbRadioGroupProps {
  value?: string | number | boolean
  // 如果传入一个数组
  data?: any[]
  // 是否垂直
  vertical?: boolean
  size?: string
  // 按钮样式
  button?: boolean
  // 100% 宽度
  long?: boolean
  // 列表样式
  list?: boolean
  reader?: {
    value?: string
    label?: string
    disabled?: string
    readonly?: string
    size?: string
  }
  disabled?: boolean
  readonly?: boolean
  // radio 间距
  radioSpace?: number | string
  // 服务获取节点数据
  service?: object | any[] | Function
  // 查询参数
  param?: object | any[] | Function
  // 网络数据过滤器
  dataFilter?: Function
  // 自动加载数据
  autoLoad?: boolean
}

export declare class FbRadioGroupData {
  prefix: string
  currentValue: string | number | boolean
  name: string
  selectedRadio: any
  myData: any[]
  showLoading: boolean
  showEmpty: boolean
  myQueryParam: object | any[] | Function
  radios: any[]
  formItem: any
}

export declare class FbRadioGroupComputed {
  radioSpaceNum: number
  getClass: string[]
}

export declare class FbRadioGroupMethods {
  addItem(radio: any): void
  removeItem(radio: any): void
  update(value: string | number | boolean): void
  // 从子组件修改
  change(value: string | number | boolean, node: any, e: Event): void
  changeValue(value: string | number | boolean, node?: any, e?: Event): void
  fetchData(): void
  updateDataAfterQuery(json: any): void
  updateData(data: any[]): void
  loading(status: boolean): void
}

declare type FbRadioGroupSlots = {
  default: void
}

declare class FbRadioGroup extends Vue {
  // Props
  value?: string | number | boolean
  // 如果传入一个数组
  data?: any[]
  // 是否垂直
  vertical?: boolean
  size?: string
  // 按钮样式
  button?: boolean
  // 100% 宽度
  long?: boolean
  // 列表样式
  list?: boolean
  reader?: {
    value?: string
    label?: string
    disabled?: string
    readonly?: string
    size?: string
  }
  disabled?: boolean
  readonly?: boolean
  // radio 间距
  radioSpace?: number | string
  // 服务获取节点数据
  service?: object | any[] | Function
  // 查询参数
  param?: object | any[] | Function
  // 网络数据过滤器
  dataFilter?: Function
  // 自动加载数据
  autoLoad?: boolean

  // Data
  prefix: string
  currentValue: string | number | boolean
  name: string
  selectedRadio: any
  myData: any[]
  showLoading: boolean
  showEmpty: boolean
  myQueryParam: object | any[] | Function
  radios: any[]
  formItem: any

  // Computed
  radioSpaceNum: number
  getClass: string[]

  // Methods
  addItem(radio: any): void
  removeItem(radio: any): void
  update(value: string | number | boolean): void
  // 从子组件修改
  change(value: string | number | boolean, node: any, e: Event): void
  changeValue(value: string | number | boolean, node?: any, e?: Event): void
  fetchData(): void
  updateDataAfterQuery(json: any): void
  updateData(data: any[]): void
  loading(status: boolean): void

  // Events
  $emit(event: 'input', value: string | number | boolean, radio?: any): void
  $emit(event: 'change', value: string | number | boolean, radio?: any): void
  $emit(event: 'on-change', value: string | number | boolean, radio?: any): void
  $emit(event: 'on-data-load', data: any): void
  $emit(event: 'on-data-update'): void

  // Lifecycle
  created(): void
  destroyed(): void
  mounted(): void

  // Slots
  $slots: FbRadioGroupSlots
}

export { FbRadioGroup }
export default FbRadioGroup