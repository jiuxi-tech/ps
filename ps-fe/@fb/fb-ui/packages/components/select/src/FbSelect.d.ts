import { VNode, Component } from 'vue';
import { FbSelectOption } from './FbSelectOption.d';
import { FbSelectOptionGroup } from './FbSelectOptionGroup.d';

export interface FbSelectProps {
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
   * 显示多选图标
   */
  showMultipleIcon?: boolean;

  /**
   * 最多选几个, 0不限制
   */
  multipleLimit?: number;

  /**
   * 多选时是否将选中值按文字的形式展示
   */
  collapseTags?: boolean;

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

  /**
   * 显示分页
   */
  showPager?: boolean;

  /**
   * 分页配置
   */
  pager?: object | boolean | Function;

  /**
   * 自动分页
   */
  autoPager?: boolean | number | string;

  /**
   * 选项扩展类名
   */
  selectOptionClass?: object | Array<any>;

  /**
   * 选项扩展样式
   */
  selectOptionStyle?: object | Array<any>;

  /**
   * 选中前的事件
   */
  beforeSelect?: Function;

  /**
   * 下拉icon图标
   */
  iconName?: string;

  /**
   * 弹出层类名
   */
  popperClass?: string;
}

export interface FbSelectData {
  uuid: string;
  focusOption: any;
  currentValue: string | number | Array<any> | boolean;
  searchValue: string;
  showClear: boolean;
  showDropdown: boolean;
  myData: Array<any>;
  options: Array<any>;
  noChildren: number;
  searching: boolean;
  showLoading: boolean;
  showEmpty: boolean;
  myPager: any;
  myShowPager: boolean;
  myQueryParam: any;
}

export interface FbSelectComputed {
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

export interface FbSelectMethods {
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
   * 处理选择前事件
   */
  handleBeforeSelect(data: any, success: Function): Promise<void>;

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

  /**
   * 混合分页
   */
  mixPager(pager: any): any;

  /**
   * 处理分页改变
   */
  handlePageChange(pager: any): void;
}

export interface FbSelectSlots {
  /**
   * 默认插槽
   */
  default: VNode[];

  /**
   * 选项插槽
   */
  option: VNode[];
}

export declare class FbSelect extends Component {
  $props: FbSelectProps;
  $data: FbSelectData;
  $computed: FbSelectComputed;
  $methods: FbSelectMethods;
  $slots: FbSelectSlots;
}

export default FbSelect;