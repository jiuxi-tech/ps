<template>
    <!-- 基础仪表盘1  -->
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
    	name: 'FbecGaugeBase1',
        mixins: [chartMixin],
        data () {
            return {
                opt: {
					series: [
						{
							type: 'gauge',
							progress: {
								show: true,
								width: 10
							},
							axisLine: {
								lineStyle: {
									width: 10,
								}
							},
							axisTick: {
								show: false
							},
							splitLine: {
								distance: 0,
								length: 8,
								lineStyle: {
									width: 1,
								}
							},
							axisLabel: {
								distance: 13,
								fontSize: 16
							},
							// anchor: {
							// 	show: true,
							// 	showAbove: true,
							// 	size: 22,
							// 	itemStyle: {
							// 		color: 'transparnt',
							// 		borderWidth: 4
							// 	}
							// },
							title: {
								offsetCenter: [0, '70%']
							},
							detail: {
								valueAnimation: true,
								offsetCenter: [0, '100%']
							},
							data: [
								// {
								// 	name: '隐患整改率',
								// 	value: 70
								// }
							]
						}
					]
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
				// 合并 系列下标 0
				this.opt.series[0] = merge(this.opt.series[0], val)

				for (let i in this.opt.series) {
					let item = this.opt.series[i]
					if (!item.type) {
						item.type = 'gauge'
					}
				}
				this.chart.clear()
this.chart.setOption(this.opt)
            }
        }
    }
</script>
