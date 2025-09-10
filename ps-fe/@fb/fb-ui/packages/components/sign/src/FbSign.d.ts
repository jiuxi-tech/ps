import { VNode, Component } from 'vue';

export interface FbSignProps {
  /**
   * 只读
   */
  readonly?: boolean;

  /**
   * 画布宽度
   */
  width?: number;

  /**
   * 画布高度
   */
  height?: number;

  /**
   * 线宽
   */
  lineWidth?: number | string;

  /**
   * 线颜色
   */
  lineColor?: string;

  /**
   * 背景色
   */
  backgroundColor?: string;

  /**
   * 背景图像
   */
  backgroundImage?: string;

  /**
   * 图像输出质量
   */
  quality?: number;

  /**
   * 显示操作按钮
   */
  showAction?: boolean;
}

export interface FbSignData {
  myValue: string;
  hasSign: boolean;
  points: Array<any>;
  canvasContext: any;
  startX: number;
  startY: number;
  isDrawing: boolean;
  ratio: number;
}

export interface FbSignComputed {
  /**
   * 矩形尺寸
   */
  rect: {
    height: number;
    width: number;
  };
}

export interface FbSignMethods {
  /**
   * 鼠标按下处理
   */
  handleMouseDown(e: Event): void;

  /**
   * 鼠标移动处理
   */
  handleMouseMove(e: Event): void;

  /**
   * 鼠标松开处理
   */
  handleMouseUp(e: Event): void;

  /**
   * 确认按钮点击处理
   */
  handleConfirmClick(): void;

  /**
   * 重置按钮点击处理
   */
  handleResetClick(): void;

  /**
   * 开始绘制
   */
  drawStart(obj: { x: number; y: number }): void;

  /**
   * 绘制移动
   */
  drawMove(obj: { x: number; y: number }): void;

  /**
   * 结束绘制
   */
  drawEnd(obj: { x: number; y: number }): void;

  /**
   * 获取Base64图像
   */
  getBase64(options?: { quality: number }): Promise<string>;

  /**
   * 清空签名
   */
  clear(): void;

  /**
   * 重置签名
   */
  reset(): void;
}

export interface FbSignSlots {
  /**
   * 头部插槽
   */
  header: VNode[];
}

export declare class FbSign extends Component {
  $props: FbSignProps;
  $data: FbSignData;
  $computed: FbSignComputed;
  $methods: FbSignMethods;
  $slots: FbSignSlots;
}

export default FbSign;