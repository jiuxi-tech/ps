<template>

	<div class="tx-demo-page fb-page-search">
		<fb-button @on-click="randomData">随机</fb-button>
		<fb-radio-group v-model="radioValue" v-if="radioData.length > 0" button :data="radioData"></fb-radio-group>
<!--		<fb-row>-->
<!--			<fb-col v-for="item in radioData" :key="item.value">-->
<!--				{{ item.label }}-->
<!--			</fb-col>-->
<!--		</fb-row>-->

<!--		<div>-->
<!--			<fb-row :gutter="gutter">-->
<!--				<fb-col span="6" v-for="(item, idx) in warnList" :key="idx">-->
<!--					<fb-card noPadding>-->
<!--						<div class="warning-card" :class="[item.className]">-->
<!--							<div class="title">-->
<!--								<span class="dot"></span>-->
<!--								<span>{{item.title}}</span>-->
<!--							</div>-->
<!--							<div class="box">-->
<!--								<div class="box-card">-->
<!--									<div class="name">{{item.name1}}</div>-->
<!--									<div class="num" :class="[item.num1 > 0 ? 'breath' : '']">{{item.num1}}</div>-->
<!--								</div>-->
<!--								<div class="box-card breath">-->
<!--									<div class="name">{{item.name2}}</div>-->
<!--									<div class="num" :class="[item.num2 > 0 ? 'breath' : '']">{{item.num2}}</div>-->
<!--								</div>-->
<!--							</div>-->
<!--						</div>-->
<!--					</fb-card>-->
<!--				</fb-col>-->
<!--			</fb-row>-->
<!--		</div>-->

<!--		<div>-->
<!--			<fb-row :gutter="gutter">-->
<!--				<fb-col span="8">-->
<!--					<fb-card header="当期承若统计" noPadding :height="368">-->
<!--						<template slot="actions">-->
<!--							<fb-radio-group-->
<!--								v-model="checked"-->
<!--								button-->
<!--								:data="checkData"-->
<!--							/>-->
<!--						</template>-->
<!--						<ec-pie-doughnut @click="pieClick" autoresize :aria="pieAria"></ec-pie-doughnut>-->
<!--					</fb-card>-->
<!--				</fb-col>-->
<!--				<fb-col span="8">-->
<!--					<fb-card header="预警分析" noPadding :height="368">-->
<!--						<template slot="actions">-->
<!--							<fb-radio-group-->
<!--								v-model="checked"-->
<!--								button-->
<!--								:data="checkData"-->
<!--							/>-->
<!--						</template>-->
<!--						<ec-bar-stacked-horizontal-->
<!--							ref="bar1"-->
<!--							:options="changeOp"-->
<!--							@legendselectchanged="barLegendChang"-->
<!--							autoresize/>-->
<!--					</fb-card>-->
<!--				</fb-col>-->
<!--				<fb-col span="8">-->
<!--					<fb-card header="离线分析" noPadding :height="368">-->
<!--						<template slot="actions">-->
<!--							<fb-radio-group-->
<!--								v-model="checked"-->
<!--								button-->
<!--								:data="checkData"-->
<!--								@on-change="handleRadio"-->
<!--							/>-->
<!--						</template>-->
<!--						<fb-echarts @click="barClick" ref="bar" :options="op" autoresize :initOptions="render"></fb-echarts>-->
<!--					</fb-card>-->
<!--				</fb-col>-->
<!--			</fb-row>-->
<!--		</div>-->

<!--		<div>-->
<!--			<fb-row :gutter="gutter">-->
<!--				<fb-col span="12">-->
<!--					<fb-card header="未承诺企业列表" noPadding :height="268">-->
<!--						<fb-empty v-if="!tableData.length"></fb-empty>-->
<!--						<fb-simple-table v-if="tableData.length > 0" :columns="dicColumns" :data="tableData"></fb-simple-table>-->
<!--					</fb-card>-->
<!--				</fb-col>-->
<!--				<fb-col span="12">-->
<!--					<fb-card header="预警趋势分析" noPadding :height="268">-->
<!--						<template slot="actions">-->
<!--							<fb-radio-group-->
<!--								v-model="checked"-->
<!--								button-->
<!--								:data="checkData"-->
<!--							/>-->
<!--						</template>-->
<!--						<ec-line-gradient-stacked-area @mousemove="lineMove" autoresize></ec-line-gradient-stacked-area>-->
<!--					</fb-card>-->
<!--				</fb-col>-->
<!--			</fb-row>-->
<!--		</div>-->

