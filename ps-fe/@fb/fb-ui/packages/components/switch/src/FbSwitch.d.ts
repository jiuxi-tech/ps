import { DefineComponent } from 'vue';

interface FbSwitchProps {
  /**
   * 绑定值
   */
  value?: boolean | string | number;
  
  /**
   * 尺寸
   */
  size?: string;
  
  /**
   * 是否禁用
   */
  disabled?: boolean;
  
  /**
   * 是否只读
   */
  readonly?: boolean;
  
  /**
   * 确认函数
   */
  confirm?: Function;
  
  /**
   * 读取器
   */
  reader?: any[];
  
  /**
   * 真值
   */
  trueValue?: boolean | string | number;
  
  /**
   * 假值
   */
  falseValue?: boolean | string | number;
}

interface FbSwitchSlots {
  /**
   * 开启状态插槽
   */
  open?: () => any;
  
  /**
   * 关闭状态插槽
   */
  close?: () => any;
}

type FbSwitchComponent = DefineComponent<FbSwitchProps, {}, {}, {}, {}, {}, {}, {}, 'transition', FbSwitchSlots>;

declare const FbSwitch: FbSwitchComponent;

export default FbSwitch;