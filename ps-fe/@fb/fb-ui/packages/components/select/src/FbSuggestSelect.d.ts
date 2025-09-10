import { VNode, Component } from 'vue';
import { FbSelectOption } from './FbSelectOption.d';

export interface FbSuggestSelectProps {
  /**
   * 尺寸
   */
  size?: string;

  /**
   * 绑定值
   */
  value?: string | number | Array<any> | boolean;

  /**
   * 占位符
   */
  placeholder?: string;

  /**
   * 禁用
   */
  disabled?: boolean;

  /**
   * 只读
   */
  readonly?: boolean;

  /**
   * 搜索框
   */
  filterable?: boolean;

  /**
   * 显示清除按钮
   */
  clearable?: boolean;

  /**
   * 多选
   */
  multiple?: boolean;

  /**
   * 最多选几个, 0不限制
   */
  multipleLimit?: number;

  /**
   * 节点数据
   */
  data?: Array<any>;

  /**
   * 网络获取节点数据
   */
  url?: string;

  /**
   * 服务获取节点数据
   */
  service?: object | Array<any> | Function;

  /**
   * 查询参数
   */
  param?: object | Array<any> | Function;

  /**
   * 网络数据过滤器
   */
  dataFilter?: Function;

  /**
   * 取值配对
   */
  reader?: {
    value: string;
    label: string;
  };

  /**
   * 自动加载数据
   */
  autoLoad?: boolean;
}

export interface FbSuggestSelectData {
  focusOption: any;
  currentValue: string | number | Array<any> | boolean;
  searchValue: string;
  showClear: boolean;
  showList: boolean;
  myData: Array<any>;
  options: Array<any>;
  noChildren: number;
  pager: {
    pageSize: number;
    page: number;
  };
  searching: boolean;
  showLoading: boolean;
  showEmpty: boolean;
  myPager: any;
  myQueryParam: any;
}

export interface FbSuggestSelectComputed {
  /**
   * tab 键顺序
   */
  getTabindex: number;

  /**
   * UI选择类
   */
  getUISelectClass: string;

  /**
   * 本地占位符
   */
  localPlaceholder: string;

  /**
   * 选中选项
   */
  selectedOption: any;

  /**
   * 禁用限制
   */
  disableLimit: boolean;

  /**
   * 搜索选项
   */
  searchOptions: Array<any>;

  /**
   * 当前选项
   */
  currentOptions: Array<any>;
}

export interface FbSuggestSelectMethods {
  /**
   * 添加标题属性
   */
  addTitleAttr(target: HTMLElement, label: string): void;

  /**
   * 处理标题属性
   */
  handleTitleAttr(e: Event): void;

  /**
   * 添加选项
   */
  addOption(option: any): void;

  /**
   * 移除选项
   */
  removeOption(option: any): void;

  /**
   * 应用数据
   */
  applyData(): Array<any>;

  /**
   * 选择选项
   */
  selectOption(e: Event): void;

  /**
   * 点击选项
   */
  clickOption(option: any): void;

  /**
   * 改变当前值
   */
  changeCurrentValue(): void;

  /**
   * 移除选中选项
   */
  removeSelectedOption(option: any): void;

  /**
   * 改变搜索值
   */
  changeSearchValue(): void;

  /**
   * 失去焦点
   */
  blur(): void;

  /**
   * 获得焦点
   */
  focus(): void;

  /**
   * 切换显示状态
   */
  toggle(): void;

  /**
   * 更新位置
   */
  updatePosition(): void;

  /**
   * 清除
   */
  clear(): void;

  /**
   * 关闭
   */
  close(): void;

  /**
   * 完成
   */
  finished(): void;

  /**
   * 上一页
   */
  prev(): void;

  /**
   * 下一页
   */
  next(): void;

  /**
   * 聚焦
   */
  doFocus(option: any): void;

  /**
   * 键盘事件
   */
  keydown(e: KeyboardEvent): void;

  /**
   * 鼠标选择选项
   */
  mouseChooseOption(direction: number): void;

  /**
   * 加载数据
   */
  loadData(): void;

  /**
   * 获取数据
   */
  fetchData(): void;

  /**
   * 更新数据查询后
   */
  updateDataAfterQuery(json: any): void;

  /**
   * 更新数据
   */
  updateData(data: any): void;

  /**
   * 加载状态
   */
  loading(status: boolean): void;
}

export interface FbSuggestSelectSlots {
  /**
   * 默认插槽
   */
  default: VNode[];

  /**
   * 选项插槽
   */
  option: VNode[];
}

export declare class FbSuggestSelect extends Component {
  $props: FbSuggestSelectProps;
  $data: FbSuggestSelectData;
  $computed: FbSuggestSelectComputed;
  $methods: FbSuggestSelectMethods;
  $slots: FbSuggestSelectSlots;
}

export default FbSuggestSelect;