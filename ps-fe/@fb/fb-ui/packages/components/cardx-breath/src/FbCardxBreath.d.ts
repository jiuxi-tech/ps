import { VNode } from 'vue'
import { FbUIComponent } from '@/fb-ui/types/component'

export type CardxBreathTitle = string
export type CardxBreathEllipsis = boolean
export type CardxBreathType = 'primary' | 'success' | 'danger' | 'warning'
export type CardxBreathCardStatus = string
export type CardxBreathBgColor = string
export type CardxBreathTextColor = string
export type CardxBreathBreath = boolean
export type CardxBreathBreathDuration = string
export type CardxBreathBreathDelay = string
export type CardxBreathBreathCount = string | number
export type CardxBreathBreathLight = any
export type CardxBreathBreathName = string
export type CardxBreathBreathColor = string

/** CardxBreath Component */
export declare class FbCardxBreath extends FbUIComponent {
  /** Card title */
  title: CardxBreathTitle

  /** Whether to ellipsis text */
  ellipsis: CardxBreathEllipsis

  /** Card type */
  type: CardxBreathType

  /** Card status */
  cardStatus: CardxBreathCardStatus

  /** Background color */
  bgColor: CardxBreathBgColor

  /** Text color */
  textColor: CardxBreathTextColor

  /** Whether to enable breath animation */
  breath: CardxBreathBreath

  /** Breath animation duration */
  breathDuration: CardxBreathBreathDuration

  /** Breath animation delay */
  breathDelay: CardxBreathBreathDelay

  /** Breath animation count */
  breathCount: CardxBreathBreathCount

  /** Breath light effect */
  breathLight: CardxBreathBreathLight

  /** Breath animation name */
  breathName: CardxBreathBreathName

  /** Breath animation color */
  breathColor: CardxBreathBreathColor
}