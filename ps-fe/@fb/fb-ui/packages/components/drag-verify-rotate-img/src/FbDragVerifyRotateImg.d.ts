import { VNode } from 'vue'
import { FbUIComponent } from '@/fb-ui/types/component'

export type DragVerifyRotateImgAction = object | string
export type DragVerifyRotateImgDisabled = boolean
export type DragVerifyRotateImgImgBoxShow = boolean
export type DragVerifyRotateImgValue = string
export type DragVerifyRotateImgWidth = string | number
export type DragVerifyRotateImgHeight = string | number
export type DragVerifyRotateImgText = string
export type DragVerifyRotateImgSuccessText = string
export type DragVerifyRotateImgBackground = string
export type DragVerifyRotateImgProgressBarBg = string
export type DragVerifyRotateImgCompletedBg = string
export type DragVerifyRotateImgCompletedBorderColor = string
export type DragVerifyRotateImgCompletedColor = string
export type DragVerifyRotateImgCircle = boolean
export type DragVerifyRotateImgHandlerIcon = string
export type DragVerifyRotateImgSuccessIcon = string
export type DragVerifyRotateImgFailIcon = string
export type DragVerifyRotateImgFailBg = string
export type DragVerifyRotateImgFailBorderColor = string
export type DragVerifyRotateImgFailDuration = string | number
export type DragVerifyRotateImgHandlerBg = string
export type DragVerifyRotateImgHandlerColor = string
export type DragVerifyRotateImgMessageColor = string
export type DragVerifyRotateImgTextSize = string | number

/** DragVerifyRotateImg Component */
export declare class FbDragVerifyRotateImg extends FbUIComponent {
  /** Action URL for image request */
  action: DragVerifyRotateImgAction

  /** Whether the component is disabled */
  disabled: DragVerifyRotateImgDisabled

  /** Whether to show the image background */
  imgBoxShow: DragVerifyRotateImgImgBoxShow

  /** Binding value */
  value: DragVerifyRotateImgValue

  /** Width of the component */
  width: DragVerifyRotateImgWidth

  /** Height of the component */
  height: DragVerifyRotateImgHeight

  /** Text to display */
  text: DragVerifyRotateImgText

  /** Success text to display */
  successText: DragVerifyRotateImgSuccessText

  /** Background color */
  background: DragVerifyRotateImgBackground

  /** Progress bar background color */
  progressBarBg: DragVerifyRotateImgProgressBarBg

  /** Completed background color */
  completedBg: DragVerifyRotateImgCompletedBg

  /** Completed border color */
  completedBorderColor: DragVerifyRotateImgCompletedBorderColor

  /** Completed text color */
  completedColor: DragVerifyRotateImgCompletedColor

  /** Whether to use circle style */
  circle: DragVerifyRotateImgCircle

  /** Handler icon */
  handlerIcon: DragVerifyRotateImgHandlerIcon

  /** Success icon */
  successIcon: DragVerifyRotateImgSuccessIcon

  /** Fail icon */
  failIcon: DragVerifyRotateImgFailIcon

  /** Fail background color */
  failBg: DragVerifyRotateImgFailBg

  /** Fail border color */
  failBorderColor: DragVerifyRotateImgFailBorderColor

  /** Fail duration */
  failDuration: DragVerifyRotateImgFailDuration

  /** Handler background color */
  handlerBg: DragVerifyRotateImgHandlerBg

  /** Handler icon color */
  handlerColor: DragVerifyRotateImgHandlerColor

  /** Message text color */
  messageColor: DragVerifyRotateImgMessageColor

  /** Text size */
  textSize: DragVerifyRotateImgTextSize
}