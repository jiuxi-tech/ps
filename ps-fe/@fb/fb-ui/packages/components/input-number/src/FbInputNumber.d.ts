import { VNode } from 'vue'
import { FbUIComponent } from '@/fb-ui/types/component'

export type InputNumberStep = number
export type InputNumberStepStrictly = boolean
export type InputNumberMax = number
export type InputNumberMin = number
export type InputNumberValue = any
export type InputNumberDisabled = boolean
export type InputNumberSize = string
export type InputNumberControls = boolean
export type InputNumberControlsPosition = string
export type InputNumberName = string
export type InputNumberLabel = string
export type InputNumberPlaceholder = string
export type InputNumberPrecision = number
export type InputNumberReadonly = boolean
export type InputNumberClearable = boolean
export type InputNumberMaxlength = number | string
export type InputNumberPrependIcon = string
export type InputNumberIcon = string
export type InputNumberWidth = string | number
export type InputNumberAutocomplete = string
export type InputNumberRound = boolean
export type InputNumberElStyle = string | any[] | object
export type InputNumberPrepend = string
export type InputNumberAppend = string

/** InputNumber Component */
export declare class FbInputNumber extends FbUIComponent {
  /** Incremental step */
  step: InputNumberStep

  /** Whether input value can only be multiple of step */
  stepStrictly: InputNumberStepStrictly

  /** Maximum allowed value */
  max: InputNumberMax

  /** Minimum allowed value */
  min: InputNumberMin

  /** Binding value */
  value: InputNumberValue

  /** Whether the component is disabled */
  disabled: InputNumberDisabled

  /** Size of the component */
  size: InputNumberSize

  /** Whether to enable the control buttons */
  controls: InputNumberControls

  /** Position of the control buttons */
  controlsPosition: InputNumberControlsPosition

  /** Same as name in native input */
  name: InputNumberName

  /** Same as label in native input */
  label: InputNumberLabel

  /** Placeholder text */
  placeholder: InputNumberPlaceholder

  /** Precision of input value */
  precision: InputNumberPrecision

  /** Whether the input is readonly */
  readonly: InputNumberReadonly

  /** Whether the input is clearable */
  clearable: InputNumberClearable

  /** Maximum length of input */
  maxlength: InputNumberMaxlength

  /** Prepend icon */
  prependIcon: InputNumberPrependIcon

  /** Append icon */
  icon: InputNumberIcon

  /** Width of the component */
  width: InputNumberWidth

  /** Autocomplete of the input */
  autocomplete: InputNumberAutocomplete

  /** Whether the input has rounded corners */
  round: InputNumberRound

  /** Style of the input element */
  elStyle: InputNumberElStyle

  /** Prepend text */
  prepend: InputNumberPrepend

  /** Append text */
  append: InputNumberAppend
}