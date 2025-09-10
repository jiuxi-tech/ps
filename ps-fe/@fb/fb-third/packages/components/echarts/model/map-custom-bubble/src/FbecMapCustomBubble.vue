<template>
    <!--  基础地图 + 定制气泡  -->
    <fb-echarts
			ref="fb-ec"
            :options="opt"
            :theme="theme"
            :initOptions="initOptions"
            :group="group"
            :autoresize="autoresize"
            :watchShallow="watchShallow"
            :manualUpdate="manualUpdate"
			@georoam="georoam"
			@click="handleClick"
			@dblclick="handleClickDB"
    >
    </fb-echarts>
</template>
<script>
    import chartMixin from '../../ChartMixin'
	import {merge} from "lodash-es";

	export default {
		name: 'FbecMapCustomBubble',
		mixins: [chartMixin],
		props: {
			mapJson: {
				type: Object,
				default: () => {}
			},
			// echarts.registerMap 命名
			mapName: {
				type: String,
				default: 'map_custom_bubble'
			}
		},
		watch: {
			options: {
				deep: true,
				handler () {
					this.init()
				}
			},
			mapJson: {
				deep: true,
				handler () {
					this.init()
				}
			},
			aria (newVal) {
				this.ariaOpt.aria.enabled = newVal
				// this.ariaOpt.aria.decal.show = newVal
				this.opt = merge(this.opt, this.ariaOpt)
			}
		},
		data () {
			return {
				opt: {
					tooltip: {
						show: false,
						trigger: 'item',
						position: 'top'
					},
					geo: {
						map: this.mapName,
						roam: false,
						aspectScale: 0.95,
						// center: [120.42800914950831, 28.86832042071385],
						show: true,
						label:{
							show: false
						},
						itemStyle: {
							areaColor: 'rgba(2, 132, 254, 1)',
							borderWidth: 0,
							shadowColor: 'rgba(30, 196, 255, 0.5)',
							shadowOffsetX: 0,
							shadowOffsetY: 25
						},
						emphasis: {
							label: {
								show: false
							}
						}
					},
					series: [
						{
							type: 'map',
							//	geoIndex: 0,
							roam: false,
							aspectScale: 0.95,
							map: this.mapName, // 自定义扩展图表类型
							label: {
								show: false,
								color: '#FFFFFF',
								fontSize: 14,
								backgroundColor: 'rgba(9, 32, 76, 0.7)',
								borderRadius: 6,
								padding: [5, 6],
							},
							itemStyle: {
								areaColor: 'rgba(2, 132, 254, 1)',
								borderWidth: 2,
								borderColor: 'rgba(82, 255, 253, 1)',
							},
							emphasis: {
//							itemStyle: {
//								areaColor: 'rgba(253, 210, 72, 0.7)',
//							},
								label: {
									show: false,
									color: '#FFFFFF',
									fontSize: 14,
									backgroundColor: 'rgba(9, 32, 76, 0.7)',
									borderRadius: 6,
									padding: [5, 6],
								},
							},
							selectedMode: false,
							select: {
								label: {
									show: true,
									color: '#FFFFFF',
								},
							},
							data: [],
						},
						{
							name: '气泡',
							type: 'scatter',
							coordinateSystem: 'geo',
							symbol: 'image://data:image/png;base64,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',
							// symbol: 'pin',
							symbolSize: [88, 80],
							symbolOffset: [0, -30],
							label: {
								show: true,
								color: '#fff',
								fontSize: 16,
								fontWeight: 'bold',
								formatter: function (node) {
									return [
										`{a|${node.value[2]}}`,
										`{b|${node.name}}`
									].join('\n')
								},

								rich: {
									a: {
										color: '#fff',
										fontSize: 16,
										fontWeight: 'bold',
										align: 'center',
										verticalAlign: 'middle',
										height: 50,
										padding: [24, 0, 0, 0],
									},
									b: {
										color: '#FFFFFF',
										fontSize: 14,
										backgroundColor: 'rgba(9, 32, 76, 0.7)',
										borderRadius: 6,
										padding: [5, 6],
									},
								}
							},
							itemStyle: {
								opacity: 1,
							},
							zlevel: 6,
							data: [],
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
				},
				showSymbol: true,
				click_type: 'click'
			}
		},
		created () {
			this.$echarts.registerMap(this.mapName, this.mapJson)
		},
		mounted () {

		},
		methods: {
			init () {
				if (JSON.stringify(this.mapJson) !== '{}') {
					this.$echarts.registerMap(this.mapName, this.mapJson)
				}
				// 开启无障碍花纹
				if (this.aria) {
					this.opt = merge(this.opt, this.ariaOpt)
				}
				// 合并数据
				if (this.options) {
					this.opt = merge(this.opt, this.options)
				}
				this.chart = this.$refs['fb-ec'].chart
				// this.$refs['fb-ec'].chart.setOption(this.opt)
			},
			updateOptions (val, option = {showSymbol: true}) {
				this.opt = merge(this.opt, val)
				// this.showSymbol = option.showSymbol

				// map数据
				this.opt.series[0].data = val.data.map(n => {
					return n
				})
				// 气泡数据
				this.opt.series[1].data = val.data.map((node) => {
					return node
				})

				if (option.clear !== false) {
					this.chart.clear()
				}
				this.chart.setOption({}, true)
				this.chart.setOption(this.opt)

				// // 标记圆点数据
				// this.opt.series[2].data = val.data.map((node) => {
				// 	return {
				// 		value: node.value,
				// 		name: node.name,
				// 		symbolSize: !this.showSymbol ? 0 : 9,
				// 	}
				// })
			},
			handleClick (params) {
				this.click_type = 'click'
				setTimeout(() => {
					if (this.click_type === 'dblclick') return
					this.$emit('click', params)
				}, 200);
			},
			handleClickDB (params) {
				this.click_type = 'dblclick'
				this.$emit('dblclick', params)
			},
			handleMouseover(val) {
				if (this.showSymbol) return
				// if (val.seriesType == 'map') {
				//     this.opt.series[1].data[val.dataIndex].label.show = true
				//     this.opt.series[1].data[val.dataIndex].symbolSize = 75
				//     this.opt.series[2].data[val.dataIndex].symbolSize = 9
				// }
				if (val.seriesType == 'map') {
					this.opt.series[0].data[val.dataIndex].label.show = true
				}
			},
			handleMouseout(val) {
				if (this.showSymbol) return
				// if (val.seriesType == 'map') {
				//     this.opt.series[1].data[val.dataIndex].label.show = false
				//     this.opt.series[1].data[val.dataIndex].symbolSize = 0
				//     this.opt.series[2].data[val.dataIndex].symbolSize = 0
				// }
				if (val.seriesType == 'map') {
					this.opt.series[0].data[val.dataIndex].label.show = false
				}
			},
			georoam(params) {
				let chart = this.chart
				var option = chart.getOption();//获得option对象

				if (params.zoom != null && params.zoom != undefined) { //捕捉到缩放时

					option.geo[0].zoom = option.series[0].zoom;//下层geo的缩放等级跟着上层的geo一起改变

					option.geo[0].center = option.series[0].center;//下层的geo的中心位置随着上层geo一起改变

				} else {//捕捉到拖曳时

					option.geo[0].center = option.series[0].center;//下层的geo的中心位置随着上层geo一起改变

				}

				chart.setOption(option);//设置option
			}
		}
	}
</script>
