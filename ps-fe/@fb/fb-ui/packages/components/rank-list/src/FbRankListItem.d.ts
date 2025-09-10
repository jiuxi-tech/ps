import { Component, Vue } from 'vue-property-decorator'

export declare class FbRankListItemProps {
  height?: number | string
}

export declare class FbRankListItemData {
  prefix: string
}

export declare class FbRankListItemComputed {
  getItemStyle: object
}

export declare class FbRankListItemMethods {
  handleClick(e: Event): void
  unit(val: number | string): string
}

declare type FbRankListItemSlots = {
  default: void
}

declare class FbRankListItem extends Vue {
  // Props
  height?: number | string

  // Data
  prefix: string

  // Computed
  getItemStyle: object

  // Methods
  handleClick(e: Event): void
  unit(val: number | string): string

  // Events
  $emit(event: 'on-click', e: Event): void

  // Slots
  $slots: FbRankListItemSlots
}

export { FbRankListItem }
export default FbRankListItem