<template>
	<div class="tx-demo-page fb-page-search">
		<div>
			<fb-row :gutter="gutter">
				<fb-col span="8">
					<div class="card-banner card-banner-1">
						<div>
							<div class="title">待整改复查</div>
							<div class="text">
								<span>32</span>家
							</div>
						</div>
					</div>
				</fb-col>
				<fb-col span="8">
					<div class="card-banner card-banner-2">
						<div>
							<div class="title">待注册审核</div>
							<div class="text">
								<span>32</span>家
							</div>
						</div>
					</div>
				</fb-col>
				<fb-col span="8">
					<div class="card-banner card-banner-3">
						<div>
							<div class="title">待变更审核</div>
							<div class="text">
								<span>32</span>家
							</div>
						</div>
					</div>
				</fb-col>
			</fb-row>
		</div>

		<div>
			<fb-row :gutter="gutter">
				<fb-col span="8">
					<fb-card>
						<div class="text-center">
							<fb-text>每日承诺</fb-text>
						</div>
						<fb-row gutter="16" style="margin-top: 16px;">
							<fb-col span="6">
								<fb-cardx-breath title="应承诺" :bgColor="colors.blue_1" ellipsis>123</fb-cardx-breath>
							</fb-col>
							<fb-col span="6">
								<fb-cardx-breath title="已承诺" :bgColor="colors.lime_1" ellipsis>123</fb-cardx-breath>
							</fb-col>
							<fb-col span="6">
								<fb-cardx-breath title="未承诺" :bgColor="colors.red_1" ellipsis>123</fb-cardx-breath>
							</fb-col>
							<fb-col span="6">
								<fb-cardx-breath title="承诺率" :bgColor="colors.orange_1" ellipsis>123</fb-cardx-breath>
							</fb-col>
						</fb-row>
					</fb-card>
				</fb-col>

				<fb-col span="8">
					<fb-card>
						<div class="text-center">
							<fb-text>每日答题</fb-text>
						</div>
						<fb-row gutter="16" style="margin-top: 16px;">
							<fb-col span="6">
								<fb-cardx-breath title="应答题" :bgColor="colors.blue_1" ellipsis>123</fb-cardx-breath>
							</fb-col>
							<fb-col span="6">
								<fb-cardx-breath title="已答题" :bgColor="colors.lime_1" ellipsis>123</fb-cardx-breath>
							</fb-col>
							<fb-col span="6">
								<fb-cardx-breath title="未答题" :bgColor="colors.red_1" ellipsis>123</fb-cardx-breath>
							</fb-col>
							<fb-col span="6">
								<fb-cardx-breath title="答题率" :bgColor="colors.orange_1" ellipsis>123</fb-cardx-breath>
							</fb-col>
						</fb-row>
					</fb-card>
				</fb-col>

				<fb-col span="8">
					<fb-card>
						<div class="text-center">
							<fb-text>证照详情</fb-text>
						</div>
						<fb-row gutter="16" style="margin-top: 16px;">
							<fb-col span="12">
								<fb-cardx-breath type="warning" title="证照到期" ellipsis>123</fb-cardx-breath>
							</fb-col>
							<fb-col span="12">
								<fb-cardx-breath type="danger" title="证照超期" ellipsis>123</fb-cardx-breath>
							</fb-col>
						</fb-row>
					</fb-card>
				</fb-col>
			</fb-row>
		</div>

		<fb-card header="企业安全码" height="324">
			<fb-row gutter="32">
				<fb-col span="8">
					<fb-row gutter="16" verticalGutter="16">
						<fb-col span="12" v-for="(codeItem, idx) in enterpriseCodes" :key="idx">
							<fb-card :bodyStyle="{height: '112px'}">
								<fb-avatar-box icon="insurance-shield" size="40" alt="圆形显示" :color="codeItem.color" :backgroundColor="codeItem.bg">
									<fb-text>{{codeItem.text}}</fb-text>
									<div>
										<fb-text>本月</fb-text>
										<fb-text class="text-color-danger">{{codeItem.warn}}</fb-text>
									</div>
									<div>
										<fb-text class="text-bold-20">{{codeItem.num}}</fb-text>
										<fb-text>个</fb-text>
									</div>
								</fb-avatar-box>
							</fb-card>
						</fb-col>
					</fb-row>
				</fb-col>
				<fb-col span="16">
					<fb-container position="absolute" top="0" left="0" z-index="100">
						<fb-radio-group button value="1">
							<fb-radio label="区域分布" value="1"></fb-radio>
							<fb-radio label="行业分布" value="2"></fb-radio>
						</fb-radio-group>
					</fb-container>
					<fb-echarts style="height: 244px" :options="ecOption1" autoresize @click="eC"></fb-echarts>
				</fb-col>
			</fb-row>
		</fb-card>

		<fb-cardx no-header-border>
			<fb-tabs slot="header">
				<fb-tab label="企业行业统计"></fb-tab>
				<fb-tab label="企业区域统计"></fb-tab>
				<fb-tab label="企业规模统计"></fb-tab>
			</fb-tabs>
			<fb-row gutter="16">
				<fb-col span="6">
					<fb-card :bodyStyle="{textAlign: 'center', height: '214px'}">
						<fb-avatar-box layout-type="center" linearIcon="enterprise">
							<div style="margin-bottom: 8px">企业总数</div>
							<div><fb-flop maxLength="4" num="111"></fb-flop></div>
						</fb-avatar-box>
					</fb-card>
				</fb-col>
				<fb-col span="18">
					<fb-row gutter="16" vertical-gutter="16">
						<fb-col style="width: 20%" span="4" v-for="item in 10" :key="item">
							<fb-card>
								<fb-table-layout>
									<fb-table-layout-cell>
										<fb-text>危险化学品</fb-text>
									</fb-table-layout-cell>
									<fb-table-layout-cell align="right">
										<fb-text class="text-bold-20">22</fb-text>
										<fb-text>家</fb-text>
									</fb-table-layout-cell>
								</fb-table-layout>
								<div>
									<fb-progress percent="50" status="active" stroke-color="#35D0CA" stroke-width="6" :show-info="false"></fb-progress>
								</div>
								<fb-table-layout>
									<fb-table-layout-cell>
										<fb-text>占比</fb-text>
									</fb-table-layout-cell>
									<fb-table-layout-cell align="right">
										<fb-text>13.68%</fb-text>
									</fb-table-layout-cell>
								</fb-table-layout>
							</fb-card>
						</fb-col>
					</fb-row>
				</fb-col>
			</fb-row>
		</fb-cardx>

		<div>
			<fb-row :gutter="gutter">
				<fb-col span="16">
					<fb-card header="涉及危化品企业">
						<div slot="actions">
							<fb-flop num="112" brandSize="s">
								<span slot="prefix">共</span>
								<span slot="suffix">家</span>
							</fb-flop>
						</div>
						<fb-row gutter="16" vertical-gutter="16">
							<fb-col :span="renderMajorSpan(idx)" v-for="(item, idx) in 10" :key="item">
								<fb-card>
									<fb-avatar-box icon="major-hazard-installations" size="40" alt="圆形显示" :color="colors.blue_5" :backgroundColor="colors.blue_1"
												   :linearIcon="renderMajorSpan(idx) == 12 ? 'major-hazard-installations' : ''">
										<div>
											<fb-text>有限空间</fb-text>
											<div>
												<fb-text class="text-bold-20">66</fb-text>
												<fb-text>家</fb-text>
											</div>
										</div>
									</fb-avatar-box>
								</fb-card>
							</fb-col>
						</fb-row>
					</fb-card>
				</fb-col>
				<fb-col span="8">
					<fb-card header="涉及三场所三企业">
						<div slot="actions">
							<fb-flop num="378" brandSize="s">
								<span slot="prefix">共</span>
								<span slot="suffix">家</span>
							</fb-flop>
						</div>

						<fb-row gutter="16" vertical-gutter="16">
							<fb-col span="12" v-for="item in 6" :key="item">
								<fb-card>
									<fb-avatar-box icon="major-hazard-installations" size="40" alt="圆形显示" :color="colors.blue_5" :backgroundColor="colors.blue_1">
										<div>
											<fb-text>有限空间</fb-text>
											<div>
												<fb-text class="text-bold-20">66</fb-text>
												<fb-text>家</fb-text>
											</div>
										</div>
									</fb-avatar-box>
								</fb-card>
							</fb-col>
						</fb-row>
					</fb-card>
				</fb-col>
			</fb-row>
		</div>

		<fb-card header="执法检查统计">
			<div slot="actions">
				<fb-radio-group button value="1">
					<fb-radio label="本月" value="1"></fb-radio>
					<fb-radio label="本年" value="2"></fb-radio>
				</fb-radio-group>
			</div>
			<fb-row gutter="16">
				<fb-col span="8">
					<fb-row gutter="16">
						<fb-col span="8">
							<fb-card :bodyStyle="{textAlign: 'center', height: '294px'}">
								<fb-avatar-box
									layout-type="center"
									icon="insurance-shield"
									size="40"
									alt="圆形显示"
									:color="colors.cyan_5"
									:backgroundColor="colors.cyan_1"
									linearType="cyan"
									linearIcon="insurance-shield">
									<div style="margin: 8px 0">已检查</div>
									<div>
										<fb-text class="text-bold-20">59</fb-text>
									</div>
									<div>家次</div>
								</fb-avatar-box>
							</fb-card>
						</fb-col>
						<fb-col span="16">
							<fb-row vertical-gutter="16">
								<fb-col span="24" v-for="item in 4" :key="item">
									<fb-card>
										<fb-table-layout>
											<fb-table-layout-cell>
												<fb-text>责令整改</fb-text>
											</fb-table-layout-cell>
											<fb-table-layout-cell align="right">
												<fb-text class="text-bold-20">22</fb-text>
												<fb-text>家次</fb-text>
											</fb-table-layout-cell>
										</fb-table-layout>
									</fb-card>
								</fb-col>
							</fb-row>
						</fb-col>
					</fb-row>
				</fb-col>
				<fb-col span="16">
					<fb-row gutter="16" vertical-gutter="16">
						<fb-container position="absolute" top="0" left="0" z-index="100">
							<fb-radio-group button value="1" @on-change="enforceRadioClick">
								<fb-radio label="执法检查区域统计" value="1"></fb-radio>
								<fb-radio label="整改复查区域统计" value="2"></fb-radio>
							</fb-radio-group>
						</fb-container>
						<fb-echarts style="height: 294px" :options="ecOption2" autoresize @click="eC"></fb-echarts>
					</fb-row>
				</fb-col>
			</fb-row>
		</fb-card>

		<fb-cardx  no-header-border>
			<fb-tabs slot="header">
				<fb-tab label="风险点统计"></fb-tab>
				<fb-tab label="当月风险巡查"></fb-tab>
			</fb-tabs>
			<fb-row gutter="32">
				<fb-col span="8">
					<fb-row gutter="16" verticalGutter="16">
						<fb-row gutter="16" verticalGutter="16">
							<fb-col span="12" v-for="(codeItem, idx) in enterpriseCodes" :key="idx">
								<fb-card :bodyStyle="{height: '112px'}">
									<fb-avatar-box icon="insurance-shield" size="40" alt="圆形显示" :color="codeItem.color" :backgroundColor="codeItem.bg">
										<fb-text>{{codeItem.text}}</fb-text>
										<div>
											<fb-text>本月</fb-text>
											<fb-text class="text-color-danger">{{codeItem.warn}}</fb-text>
										</div>
										<div>
											<fb-text class="text-bold-20">{{codeItem.num}}</fb-text>
											<fb-text>个</fb-text>
										</div>
									</fb-avatar-box>
								</fb-card>
							</fb-col>
						</fb-row>
					</fb-row>
				</fb-col>
				<fb-col span="16">
					<fb-container position="absolute" top="0" left="0" z-index="100">
						<fb-radio-group button value="1">
							<fb-radio label="按风险点统计" value="1"></fb-radio>
							<fb-radio label="按涉及企业统计" value="2"></fb-radio>
						</fb-radio-group>
					</fb-container>
					<fb-echarts style="height: 244px" :options="ecOption3" autoresize @click="eC"></fb-echarts>
				</fb-col>
			</fb-row>
		</fb-cardx>

		<div>
			<fb-row :gutter="gutter">
				<fb-col span="8">
					<fb-cardx no-header-border>
						<fb-tabs slot="header">
							<fb-tab label="隐患统计"></fb-tab>
							<fb-tab label="隐患来源"></fb-tab>
							<fb-tab label="未整改排名"></fb-tab>
						</fb-tabs>
						<fb-row gutter="16">
							<fb-col span="12">
								<fb-row vertical-gutter="16">
									<fb-col span="24" v-for="item in 2" :key="item">
										<fb-card>
											<fb-avatar-box linearIcon="accident-information">
												<div>
													<fb-text>挂牌督办</fb-text>
													<div>
														<fb-text class="about-box-warn-info">本月</fb-text>
														<fb-text class="text-color-success">+2</fb-text>
													</div>
													<div>
														<fb-text class="text-bold-20">16</fb-text>
														<fb-text>个</fb-text>
													</div>
												</div>
											</fb-avatar-box>
										</fb-card>
									</fb-col>
								</fb-row>
							</fb-col>
							<fb-col span="12">
								<fb-row vertical-gutter="16">
									<fb-col span="24" v-for="item in 3" :key="item">
										<fb-card>
											<fb-table-layout>
												<fb-table-layout-cell>
													<fb-text>已整改</fb-text>
												</fb-table-layout-cell>
												<fb-table-layout-cell align="right">
													<fb-text class="text-bold-20">22</fb-text>
													<fb-text>个</fb-text>
												</fb-table-layout-cell>
											</fb-table-layout>
										</fb-card>
									</fb-col>
								</fb-row>
							</fb-col>
						</fb-row>
					</fb-cardx>
				</fb-col>
				<fb-col span="16">
					<fb-cardx no-header-border>
						<fb-tabs slot="header">
							<fb-tab label="本月排查情况"></fb-tab>
							<fb-tab label="区域隐患分布"></fb-tab>
							<fb-tab label="行业隐患分布"></fb-tab>
						</fb-tabs>
						<fb-echarts style="height: 226px" :options="ecOption3" autoresize @click="eC"></fb-echarts>
					</fb-cardx>
				</fb-col>
			</fb-row>
		</div>

		<div>
			<fb-row :gutter="gutter">
				<fb-col span="8">
					<fb-cardx no-header-border height="304">
						<fb-tabs slot="header">
							<fb-tab label="隐患统计"></fb-tab>
							<fb-tab label="隐患来源"></fb-tab>
							<fb-tab label="未整改排名"></fb-tab>
						</fb-tabs>
						<fb-simple-table
							style="height: 100%"
							:columns="table1.columns"
							:data="table1.data"
							:multiple="false">
							<!-- <template v-slot:entUnifiedCode="{name, row}">
								<fb-icon name="file-text-fill" :color="colors.grey_8" style="margin-right: 6px;"></fb-icon>
								<fb-link :label="row.entUnifiedCode"></fb-link>
							</template>
							<template v-slot:legalRepr="{name, row}">
								<fb-icon name="warning-fill" :color="colors.orange_6" style="margin-right: 6px;"></fb-icon>
								<fb-link :label="row.legalRepr"></fb-link>
							</template>
							<template v-slot:legalReprTel="{name, row}">
								<fb-icon name="file-text-fill" :color="colors.orange_6" style="margin-right: 6px;"></fb-icon>
								<fb-link :label="row.legalReprTel"></fb-link>
							</template> -->
						</fb-simple-table>
					</fb-cardx>
				</fb-col>
				<fb-col span="16">
					<fb-cardx no-header-border>
						<fb-tabs slot="header">
							<fb-tab label="本月排查情况"></fb-tab>
							<fb-tab label="区域隐患分布"></fb-tab>
							<fb-tab label="行业隐患分布"></fb-tab>
						</fb-tabs>
						<fb-echarts style="height: 226px" :options="ecOption4" autoresize @click="eC"></fb-echarts>
					</fb-cardx>
				</fb-col>
			</fb-row>
		</div>

		<fb-cardx no-header-border>
			<fb-tabs slot="header">
				<fb-tab label="企业行业统计"></fb-tab>
				<fb-tab label="企业区域统计"></fb-tab>
				<fb-tab label="企业规模统计"></fb-tab>
			</fb-tabs>
			<fb-row gutter="16">
				<fb-col span="6">
					<fb-card :bodyStyle="{textAlign: 'center', height: '214px'}">
						<fb-avatar-box layout-type="center" linearIcon="enterprise">
							<div style="margin-bottom: 8px">企业总数</div>
							<div><fb-flop maxLength="4" num="111"></fb-flop></div>
						</fb-avatar-box>
					</fb-card>
				</fb-col>
				<fb-col span="18">
					<fb-row gutter="16" vertical-gutter="16">
						<fb-col span="6" v-for="item in 4" :key="item">
							<fb-card>
								<fb-echarts class="re-echart-blue" style="height: 154px;" :options="reOptions(colors.blue_5, item *10)" autoresize></fb-echarts>
								<fb-table-layout>
									<fb-table-layout-cell>
										<fb-text>占比</fb-text>
									</fb-table-layout-cell>
									<fb-table-layout-cell align="right">
										<fb-text class="text-bold-20">13.68</fb-text>%
									</fb-table-layout-cell>
								</fb-table-layout>
							</fb-card>
						</fb-col>
					</fb-row>
				</fb-col>
			</fb-row>
		</fb-cardx>
	</div>

