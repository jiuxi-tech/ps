import { VNode, Component } from 'vue';

export interface FbSignButtonProps {
  // 暂无特定属性
}

export interface FbSignButtonData {
  // 暂无数据属性
}

export interface FbSignButtonComputed {
  // 暂无计算属性
}

export interface FbSignButtonMethods {
  // 暂无方法
}

export interface FbSignButtonSlots {
  /**
   * 默认插槽
   */
  default: VNode[];
}

export declare class FbSignButton extends Component {
  $props: FbSignButtonProps;
  $data: FbSignButtonData;
  $computed: FbSignButtonComputed;
  $methods: FbSignButtonMethods;
  $slots: FbSignButtonSlots;
}

export default FbSignButton;