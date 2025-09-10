// import FbecLineBase from './model/line-base/src/FbecLineBase'
// import FbecLineLinear from './model/line-linear/src/FbecLineLinear'
// import FbecBarBase from './model/bar-base/src/FbecBarBase'
// import FbecBarHorizontal from './model/bar-horizontal/src/FbecBarHorizontal'
// import FbecBarHorizontalAbout from './model/bar-horizontal-about/src/FbecBarHorizontalAbout'
// import FbecBarThreeQuarter from './model/bar-three-quarter/src/FbecBarThreeQuarter'
// import FbecBarBaseTwoy from './model/bar-base-twoy/src/FbecBarBaseTwoy'
// import FbecPieBase from './model/pie-base/src/FbecPieBase'
// import FbecPieRing from './model/pie-ring/src/FbecPieRing'
// import FbecPieBar from './model/pie-bar/src/FbecPieBar'
// import FbecPieCustomLegend from './model/pie-custom-legend/src/FbecPieCustomLegend'
// import FbecRadarBase from './model/radar-base'
// import FbecTreemapBase from './model/treemap-base/src/FbecTreemapBase'
// import FbecMapBase from './model/map-base/src/FbecMapBase'
// import FbecGaugeBase1 from './model/gauge-base1/src/FbecGaugeBase1'
// import FbecGaugeBase2 from './model/gauge-base2/src/FbecGaugeBase2'
// import FbecGaugeSub1 from './model/gauge-sub1/src/FbecGaugeSub1'
// import FbecGaugeSub2 from './model/gauge-sub2/src/FbecGaugeSub2'
// import FbecScatterBase from './model/scatter-base/src/FbecScatterBase'
// import FbecFunnelBase from './model/funnel-base/src/FbecFunnelBase'
// import FbecTimeline from './model/timeline/src/FbecTimeline'
// import FbecSankeyBase from './model/sankey-base/src/FbecSankeyBase'
// import FbecCalendarColor from './model/calendar-color/src/FbecCalendarColor'
// import FbecGraphBase from './model/graph-base/src/FbecGraphBase'

// 需要插件
// import FbecLiquidFill from './model/liquid-fill/src/FbecLiquidFill'
// import FbecWords from './model/words/src/FbecWords'

// 主题 json
import fbecDark from "./theme/fbecDark.json"
import fbecLight from "./theme/fbecLight.json"

let themes = {
	fbecDark: fbecDark,
	fbecLight: fbecLight,
}

/**
 * 初始化自定义主题
 * @param {Object} echarts
 * @param {Object} opts
 */
export function initThemes(echarts, opts = {}) {
	for (let key in themes) {

		let obj = themes[key]
		echarts.registerTheme(key, obj)
	}

	// echarts 主题外引入初始配置
	if (opts.echarts && opts.echarts.theme && opts.echarts.theme.registers) {
		for (let key in opts.echarts.theme.registers) {
			let obj = opts.echarts.theme.registers[key]
			echarts.registerTheme(key, obj)
		}
	}
}
// const components = [
// 	FbecLineBase,
// 	FbecLineLinear,
// 	FbecBarBase,
// 	FbecBarHorizontal,
// 	FbecBarHorizontalAbout,
// 	FbecBarThreeQuarter,
// 	FbecBarBaseTwoy,
// 	FbecPieBase,
// 	FbecPieRing,
// 	FbecPieBar,
// 	FbecPieCustomLegend,
// 	FbecRadarBase,
// 	FbecTreemapBase,
// 	FbecMapBase,
// 	FbecGaugeBase1,
// 	FbecGaugeBase2,
// 	FbecGaugeSub1,
// 	FbecGaugeSub2,
// 	FbecScatterBase,
// 	FbecFunnelBase,
// 	FbecWords,
// 	FbecLiquidFill,
// 	FbecTimeline,
// 	FbecSankeyBase,
// 	FbecCalendarColor,
// 	FbecGraphBase
// ]


// export const ecInstall = components.map(comp => {
// 	comp.install = function(Vue) {
// 		Vue.component(comp.name, comp);
// 	}
// })
