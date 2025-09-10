<template>
	<div style="width: 100%; height: 100%"
		 @mouseenter="handleEnter"
		 @mouseleave="handleLeave">
		<!--  时间轴  -->
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
	import chartMixin from '../../ChartMixin'
	import { merge } from 'lodash-es'

	export default {
		name: 'FbecTimeline',
		mixins: [chartMixin],
		props: {
			timelineData: {
				type: [Array],
				default: () => []
			},
		},
		data() {
			return {
				opt: {
					tooltip: {},
					timeline: {
						data: this.timelineData,
						axisType: 'category',
						currentIndex: this.timelineData.length,
						top: 10,
						left: 10,
						right: 10,
						symbol: 'diamond',
						symbolSize: 10,
						itemStyle: {
							color: '#fff',
							borderWidth: 3,
							borderColor: '#0284FE'
						},
						lineStyle: {
							type: [4, 4],
							dashOffset: 1,
							color: 'rgba(13, 128, 254, .6)'
						},
						checkpointStyle: {
							symbolSize: 12,
							color: '#fff',
							borderWidth: 3,
							shadowColor: '#0D80FE',
							shadowBlur: 12,
						},
						autoPlay: false,
						playInterval: 5000,
						label: {
							// formatter (val, i) {
							// 	// return app.dayjs(val).format('MM-DD')
							// },
						},
					},
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
			updateOptions(val, option = {}) {
				this.opt = merge(this.opt, val)
				this.chart.setOption(val)
			},
			handleEnter(params) {
				// this.timelinePlayChange(false)
			},
			handleLeave() {
				// this.timelinePlayChange(true)
			},
			timelinePlayChange(playState) {
				this.$refs['fb-ec'].dispatchAction({
					type: 'timelinePlayChange',
					// 播放状态，true 为自动播放
					playState: playState
				})
			}
		}
	}
</script>
