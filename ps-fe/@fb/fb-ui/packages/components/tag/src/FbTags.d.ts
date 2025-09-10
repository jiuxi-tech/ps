import { DefineComponent } from 'vue';
import { FbTagProps } from './FbTag.d';

interface FbTagsProps {
  /**
   * 绑定值
   */
  value?: any[];
  
  /**
   * 数据
   */
  data?: any[];
  
  /**
   * 是否垂直排列
   */
  vertical?: boolean;
  
  /**
   * 读取器配置
   */
  reader?: object;
  
  /**
   * 是否可关闭
   */
  closable?: boolean;
  
  /**
   * 是否禁用
   */
  disabled?: boolean;
  
  /**
   * 是否只读
   */
  readonly?: boolean;
  
  /**
   * 是否圆角
   */
  round?: boolean;
  
  /**
   * 类型
   */
  type?: string;
  
  /**
   * 颜色
   */
  color?: string;
  
  /**
   * 背景色
   */
  backgroundColor?: string;
  
  /**
   * 边框颜色
   */
  borderColor?: string;
  
  /**
   * 标签文字颜色
   */
  labelColor?: string;
  
  /**
   * 效果
   */
  effect?: string;
  
  /**
   * 是否浅色效果
   */
  light?: boolean;
  
  /**
   * 是否朴素效果
   */
  plain?: boolean;
  
  /**
   * 是否深色效果
   */
  dark?: boolean;
  
  /**
   * 是否实心效果
   */
  solid?: boolean;
  
  /**
   * 尺寸
   */
  size?: string;
  
  /**
   * 图标
   */
  icon?: string;
}

interface FbTagsSlots {
  /**
   * 节点插槽
   */
  node?: (props: { node: any }) => any;
}

type FbTagsComponent = DefineComponent<FbTagsProps, {}, {}, {}, {}, {}, {}, {}, 'transition', FbTagsSlots>;

declare const FbTags: FbTagsComponent;

export default FbTags;