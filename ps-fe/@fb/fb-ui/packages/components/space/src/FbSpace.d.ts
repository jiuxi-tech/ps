import { VNode, Component } from 'vue';

export type SpaceSize = 's' | 'm' | 'l' | 'xl' | number | string;
export type SpaceDirection = 'horizontal' | 'vertical';
export type SpaceAlign = 'start' | 'end' | 'center' | 'baseline';

export interface FbSpaceProps {
  /**
   * 间隔尺寸
   *
   * if type string 's', 'm', 'l', 'xl' '1px' '2px'
   * if type number 1 2 3 4 => 1px 2px 3px 4px
   */
  size?: SpaceSize;

  /**
   * 方向
   */
  direction?: SpaceDirection;

  /**
   * 对齐方式
   */
  align?: SpaceAlign;
}

export interface FbSpaceData {
  items: Array<any>;
}

export interface FbSpaceComputed {
  // 暂无计算属性
}

export interface FbSpaceMethods {
  /**
   * 获取尺寸
   */
  getSize(size: SpaceSize): string | number;

  /**
   * 渲染函数
   */
  render(h: Function, context: any): VNode | null;
}

export interface FbSpaceSlots {
  /**
   * 默认插槽
   */
  default: VNode[];
}

export declare class FbSpace extends Component {
  $props: FbSpaceProps;
  $data: FbSpaceData;
  $computed: FbSpaceComputed;
  $methods: FbSpaceMethods;
  $slots: FbSpaceSlots;
}

export default FbSpace;