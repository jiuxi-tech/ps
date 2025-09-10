<template>
    <!--  02-日历色块图  -->
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
    	name: 'FbecCalendarColor',
        mixins: [chartMixin],
        data () {
			// let time ='2021-12'
			//
			// let maxArr = [];
			//
			// let month = new Date(time).getMonth() + 1; //获取当前月
			// var year = new Date(time).getFullYear(); //获取当前年
			// var datas = []; //新建日期数组
			// var dataLength = new Date(year, month).toJSON().substring(0, 10).split('-')[2]; //获取当前年月日期最大值
			// for (var i = 0; i < Number(dataLength); i++) {
			// 	let months = month >= 10 ? month : '0' + month;
			// 	let day = i + 1 >= 10 ? i + 1 : '0' + (i + 1);
			// 	datas.push({ date: year + '-' + months + '-' + day, total: Math.floor(Math.random() * 100) }); //存入数组
			// }
			// let chartData = [];
			// datas.forEach((item) => {
			// 	maxArr.push(item.total);
			// 	chartData.push({
			// 		value: [item.date, item.total],
			// 	});
			// });
			// let thisMonth = time; //当前月份

            return {
                opt: {
                	legend: {
                		show: false,
                		data: []
					},
					tooltip: {
                		trigger: 'item'
					},
					calendar: {

						cellSize: [63, 36],
						range: [],
						itemStyle: {
							borderColor: '#2FA1FE',
							borderWidth: 1
						},
						splitLine: {
							show: false,
						},
						yearLabel: { show: false },
						monthLabel: {
							show: false,
						},
					},
					series: [
						// {
						// 	type: 'scatter',
						// 	coordinateSystem: 'calendar',
						// 	symbolSize: [63, 36],
						// 	symbol: 'reat',
						// 	label: {
						// 		show: true,
						// 		formatter: function (params) {
						// 			var d = echarts.number.parseDate(params.value[0]);
						// 			return d.getDate();
						// 		},
						// 		color: '#fff',
						// 		fontSize: 16,
						// 		textShadowColor: '#0B1131',
						// 		textShadowBlur: 0,
						// 		textShadowOffsetX: 1,
						// 		textShadowOffsetY: 1
						// 	},
						// 	data: [],
						// },
					],
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
            	const _this = this
				this.opt.series = []
				this.opt = merge(this.opt, val)

				for (let i in this.opt.series) {
					let item = Object.assign({
						type: 'scatter',
						coordinateSystem: 'calendar',
						symbolSize: [63, 36],
						symbol: 'reat',
						label: {
							show: true,
							formatter (params) {
								let d = _this.$echarts ?
									_this.$echarts.number.parseDate(params.value[0]) : params.value[0];
								return d.getDate();
							},
						},
					}, this.opt.series[i])
					this.opt.series[i] = item

					if (item.name) {
						this.opt.legend.data[i] = item.name
					}
				}

				this.chart.clear()
this.chart.setOption(this.opt)
            }
        }
    }
</script>
