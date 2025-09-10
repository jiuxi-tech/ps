import { VNode } from 'vue'
import { FbUIComponent } from '@/fb-ui/types/component'

export type FlopperFlipNum = number | string
export type FlopperFlipType = string
export type FlopperFlipStyle = object | any[]
export type FlopperFrontText = number | string
export type FlopperBackText = number | string
export type FlopperDuration = number

/** FbFlopper Component */
export declare class FbFlopper extends FbUIComponent {
  /** Number to flip */
  flipNum: FlopperFlipNum

  /** Type of flip animation */
  flipType: FlopperFlipType

  /** Style of the flipper */
  flipStyle: FlopperFlipStyle

  /** Front text */
  frontText: FlopperFrontText

  /** Back text */
  backText: FlopperBackText

  /** Duration of the flip animation */
  duration: FlopperDuration
}