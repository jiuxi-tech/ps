<template>
	<!--  简单环形图  -->
	<div class="ec-liquidFill-box" :class="[
		`ec-liquidFill-box-${labelGrid}`,
		`ec-liquidFill-box-${theme}`
	]">
		<div v-if="showLottie" style="width: 100%; height: 100%; position: absolute; top: 0; left: 0; z-index: 0" :style="lottieShellStyle">
			<fb-lottie :data="lottieData" style="width: 100%; height: 100%"></fb-lottie>
		</div>

		<div class="ec-liquidFill-lv" :style="[titleStyle]">
			<div class="ec-liquidFill-value">
				<span :style="[valueStyle]" class="value-text">
					<span v-if="!numRun">{{ valueText }}</span>
					<fb-number v-else :decimals="decimals" :end="valueText"></fb-number>
				</span>
				<span :style="[unitStyle]" class="unit-text" v-if="!labelGrid">
					{{ unitText }}
				</span>
			</div>
			<div class="ec-liquidFill-label" :style="[labelStyle]">
				<fb-badge v-if="labelGrid === 'show_value' && labelText" class="label-dot" dot dot-size="16" :dot-color="dotColor"></fb-badge>
				<span v-html="labelText"></span>
			</div>
		</div>

		<fb-echarts
			ref="fb-ec"
			:options="opt"
			:theme="theme"
			:initOptions="initOptions"
			:group="group"
			:autoresize="autoresize"
			:watchShallow="watchShallow"
			:manualUpdate="manualUpdate"
		>
		</fb-echarts>
	</div>

</template>
<script>
	import chartMixin from '../../ChartMixin'
	import {linearColors} from '../../../utils'
	import {cloneDeep, merge, isNumber} from 'lodash-es'
	import pieBarBg from '../../../lottie-json/pie-bar-bg.json'

	export default {
		name: 'FbecPieBar',
		mixins: [chartMixin],
		/**
		 * labelGrid
		 * show_value 展示 value 模式，上下结构
		 * */
		props: {
			labelGrid: {
				type: String,
				default: ''
			},
			numRun: { // 是否使用 fb-number
				type: Boolean,
				default: true
			},
			titleStyle: {},
			valueStyle: {},
			unitStyle: {},
			labelStyle: {},

			showLottie: {
				type: Boolean,
				default: false
			},
			lottieShellStyle: {},
			lottieData: {
				type: [Object, String],
				default: () => pieBarBg
			},
			lottieOption: {
				type: [Object],
				default: () => ({})
			}
		},
		data() {
			return {
				opt: {
					color: [linearColors.y2.green],
					angleAxis: {
						max: 100,
						clockwise: true, // 逆时针
						show: false,// 隐藏刻度线
					},
					radiusAxis: {
						type: 'category',
						show: true,
						axisLabel: {
							show: false,
						},
						axisLine: {
							show: false,

						},
						axisTick: {
							show: false
						},
					},
					polar: {
						center: ['50%', '50%'],
						radius: '160%' //图形大小
					},
					series: [{
						type: 'bar',
						data: [0],
						showBackground: true,
						itemStyle: {
							color: '#52FFFD',
						},
						backgroundStyle: {},
						coordinateSystem: 'polar',
						roundCap: true,
						barWidth: 14,
					}]
				},
				ariaOpt: {
					aria: {
						enabled: true,
						decal: {
							show: true
						}
					}
				},
				valueText: '0',
				unitText: '%',
				labelText: '',
				decimals: 0,
				dotColor: '#0284FE'
			}
		},

		methods: {
			updateOptions(val, options = {}) {
				let colors = {
					red: {
						color: 'rgba(230, 0, 0, 1)',
						bg: 'rgba(230, 0, 0, .3)',
					},
					orange: {
						color: 'rgba(251, 171, 2, 1)',
						bg: 'rgba(251, 171, 2, .3)',
					},
					yellow: {
						color: 'rgba(249, 253, 38, 1)',
						bg: 'rgba(249, 253, 38, .3)',
					},
					blue: {
						color: 'rgba(2, 132, 254, 1)',
						bg: 'rgba(2, 132, 254, .3)',
					},
					grey: {
						color: 'rgba(153, 153, 153, 1)',
						bg: 'rgba(153, 153, 153, .3)',
					},
				}
				this.labelText = val.labelText || ''

				if (val.polar) {
					this.opt.polar = Object.assign(this.opt.polar, val.polar)
				}

				for (let i = 0; i < val.series.length; i++) {
					let item = val.series[i]
					// 判断 type 赋值颜色
					if (options.type) {
						this.opt.series[0].itemStyle.color = colors[options.type].color
						this.opt.series[0].backgroundStyle.color = colors[options.type].bg
						this.dotColor = colors[options.type].color
					}

					this.opt.series[i] = cloneDeep(this.opt.series[i]) || {}
					this.opt.series[i] = merge(this.opt.series[i], item)

					// 取 value 值
					let value = this.opt.series[i].data[0]
					if (isNumber(value)) {
						this.valueText = value
					} else {
						this.valueText = value.showValue
					}

					// 取小数点几位
					let arr = this.valueText.toString().split('.')
					if (arr[1]) {
						this.decimals = arr[1].length
					}

					if (this.opt.legend) {
						this.opt.legend.data[i] = item.name
					}
				}
				this.$refs['fb-ec'].chart.setOption(this.opt)
			}
		}
	}
</script>

<style lang="less" scoped>
	.ec-liquidFill-box {
		width: 100%;
		height: 100%;
		border-radius: 50%;
		position: relative;
		text-align: center;
		color: #FFFFFF;

		.ec-liquidFill-lv {
			font-family: DINPro-Medium, DINPro;
			font-weight: 500;
			position: absolute;
			top: 50%;
			width: 100%;
			z-index: 99;
			transform: translateY(-58%);

			.value-text {
				font-size: 30px;
			}

			.unit-text {
				font-size: 16px;
			}
		}

		.ec-liquidFill-label {
			text-align: center;
			font-size: 16px;
			font-family: MicrosoftYaHei;
			line-height: 19px;
			/*position: absolute;*/
			/*bottom: 16%;*/
			/*left: 50%;*/
			/*transform: translateX(-50%);*/
			/*z-index: 99;*/
		}

		&.ec-liquidFill-box-show_value {
			.ec-liquidFill-lv {
				top: 0px;
				left: 0px;
				transform: translateY(0);
				height: 100%;

				.value-text {
					font-size: 20px;
					position: absolute;
					top: 50%;
					left: 50%;
					transform: translate(-50%, -50%);
				}
			}

			.ec-liquidFill-label {
				position: absolute;
				bottom: -10px;
				width: 100%;
				text-align: center;

				.label-dot {
					vertical-align: -2px;
					margin-right: 4px;
				}
			}
		}

		&.ec-liquidFill-box-fbecLight {
			color: #313C47;
		}
	}
</style>
