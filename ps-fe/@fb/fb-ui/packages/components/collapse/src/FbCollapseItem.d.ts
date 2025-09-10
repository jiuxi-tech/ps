import { VNode } from 'vue'
import { FbUIComponent } from '@/fb-ui/types/component'

export type CollapseItemTitle = string
export type CollapseItemItemKey = string | number
export type CollapseItemDisabled = boolean
export type CollapseItemIsActions = boolean
export type CollapseItemCollapseIcon = string
export type CollapseItemExpandIcon = string

/** CollapseItem Component */
export declare class FbCollapseItem extends FbUIComponent {
  /** Title of the panel */
  title: CollapseItemTitle

  /** Unique identifier of the panel */
  itemKey: CollapseItemItemKey

  /** Whether the panel is disabled */
  disabled: CollapseItemDisabled

  /** Whether to use actions slot */
  isActions: CollapseItemIsActions

  /** Icon for collapsed state */
  collapseIcon: CollapseItemCollapseIcon

  /** Icon for expanded state */
  expandIcon: CollapseItemExpandIcon
}