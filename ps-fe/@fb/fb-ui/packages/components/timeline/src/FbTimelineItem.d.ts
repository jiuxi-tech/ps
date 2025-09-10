import { DefineComponent } from 'vue';

interface FbTimelineItemProps {
  /**
   * 描述
   */
  describe?: string;
  
  /**
   * 时间
   */
  time?: string;
  
  /**
   * 圆点颜色
   */
  dotColor?: string;
  
  /**
   * 位置
   */
  position?: string;
}

interface FbTimelineItemSlots {
  /**
   * 默认插槽
   */
  default?: () => any;
  
  /**
   * 圆点插槽
   */
  dot?: () => any;
  
  /**
   * 描述插槽
   */
  describe?: () => any;
  
  /**
   * 时间插槽
   */
  time?: () => any;
}

type FbTimelineItemComponent = DefineComponent<FbTimelineItemProps, {}, {}, {}, {}, {}, {}, {}, 'transition', FbTimelineItemSlots>;

declare const FbTimelineItem: FbTimelineItemComponent;

export default FbTimelineItem;