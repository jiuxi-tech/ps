import { Component, Vue } from 'vue-property-decorator'

export declare class FbPropertyItemProps {
  size?: string
  type?: string
  label?: string
  value?: string
  span?: string | number
  colon?: boolean
  labelWidth?: string | number
  labelAlign?: string
  contentAlign?: string
  ellipsis?: boolean
  verticalAlign?: string
  labelStyle?: object
  contentStyle?: object
  rowIndex?: string | number
  colSpan?: string | number
  rowSpan?: string | number
}

export declare class FbPropertyItemData {
  prefix: string
  myColon: boolean
  myLabelAlign: string
  myContentAlign: string
  myLabelWidth: string | number
  myVerticalAlign: string
  myEllipsis: boolean
  myLabelStyle: object
  myContentStyle: object
  fbProperty: any
  myMode: string
  fbRow: any
  debouncedUpdateRowHeight: Function
}

export declare class FbPropertyItemComputed {
  getClass: string[]
  getLabelStyle: object
  getContentStyle: object
}

export declare class FbPropertyItemMethods {
  // 更新 table 里得行高度 对象
  updateRowHeight(): void
}

declare type FbPropertyItemSlots = {
  label: void
  'label-extra': void
  default: void
}

declare class FbPropertyItem extends Vue {
  // Props
  size?: string
  type?: string
  label?: string
  value?: string
  span?: string | number
  colon?: boolean
  labelWidth?: string | number
  labelAlign?: string
  contentAlign?: string
  ellipsis?: boolean
  verticalAlign?: string
  labelStyle?: object
  contentStyle?: object
  rowIndex?: string | number
  colSpan?: string | number
  rowSpan?: string | number

  // Data
  prefix: string
  myColon: boolean
  myLabelAlign: string
  myContentAlign: string
  myLabelWidth: string | number
  myVerticalAlign: string
  myEllipsis: boolean
  myLabelStyle: object
  myContentStyle: object
  fbProperty: any
  myMode: string
  fbRow: any
  debouncedUpdateRowHeight: Function

  // Computed
  getClass: string[]
  getLabelStyle: object
  getContentStyle: object

  // Methods
  // 更新 table 里得行高度 对象
  updateRowHeight(): void

  // Lifecycle
  created(): void
  beforeDestroy(): void
  mounted(): void
  beforeUnmounted(): void

  // Slots
  $slots: FbPropertyItemSlots
}

export { FbPropertyItem }
export default FbPropertyItem