<!--		<div>-->
<!--			<fb-card header="企业接入数量" noPadding height="666">-->
<!--				<template slot="actions">-->
<!--					<fb-radio-group-->
<!--						v-model="checked"-->
<!--						button-->
<!--						:data="checkData"-->
<!--					/>-->
<!--				</template>-->
<!--				<fb-simple-table :columns="dicColumns" :data="tableData2"></fb-simple-table>-->
<!--			</fb-card>-->
<!--		</div>-->
	</div>

</template>

<script>
	import { sysconfig } from '../../../../../project.config';
	export default {
		name: 'show-page',
		// 接收父组件的传参
		props: {},
		components: {},
		data () {
			return {
				radioValue: 0,
				radioData: [],
				gutter: sysconfig.gutter,
				warnList: [
					{title: '红色预警2222', className: 'red', name1: '今日预警', num1: 0, name2: '超期未处理', num2: 10},
					{title: '橙色预警', className: 'orange', name1: '今日预警', num1: 0, name2: '超期未处理', num2: 10},
					{title: '今日离线企业', className: 'gray', name1: '部分离线', num1: 0, name2: '全面掉线', num2: 10},
					{title: '数据推送', className: 'blue', name1: '新增信息数', num1: 0, name2: '删除信息数', num2: 10},
				],
				checked: '1', // 这个需要每个 fb-radio-group 独有
				checkData: [
					{
						value: '1',
						label: '今日',
					},
					{
						value: '2',
						label: '本月',
					},
					{
						value: '3',
						label: '本年',
					},
				],
				// 这个一定要 data 中
				render: {renderer: 'svg'},   // 直接传对象 会引起监听 刷新图表 :initOptions="{renderer: 'svg'}"
				pieAria: true, // 开启圆环 无障碍花纹
				op: {
					title: {
						text: 'ECharts 入门示例',
					},
					tooltip: {},
					xAxis: {
						data: ['衬衫', '羊毛衫', '雪纺衫', '裤子', '高跟鞋', '袜子'],
					},
					yAxis: {},
					series: [
						{
							name: '销量',
							type: 'bar',
							data: [10, 20, 36, 10, 10, 20],
						},
					],
				},
				changeOp: {
					grid: {
						left: '13%',
						right: '14%',
						bottom: '13%',
						containLabel: false,
					},
					legend: {
						data: ['蓝色预警', '绿色预警', '黄色预警', '红色预警'],
					},
					yAxis: {
						data: ['杭州', '舟山', '温州', '绍兴', '金华', '嘉兴', '宁波'],
					},
					series: [
						{
							name: '蓝色预警',
							data: [320, 302, 301, 334, 390, 330, 320],
						},
						{
							name: '绿色预警',
							data: [120, 132, 101, 134, 90, 230, 210],
						},
						{
							name: '黄色预警',
							data: [220, 182, 191, 234, 290, 330, 310],
						},
						{
							name: '红色预警',
							data: [150, 212, 201, 154, 190, 330, 410],
						},
					],
				},
				tableData: [],
				dicColumns: [
					{
						name: 'dicId',
						hidden: true,
						width: 1,
					}, {
						name: 'dicCode',
						label: '字典编码',
						sortable: false,
						width: 120,
						align: 'left',
					}, {
						name: 'dicName',
						label: '字典名称',
						sortable: false,
						width: 100,
						align: 'left',
					}, {
						name: 'dicDesc',
						label: '字典描述',
						sortable: false,
						align: 'left',
						slot: 'tags',
					},
					{
						name: 'enabled',
						label: '是否启用',
						sortable: false,
						align: 'left',
						width: 220,
						slot: 'tags',
					},
					{
						name: '',
						label: '操作',
						sortable: false,
						align: 'left',
						slot: 'actions',
					},
				],
				tableData2: [
					{
						dicId: '1',
						dicCode: '南京市',
						dicName: '1123',
						dicDesc: 'fasdfasdf',
						enabled: '123',
					},
					{
						dicId: '1',
						dicCode: '南京市',
						dicName: '1123',
						dicDesc: 'fasdfasdf',
						enabled: '123',
					},
					{
						dicId: '1',
						dicCode: '南京市',
						dicName: '1123',
						dicDesc: 'fasdfasdf',
						enabled: '123',
					},
					{
						dicId: '1',
						dicCode: '南京市',
						dicName: '1123',
						dicDesc: 'fasdfasdf',
						enabled: '123',
					},
				],
			}
		},
		// 初始化方法
		mounted () {

		},

		// 方法
		methods: {
			randomData() {
				let num = Math.random() * 10
				num = parseInt(num, 10)
				let arr = []
				for (let i = 0; i < num; i++) {
					arr.push({label: 'label' + i, value: i})
				}
				this.radioData = arr
				console.log(this.radioData, arr)
			},
			handleRadio (val) {
				let num = parseFloat(val + '0')
				// this.op.series[0].data = [num, 20, 36, 10, 10, 20]

				// Vue 深度监听数组单值 触发 watch
				this.$set(this.op.series[0].data, '0', num)

				this.changeOp.yAxis = {
					data: [num, '222', '333', '444', '555', '666', '777'],
				}

				this.pieAria = !this.pieAria

			},
			barLegendChang (p) {
				console.log(p,111111111111)
			},
			pieClick (p) {
				console.log(p)
			},
			barClick (p) {
				console.log(p)
			},
			lineMove (p) {
				console.log(p)
			}
		},
	}
