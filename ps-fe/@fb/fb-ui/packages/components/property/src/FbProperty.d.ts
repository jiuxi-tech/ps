import { Component, Vue } from 'vue-property-decorator'
import { FbPropertyItem } from './FbPropertyItem'

export declare class FbPropertyProps {
  size?: string
  title?: string
  split?: number | string
  bordered?: boolean
  // 是否上下显示
  vertical?: boolean
  // 冒号
  colon?: boolean
  value?: any[] | object
  sort?: string
  labelWidth?: number | string
  labelAlign?: string
  contentAlign?: string
  ellipsis?: boolean
  // form
  mode?: string
  // top middle bottom
  verticalAlign?: string
  bodyStyle?: object
  labelStyle?: object
  contentStyle?: object
}

export declare class FbPropertyData {
  prefix: string
  pk: string
  myColon: boolean
  myValue: any[] | null
  rowsHeight: any[]
  timer: any
  debounceSyncItemHeight: Function
}

export declare class FbPropertyComputed {
  getClass: string[]
}

export declare class FbPropertyMethods {
  toCols(value: any): any[] | null
  syncItemHeight(): void
  handleWindowResize(): void
  sync(): void
}

declare type FbPropertySlots = {
  header: void
  title: void
  actions: void
  footer: void
  default: void
}

declare class FbProperty extends Vue {
  // Props
  size?: string
  title?: string
  split?: number | string
  bordered?: boolean
  // 是否上下显示
  vertical?: boolean
  // 冒号
  colon?: boolean
  value?: any[] | object
  sort?: string
  labelWidth?: number | string
  labelAlign?: string
  contentAlign?: string
  ellipsis?: boolean
  // form
  mode?: string
  // top middle bottom
  verticalAlign?: string
  bodyStyle?: object
  labelStyle?: object
  contentStyle?: object

  // Data
  prefix: string
  pk: string
  myColon: boolean
  myValue: any[] | null
  rowsHeight: any[]
  timer: any
  debounceSyncItemHeight: Function

  // Computed
  getClass: string[]

  // Methods
  toCols(value: any): any[] | null
  syncItemHeight(): void
  handleWindowResize(): void
  sync(): void

  // Events
  $emit(event: 'input', value: any): void

  // Lifecycle
  updated(): void
  created(): void
  beforeDestroy(): void
  mounted(): void

  // Slots
  $slots: FbPropertySlots
}

export { FbProperty }
export default FbProperty