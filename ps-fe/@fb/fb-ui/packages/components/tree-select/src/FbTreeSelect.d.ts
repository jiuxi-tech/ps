import { VNode } from 'vue'
import { FbIcon } from '../../icon/src/FbIcon'
import { FbTree } from '../../tree/src/FbTree'
import { FbContainer } from '../../container/src/FbContainer'
import { FbInput } from '../../input/src/FbInput'

export interface FbTreeSelectProps {
  /**
   * 绑定值
   */
  value?: any[] | number | string
  
  /**
   * 占位符
   */
  placeholder?: string
  
  /**
   * 是否禁用
   */
  disabled?: boolean
  
  /**
   * 是否只读
   */
  readonly?: boolean
  
  /**
   * 位置
   */
  position?: string
  
  /**
   * 下拉的最大高度
   */
  maxHeight?: string | number
  
  /**
   * 是否显示确认按钮
   */
  showConfirm?: boolean
  
  /**
   * 是否可清除
   */
  clearable?: boolean
  
  /**
   * 数据
   */
  data?: any[]
  
  /**
   * 数据读取配置
   */
  reader?: {
    value: string
    label: string
  }
  
  /**
   * 是否显示图标
   */
  showIcon?: boolean
  
  /**
   * 是否显示标题
   */
  showTitle?: boolean
  
  /**
   * 是否内联显示
   */
  inline?: boolean
  
  /**
   * 是否多选
   */
  multiple?: boolean
  
  /**
   * URL
   */
  url?: string
  
  /**
   * 服务获取节点数据
   */
  service?: object | any[] | Function
  
  /**
   * 查询参数
   */
  param?: object | any[] | Function
  
  /**
   * 网络数据过滤器
   */
  dataFilter?: Function
  
  /**
   * 加载数据函数
   */
  loadData?: Function
  
  /**
   * 二次点击时，是否继续保持选中状态
   */
  twiceClickSelected?: boolean
  
  /**
   * 头部格式化函数
   */
  headerFormat?: Function
  
  /**
   * checkbox 被勾选后的影响范围
   */
  doCheck?: string
  
  /**
   * checkbox 取消勾选后的影响范围
   */
  doUnCheck?: string
  
  /**
   * 只有叶子结点的选择模式
   */
  onlyLeaf?: boolean
  
  /**
   * 叶子结点字段名称
   */
  leafName?: string
  
  /**
   * 去除半选的选择模式
   */
  noHalf?: boolean
  
  /**
   * 下拉样式
   */
  listStyle?: object
  
  /**
   * 当前选中值发生变化后，是否隐藏选择框
   */
  hidePickerAfterChange?: boolean
  
  /**
   * 是否合并 tag +num 形式
   */
  collapseTags?: boolean
  
  /**
   * 是否可过滤
   */
  filterable?: boolean
  
  /**
   * 过滤框占位符
   */
  filterPlaceholder?: string
  
  /**
   * 过滤节点方法
   */
  filterNodeMethod?: Function
  
  /**
   * 下拉图标名称
   */
  iconName?: string
  
  /**
   * 弹出层类名
   */
  popperClass?: string
}

export interface FbTreeSelectData {
  /**
   * 过滤文本
   */
  filterText: string
  
  /**
   * 当前值
   */
  currentValue: any[] | number | string
  
  /**
   * 当前节点
   */
  currentNodes: any[]
  
  /**
   * 是否显示清除按钮
   */
  showClear: boolean
  
  /**
   * 是否显示下拉框
   */
  showDropdown: boolean
  
  /**
   * 是否显示加载状态
   */
  showLoading: boolean
  
  /**
   * 是否显示空状态
   */
  showEmpty: boolean
  
  /**
   * 分页器
   */
  myPager: object
  
  /**
   * 查询参数
   */
  myQueryParam: any
  
  /**
   * 头部格式化函数
   */
  myHeaderFormat: Function
  
  /**
   * 是否远程数据
   */
  isRemote: boolean
  
  /**
   * 是否已加载
   */
  isLoaded: boolean
  
  /**
   * 临时值
   */
  tempValue: any[] | number | string
}

export interface FbTreeSelectComputed {
  /**
   * Tab索引
   */
  getTabindex: number
  
  /**
   * 选中文本类名
   */
  getSelectionTextClass: string
}

