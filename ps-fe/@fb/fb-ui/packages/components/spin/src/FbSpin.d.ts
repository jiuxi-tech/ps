import { DefineComponent } from 'vue';

interface FbSpinProps {
  /**
   * 颜色
   */
  color?: string;
  
  /**
   * 文字颜色
   */
  textColor?: string;
  
  /**
   * 文字大小
   */
  size?: string;
  
  /**
   * 圈的宽度
   */
  borderWidth?: string;
  
  /**
   * 显示文字
   */
  text?: string | number;
  
  /**
   * 锁定
   */
  lock?: boolean;
  
  /**
   * 是否显示
   */
  show?: boolean;
  
  /**
   * 旋转器 icon
   */
  spinner?: string;
  
  /**
   * 过渡动画名称
   */
  transition?: string;
}

interface FbSpinSlots {
  /**
   * 默认插槽
   */
  default?: () => any;
}

type FbSpinComponent = DefineComponent<FbSpinProps, {}, {}, {}, {}, {}, {}, {}, 'transition', FbSpinSlots>;

declare const FbSpin: FbSpinComponent;

export default FbSpin;