<template>
    <!--  极坐标 3/4 柱状图  -->
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

    const color = [
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
	]
	export default {
    	name: 'FbecBarThreeQuarter',
        mixins: [chartMixin],
        data () {
            return {
                opt: {
					angleAxis: {
						show: 0,
						clockwise: false,
						max: 100,
					},
					radiusAxis: {
						type: 'category',
						data: [],
						z: 10,
						axisLine: {
							show: false,
						},
						axisTick: {
							show: false,
						},
						axisLabel: {
							interval: 0,
							align: 'left',
							margin: -15,
						},
					},
					polar: {
						show: 0,
						center: ['50%', '50%'],
					},
					series: [
						{
							type: 'bar', // 背景
							data: [],
							barWidth: 12,
							itemStyle: {
								color: 'rgba(47, 161, 254, 0.4)'
							},
							roundCap: true,
							coordinateSystem: 'polar',
						},
						{
							type: 'bar',
							data: [],
							barWidth: 12,
							roundCap: true,
							coordinateSystem: 'polar',
							barGap: '-100%',
						},

					],
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
				this.opt = merge(this.opt, val)
				if (val.customValue && val.customValue.data) {
					let data = val.customValue.data
					let max = Math.max(...data.map((item) => item.value));
					this.opt.angleAxis.max = max + max / 3; // 3/4 坐标系
					this.opt.radiusAxis.data = data.map((item) => { // 类名层
						return item.name + ':' + '　' + item.value;
					})
					this.opt.series[0].data = data.map(() => { // 背景层
						return max
					})
					this.opt.series[1].data = data.map((item, index) => { // 数据层
						return {
							value: item.value,
							name: item.name,
							itemStyle: {
								color: color[index]
							}
						}
					})
				}
				this.chart.clear()
this.chart.setOption(this.opt)
            }
        }
    }
</script>
