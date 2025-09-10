<template>
    <!-- 基础仪表盘2  -->
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
    	name: 'FbecGaugeBase2',
        mixins: [chartMixin],
        data () {
            return {
                opt: {
					series: [
						{
							type: 'gauge',
							radius: '60%',
							startAngle: 240,
							endAngle: -60,
							splitNumber: 12,
							itemStyle: {
								color: linearColors.y.green
							},
							progress: {
								show: true,
								width: 20
							},
							pointer: {
								show: false
							},
							axisLine: {
								lineStyle: {
									width: 20,
								}
							},
							axisTick: {
								distance: -45,
								splitNumber: 5,
								lineStyle: {
									width: 2,
								}
							},
							splitLine: {
								distance: -52,
								length: 14,
								lineStyle: {
									width: 2,
								}
							},
							axisLabel: {
								show: false,
							},
							anchor: {
								show: false
							},
							title: {
								show: true,
								offsetCenter: [0, '-20%'],
							},
							detail: {
								valueAnimation: true,
								offsetCenter: [0, '28%'],
								fontSize: 48,
								fontWeight: 'bolder',
							},
							data: [
								// {
								// 	name: '安全分数',
								// 	value: 20
								// }
							]
						},
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
