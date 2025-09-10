import { DefineComponent } from 'vue';

interface FbSwitchAccordionGroupProps {
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
}

interface FbSwitchAccordionGroupSlots {
  /**
   * 默认插槽
   */
  default?: () => any;
}

type FbSwitchAccordionGroupComponent = DefineComponent<FbSwitchAccordionGroupProps, {}, {}, {}, {}, {}, {}, {}, 'transition', FbSwitchAccordionGroupSlots>;

declare const FbSwitchAccordionGroup: FbSwitchAccordionGroupComponent;

export default FbSwitchAccordionGroup;