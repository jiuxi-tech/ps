<template>
	<div style="width: 100%; height: 100%" @mouseenter="clearTimer"
		 @mouseleave="loopChart">
		<!--  基础3d饼图  -->
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
			@mouseover="mouseover"
		>
		</fb-echarts>
	</div>
</template>
<script>
	/***
	 * FbecPie3dBase 3d 饼图
	 * */
	import 'echarts-gl'
	import chartMixin from '../../ChartMixin'
	import {linearColors} from "../../../utils";
	import {merge, cloneDeep} from "lodash-es";

	// 生成扇形的曲面参数方程，用于 series-surface.parametricEquation
	function getParametricEquation(startRatio, endRatio, isSelected, isHovered, k, h) {

		// 计算
		let midRatio = (startRatio + endRatio) / 2;

		let startRadian = startRatio * Math.PI * 2;
		let endRadian = endRatio * Math.PI * 2;
		let midRadian = midRatio * Math.PI * 2;

		// 如果只有一个扇形，则不实现选中效果。
		if (startRatio === 0 && endRatio === 1) {
			isSelected = false;
		}

		// 通过扇形内径/外径的值，换算出辅助参数 k（默认值 1/3）
		k = typeof k !== 'undefined' ? k : 1 / 3;

		// 计算选中效果分别在 x 轴、y 轴方向上的位移（未选中，则位移均为 0）
		let offsetX = isSelected ? Math.cos(midRadian) * 0.1 : 0;
		let offsetY = isSelected ? Math.sin(midRadian) * 0.1 : 0;

		// 计算高亮效果的放大比例（未高亮，则比例为 1）
		let hoverRate = isHovered ? 1.05 : 1;

		// 返回曲面参数方程
		return {

			u: {
				min: -Math.PI,
				max: Math.PI * 3,
				step: Math.PI / 32
			},

			v: {
				min: 0,
				max: Math.PI * 2,
				step: Math.PI / 20
			},

			x: function(u, v) {
				if (u < startRadian) {
					return offsetX + Math.cos(startRadian) * (1 + Math.cos(v) * k) * hoverRate;
				}
				if (u > endRadian) {
					return offsetX + Math.cos(endRadian) * (1 + Math.cos(v) * k) * hoverRate;
				}
				return offsetX + Math.cos(u) * (1 + Math.cos(v) * k) * hoverRate;
			},

			y: function(u, v) {
				if (u < startRadian) {
					return offsetY + Math.sin(startRadian) * (1 + Math.cos(v) * k) * hoverRate;
				}
				if (u > endRadian) {
					return offsetY + Math.sin(endRadian) * (1 + Math.cos(v) * k) * hoverRate;
				}
				return offsetY + Math.sin(u) * (1 + Math.cos(v) * k) * hoverRate;
			},

			z: function(u, v) {
				if (u < -Math.PI * 0.5) {
					return Math.sin(u);
				}
				if (u > Math.PI * 2.5) {
					return Math.sin(u) * h * .1;
				}
				return Math.sin(v) > 0 ? 1 * h * .1 : -1;
			}
		};
	}
	// 生成模拟 3D 饼图的配置项
	function getPie3D(pieData, internalDiameterRatio) {

		const series = [];
		// 总和
		let sumValue = 0;
		let startValue = 0;
		let endValue = 0;
		const legendData = [];
		const k =
			typeof internalDiameterRatio !== 'undefined'
				? (1 - internalDiameterRatio) / (1 + internalDiameterRatio)
				: 1 / 3;

		// 为每一个饼图数据，生成一个 series-surface 配置
		for (let i = 0; i < pieData.length; i += 1) {
			sumValue += pieData[i].value;

			const seriesItem = {
				name: typeof pieData[i].name === 'undefined' ? `series${i}` : pieData[i].name,
				type: 'surface',
				parametric: true,
				wireframe: {
					show: false,
				},
				pieData: pieData[i],
				height: i * 6 + 1,
				pieStatus: {
					selected: false,
					hovered: false,
					k,
				},
			};

			if (typeof pieData[i].itemStyle !== 'undefined') {
				const { itemStyle } = pieData[i];

				// eslint-disable-next-line no-unused-expressions
				typeof pieData[i].itemStyle.color !== 'undefined' ? (itemStyle.color = pieData[i].itemStyle.color) : null;
				// eslint-disable-next-line no-unused-expressions
				typeof pieData[i].itemStyle.opacity !== 'undefined'
					? (itemStyle.opacity = pieData[i].itemStyle.opacity)
					: null;

				seriesItem.itemStyle = itemStyle;
			}
			series.push(seriesItem);
		}

		// 使用上一次遍历时，计算出的数据和 sumValue，调用 getParametricEquation 函数，
		// 向每个 series-surface 传入不同的参数方程 series-surface.parametricEquation，也就是实现每一个扇形。
		for (let i = 0; i < series.length; i++) {
			endValue = startValue + series[i].pieData.value;

			series[i].pieData.startRatio = startValue / sumValue;
			series[i].pieData.endRatio = endValue / sumValue;
			series[i].parametricEquation = getParametricEquation(
				series[i].pieData.startRatio,
				series[i].pieData.endRatio,
				false,
				false,
				k,
				series[i].height
			);

			startValue = endValue;

			legendData.push(series[i].name);
		}

		// 准备待返回的配置项，把准备好的 legendData、series 传入。
		let option = {
			// animation: false,
			legend: {
				data: legendData
			},
			tooltip: {
				trigger: 'item',
				formatter: (params) => {
					if (params.seriesName !== 'mouseoutSeries' && params.seriesName !== 'pie2d') {
						if (!option.series[params.seriesIndex].pieData) {
							return ''
						}
						return `${
							params.seriesName
						}<br/><span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:${
							params.color
						};"></span>${option.series[params.seriesIndex].pieData.value}`;
					}
					return '';
				},
			},
			xAxis3D: {
				min: -1,
				max: 1,
			},
			yAxis3D: {
				min: -1,
				max: 1,
			},
			zAxis3D: {
				min: -1,
				max: 1,
			},
			grid3D: {
				show: false,
				boxHeight: 10,
				top: -10,
				viewControl: {
					// 3d效果可以放大、旋转等，请自己去查看官方配置
					alpha: 35,
					// beta: 30,
					rotateSensitivity: 1,
					zoomSensitivity: 0,
					panSensitivity: 0,
					autoRotate: false,
				},
				// 后处理特效可以为画面添加高光、景深、环境光遮蔽（SSAO）、调色等效果。可以让整个画面更富有质感。
				postEffect: {
					// 配置这项会出现锯齿，请自己去查看官方配置有办法解决
					enable: false,
					bloom: {
						enable: true,
						bloomIntensity: 0.1,
					},
					SSAO: {
						enable: true,
						quality: 'medium',
						radius: 1,
					},
					// temporalSuperSampling: {
					//   enable: true,
					// },
				},
			},
			series,
		};
		return option;
	}

	export default {
		name: 'FbecPie3dBase',
		mixins: [chartMixin],
		components: {},
		data() {
			return {
				sourceOpt: {},
				opt: {},
				hoverIndex: '',

				currentIndex: '',
				timer: 0,
				dataLen: 0,
				isMouseout: false,
				speed: 5000,
				loop: false,

				itemColors: [
					'rgba(92, 207, 255, 1)',
					'rgba(17, 245, 215, 1)',
					'rgba(255, 231, 94, 1)',
					'rgba(253, 141, 134, 1)',
					'rgba(161, 171, 250, 1)',
					'rgba(208, 170, 246, 1)',
					'rgba(254, 203, 110, 1)',
					'rgba(75, 210, 227, 1)',
					'rgba(254, 192, 233, 1)',
					'rgba(150, 169, 196, 1)',
				],
			}
		},
		mounted() {

		},
		beforeDestroy() {
			this.clearTimer()
		},
		methods: {
			/**
			 * 更新3d饼图
			 * @param {object} pieData 饼图数据
			 * @param {object} config 配置项 内径比，轮播
			 * @param {object} settings 合并 opt
			 */
			updateOptions(pieData = [], config = {}, settings) {
				if (config.itemColors) {
					this.itemColors = config.itemColors
				}
				pieData = pieData.map((item, i) => {
					if (!item.itemStyle) {
						item.itemStyle = {
							color: this.itemColors[i % 10],
							opacity: 0.6
						}
					}
					return item
				})

				// 传入数据生成 option
				let option = getPie3D(pieData, config.internalDiameterRatio || 0.05);

				if (settings) {
					option = merge(option, settings)
				}

				if (config.label) {
					let labelOpt = Object.assign({
						name: 'pie2d',
						type: 'pie',
						labelLine: {
							length: 10,
							length2: 10,
						},
						startAngle: -25, //起始角度，支持范围[0, 360]。
						clockwise: false, //饼图的扇区是否是顺时针排布。上述这两项配置主要是为了对齐3d的样式
						radius: ['65%', '65%'],
						center: ['55%', '48%'], //指示线的位置
						data: pieData,
						itemStyle: {
							opacity: 1,
						},
					}, config.label)
					//是否需要label指引线，如果要就添加一个透明的2d饼状图并调整角度使得labelLine和3d的饼状图对齐，并再次setOption
					option.series.push(labelOpt);
				}

				this.opt = option
				this.sourceOpt = cloneDeep(option)


				this.startAni(config, pieData)
				this.chart.clear()
				this.chart.setOption(this.opt)
				// console.log(this.opt, 1111111111, this.chart.getOption())

			},
			mouseover(params) {
				this.setHighlight(params)
			},
			globalout() {
				let option = this.chart.getOption()
				let {currentIndex} = this
				if (currentIndex !== '') {
					// 从 option.series 中读取重新渲染扇形所需的参数，将是否高亮设置为 true。
					let isSelected = option.series[currentIndex].pieStatus.selected;
					let isHovered = false;
					let k = option.series[currentIndex].pieStatus.k;
					let startRatio = option.series[currentIndex].pieData.startRatio;
					let endRatio = option.series[currentIndex].pieData.endRatio;
					// 对当前点击的扇形，执行取消高亮操作（对 option 更新）
					let i = option.series[currentIndex].pieData.value === option.series[0].pieData.value ? 35 : 10;
					option.series[currentIndex].parametricEquation = getParametricEquation(
						startRatio,
						endRatio,
						isSelected,
						isHovered,
						k,
						i
					);
					option.series[currentIndex].pieStatus.hovered = isHovered;

					// 将此前记录的上次选中的扇形对应的系列号 seriesIndex 清空
					currentIndex = '';
				}

				// 使用更新后的 option，渲染图表
				this.chart.setOption(option);
			},
			setHighlight(params) {
				let option = this.sourceOpt
				let {hoverIndex} = this

				// 准备重新渲染扇形所需的参数
				let isSelected;
				let isHovered;
				let startRatio;
				let endRatio;
				let k;
				let h;

				// 如果触发 mouseover 的扇形当前已高亮，则不做操作
				if (hoverIndex === params.seriesIndex) {
					return;

					// 否则进行高亮及必要的取消高亮操作
				} else {
					// 如果当前有高亮的扇形，取消其高亮状态（对 option 更新）
					if (hoverIndex !== '') {
						// 从 option.series 中读取重新渲染扇形所需的参数，将是否高亮设置为 false。
						isSelected = option.series[hoverIndex].pieStatus.selected;
						isHovered = false;
						startRatio = option.series[hoverIndex].pieData.startRatio;
						endRatio = option.series[hoverIndex].pieData.endRatio;
						k = option.series[hoverIndex].pieStatus.k;
						h = option.series[hoverIndex].height + 6;
						// 对当前点击的扇形，执行取消高亮操作（对 option 更新）
						option.series[hoverIndex].parametricEquation = getParametricEquation(
							startRatio,
							endRatio,
							isSelected,
							isHovered,
							k,
							h
						);
						option.series[hoverIndex].pieStatus.hovered = isHovered;

						// 将此前记录的上次选中的扇形对应的系列号 seriesIndex 清空
						this.hoverIndex = '';
					}

					// 如果触发 mouseover 的扇形不是透明圆环，将其高亮（对 option 更新）
					if (params.seriesName !== 'mouseoutSeries' && params.seriesName !== 'pie2d') {
						if (!option.series[params.seriesIndex]) {
							return;
						}
						// 从 option.series 中读取重新渲染扇形所需的参数，将是否高亮设置为 true。
						isSelected = option.series[params.seriesIndex].pieStatus.selected;
						isHovered = true;
						startRatio = option.series[params.seriesIndex].pieData.startRatio;
						endRatio = option.series[params.seriesIndex].pieData.endRatio;
						k = option.series[params.seriesIndex].pieStatus.k;

						// 对当前点击的扇形，执行高亮操作（对 option 更新）
						option.series[params.seriesIndex].parametricEquation = getParametricEquation(
							startRatio,
							endRatio,
							isSelected,
							isHovered,
							k,
							option.series[params.seriesIndex].pieData.value + 5
						);
						option.series[params.seriesIndex].pieStatus.hovered = isHovered;

						// 记录上次高亮的扇形对应的系列号 seriesIndex
						this.hoverIndex = params.seriesIndex;
					}

					// 使用更新后的 option，渲染图表
					this.chart.setOption(option);
				}
			},
			startAni(config = {}, pieData) {
				// 初始化轮播事件
				this.loop = config.loop
				if (!config.loop) return
				this.dataLen = pieData.length
				this.currentIndex = 0
				clearInterval(this.timer)
				setTimeout(() => {
					this.loopOnChange = true
					// 高亮当前图形
					this.setHighlight({
						seriesIndex: this.currentIndex,
						seriesName: 'loop'
					})
					// 显示 tooltip
					this.myChart.dispatchAction({
						type: 'showTip',
						geoIndex: this.currentIndex
					})
				}, 10)
				this.loopChart(config)
			},
			loopChart(config = {}) {
				if (!this.loop) {
					return
				}
				let dataLen = this.dataLen
				if (config.speed) {
					this.speed = config.speed
				}
				clearInterval(this.timer)
				this.loopOnChange = true
				this.timer = setInterval(() => {
					this.currentIndex = (this.currentIndex + 1) % dataLen
					// 高亮当前图形
					this.setHighlight({
						seriesIndex: this.currentIndex,
						seriesName: 'loop'
					})
					// 显示 tooltip
					this.myChart.dispatchAction({
					    type: 'showTip',
					    // seriesIndex: this.currentIndex,
						// geoIndex: this.currentIndex,
						name: this.sourceOpt.series[this.currentIndex].name
					})
				}, this.speed)
			},
			clearTimer() {
				this.loopOnChange = false
				clearInterval(this.timer)
			}
		},

	}

</script>
