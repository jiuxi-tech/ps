import { VNode } from 'vue'
import { FbUIComponent } from '@/fb-ui/types/component'

export type CascaderValue = any
export type CascaderData = any[]
export type CascaderProps = object
export type CascaderSize = string
export type CascaderPlaceholder = string
export type CascaderDisabled = boolean
export type CascaderClearable = boolean
export type CascaderFilterable = boolean
export type CascaderFilterMethod = Function
export type CascaderSeparator = string
export type CascaderShowAllLevels = boolean
export type CascaderCollapseTags = boolean
export type CascaderDebounce = number
export type CascaderBeforeFilter = Function
export type CascaderPopperClass = string

/** Cascader Component */
export declare class FbCascader extends FbUIComponent {
  /** Selected value */
  value: CascaderValue

  /** Data of the cascader */
  data: CascaderData

  /** Configuration options */
  props: CascaderProps

  /** Size of the cascader */
  size: CascaderSize

  /** Placeholder text */
  placeholder: CascaderPlaceholder

  /** Whether the cascader is disabled */
  disabled: CascaderDisabled

  /** Whether the cascader is clearable */
  clearable: CascaderClearable

  /** Whether the cascader is filterable */
  filterable: CascaderFilterable

  /** Custom filter method */
  filterMethod: CascaderFilterMethod

  /** Separator of the cascader */
  separator: CascaderSeparator

  /** Whether to show all levels */
  showAllLevels: CascaderShowAllLevels

  /** Whether to collapse tags */
  collapseTags: CascaderCollapseTags

  /** Debounce delay for filtering */
  debounce: CascaderDebounce

  /** Hook function before filtering */
  beforeFilter: CascaderBeforeFilter

  /** Custom class name for the popper */
  popperClass: CascaderPopperClass
}