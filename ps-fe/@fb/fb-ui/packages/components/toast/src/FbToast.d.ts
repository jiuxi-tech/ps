import { DefineComponent } from 'vue';

interface FbToastProps {
  /**
   * 消息内容
   */
  message?: string | number;
  
  /**
   * 类型
   */
  type?: string;
}

interface FbToastSlots {
  /**
   * 默认插槽
   */
  default?: () => any;
}

type FbToastComponent = DefineComponent<FbToastProps, {}, {}, {}, {}, {}, {}, {}, 'transition', FbToastSlots>;

declare const FbToast: FbToastComponent;

export default FbToast;