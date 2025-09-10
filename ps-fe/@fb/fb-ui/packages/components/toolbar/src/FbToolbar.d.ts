import { DefineComponent } from 'vue';

interface FbToolbarProps {
  // Toolbar组件没有特定的props
}

interface FbToolbarSlots {
  /**
   * 状态插槽
   */
  status?: () => any;
  
  /**
   * 按钮插槽
   */
  buttons?: () => any;
  
  /**
   * 操作插槽
   */
  actions?: () => any;
  
  /**
   * 默认插槽
   */
  default?: () => any;
}

type FbToolbarComponent = DefineComponent<FbToolbarProps, {}, {}, {}, {}, {}, {}, {}, 'transition', FbToolbarSlots>;

declare const FbToolbar: FbToolbarComponent;

export default FbToolbar;