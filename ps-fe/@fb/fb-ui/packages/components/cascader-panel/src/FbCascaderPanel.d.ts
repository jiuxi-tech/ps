import { VNode } from 'vue'
import { FbUIComponent } from '@/fb-ui/types/component'

export type CascaderPanelValue = any
export type CascaderPanelData = any[]
export type CascaderPanelProps = object
export type CascaderPanelBorder = boolean
export type CascaderPanelRenderLabel = Function

/** CascaderPanel Component */
export declare class FbCascaderPanel extends FbUIComponent {
  /** Binding value */
  value: CascaderPanelValue

  /** Data of the cascader */
  data: CascaderPanelData

  /** Configuration options */
  props: CascaderPanelProps

  /** Whether to show border */
  border: CascaderPanelBorder

  /** Render function for label */
  renderLabel: CascaderPanelRenderLabel
}