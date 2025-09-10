<template>
	<!--  基础散点图  -->
	<fb-echarts
		ref="fb-ec"
		v-on="$listeners"
		:options="opt"
		:theme="theme"
		:initOptions="initOptions"
		:group="group"
		:autoresize="autoresize"
		:watchShallow="watchShallow"
		:manualUpdate="manualUpdate"
	>
	</fb-echarts>
</template>
<script>
import chartMixin from '../../ChartMixin'
import {merge} from "lodash-es";
import {linearColors} from "../../../utils";

export default {
	name: 'FbecScatterBase',
	mixins: [chartMixin],
	data() {
		return {
			opt: {
				tooltip: {},
				legend: {
					show: false,
					data: []
				},
				xAxis: {
					type: "value",
					data: []
				},
				yAxis: {
					type: "value",
					data: []
				},
				series: [],
			},
			ariaOpt: {
				aria: {
					enabled: true,
					decal: {
						show: true
					}
				}
			}
		}
	},
	methods: {
		updateOptions(val, option = {}) {
			this.opt.series = []
			this.opt.xAxis.data = []
			this.opt.yAxis.data = []
			this.opt = merge(this.opt, val)

			for (let i in this.opt.series) {
				let item = this.opt.series[i]
				if (!item.type) {
					item.type = 'scatter'
				}
				if (item.name) {
					this.opt.legend.data[i] = item.name
				}
			}
			this.chart.clear()
this.chart.setOption(this.opt)
		}
	}
}
</script>
