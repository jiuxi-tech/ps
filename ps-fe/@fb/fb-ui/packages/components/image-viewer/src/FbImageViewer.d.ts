import { VNode } from 'vue'
import { FbUIComponent } from '@/fb-ui/types/component'

export type ImageViewerImageUrls = any[]
export type ImageViewerZIndex = number
export type ImageViewerBeforeClose = Function
export type ImageViewerImageIndex = number
export type ImageViewerAppendToBody = boolean
export type ImageViewerCloseOnClickShadow = boolean
export type ImageViewerDisableEsc = boolean

/** ImageViewer Component */
export declare class FbImageViewer extends FbUIComponent {
  /** Array of image URLs */
  imageUrls: ImageViewerImageUrls

  /** zIndex of the viewer */
  zIndex: ImageViewerZIndex

  /** Callback function before closing */
  beforeClose: ImageViewerBeforeClose

  /** Index of the initial image */
  imageIndex: ImageViewerImageIndex

  /** Whether to append to body */
  appendToBody: ImageViewerAppendToBody

  /** Whether to close when clicking on shadow */
  closeOnClickShadow: ImageViewerCloseOnClickShadow

  /** Whether to disable ESC key */
  disableEsc: ImageViewerDisableEsc
}