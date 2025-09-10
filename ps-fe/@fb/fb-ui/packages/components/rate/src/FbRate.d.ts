import { Component, Vue } from 'vue-property-decorator'
import { FbText } from '../../text/src/FbText'
import { FbIcon } from '../../icon/src/FbIcon'
import { FbInput } from '../../input/src/FbInput'
import { FbButton } from '../../button/src/FbButton'
import { FbTooltip } from '../../tooltip/src/tooltip'

export declare class FbRateProps {
  /**
   * 当前值
   */
  value?: number | string
  /**
   * 是否显示半数
   */
  half?: boolean
  /**
   * 尺寸
   * 支持单一数值及数组
   */
  size?: string | number | any[]
  /**
   * 最大数量
   */
  maxLength?: string | number
  /**
   * 激活项颜色
   */
  activeColor?: string | any[]
  /**
   * 鼠标移动时高亮颜色
   */
  hoverColor?: string | any[]
  /**
   * 不活动项的颜色
   */
  inActiveColor?: string | any[]
  /**
   * 图标，支持单一图标或图标数组
   */
  icon?: string | any[]
  /**
   * 分值描述数组
   */
  labels?: any[]
  /**
   * 分值数组
   */
  values?: any[]
  /**
   * 节点数据, 暂不支持
   */
  data?: any[]
  /**
   * 是否只读
   */
  readonly?: boolean
  /**
   * 可清除
   * 在当前激活分值上再次点击，可以取消分值
   */
  clearable?: boolean
  /**
   * 前缀内容
   */
  prepend?: string
  /**
   * 后缀内容
   */
  append?: string
  /**
   * 是否显示评分值描述
   */
  desc?: boolean
  /**
   * 是否在每个分值上显示 tooltip
   * 当类型为数组时，可以显示每个元素对应的内容
   */
  tooltip?: boolean | any[]
  /**
   * 键盘表单顺序值
   */
  tabindex?: number
}

export declare class FbRateData {
  prefix: string
  // 最终值
  activeValue: number | string
  hoverValue: number | string
  myData: any[]
  // 数据来源模式 maxLength | data
  // 如果有 maxLength，那区间就是[1-maxLength]，
  // 如果有 data，那区间便是[1-data.length]
  dataMode: string
  hoverStar: any
  hoverIndex: number
  isFocused: boolean
}

export declare class FbRateComputed {
  currentStar: any
  activeIndex: number
}

export declare class FbRateMethods {
  //
  toInteger(val: string | number): number
  mixStar(index: number, defaultStar?: object, defaultProps?: object): object
  genMyData(): any[]
  getStarByValue(value: number | string): any
  getStarByIndex(index: number): any
  getColor(star: any): string
  clearValue(): void
  changeValue(star: any): void
  handleMouseover(star: any): void
  handleMouseout(star: any): void
  /**
   * 点击事件
   * @param star
   */
  handleClick(star: any): void
  /**
   * 键盘事件
   * @param e
   * @returns {boolean}
   */
  handleKeydown(e: KeyboardEvent): boolean
  /**
   * 清空按钮点击
   */
  handleClearClick(): void
}

declare type FbRateSlots = {
  prepend: void
  star: void
  append: void
  desc: void
}

declare class FbRate extends Vue {
  // Props
  /**
   * 当前值
   */
  value?: number | string
  /**
   * 是否显示半数
   */
  half?: boolean
  /**
   * 尺寸
   * 支持单一数值及数组
   */
  size?: string | number | any[]
  /**
   * 最大数量
   */
  maxLength?: string | number
  /**
   * 激活项颜色
   */
  activeColor?: string | any[]
  /**
   * 鼠标移动时高亮颜色
   */
  hoverColor?: string | any[]
  /**
   * 不活动项的颜色
   */
  inActiveColor?: string | any[]
  /**
   * 图标，支持单一图标或图标数组
   */
  icon?: string | any[]
  /**
   * 分值描述数组
   */
  labels?: any[]
  /**
   * 分值数组
   */
  values?: any[]
  /**
   * 节点数据, 暂不支持
   */
  data?: any[]
  /**
   * 是否只读
   */
  readonly?: boolean
  /**
   * 可清除
   * 在当前激活分值上再次点击，可以取消分值
   */
  clearable?: boolean
  /**
   * 前缀内容
   */
  prepend?: string
  /**
   * 后缀内容
   */
  append?: string
  /**
   * 是否显示评分值描述
   */
  desc?: boolean
  /**
   * 是否在每个分值上显示 tooltip
   * 当类型为数组时，可以显示每个元素对应的内容
   */
  tooltip?: boolean | any[]
  /**
   * 键盘表单顺序值
   */
  tabindex?: number

  // Data
  prefix: string
  // 最终值
  activeValue: number | string
  hoverValue: number | string
  myData: any[]
  // 数据来源模式 maxLength | data
  // 如果有 maxLength，那区间就是[1-maxLength]，
  // 如果有 data，那区间便是[1-data.length]
  dataMode: string
  hoverStar: any
  hoverIndex: number
  isFocused: boolean

  // Computed
  currentStar: any
  activeIndex: number

  // Methods
  //
  toInteger(val: string | number): number
  mixStar(index: number, defaultStar?: object, defaultProps?: object): object
  genMyData(): any[]
  getStarByValue(value: number | string): any
  getStarByIndex(index: number): any
  getColor(star: any): string
  clearValue(): void
  changeValue(star: any): void
  handleMouseover(star: any): void
  handleMouseout(star: any): void
  /**
   * 点击事件
   * @param star
   */
  handleClick(star: any): void
  /**
   * 键盘事件
   * @param e
   * @returns {boolean}
   */
  handleKeydown(e: KeyboardEvent): boolean
  /**
   * 清空按钮点击
   */
  handleClearClick(): void

  // Events
  $emit(event: 'on-change', value: number | string): void
  $emit(event: 'input', value: number | string): void
  $emit(event: 'on-click', star: any): void

  // Slots
  $slots: FbRateSlots
}

export { FbRate }
export default FbRate