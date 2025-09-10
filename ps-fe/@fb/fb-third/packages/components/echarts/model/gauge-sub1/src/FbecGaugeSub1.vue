<template>
    <!-- 分段仪表盘1  -->
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
    	name: 'FbecGaugeSub1',
        mixins: [chartMixin],
        data () {
            return {
                opt: {
					series: [
						{
							type: 'gauge',
							axisLine: {
								lineStyle: {
									width: 20,
									color: [
										[0.2, '#FB544E'],
										[0.8, '#FDCA30'],
										[1, '#2FA1FE']
									]
								}
							},
							pointer: {
								itemStyle: {
									color: 'auto'
								}
							},
							axisTick: {
								show: false
							},
							splitLine: {
								distance: 0,
								length: 8,
								lineStyle: {
									width: 2
								}
							},
							axisLabel: {
								distance: 30,
								fontSize: 16
							},
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
