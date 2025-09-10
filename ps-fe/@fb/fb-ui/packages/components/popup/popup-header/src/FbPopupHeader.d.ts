import { Component, Vue } from 'vue-property-decorator'
import { FbIcon } from '../../../icon/src/FbIcon'

export declare class FbPopupHeaderProps {
  value?: string | number | any[] | boolean
  placeholder?: string | number
  icon?: string
  disabled?: boolean
  clearable?: boolean
  readonly?: boolean
  multiple?: boolean
}

export declare class FbPopupHeaderData {
  prefix: string
  showClear: boolean
}

export declare class FbPopupHeaderComputed {
  showClearIcon: boolean
  tabindex: number
}

export declare class FbPopupHeaderMethods {
  // 无特定方法
}

declare type FbPopupHeaderSlots = {
  default: void
}

declare class FbPopupHeader extends Vue {
  // Props
  value?: string | number | any[] | boolean
  placeholder?: string | number
  icon?: string
  disabled?: boolean
  clearable?: boolean
  readonly?: boolean
  multiple?: boolean

  // Data
  prefix: string
  showClear: boolean

  // Computed
  showClearIcon: boolean
  tabindex: number

  // Events
  $emit(event: 'on-click'): void
  $emit(event: 'on-focus', e: Event): void
  $emit(event: 'on-blur', e: Event): void
  $emit(event: 'on-keydown', e: Event): void
  $emit(event: 'on-icon-click'): void
  $emit(event: 'on-clear'): void

  // Slots
  $slots: FbPopupHeaderSlots
}

export { FbPopupHeader }
export default FbPopupHeader