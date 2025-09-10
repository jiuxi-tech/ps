import { DefineComponent } from 'vue';

interface FbTimeSelectProps {
  /**
   * 绑定值
   */
  value?: string;
  
  /**
   * 显示格式
   */
  format?: string;
  
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

type FbTimeSelectComponent = DefineComponent<FbTimeSelectProps, {}, {}, {}, {}, {}, {}, {}, 'transition'>;

declare const FbTimeSelect: FbTimeSelectComponent;

export default FbTimeSelect;