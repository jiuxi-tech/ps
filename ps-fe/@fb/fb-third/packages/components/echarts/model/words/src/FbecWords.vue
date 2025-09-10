<template>
    <!--  插件-词云  -->
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
	import 'echarts-wordcloud'
    import chartMixin from '../../ChartMixin'
	import {merge} from "lodash-es";

	export default {
    	name: 'FbecWords',
        mixins: [chartMixin],
        data () {
			const colorList = [
				'#3a96f5',
				'#F1BF10',
				'#E38712',
				'#347D3F',
				'#add2f8',
				'#A35D16',
				'#1acaff',
				'#ffde00',
				'#89fda5'
			]
            return {
                opt: {
					tooltip: {
						show: true,
					},
					series: [
						{
							name: '热点分析',
							type: 'wordCloud',
							//shape: 'circle',
							width: '100%',
							height: '90%',
							sizeRange: [12, 28],
							rotationRange: [0, 0],
							// set to true to allow word being draw partly outside of the canvas.
							// Allow word bigger than the size of the canvas to be drawn
							drawOutOfBound: true,
							// If perform layout animation.
							// NOTE disable it will lead to UI blocking when there is lots of words.
							layoutAnimation: true,
							gridSize: 14,
							textStyle: {
								color: function() {
									let index = Math.floor(Math.random() * colorList.length);
									return colorList[index];
									// return 'rgb(' + [
									//     Math.round(Math.random() * 160),
									//     Math.round(Math.random() * 160),
									//     Math.round(Math.random() * 160)
									// ].join(',') + ')';
								},
							},
							emphasis: {
								// focus: 'self',
								textStyle: {
									shadowBlur: 10,
									shadowColor: '#333'
								}
							},
							data: []
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
				this.opt = merge(this.opt, val)
				for (let i in this.opt.series) {
					let item = this.opt.series[i]
					if (!item.type) {
						item.type = 'wordCloud'
					}
				}
				this.chart.clear()
this.chart.setOption(this.opt)
            }
        }
    }
</script>
