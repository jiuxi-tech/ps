<template>
	<!--  基础线  -->
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

	export default {
		name: 'FbecLineBase',
		mixins: [chartMixin],
		data() {
			return {
				opt: {
					tooltip: {
						trigger: 'axis',
						axisPointer: {
							type: 'shadow'
						}
					},
					legend: {
						top: '3%',
						left: 'center',
						data: [],
						show: false
					},
					grid: {
						top: '16%',
						left: '3%',
						right: '5%',
						bottom: '13%',
						containLabel: true
					},
					xAxis: {
						type: 'category',
						boundaryGap: false,
						data: []
					},
					yAxis: [{
						type: 'value',
						scale: true,
						splitNumber: 3,
						axisTick: {show: false},
					}],
					series: []
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

				this.opt = merge(this.opt, val)
				for (let i in this.opt.series) {
					let item = this.opt.series[i]
					if (!item.type) {
						item.type = 'line'
					}
					if (item.name) {
						this.opt.legend.data[i] = item.name
					}
				}

				if (option.clear !== false) {
					this.chart.clear()
				}
				this.chart.setOption(this.opt)
			}
		}
	}
</script>
