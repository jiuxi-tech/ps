<template>
    <!--  02-桑基图  -->
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
    	name: 'FbecSankeyBase',
        mixins: [chartMixin],
        data () {
            return {
                opt: {
					tooltip: {
						trigger: 'item',
						triggerOn: 'mousemove'
					},
                    legend: {
                        top: '3%',
                        left: 'center',
                        data: [],
                        show: false
                    },
					series: [
						{
							type: 'sankey',
							data: [
								// {name: "分类1"},
								// {name: '分类2'},
								// {name: '分类3'},
							],
							links: [
								// {source: "分类1", target: '分类2', value: 124.729},
								// {source: "分类1", target: '分类3', value: 124.729},
							],
							emphasis: {
								focus: 'adjacency'
							},
							lineStyle: {
								color: 'target',
								curveness: 0.5
							}
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
						item.type = 'sankey'
					}
					// if (item.name) {
					// 	this.opt.legend.data[i] = item.name
					// }
				}
				this.chart.clear()
this.chart.setOption(this.opt)
            }
        }
    }
</script>
