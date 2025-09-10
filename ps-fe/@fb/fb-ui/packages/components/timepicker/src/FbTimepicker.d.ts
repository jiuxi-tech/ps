import { DefineComponent } from 'vue';

interface FbTimepickerProps {
  /**
   * 绑定值
   */
  value?: string;
  
  /**
   * 占位符
   */
  placeholder?: string;
  
  /**
   * 图标
   */
  icon?: string;
  
  /**
   * 是否禁用
   */
  disabled?: boolean;
  
  /**
   * 是否可清除
   */
  clearable?: boolean;
  
  /**
   * 是否只读
   */
  readonly?: boolean;
  
  /**
   * 显示格式
   */
  format?: string;
  
  /**
   * 值格式
   */
  valueFormat?: string;
  
  /**
   * 小时间隔
   */
  hourStep?: number;
  
  /**
   * 分钟间隔
   */
  minuteStep?: number;
  
  /**
   * 秒间隔
   */
  secondStep?: number;
  
  /**
   * 禁用小时
   */
  disabledHours?: Function;
  
  /**
   * 禁用分钟
   */
  disabledMinutes?: Function;
  
  /**
   * 禁用秒
   */
  disabledSeconds?: Function;
}

interface FbTimepickerSlots {
  /**
   * 附加插槽
   */
  addon?: () => any;
}

type FbTimepickerComponent = DefineComponent<FbTimepickerProps, {}, {}, {}, {}, {}, {}, {}, 'transition', FbTimepickerSlots>;

declare const FbTimepicker: FbTimepickerComponent;

export default FbTimepicker;