</template>

<script>
	import { colors } from '@fb/fb-ui/packages/theme/pc3/src/common/colors'
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
				// 企业安全码
				enterpriseCodes: [
					{color: colors.red_5, bg: colors.red_1, text: '红码', num: 22, warn: '+2'},
					{color: colors.orange_5, bg: colors.orange_1, text: '橙码', num: 22, warn: '+2'},
					{color: colors.yellow_6, bg: colors.yellow_1, text: '黄码', num: 22, warn: ''},
					{color: colors.blue_5, bg: colors.blue_2, text: '蓝码', num: 22, warn: ''},
				],
				ecOption1: {
					grid: {
						top: 50,
						bottom: 30,
						left: 40,
						right: 20,
					},
					color: [colors.red_5, colors.orange_5, colors.yellow_6, colors.blue_5],
					tooltip: {
						trigger: 'item',
					},
					legend: {
						data: ['红码', '橙码', '黄码', '蓝码'],
						left: 'right'
					},
					xAxis: {
						type: 'category',
						...xAxisDefault,
						data: month
					},
					yAxis: {
						type: 'value',
						...yAxisDefault
					},
					series: [
						{
							name: '红码',
							data: [120, 200, 150],
							type: 'bar',
							barWidth: 10,
							itemStyle: {
								borderRadius: 4
							}
						},{
							name: '橙码',
							data: [30, 20, 15],
							type: 'bar',
							barWidth: 10,
							itemStyle: {
								borderRadius: 4
							}
						},{
							name: '黄码',
							data: [120, 200, 150],
							type: 'bar',
							barWidth: 10,
							itemStyle: {
								borderRadius: 4
							}
						},{
							name: '蓝码',
							data: [30, 20, 15],
							type: 'bar',
							barWidth: 10,
							itemStyle: {
								borderRadius: 4
							}
						}
					]
				},
				// 执法检查统计
				ecOption2: {
					grid: {
						top: 50,
						bottom: 20,
						left: 40,
						right: 20,
					},
					color: ['#35D0CA', '#FFB500'],
					tooltip: {
						trigger: 'item',
					},
					legend: {
						data: ['已复查家次', '未复查家次'],
						left: 'right'
					},
					xAxis: {
						type: 'category',
						...xAxisDefault,
						data: month
					},
					yAxis: {
						type: 'value',
						...yAxisDefault
					},
					series: [
						{
							name: '已复查家次',
							stack: 'total',
							data: [120, 200, 150],
							type: 'bar',
							barWidth: 10,
							itemStyle: {
								borderRadius: 1
							}
						},{
							name: '未复查家次',
							stack: 'total',
							data: [30, 20, 15],
							type: 'bar',
							barWidth: 10,
							itemStyle: {
								borderRadius: 1
							}
						}
					]
				},
				// 风险点统计
				ecOption3: {
					grid: {
						top: 50,
						bottom: 30,
						left: 40,
						right: 20,
					},
					color: [colors.red_5, colors.orange_5, colors.yellow_6, colors.blue_5],
					tooltip: {
						trigger: 'item',
					},
					legend: {
						data: ['重大风险', '较大风险', '一般风险', '低风险'],
						left: 'right'
					},
					xAxis: {
						type: 'category',
						...xAxisDefault,
						data: month
					},
					yAxis: {
						type: 'value',
						...yAxisDefault
					},
					series: [
						{
							name: '重大风险',
							data: [120, 200, 150],
							type: 'bar',
							barWidth: 10,
							itemStyle: {
								borderRadius: 4
							}
						},{
							name: '较大风险',
							data: [30, 20, 15],
							type: 'bar',
							barWidth: 10,
							itemStyle: {
								borderRadius: 4
							}
						},{
							name: '一般风险',
							data: [120, 200, 150],
							type: 'bar',
							barWidth: 10,
							itemStyle: {
								borderRadius: 4
							}
						},{
							name: '低风险',
							data: [30, 20, 15],
							type: 'bar',
							barWidth: 10,
							itemStyle: {
								borderRadius: 4
							}
						}
					]
				},
				// line
				ecOption4: {
					grid: {
						top: 50,
						bottom: 30,
						left: 40,
						right: 20,
					},
					color: [colors.blue_5],
					tooltip: {
						trigger: 'axis',
					},
					xAxis: {
						type: 'category',
						...xAxisDefault,
						data: month
					},
					yAxis: {
						type: 'value',
						...yAxisDefault
					},
					series: [
						{
							name: '百分比',
							data: [120, 200, 150],
							type: 'line',
							label: {
								show: true,
								formatter: '{c}%'
							},
							areaStyle: {
								color: {
									type: 'linear',
									x: 0,
									y: 0,
									x2: 0,
									y2: 1,
									colorStops: [{
										offset: 0, color: 'rgba(2, 132, 254, 0.6)' // 0% 处的颜色
									}, {
										offset: 1, color: 'rgba(2, 132, 254, 0)' // 100% 处的颜色
									}],
									global: false // 缺省为 false
								}
							}
						}
					]
				},
				// Table1列
				table1: {
					columns: [
						{
							name: 'entId',
							label: 'id',
							hidden: true,
							width: 1,
						}, {
							name: 'entFullName',
							label: '企业名称',
							sortable: false,
							align: 'left',
						}, {
							name: 'prodAddrName',
							label: '巡查率',
							sortable: false,
							align: 'right',
						}
					],

					data: [
						{
							entId: '1',
							entFullName: '骚朱11',
							prodAddrName: '迎春ooo',
						}
					]
				},

			}
		},
		// 初始化方法
		mounted () {
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
			},
			renderMajorSpan(idx) {
				if (idx == 0 || idx == 1) {
					return 12
				} else {
					return 6
				}
			},
			enforceRadioClick (val) {
				console.log(val)
			},
			// 环形图
			reOptions (color, data) {
				return {
					title: {
						text: data + '%',
						textStyle: {
							color: '#111',
							fontSize: 24
						},
						subtext: '占比',
						subtextStyle: {
							color: '#111',
							fontSize: 13
						},
						itemGap: 10,
						left: 'center',
						top: '36%'
					},
					tooltip: {
						formatter: function (params) {
							return '<span>'+data+'%</span>';
						}
					},
					angleAxis: {
						max: 100,
						clockwise: true, // 逆时针
						// 隐藏刻度线
						show: false
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
						radius: '180%' //图形大小
					},
					series: [{
						type: 'bar',
						data: [data],
						showBackground: true,
						backgroundStyle: {
							color: 'rgba(18, 26, 35, 0.2)',
						},
						coordinateSystem: 'polar',
						roundCap: true,
						barWidth: 10,
						itemStyle: {
							normal: {
								opacity: 1,
								color: color,
							}
						},
					}]
				}
			},
		},
	}
