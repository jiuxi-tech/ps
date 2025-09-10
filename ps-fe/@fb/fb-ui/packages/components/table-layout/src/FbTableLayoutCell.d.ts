import { DefineComponent } from 'vue';

interface FbTableLayoutCellProps {
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
   * 水平对齐方式
   */
  align?: string;
  
  /**
   * 垂直对齐方式
   */
  valign?: string;
  
  /**
   * 内边距
   */
  padding?: string | number;
  
  /**
   * 值
   */
  value?: string | number | boolean | object | any[];
}

interface FbTableLayoutCellSlots {
  /**
   * 默认插槽
   */
  default?: () => any;
}

type FbTableLayoutCellComponent = DefineComponent<FbTableLayoutCellProps, {}, {}, {}, {}, {}, {}, {}, 'transition', FbTableLayoutCellSlots>;

declare const FbTableLayoutCell: FbTableLayoutCellComponent;

export default FbTableLayoutCell;