import { Component, Vue } from 'vue-property-decorator'
import { FbPopupPicker } from '../../popup-picker/src/FbPopupPicker'

export declare class FbPopupSelectProps {
  value?: boolean
  disabled?: boolean
  position?: string
  // 下拉的最大高度
  maxHeight?: string | number
  showConfirm?: boolean
  // 多选
  multiple?: boolean
  show?: boolean
}

export declare class FbPopupSelectData {
  prefix: string
  myShow: boolean
}

export declare class FbPopupSelectComputed {
  getTabindex: number
}

export declare class FbPopupSelectMethods {
  getSizeStyle: Function
  hidePopup(): void
  showPopup(): void
  blur(): void
  focus(): void
  toggle(): void
  updatePosition(): void
  close(): void
}

declare type FbPopupSelectSlots = {
  header: void
  picker: void
}

declare class FbPopupSelect extends Vue {
  // Props
  value?: boolean
  disabled?: boolean
  position?: string
  // 下拉的最大高度
  maxHeight?: string | number
  showConfirm?: boolean
  // 多选
  multiple?: boolean
  show?: boolean

  // Data
  prefix: string
  myShow: boolean

  // Computed
  getTabindex: number

  // Methods
  getSizeStyle: Function
  hidePopup(): void
  showPopup(): void
  blur(): void
  focus(): void
  toggle(): void
  updatePosition(): void
  close(): void

  // Events
  $emit(event: 'input', value: boolean): void
  $emit(event: 'on-blur'): void
  $emit(event: 'on-focus'): void
  $emit(event: 'on-click'): void

  // Slots
  $slots: FbPopupSelectSlots
}

export { FbPopupSelect }
export default FbPopupSelect