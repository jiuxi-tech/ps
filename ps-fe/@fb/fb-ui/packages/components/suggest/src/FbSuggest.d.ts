import { DefineComponent } from 'vue';

interface FbSuggestProps {
  /**
   * 尺寸
   */
  size?: 's' | 'm' | 'l';
  
  /**
   * 图标
   */
  icon?: string;
  
  /**
   * 绑定值
   */
  value?: string;
  
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
   * 是否显示清除按钮
   */
  clearable?: boolean;
  
  /**
   * 数据
   */
  data?: any[];
  
  /**
   * 服务
   */
  service?: object | any[] | Function;
  
  /**
   * 查询参数
   */
  param?: object | any[] | Function;
  
  /**
   * 是否总是查询
   */
  always?: boolean;
  
  /**
   * 关键字字段名
   */
  keywordName?: string;
  
  /**
   * 数据过滤器
   */
  dataFilter?: Function;
  
  /**
   * 加载中文本
   */
  loadingText?: string;
  
  /**
   * 空数据文本
   */
  emptyText?: string;
  
  /**
   * 过滤字段
   */
  filterBy?: string;
  
  /**
   * 自定义过滤器
   */
  filter?: Function;
  
  /**
   * 弹出层类名
   */
  popperClass?: string;
}

interface FbSuggestSlots {
  /**
   * 选项插槽
   */
  option?: (props: { option: any; index: number }) => any;
}

type FbSuggestComponent = DefineComponent<FbSuggestProps, {}, {}, {}, {}, {}, {}, {}, 'transition', FbSuggestSlots>;

declare const FbSuggest: FbSuggestComponent;

export default FbSuggest;