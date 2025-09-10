import { Component, Vue } from 'vue-property-decorator'
import { FbDialog } from '../../dialog/src/FbDialog'

export declare class FbPdfViewerProps {
  src?: string
  beforeClose?: Function
}

export declare class FbPdfViewerData {
  prefix: string
  showPdfViewer: boolean
  pdfSrc: string
}

export declare class FbPdfViewerMethods {
  close(): void
  show(): void
}

declare type FbPdfViewerSlots = {
  default: void
}

declare class FbPdfViewer extends Vue {
  // Props
  src?: string
  beforeClose?: Function

  // Data
  prefix: string
  showPdfViewer: boolean
  pdfSrc: string

  // Methods
  close(): void
  show(): void

  // Lifecycle
  mounted(): void
  beforeDestroy(): void
  destroyed(): void
}

export { FbPdfViewer }
export default FbPdfViewer