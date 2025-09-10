import { Component, Vue } from 'vue-property-decorator'
import { FbProgress } from '../../progress/src/FbProgress'
import { FbTableLayout } from '../../table-layout/src/FbTableLayout'
import { FbTableLayoutCell } from '../../table-layout/src/FbTableLayoutCell'

export declare class FbProgressBoxProps {
  // 上半区
  tPrefix?: string | number
  tNum?: string | number
  tSuffix?: string | number
  // 下半区
  bPrefix?: string | number
  bNum?: string | number
  numSize?: string | number
  bSuffix?: string | number
  type?: string
  format?: Function
  percent?: number | string
  showInfo?: boolean
  status?: string
  strokeWidth?: string | number
  strokeLinecap?: string
  width?: number
  strokeColor?: string | object | any[] | Function
  strokeBgColor?: string
  textInside?: boolean
  textStyle?: object
  borderRadius?: string | number
}

export declare class FbProgressBoxData {
  prefix: string
}

export declare class FbProgressBoxComputed {
  numSizeStyle: object
}

export declare class FbProgressBoxMethods {
  onClick(e: Event): void
  onTnumClick(e: Event): void
  onProgressClick(e: Event): void
  onBnumClick(e: Event): void
}

declare type FbProgressBoxSlots = {
  top: void
  progress: void
  bottom: void
}

declare class FbProgressBox extends Vue {
  // Props
  // 上半区
  tPrefix?: string | number
  tNum?: string | number
  tSuffix?: string | number
  // 下半区
  bPrefix?: string | number
  bNum?: string | number
  numSize?: string | number
  bSuffix?: string | number
  type?: string
  format?: Function
  percent?: number | string
  showInfo?: boolean
  status?: string
  strokeWidth?: string | number
  strokeLinecap?: string
  width?: number
  strokeColor?: string | object | any[] | Function
  strokeBgColor?: string
  textInside?: boolean
  textStyle?: object
  borderRadius?: string | number

  // Data
  prefix: string

  // Computed
  numSizeStyle: object

  // Methods
  onClick(e: Event): void
  onTnumClick(e: Event): void
  onProgressClick(e: Event): void
  onBnumClick(e: Event): void

  // Events
  $emit(event: 'on-click', e: Event): void
  $emit(event: 'on-tnum-click', e: Event): void
  $emit(event: 'on-progress-click', e: Event): void
  $emit(event: 'on-bnum-click', e: Event): void

  // Lifecycle
  mounted(): void
  destroyed(): void

  // Slots
  $slots: FbProgressBoxSlots
}

export { FbProgressBox }
export default FbProgressBox