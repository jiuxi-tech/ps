<template>
    <!--  基础漏斗图  -->
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
    	name: 'FbecFunnelBase',
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
						formatter: '{b} : {c}%'
					},
					legend: {
						data: []
					},
					series: [
						{
							type: 'funnel',
							left: '10%',
							width: '80%',
							maxSize: '80%',
							label: {
								position: 'inside',
								formatter: '{b}: {c}%',
							},
							emphasis: {
								label: {
									position: 'inside',
									formatter: '{b}Actual: {c}%'
								}
							},
							data: [
								// { value: 30, name: 'Visit' },
								// { value: 10, name: 'Inquiry' },
								// { value: 5, name: 'Order' },
								// { value: 50, name: 'Click' },
								// { value: 80, name: 'Show' }
							],
							// Ensure outer shape will not be over inner shape when hover.
							z: 100
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
				this.opt.series = []
				this.opt = merge(this.opt, val)
				for (let i in this.opt.series) {
					let item = this.opt.series[i]
					if (!item.type) {
						item.type = 'funnel'
					}
					if (item.data && item.data.length > 0) {
						item.data.forEach((dItem, dIndex) => {
							this.opt.legend.data[dIndex] = dItem.name || ''
						})
					}
				}
				this.chart.clear()
this.chart.setOption(this.opt)
            }
        }
    }
</script>
