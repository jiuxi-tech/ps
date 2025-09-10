import { VNode, Component } from 'vue';

export interface FbSelectOptionProps {
  /**
   * 父级ID
   */
  parentId?: string;

  /**
   * 父级组件
   */
  parent?: any;

  /**
   * 值绑定
   */
  value?: string | number | boolean;

  /**
   * 显示文字
   */
  label?: string | number | boolean;

  /**
   * 图标
   */
  icon?: string | number | boolean;

  /**
   * 禁用
   */
  disabled?: boolean;

  /**
   * 格式化函数
   */
  format?: Function;
}

export interface FbSelectOptionData {
  currentLabel: string | number | boolean;
  isFocus: boolean;
  myFormat: Function;
}

export interface FbSelectOptionComputed {
  /**
   * 获取类名
   */
  getClass: Array<string>;

  /**
   * 是否选中
   */
  selected: boolean;

  /**
   * 是否显示
   */
  show: boolean;
}

export interface FbSelectOptionMethods {
  /**
   * 处理鼠标进入事件
   */
  handleMouseenter(e: Event): void;

  /**
   * 处理焦点事件
   */
  handleFocus(): void;
}

export interface FbSelectOptionSlots {
  /**
   * 默认插槽
   */
  default: VNode[];
}

export declare class FbSelectOption extends Component {
  $props: FbSelectOptionProps;
  $data: FbSelectOptionData;
  $computed: FbSelectOptionComputed;
  $methods: FbSelectOptionMethods;
  $slots: FbSelectOptionSlots;
}

export default FbSelectOption;