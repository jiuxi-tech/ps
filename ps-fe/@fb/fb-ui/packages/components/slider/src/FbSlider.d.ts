import { VNode, Component } from 'vue';
import { FbSliderButton } from './FbSliderButton.d';

export interface FbSliderProps {
  /**
   * 最小值
   */
  min?: number;

  /**
   * 最大值
   */
  max?: number;

  /**
   * 步长
   */
  step?: number;

  /**
   * 绑定值
   */
  value?: number | Array<number>;

  /**
   * 是否显示输入框
   */
  showInput?: boolean;

  /**
   * 是否显示输入框的控制按钮
   */
  showInputControls?: boolean;

  /**
   * 输入框的尺寸
   */
  inputSize?: string;

  /**
   * 是否显示间断点
   */
  showStops?: boolean;

  /**
   * 是否显示 tooltip
   */
  showTooltip?: boolean;

  /**
   * 格式化 tooltip
   */
  formatTooltip?: Function;

  /**
   * 是否禁用
   */
  disabled?: boolean;

  /**
   * 是否为范围选择
   */
  range?: boolean;

  /**
   * 是否竖向模式
   */
  vertical?: boolean;

  /**
   * 竖向模式起始位置
   */
  verticalStart?: string;

  /**
   * 滑块高度
   */
  height?: string;

  /**
   * 防抖延迟
   */
  debounce?: number;

  /**
   * 标签
   */
  label?: string;

  /**
   * tooltip 类名
   */
  tooltipClass?: string;

  /**
   * 标记
   */
  marks?: object;

  /**
   * 按钮标签方向
   */
  buttonLabelDirection?: string;
}

export interface FbSliderData {
  firstValue: number | null;
  secondValue: number | null;
  oldValue: number | Array<number> | null;
  dragging: boolean;
  sliderSize: number;
}

export interface FbSliderComputed {
  /**
   * 间断点
   */
  stops: Array<number>;

  /**
   * 标记列表
   */
  markList: Array<{
    point: number;
    position: number;
    mark: string | object;
  }>;

  /**
   * 最小值
   */
  minValue: number;

  /**
   * 最大值
   */
  maxValue: number;

  /**
   * 进度条尺寸
   */
  barSize: string;

  /**
   * 进度条起始位置
   */
  barStart: string;

  /**
   * 精度
   */
  precision: number;

  /**
   * 跑道样式
   */
  runwayStyle: object;

  /**
   * 进度条样式
   */
  barStyle: object;

  /**
   * 滑块是否禁用
   */
  sliderDisabled: boolean;
}

export interface FbSliderMethods {
  /**
   * 值是否改变
   */
  valueChanged(): boolean;

  /**
   * 设置值
   */
  setValues(): void;

  /**
   * 设置位置
   */
  setPosition(percent: number): void;

  /**
   * 滑块点击处理
   */
  onSliderClick(event: Event): void;

  /**
   * 滑块移动处理
   */
  onSliderMove(event: Event): void;

  /**
   * 重置尺寸
   */
  resetSize(): void;

  /**
   * 触发改变事件
   */
  emitChange(): void;

  /**
   * 触发改变结束事件
   */
  emitChangeEnd(): void;

  /**
   * 获取间断点样式
   */
  getStopStyle(position: number): object;
}

export interface FbSliderSlots {
  /**
   * 第一个按钮标签插槽
   */
  'first-button-label': VNode[];

  /**
   * 结束按钮标签插槽
   */
  'end-button-label': VNode[];
}

export declare class FbSlider extends Component {
  $props: FbSliderProps;
  $data: FbSliderData;
  $computed: FbSliderComputed;
  $methods: FbSliderMethods;
  $slots: FbSliderSlots;
}

export default FbSlider;