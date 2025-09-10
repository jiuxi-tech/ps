import { Component, Vue } from 'vue-property-decorator'
import { FbIcon } from '../../icon/src/FbIcon'

export declare class FbResultProps {
  type?: string
  imgSrc?: string | object
  message?: string
  describe?: string
  iconSize?: string | number
  iconName?: string
  iconBg?: string
}

export declare class FbResultData {
  prefix: string
}

export declare class FbResultComputed {
  getBodyClass: string[]
  getIconStyle: object
  getIconName: string
  getFbIconStyle: object
}

export declare class FbResultMethods {
  // 无特定方法
}

declare type FbResultSlots = {
  describe: void
}

declare class FbResult extends Vue {
  // Props
  type?: string
  imgSrc?: string | object
  message?: string
  describe?: string
  iconSize?: string | number
  iconName?: string
  iconBg?: string

  // Data
  prefix: string

  // Computed
  getBodyClass: string[]
  getIconStyle: object
  getIconName: string
  getFbIconStyle: object

  // Methods
  // 无特定方法

  // Slots
  $slots: FbResultSlots
}

export { FbResult }
export default FbResult