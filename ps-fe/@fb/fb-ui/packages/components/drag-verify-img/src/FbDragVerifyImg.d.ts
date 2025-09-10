import { VNode } from 'vue'
import { FbUIComponent } from '@/fb-ui/types/component'

export type DragVerifyImgAction = object | string
export type DragVerifyImgDisabled = boolean
export type DragVerifyImgImgBoxShow = boolean
export type DragVerifyImgValue = string
export type DragVerifyImgWidth = string | number
export type DragVerifyImgHeight = string | number
export type DragVerifyImgText = string
export type DragVerifyImgSuccessText = string
export type DragVerifyImgBackground = string
export type DragVerifyImgProgressBarBg = string
export type DragVerifyImgCompletedBg = string
export type DragVerifyImgCompletedBorderColor = string
export type DragVerifyImgCompletedColor = string
export type DragVerifyImgCircle = boolean
export type DragVerifyImgHandlerIcon = string
export type DragVerifyImgSuccessIcon = string
export type DragVerifyImgFailIcon = string
export type DragVerifyImgFailBg = string
export type DragVerifyImgFailBorderColor = string
export type DragVerifyImgFailDuration = string | number
export type DragVerifyImgHandlerBg = string
export type DragVerifyImgHandlerColor = string
export type DragVerifyImgMessageColor = string
export type DragVerifyImgTextSize = string | number
export type DragVerifyImgCaptchaType = string | number
export type DragVerifyImgXScale = number

/** DragVerifyImg Component */
export declare class FbDragVerifyImg extends FbUIComponent {
  /** Action URL for image request */
  action: DragVerifyImgAction

  /** Whether the component is disabled */
  disabled: DragVerifyImgDisabled

  /** Whether to show the image background */
  imgBoxShow: DragVerifyImgImgBoxShow

  /** Binding value */
  value: DragVerifyImgValue

  /** Width of the component */
  width: DragVerifyImgWidth

  /** Height of the component */
  height: DragVerifyImgHeight

  /** Text to display */
  text: DragVerifyImgText

  /** Success text to display */
  successText: DragVerifyImgSuccessText

  /** Background color */
  background: DragVerifyImgBackground

  /** Progress bar background color */
  progressBarBg: DragVerifyImgProgressBarBg

  /** Completed background color */
  completedBg: DragVerifyImgCompletedBg

  /** Completed border color */
  completedBorderColor: DragVerifyImgCompletedBorderColor

  /** Completed text color */
  completedColor: DragVerifyImgCompletedColor

  /** Whether to use circle style */
  circle: DragVerifyImgCircle

  /** Handler icon */
  handlerIcon: DragVerifyImgHandlerIcon

  /** Success icon */
  successIcon: DragVerifyImgSuccessIcon

  /** Fail icon */
  failIcon: DragVerifyImgFailIcon

  /** Fail background color */
  failBg: DragVerifyImgFailBg

  /** Fail border color */
  failBorderColor: DragVerifyImgFailBorderColor

  /** Fail duration */
  failDuration: DragVerifyImgFailDuration

  /** Handler background color */
  handlerBg: DragVerifyImgHandlerBg

  /** Handler icon color */
  handlerColor: DragVerifyImgHandlerColor

  /** Message text color */
  messageColor: DragVerifyImgMessageColor

  /** Text size */
  textSize: DragVerifyImgTextSize

  /** Captcha type */
  captchaType: DragVerifyImgCaptchaType

  /** X scale */
  xScale: DragVerifyImgXScale
}