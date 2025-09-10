import { Component, Vue } from 'vue-property-decorator'

export declare class FbPageSearchProps {
  dialog?: object
}

export declare class FbPageSearchData {
  prefix: string
}

export declare class FbPageSearchComputed {
  getClass: string[]
}

export declare class FbPageSearchMethods {
  // 无特定方法
}

declare type FbPageSearchSlots = {
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

declare class FbPageSearch extends Vue {
  // Props
  dialog?: object

  // Data
  prefix: string

  // Computed
  getClass: string[]

  // Slots
  $slots: FbPageSearchSlots
}

export { FbPageSearch }
export default FbPageSearch