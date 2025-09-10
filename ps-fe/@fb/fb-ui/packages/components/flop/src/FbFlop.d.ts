import { VNode } from 'vue'
import { FbUIComponent } from '@/fb-ui/types/component'

export type FlopType = string
export type FlopNum = number | string
export type FlopBrandPrefix = number | string
export type FlopBrandSuffix = number | string
export type FlopMinLength = number | string
export type FlopMaxLength = number | string
export type FlopBrandSize = string
export type FlopBrandStatus = string
export type FlopBrandStyle = object | any[]
export type FlopNoPlay = boolean
export type FlopDuration = number
export type FlopFlipType = string
export type FlopReverse = boolean
export type FlopTheme = string
export type FlopSize = string
export type FlopBrandHeight = number
export type FlopPanelDelay = number
export type FlopNumberProps = object

/** FbFlop Component */
export declare class FbFlop extends FbUIComponent {
  /** Type of the flop */
  type: FlopType

  /** Number to display */
  num: FlopNum

  /** Prefix for the brand type */
  brandPrefix: FlopBrandPrefix

  /** Suffix for the brand type */
  brandSuffix: FlopBrandSuffix

  /** Minimum length */
  minLength: FlopMinLength

  /** Maximum length */
  maxLength: FlopMaxLength

  /** Size of the brand */
  brandSize: FlopBrandSize

  /** Status of the brand */
  brandStatus: FlopBrandStatus

  /** Style of the brand */
  brandStyle: FlopBrandStyle

  /** Whether to disable animation */
  noPlay: FlopNoPlay

  /** Duration of the animation */
  duration: FlopDuration

  /** Type of flip animation */
  flipType: FlopFlipType

  /** Whether to reverse the order */
  reverse: FlopReverse

  /** Theme of the component */
  theme: FlopTheme

  /** Size of the component */
  size: FlopSize

  /** Height of the brand */
  brandHeight: FlopBrandHeight

  /** Delay for panel animation */
  panelDelay: FlopPanelDelay

  /** Props for the number component */
  numberProps: FlopNumberProps
}