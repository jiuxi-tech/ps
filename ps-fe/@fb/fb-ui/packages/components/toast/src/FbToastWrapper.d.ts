import { DefineComponent } from 'vue';

interface FbToastWrapperProps {
  /**
   * 消息内容
   */
  message?: string | number;
  
  /**
   * 类型
   */
  type?: string;
}

interface FbToastWrapperSlots {
  /**
   * 默认插槽
   */
  default?: () => any;
}

type FbToastWrapperComponent = DefineComponent<FbToastWrapperProps, {}, {}, {}, {}, {}, {}, {}, 'transition', FbToastWrapperSlots>;

declare const FbToastWrapper: FbToastWrapperComponent;

export default FbToastWrapper;