</script>

<style lang="less" scoped>
	@import "@/assets/styles/common.less";
	.tx-demo-page {

		.text-bold-20 {
			font-size: 20px;
			font-weight: bold;
		}
		.icon-bg-bottom {
			position: absolute;
			left: 0;
			bottom: -2px;
			width: 100%;
		}
		.icon-bg-bottom-right {
			position: absolute;
			right: 16px;
			bottom: -2px;
		}

		.icon-linear {
			opacity: 0.6;
			background-image: -webkit-linear-gradient(top, #D0E8FF, #FEFEFF);
			-webkit-background-clip: text;
			-webkit-text-fill-color: transparent;
		}
		.icon-linear-cyan {
			opacity: 0.6;
			background-image: -webkit-linear-gradient(top, #DAF7F6, #FEFEFF);
			-webkit-background-clip: text;
			-webkit-text-fill-color: transparent;
		}
		.icon-linear-danger {
			opacity: 0.6;
			background-image: -webkit-linear-gradient(top, #FFE1E1, #FEFEFF);
			-webkit-background-clip: text;
			-webkit-text-fill-color: transparent;
		}

		.text-center {
			text-align: center;

			.@{FbUiPrefix}-badge {
				margin-right: 8px;
			}
		}

		.card-banner {
			height: 80px;
			padding: 16px 16px 10px 88px;
			border-radius: 4px;
			color: #fff;
			position: relative;

			.title {
				font-size: 14px;
				line-height: 22px;
			}

			.text {
				font-size: 13px;
				line-height: 22px;

				span {
					font-size: 20px;
					font-weight: bold;
				}
			}
		}

		.card-banner-1 {
			background: #5DC0FF url("../../../assets/img/index/bg_banner1_blue.png") no-repeat;
		}
		.card-banner-2 {
			background: #FFD298 url("../../../assets/img/index/bg_banner1_orange.png") no-repeat;
		}
		.card-banner-3 {
			background: #DCBDFD url("../../../assets/img/index/bg_banner1_purple.png") no-repeat;
		}

		.about-box {
			>div {
				float: left;

				&:first-child {
					margin-right: 12px;
				}
			}

			&-warn-info {
				font-size: 12px;
				margin-right: 8px;
			}
			&-warn {
				color: #FB544E;
			}
			&-num {
				font-size: 20px;
				font-weight: bold;
				margin-right: 2px;
			}
		}

		.ly-layout {
			>div:first-child {
				float: left;
			}
			>div:last-child {
				float: right;
			}
		}

		.re-echart-blue {
			background: url("../../../assets/img/index/icon-scale-fill-blue.png") no-repeat center;
		}
		.re-echart-red {
			background: url("../../../assets/img/index/icon-scale-fill-red.png") no-repeat center;
		}
		.re-echart-yellow {
			background: url("../../../assets/img/index/icon-scale-fill-yellow.png") no-repeat center;
		}
		.re-echart-green {
			background: url("../../../assets/img/index/icon-scale-fill-green.png") no-repeat center;
		}
	}

</style>
