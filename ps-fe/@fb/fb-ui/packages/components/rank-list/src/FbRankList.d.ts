import { Component, Vue } from 'vue-property-decorator'

export declare class FbRankListProps {
  config?: object
  data?: any[]
  // 开启首尾相接
  scroll?: boolean
  scrollDirection?: string
  scrollSpeed?: number
  pauseOnHover?: boolean
  forceStart?: boolean
  /**
   * @description Scroll wait time
   * @type {Number}
   * @default waitTime = 2000
   */
  waitTime?: number
}

export declare class FbRankListData {
  ref: string
  defaultConfig: {
    /**
     * @description Board data
     * @type {Array<Object>}
     * @default data = []
     */
    data: any[]
    /**
     * @description Row num
     * @type {Number}
     * @default rowNum = 0
     */
    rowNum: number
    /**
     * @description Scroll wait time
     * @type {Number}
     * @default waitTime = 1000
     */
    // waitTime: 2000
    /**
     * @description Carousel type
     * @type {String}
     * @default carousel = 'single'
     * @example carousel = 'single' | 'page'
     */
    carousel: string
    /**
     * @description Value unit
     * @type {String}
     * @default unit = ''
     * @example unit = 'ton'
     */
    unit: string
    /**
     * @description Auto sort by value
     * @type {Boolean}
     * @default sort = true
     */
    sort: boolean
    /**
     * @description Value formatter
     * @type {Function}
     * @default valueFormatter = null
     */
    valueFormatter: Function | null
  }
  mergedConfig: any
  rowsData: any[]
  rows: any[]
  heights: number[]
  opacityArr: number[]
  animationIndex: number
  animationHandler: any
  updater: number
  start: boolean
  width: number
  height: number
  dom: any
  avgHeight: number
}

export declare class FbRankListMethods {
  initWH(): void
  afterAutoResizeMixinInit(): void
  onResize(): void
  calcData(): void
  mergeConfig(): void
  calcRowsData(): void
  calcRowsHeight(rows: any[], rowLength: number): number
  calcHeights(onresize?: boolean): void
  runAnimation(): void
  animation(start?: boolean): Promise<void>
  animationScroll(start: boolean): Promise<void>
  stopAnimation(): void
  hoverStarted(): void
  hoverEnded(): void
}

declare type FbRankListSlots = {
  default: void
}

declare class FbRankList extends Vue {
  // Props
  config?: object
  data?: any[]
  // 开启首尾相接
  scroll?: boolean
  scrollDirection?: string
  scrollSpeed?: number
  pauseOnHover?: boolean
  forceStart?: boolean
  /**
   * @description Scroll wait time
   * @type {Number}
   * @default waitTime = 2000
   */
  waitTime?: number

  // Data
  ref: string
  defaultConfig: {
    /**
     * @description Board data
     * @type {Array<Object>}
     * @default data = []
     */
    data: any[]
    /**
     * @description Row num
     * @type {Number}
     * @default rowNum = 0
     */
    rowNum: number
    /**
     * @description Scroll wait time
     * @type {Number}
     * @default waitTime = 1000
     */
    // waitTime: 2000
    /**
     * @description Carousel type
     * @type {String}
     * @default carousel = 'single'
     * @example carousel = 'single' | 'page'
     */
    carousel: string
    /**
     * @description Value unit
     * @type {String}
     * @default unit = ''
     * @example unit = 'ton'
     */
    unit: string
    /**
     * @description Auto sort by value
     * @type {Boolean}
     * @default sort = true
     */
    sort: boolean
    /**
     * @description Value formatter
     * @type {Function}
     * @default valueFormatter = null
     */
    valueFormatter: Function | null
  }
  mergedConfig: any
  rowsData: any[]
  rows: any[]
  heights: number[]
  opacityArr: number[]
  animationIndex: number
  animationHandler: any
  updater: number
  start: boolean
  width: number
  height: number
  dom: any
  avgHeight: number

  // Methods
  initWH(): void
  afterAutoResizeMixinInit(): void
  onResize(): void
  calcData(): void
  mergeConfig(): void
  calcRowsData(): void
  calcRowsHeight(rows: any[], rowLength: number): number
  calcHeights(onresize?: boolean): void
  runAnimation(): void
  animation(start?: boolean): Promise<void>
  animationScroll(start: boolean): Promise<void>
  stopAnimation(): void
  hoverStarted(): void
  hoverEnded(): void

  // Events
  $emit(event: 'onPause'): void
  $emit(event: 'onResume'): void

  // Lifecycle
  mounted(): void
  destroyed(): void

  // Slots
  $slots: FbRankListSlots
}

export { FbRankList }
export default FbRankList