import { Component, Vue } from 'vue-property-decorator'

export declare class FbNumberProps {
  // 开始数
  start?: number | string
  // 结尾数
  end?: number | string
  // 动画时长
  duration?: number
  // 自动播放
  auto?: boolean
  // 小数位数
  decimals?: number
  // 小数点
  decimal?: string
  // 千分位分隔符
  separator?: string
  // 前缀
  prepend?: string
  // 后缀
  append?: string
  useQueue?: boolean
  // 缓动函数
  useEasing?: boolean
  easingFn?: (t: number, b: number, c: number, d: number) => number
  formatter?: (num: number) => string
  // 是否动画
  noPlay?: boolean
}

export declare class FbNumberData {
  myStart: number
  myEnd: number
  myValue: string
  realVal: number | null
  paused: boolean
  myDuration: number
  startTime: number | null
  timestamp: number | null
  remaining: number | null
  queue: Array<{
    start: number
    end: number
    fn: () => void
  }>
  rAF: number | null
  queueTimer: number | null
  debouncePlay: () => void
}

export declare class FbNumberComputed {
  stepDown: boolean
}

export declare class FbNumberMethods {
  play(): void
  doQueue(): void
  pauseResume(): void
  pause(): void
  resume(): void
  reset(): void
  step(timestamp: number): void
  isNumber(val: any): boolean
  formatNumber(num: number): string
  handleClick(): void
}

declare type FbNumberSlots = {
  default: void
}

declare class FbNumber extends Vue {
  // Props
  start: number | string
  end: number | string
  duration: number
  auto: boolean
  decimals: number
  decimal: string
  separator: string
  prepend: string
  append: string
  useQueue: boolean
  useEasing: boolean
  easingFn: (t: number, b: number, c: number, d: number) => number
  formatter: ((num: number) => string) | undefined
  noPlay: boolean

  // Data
  myStart: number
  myEnd: number
  myValue: string
  realVal: number | null
  paused: boolean
  myDuration: number
  startTime: number | null
  timestamp: number | null
  remaining: number | null
  queue: Array<{
    start: number
    end: number
    fn: () => void
  }>
  rAF: number | null
  queueTimer: number | null
  debouncePlay: () => void

  // Computed
  stepDown: boolean

  // Methods
  play(): void
  doQueue(): void
  pauseResume(): void
  pause(): void
  resume(): void
  reset(): void
  step(timestamp: number): void
  isNumber(val: any): boolean
  formatNumber(num: number): string
  handleClick(): void

  // Events
  $emit(event: 'on-mounted'): void
  $emit(event: 'on-step'): void
  $emit(event: 'on-click'): void
}

export { FbNumber }
export default FbNumber