<template>
	<!--  环形图 右侧legend linear 渐变款 -->
	<div class="ring-legend-right-custom" :class="[`ring-legend-right-custom-${theme}`]"
		 style="width: 100%; height: 100%" @mouseenter="clearTimer"
		 @mouseleave="loopChart">
		<fb-echarts
			ref="fb-ec"
			@highlight="highlight"
			@mouseover="changeHighlight"
			@mouseout="changeHighlight"
			@click="handleClick"
			:options="opt"
			:theme="theme"
			:initOptions="initOptions"
			:group="group"
			:autoresize="autoresize"
			:watchShallow="watchShallow"
			:manualUpdate="manualUpdate"
		>
		</fb-echarts>

		<div class="legend" v-show="opt.series[0].data.length > 0" :style="legendStyle">
			<div class="legend-item"
				 :class="{active: currentIndex == idx}"
				 v-for="(item, idx) in legends" :key="idx"
				 @click="legendClick({dataIndex: idx})">
				<span class="legend-item-dot"
					  :style="[{background: legendItemReturnC(itemColors[idx % itemColors.length])}]"></span>
				<span style="margin-right: 4px" :title="legendTitles[idx]">{{ item }}</span>
				<span>{{ opt.series[0].data[idx].showValue }}</span>
				<span>{{ unit }}</span>
			</div>
		</div>
	</div>
