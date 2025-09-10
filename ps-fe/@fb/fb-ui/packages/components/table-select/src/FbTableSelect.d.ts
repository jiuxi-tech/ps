import { DefineComponent } from 'vue';

interface FbTableSelectProps {
  /**
   * 绑定值
   */
  value?: any[];
  
  /**
   * 是否显示边框
   */
  bordered?: boolean;
  
  /**
   * 是否禁用
   */
  disabled?: boolean;
  
  /**
   * 是否只读
   */
  readonly?: boolean;
  
  /**
   * 位置
   */
  position?: string;
  
  /**
   * 下拉框最大高度
   */
  maxHeight?: string | number;
  
  /**
   * 表格宽度
   */
  tableWidth?: string | number;
  
  /**
   * 是否显示确认按钮
   */
  showConfirm?: boolean;
  
  /**
   * 是否可清除
   */
  clearable?: boolean;
  
  /**
   * 占位符
   */
  placeholder?: string;
  
  /**
   * 是否显示图标
   */
  showIcon?: boolean;
  
  /**
   * 是否显示标题
   */
  showTitle?: boolean;
  
  /**
   * 表头格式化函数
   */
  headerFormat?: Function;
}

interface FbTableSelectSlots {
  /**
   * 默认插槽
   */
  default?: () => any;
}

type FbTableSelectComponent = DefineComponent<FbTableSelectProps, {}, {}, {}, {}, {}, {}, {}, 'transition', FbTableSelectSlots>;

declare const FbTableSelect: FbTableSelectComponent;

export default FbTableSelect;