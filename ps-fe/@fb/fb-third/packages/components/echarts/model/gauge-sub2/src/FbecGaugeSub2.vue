<template>
    <!-- 分段仪表盘2  -->
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
    	name: 'FbecGaugeSub2',
        mixins: [chartMixin],
		watch: {
    		theme: {
    			immediate: true,
				handler(val) {
    				if (val === 'fbecLight') {
    					this.setThemeOpt({
							colors: {
								line1: "#D3D3D3",
								line4: "rgba(232, 232, 232, 1)"
							}
						})
					} else if (val === 'fbecDark') {
						this.setThemeOpt({
							colors: {
								line1: "rgba(47, 161, 254, 0.4)",
								line4: "rgba(255, 255, 255, 0.6)"
							}
						})
					}
				}
			}
		},
        data () {
			let dataObj = {
				name: "",
				value: 0
			}
			let radius = 101;
			let splitArea = [0.05, 0.1, 0.15, 0.2, 0.25, 0.3, 0.35, 0.4, 0.45, 0.5, 0.55, 0.6, 0.65, 0.7, 0.75, 0.8, 0.85, 0.9, 0.95, 1]
            return {
                opt: {
					series: [
						// 最外
						{
							type: 'gauge',
							radius: radius,
							splitNumber: 10,
							splitLine: {
								show: false
							},
							min: 0,
							max: 100,
							startAngle: 225,
							endAngle: -45,
							axisLabel: {
								show: false
							},
							axisLine: {
								show: true,
								lineStyle: {
									width: 2,
									shadowBlur: 0,
									color: splitArea.map(function (node) {
										return [node, 'rgba(0, 145, 255, 0.53)']
									})
								}
							},
							pointer: {
								show: 0
							},
							axisTick: {
								show: false
							},
							detail: {
								show: false
							}
						},
						// 最外 - 1
						{
							type: 'gauge',
							radius: radius - 7,
							splitNumber: 10,
							splitLine: {
								show: false
							},
							min: 0,
							max: 100,
							startAngle: 225,
							endAngle: -45,
							axisLabel: {
								show: false
							},
							axisLine: {
								show: true,
								lineStyle: {
									width: 1,
									shadowBlur: 0,
									color: splitArea.map(function (node) {
										return [node, "rgba(68, 215, 182, 0.5)"]
									})

								}

							},
							pointer: {
								show: 0
							},
							axisTick: {
								show: false
							},
							detail: {
								show: false
							}
						},
						// 扇形 数据展示数段
						{
							name: dataObj.name,
							type: 'gauge',
							radius: radius - 7,
							startAngle: 225,
							endAngle: -45,
							min: 0,
							max: 100,
							axisLine: {
								show: true,
								lineStyle: {
									width: 25,
									color: [
										[
											dataObj.value / 100,
											this.$echarts.graphic.LinearGradient(0, 1, 0, 0, [
												{
													offset: 0,
													color: 'rgba(69, 161, 255,0)',
												}, {
													offset: 0.3,
													color: 'rgba(69, 161, 255,0)',
												}, {
													offset: 1,
													color: 'rgba(0, 145, 255, 0.53)',
												}
											])
										], [
											1, 'rgba(0,0,0,0)'
										]
									],
								}
							},
							axisTick: {
								show: false,
							},
							splitLine: {
								show: 0,
							},
							axisLabel: {
								show: 0
							},
							pointer: {
								show: false,
								length: '100%'
							},
							detail: {
								show: true,
								valueAnimation: true,
								offsetCenter: [0, 0],
								fontSize: 32,
								formatter: [
									'{value}'
								].join('\n'),

							},
							title: {
								show: true,
								offsetCenter: [0, '78%'],
							},
							itemStyle: {
								color: 'rgba(28, 128, 245,.3)',
								borderColor: 'rgba(28, 128, 245,1)',
							},
							data: [
								{
									value: dataObj.value,
									name: dataObj.name,
								}
							]
						},
						// 数据色段
						{
							type: 'gauge',
							radius: radius - 10,
							startAngle: 225,
							endAngle: -45,
							min: 0,
							max: 100,
							axisLine: {
								show: true,
								lineStyle: {
									width: 4,
									shadowBlur: 0,
									color: [
										[0, 'transparent'], [0.1, '#0284FE'], [0.108, 'transparent'], [0.2, '#0284FE'],
										[0.208, 'transparent'], [0.3, '#F0EC00'], [0.308, 'transparent'], [0.4, '#F0EC00'],
										[0.408, 'transparent'], [0.5, '#FE7802'], [0.508, 'transparent'], [0.6, '#FE7802'],
										[0.608, 'transparent'], [0.7, '#FD201C'], [0.708, 'transparent'], [0.8, '#FD201C'],
										[0.808, 'transparent'], [0.9, '#FD201C'], [0.908, 'transparent'], [1, '#FD201C']
									],
								}
							},
							axisTick: {
								show: false,
							},
							splitLine: {
								show: 0,
							},
							axisLabel: {
								show: 0
							},
							pointer: {
								show: false,
							},
							detail: {
								show: false,
							},
							data: [
								{
									show: false,
								}
							]
						},
						// 齿
						{
							type: 'gauge',
							radius: radius - 25,
							splitNumber: 20,
							min: 0,
							max: 100,
							startAngle: 225,
							endAngle: -45,
							axisLabel: {
								show: false
							},
							axisLine: {
								show: false,
							},
							axisTick: {
								show: true,
								distance: -16,
								length: 4,
								lineStyle: {
									width: 1,
									color: "rgba(255, 255, 255, 0.5)"
								}
							},
							splitLine: {
								show: false,
							},
							pointer: {
								show: false
							},
							detail: {
								show: false
							},
							title: {
								offsetCenter: [0, '128%'],
							},
							data: [
								{
									name: '',
								}
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

				this.opt.series[2].data[0] = val
				this.opt.series[2].name = val.name
				this.opt.series[2].axisLine.lineStyle.color[0][0] = val.value / 100

				if (val.timeText) {
					this.opt.series[4].data[0].name = val.timeText
				}

				if (val.radius) {
					this.opt.series[0].radius = option.radius
					this.opt.series[1].radius = option.radius - 7
					this.opt.series[2].radius = option.radius - 7
					this.opt.series[3].radius = option.radius - 10
					this.opt.series[4].radius = option.radius - 20
				}
				if (val.splitColors) {
					this.opt.series[3].axisLine.lineStyle.color = val.splitColors
				}

				this.chart.clear()
this.chart.setOption(this.opt)
            },
			/**
			 * 主题设置颜色
			 * @param option 选项
			 * **/
			setThemeOpt(option = {}) {
				let colors = option.colors || {}
				this.opt.series[0].axisLine.lineStyle.color = [[1, colors.line1]]
				this.opt.series[1].axisLine.lineStyle.color = [[1, colors.line1]]
				this.opt.series[4].axisTick.lineStyle.color = colors.line4
			}
        }
    }
</script>
