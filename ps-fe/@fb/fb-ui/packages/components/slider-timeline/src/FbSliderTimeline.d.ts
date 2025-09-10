import { VNode, Component } from 'vue';

export interface FbSliderTimelineProps {
  /**
   * 绑定值
   */
  value?: number;

  /**
   * 布局模式 top/center/bottom/center-triangle
   */
  mode?: string;

  /**
   * 标记事件区间
   */
  timeParts?: Array<any>;

  /**
   * 标记事件区间 阻止移动
   */
  timePartPrevent?: boolean;

  /**
   * 自动移动
   */
  autoMove?: boolean;

  /**
   * 自动移动部署 毫秒时
   */
  autoMoveStep?: number;

  /**
   * 当前时间
   */
  nowTime?: number;

  /**
   * 刻度长度
   */
  markHeight?: number;

  /**
   * 小刻度长度
   */
  minMarkHeight?: number;

  /**
   * 大刻度长度
   */
  maxMarkHeight?: number;

  /**
   * 游标颜色
   */
  drawCursorColor?: string;

  /**
   * 游标文字颜色
   */
  drawCursorTextColor?: string;

  /**
   * 游标显示文字 --- 强制修改
   */
  drawCursorText?: string;

  /**
   * 游标显示文字 --- 时间格式化，遵守dayjs规则
   */
  drawCursorFormat?: string;

  /**
   * 游标字体
   */
  drawCursorFont?: string;

  /**
   * 游标线宽
   */
  drawCursorWidth?: number;

  /**
   * 游标绘制距离画布上部 y 偏移量
   */
  drawCursorOffsetY?: number;

  /**
   * 游标绘制中线 x 偏移量
   */
  drawCursorOffsetX?: number;

  /**
   * 游标中线 三角 宽度
   */
  drawCursorTriangleWidth?: number;

  /**
   * 游标中线 三角 高度
   */
  drawCursorTriangleHeight?: number;

  /**
   * 游标中线 三角 颜色
   */
  drawCursorTriangleColor?: string;

  /**
   * 刻度处背景颜色
   */
  fillScaleBgColor?: string;

  /**
   * 刻度处背景高度
   */
  fillScaleHeight?: number;

  /**
   * 刻度格式化
   */
  scaleTextFormat?: string | Function;

  /**
   * 刻度颜色
   */
  drawScaleColor?: string;

  /**
   * 刻度字体
   */
  drawScaleFont?: string;

  /**
   * 刻度 绘制距离长分割线 x 偏移量
   */
  drawScaleOffsetX?: number;

  /**
   * 刻度 绘制距离长分割线 y 偏移量
   */
  drawScaleOffsetY?: number;

  /**
   * 鼠标悬浮移动 游标颜色
   */
  hoverCursorColor?: string;

  /**
   * 鼠标悬浮移动显示文字 --- 强制修改
   */
  hoverCursorText?: string;

  /**
   * 鼠标悬浮移动显示文字 --- 时间格式化，遵守dayjs规则
   */
  hoverCursorFormat?: string;

  /**
   * 鼠标悬浮移动 游标字体
   */
  hoverCursorFont?: string;

  /**
   * 鼠标悬浮移动 游标宽度
   */
  hoverCursorWidth?: number;

  /**
   * 鼠标悬浮移动绘制距离画布上部 y 偏移量
   */
  hoverCursorOffsetY?: number;

  /**
   * 鼠标悬浮移动绘制中线 x 偏移量
   */
  hoverCursorOffsetX?: number;

  /**
   * 录像时间块颜色
   */
  fillTimePartsColor?: string;

  /**
   * 录像时间块高度
   */
  fillTimePartHeight?: number;

  /**
   * 最小刻度间距
   */
  minScaleSpacing?: number;

  /**
   * 允许的最小大格长度px值 如果调小 大格会变密集
   */
  minLargeScaleSpacing?: number;

  /**
   * 缩放层级
   */
  zoomLevel?: number;

  /**
   * 缩放步长
   */
  zoomStep?: number;
}

export interface FbSliderTimelineData {
  ratio: number;
  canvasRef: any;
  containerRef: any;
  ctxRef: any;
  minutePerStep: Array<number>;
  currentTime: number;
  isMove: boolean;
  moveTimer: any;
  startTimestamp: number;
  isMouseDownFlag: boolean;
  isDragFlag: boolean;
  mousedownX: number;
  zoom: number;
  totalRuler: number;
  totalRulerMultiple: number;
}

export interface FbSliderTimelineComputed {
  /**
   * 小刻度高度
   */
  smallMarkHeight: number;

  /**
   * 大刻度高度
   */
  bigMarkHeight: number;
}

export interface FbSliderTimelineMethods {
  /**
   * 初始化
   */
  init(): void;

  /**
   * 刷新开始时间戳
   */
  refreshStartTimestamp(): void;

  /**
   * 清空画布
   */
  clearCanvas(): void;

  /**
   * 填充刻度背景
   */
  fillScaleBg(): void;

  /**
   * 创建刻度文字
   */
  createScaleText(time: Date): string;

  /**
   * 绘制刻度
   */
  drawScale(): void;

  /**
   * 绘制刻度线
   */
  drawScaleLine(left: number, height: number): void;

  /**
   * Y轴取消数字
   */
  yAxisCancelNum(height: number): number;

  /**
   * 填充时间块
   */
  fillTimeParts(part: any): void;

  /**
   * 创建填充部分文字
   */
  createFillPartText(time: number, format?: string): string;

  /**
   * 绘制游标
   */
  drawCursor(): void;

  /**
   * 绘制游标三角形
   */
  drawCursorTriangle(x: number, y: number): void;

  /**
   * 设置是否移动
   */
  setIsMove(Move: boolean): void;

  /**
   * 拖拽移动
   */
  dragMove(event: Event): void;

  /**
   * 悬浮移动
   */
  hoverMove(event: Event): void;

  /**
   * 计算比例字体
   */
  calcRadioFont(font: string): string;

  /**
   * 获取鼠标位置时间
   */
  getMousePosTime(event: Event): Date;

  /**
   * 点击事件
   */
  clickEvent(event: Event): void;

  /**
   * 滚轮事件
   */
  wheelEvent(event: Event): void;

  /**
   * 计算总刻度倍数
   */
  calcTotalRulerMultiple(): void;

  /**
   * 获取鼠标X相对位置
   */
  getMouseXRelativePos(event: Event): number;

  /**
   * 滚轮处理
   */
  wheel(event: Event): void;

  /**
   * 鼠标按下处理
   */
  mousedown(event: Event): void;

  /**
   * 鼠标双击处理
   */
  mousedblclick(event: Event): void;

  /**
   * 鼠标移动处理
   */
  mousemove(event: Event): void;

  /**
   * 鼠标松开处理
   */
  mouseup(event: Event): void;

  /**
   * 鼠标离开处理
   */
  mouseleave(event: Event): void;
}

export interface FbSliderTimelineSlots {
  /**
   * 默认插槽
   */
  default: VNode[];
}

export declare class FbSliderTimeline extends Component {
  $props: FbSliderTimelineProps;
  $data: FbSliderTimelineData;
  $computed: FbSliderTimelineComputed;
  $methods: FbSliderTimelineMethods;
  $slots: FbSliderTimelineSlots;
}

export default FbSliderTimeline;