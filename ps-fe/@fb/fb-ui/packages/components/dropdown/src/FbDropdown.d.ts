import { VNode } from 'vue'
import { FbUIComponent } from '@/fb-ui/types/component'

export type DropdownSize = 's' | 'm' | 'l' | 'xl'
export type DropdownType = string | null
export type DropdownColor = string | null
export type DropdownEllipsis = boolean
export type DropdownWidth = string | number | null
export type DropdownBold = boolean
export type DropdownWeight = string | number | null
export type DropdownAlign = string | null

/** Dropdown Component */
export declare class FbDropdown extends FbUIComponent {
  /** Size of the dropdown */
  size: DropdownSize

  /** Type of the dropdown */
  type: DropdownType

  /** Color of the dropdown */
  color: DropdownColor

  /** Whether the text should be ellipsis */
  ellipsis: DropdownEllipsis

  /** Width of the dropdown */
  width: DropdownWidth

  /** Whether the text should be bold */
  bold: DropdownBold

  /** Font weight of the dropdown */
  weight: DropdownWeight

  /** Text alignment of the dropdown */
  align: DropdownAlign
}