import { VNode } from 'vue'
import { FbUIComponent } from '@/fb-ui/types/component'

export type IconSelectValue = string
export type IconSelectLabel = string
export type IconSelectPlaceholder = string
export type IconSelectDisabled = boolean
export type IconSelectClearable = boolean
export type IconSelectReadonly = boolean
export type IconSelectPosition = string
export type IconSelectSize = string

/** IconSelect Component */
export declare class FbIconSelect extends FbUIComponent {
  /** Binding value */
  value: IconSelectValue

  /** Label text */
  label: IconSelectLabel

  /** Placeholder text */
  placeholder: IconSelectPlaceholder

  /** Whether IconSelect is disabled */
  disabled: IconSelectDisabled

  /** Whether IconSelect is clearable */
  clearable: IconSelectClearable

  /** Whether IconSelect is readonly */
  readonly: IconSelectReadonly

  /** Position of the popup */
  position: IconSelectPosition

  /** Size of the IconSelect */
  size: IconSelectSize
}