</script>

<style lang="less" scoped>
	.tx-demo-page {


		.warning-card {
			padding: 12px;
			height:  114px;

			.title {
				margin-bottom: 14px;
				text-align:    center;

				.dot {
					display:       inline-block;
					width:         12px;
					height:        12px;
					border-radius: 50%;
					background:    #BFBFBF;
					margin-right:  6px;
				}
			}

			.box {
				display:         flex;
				justify-content: space-between;

				> div {
					height:        60px;
					border-radius: 4px;
					background:    #F9F9F9;
					text-align:    center;
					width:         48%;
					padding-top:   9px;

					.name {
						font-size:   13px;
						line-height: 17px;
					}

					.num {
						line-height: 27px;
						font-size:   20px;
						font-weight: bold;
					}
				}
			}

			&.red {
				.title {
					.dot {
						background:    #FB544E;
					}
				}
				.box {
					>div {
						background:    #FEF2F1;
						box-shadow: 0px 0px 0px 0px #FF2600;
						&.breath {
							animation: breath__red 1s infinite;
						}
					}
				}
			}
			&.orange {
				.title {
					.dot {
						background:    #FFB500;
					}
				}
				.box {
					>div {
						background:    #FEFBE5;
						box-shadow: 0px 0px 0px 0px #F79400;
						&.breath {
							animation: breath__orange 1s infinite;
						}
					}
				}
			}
			&.blue {
				.title {
					.dot {
						background:    #0284FE;
					}
				}
				.box {
					>div {
						background:    #E6F7FF;
						box-shadow: 0px 0px 0px 0px #2B96FE;
						&.breath {
							animation: breath__blue 1s infinite;
						}
					}
				}
			}
		}
	}

	@keyframes breath__red {
		50% {
			box-shadow: 0px 0px 8px 0px #FF2600;
			border:     1px solid rgba(255, 38, 0, 0.8);
		}
	}

	@keyframes breath__orange {
		50% {
			box-shadow: 0px 0px 8px 0px #F79400;
			border:     1px solid rgba(247, 148, 0, 0.8);
		}
	}

	@keyframes breath__blue {
		50% {
			box-shadow: 0px 0px 8px 0px #2B96FE;
			border:     1px solid rgba(43, 150, 254, 0.8);
		}
	}

</style>
