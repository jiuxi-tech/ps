import { Component, Vue } from 'vue-property-decorator'
import { FbNumber } from '../../number/src/FbNumber'

export declare class FbProgressProps {
  type?: string
  format?: Function
  percent?: number | string
  decimals?: number | string
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
  innerStyle?: object
}

export declare class FbProgressData {
  prefix: string
  generatorId: number
}

export declare class FbProgressComputed {
  formatPercent: string
  strokeStyle: any[]
  getStrokeInnerStyle: any[]
  progressTextStyle: any[]
  relativeStrokeWidth: string
  radius: number
  trackPath: string
  perimeter: number
  rate: number
  strokeDashoffset: string
  trailPathStyle: object
  circlePathStyle: object
  stroke: string
}

export declare class FbProgressMethods {
  onClick(e: Event): void
  getCurrentColor(percent: number): string
  getLevelColor(percent: number): string
  getColorArray(): Array<{ color: string; percent: number }>
}

declare type FbProgressSlots = {
  text: void
  innerRight: void
}

declare class FbProgress extends Vue {
  // Props
  type?: string
  format?: Function
  percent?: number | string
  decimals?: number | string
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
  innerStyle?: object

  // Data
  prefix: string
  generatorId: number

  // Computed
  formatPercent: string
  strokeStyle: any[]
  getStrokeInnerStyle: any[]
  progressTextStyle: any[]
  relativeStrokeWidth: string
  radius: number
  trackPath: string
  perimeter: number
  rate: number
  strokeDashoffset: string
  trailPathStyle: object
  circlePathStyle: object
  stroke: string

  // Methods
  onClick(e: Event): void
  getCurrentColor(percent: number): string
  getLevelColor(percent: number): string
  getColorArray(): Array<{ color: string; percent: number }>

  // Events
  $emit(event: 'on-click', e: Event): void

  // Lifecycle
  mounted(): void
  destroyed(): void

  // Slots
  $slots: FbProgressSlots
}

export { FbProgress }
export default FbProgress