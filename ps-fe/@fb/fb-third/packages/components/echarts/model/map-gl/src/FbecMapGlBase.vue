<!--  基础地图  -->
<template>
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
import { merge } from 'lodash-es'

let mapName = 'zj' // echarts.registerMap 命名

export default {
	name: 'FbecMapGlBase',
	mixins: [chartMixin],
	props: {
		mapJson: {
			type: Object,
			default: () => {
			},
		},
	},
	watch: {
		options: {
			deep: true,
			handler () {
				this.init()
			},
		},
		mapJson: {
			deep: true,
			handler () {
				this.init()
			},
		},
		aria (newVal) {
			this.ariaOpt.aria.enabled = newVal
			// this.ariaOpt.aria.decal.show = newVal
			this.opt = merge(this.opt, this.ariaOpt)
		},
	},
	data () {

		console.log(mapName)

		return {
//			ariaOpt: {
//				aria: {
//					enabled: true,
//					decal: {
//						show: true,
//					},
//				},
//			},
			//showSymbol: true,
			click_type: 'click',
			opt: {
				backgroundColor: 'transparent',
				geo3D: {
					map: mapName,
					shading: 'lambert',
					environment: '#000',
//						new this.$echarts.graphic.LinearGradient(0, 0, 0, 1, [{
//						offset: 0, color: '#00aaff' // 天空颜色
//					}, {
//						offset: 0.7, color: '#998866' // 地面颜色
//					}, {
//						offset: 1, color: '#998866' // 地面颜色
//					}], false),

					light: {
						main: {
							intensity: 5,
							shadow: true,
							shadowQuality: 'high',
							alpha: 30,
						},
						ambient: {
							intensity: 0,
						},
//						ambientCubemap: {
//							texture: 'data-gl/asset/canyon.hdr',
//							exposure: 1,
//							diffuseIntensity: 0.5,
//						},
					},
					viewControl: {
						distance: 50,
						panMouseButton: 'left',
						rotateMouseButton: 'right',
					},
					groundPlane: {
						show: true,
						color: 'red',
					},
					postEffect: {
						enable: true,
						bloom: {
							enable: false,
						},
						SSAO: {
							radius: 1,
							intensity: 1,
							enable: true,
						},
						depthOfField: {
							enable: false,
							focalRange: 10,
							blurRadius: 10,
							fstop: 1,
						},
					},
					temporalSuperSampling: {
						enable: true,
					},
					itemStyle: {},
					regionHeight: 2,
				},
//				visualMap: {
//					max: 40,
//					calculable: true,
//					realtime: false,
//					inRange: {
//						color: [
//							'#313695',
//							'#4575b4',
//							'#74add1',
//							'#abd9e9',
//							'#e0f3f8',
//							'#ffffbf',
//							'#fee090',
//							'#fdae61',
//							'#f46d43',
//							'#d73027',
//							'#a50026',
//						],
//					},
//					outOfRange: {
//						colorAlpha: 0,
//					},
//				},
				series: [
//					{
//						type: 'bar3D',
//						coordinateSystem: 'geo3D',
//						shading: 'lambert',
//						data: [],
//						barSize: 0.1,
//						minHeight: 0.2,
//						silent: true,
//						itemStyle: {
//							color: 'orange',
//							// opacity: 0.8
//						},
//					},
				],
			},
		}
	},
	created () {
//		if (JSON.stringify(this.mapJson) !== '{}') {
//			debugger
//
//			this.$echarts.registerMap(mapName, this.mapJson)
//		}

		this.$echarts.registerMap(mapName, {
			'type': 'FeatureCollection',
			'features': [
				{
					'type': 'Feature',
					'properties': {
					},
					'geometry': {
						'type': 'Polygon',
						'coordinates': [],
					},
				},
			],
		})
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

			this.$refs['fb-ec'].chart.setOption(this.opt)
			this.chart = this.$refs['fb-ec'].chart
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
					show: this.showSymbol,
				}
				return n
			})
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
			}, 200)
		},
		handleClickDB (params) {
			this.click_type = 'dblclick'
			this.$emit('dblclick', params)
		},
		handleMouseover (val) {
			if (this.showSymbol) return
			// if (val.seriesType == 'map') {
			//     this.opt.series[1].data[val.dataIndex].label.show = true
			//     this.opt.series[1].data[val.dataIndex].symbolSize = 75
			//     this.opt.series[2].data[val.dataIndex].symbolSize = 9
			// }
			if (val.seriesType === 'map') {
				this.opt.series[0].data[val.dataIndex].label.show = true
			}
		},
		handleMouseout (val) {
			if (this.showSymbol) return
			// if (val.seriesType == 'map') {
			//     this.opt.series[1].data[val.dataIndex].label.show = false
			//     this.opt.series[1].data[val.dataIndex].symbolSize = 0
			//     this.opt.series[2].data[val.dataIndex].symbolSize = 0
			// }
			if (val.seriesType === 'map') {
				this.opt.series[0].data[val.dataIndex].label.show = false
			}
		},
	},
}
</script>
<style scoped lang="less">

</style>
