<template>
    <!--  左右布局横向柱状图  -->
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
    	name: 'FbecBarHorizontalAbout',
        mixins: [chartMixin],
        data () {
            return {
                opt: {
					legend: {
						data: []
					},
					grid: [{
						show: false,
						left: '5%',
						top: '10%',
						bottom: '8%',
						containLabel: true,
						width: '37%'
					}, {
						show: false,
						left: '53%',
						top: '10%',
						bottom: '8%',
						width: '0%'
					}, {
						show: false,
						right: '2%',
						top: '10%',
						bottom: '8%',
						containLabel: true,
						width: '37%'
					}],
					xAxis: [{
						type: 'value',
						inverse: true,
						axisLine: {
							show: false
						},
						axisTick: {
							show: false
						},
						position: 'top',
						axisLabel: {
							show: false,
						},
						splitLine:{
							show: false,
						},
					}, {
						gridIndex: 1,
						show: false
					}, {
						gridIndex: 2,
						axisLine: {
							show: false
						},
						axisTick: {
							show: false
						},
						position: 'top',
						axisLabel: {
							show: false,
						},
						splitLine:{
							show: false,
						},
					}],
					yAxis: [{
						type: 'category',
						inverse: true,
						position: 'right',
						axisLine: {
							show: false,
						},

						axisTick: {
							show: false
						},
						axisLabel: {
							show: false
						},
					},
						{
							gridIndex: 1,
							type: 'category',
							inverse: true,
							position: 'left',
							axisLine: {
								show: false
							},
							axisTick: {
								show: false
							},
							axisLabel: {
								show: true,
								padding:[30,0,0,0],
                                fontSize: 16,
								align: "center"
							},
							data: []
						},
						{
							gridIndex: 2,
							type: 'category',
							inverse: true,
							position: 'left',
							axisLine: {
								show: false,
							},
							axisTick: {
								show: false
							},
							axisLabel: {
								show: false
							},
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
            updateOptions (val, option = {}) {
				this.opt.series = []
				this.opt.yAxis[1].data = []
				this.opt = merge(this.opt, val)

				for (let i in this.opt.series) {
					let item = this.opt.series[i]
					if (!item.type) {
						item.type = 'bar'
					}
					if (!item.label) {
						item.label = {
							show: true,
							position: 'right'
						}
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
