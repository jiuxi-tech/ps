import { VNode } from 'vue'
import { FbDivider } from '../../divider/src/FbDivider'
import { FbUploadList } from './FbUploadList'
import { FbAjaxUpload } from './FbAjaxUpload'
import { FbAvatar } from '../../avatar/src/FbAvatar'
import { FbIcon } from '../../icon/src/FbIcon'
import { FbText } from '../../text/src/FbText'

export interface FbUploadFile {
  /**
   * 文件最后修改时间
   */
  lastModified?: number
  
  /**
   * 文件最后修改日期
   */
  lastModifiedDate?: Date
  
  /**
   * 文件名
   */
  name: string
  
  /**
   * 文件大小
   */
  size: number
  
  /**
   * 文件类型
   */
  type: string
  
  /**
   * 文件唯一标识
   */
  uid: string
  
  /**
   * 上传进度百分比
   */
  percent: number
  
  /**
   * 原始文件对象
   */
  originFileObj: any
  
  /**
   * 文件状态
   */
  status: 'uploading' | 'done' | 'error' | 'removed'
  
  /**
   * 附件名称
   */
  attachName?: string
  
  /**
   * 附件ID
   */
  attachId?: string
  
  /**
   * 附件大小
   */
  attachSize?: number
  
  /**
   * 响应数据
   */
  res?: any
  
  /**
   * 错误信息
   */
  error?: any
}

export interface FbUploadProps {
  /**
   * 上传模式
   */
  mode?: 'drag' | 'select'
  
  /**
   * 文件字段名
   */
  name?: string
  
  /**
   * 文件列表
   */
  fileList?: FbUploadFile[]
  
  /**
   * 服务配置
   */
  service?: object | Function
  
  /**
   * 上传参数
   */
  param?: object
  
  /**
   * 是否为目录上传
   */
  directory?: boolean
  
  /**
   * 附加数据
   */
  data?: any
  
  /**
   * 是否多选
   */
  multiple?: boolean
  
  /**
   * 最大文件数量
   */
  maxLength?: number | string
  
  /**
   * 可接受的文件类型
   */
  accept?: string
  
  /**
   * 上传前回调函数
   */
  beforeUpload?: Function
  
  /**
   * 选择后回调函数
   */
  afterSelect?: Function
  
  /**
   * 删除前回调函数
   */
  beforeRemove?: Function
  
  /**
   * 显示视图
   */
  view?: 'list' | 'image' | 'avatar' | 'grid'
  
  /**
   * 是否禁用
   */
  disabled?: boolean
  
  /**
   * 是否携带凭证
   */
  withCredentials?: boolean
  
  /**
   * 是否快速选择
   */
  fastSelect?: boolean
  
  /**
   * 高度
   */
  height?: string | number
  
  /**
   * 是否显示上传列表
   */
  showUploadList?: boolean
  
  /**
   * 是否显示上传按钮
   */
  showUpload?: boolean
  
  /**
   * 压缩类型
   */
  compressTypes?: string
  
  /**
   * 压缩质量
   */
  quality?: number
  
  /**
   * 最大宽度
   */
  maxWidth?: number | boolean
  
  /**
   * 最大高度
   */
  maxHeight?: number | boolean
  
  /**
   * 是否只读
   */
  readonly?: boolean
  
  /**
   * 头像大小
   */
  avatarSize?: number
  
  /**
   * 是否圆形头像
   */
  avatarCircle?: boolean
  
  /**
   * 按钮图标
   */
  buttonIcon?: string
  
  /**
   * 按钮文字
   */
  buttonText?: string
  
  /**
   * 描述文字
   */
  desc?: string
  
  /**
   * 是否显示预览
   */
  showPreview?: boolean
  
  /**
   * 是否显示下载按钮
   */
  showDownload?: boolean
  
  /**
   * 是否显示删除按钮
   */
  showRemove?: boolean
  
  /**
   * 是否支持拖拽
   */
  drag?: boolean
  
  /**
   * 是否支持粘贴
   */
  paste?: boolean
  
  /**
   * 是否不压缩
   */
  noCompress?: boolean
  
