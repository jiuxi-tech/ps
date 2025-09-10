import { VNode } from 'vue'
import { FbUIComponent } from '@/fb-ui/types/component'

export type ColorPickerValue = string
export type ColorPickerShowAlpha = boolean
export type ColorPickerColorFormat = string
export type ColorPickerDisabled = boolean
export type ColorPickerSize = string
export type ColorPickerPopperClass = string
export type ColorPickerPredefine = any[]
export type ColorPickerThemePredefine = boolean
export type ColorPickerAlpha = boolean
export type ColorPickerFormat = string
export type ColorPickerNeedConfirm = boolean

/** ColorPicker Component */
export declare class FbColorPicker extends FbUIComponent {
  /** Binding value */
  value: ColorPickerValue

  /** Whether to display the alpha slider */
  showAlpha: ColorPickerShowAlpha

  /** Color format of the binding value */
  colorFormat: ColorPickerColorFormat

  /** Whether ColorPicker is disabled */
  disabled: ColorPickerDisabled

  /** Size of ColorPicker */
  size: ColorPickerSize

  /** Custom class name for ColorPicker's dropdown */
  popperClass: ColorPickerPopperClass

  /** Predefined color options */
  predefine: ColorPickerPredefine

  /** Whether to use theme predefined colors */
  themePredefine: ColorPickerThemePredefine

  /** Whether to enable alpha channel */
  alpha: ColorPickerAlpha

  /** Color format */
  format: ColorPickerFormat

  /** Whether to need confirm */
  needConfirm: ColorPickerNeedConfirm
}