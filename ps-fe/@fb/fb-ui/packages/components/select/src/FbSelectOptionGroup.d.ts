import { VNode, Component } from 'vue';

export interface FbSelectOptionGroupProps {
  /**
   * 禁用
   */
  disabled?: boolean;

  /**
   * 只读
   */
  readonly?: boolean;
}

export interface FbSelectOptionGroupData {
  // 暂无数据属性
}

export interface FbSelectOptionGroupComputed {
  // 暂无计算属性
}

export interface FbSelectOptionGroupMethods {
  // 暂无方法
}

export interface FbSelectOptionGroupSlots {
  /**
   * 默认插槽
   */
  default: VNode[];
}

export declare class FbSelectOptionGroup extends Component {
  $props: FbSelectOptionGroupProps;
  $data: FbSelectOptionGroupData;
  $computed: FbSelectOptionGroupComputed;
  $methods: FbSelectOptionGroupMethods;
  $slots: FbSelectOptionGroupSlots;
}

export default FbSelectOptionGroup;