<template>
	<fb-container className="screen-page screen-page-index">
		<screen-header/>
		<fb-container absolute top="100px" left="490px" class="custom-box-1">
			<fb-button @on-click="$refs['dialog'].show()">dialog</fb-button>
			<fb-select :data="[{label: 123, value: 123}]"></fb-select>
		</fb-container>

		<screen-panel-left>
			<screen-panel no-header width="464px" height="480px" left="8px" top="90px" padding="4px">
				<screen-flop-board theme="l-v" label="标题居左123" @on-value-click="handleClick" :value="1" unit="家"></screen-flop-board>
				<screen-flop-board mt="6px"  @on-click="handleClick" theme="l-v" label="标题居左" :value="0" unit="家"></screen-flop-board>
				<screen-flop-board mt="6px" theme="i-l-v" icon="risk-point-search-fill" icon-bg="linear-gradient(360deg, #5CCFFF 0%, #2FA1FE 100%)" label="标题居左" :value="222"></screen-flop-board>
				<screen-flop-board mt="6px" theme="l-f" label="标题居左">
					<fb-flop slot="flop" :num="1"  minLength="3" theme="screen-blue-s"></fb-flop>
				</screen-flop-board>

				<fb-container mt="6px" class="board-1">
					<fb-container pl="8px" pt="8px" size="16" style="font-weight: bold">标题加粗 组合样式</fb-container>
					<screen-flop-board theme="l-v" label="标题居左" :value="222" unit="家" height="38px" no-bg></screen-flop-board>
					<screen-flop-board theme="l-v" label="标题居左" :value="222" unit="家" height="38px" no-bg></screen-flop-board>
				</fb-container>
				<fb-container mt="6px" class="board-1" height="76px">
					<screen-flop-board theme="l-v" label="标题居左" :value="222" unit="家" height="38px" no-bg></screen-flop-board>
					<screen-flop-board theme="l-v" label="标题居左" :value="222" unit="家" height="38px" no-bg></screen-flop-board>
				</fb-container>
				<fb-container mt="6px" class="board-1" height="76px">
					<fb-row>
						<fb-col span="8">
							<fb-container pl="8px" display="flex" height="76px" style="align-items: center">标题居左</fb-container>
						</fb-col>
						<fb-col span="8">
							<screen-flop-board theme="vertical_v-l" label="标题居左" :value="222" unit="家" height="76px" no-bg align="left"></screen-flop-board>
						</fb-col>
						<fb-col span="8">
							<screen-flop-board theme="vertical_v-l" label="标题居左" :value="222" :value-style="{color: '#F9FD26'}" unit="家" height="76px" no-bg align="left"></screen-flop-board>
						</fb-col>
					</fb-row>
				</fb-container>
			</screen-panel>
			<!--  隐患到期提醒 -->
			<screen-panel title="隐患到期提醒" width="464px" height="498px" left="8px" top="578px">
				<screen-flop-board theme="vertical_l-v" label="标题居中" :value="222" height="68px"></screen-flop-board>
				<fb-container mt="6px" class="board-1" height="68px" pl="8px">
					<fb-row>
						<fb-col span="12">
							<screen-flop-board theme="vertical_l-v" label="标题居左" :value="222" unit="家" no-bg align="left"></screen-flop-board>
						</fb-col>
						<fb-col span="12">
							<screen-flop-board theme="vertical_l-v" label="&nbsp;" :value="222" :value-style="{color: '#F9FD26'}" unit="家" no-bg align="left"></screen-flop-board>
						</fb-col>
					</fb-row>
				</fb-container>
				<screen-flop-board mt="6px" theme="vertical_t-f" title="标题文字" height="90px">
					<fb-flop slot="flop" :num="1"  minLength="3" theme="screen-blue-l"></fb-flop>
				</screen-flop-board>
				<screen-flop-board mt="6px" theme="vertical_ileft-l-v" icon="send" :icon-size="40" label="标题居左" :value="222" height="76px" align="left"></screen-flop-board>
				<fb-container mt="6px" class="board-1" height="123px">
					<screen-panel-title title="标题加粗" mt="6px" mb="4px"></screen-panel-title>
					<fb-row>
						<fb-col span="8">
							<screen-flop-board theme="vertical_l-f" label="已整改" no-bg>
								<fb-flop slot="flop" :num="1"  minLength="3" theme="screen-red-l"></fb-flop>
							</screen-flop-board>
						</fb-col>
						<fb-col span="8">
							<screen-flop-board theme="vertical_l-f" label="未整改" no-bg>
								<fb-flop slot="flop" :num="1"  minLength="3" theme="screen-orange-l"></fb-flop>
							</screen-flop-board>
						</fb-col>
						<fb-col span="8">
							<screen-flop-board theme="vertical_l-f" label="整改率(%)" no-bg>
								<screen-flop slot="flop" :num="9.90" theme="screen-blue-l" min-length="2" :end-flop="{brandSuffix: '%'}"></screen-flop>
