<template>
    <!--  基础雷达图  -->
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
    	name: 'FbecRadarBase',
        mixins: [chartMixin],
        data () {
            return {
                opt: {
					tooltip: {
						trigger: 'item',
						confine: true
					},
					legend: {
						show: false,
						data: [],
					},
					// radar: {
					// 	// shape: 'circle',
					// 	// indicator: [
					// 	// 	{ name: '', max: 100},
					// 	// 	{ name: '', max: 100},
					// 	// 	{ name: '', max: 100},
					// 	// ],
					// },
					series: [{
						name: '',
						type: 'radar',
						symbolSize: 4,
						itemStyle: {
							color: 'rgba(7, 232, 174, 1)',
							borderWidth: 0.5,
						},
						lineStyle: {
							color: 'rgba(7, 232, 174, 1)'
						},
						areaStyle: {
							color: 'rgba(7, 232, 174, 1)'
						},
						data: [
							// {
							// 	value: [4200, 3000, 20000, 35000, 50000, 18000],
							// 	name: '预算分配'
							// },
						]
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
            updateOptions (val, option = {}) {
				if (this.opt.radar && this.opt.radar.indicator) {
					this.opt.radar.indicator = []
				}

				this.opt = merge(this.opt, val)
				for (let i in this.opt.series) {
					let item = this.opt.series[i]
					if (!item.type) {
						item.type = 'radar'
					}
					if (item.data && item.data.length > 0) {
						item.data.forEach((dataItem, dataIndex) => {
							this.opt.legend.data[dataIndex] = dataItem.name
						})
					}
				}
				this.chart.clear()
				this.chart.setOption(this.opt)
            }
        }
    }
</script>
