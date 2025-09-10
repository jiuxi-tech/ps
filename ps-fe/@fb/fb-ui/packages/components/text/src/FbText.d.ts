import { DefineComponent } from 'vue';

interface FbTextProps {
  /**
   * 显示类型
   */
  display?: string;
  
  /**
   * 内联显示
   */
  inline?: boolean;
  
  /**
   * 定位类型
   */
  position?: string;
  
  /**
   * 颜色
   */
  color?: string;
  
  /**
   * 背景色
   */
  background?: string;
  
  /**
   * 文本对齐方式
   */
  textAlign?: string;
  
  /**
   * 布局类型
   */
  layout?: string;
  
  /**
   * 字体粗细
   */
  fontWeight?: string | number;
  
  /**
   * 是否省略号显示
   */
  ellipsis?: boolean;
  
  /**
   * 字体族
   */
  family?: string;
  
  /**
   * 行高
   */
  lineHeight?: string | number;
  
  /**
   * 字体样式
   */
  fontStyle?: string;
  
  /**
   * 文本装饰
   */
  textDecoration?: string;
  
  /**
   * 文本缩进
   */
  indent?: string | number;
  
  /**
   * 外边距
   */
  margin?: string | number;
  
  /**
   * 内边距
   */
  padding?: string | number;
  
  /**
   * 过滤器
   */
  filter?: string;
  
  /**
   * 排序
   */
  order?: number;
  
  /**
   * 边框
   */
  border?: string;
  
  /**
   * 溢出处理
   */
  overflow?: string;
  
  /**
   * 盒模型
   */
  boxSizing?: string;
  
  /**
   * 垂直对齐
   */
  verticalAlign?: string;
  
  /**
   * 弹性布局
   */
  flex?: string | number;
  
  /**
   * 盒阴影
   */
  boxShadow?: string;
  
  /**
   * 自身对齐
   */
  justifySelf?: string;
  
  /**
   * 鼠标指针
   */
  cursor?: string;
  
  /**
   * 文本大小
   */
  size?: string | number;
  
  /**
   * 文本类型
   */
  type?: string;
  
  /**
   * 渐变背景
   */
  gradient?: string;
}

interface FbTextSlots {
  /**
   * 默认插槽
   */
  default?: () => any;
}

type FbTextComponent = DefineComponent<FbTextProps, {}, {}, {}, {}, {}, {}, {}, 'transition', FbTextSlots>;

declare const FbText: FbTextComponent;

export default FbText;