</template>
<script>
	import chartMixin from '../../ChartMixin'
	import {linearColors} from '../../../utils'
	import {merge, cloneDeep} from 'lodash-es'

	export default {
		name: 'FbecPieCustomLegend',
		mixins: [chartMixin],
		props: {
			legendStyle: {},
			ellipsisLength: {
				type: [Number],
				default: 6
			},
			unit: {
				type: [String, Number],
				default: ''
			}
		},
		data() {
			return {
				opt: {
					// color: ['#7324B8', '#C3AD00', '#FA6400', '#12A0D8', '#F5F200', '#18A7A7', '#016AD2', '#6972F1', '#F962B5'],
					tooltip: {
						trigger: 'item',
						formatter(params) {
							return `${params.marker}${params.name}${params.data.showValue}`
						}
					},
					legend: {
						show: false,
						top: 'center',
						right: '12%',
						orient: 'vertical',
						icon: 'circle',
						selectedMode: true,
						itemGap: 6,
						fontSize: 16
					},
					series: [
						{
							name: '',
							type: 'pie',
							center: ['30%', '50%'],
							radius: ['50%', '70%'],
							avoidLabelOverlap: false,
							label: {
								show: false,
								position: 'center'
							},
							emphasis: {
								scaleSize: 12,
								label: {
									show: true,
									fontSize: '22',
									fontWeight: 'bold',
									// formatter: '{b}\n{c}'
									formatter(val) {
										return `${parseFloat(val.data.showValue).toLocaleString()}`
									},
								}
							},
							labelLine: {
								show: false
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
				},
				myChart: null,
				timer: 0,
				currentIndex: 0,
				dataLen: 0,
				isMouseout: false,
				speed: 5000,
				isLoop: true,
				loopOnChange: false,
				legends: [],
				legendTitles: [], // 当文字被截取接收全称
				itemColors: [
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
			}
		},
		watch: {
			theme() {
				this.$nextTick(() => {
					this.myChart = this.chart = this.$refs['fb-ec'].chart
				})
			}
		},
		methods: {
			legendItemReturnC(value = '#3B51FF') {
				let color = value
				if (Object.prototype.toString.call(color) === '[object Object]') {
					color = value.colorStops[0].color
				}
				return color
			},
			/**
			 * tailColorIdx 颜色尾差
			 * */
			updateOptions(val, option = {loop: true}) {
				this.legends = []
				this.opt = merge(this.opt, val)
				if (val.series) {
					if (option && option.itemColors) {
						this.itemColors = option.itemColors
					}
					this.opt.color = cloneDeep(this.itemColors)

					for (let i = 0; i < val.series.length; i++) {
						let item = val.series[i]
						this.opt.series[i] = this.opt.series[i] || {}

						if (item.data.length > 0) {
							if (i === 0) {
								item.data.forEach((dataItem, idx) => {
									if (this.ellipsisLength > 0) {
										// 字符长度 是否大于 截取长度
										let text = dataItem.name.length > this.ellipsisLength ? dataItem.name.slice(0, this.ellipsisLength) + '...' : dataItem.name
										this.legends.push(text)
									} else {
										this.legends.push(dataItem.name)
									}
									this.legendTitles.push(dataItem.name)

									if (dataItem) {
										/**
										 * tailColorIdx 颜色尾差
										 * */
										dataItem.itemStyle = {}
										dataItem.itemStyle.color = this.itemColors[idx % this.itemColors.length]
									}
								})
							}

							this.opt.series[i] = merge(this.opt.series[i], item)

						} else {
							this.opt.series[i].data = []
						}
					}
				}

				this.chart.clear()
				this.chart.setOption(this.opt)
				this.myChart = this.chart

				this.startAni(option)
			},
			startAni(option = {}) {
				this.isLoop = option.loop
				this.dataLen = this.opt.series[0].data.length
				this.currentIndex = 0
				clearInterval(this.timer)
				// 取消之前所有高亮的图形
				this.myChart.dispatchAction({
					type: 'downplay',
					seriesIndex: 0,
				})
				setTimeout(() => {
					this.loopOnChange = true
					// 高亮当前图形
					this.myChart.dispatchAction({
						type: 'highlight',
						seriesIndex: 0,
						dataIndex: 0
					})
					// this.myChart.dispatchAction({
					//     type: 'showTip',
					//     seriesIndex: 0,
					//     dataIndex: 0
					// })
				}, 10)
				this.loopChart(option)
			},
			highlight(val) {
				if (this.isMouseout) {
					// 防止移出丢失 showTip 状态 多次触发事件查询
					this.isMouseout = false
					return false
				}
				let color = this.itemColors[this.currentIndex]

				if (this.loopOnChange) {
					this.$emit('on-change', {
						dataIndex: this.currentIndex,
						color: color,
						data: this.opt.series[0].data[this.currentIndex],
					})
				}
			},
			loopChart(option = {}) {
				// 初始化轮播事件
				if (!this.isLoop) return
				let dataLen = this.dataLen
				if (option.speed) {
					this.speed = option.speed
				}
				clearInterval(this.timer)
				this.loopOnChange = true
				this.timer = setInterval(() => {
					// 取消之前高亮的图形
					this.myChart.dispatchAction({
						type: 'downplay',
						seriesIndex: 0,
						dataIndex: this.currentIndex
					})
					this.currentIndex = (this.currentIndex + 1) % dataLen
					// 高亮当前图形
					this.myChart.dispatchAction({
						type: 'highlight',
						seriesIndex: 0,
						dataIndex: this.currentIndex
					})
					// 显示 tooltip
					// myChart.dispatchAction({
					//     type: 'showTip',
					//     seriesIndex: 0,
					//     dataIndex: this.currentIndex
					// })
				}, this.speed)
			},
			handleClick(val) {
				this.isMouseout = true
				// 取消之前高亮的图形
				this.myChart.dispatchAction({
					type: 'downplay',
					seriesIndex: 0,
					dataIndex: this.currentIndex
				})
				this.currentIndex = val.dataIndex
				// 高亮当前图形
				this.myChart.dispatchAction({
					type: 'highlight',
					seriesIndex: 0,
					dataIndex: this.currentIndex
				})
			},
			changeHighlight(val) {
				// 取消之前所有高亮的图形
				this.myChart.dispatchAction({
					type: 'downplay',
					seriesIndex: 0,
				})
				this.myChart.dispatchAction({
					type: 'highlight',
					seriesIndex: 0,
					dataIndex: val.dataIndex
				})
				this.currentIndex = val.dataIndex
				this.clearTimer()
			},
			legendClick(val) {
				this.changeHighlight(val);
				let color = this.itemColors[this.currentIndex]
				this.$emit('click', {
					dataIndex: this.currentIndex,
					color: color,
					data: this.opt.series[0].data[this.currentIndex],
				})
			},
			clearTimer() {
				this.loopOnChange = false
				clearInterval(this.timer)
			}
		},
		beforeDestroy() {
			clearInterval(this.timer)
		},
		deactivated() {
			clearInterval(this.timer)
		}
	}
</script>

<style scoped lang="less">
	.ring-legend-right-custom {
		position: relative;

		.legend {
			position: absolute;
			top: 50%;
			left: 60%;
			transform: translateY(-50%);
			font-size: 16px;
			cursor: pointer;
			color: rgba(255, 255, 255, 0.8);
			max-height: 100%;
			overflow: auto;

			.legend-item {

				&.active {
					font-size: 18px;
					font-family: MicrosoftYaHeiSemibold;
					color: #FFFFFF;
					font-weight: 800;
				}
			}

			.legend-item-dot {
				display: inline-block;
				width: 16px;
				height: 16px;
				border-radius: 50%;
				margin-right: 6px;
				vertical-align: -3px;
				background: grey;
			}
		}

		&.ring-legend-right-custom-fbecLight {
			.legend {
				color: #313C47;

				.legend-item {

					&.active {
						color: #313C47;
					}
				}

				.legend-item-dot {
					background: grey;
				}
			}
		}
	}
</style>
