import { VNode } from 'vue'
import { FbUIComponent } from '@/fb-ui/types/component'

export type DragVerifyValue = boolean
export type DragVerifyWidth = number | string
export type DragVerifyHeight = number | string
export type DragVerifyText = string
export type DragVerifySuccessText = string
export type DragVerifyBackground = string
export type DragVerifyProgressBarBg = string
export type DragVerifyCompletedBg = string
export type DragVerifyCompletedColor = string
export type DragVerifyCircle = boolean
export type DragVerifyHandlerIcon = string
export type DragVerifySuccessIcon = string
export type DragVerifyHandlerBg = string
export type DragVerifyHandlerColor = string
export type DragVerifyMessageColor = string
export type DragVerifyTextSize = number | string

/** DragVerify Component */
export declare class FbDragVerify extends FbUIComponent {
  /** Binding value */
  value: DragVerifyValue

  /** Width of the drag verify */
  width: DragVerifyWidth

  /** Height of the drag verify */
  height: DragVerifyHeight

  /** Text to display */
  text: DragVerifyText

  /** Success text to display */
  successText: DragVerifySuccessText

  /** Background color */
  background: DragVerifyBackground

  /** Progress bar background color */
  progressBarBg: DragVerifyProgressBarBg

  /** Completed background color */
  completedBg: DragVerifyCompletedBg

  /** Completed text color */
  completedColor: DragVerifyCompletedColor

  /** Whether to use circle style */
  circle: DragVerifyCircle

  /** Handler icon */
  handlerIcon: DragVerifyHandlerIcon

  /** Success icon */
  successIcon: DragVerifySuccessIcon

  /** Handler background color */
  handlerBg: DragVerifyHandlerBg

  /** Handler icon color */
  handlerColor: DragVerifyHandlerColor

  /** Message text color */
  messageColor: DragVerifyMessageColor

  /** Text size */
  textSize: DragVerifyTextSize
}