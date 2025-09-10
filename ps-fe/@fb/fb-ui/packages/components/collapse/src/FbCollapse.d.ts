import { VNode } from 'vue'
import { FbUIComponent } from '@/fb-ui/types/component'

export type CollapseAccordion = boolean
export type CollapseValue = any[] | string | number
export type CollapseCollapseIcon = string | number
export type CollapseExpandIcon = string | number

/** Collapse Component */
export declare class FbCollapse extends FbUIComponent {
  /** Whether to activate accordion mode */
  accordion: CollapseAccordion

  /** Currently active panel */
  value: CollapseValue

  /** Icon for collapsed state */
  collapseIcon: CollapseCollapseIcon

  /** Icon for expanded state */
  expandIcon: CollapseExpandIcon
}