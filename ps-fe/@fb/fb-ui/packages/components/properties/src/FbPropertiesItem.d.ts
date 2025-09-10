import { Component, Vue } from 'vue-property-decorator'

export declare class FbPropertiesItemProps {
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

export declare class FbPropertiesItemData {
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
}

export declare class FbPropertiesItemComputed {
  getClass: string[]
  getLabelStyle: object
  getContentStyle: object
}

export declare class FbPropertiesItemMethods {
  // 无特定方法
}

declare type FbPropertiesItemSlots = {
  label: void
  'label-extra': void
  default: void
}

declare class FbPropertiesItem extends Vue {
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

  // Computed
  getClass: string[]
  getLabelStyle: object
  getContentStyle: object

  // Methods
  // 无特定方法

  // Lifecycle
  created(): void
  beforeDestroy(): void
  mounted(): void
  beforeUnmounted(): void

  // Slots
  $slots: FbPropertiesItemSlots
}

export { FbPropertiesItem }
export default FbPropertiesItem