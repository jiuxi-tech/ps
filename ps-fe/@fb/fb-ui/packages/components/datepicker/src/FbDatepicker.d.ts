import { VNode } from 'vue'
import { FbUIComponent } from '@/fb-ui/types/component'

export type DatepickerValue = Date | any[] | number | string
export type DatepickerDisabled = boolean
export type DatepickerReadonly = boolean
export type DatepickerPosition = string
export type DatepickerMode = 'single' | 'multiple' | 'range'
export type DatepickerShowConfirm = boolean
export type DatepickerRangeSeparator = string
export type DatepickerClearable = boolean
export type DatepickerPlaceholder = string
export type DatepickerFormat = string
export type DatepickerEnableTime = boolean
export type DatepickerEnableSeconds = boolean
export type DatepickerOnlyMonth = boolean
export type DatepickerMinDate = Date
export type DatepickerMaxDate = Date
export type DatepickerMaxRange = string
export type DatepickerDisable = any[]
export type DatepickerEnable = any[]
export type DatepickerDisabledDate = Function
export type DatepickerShowFootLeftBtns = any[]
export type DatepickerSize = string

/** Datepicker Component */
export declare class FbDatepicker extends FbUIComponent {
  /** Binding value */
  value: DatepickerValue

  /** Whether Datepicker is disabled */
  disabled: DatepickerDisabled

  /** Whether Datepicker is readonly */
  readonly: DatepickerReadonly

  /** Position of the panel */
  position: DatepickerPosition

  /** Mode of the Datepicker */
  mode: DatepickerMode

  /** Whether to show confirm button */
  showConfirm: DatepickerShowConfirm

  /** Separator for range mode */
  rangeSeparator: DatepickerRangeSeparator

  /** Whether to show clear button */
  clearable: DatepickerClearable

  /** Placeholder text */
  placeholder: DatepickerPlaceholder

  /** Format of the date */
  format: DatepickerFormat

  /** Whether to enable time selection */
  enableTime: DatepickerEnableTime

  /** Whether to enable seconds selection */
  enableSeconds: DatepickerEnableSeconds

  /** Whether to only show month */
  onlyMonth: DatepickerOnlyMonth

  /** Minimum date */
  minDate: DatepickerMinDate

  /** Maximum date */
  maxDate: DatepickerMaxDate

  /** Maximum range */
  maxRange: DatepickerMaxRange

  /** Disabled dates */
  disable: DatepickerDisable

  /** Enabled dates */
  enable: DatepickerEnable

  /** Function to disable dates */
  disabledDate: DatepickerDisabledDate

  /** Buttons to show in the footer */
  showFootLeftBtns: DatepickerShowFootLeftBtns

  /** Size of the Datepicker */
  size: DatepickerSize
}