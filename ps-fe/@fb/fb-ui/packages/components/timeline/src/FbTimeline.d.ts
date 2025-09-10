import { DefineComponent } from 'vue';

interface FbTimelineProps {
  /**
   * 模式
   */
  mode?: string;
  
  /**
   * 是否反转
   */
  reverse?: boolean;
  
  /**
   * 是否水平显示
   */
  horizontal?: boolean;
}

interface FbTimelineSlots {
  /**
   * 默认插槽
   */
  default?: () => any;
}

type FbTimelineComponent = DefineComponent<FbTimelineProps, {}, {}, {}, {}, {}, {}, {}, 'transition', FbTimelineSlots>;

declare const FbTimeline: FbTimelineComponent;

export default FbTimeline;