  /**
   * 是否支持不带后缀的文件
   */
  noSuffix?: boolean
  
  /**
   * 是否以blob形式上传
   */
  blob?: boolean
  
  /**
   * 是否抛出预览事件
   */
  throwPreviewEvent?: boolean
}

export interface FbUploadData {
  /**
   * 进度定时器
   */
  progressTimer: any
  
  /**
   * 是否聚焦
   */
  focus: boolean
  
  /**
   * 文件列表
   */
  myFileList: FbUploadFile[]
}

export interface FbUploadComputed {
  /**
   * 头像图片源
   */
  avatarSrc: string | null
}

export interface FbUploadMethods {
  /**
   * 处理包装器点击
   */
  handleWrapperClick(): void
  
  /**
   * 清除进度定时器
   */
  clearProgressTimer(): void
  
  /**
   * 处理开始上传
   */
  handleStart(file: FbUploadFile): void
  
  /**
   * 处理上传进度
   */
  handleProgress(e: { percent: number }, file: FbUploadFile): void
  
  /**
   * 处理上传成功
   */
  handleSuccess(res: any, file: FbUploadFile): void
  
  /**
   * 处理上传错误
   */
  handleError(error: any, res: any, file: FbUploadFile): void
  
  /**
   * 处理文件拒绝
   */
  handleReject(fileList: FbUploadFile[]): void
  
  /**
   * 处理文件删除
   */
  handleRemove(file: FbUploadFile): void
  
  /**
   * 处理预览
   */
  handlePreview(e: any): void
  
  /**
   * 处理预览关闭
   */
  handlePreviewClose(e: any): void
  
  /**
   * 处理文件变化
   */
  handleChange(info: { file: FbUploadFile; fileList: FbUploadFile[] }): void
  
  /**
   * 处理文件拖拽
   */
  onFileDrop(e: any): void
  
  /**
   * 自动更新进度
   */
  autoUpdateProgress(_: any, file: FbUploadFile): void
}

export interface FbUploadSlots {
  /**
   * 默认插槽
   */
  default: VNode[]
  
  /**
   * 头像按钮插槽
   */
  avatarButton: VNode[]
}

export declare class FbUpload extends Vue implements FbUploadProps, FbUploadData, FbUploadComputed, FbUploadMethods {
  static readonly componentName: 'FbUpload'
  
  // Props
  mode: 'drag' | 'select'
  name: string
  fileList: FbUploadFile[]
  service: object | Function
  param: object
  directory: boolean
  data: any
  multiple: boolean
  maxLength: number | string
  accept: string
  beforeUpload: Function
  afterSelect: Function
  beforeRemove: Function
  view: 'list' | 'image' | 'avatar' | 'grid'
  disabled: boolean
  withCredentials: boolean
  fastSelect: boolean
  height: string | number
  showUploadList: boolean
  showUpload: boolean
  compressTypes: string
  quality: number
  maxWidth: number | boolean
  maxHeight: number | boolean
  readonly: boolean
  avatarSize: number
  avatarCircle: boolean
  buttonIcon: string
  buttonText: string
  desc: string
  showPreview: boolean
  showDownload: boolean
  showRemove: boolean
  drag: boolean
  paste: boolean
  noCompress: boolean
  noSuffix: boolean
  blob: boolean
  throwPreviewEvent: boolean
  
  // Data
  progressTimer: any
  focus: boolean
  myFileList: FbUploadFile[]
  
  // Computed
  avatarSrc: string | null
  
  // Methods
  handleWrapperClick(): void
  clearProgressTimer(): void
  handleStart(file: FbUploadFile): void
  handleProgress(e: { percent: number }, file: FbUploadFile): void
  handleSuccess(res: any, file: FbUploadFile): void
  handleError(error: any, res: any, file: FbUploadFile): void
  handleReject(fileList: FbUploadFile[]): void
  handleRemove(file: FbUploadFile): void
  handlePreview(e: any): void
  handlePreviewClose(e: any): void
  handleChange(info: { file: FbUploadFile; fileList: FbUploadFile[] }): void
  onFileDrop(e: any): void
  autoUpdateProgress(_: any, file: FbUploadFile): void
}

export default FbUpload