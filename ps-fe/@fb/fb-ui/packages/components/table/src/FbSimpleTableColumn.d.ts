import { DefineComponent } from 'vue';

interface FbSimpleTableColumnProps {
  /**
   * 行合并数
   */
  rowspan?: string | number;
  
  /**
   * 列合并数
   */
  colspan?: string | number;
}

type FbSimpleTableColumnComponent = DefineComponent<FbSimpleTableColumnProps, {}, {}, {}, {}, {}, {}, {}, 'transition'>;

declare const FbSimpleTableColumn: FbSimpleTableColumnComponent;

export default FbSimpleTableColumn;