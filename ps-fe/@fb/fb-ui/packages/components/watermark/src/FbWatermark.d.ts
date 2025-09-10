import { VNode, Component } from 'vue'

interface FbWatermarkProps {
  /**
   * 超级水印模式
   * 采用 mask 实现
   */
  mask?: boolean
  
  /**
   * 超级水印透明度
   */
  maskOpacity?: number
  
  /**
   * 内容
   * 单行 '朱红掏'
   * 换行 ['章了了', '13999999999', 'zht@cleverlin.com']
   */
  content?: string | string[]
  
  /**
   * 水印块的宽度
   */
  width?: number
  
  /**
   * 水印块的高度
   */
  height?: number
  
  /**
   * 旋转角度
   */
  rotate?: number
  
  /**
   * 水印文字尺寸
   * 尺码 xs,s,m,l,xl,xxl,xxxl
   * 数字 12,13,14,15...
   * 文本 12px, 13px, 14px...
   */
  size?: string | number
  
  /**
   * 水印文字颜色
   * hex #333 #666666 #dddddd01
   * rgba rgba(1a, 2b, 3c, 0.5)
   */
  color?: string
  
  /**
   * 水印背景颜色
   * hex #333 #666666 #dddddd01
   * rgba rgba(1a, 2b, 3c, 0.5)
   */
  backgroundColor?: string
  
  /**
   * 水印文字字重
   * normal bold
   */
  weight?: string
  
  /**
   * 水印文字行高
   */
  lineHeight?: number
  
  /**
   * 水印文字字体
   * 'sans-serif' 'Microsoft YaHei'
   */
  family?: string
  
  /**
   * 文本对齐选项。
   * 可选的值包括：start, end, left, right or center.
   * 默认值是 start
   */
  align?: string
  
  /**
   * 文本方向。
   * 可能的值包括：ltr, rtl, inherit。
   * 默认值是 inherit
   */
  direction?: string
  
  /**
   * 斜体
   * 可能的值包括：false， true。
   * 默认值是 false
   */
  italic?: boolean
  
  /**
   * 水印图像
   * 图像链接 http://abc.com/xxx.png
   * 图像导入 require('./xxx.png')
   * base64
   */
  image?: string
  
  block?: boolean
  
  zIndex?: number
  
  /** 水印样式 */
  markStyle?: Record<string, any>
  
  /** 水印类名 */
  markClassName?: string
  
  /** 水印之间的水平间距 */
  gapX?: number
  
  /** 水印之间的垂直间距 */
  gapY?: number
  
  /**
   * 水印在canvas 画布上绘制的垂直偏移量，正常情况下，水印绘制在中间位置, 即 offsetTop = gapY / 2
   */
  offsetTop?: number
  
  /**
   * 水印图片距离绘制 canvas 单元的顶部距离
   * 水印在canvas 画布上绘制的水平偏移量, 正常情况下，水印绘制在中间位置, 即 offsetTop = gapX / 2
   */
  offsetLeft?: number
}

interface FbWatermarkData {
  base64Url: string
  base64Image: string
}

interface FbWatermarkMethods {
  update(): void
}

interface FbWatermarkComputed {
  getClass: string[]
  getStyle: Record<string, any>
}

export interface FbWatermark extends Component {
  $props: FbWatermarkProps
  $data: FbWatermarkData
  $methods: FbWatermarkMethods
  $computed: FbWatermarkComputed
}

declare const FbWatermark: Component

export default FbWatermark