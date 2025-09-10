import { VNode, Component } from 'vue';

export interface FbSliderButtonProps {
  /**
   * 绑定值
   */
  value?: number;

  /**
   * 是否竖向模式
   */
  vertical?: boolean;

  /**
   * 竖向模式起始位置
   */
  verticalStart?: string;

  /**
   * tooltip 类名
   */
  tooltipClass?: string;

  /**
   * 按钮标签方向
   */
  buttonLabelDirection?: string;
}

export interface FbSliderButtonData {
  hovering: boolean;
  dragging: boolean;
  isClick: boolean;
  startX: number;
  currentX: number;
  startY: number;
  currentY: number;
  startPosition: number;
  newPosition: number | null;
  oldValue: number;
}

export interface FbSliderButtonComputed {
  /**
   * 是否禁用
   */
  disabled: boolean;

  /**
   * 最大值
   */
  max: number;

  /**
   * 最小值
   */
  min: number;

  /**
   * 步长
   */
  step: number;

  /**
   * 是否显示 tooltip
   */
  showTooltip: boolean;

  /**
   * 精度
   */
  precision: number;

  /**
   * 当前位置
   */
  currentPosition: string;

  /**
   * 是否启用格式化
   */
  enableFormat: boolean;

  /**
   * 格式化值
   */
  formatValue: string | number;

  /**
   * 包装器样式
   */
  wrapperStyle: object;
}

export interface FbSliderButtonMethods {
  /**
   * 显示 tooltip
   */
  displayTooltip(): void;

  /**
   * 隐藏 tooltip
   */
  hideTooltip(): void;

  /**
   * 鼠标进入处理
   */
  handleMouseEnter(): void;

  /**
   * 鼠标离开处理
   */
  handleMouseLeave(): void;

  /**
   * 按钮按下处理
   */
  onButtonDown(event: Event): void;

  /**
   * 左方向键按下处理
   */
  onLeftKeyDown(): void;

  /**
   * 右方向键按下处理
   */
  onRightKeyDown(): void;

  /**
   * 拖拽开始处理
   */
  onDragStart(event: Event): void;

  /**
   * 拖拽中处理
   */
  onDragging(event: Event): void;

  /**
   * 拖拽结束处理
   */
  onDragEnd(): void;

  /**
   * 设置位置
   */
  setPosition(newPosition: number): void;
}

export interface FbSliderButtonSlots {
  /**
   * 标签插槽
   */
  label: VNode[];
}

export declare class FbSliderButton extends Component {
  $props: FbSliderButtonProps;
  $data: FbSliderButtonData;
  $computed: FbSliderButtonComputed;
  $methods: FbSliderButtonMethods;
  $slots: FbSliderButtonSlots;
}

export default FbSliderButton;