import { Component, Vue } from 'vue-property-decorator'

export declare class FbRateBoxProps {
  rateText?: string
  rateNumText?: string
  rateBg?: string
  title?: string
  subTitle?: string
  ellipsis?: boolean
}

export declare class FbRateBoxData {
  prefix: string
}

export declare class FbRateBoxComputed {
  rateLeftStyle: object
}

export declare class FbRateBoxMethods {
  onClick(e: Event): void
  onRateClick(e: Event): void
  onTitleClick(e: Event): void
  onSubtitleClick(e: Event): void
}

declare type FbRateBoxSlots = {
  left: void
  right: void
  title: void
  subTitle: void
}

declare class FbRateBox extends Vue {
  // Props
  rateText?: string
  rateNumText?: string
  rateBg?: string
  title?: string
  subTitle?: string
  ellipsis?: boolean

  // Data
  prefix: string

  // Computed
  rateLeftStyle: object

  // Methods
  onClick(e: Event): void
  onRateClick(e: Event): void
  onTitleClick(e: Event): void
  onSubtitleClick(e: Event): void

  // Events
  $emit(event: 'on-click', e: Event): void
  $emit(event: 'on-rate-click', e: Event): void
  $emit(event: 'on-title-click', e: Event): void
  $emit(event: 'on-subtitle-click', e: Event): void

  // Lifecycle
  mounted(): void
  destroyed(): void

  // Slots
  $slots: FbRateBoxSlots
}

export { FbRateBox }
export default FbRateBox