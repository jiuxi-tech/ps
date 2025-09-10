import { DefineComponent } from 'vue';

interface FbSimpleTableProps {
  /**
   * 主键字段名
   */
  pk?: string;
  
  /**
   * 是否使用冻结表头
   */
  useFreezeHeader?: boolean;
  
  /**
   * 是否固定表格布局
   */
  fixed?: boolean;
  
  /**
   * 是否显示表头
   */
  showHeader?: boolean;
  
  /**
   * 是否不显示表头分隔线
   */
  noHeadSplitter?: boolean;
  
  /**
   * 是否显示长表头分隔线
   */
  longHeadSplitter?: boolean;
  
  /**
   * 是否显示边框
   */
  bordered?: boolean;
  
  /**
   * 表头是否显示边框
   */
  headBordered?: boolean;
  
  /**
   * 是否加载中
   */
  loading?: boolean;
  
  /**
   * 加载中文本
   */
  loadingText?: string;
  
  /**
   * 列配置
   */
  columns?: any[];
  
  /**
   * 表格标题
   */
  caption?: string;
  
  /**
   * 表格底部内容
   */
  footer?: string;
  
  /**
   * 表格布局
   */
  tableLayout?: string;
  
  /**
   * 无数据时显示的文本
   */
  noDataText?: string;
  
  /**
   * 是否不显示空状态
   */
  noEmpty?: boolean;
  
  /**
   * 滚动配置
   */
  scroll?: object;
  
  /**
   * 包装器样式
   */
  wrapperStyle?: object | any[] | string;
  
  /**
   * 表格主体样式
   */
  bodyStyle?: object | any[] | string;
  
  /**
   * 是否显示行号
   */
  rownum?: boolean;
  
  /**
   * 行号列宽度
   */
  rownumWidth?: number;
  
  /**
   * 行号列标题
   */
  rownumTitle?: string;
  
  /**
   * 是否支持多选
   */
  multiple?: boolean;
  
  /**
   * 是否隐藏多选列
   */
  hideMultiple?: boolean;
  
  /**
   * 多选列宽度
   */
  multipleWidth?: number;
  
  /**
   * 多选列标题
   */
  multipleTitle?: string;
  
  /**
   * 是否支持单选
   */
  radio?: boolean;
  
  /**
   * 是否隐藏单选列
   */
  hideRadio?: boolean;
  
  /**
   * 单选列宽度
   */
  radioWidth?: number;
  
  /**
   * 单选列标题
   */
  radioTitle?: string;
  
  /**
   * 子行宽度
   */
  subrowWidth?: number;
  
  /**
   * 子行标题
   */
  subrowTitle?: string;
  
  /**
   * 单元格格式化器
   */
  formatters?: object;
  
  /**
   * 单元格样式处理器
   */
  cellStyles?: object;
  
  /**
   * 单元格合并处理器
   */
  cellSpans?: object;
  
  /**
   * 渲染器
   */
  renders?: object;
  
  /**
   * 表头格式化器
   */
  headerFormatters?: object;
  
  /**
   * 表头属性
   */
  headerAttrs?: object;
  
  /**
   * 服务
   */
  service?: object | Function;
  
  /**
   * 是否自动加载
   */
  autoLoad?: boolean;
  
  /**
   * 参数
   */
  param?: object | any[] | Function;
  
  /**
   * 数据挂载前的处理函数
   */
  beforeDataMounted?: Function;
  
  /**
   * 数据
   */
  data?: any[] | Function;
  
  /**
   * 是否自动生成主键
   */
  autoPk?: boolean | Function;
  
  /**
   * 尺寸
   */
  size?: string;
  
  /**
   * 读取器配置
   */
  reader?: object | Function;
  
  /**
   * 分页配置
   */
  pager?: object | boolean | Function;
  
  /**
   * 是否显示分页
   */
  showPager?: boolean;
  
  /**
   * 是否不显示分页
   */
  noPager?: boolean;
  
  /**
   * 数据渲染后的处理函数
   */
  afterRenderData?: Function;
  
  /**
   * 数据解析函数
   */
  dataParse?: Function;
  
  /**
   * 绑定值
   */
  value?: any[] | string;
  
  /**
   * 是否保持选中状态
   */
  keepSelected?: boolean;
  
  /**
   * 是否点击行自动选中
   */
  autoSelect?: boolean;
  
  /**
   * 参数改变时是否自动查询
   */
  autoQuery?: boolean;
  
  /**
   * 单元格点击前的处理函数
   */
  beforeCellClick?: Function;
  
  /**
   * 单元格属性处理器
   */
  cellAttrs?: object;
  
  /**
   * 其他高度提供者
   */
  otherHeightProvider?: object | Window | Element | Function;
  
  /**
   * 行分组
   */
  rowGroups?: any[] | object;
  
  /**
   * 是否自动滚动
   */
  autoScroll?: boolean;
  
  /**
   * 自动滚动速度
   */
  autoScrollSpeed?: number | string;
  
  /**
   * 自动滚动步长
   */
  autoScrollStep?: number | string;
  
  /**
   * 自动滚动延迟
   */
  autoScrollDelay?: number | string;
  
  /**
   * 是否无限滚动
   */
  autoScrollInfinite?: boolean;
  
  /**
   * 无数据时的样式
   */
  noDataStyle?: object | any[] | string;
  
  /**
   * 是否显示加载状态
   */
  showLoading?: boolean;
  
  /**
   * 容器样式
   */
  containerStyle?: object | any[] | string;
  
  /**
   * 排序器
   */
  sorters?: object;
  
  /**
   * 行悬停样式
   */
  hoverStyle?: object | any[] | string;
  
  /**
   * 文本对齐方式
   */
  textAlign?: string;
  
  /**
   * 是否启用列宽调整
   */
  enableResize?: boolean;
}

interface FbSimpleTableSlots {
  /**
   * 默认插槽
   */
  default?: () => any;
}

type FbSimpleTableComponent = DefineComponent<FbSimpleTableProps, {}, {}, {}, {}, {}, {}, {}, 'transition', FbSimpleTableSlots>;

declare const FbSimpleTable: FbSimpleTableComponent;

export default FbSimpleTable;