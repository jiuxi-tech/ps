import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbCalendar 组件的 Props 类型
export interface FbCalendarProps {
  /** 绑定值 */
  value?: Date | Date[]
  
  /** 模式：单选single、多选multiple、区间range */
  mode?: 'single' | 'range' | 'multiple'
  
  /** 格式化模式 */
  format?: string
  
  /** 显示位置 */
  position?: 'left' | 'right'
  
  /** 禁用日期函数 */
  disabledDate?: Function
  
  /** 是否显示左箭头 */
  showLeftArrow?: boolean
  
  /** 是否显示右箭头 */
  showRightArrow?: boolean
  
  /** 是否显示时间选择 */
  showTime?: boolean
  
  /** 特殊日期 */
  specialDays?: any[]
}

// 定义 FbCalendar 组件的 Data 属性类型
export interface FbCalendarData {
  /** 组件前缀 */
  prefix: string
  
  /** 星期名称 */
  weekName: string[]
  
  /** 月份名称 */
  months: string[]
  
  /** 模式标识 */
  pattern: number
  
  /** 是否显示年份选择 */
  showYears: boolean
  
  /** 是否显示月份选择 */
  showMonths: boolean
  
  /** 是否显示小时选择 */
  showHours: boolean
  
  /** 是否显示分钟选择 */
  showMinutes: boolean
  
  /** 是否显示秒选择 */
  showSeconds: boolean
  
  /** 时间 */
  times: string
  
  /** 当前日期 */
  current: object
}

// 定义 FbCalendar 组件的 Computed 属性类型
export interface FbCalendarComputed {
  /** 格式化后的日期 */
  vFormat: string
  
  /** 一周的第一天 */
  weekFirstDay: number
  
  /** 星期数组 */
  WEEKS: string[]
  
  /** 年份开始 */
  yearStart: number
  
  /** 年份结束 */
  yearEnd: number
  
  /** 年份数组 */
  years: number[]
  
  /** 日期数组 */
  days: object[]
  
  /** 是否有月份 */
  hasMonth: boolean
  
  /** 是否有日期 */
  hasDay: boolean
  
  /** 是否有小时 */
  hasHour: boolean
  
  /** 是否有分钟 */
  hasMinute: boolean
  
  /** 是否有秒 */
  hasSecond: boolean
  
  /** 时间格式 */
  tFormat: string
  
  /** 是否显示时间 */
  vShowTime: boolean
  
  /** 选中的时间 */
  getSelectedTime: Date[]
  
  /** 时间标题 */
  timeTitle: string
}

// 定义 FbCalendar 组件的 Slots 类型
export interface FbCalendarSlots {
  /** 日期插槽 */
  day: VNode[]
  
  /** 特殊日期插槽 */
  'special-day': VNode[]
}

// 定义 FbCalendar 组件实例类型
export interface FbCalendar extends Vue, FbCalendarProps, FbCalendarData, FbCalendarComputed {
  $slots: FbCalendarSlots
  
  /** 初始化模式 */
  initPattern(): void
  
  /** 格式化时间 */
  timeFormat(date: Date, format?: string): string
  
  /** 解析日期 */
  parseDate(date: Date): object
  
  /** 默认日期 */
  defaultDate(): Date
  
  /** 初始化时间 */
  initTime(value: Date | Date[]): void
  
  /** 补零 */
  padStartZero(target: any, len?: number): string
  
  /** 获取时间 */
  getTime(date: Date): string
  
  /** 获取范围日期 */
  getRangeDate(date: Date[], activeDate: Date): Date
  
  /** 获取当前日期 */
  getCurrentDate(date: Date | Date[]): object
  
  /** 获取日期信息 */
  get(date: Date | Date[], isTime?: boolean): object
  
  /** 是否选中 */
  isSelected(time: Date, format: string): boolean
  
  /** 是否选中年份 */
  isSelectedYear(time: Date, format: string): boolean
  
  /** 是否选中月份 */
  isSelectedMonth(time: Date, format: string): boolean
  
  /** 是否在范围内 */
  inRangeDate(time: Date, format: string): boolean
  
  /** 是否为特殊日期 */
  isSpecialDay(item: object): boolean
  
  /** 年份类名 */
  yearCls(year: number): object
  
  /** 月份类名 */
  monthCls(month: number): object
  
  /** 日期类名 */
  dayCls(item: object): object
  
  /** 获取时间日期 */
  getTimesDate(): object
  
  /** 禁用小时 */
  disabledHours(): number[]
  
  /** 禁用分钟 */
  disabledMinutes(): number[]
  
  /** 禁用秒 */
  disabledSeconds(): number[]
  
  /** 下一个月 */
  monthToNext(): void
  
  /** 上一个月 */
  monthToPre(): void
  
  /** 判断是否可点击 */
  is(e: Event): boolean
  
  /** 选择年份 */
  selectedYear(year: number): void
  
  /** 选择月份 */
  selectedMonth(month: number): void
  
  /** 时间转数字 */
  timesToNum(): object
  
  /** 选择日期 */
  selectedDay(info: object): void
  
  /** 改变时间 */
  changeTimes(): void
  
  /** 值改变事件 */
  onChange(date: Date, isTime?: boolean, isOut?: boolean): void
}

// 定义 FbCalendar 组件构造函数类型
export interface FbCalendarConstructor extends VueConstructor<FbCalendar> {}

// 导出 FbCalendar 组件类型
export const FbCalendar: FbCalendarConstructor

// 默认导出
export default FbCalendar