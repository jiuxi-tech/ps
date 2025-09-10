<template>
	<!--  基础矩形图  -->
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
		name: 'FbecTreemapBase',
		mixins: [chartMixin],
		data() {
			return {
				opt: {
					legend: {
						show: false,
						data: []
					},
					tooltip: {
						trigger: 'item',
						confine: true
					},
					series: [{
						type: 'treemap',
						itemStyle: {
							borderColor: '#fff',
							gapWidth: 1
						},
					}]
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
				this.opt.legend.data = []
				this.opt = merge(this.opt, val)
				for (let i in this.opt.series) {
					let item = this.opt.series[i]
					if (!item.type) {
						item.type = 'treemap'
					}

					if (item.data.length > 0) {
						for(let j in item.data) {
							let son = item.data[j]
							this.opt.legend.data.push(son.name)
						}
					}
				}

				this.chart.clear()
this.chart.setOption(this.opt)
			}
		}
	}
</script>
