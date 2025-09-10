import { Component, Vue } from 'vue-property-decorator'
import { FbIcon } from '../../icon/src/FbIcon'

export declare class FbPageHeaderProps {
  // 面包屑
  breadcrumb?: object | any[]
  // 返回图标
  back?: string
  // 标题
  title?: string | number
  // 副标题
  subTitle?: string | number
  // 标签组
  tags?: any[]
  titleStyle?: string | object
}

export declare class FbPageHeaderData {
  prefix: string
  myBreadcrumb: any[]
}

export declare class FbPageHeaderComputed {
  hasBreadcrumb: boolean
  getClass: string[]
}

export declare class FbPageHeaderMethods {
  handleBack(): void
}

declare type FbPageHeaderSlots = {
  back: void
  tags: void
  actions: void
  default: void
}

declare class FbPageHeader extends Vue {
  // Props
  breadcrumb?: object | any[]
  back?: string
  title?: string | number
  subTitle?: string | number
  tags?: any[]
  titleStyle?: string | object

  // Data
  prefix: string
  myBreadcrumb: any[]

  // Computed
  hasBreadcrumb: boolean
  getClass: string[]

  // Methods
  handleBack(): void

  // Events
  $emit(event: 'on-back'): void

  // Slots
  $slots: FbPageHeaderSlots
}

export { FbPageHeader }
export default FbPageHeader