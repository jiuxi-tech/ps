<template>
	<div class="tx-demo-page fb-page-search">

		<fb-cardx header="松鼠家有限公司" no-header-border no-border>
			<div slot="tags">
				<fb-tag type="primary" effect="plain">塘西镇</fb-tag>
				<fb-tag type="danger" effect="light">危化品生产</fb-tag>
			</div>
			<div slot="actions">
				<fb-radio-group @on-change="handleChange" button :data="btnOpts" value="1" size="m"/>
			</div>


			<fb-container radius="4px" :background="colors.blue_1" pt="16px">
				<fb-property colon label-width="140px" mode="form" class="card-property">
					<fb-row>
						<fb-col span="12">
							<fb-property-item label="统一社会信用代码" ellipsis>
								91330110731531980B
							</fb-property-item>
						</fb-col>
						<fb-col span="12">
							<fb-property-item label="系统注册时间" ellipsis>
								2018-09-21
							</fb-property-item>
						</fb-col>
					</fb-row>

					<fb-row>
						<fb-col span="12">
							<fb-property-item label="法定代表人" ellipsis>
								李岚清  15711112222
							</fb-property-item>
						</fb-col>
						<fb-col span="12">
							<fb-property-item label="主要负责人" ellipsis>
								何庆峰  15966668888
							</fb-property-item>
						</fb-col>
					</fb-row>

					<fb-row>
						<fb-col span="12">
							<fb-property-item label="详细地址" ellipsis>
								杭州余杭区塘西镇唐家埭村 <fb-icon name="location-fill" :color="colors.red_6"></fb-icon>
							</fb-property-item>
						</fb-col>
						<fb-col span="12">
							<fb-property-item label="经营范围" ellipsis>
								稀硝酸、亚硝酸钠、氨水、试剂产品
							</fb-property-item>
						</fb-col>
					</fb-row>



				</fb-property>
			</fb-container>

		</fb-cardx>

		<fb-card header="企业风险情况">
			<fb-row gutter="16" style="margin-bottom: 16px;">
				<fb-col span="6">
					<fb-cardx-breath title="重大风险" breath ellipsis>123</fb-cardx-breath>
				</fb-col>
				<fb-col span="6">
					<fb-cardx-breath type="danger" title="较大风险" breath ellipsis>123</fb-cardx-breath>
				</fb-col>
				<fb-col span="6">
					<fb-cardx-breath type="warning" title="一般风险" breath ellipsis>123</fb-cardx-breath>
				</fb-col>
				<fb-col span="6">
					<fb-cardx-breath :bgColor="colors.green_1" :breathColor="colors.red_10" title="低风险" breath ellipsis>123</fb-cardx-breath>
				</fb-col>
			</fb-row>

			<fb-simple-table
				:columns="table.columns"
				:data="table.data"
				:multiple="false">
			</fb-simple-table>
		</fb-card>

		<fb-card header="企业风险情况" class="alert-body">
			<fb-row gutter="16" class="alert-row">
				<fb-col span="4" v-for="(item,i) in 6" :key="i">
					<fb-container border="1px solid #E8E8E8" radius="4px" padding="8px">
						<fb-table-layout>
							<fb-table-layout-cell width="48px">
								<fb-avatar class="risk-avatar" size="40" icon="user" circle alt="圆形显示" color="rgba(2, 132, 254, 1)" backgroundColor="rgba(230, 247, 255, 1)"/>
							</fb-table-layout-cell>
							<fb-table-layout-cell>
								<fb-text>诚信等级A级</fb-text>
							</fb-table-layout-cell>
						</fb-table-layout>
					</fb-container>
				</fb-col>
				<fb-col span="12">
					<fb-container border="1px solid #E8E8E8" radius="4px" padding="8px">
						<fb-table-layout>
							<fb-table-layout-cell width="48px">
								<fb-avatar class="risk-avatar" size="40" icon="user" circle alt="圆形显示" :color="colors.orange_5" :backgroundColor="colors.orange_1"/>
							</fb-table-layout-cell>
							<fb-table-layout-cell>
								<fb-text>涉及重点监管工艺：</fb-text>
								<fb-text>新型焊化化工工艺</fb-text>
								<span class="middle-line">|</span>
								<fb-text>氯化工艺</fb-text>
								<span class="middle-line">|</span>
								<fb-text>加氢工艺</fb-text>
							</fb-table-layout-cell>
						</fb-table-layout>
					</fb-container>
				</fb-col>
			</fb-row>
		</fb-card>

		<div>
			<fb-row :gutter="gutter">
				<fb-col span="12">
					<fb-card header="企业风险情况" :bodyStyle="{height: '266px'}">
						<fb-simple-table
							style="height: 100%"
							class="risk-table"
							:columns="table1.columns"
							:data="table1.data"
							:rownum="false"
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
					</fb-card>
				</fb-col>
				<fb-col span="12">
					<fb-card header="隐患自查情况" height="313">
						<fb-row gutter="16" style="margin-bottom: 16px;">
							<fb-col span="6">
								<fb-cardx-breath title="隐患总数" breath ellipsis>123</fb-cardx-breath>
							</fb-col>
							<fb-col span="6">
								<fb-cardx-breath type="danger" title="已整改隐患数" breath ellipsis>123</fb-cardx-breath>
							</fb-col>
							<fb-col span="6">
								<fb-cardx-breath type="warning" title="超期未整改隐患数" breath ellipsis>123</fb-cardx-breath>
							</fb-col>
							<fb-col span="6">
								<fb-cardx-breath :bgColor="colors.green_1" :breathColor="colors.red_10" title="企业自查隐患总数" breath ellipsis>123</fb-cardx-breath>
							</fb-col>
						</fb-row>

						<fb-row gutter="16">
							<fb-col span="8" style="height: 170px;">
								<fb-echarts :options="reOptions" autoresize></fb-echarts>
							</fb-col>
							<fb-col span="16" class="danger-list">
								<div class="clearfix">
									<span>隐患自查总次数</span>
									<div>
										<span class="num">23</span>
										<span>次</span>
									</div>
								</div>
								<div class="clearfix">
									<span>隐患自查总次数</span>
									<div>
										<span class="num">23</span>
										<span>次</span>
									</div>
								</div>
								<div class="clearfix">
									<span>隐患自查总次数</span>
									<div>
										<span class="num">23</span>
										<span>次</span>
									</div>
								</div>
							</fb-col>
						</fb-row>
					</fb-card>
				</fb-col>
			</fb-row>
		</div>
	</div>

