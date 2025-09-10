<template>
	<!--  插件-水波球  -->
	<div class="ec-liquidFill-box" :class="[`ec-liquidFill-box-${theme}`]">
		<div class="ec-liquidFill-title" :style="[titleStyle]">
			<span :style="[valueStyle]">
				<slot name="value">
					{{ valueText }}
				</slot>
			</span>
			<span :style="[unitStyle]">{{ unitText }}</span>
			<div class="ec-liquidFill-label" :style="[labelStyle]" v-html="labelText"></div>
		</div>
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
	</div>
</template>
<script>
	import 'echarts-liquidfill'
	import chartMixin from '../../ChartMixin'
	import {merge} from "lodash-es";
	import {linearColors} from "../../../utils";

	export default {
		name: 'FbecLiquidFill',
		mixins: [chartMixin],
		props: {
			titleStyle: {},
			valueStyle: {},
			unitStyle: {},
			labelStyle: {},
		},
		data() {
			return {
				opt: {
					series: [{
						type: 'liquidFill',
						data: [],
						radius: '76%',
						label: {show: false},
						backgroundStyle: {
							color: 'rgba(0, 145, 255, 0)'
						},
						color: [linearColors.liquidFill.yellow],
						outline: {
							show: true,
							borderDistance: 0,
							itemStyle: {
								color: 'none',
								borderWidth: 3,
								borderColor: linearColors.liquidFill.border.yellow,
							}
						},
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
			}
		},
		methods: {
			updateOptions(params = {}, option = {}) {
				let num = parseFloat(params.value / 100)
				if (num > 0) {
					let num1 = num - 0.1
					let num2 = num - 0.2
					this.opt.series[0].data = [num, num1, num2]
				} else {
					this.opt.series[0].data = []
				}

				if (params.unitText) {
					this.unitText = params.unitText
				}
				this.valueText = params.value
				this.labelText = params.labelText.split('\n').join('<br/>')

				if (option && option.type) {
					this.opt.series[0].color = [linearColors.liquidFill[option.type]]
					this.opt.series[0].outline.itemStyle.borderColor = linearColors.liquidFill.border[option.type]
				}

				// 合并配置项
				if (params.config) {
					this.opt.series[0] = Object.assign(this.opt.series[0], params.config)
				}

				this.chart.clear()
this.chart.setOption(this.opt)
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

		.ec-liquidFill-title {
			width: 100%;
			font-family: DINPro-Medium, DINPro;
			font-weight: 500;
			position: absolute;
			top: 50%;
			left: 50%;
			transform: translate(-50%, -50%);
			z-index: 99;
			span:first-child {
				font-size: 30px;
				margin-right: 2px;
			}
			span:last-child {
				font-size: 16px;
			}
		}

		.ec-liquidFill-label {
			text-align: center;
			font-size: 16px;
			font-family: MicrosoftYaHei;
			line-height: 19px;
			width: 100%;
			/*position: absolute;*/
			/*bottom: 16%;*/
			/*left: 50%;*/
			/*transform: translateX(-50%);*/
			/*z-index: 99;*/
		}

		&.ec-liquidFill-box-fbecLight {
			color: #313C47;
		}
	}
</style>
