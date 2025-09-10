import { VNode } from 'vue'
import { FbUIComponent } from '@/fb-ui/types/component'

export type CheckboxGroupValue = any[]
export type CheckboxGroupData = any[]
export type CheckboxGroupVertical = boolean
export type CheckboxGroupReader = {
  value: string
  label: string
}
export type CheckboxGroupDisabled = boolean
export type CheckboxGroupReadonly = boolean
export type CheckboxGroupCheckboxStyle = object | any[] | string
export type CheckboxGroupSize = string

/** CheckboxGroup Component */
export declare class FbCheckboxGroup extends FbUIComponent {
  /** Selected values */
  value: CheckboxGroupValue

  /** Data for checkboxes */
  data: CheckboxGroupData

  /** Whether the checkboxes are vertical */
  vertical: CheckboxGroupVertical

  /** Reader configuration */
  reader: CheckboxGroupReader

  /** Whether the checkboxes are disabled */
  disabled: CheckboxGroupDisabled

  /** Whether the checkboxes are readonly */
  readonly: CheckboxGroupReadonly

  /** Style for checkboxes */
  checkboxStyle: CheckboxGroupCheckboxStyle

  /** Size of the checkboxes */
  size: CheckboxGroupSize
}