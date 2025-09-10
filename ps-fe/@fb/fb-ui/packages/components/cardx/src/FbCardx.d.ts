import { VNode } from 'vue'
import { FbUIComponent } from '@/fb-ui/types/component'

export type CardxHeader = string | number
export type CardxShadow = 'on' | 'off' | 'hover'
export type CardxMargin = string | number
export type CardxHeight = string | number
export type CardxNoBorder = boolean
export type CardxNoHeaderBorder = boolean
export type CardxNoPadding = boolean
export type CardxBodyStyle = string | object | any[]
export type CardxTitleSize = string | number

/** Cardx Component */
export declare class FbCardx extends FbUIComponent {
  /** Card header content */
  header: CardxHeader

  /** When to show card shadows */
  shadow: CardxShadow

  /** Card margin */
  margin: CardxMargin

  /** Card body height */
  height: CardxHeight

  /** Whether to hide the border */
  noBorder: CardxNoBorder

  /** Whether to hide the header border */
  noHeaderBorder: CardxNoHeaderBorder

  /** Whether to hide the padding */
  noPadding: CardxNoPadding

  /** Body style */
  bodyStyle: CardxBodyStyle

  /** Title size */
  titleSize: CardxTitleSize
}