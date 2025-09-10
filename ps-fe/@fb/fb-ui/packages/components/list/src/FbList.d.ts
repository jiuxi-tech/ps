import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbList 组件的 Props 类型
export interface FbListProps {
  /** 头部 */
  header?: string
  
  /** 尾部 */
  footer?: string
  
  /** 数据 */
  data?: any[]
  
  /** 是否横向排列 */
  horizontal?: boolean
  
  /** 是否有边框 */
  bordered?: boolean
  
  /** 是否有分割线 */
  splitLine?: boolean
  
  /** 服务 */
  service?: object | Function
  
  /** 数据过滤器 */
  dataFilter?: Function
  
  /** 参数 */
  param?: object | Function
  
  /** 分页参数 */
  pager?: object
  
  /** 分页对齐方式 */
  pagerAlign?: string
  
  /** 分页最大长度 */
  pagerMaxLength?: number
  
  /** 是否为简单分页 */
  pagerSimple?: boolean
  
  /** 是否显示分页总数信息 */
  pagerShowTotalInfo?: boolean
  
  /** 容器样式 */
  containerStyle?: object | any[] | string | Function
}

// 定义 FbList 组件的 Data 属性类型
export interface FbListData {
  /** 组件前缀 */
  prefix: string
  
  /** 数据 */
  myData: any[]
  
  /** 是否显示空状态 */
  showEmpty: boolean
  
  /** 是否显示加载状态 */
  showLoading: boolean
  
  /** 分页参数 */
  myPager: object
  
  /** 查询参数 */
  myQueryParam: object
}

// 定义 FbList 组件的 Computed 属性类型
// 暂无计算属性

// 定义 FbList 组件的 Slots 类型
export interface FbListSlots {
  /** 头部插槽 */
  header: VNode[]
  
  /** 项插槽 */
  item: VNode[]
  
  /** 尾部插槽 */
  footer: VNode[]
}

// 定义 FbList 组件实例类型
export interface FbList extends Vue, FbListProps, FbListData {
  $slots: FbListSlots
  
  /** 混合分页参数 */
  mixPager(pager: object): object
  
  /** 处理分页变化 */
  handlePagerChange(pager: object): void
  
  /** 获取数据 */
  fetchData(): void
  
  /** 查询后更新列表 */
  updateListAfterQuery(json: object): void
  
  /** 更新数据 */
  updateData(data: any[]): void
  
  /** 执行搜索 */
  doSearch(param?: object): void
  
  /** 重新加载 */
  reload(): void
}

// 定义 FbList 组件构造函数类型
export interface FbListConstructor extends VueConstructor<FbList> {}

// 导出 FbList 组件类型
export const FbList: FbListConstructor

// 默认导出
export default FbList