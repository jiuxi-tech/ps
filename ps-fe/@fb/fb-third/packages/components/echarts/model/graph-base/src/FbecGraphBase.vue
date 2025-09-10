<template>
    <!--  基础关系图  -->
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
    	name: 'FbecGraphBase',
        mixins: [chartMixin],
        data () {
            return {
                opt: {
                    tooltip: {},
                    legend: {
                        top: '3%',
                        left: 'center',
                        data: [],
                        show: false
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
				this.opt = merge(this.opt, val)
				for (let i in this.opt.series) {
					let item = this.opt.series[i]
					if (!item.type) {
						item.type = 'graph'
					}
				}
				console.log(this.opt)
				this.chart.clear()
this.chart.setOption(this.opt)
            }
        }
    }
</script>
