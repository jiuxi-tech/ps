<template>
	<fb-container className="screen-page screen-page-index">


		<screen-panel-top>
			<screen-header/>
			<fb-container absolute top="100px" left="490px" class="custom-box-1">
				<fb-button @on-click="$refs['dialog'].show()">dialog</fb-button>
				<fb-button @on-click="changePanelShow()">测试动画</fb-button>
			</fb-container>

			<fb-container absolute top="100px" left="1200px" width="200px" height="40px">
				<screen-search @on-search="doSearch" :data="searchList" showSenior
							   @on-result-click="doResultSearch" @on-senior-click="seniorClick" noAppendButton/>
			</fb-container>
		</screen-panel-top>


		<screen-panel-left ref="panelLeft">
			<screen-panel title="面板一" width="464px" height="480px" left="8px" top="90px" padding="4px">
				<fb-container slot="actions" absolute right="10px" top="8px">
					<fb-link type="primary" label="更多>>"></fb-link>
				</fb-container>
				<div style="overflow: hidden;" >
					<screen-flop-board pt="12px" theme="vertical_t-f" title="反馈率1"
									   :title-style="{fontSize: '24px', fontWeight: 700}"
									   height="125px">
						<fb-flex slot="flop" jc-center>
							<screen-flop :num="99.99"
										 type="simple"
										 :number-props="{decimals: 2 }"
										 theme="screen-cyan-l">
								             <fb-text size="18" slot="endSuffix">%</fb-text>
							</screen-flop>
						</fb-flex>
					</screen-flop-board>

				</div>
				-------
				<div>
					<screen-flop-board pt="12px" theme="vertical_t-f" title="反馈率2"
									   :title-style="{fontSize: '24px', fontWeight: 700}"
									   height="125px">
						<fb-flex slot="flop" jc-center>
							<screen-flop brand-suffix="%"
										 type="simple"
										 :num="99.99"
										 theme="screen-cyan-l"></screen-flop>
						</fb-flex>
					</screen-flop-board>

				</div>
				-------
				<div>
					<fb-col span="12" align="center">
						<fb-text size="18">完成率</fb-text>
						<fb-container mt="6px">
							<fb-flop :num="99.99"
									 :number-props="{decimals: 2, append: '%' }"
									 key="schedulePercent"
									 min-length="0"></fb-flop>
						</fb-container>
					</fb-col>
				</div>
				-------
				<div>
					<fb-col span="12" align="center">
						<fb-text size="18">完成率</fb-text>
						<fb-container mt="6px">
							<fb-flop :num="99.99"
									 :number-props="{decimals: 2,  }"
									 key="schedulePercent"
									 min-length="0" brand-suffix="%"></fb-flop>
						</fb-container>
					</fb-col>
				</div>

			</screen-panel>
			<!--  隐患到期提醒 -->
			<screen-panel title="隐患到期提醒" width="464px" height="498px" left="8px" top="578px">

			</screen-panel>
		</screen-panel-left>


		<screen-panel-bottom ref="panelBottom">
			<screen-panel show-corner width="944px" height="230px" left="488px" top="844px">
				<screen-panel-title title="12"></screen-panel-title>
				<screen-panel-title title="12" theme="l2"></screen-panel-title>
			</screen-panel>
		</screen-panel-bottom>


		<screen-panel-right ref="panelRight">
			<!--  隐患类别TOP10 -->
			<screen-panel no-top title="隐患类别TOP10" width="464px" height="362px" left="1448px" top="90px"
						  :title-style="{width: '100%'}">
				<screen-panel-tab slot="title" :data="[
            					{label: 'tab1', value: 1},
            					{label: 'tab2', value: 2},
            				]" v-model="tabValue"></screen-panel-tab>
			</screen-panel>

			<!--  重大隐患预警 -->
			<screen-panel title="重大隐患预警" width="464px" height="320px" left="1448px" top="460px">
				<screen-flop :num="99" unit="%">
				</screen-flop>
			</screen-panel>

			<!--  行业隐患分析 -->
			<screen-panel title="行业隐患分析" width="464px" height="284px" left="1448px" top="788px">
			</screen-panel>
		</screen-panel-right>


		<fb-dialog title="弹窗" ref="dialog">
			<screen-table-chart :table-prop="table"></screen-table-chart>
		</fb-dialog>
	</fb-container>
</template>

<script>
/**
 * index
 * (c) 2021 lincong1987
 */
import ScreenHeader from '../components/ScreenHeader'
import ScreenPanel from '../components/ScreenPanel'
import ScreenFlop from '../components/ScreenFlop'
import ScreenFlopBoard from '@/components/ScreenFlopBoard'
import ScreenPanelTitle from '@/components/ScreenPanelTitle'
import ScreenPanelLeft from '@/components/ScreenPanelLeft'
import ScreenPanelRight from '@/components/ScreenPanelRight'
import ScreenPanelBottom from '@/components/ScreenPanelBottom'

export default {
	components: {
		ScreenPanelBottom,
		ScreenPanelLeft,
		ScreenPanelRight,
		ScreenPanelTitle,
		ScreenFlopBoard,
		ScreenHeader,
		ScreenPanel,
		ScreenFlop,
	},
	data () {
		return {
			searchList: [],
			tabValue: 1,
			table: {
				columns: [
					{
						name: 'userId',
						label: 'id',
						hidden: true,
						width: 1,
						disabled: true,
					},
					{
						name: 'userName',
						label: '姓名',
						sortable: false,
						width: 80,
						align: 'left',
					},
					{
						name: 'userGander',
						label: '性别',
						sortable: false,
						align: 'left',
					},
					{
						name: 'userTel',
						label: '电话',
						sortable: false,
						align: 'left',
					},
				],
				data: [
					{
						userId: '11',
						userName: '用户11',
						userGander: '1',
						userTel: '16',
						tags: [1, 2, 3],
					},
					{
						userId: '12',
						userName: '用户12',
						userGander: '2',
						userTel: '19',
					},
					{
						userId: '13',
						userName: '用户13',
						userGander: '3',
						userTel: '19',
					},
					{
						userId: '14',
						userName: '用户14',
						userGander: '4',
						userTel: '1',
					},
					{
						userId: '15',
						userName: '用户15',
						userGander: '5',
						userTel: '9',
					},
					{
						userId: '16',
						userName: '用户16',
						userGander: '6',
						userTel: '2',
					},
				],
			},
			panelShow: true,
		}
	},
	mounted () {

	},
	methods: {
		handleClick () {

		},
		doSearch (res) {
			console.log(res)
			this.searchList = [
				{
					label: res + '1',
					value: 1,
				},
				{
					label: res + '2',
					value: 2,
				},
				{
					label: res + '3',
					value: 3,
				},
			]
		},
		doResultSearch (res) {
			console.log(res)
		},
		seniorClick () {
			console.log('seniorClick')
		},
		changePanelShow () {
			this.panelShow = !this.panelShow
			this.$refs['panelLeft'].changeDivShow(this.panelShow)
			this.$refs['panelRight'].changeDivShow(this.panelShow)
			this.$refs['panelBottom'].changeDivShow(this.panelShow)
		},
	},
}
</script>
<style lang="less" scoped>
</style>
