/**
 * 统一导出 fb-echarts 二次封装组件
 * 注意：文件不要使用 export default 导出的模块，lib 包会有问题
 * */

export { default as FbecLineBase } from './model/line-base'
export { default as FbecLineLinear } from './model/line-linear'
export { default as FbecBarBase } from './model/bar-base'
export { default as FbecBarHorizontal } from './model/bar-horizontal'
export { default as FbecBarHorizontalAbout } from './model/bar-horizontal-about'
export { default as FbecBarThreeQuarter } from './model/bar-three-quarter'
export { default as FbecBarBaseTwoy } from './model/bar-base-twoy'
export { default as FbecPieBase } from './model/pie-base'
export { default as FbecPieRing } from './model/pie-ring'
export { default as FbecPieBar } from './model/pie-bar'
export { default as FbecPieCustomLegend } from './model/pie-custom-legend'
export { default as FbecRadarBase } from './model/radar-base'
export { default as FbecTreemapBase } from './model/treemap-base'
export { default as FbecMapBase } from './model/map-base'
export { default as FbecMapBaseBubble } from './model/map-base-bubble'
export { default as FbecMapCustomBubble } from './model/map-custom-bubble'
export { default as FbecGaugeBase1 } from './model/gauge-base1'
export { default as FbecGaugeBase2 } from './model/gauge-base2'
export { default as FbecGaugeSub1 } from './model/gauge-sub1'
export { default as FbecGaugeSub2 } from './model/gauge-sub2'
export { default as FbecScatterBase } from './model/scatter-base'
export { default as FbecFunnelBase } from './model/funnel-base'
export { default as FbecTimeline } from './model/timeline'
export { default as FbecSankeyBase } from './model/sankey-base'
export { default as FbecCalendarColor } from './model/calendar-color'
export { default as FbecGraphBase } from './model/graph-base'

// 需要插件
export { default as FbecLiquidFill } from './model/liquid-fill'
export { default as FbecWords } from './model/words'

// 需要插件3d
export { default as FbecPie3dBase } from './model/pie3d-base'
export { default as FbecPie3dCustomLegend } from './model/pie3d-custom-legend'

// 纯 dom
export { default as FbecTagsCloud } from './model/tags-cloud'

export { default as FbecMapGlBase } from './model/map-gl'
