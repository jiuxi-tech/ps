import { DefineComponent } from 'vue';

interface FbTimerProps {
  /**
   * 尺寸
   */
  size?: string;
  
  /**
   * 类型
   */
  type?: string;
  
  /**
   * 颜色
   */
  color?: string;
  
  /**
   * 是否省略
   */
  ellipsis?: boolean;
  
  /**
   * 宽度
   */
  width?: string | number;
  
  /**
   * 是否粗体
   */
  bold?: boolean;
  
  /**
   * 字体粗细
   */
  weight?: string | number;
  
  /**
   * 文本对齐方式
   */
  align?: string;
}

interface FbTimerSlots {
  /**
   * 默认插槽
   */
  default?: () => any;
}

type FbTimerComponent = DefineComponent<FbTimerProps, {}, {}, {}, {}, {}, {}, {}, 'transition', FbTimerSlots>;

declare const FbTimer: FbTimerComponent;

export default FbTimer;