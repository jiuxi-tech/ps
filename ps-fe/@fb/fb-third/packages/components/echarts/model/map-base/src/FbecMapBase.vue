<template>
    <!--  基础地图  -->
    <fb-echarts
			ref="fb-ec"
            :options="opt"
            :theme="theme"
            :initOptions="initOptions"
            :group="group"
            :autoresize="autoresize"
            :watchShallow="watchShallow"
            :manualUpdate="manualUpdate"
			@click="handleClick"
			@dblclick="handleClickDB"
    >
    </fb-echarts>
</template>
<script>
    import chartMixin from '../../ChartMixin'
	import {merge} from "lodash-es";
	let mapName = 'zj' // echarts.registerMap 命名

	export default {
		name: 'FbecMapBase',
		mixins: [chartMixin],
		props: {
			mapJson: {
				type: Object,
				default: () => {}
			},
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
						trigger: 'item',
						formatter (item) {
							return `${item.name} - ${item.data ? item.data.value[2] : ''}`
						},
					},
					geo: {
						map: mapName,
						roam: false,
						aspectScale: 0.95,
						// center: [120.42800914950831, 28.86832042071385],
						show: false,
						label:{
							show: true
						}
					},
					series: [
						{
							type: 'map',
							//	geoIndex: 0,
							roam: false,
							aspectScale: 0.95,
							map: mapName, // 自定义扩展图表类型
							label: {
								show: true,
								color: '#FFFFFF',
								fontSize: 14,
								backgroundColor: '#1E1BB6',
								borderRadius: 6,
								padding: [5, 6],
							},
							emphasis: {
								itemStyle: {
									areaColor: '#77D86E',
								},
								label: {
									show: true,
									color: '#FFFFFF',
									fontSize: 14,
									backgroundColor: '#1E1BB6',
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
								itemStyle: {
									areaColor: '#77D86E',
								},
							},
							data: [],
						},
						{
							name: '气泡',
							type: 'scatter',
							coordinateSystem: 'geo',
							symbol: 'pin',
							symbolSize: 75,
							symbolOffset: [0, -20],
							label: {
								show: true,
								color: '#1E1BB6',
								fontSize: 16,
								fontWeight: 'bold',
								formatter: function (node) {
									return node.value[2]
								},
							},
							itemStyle: {
								color: '#52FFFD',
							},
							zlevel: 6,
							data: [],
						},
						{
							name: '标记圆点',
							type: 'scatter',
							coordinateSystem: 'geo',
							symbol: 'circle',
							symbolSize: 9,
							symbolOffset: [0, -20],
							label: {
								show: false,
								color: '#FFFFFF',
								fontSize: 14,
								backgroundColor: '#1E1BB6',
								borderRadius: 6,
								padding: [5, 6],
								offset: [0, 30],
								formatter: '{b}'
							},
							itemStyle: {
								color: '#1E1BB6',
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
			this.$echarts.registerMap(mapName, this.mapJson)
		},
		mounted () {

		},
		methods: {
			init () {
				if (JSON.stringify(this.mapJson) !== '{}') {
					this.$echarts.registerMap(mapName, this.mapJson)
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
					n.itemStyle = {
						areaColor: n.areaColor,
					}
					n.label = {
						show: !this.showSymbol ? false : true
					}
					return n
				})
				if (option.clear !== false) {
					this.chart.clear()
				}
				this.chart.setOption({}, true)
				this.chart.setOption(this.opt)

				// // 气泡数据
				// this.opt.series[1].data = val.data.map((node) => {
				// 	return {
				// 		value: node.value,
				// 		name: node.name,
				// 		symbolSize: !this.showSymbol ? 0 : 75,
				// 		label: {
				// 			show: !this.showSymbol ? false : true
				// 		},
				// 	}
				// })
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
			}
		}
	}
</script>
