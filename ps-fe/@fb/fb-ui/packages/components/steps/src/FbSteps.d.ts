import { DefineComponent } from 'vue';

interface FbStepsProps {
  /**
   * 当前步骤
   */
  current?: string | number;
  
  /**
   * 绑定值
   */
  value?: string | number;
  
  /**
   * 数据绑定
   */
  data?: any[];
  
  /**
   * 尺寸
   */
  size?: string;
  
  /**
   * 对齐方式
   */
  align?: string;
  
  /**
   * 是否垂直显示
   */
  vertical?: boolean;
}

interface FbStepsSlots {
  /**
   * 默认插槽
   */
  default?: () => any;
}

type FbStepsComponent = DefineComponent<FbStepsProps, {}, {}, {}, {}, {}, {}, {}, 'transition', FbStepsSlots>;

declare const FbSteps: FbStepsComponent;

export default FbSteps;