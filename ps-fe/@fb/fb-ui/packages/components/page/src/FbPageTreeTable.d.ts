import { Component, Vue } from 'vue-property-decorator'
import { FbCol } from '../../col/src/FbCol'
import { FbRow } from '../../row/src/FbRow'
import { FbCard } from '../../card/src/FbCard'
import { FbTabs } from '../../tab/src/FbTabs'

export declare class FbPageTreeTableProps {
  title?: string
  dialog?: object
  gutter?: number | string
  spans?: number[]
}

export declare class FbPageTreeTableData {
  prefix: string
}

export declare class FbPageTreeTableComputed {
  getClass: string[]
}

export declare class FbPageTreeTableMethods {
  // 无特定方法
}

declare type FbPageTreeTableSlots = {
  title: void
  'tree-actions': void
  tree: void
  query: void
  actions: void
  buttons: void
  table: void
  pager: void
  'footer-left': void
  'footer-center': void
  'footer-right': void
  default: void
}

declare class FbPageTreeTable extends Vue {
  // Props
  title?: string
  dialog?: object
  gutter?: number | string
  spans?: number[]

  // Data
  prefix: string

  // Computed
  getClass: string[]

  // Slots
  $slots: FbPageTreeTableSlots
}

export { FbPageTreeTable }
export default FbPageTreeTable