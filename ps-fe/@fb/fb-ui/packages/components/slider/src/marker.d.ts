import { VNode, Component } from 'vue';

export interface FbSliderMarkerProps {
  /**
   * 标记
   */
  mark?: string | object;
}

export interface FbSliderMarkerData {
  // 暂无数据属性
}

export interface FbSliderMarkerComputed {
  // 暂无计算属性
}

export interface FbSliderMarkerMethods {
  // 暂无方法
}

export interface FbSliderMarkerSlots {
  // 暂无插槽
}

export declare class FbSliderMarker extends Component {
  $props: FbSliderMarkerProps;
  $data: FbSliderMarkerData;
  $computed: FbSliderMarkerComputed;
  $methods: FbSliderMarkerMethods;
  $slots: FbSliderMarkerSlots;
}

export default FbSliderMarker;