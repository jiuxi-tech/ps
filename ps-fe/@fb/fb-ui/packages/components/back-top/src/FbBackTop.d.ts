import { VNode } from 'vue'
import { FbUIComponent } from '@/fb-ui/types/component'

export type BackTopVisibilityHeight = number
export type BackTopTarget = string
export type BackTopRight = number
export type BackTopBottom = number
export type BackTopCircle = boolean

/** BackTop Component */
export declare class FbBackTop extends FbUIComponent {
  /** The scroll height that triggers the BackTop to show */
  visibilityHeight: BackTopVisibilityHeight

  /** The target to trigger scroll */
  target: BackTopTarget

  /** The position of the BackTop from the right */
  right: BackTopRight

  /** The position of the BackTop from the bottom */
  bottom: BackTopBottom

  /** Whether to use circle style */
  circle: BackTopCircle
}