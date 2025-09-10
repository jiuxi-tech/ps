import { DefineComponent } from 'vue';

interface FbSwitchAccordionProps {
  /**
   * 图标
   */
  icon?: string;
  
  /**
   * 标题
   */
  title?: string;
  
  /**
   * 绑定值
   */
  value?: string | boolean | number;
  
  /**
   * 主体样式
   */
  bodyStyle?: string | any[] | object;
  
  /**
   * 是否禁用
   */
  disabled?: boolean;
  
  /**
   * 是否只读
   */
  readonly?: boolean;
}

interface FbSwitchAccordionSlots {
  /**
   * 标题插槽
   */
  title?: () => any;
  
  /**
   * 默认插槽
   */
  default?: () => any;
}

type FbSwitchAccordionComponent = DefineComponent<FbSwitchAccordionProps, {}, {}, {}, {}, {}, {}, {}, 'transition', FbSwitchAccordionSlots>;

declare const FbSwitchAccordion: FbSwitchAccordionComponent;

export default FbSwitchAccordion;