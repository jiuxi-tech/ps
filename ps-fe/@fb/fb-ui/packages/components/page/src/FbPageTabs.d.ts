import { Component, Vue } from 'vue-property-decorator'

export declare class FbPageTabsProps {
  dialog?: object
}

export declare class FbPageTabsData {
  prefix: string
}

export declare class FbPageTabsComputed {
  getClass: string[]
}

export declare class FbPageTabsMethods {
  // 无特定方法
}

declare type FbPageTabsSlots = {
  form: void
  'footer-left': void
  'footer-center': void
  'footer-right': void
  default: void
}

declare class FbPageTabs extends Vue {
  // Props
  dialog?: object

  // Data
  prefix: string

  // Computed
  getClass: string[]

  // Lifecycle hooks
  beforeCreate(): void
  created(): void
  beforeMount(): void
  mounted(): void
  beforeUpdate(): void
  updated(): void

  // Slots
  $slots: FbPageTabsSlots
}

export { FbPageTabs }
export default FbPageTabs