</template>

<script>
	import { sysconfig } from '../../../../../project.config';

	export default {
		name: 'index2',
		// 接收父组件的传参
		props: {},
		components: {},
		data () {
			return {
				gutter: sysconfig.gutter,
				btnOpts: [
					{value: '1', label: '企业画像'},
					{value: '2', label: '一企一档'}
				],

				// Table列
				table: {
					columns: [
						{
							name: 'entId',
							hidden: true,
							width: 1,
						}, {
							name: 'entFullName',
							label: '检查名称',
							sortable: false,
							align: 'left',
						}, {
							name: 'prodAddrName',
							label: '检查时间',
							sortable: false,
							align: 'left',
						}, {
							name: 'entUnifiedCode',
							label: '检查人员',
							sortable: false,
							align: 'left',
						}, {
							name: 'legalRepr',
							label: '检查风险点数量',
							sortable: false,
							align: 'right',
						},
						{
							name: 'legalReprTel',
							label: '登记隐患数',
							sortable: false,
							align: 'right'
						},
					],

					data: [
						{
							entFullName: '骚朱11',
							prodAddrName: '迎春ooo',
							entUnifiedCode: '16722299',
							legalRepr: '迎春ooo',
							legalReprTel: '毛尖xxx',
						}
					]
				},

				// Table1列
				table1: {
					columns: [
						{
							name: 'entFullName',
							label: '检查日期',
							sortable: false,
							align: 'left',
						}, {
							name: 'prodAddrName',
							label: '检查单位',
							sortable: false,
							align: 'left',
						}, {
							name: 'entUnifiedCode',
							slot: 'entUnifiedCode',
							label: '隐患数',
							sortable: false,
							align: 'left',

						}, {
							name: 'legalRepr',
							slot: 'legalRepr',
							label: '现措文书',
							sortable: false,
							align: 'left',
						},
						{
							name: 'legalReprTel',
							slot: 'legalReprTel',
							label: '责改文书',
							sortable: false,
							align: 'left'
						},
					],

					data: [
						{
							entFullName: '骚朱11',
							prodAddrName: '迎春ooo',
							entUnifiedCode: '2',
							legalRepr: '3',
							legalReprTel: '4',
						}
					]
				},

				// 环形echarts
				reOptions: {
					title: {
						text: '75%',
						textStyle: {
							color: '#111',
							fontSize: 24
						},
						subtext: '在线率',
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
							return '<span>75%</span>';
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
						data: [75],
						showBackground: true,
						backgroundStyle: {
							color: 'rgba(18, 26, 35, 0.2)',
						},
						coordinateSystem: 'polar',
						roundCap: true,
						barWidth: 15,
						itemStyle: {
							normal: {
								opacity: 1,
								color:  '#0284FE',
							}
						},
					}]
				},


			}
		},
		// 初始化方法
		mounted () {

		},

		// 方法
		methods: {
			handleChange (val) {
				console.log(val)
				if (val == '2') {
					this.$router.push('/sys/index/index2')
				}
			}
		},
	}
</script>

<style lang="less" scoped>
	@import "@/assets/styles/common.less";
	.tx-demo-page {

		.alert-body {
			/deep/ .@{FbUiPrefix}-card__body{
				padding-bottom: 0;
			}
		}

		.alert-row {
			.@{FbUiPrefix}-col {
				margin-bottom: 16px;
				cursor: pointer;

				&:nth-child(-n + 6) {
					margin-top: 0;
				}
			}
		}

		.card-property {
			.@{FbUiPrefix}-row {
				padding-bottom: 12px;
			}
		}

		.middle-line {
			margin: 0 12px;
		}

		.danger-list {
			>div {
				color: #313C47;
				font-size: 13px;
				padding: 16px;
				border-bottom: 1px solid #E8E8E8;

				>span {
					float: left;
					margin-top: 9px;
				}
				>div {
					float: right;
					.num {
						font-size: 20px;
						font-weight: bold;
						margin-right: 12px;
					}
				}
			}
		}


		.risk-avatar {
			font-size: 20px !important;
			/deep/ .@{FbUiPrefix}-icon {
				font-size: 20px !important;
			}
		}
	}



</style>
