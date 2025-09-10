import { DefineComponent } from 'vue';

interface FbTimeItemProps {
  /**
   * 绑定值
   */
  value?: string;
  
  /**
   * 焦点索引
   */
  focus?: number;
  
  /**
   * 时间选项
   */
  times?: any[];
}

type FbTimeItemComponent = DefineComponent<FbTimeItemProps, {}, {}, {}, {}, {}, {}, {}, 'transition'>;

declare const FbTimeItem: FbTimeItemComponent;

export default FbTimeItem;