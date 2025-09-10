<template>
	<div class="tx-demo-page fb-page-search">
		<fb-alert
			bordered
			type="danger"
			message="温馨提示："
			description="抱歉！变更信息没有通过审核，请点击右侧按钮进行信息修改！"
		>
			<template slot="actions">
				<fb-button type="primary">更改申请修改</fb-button>
			</template>
		</fb-alert>

		<div>
			<fb-row :gutter="gutter">
				<fb-col span="12">
					<div class="card-banner card-banner-1">
						<div>
							<div class="title">每日安全承诺</div>
							<div class="text">您今天尚未完成安全承诺任务，请进行任务上报！</div>
						</div>

						<button v-if="true">立即上报</button>
						<div class="icon" v-else></div>
					</div>
				</fb-col>
				<fb-col span="12">
					<div class="card-banner card-banner-2">
						<div>
							<div class="title">每日答题</div>
							<div class="text">您今天尚未完成答题任务，请进行答题任务！</div>
						</div>

						<button v-if="true">立即答题</button>
						<div class="icon" v-else></div>
					</div>
				</fb-col>
			</fb-row>
		</div>

		<div>
			<fb-row :gutter="gutter">
				<fb-col span="9">
					<fb-card>
						<div class="text-center">
							<fb-badge dot dotSize="12" :dotColor="colors.red_5" vertical="-1px"></fb-badge>
							<fb-text>日常检查7天内到期情况</fb-text>
						</div>
						<fb-row gutter="16" style="margin-top: 16px;">
							<fb-col span="8">
								<fb-cardx-breath type="danger" title="风险点待辨识" breath ellipsis>123</fb-cardx-breath>
							</fb-col>
							<fb-col span="8">
								<fb-cardx-breath type="danger" title="风险点待巡查" breath ellipsis>123</fb-cardx-breath>
							</fb-col>
							<fb-col span="8">
								<fb-cardx-breath type="danger" title="日常待检查" breath ellipsis>123</fb-cardx-breath>
							</fb-col>
						</fb-row>
					</fb-card>
				</fb-col>

				<fb-col span="6">
					<fb-card>
						<div class="text-center">
							<fb-badge dot dotSize="12" :dotColor="colors.blue_5" vertical="-1px"></fb-badge>
							<fb-text>隐患7天内到期情况</fb-text>
						</div>
						<fb-row gutter="16" style="margin-top: 16px;">
							<fb-col span="8">
								<fb-cardx-breath title="风险点待辨识" breath ellipsis>123</fb-cardx-breath>
							</fb-col>
							<fb-col span="8">
								<fb-cardx-breath title="风险点待巡查" breath ellipsis>123</fb-cardx-breath>
							</fb-col>
							<fb-col span="8">
								<fb-cardx-breath title="日常待检查" breath ellipsis>123</fb-cardx-breath>
							</fb-col>
						</fb-row>
					</fb-card>
				</fb-col>

				<fb-col span="9">
					<fb-card>
						<div class="text-center">
							<fb-badge dot dotSize="12" :dotColor="colors.orange_5" vertical="-1px"></fb-badge>
							<fb-text>证书3个月内到期情况</fb-text>
						</div>
						<fb-row gutter="16" style="margin-top: 16px;">
							<fb-col span="8">
								<fb-cardx-breath type="warning" title="风险点待辨识" breath ellipsis>123</fb-cardx-breath>
							</fb-col>
							<fb-col span="8">
								<fb-cardx-breath type="warning" title="风险点待巡查" breath ellipsis>123</fb-cardx-breath>
							</fb-col>
							<fb-col span="8">
								<fb-cardx-breath type="warning" title="日常待检查" breath ellipsis>123</fb-cardx-breath>
							</fb-col>
						</fb-row>
					</fb-card>
				</fb-col>
			</fb-row>
		</div>

		<div>
			<fb-row :gutter="gutter">
				<fb-col span="16">
					<fb-cardx :header="formData.val1" no-border>
						<div slot="tags">
							<fb-tag type="danger" effect="light">危化品类</fb-tag>
						</div>

						<fb-container radius="4px" :background="colors.blue_1" pt="12px">
							<fb-property colon label-width="140px" mode="form">
								<fb-row>
									<fb-col span="12">
										<fb-property-item label="安全负责人" ellipsis>
											{{formData.val1}}
										</fb-property-item>
									</fb-col>
									<fb-col span="12">
										<fb-property-item label="安全负责人手机" ellipsis>
											0571-87526326
										</fb-property-item>
									</fb-col>
								</fb-row>

								<fb-row>
									<fb-col span="12">
										<fb-property-item label="行业分类" ellipsis>
											危险化学品生产企业
										</fb-property-item>
									</fb-col>
									<fb-col span="12">
										<fb-property-item label="监管等级" ellipsis>
											区县级
										</fb-property-item>
									</fb-col>
								</fb-row>

								<fb-row>
									<fb-col span="12">
										<fb-property-item label="监管部门" ellipsis>
											XXX应急管理局
										</fb-property-item>
									</fb-col>
									<fb-col span="12">
										<fb-property-item label="经营范围" ellipsis>
											乙醇、汽油、柴油等
										</fb-property-item>
									</fb-col>
								</fb-row>

								<fb-row>
									<fb-col span="24">
										<fb-property-item label="生产经营地址" ellipsis>
											东营市XX区XX街道XX路123号 <fb-icon name="location-fill" :color="colors.red_6"></fb-icon>
										</fb-property-item>
									</fb-col>
								</fb-row>

							</fb-property>
						</fb-container>

					</fb-cardx>
				</fb-col>
				<fb-col span="8">
					<fb-card header="通知公告" height="225" bodyOverflow="y">
						<template slot="actions">
							<fb-link type="primary" label="更多"></fb-link>
						</template>

						<fb-table-layout style="margin-bottom: 8px">
							<fb-table-layout-cell ellipsis width="20px">
								<fb-badge dot vertical="2px"></fb-badge>
							</fb-table-layout-cell>
							<fb-table-layout-cell ellipsis value="222222222222222222222222222222222222222222222222222222222222222"></fb-table-layout-cell>
							<fb-table-layout-cell align="right" width="100px" value="2010-02-01"></fb-table-layout-cell>
						</fb-table-layout>

						<fb-table-layout v-for="(item,i) in 9" :key="i" style="margin-bottom: 8px">
							<fb-table-layout-cell ellipsis width="20px">
								<fb-badge dot :dot-color="colors.blue_9" vertical="2px"></fb-badge>
							</fb-table-layout-cell>
							<fb-table-layout-cell ellipsis value="222222222222222222222222222222222222222222222222222222222222222"></fb-table-layout-cell>
							<fb-table-layout-cell align="right" width="100px" value="2010-02-01"></fb-table-layout-cell>
						</fb-table-layout>
					</fb-card>
				</fb-col>
			</fb-row>
		</div>

		<div>
			<fb-row :gutter="gutter">
				<fb-col span="8">
					<fb-card header="风险点等级分布情况" height="240">
						<fb-echarts :options="ecOption1" autoresize></fb-echarts>
					</fb-card>
				</fb-col>
				<fb-col span="8">
					<fb-card header="隐患排查情况" height="240">
						<template slot="actions">
							<fb-radio-group @on-change="handleChange" button :data="ridioData" value="1"/>
						</template>
						<fb-echarts :options="ecOption2" autoresize></fb-echarts>
					</fb-card>
				</fb-col>
				<fb-col span="8">
					<fb-card header="应急预案情况" height="240">
						<fb-echarts :options="ecOption3" autoresize></fb-echarts>
					</fb-card>
				</fb-col>
			</fb-row>
		</div>


		<fb-cardx height="323">
			<fb-container slot="tags" width="360px">
				<fb-tabs @on-tab-choose="handleTabChoose">
					<fb-tab label="各月度隐患排查情况"></fb-tab>
					<fb-tab label="各月度日常检查情况"></fb-tab>
				</fb-tabs>
			</fb-container>

			<fb-container slot="actions" width="140px">
				<fb-select v-model="selectValue"
						   :placeholder="'请选择'"
						   :data="selectNodes"
						   :reader="{value:'id', label: 'name'}" clearable />
			</fb-container>

			<fb-echarts :options="ecOption4" autoresize theme="macarons" @click="eC"></fb-echarts>
		</fb-cardx>

	</div>

