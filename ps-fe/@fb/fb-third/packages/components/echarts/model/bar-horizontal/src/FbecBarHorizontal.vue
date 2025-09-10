<template>
    <!--  基础柱状图  -->
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
    import {linearColors} from "../../../utils";
	import {merge} from "lodash-es";

	export default {
    	name: 'FbecBarHorizontal',
        mixins: [chartMixin],
        data () {
            return {
                opt: {
                	color: [
						linearColors.y.blue,
						linearColors.y.green,
						linearColors.y.yellow,
						linearColors.y.red,
						linearColors.y.geekBlue,
						linearColors.y.purple,
						linearColors.y.orange,
						linearColors.y.cyan,
						linearColors.y.pink,
						linearColors.y.grey,
					],
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
						type: 'value',
						show: false
                    },
                    yAxis: {
						type: 'category',
						boundaryGap: true,
						data: []
					},
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
				this.opt.yAxis.data = []
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
