<!--<template>-->
<!--	<fb-container class="screen-table-chart">-->
<!--		<fb-fieldset></fb-fieldset>-->
<!--		<fb-fieldset></fb-fieldset>-->
<!--	</fb-container>-->
<!--</template>-->

<script>
/**
 * 表格图表切换组件
 * */
import {merge, isString, isArray} from 'lodash-es'
export default {
	name: "ScreenTableChart",
	props: {
		tableProp: {
			type: [Object],
			default: () => ({})
		},
		chartOptionMix: {
			type: [Object],
			default: () => ({})
		}
	},
	data() {
		return {
			isTable: true,
			showChartTool: true,
			chartType: 'bar',
			chartTypeData: [
				{label: '柱状', value: 'bar'},
				{label: '折线', value: 'line'},
				{label: '饼图', value: 'pie'},
			],
			// 数据项
			chartDataItem: '',
			// 统计项
			chartCountItem: '',
			chartItemData: [],
			chartOption: {
				toolbox: {
					show: true,
					feature: {
						dataView: {
							readOnly: false,
						},
						magicType: {
							type: ["line", "bar"]
						},
						restore: {
						},
						saveAsImage: {
						}
					}
				},
				dataZoom: {
					show: true,
				},
				tooltip: {
					trigger: 'axis',
					axisPointer: {
						type: 'shadow'
					}
				},
				legend: {
					top: '3%',
					left: 'center',
					data: [],
					show: true
				},
				grid: {
					top: '16%',
					left: '3%',
					right: '5%',
					bottom: '13%',
					containLabel: true
				},
				xAxis: {
					type: 'category',
					boundaryGap: true,
					data: []
				},
				yAxis: [{
					type: 'value',
					scale: true,
					axisTick: {show: false},
				}],
				series: []
			}
		}
	},
	methods: {
		renderHeader(h) {
			let props = this.tableProp
			return h('fb-container', {
				class: ['screen-table-chart-header']
			}, [
				h('fb-container', {
					class: ['screen-table-chart-header__btn'],
				}, [
					h('fb-button', {
						props: {

						},
						on: {
							'on-click': () => {
								this.showChartTool = !this.showChartTool
							}
						}

					}, ['数据可视化']),
					h('fb-button', {
						props: {

						},
						style: {
							display: !this.isTable ? 'inline-block' : 'none',
							marginLeft: '6px',
						},
						on: {
							'on-click': () => {
								this.isTable = true
								this.showChartTool = false
							}
						}

					}, ['返回表格']),
				])

			])
		},
		renderTable(h) {
			let props = this.tableProp
			return h('fb-simple-table', {
				props
			}, [

			])
		},
		renderChart(h) {
			return h('fb-echarts', {
				props: {
					options: this.chartOption,
					autoresize: true
				},
				ref: 'chart',
			})
		},
		renderChartTool(h) {
			let {tableProp} = this
			let selectDataItem = []
			if (tableProp && tableProp.columns && tableProp.columns.length > 0) {
				selectDataItem = tableProp.columns.map(item => {
					return {
						label: item.label,
						value: item.name,
					}
				})
			}
			return h('fb-card', {
				props: {
					header: '数据可视化'
				},
				style: {
					display: this.showChartTool ? 'block' : 'none'
				}
			}, [
				h('fb-container', {style: 'margin-bottom: 4px'}, ['请先选择数据项、统计项等条件，再选择合适的图形展现可视化']),
				h('fb-form', {
					props: {
						labelWidth: 90
					}
				}, [
					h('fb-form-item', {
						props: {
							label: '图表类型'
						}
					}, [
						h('fb-select', {
							props: {
								value: this.chartType,
								data: this.chartTypeData
							},
							on: {
								change: (val) => {
									this.chartType = val
								}
							}
						}, []),
					]),
					h('fb-fieldset', {
						props: {
							label: '条件'
						}
					}, ),
					h('fb-form-item', {
						props: {
							label: '数据项'
						}
					}, [
						h('fb-select', {
							props: {
								value: this.chartDataItem,
								data: selectDataItem
							},
							on: {
								change: (val) => {
									this.chartDataItem = val
								}
							}
						}, []),
					]),
					h('fb-form-item', {
						props: {
							label: '统计项',
						}
					}, [
						h('fb-select', {
							props: {
								value: this.chartCountItem,
								data: selectDataItem,
								multiple: this.chartType !== 'pie' ? true : false
							},
							on: {
								change: (val) => {
									this.chartCountItem = val
								}
							}
						}, []),
					]),
					h('fb-container', {
						props: {
							align: 'right'
						}
					}, [
						h('fb-button', {
							on: {
								'on-click': this.createChart
							},
						}, ['生成图表']),
						h('fb-button', {
							style: [{marginLeft: '6px'}],
							on: {
								'on-click': () => {
									this.showChartTool = false
								}
							},
						}, ['关闭']),
					]),
				])
			])
		},
		renderContent(h) {
			const {showChartTool} = this
			const tableDom = this.renderTable(h)
			const chartDom = this.renderChart(h)
			const chartToolDom = this.renderChartTool(h)

			let child = [
				h('fb-col', {
					props: {
						span: showChartTool ? 12 : 24
					}
				}, [this.isTable ? tableDom : chartDom]),
				h('fb-col', {
					props: {
						span: showChartTool ? 12 : 0
					}
				}, [
					chartToolDom
				])
			]

			return h('fb-row', {
				class: 'screen-table-chart-content',
				props: {
					gutter: 8
				}
			}, child)
		},

		// 事件部分
		createChart() {
			let {tableProp, chartDataItem, chartCountItem} = this
			if (!chartDataItem) {
				this.$message.warn('请选择数据项！')
				return
			}
			if (!chartCountItem) {
				this.$message.warn('请选择统计项！')
				return
			}
			if (isString(chartCountItem) && chartCountItem === chartDataItem) {
				this.$message.warn('数据项和统计项相同？')
				return
			}
			if (isArray(chartCountItem) && chartCountItem.some(item => item === chartDataItem)) {
				this.$message.warn('数据项和统计项相同？')
				return
			}

			// table 为 data 格式
			if (tableProp.data && tableProp.data.length > 0) {
				this.initChart(tableProp.data)
			}

		},
		initChart(data) {
			this.chartOption.series = []
			let options = merge(this.chartOption, this.chartOptionMix)
			if (this.chartType === 'pie') {
				let pieData = data.map(item => {
					return {
						name: item[this.chartDataItem],
						value: item[this.chartCountItem],
					}
				})
				options.toolbox.show = false
				options.dataZoom.show = false
				options.xAxis.show = false
				options.yAxis[0].show = false
				options.legend.data = pieData.map(item => item.name)
				options.series = [{
					type: this.chartType,
					data: pieData,
					radius: '40%'
				}]
			} else {
				let xData = data.map(item => item[this.chartDataItem])
				let series = this.initSeries(data)
				options.toolbox.show = true
				options.dataZoom.show = true
				options.xAxis.show = true
				options.yAxis[0].show = true
				options.xAxis.data = xData
				options.legend.data = series.map(item => item.name)
				options.series = series
			}

			this.isTable = false
			this.$nextTick(() => {
				this.$refs['chart'].chart.clear()
				this.$refs['chart'].chart.setOption(options)
			})
		},
		initSeries(data) {
			let series = []
			if (this.chartCountItem.length > 0) {
				for (let i in this.chartCountItem) {
					let key = this.chartCountItem[i]
					let col = this.tableProp.columns.find(item => item.name === key)
					series.push({
						name: col.label,
						type: this.chartType,
						data: data.map(item => item[key])
					})
				}
			}
			return series
		}
	},
	render(h) {
		const headerDom = this.renderHeader(h)
		const contentDom = this.renderContent(h)

		return h('fb-container', {
			class: ['screen-table-chart'],
			props: {
				...this.$attrs
			}
		}, [headerDom, contentDom])
	}
}
</script>

<style lang="less" scoped>
	.screen-table-chart {
		width: 100%;
		height: 100%;

		.screen-table-chart-header {
			height: 40px;
			line-height: 40px;
			position: relative;

			&__btn {
				position: absolute;
				top: 50%;
				right: 8px;
				transform: translateY(-50%);
			}
		}
		.screen-table-chart-content {
			height: calc(100% - 40px);
			>div {
				height: 100%;
			}
		}

		::v-deep.jpx-form-fieldset .jpx-form-fieldset__legend {
			margin: 0;
			margin-bottom: 8px;
		}
	}
</style>
