import { Component, Vue } from 'vue-property-decorator'
import { FbIcon } from '../../icon/src/FbIcon'
import { FbInput } from '../../input/src/FbInput'
import { FbSelect } from '../../select/src/FbSelect'

export declare class FbPagerProps {
  // 对齐方式
  align?: string
  // 分页大小
  size?: number | string
  // 当前页面
  current?: number | string
  // 总页数
  pages?: number | string
  // 总记录数
  total?: number | string
  // 锁定
  locking?: boolean
  // 分页数下拉
  showSizeChanger?: boolean
  // 分页数列表
  sizeList?: number[]
  // 跳转框
  showQuickJumper?: boolean
  // 显示总页数
  showTotalInfo?: boolean
  simple?: boolean
  maxLength?: number | string
}

export declare class FbPagerData {
  prefix: string
  pageSizeList: number[]
  len: number | string
  myCurrent: number | string
  mySize: number | string
  myPages: number | string
  jumperPage: string
}

export declare class FbPagerComputed {
  myPageSizeList: Array<{ value: number; label: number }>
  getClass: string[]
  pageCode: number[]
  codes: number[]
  showFirst: boolean
  showLast: boolean
}

export declare class FbPagerMethods {
  dispatch(): void
  last(): void
  next(): void
  prev(): void
  nnext(): void
  go(code: number | string): void
  jumper(): void
  changePageSize(): void
}

declare type FbPagerSlots = {
  default: void
}

declare class FbPager extends Vue {
  // Props
  align?: string
  size?: number | string
  current?: number | string
  pages?: number | string
  total?: number | string
  locking?: boolean
  showSizeChanger?: boolean
  sizeList?: number[]
  showQuickJumper?: boolean
  showTotalInfo?: boolean
  simple?: boolean
  maxLength?: number | string

  // Data
  prefix: string
  pageSizeList: number[]
  len: number | string
  myCurrent: number | string
  mySize: number | string
  myPages: number | string
  jumperPage: string

  // Computed
  myPageSizeList: Array<{ value: number; label: number }>
  getClass: string[]
  pageCode: number[]
  codes: number[]
  showFirst: boolean
  showLast: boolean

  // Methods
  dispatch(): void
  last(): void
  next(): void
  prev(): void
  nnext(): void
  go(code: number | string): void
  jumper(): void
  changePageSize(): void

  // Events
  $emit(event: 'on-change', data: { 
    current: number | string; 
    page: number | string; 
    size: number | string; 
    pageSize: number | string; 
    myPages: number | string; 
    pages: number | string; 
    total: number | string 
  }): void
  $emit(event: 'update:current', value: number | string): void
  $emit(event: 'update:size', value: number | string): void
}

export { FbPager }
export default FbPager