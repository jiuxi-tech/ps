import { DefineComponent } from 'vue';

interface FbTagProps {
  /**
   * 值
   */
  value?: string | number;
  
  /**
   * 标签文本
   */
  label?: string;
  
  /**
   * 是否圆角
   */
  round?: boolean;
  
  /**
   * 是否可关闭
   */
  closable?: boolean;
  
  /**
   * 类型
   */
  type?: string;
  
  /**
   * 是否禁用过渡动画
   */
  disableTransitions?: boolean;
  
  /**
   * 颜色
   */
  color?: string;
  
  /**
   * 背景色
   */
  backgroundColor?: string;
  
  /**
   * 边框颜色
   */
  borderColor?: string;
  
  /**
   * 标签文字颜色
   */
  labelColor?: string;
  
  /**
   * 效果
   */
  effect?: string;
  
  /**
   * 是否浅色效果
   */
  light?: boolean;
  
  /**
   * 是否朴素效果
   */
  plain?: boolean;
  
  /**
   * 是否深色效果
   */
  dark?: boolean;
  
  /**
   * 是否实心效果
   */
  solid?: boolean;
  
  /**
   * 尺寸
   */
  size?: string;
  
  /**
   * 图标
   */
  icon?: string;
}

interface FbTagSlots {
  /**
   * 默认插槽
   */
  default?: () => any;
}

type FbTagComponent = DefineComponent<FbTagProps, {}, {}, {}, {}, {}, {}, {}, 'transition', FbTagSlots>;

declare const FbTag: FbTagComponent;

export default FbTag;