import { DefineComponent } from 'vue';

interface FbTableLayoutProps {
  /**
   * 尺寸
   */
  size?: string;
  
  /**
   * 类型
   */
  type?: string;
  
  /**
   * 颜色
   */
  color?: string;
  
  /**
   * 是否省略
   */
  ellipsis?: boolean;
  
  /**
   * 宽度
   */
  width?: string | number;
  
  /**
   * 是否长表格
   */
  long?: boolean;
}

interface FbTableLayoutSlots {
  /**
   * 默认插槽
   */
  default?: () => any;
}

type FbTableLayoutComponent = DefineComponent<FbTableLayoutProps, {}, {}, {}, {}, {}, {}, {}, 'transition', FbTableLayoutSlots>;

declare const FbTableLayout: FbTableLayoutComponent;

export default FbTableLayout;