</template>

<script>
	import { sysconfig } from '../../../../../project.config';

	export default {
		name: 'index',
		// 接收父组件的传参
		props: {},
		components: {},
		data () {
			let xAxisDefault = {
				axisLabel: {
					interval: 0,
					color: '#666666'
				},
				axisTick: {
					show: false
				},
				axisLine: {
					lineStyle: {
						color: '#BFBFBF'
					}
				}
			}

			let yAxisDefault = {
				axisLabel: {
					color: '#666666'
				},
				axisTick: {
					show: false
				},
				splitLine: {
					lineStyle: {
						type: 'dotted',
						color: '#BFBFBF'
					}
				}
			}

			let month = []
			for (var i = 1; i < 13; i++) {
				month.push(i + '月')
			}
			return {
				gutter: sysconfig.gutter,
				formData: {
					val1: '王名',
					val2: '王名',
					val3: '王名',
				},
				ecOption1: {
					tooltip: {
						trigger: 'item'
					},
					series: [
						{
							name: '访问来源',
							type: 'pie',
							radius: ['40%', '70%'],
							data: [],
							label: {
								formatter: '{b}: {c} {d}%'
							},
							emphasis: {
								itemStyle: {
									shadowBlur: 10,
									shadowOffsetX: 0,
									shadowColor: 'rgba(0, 0, 0, 0.5)'
								}
							}
						}
					]
				},
				ecOption2: {
					grid: {
						top: 10,
						bottom: 30
					},
					xAxis: {
						type: 'category',
						...xAxisDefault,
						data: ['企业自查一般隐患', '政府排查一般隐患', '重大隐患']
					},
					yAxis: {
						type: 'value',
						...yAxisDefault
					},
					series: [{
						data: [120, 200, 150],
						type: 'bar',
						barWidth: 24,
						itemStyle: {
							borderRadius: 4
						}
					}]
				},
				ecOption3: {
					color: ['#A371EA', '#2FA1FE', '#FC9C3B', '#35D0CA'],
					tooltip: {
						trigger: 'item'
					},
					series: [
						{
							name: '访问来源',
							type: 'pie',
							radius: ['40%', '70%'],
							data: [
								{value: 1, name: '综合预案'},
								{value: 2, name: '专项预案'},
								{value: 3, name: '现场预案'},
								{value: 4, name: '其他预案'},
							],
							label: {
								formatter: '{b}: {c} {d}%'
							},
							emphasis: {
								itemStyle: {
									shadowBlur: 10,
									shadowOffsetX: 0,
									shadowColor: 'rgba(0, 0, 0, 0.5)'
								}
							}
						}
					]
				},
				ecOption4: {
					grid: {
						top: 40,
						right: 30,
						bottom: 30,
						left: 60
					},
					legend: {
						data: ['隐患排查数', '隐患整改数']
					},
					xAxis: {
						type: 'category',
						...xAxisDefault,
						data: month
					},
					yAxis: {
						type: 'value',
						name: '单位：个',
						...yAxisDefault
					},
					series: [{
						name: '隐患排查数',
						data: [120, 200, 150],
						type: 'bar',
						barWidth: 24,
						itemStyle: {
							borderRadius: 4
						}
					},{
						name: '隐患整改数',
						data: [30, 20, 15],
						type: 'bar',
						barWidth: 24,
						itemStyle: {
							borderRadius: 4
						}
					}]
				},
				ridioData: [
					{value: '1', label: '本月'},
					{value: '2', label: '本年'},
					{value: '3', label: '全部'},
				],
				selectValue: 1,
				selectNodes: [
					{
						id: 1,
						name: '2021年',
					}, {
						id: 2,
						name: '2020年',
					}, {
						id: 3,
						name: '2019年',
					},
				],
			}
		},
		// 初始化方法
		mounted () {
			// setTimeout(() => {
			// 	this.ecOption1.series[0].data = [
			// 		{value: 4, name: '综合预案'},
			// 		{value: 3, name: '专项预案'},
			// 		{value: 3, name: '现场预案'},
			// 		{value: 3, name: '其他预案'},
			// 	]
			// }, 0)
		},

		// 方法
		methods: {
			eC (e) {
				console.log(e)
			},
			handleChange (val) {
				let data = [120, 200, 150]
				data[0] += parseFloat(val + 0)
				this.ecOption2.series[0].data = data
			},
			handleTabChoose (tab) {
				let data = []
				for (var i = 0; i < 12; i++) {
					let num = Math.floor(Math.random() * 10)
					data.push(num)
				}

				if (tab.label === '各月度隐患排查情况') {
					this.ecOption4.series[0].data = data
				} else {
					this.ecOption4.series[0].data = data
				}
			}
		},
	}
