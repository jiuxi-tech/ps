<template>
    <!--  基础饼图  -->
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
    	name: 'FbecPieBase',
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
						trigger: 'item',
						formatter: '{b}，{c}，{d}%',
					},
                    legend: {
                        top: '3%',
                        left: 'center',
                        show: false
                    },

					series: [
						// {
						// 	type: 'pie',
						// 	radius: '50%',
						// }
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
				this.opt.series = []
				this.opt = merge(this.opt, val)
				for (let i in this.opt.series) {
					let item = this.opt.series[i]
					if (!item.type) {
						item.type = 'pie'
						item.radius = '50%'
					}
				}
				this.chart.clear()
this.chart.setOption(this.opt)
            }
        }
    }
</script>
