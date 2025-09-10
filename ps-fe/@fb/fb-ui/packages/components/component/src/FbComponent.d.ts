import { VNode } from 'vue'
import { FbUIComponent } from '@/fb-ui/types/component'

export type ComponentComponent = object

/** Component Component */
export declare class FbComponent extends FbUIComponent {
  /** Component to render */
  component: ComponentComponent
}