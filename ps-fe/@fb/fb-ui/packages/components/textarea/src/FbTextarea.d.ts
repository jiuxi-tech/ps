import { DefineComponent } from 'vue';

interface FbTextareaProps {
  /**
   * 绑定值
   */
  value?: number | string;
  
  /**
   * 类型
   */
  type?: string;
  
  /**
   * 尺寸
   */
  size?: string;
  
  /**
   * 占位符
   */
  placeholder?: string;
  
  /**
   * 是否禁用
   */
  disabled?: boolean;
  
  /**
   * 是否只读
   */
  readonly?: boolean;
  
  /**
   * 是否可清除
   */
  clearable?: boolean;
  
  /**
   * 最大长度
   */
  maxlength?: number | string;
  
  /**
   * 图标
   */
  icon?: string;
  
  /**
   * 行数
   */
  rows?: number | string;
  
  /**
   * 宽度
   */
  width?: string | number;
  
  /**
   * 名称
   */
  name?: string;
  
  /**
   * 自动完成
   */
  autocomplete?: string;
  
  /**
   * 是否圆角
   */
  round?: boolean;
  
  /**
   * 前置文本
   */
  prepend?: string;
  
  /**
   * 后置文本
   */
  append?: string;
}

interface FbTextareaSlots {
  /**
   * 前置插槽
   */
  prepend?: () => any;
  
  /**
   * 前置按钮插槽
   */
  'prepend-button'?: () => any;
  
  /**
   * 默认插槽
   */
  default?: () => any;
  
  /**
   * 后置插槽
   */
  append?: () => any;
  
  /**
   * 后置按钮插槽
   */
  'append-button'?: () => any;
}

type FbTextareaComponent = DefineComponent<FbTextareaProps, {}, {}, {}, {}, {}, {}, {}, 'transition', FbTextareaSlots>;

declare const FbTextarea: FbTextareaComponent;

export default FbTextarea;