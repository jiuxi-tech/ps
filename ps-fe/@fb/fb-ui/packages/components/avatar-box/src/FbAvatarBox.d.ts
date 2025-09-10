import { VNode } from 'vue'
import { FbUIComponent } from '@/fb-ui/types/component'

export type AvatarBoxLayoutType = string
export type AvatarBoxSize = string | number
export type AvatarBoxCircle = boolean
export type AvatarBoxIcon = string
export type AvatarBoxColor = string
export type AvatarBoxBackgroundColor = string
export type AvatarBoxText = string | number
export type AvatarBoxSrc = string
export type AvatarBoxSrcFill = boolean
export type AvatarBoxAlt = string
export type AvatarBoxLinearIcon = string
export type AvatarBoxLinearSize = string | number
export type AvatarBoxLinearColor = string
export type AvatarBoxLinearType = string
export type AvatarBoxLinearSrc = string
export type AvatarBoxLinearStyle = object | string

/** AvatarBox Component */
export declare class FbAvatarBox extends FbUIComponent {
  /** Layout type */
  layoutType: AvatarBoxLayoutType

  /** Avatar size */
  size: AvatarBoxSize

  /** Whether to display as a circle */
  circle: AvatarBoxCircle

  /** Icon name */
  icon: AvatarBoxIcon

  /** Text color */
  color: AvatarBoxColor

  /** Background color */
  backgroundColor: AvatarBoxBackgroundColor

  /** Avatar text */
  avatarText: AvatarBoxText

  /** Image source */
  src: AvatarBoxSrc

  /** Whether to fill the image */
  srcFill: AvatarBoxSrcFill

  /** Image alt text */
  alt: AvatarBoxAlt

  /** Linear icon name */
  linearIcon: AvatarBoxLinearIcon

  /** Linear icon size */
  linearSize: AvatarBoxLinearSize

  /** Linear icon color */
  linearColor: AvatarBoxLinearColor

  /** Linear type */
  linearType: AvatarBoxLinearType

  /** Linear image source */
  linearSrc: AvatarBoxLinearSrc

  /** Linear style */
  linearStyle: AvatarBoxLinearStyle
}