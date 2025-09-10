import { DefineComponent } from 'vue';

interface FbStepProps {
  /**
   * 标签
   */
  label?: string;
  
  /**
   * 描述
   */
  description?: string;
  
  /**
   * 图标
   */
  icon?: string;
  
  /**
   * 宽度
   */
  width?: string;
  
  /**
   * 状态
   */
  status?: 'ready' | 'doing' | 'done' | 'wait' | 'error';
}

interface FbStepSlots {
  /**
   * 默认插槽
   */
  default?: () => any;
  
  /**
   * 标签插槽
   */
  label?: () => any;
  
  /**
   * 描述插槽
   */
  description?: () => any;
}

type FbStepComponent = DefineComponent<FbStepProps, {}, {}, {}, {}, {}, {}, {}, 'transition', FbStepSlots>;

declare const FbStep: FbStepComponent;

export default FbStep;