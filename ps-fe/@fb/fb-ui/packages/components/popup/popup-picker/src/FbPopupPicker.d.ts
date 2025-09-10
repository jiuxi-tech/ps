import { Component, Vue } from 'vue-property-decorator'

export declare class FbPopupPickerProps {
  // 显示 管控 点击其他位置消失
  value?: boolean
  // 显示位置
  position?: '' | 'bottomLeft' | 'bottomCenter' | 'bottomRight' | 'topLeft' | 'topCenter' | 'topRight'
  // 触发方式 -- 判断 绑定点击全局事件
  trigger?: '' | 'clickOnly'
}

export declare class FbPopupPickerData {
  prefix: string
  popupPosition: string
}

export declare class FbPopupPickerComputed {
  getClass: string[]
  getTrClass: string
  bindHandleHide: boolean
}

export declare class FbPopupPickerMethods {
  /**
   * 计算位置
   */
  calPosition(): void
  /**
   * 点击后自动关闭
   */
  handleHide(): void
  hidePop(e: Event): void
}

declare type FbPopupPickerSlots = {
  default: void
}

declare class FbPopupPicker extends Vue {
  // Props
  value?: boolean
  position?: '' | 'bottomLeft' | 'bottomCenter' | 'bottomRight' | 'topLeft' | 'topCenter' | 'topRight'
  trigger?: '' | 'clickOnly'

  // Data
  prefix: string
  popupPosition: string

  // Computed
  getClass: string[]
  getTrClass: string
  bindHandleHide: boolean

  // Methods
  /**
   * 计算位置
   */
  calPosition(): void
  /**
   * 点击后自动关闭
   */
  handleHide(): void
  hidePop(e: Event): void

  // Events
  $emit(event: 'input', value: boolean): void

  // Lifecycle
  beforeDestroy(): void

  // Slots
  $slots: FbPopupPickerSlots
}

export { FbPopupPicker }
export default FbPopupPicker