<!--								<fb-space slot="flop">-->
<!--									<fb-flop :num="99"  minLength="1" theme="screen-blue-l"></fb-flop>-->
<!--									<fb-text size="30" style="font-weight: bold">.</fb-text>-->
<!--									<fb-flop :num="8"  minLength="1" theme="screen-blue-l"></fb-flop>-->
<!--								</fb-space>-->
							</screen-flop-board>
						</fb-col>
					</fb-row>
				</fb-container>
			</screen-panel>
		</screen-panel-left>


		<screen-panel show-corner width="944px" height="230px" left="488px" top="844px">
			<fb-row gutter="8">
				<fb-col span="4">
					<screen-flop-board theme="vertical_i-l-v" icon="send" :icon-size="40" label="整改率<br/>(%)" :value="99" height="194px"></screen-flop-board>
				</fb-col>
				<fb-col span="4">
					<screen-flop-board theme="vertical_i-l-v" :class="{active: true}" icon="send" :icon-size="40" label="整改率<br/>(%)" :value="99" height="194px"></screen-flop-board>
				</fb-col>
			</fb-row>

		</screen-panel>

		<screen-panel-right>
			<!--  隐患类别TOP10 -->
			<screen-panel title="隐患类别TOP10" width="464px" height="362px" left="1448px" top="90px">
				<screen-panel-title title="123"></screen-panel-title>
				<screen-panel-title>
					<fb-tabs slot="title" :data="[
						{label: '二级标题', value: 1},
						{label: '二级标题', value: 2},
						{label: '二级标题', value: 3}
					]"></fb-tabs>
				</screen-panel-title>
				<fb-container @on-click="handleClick">
					<screen-panel-title title="我是container 小手" theme="c1"></screen-panel-title>
				</fb-container>

				<screen-panel-title title="三级标题" theme="c2"></screen-panel-title>
				<screen-panel-title title="三级标题" theme="l2"></screen-panel-title>
				<screen-panel-title title="三级标题" theme="l3"></screen-panel-title>
			</screen-panel>

			<!--  重大隐患预警 -->
			<screen-panel title="重大隐患预警" no-header-angle width="464px" height="320px" left="1448px" top="460px">
				<screen-rank-bar current="标题居左1" span="8" :data="[
					{label: '标题居左1', value: 12, percent: 80},
					{label: '标题居左2', value: 12, percent: 80},
					{label: '标题居左3', value: 12, percent: 80},
					{label: '标题居左4', value: 12, percent: 80},
					{label: '标题居左5', value: 12, percent: 80},
				]"></screen-rank-bar>
			</screen-panel>

			<!--  行业隐患分析 -->
			<screen-panel no-top width="464px" height="284px" left="1448px" top="788px">
				<screen-panel-tab slot="title" :data="[
					{label: 'tab1', value: 1},
					{label: 'tab2', value: 2},
				]" v-model="tabValue"></screen-panel-tab>
			</screen-panel>
		</screen-panel-right>


		<fb-dialog title="弹窗" ref="dialog"></fb-dialog>
	</fb-container>
</template>

<script>
/**
 * index
 * (c) 2021 lincong1987
 */
import ScreenHeader from '../../components/ScreenHeader'
import ScreenPanel from "../../components/ScreenPanel";
import ScreenFlopBoard from "@/components/ScreenFlopBoard";
import ScreenPanelTitle from "@/components/ScreenPanelTitle";
import ScreenPanelLeft from "@/components/ScreenPanelLeft";
import ScreenPanelRight from "@/components/ScreenPanelRight";
import ScreenRankBar from "@/components/ScreenRankBar";
import ScreenFlop from "@/components/ScreenFlop";
import ScreenPanelTab from "@/components/ScreenPanelTab";

export default {
	route: {
		path: '/index'
	},
	components: {
		ScreenPanelTab,
		ScreenFlop,
		ScreenRankBar,
		ScreenPanelLeft,
		ScreenPanelRight,
		ScreenPanelTitle,
		ScreenFlopBoard,
		ScreenHeader,
		ScreenPanel,
	},
	data() {
		return {
			tabValue: 1
		}
	},
	mounted() {
		this.$screenUtil.setTheme('midnight-blue')
	},
	methods: {
		handleClick() {
			this.$message.success(123123)
		}
	}
}
</script>
<style lang="less" scoped>
</style>
