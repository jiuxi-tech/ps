import { DefineComponent } from 'vue';

interface FbStateProps {
  /**
   * 尺寸
   */
  size?: string | number;
  
  /**
   * 类型
   */
  type?: string;
  
  /**
   * 颜色
   */
  color?: string;
  
  /**
   * 是否激活
   */
  active?: boolean;
  
  /**
   * 右边距
   */
  mr?: string | number;
  
  /**
   * 左边距
   */
  ml?: string | number;
  
  /**
   * 上边距
   */
  mt?: string | number;
  
  /**
   * 下边距
   */
  mb?: string | number;
}

interface FbStateSlots {
  /**
   * 默认插槽
   */
  default?: () => any;
}

type FbStateComponent = DefineComponent<FbStateProps, {}, {}, {}, {}, {}, {}, {}, 'transition', FbStateSlots>;

declare const FbState: FbStateComponent;

export default FbState;