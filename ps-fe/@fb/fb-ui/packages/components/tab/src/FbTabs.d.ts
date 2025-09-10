import { DefineComponent } from 'vue';
import { FbTabProps } from './FbTab.d';

interface FbTabsProps {
  /**
   * 绑定值
   */
  value?: number | string;
  
  /**
   * 类型
   */
  type?: string;
  
  /**
   * 是否可关闭
   */
  closable?: boolean;
  
  /**
   * 过渡动画
   */
  transition?: string;
  
  /**
   * 关闭前的回调
   */
  beforeRemove?: Function;
  
  /**
   * 切换前的回调
   */
  beforeChange?: Function;
  
  /**
   * 数据
   */
  data?: any[];
  
  /**
   * 主体样式
   */
  bodyStyle?: object;
}

interface FbTabsSlots {
  /**
   * 操作按钮插槽
   */
  actions?: () => any;
  
  /**
   * 默认插槽
   */
  default?: () => any;
  
  /**
   * 标签页插槽
   */
  tab?: (props: { tab: any }) => any;
}

type FbTabsComponent = DefineComponent<FbTabsProps, {}, {}, {}, {}, {}, {}, {}, 'transition', FbTabsSlots>;

declare const FbTabs: FbTabsComponent;

export default FbTabs;