export interface FbTreeSelectMethods {
  /**
   * 过滤节点
   */
  doFilterNode(): void
  
  /**
   * 隐藏弹出框
   */
  hidePopup(): void
  
  /**
   * 显示弹出框
   */
  showPopup(): void
  
  /**
   * 失焦处理
   */
  blur(): void
  
  /**
   * 聚焦处理
   */
  focus(): void
  
  /**
   * 切换显示状态
   */
  toggle(): void
  
  /**
   * 更新位置
   */
  updatePosition(): void
  
  /**
   * 清除选中
   */
  clear(e: Event): void
  
  /**
   * 关闭
   */
  close(): void
  
  /**
   * 完成处理
   */
  finished(): void
  
  /**
   * 加载状态处理
   */
  loading(status: boolean): void
  
  /**
   * 处理选择变化
   */
  handleSelectChange(node: any, e: Event): void
  
  /**
   * 处理复选框变化
   */
  handleCheckChange(nodes: any[], e: Event): void
  
  /**
   * 处理加载数据
   */
  handleLoadData(data: any): void
  
  /**
   * 处理数据加载
   */
  handleDataLoad(data: any): void
  
  /**
   * 处理数据更新
   */
  handleDataUpdate(): void
  
  /**
   * 改变当前值
   */
  changeCurrentValue(e?: Event): void
  
  /**
   * 更新树节点
   */
  updateTreeNodes(): void
  
  /**
   * 根据值选择节点
   */
  selectNodeByValue(value: any): void
  
  /**
   * 根据值选中节点
   */
  checkNodesByValue(value: any): void
  
  /**
   * 移除选中选项
   */
  removeSelectedOption(node: any): void
  
  /**
   * 计算折叠标题
   */
  calcCollapseTitle(currentNodes: any[]): string
}

export interface FbTreeSelectSlots {
  /**
   * 头部插槽
   */
  header: VNode[]
  
  /**
   * 默认插槽
   */
  default: VNode[]
}

export declare class FbTreeSelect extends Vue implements FbTreeSelectProps, FbTreeSelectData, FbTreeSelectComputed, FbTreeSelectMethods {
  static readonly componentName: 'FbTreeSelect'
  
  // Props
  value: any[] | number | string
  placeholder: string
  disabled: boolean
  readonly: boolean
  position: string
  maxHeight: string | number
  showConfirm: boolean
  clearable: boolean
  data: any[]
  reader: {
    value: string
    label: string
  }
  showIcon: boolean
  showTitle: boolean
  inline: boolean
  multiple: boolean
  url: string
  service: object | any[] | Function
  param: object | any[] | Function
  dataFilter: Function
  loadData: Function
  twiceClickSelected: boolean
  headerFormat: Function
  doCheck: string
  doUnCheck: string
  onlyLeaf: boolean
  leafName: string
  noHalf: boolean
  listStyle: object
  hidePickerAfterChange: boolean
  collapseTags: boolean
  filterable: boolean
  filterPlaceholder: string
  filterNodeMethod: Function
  iconName: string
  popperClass: string
  
  // Data
  filterText: string
  currentValue: any[] | number | string
  currentNodes: any[]
  showClear: boolean
  showDropdown: boolean
  showLoading: boolean
  showEmpty: boolean
  myPager: object
  myQueryParam: any
  myHeaderFormat: Function
  isRemote: boolean
  isLoaded: boolean
  tempValue: any[] | number | string
  
  // Computed
  getTabindex: number
  getSelectionTextClass: string
  
  // Methods
  doFilterNode(): void
  hidePopup(): void
  showPopup(): void
  blur(): void
  focus(): void
  toggle(): void
  updatePosition(): void
  clear(e: Event): void
  close(): void
  finished(): void
  loading(status: boolean): void
  handleSelectChange(node: any, e: Event): void
  handleCheckChange(nodes: any[], e: Event): void
  handleLoadData(data: any): void
  handleDataLoad(data: any): void
  handleDataUpdate(): void
  changeCurrentValue(e?: Event): void
  updateTreeNodes(): void
  selectNodeByValue(value: any): void
  checkNodesByValue(value: any): void
  removeSelectedOption(node: any): void
  calcCollapseTitle(currentNodes: any[]): string
}

export default FbTreeSelect