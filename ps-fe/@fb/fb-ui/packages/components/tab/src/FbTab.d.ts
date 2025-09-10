import { DefineComponent } from 'vue';

interface FbTabProps {
  /**
   * 标签页名称
   */
  name?: string | number;
  
  /**
   * 标签页标签
   */
  label?: string | object;
  
  /**
   * 图标
   */
  icon?: string;
  
  /**
   * 是否禁用
   */
  disabled?: boolean;
  
  /**
   * 是否隐藏
   */
  hidden?: boolean;
  
  /**
   * 是否不可关闭
   */
  noClose?: boolean;
  
  /**
   * 是否无内边距
   */
  noPadding?: boolean;
  
  /**
   * 过渡动画
   */
  transition?: string;
}

interface FbTabSlots {
  /**
   * 默认插槽
   */
  default?: () => any;
  
  /**
   * 标签插槽
   */
  label?: () => any;
}

type FbTabComponent = DefineComponent<FbTabProps, {}, {}, {}, {}, {}, {}, {}, 'transition', FbTabSlots>;

declare const FbTab: FbTabComponent;

export default FbTab;