</script>

<style lang="less" scoped>
	@import "@/assets/styles/common.less";
	.tx-demo-page {

		/deep/ .@{FbUiPrefix}-alert__actions {
			top: 21px;
		}

		.text-center {
			text-align: center;

			.@{FbUiPrefix}-badge {
				margin-right: 8px;
			}
		}

		.@{FbUiPrefix}-property--form .@{FbUiPrefix}-property-item {
			padding-bottom: 12px;
		}

		/deep/ .@{FbUiPrefix}-tabs .@{FbUiPrefix}-tabs-header .@{FbUiPrefix}-tabs-header-container {
			border-bottom: 0px;
		}

		.card-banner {
			padding: 10px 16px 10px 142px;
			border-radius: 4px;
			color: #fff;
			position: relative;

			.title {
				font-size: 14px;
				line-height: 22px;
			}

			.text {
				font-size: 12px;
				line-height: 22px;
			}

			> button {
				width: 84px;
				height: 30px;
				background: #2FA1FE;
				border-radius: 4px;
				outline: none;
				border: none;
				cursor: pointer;
				position: absolute;
				top: 17px;
				right: 16px;

				&:hover {
					opacity: .8;
				}
			}

			.icon {
				position: absolute;
				top: 8px;
				right: 24px;
				width: 55px;
				height: 48px;
			}
		}

		.card-banner-1 {
			background: #8AD2FE url("../../../assets/img/index/bg_banner1.png") no-repeat;

			.icon {
				background: url("../../../assets/img/index/bg_banner1_icon.png") no-repeat;
			}
		}
		.card-banner-2 {
			background: #D8B7FB url("../../../assets/img/index/bg_banner2.png") no-repeat;

			> button {
				background: #FC9C3B;
			}

			.icon {
				background: url("../../../assets/img/index/bg_banner2_icon.png") no-repeat;
			}
		}
	}

</style>
