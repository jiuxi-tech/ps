import { Component, Vue } from 'vue-property-decorator'

export declare class FbPropertiesProps {
  size?: string
  title?: string
  split?: number | string
  bordered?: boolean
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

export declare class FbPropertiesData {
  prefix: string
  pk: string
  myColon: boolean
  myValue: any[] | null
  rowsHeight: any[]
}

export declare class FbPropertiesComputed {
  getClass: string[]
}

export declare class FbPropertiesMethods {
  getLabelClass(propsData: any): string[]
  getLabelStyle(propsData: any): object
  getContentClass(propsData: any): string[]
  getContentStyle(propsData: any): object
  hasColon(propsData: any): boolean
  toCols(value: any): any[] | null
  renderRows(h: any): any[]
}

declare type FbPropertiesSlots = {
  header: void
  title: void
  actions: void
  footer: void
  default: void
}

declare class FbProperties extends Vue {
  // Props
  size?: string
  title?: string
  split?: number | string
  bordered?: boolean
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

  // Computed
  getClass: string[]

  // Methods
  getLabelClass(propsData: any): string[]
  getLabelStyle(propsData: any): object
  getContentClass(propsData: any): string[]
  getContentStyle(propsData: any): object
  hasColon(propsData: any): boolean
  toCols(value: any): any[] | null
  renderRows(h: any): any[]

  // Events
  $emit(event: 'input', value: any): void

  // Lifecycle
  created(): void
  beforeDestroy(): void
  mounted(): void

  // Slots
  $slots: FbPropertiesSlots
}

export { FbProperties }
export default FbProperties