import { Vue, VueConstructor } from 'vue/types/vue'
import { VNode } from 'vue'

// 定义 FbTree 组件的 Props 类型
export interface FbTreeProps {
  /** 读取器配置 */
  reader?: object
  
  /** 是否显示图标 */
  showIcon?: boolean
  
  /** 是否显示标题 */
  showTitle?: boolean
  
  /** 是否显示线条 */
  showLine?: boolean
  
  /** 是否内联显示 */
  inline?: boolean
  
  /** 是否多选 */
  multiple?: boolean
  
  /** 是否单选 */
  radio?: boolean
  
  /** 单选分组范围 */
  radioGroup?: string
  
  /** 数据 */
  data?: Array<any>
  
  /** 高度 */
  height?: string | number
  
  /** URL */
  url?: string
  
  /** 服务 */
  service?: object | Array<any> | Function
  
  /** 参数 */
  param?: object | Array<any> | Function
  
  /** 数据过滤器 */
  dataFilter?: Function
  
  /** 加载数据函数 */
  loadData?: Function
  
  /** 二次点击是否保持选中 */
  twiceClickSelected?: boolean
  
  /** 二次点击是否展开 */
  twiceClickExpand?: boolean
  
  /** 选中时影响范围 */
  doCheck?: string
  
  /** 取消选中时影响范围 */
  doUnCheck?: string
  
  /** 是否只选择叶子节点 */
  onlyLeaf?: boolean
  
  /** 叶子节点字段名 */
  leafName?: string
  
  /** 是否无半选 */
  noHalf?: boolean
  
  /** 作用域插槽 */
  scopedSlots?: any
  
  /** 节点过滤方法 */
  filterNodeMethod?: Function
  
  /** 展开图标 */
  expandIcon?: string
  
  /** 收起图标 */
  reduceIcon?: string
}

// 定义 FbTree 组件的 Data 属性类型
export interface FbTreeData {
  /** 组件前缀 */
  prefix: string
  
  /** 键值 */
  key: number
  
  /** 服务 */
  myService: any
  
  /** 是否首次加载 */
  firstLoad: boolean
  
  /** 数据 */
  myData: Array<any>
  
  /** 选中的节点 */
  selectedNode: any
  
  /** 根节点 */
  root: object
  
  /** 查询参数 */
  myQueryParam: any
  
  /** 分页器 */
  myPager: object
}

// 定义 FbTree 组件的 Computed 属性类型
export interface FbTreeComputed {
  /** UI样式 */
  getUiStyle: Array<any>
  
  /** 类名 */
  getClass: string[]
}

// 定义 FbTree 组件的 Slots 类型
export interface FbTreeSlots {
  /** 默认插槽 */
  default: VNode[]
}

// 定义 FbTree 组件实例类型
export interface FbTree extends Vue, FbTreeProps, FbTreeData, FbTreeComputed {
  $slots: FbTreeSlots
  
  /** 过滤 */
  filter(value: any): void
  
  /** 节点点击处理 */
  onNodeClick(node: any, e: Event): void
  
  /** 复选框点击处理 */
  onCheckClick(node: any, checked: boolean, e?: Event): void
  
  /** 复选框处理 */
  onCheck(node: any, checked: boolean): void
  
  /** 设置子节点 */
  setChild(node: any, checked: boolean): void
  
  /** 设置父节点 */
  setParent(node: any, checked: boolean): void
  
  /** 初始化 */
  init(): void
  
  /** 重新加载 */
  reload(): void
  
  /** 清除 */
  clear(): void
  
  /** 加载数据 */
  doLoadData(): void
  
  /** 搜索 */
  doSearch(param?: any): void
  
  /** 重新加载 */
  doReload(): void
  
  /** 获取数据 */
  fetchData(): void
  
  /** 查询后更新数据 */
  updateDataAfterQuery(json: any): void
  
  /** 获取选中的节点 */
  getCheckedNodes(data?: Array<any>, childrenCheckedStatus?: string): Array<any>
  
  /** 获取选中的节点 */
  getSelectedNodes(data?: Array<any>): Array<any>
  
  /** 递归处理 */
  recursion(collection: Array<any>, node: any, parent: any): void
  
  /** 设置加载的数据 */
  setLoadData(parent: any, data: Array<any>): void
  
  /** 添加节点 */
  addNode(parent: any, node: any): void
  
  /** 移除节点 */
  removeNode(node: any): void
  
  /** 获取所有节点 */
  getAllNodes(): any
  
  /** 更新节点 */
  updateNode(node: any, config: any): void
  
  /** 选中节点 */
  checkNode(node: any, checked?: boolean, expand?: boolean): void
  
  /** 选择节点 */
  selectNode(node: any, selected?: boolean, expand?: boolean): void
  
  /** 根据值选中节点 */
  checkNodeByValue(value: any, checked?: boolean, expand?: boolean): void
  
  /** 根据值选中多个节点 */
  checkNodesByValue(value: any, checked?: boolean, expand?: boolean): void
  
  /** 根据值更新多个节点 */
  updateNodesByValue(value: any, options: any, expand?: boolean): void
  
  /** 根据值获取节点 */
  getNodesByValue(value: any): Array<any>
  
  /** 根据值获取节点 */
  getNodeByValue(value: any, collections: Array<any>, parent?: any): Array<any>
  
  /** 根据值查找节点 */
  findNodesByValue(parent: any, value: any, nodes: Array<any>): void
  
  /** 获取父节点 */
  getParents(node: any): Array<any>
  
  /** 全部展开/收起 */
  expandAll(flag?: boolean, data?: Array<any>): void
}

// 定义 FbTree 组件构造函数类型
export interface FbTreeConstructor extends VueConstructor<FbTree> {}

// 导出 FbTree 组件类型
export const FbTree: FbTreeConstructor

// 默认导出
export default FbTree