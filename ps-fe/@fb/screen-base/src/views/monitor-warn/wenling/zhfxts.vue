<template>
	<!--  综合风险态势一张图  -->
	<fb-container className="screen-page screen-page-index">
		<screen-header/>

		<fb-container absolute left="488px" top="98px" width="208px" class="custom-box-1">
			<fb-select></fb-select>
		</fb-container>

		<fb-container absolute left="1192px" top="98px" width="240px" height="36px">
			<screen-search @on-search="doSearch" :data="searchList"
						   @on-result-click="doResultSearch"/>
		</fb-container>
		<fb-container>
			<div class="leaflet-map-box">
				<fb-lmap ref="lmap" @load-map="loadMap" :options="lmap.options" :basemaps="lmap.basemaps">
					<fb-lmap-marker-cluster :data="map_ent_markers" @on-click="entMarkerClick"/>

				</fb-lmap>
				<fb-container class="board-1" z-index="999" absolute left="1282px" top="638px" width="158px" height="116px" padding="16px">
					<fb-container v-for="(item, index) in mapLegends" :key="item.cls" :mt="index === 0 ? 0 : 8">
						<fb-space align="center">
							<fb-container :class="[item.cls]" width="24px" height="34px"></fb-container>
							<span>{{ item.label }}</span>
						</fb-space>
					</fb-container>
				</fb-container>
			</div>
		</fb-container>



		<screen-panel-left>
			<screen-panel title="监管企业数" left="8px" top="98px" width="464px" height="160px">
				<fb-container align="center" mt="8px">
					<fb-flop class="royal-blue-flop" :num="111"  minLength="6" theme="screen-blue-xl"></fb-flop>
				</fb-container>
				<fb-container absolute left="32px" bottom="24px" width="400px" height="42px" z-index="-1">
<!--					<img :src="require('../assets/img/common/floor-bottom.png')" alt="">-->
				</fb-container>
			</screen-panel>
			<screen-panel title="企业行业分布情况" left="8px" top="266px" width="464px" height="202px">
				<screen-rank-bar span="12" :data="panel2Data"></screen-rank-bar>
			</screen-panel>
			<screen-panel title="企业规模情况" left="8px" top="476px" width="464px" height="278px">
				<fbec-bar-base ref="ec_3_1"></fbec-bar-base>
			</screen-panel>
			<screen-panel title="企业类型分布情况" left="8px" top="762px" width="464px" height="310px">
				<screen-panel-title>
					<fb-tabs slot="title" class="royal-blue-tabs" :data="[
                        {label: '涉及危化品企业', value: 1},
                        {label: '三场所三企业', value: 2},
                    ]"></fb-tabs>
				</screen-panel-title>
				<fb-container height="220px">
					<fbec-bar-horizontal ref="ec_4_1"></fbec-bar-horizontal>
				</fb-container>
			</screen-panel>
		</screen-panel-left>


		<screen-panel show-corner left="480px" top="762px" width="960px" height="310px">
			<screen-panel-title mt="12px">
				<fb-tabs slot="title" class="royal-blue-tabs" :data="[
                        {label: '各乡镇检查情况', value: 1},
                        {label: '各乡镇隐患情况', value: 2},
                    ]"></fb-tabs>
			</screen-panel-title>
			<fb-container absolute top="16px" right="16px">
				<fb-radio-group button :data="[
                    {label: '上月', value: 1},
                    {label: '本月', value: 2},
                ]"></fb-radio-group>
			</fb-container>
			<fb-container height="250px">
				<fbec-bar-base-twoy ref="ec_b_1"></fbec-bar-base-twoy>
			</fb-container>
		</screen-panel>

		<screen-panel-right>
			<screen-panel title="企业风险分布情况" left="1448px" top="98px" width="464px" height="177px">
				<fb-row>
					<fb-col span="6">
						<fb-container height="100px">
							<fbec-pie-bar :labelStyle="{bottom: '-20px'}" label-grid="show_value" style="height: 100px"
										  ref="ec_6_1"></fbec-pie-bar>
						</fb-container>
					</fb-col>
					<fb-col span="6">
						<fb-container height="100px">
							<fbec-pie-bar :labelStyle="{bottom: '-20px'}" label-grid="show_value" style="height: 100px"
										  ref="ec_6_2"></fbec-pie-bar>
						</fb-container>
					</fb-col>
					<fb-col span="6">
						<fb-container height="100px">
							<fbec-pie-bar :labelStyle="{bottom: '-20px'}" label-grid="show_value" style="height: 100px"
										  ref="ec_6_3"></fbec-pie-bar>
						</fb-container>
					</fb-col>
					<fb-col span="6">
						<fb-container height="100px">
							<fbec-pie-bar :labelStyle="{bottom: '-20px'}" label-grid="show_value" style="height: 100px"
										  ref="ec_6_4"></fbec-pie-bar>
						</fb-container>
					</fb-col>
				</fb-row>
			</screen-panel>
			<screen-panel no-header left="1448px" top="283px" width="464px" height="212px">
				<fb-simple-table></fb-simple-table>
			</screen-panel>
			<screen-panel title="检查情况" left="1448px" top="503px" width="464px" height="178px">
				<screen-panel-title>
					<fb-tabs slot="title" class="royal-blue-tabs royal-blue-tabs-grow" :data="[
                        {label: '今天', value: 1},
                        {label: '本月', value: 2},
                        {label: '本年', value: 3},
                    ]"></fb-tabs>
				</screen-panel-title>
				<fb-container mt="22px">
					<fb-row gutter="8">
						<fb-col span="12">
							<screen-flop-board theme="l-v" height="68px" label="标题居左" :value="1"
											   unit="家"></screen-flop-board>
						</fb-col>
						<fb-col span="12">
							<screen-flop-board theme="l-v" height="68px" label="标题居左" :value="1"
											   unit="家"></screen-flop-board>
						</fb-col>
					</fb-row>
				</fb-container>
			</screen-panel>
			<screen-panel title="隐患排查情况" left="1448px" top="689px" width="464px" height="383px">
				<screen-panel-title>
					<fb-tabs slot="title" class="royal-blue-tabs royal-blue-tabs-grow" :data="[
                        {label: '今天', value: 1},
                        {label: '本月', value: 2},
                        {label: '本年', value: 3},
                    ]"></fb-tabs>
				</screen-panel-title>
				<fb-container mt="4px">
					<fb-row gutter="8">
						<fb-col span="12">
							<screen-flop-board mt="6px" theme="vertical_t-f" title="隐患总数" height="90px">
								<fb-flop slot="flop" :num="1" minLength="3" theme="screen-red-l"></fb-flop>
							</screen-flop-board>
						</fb-col>
						<fb-col span="12">
							<screen-flop-board mt="6px" theme="vertical_t-f" title="整改中隐患" height="90px">
								<fb-flop slot="flop" :num="1" minLength="3" theme="screen-orange-l"></fb-flop>
							</screen-flop-board>
						</fb-col>
					</fb-row>
				</fb-container>
				<fb-container mt="8px">
					<fb-row gutter="8">
						<fb-col span="12">
							<fb-container height="190px">
								<fbec-pie-bar ref="ec_8_1"></fbec-pie-bar>
							</fb-container>
						</fb-col>
						<fb-col span="12">
							<screen-flop-board theme="l-v" height="45px" :value="1" label="挂牌隐患"
											   :value-style="{color: '#FD201C'}"></screen-flop-board>
							<screen-flop-board theme="l-v" height="45px" :value="1" label="重大隐患" mt="3px"
											   :value-style="{color: '#FD201C'}"></screen-flop-board>
							<screen-flop-board theme="l-v" height="45px" :value="1" label="较大隐患" mt="3px"
											   :value-style="{color: '#F9FD26'}"></screen-flop-board>
							<screen-flop-board theme="l-v" height="45px" :value="1" label="一般隐患"
											   mt="3px"></screen-flop-board>
						</fb-col>
					</fb-row>
				</fb-container>
			</screen-panel>
		</screen-panel-right>

	</fb-container>
</template>

<script>
/**
 * zhfxts
 * (c) 2021 lincong1987
 */
import {FbecPieBar, FbecBarBase, FbecBarHorizontal, FbecBarBaseTwoy} from '@fb/fb-third/src/index'
import {linearColors} from "@fb/fb-third/packages/components/echarts/utils";
import ScreenHeader from "@/file/ScreenHeader";

export default {
	components: {
		ScreenHeader,
		FbecPieBar,
		FbecBarBase,
		FbecBarHorizontal,
		FbecBarBaseTwoy
	},
	data() {
		return {
			mapLegends: [
				{label: '隐患整改', cls: 'lmap-marker-risk-red'},
				{label: '企业', cls: 'lmap-marker-risk-blue'},
			],
			panel2Data: [
				{label: '标题居左1', value: 12, percent: 80},
				{label: '标题居左2', value: 12, percent: 80},
				{label: '标题居左3', value: 12, percent: 80},
				{label: '标题居左4', value: 12, percent: 80},
				{label: '标题居左5', value: 12, percent: 80},
			],

			searchList: [],

			lmap: {
				options: {
					center: {
						"lat": app.projectConfig.centerPoint[1],
						"lng": app.projectConfig.centerPoint[0]
					},
					zoom: 7
				},
				basemaps: [
					{type: 'TianDiTu.Satellite.Map', name: '天地图瓦片'},
					{type: 'TianDiTu.Satellite.Annotion', name: '天地图瓦片标注'},
				]
			},
			// 注意 现在 绘制点位封装成组件，清空只需要制空数组
			cityMarkers: [],
			map_ent_markers: [],
			map_current_ent_marker: null,
			ent_marker: {}, // 单点
			mapPopShow: false,
			mapPopObj: {
				title: '123'
			},
			geoJson: {}
		}
	},
	mounted() {
		this.$nextTick(() => {
			this.updateEc_3_1()
			this.updateEc_4_1()
			this.updateEc_6()
			this.updateEc_8_1()
			this.updateEc_b_1()
		})
	},
	methods: {
		doSearch(res) {
			console.log(res)
			this.searchList = [
				{label: res + '1', value: 1},
				{label: res + '2', value: 2},
				{label: res + '3', value: 3},
			]
		},
		doResultSearch(res) {
			console.log(res)
		},
		updateEc_3_1() {
			let val = {
				legend: {
					show: true
				},
				grid: {
					bottom: 8
				},
				xAxis: {
					data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
				},
				series: [
					{
						name: '企业数量',
						data: [820, 932, 901, 934, 1290, 1330, 1320],
						label: {
							show: true
						}
					}
				]
			}
			this.$refs['ec_3_1'].updateOptions(val)
		},
		updateEc_4_1() {
			let val = {
				grid: {
					top: 8,
					bottom: -20
				},
				legend: {
					show: true
				},
				yAxis: {
					data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
				},
				series: [
					{
						data: [82, 93, 90, 93, 90, 30, 32],
						itemStyle: {
							color: 'rgba(7, 232, 174, 1)'
						}
					},
				]
			}
			this.$refs['ec_4_1'].updateOptions(val)
		},
		updateEc_6() {
			let val1 = {
				labelText: '重大风险',
				series: [{
					barWidth: 8,
					data: [{value: 10, showValue: 9099}]
				}]
			}
			let val2 = {
				labelText: '较大风险',
				series: [{
					barWidth: 8,
					data: [{value: 10, showValue: 9099}]
				}]
			}
			let val3 = {
				labelText: '一般风险',
				series: [{
					barWidth: 8,
					data: [{value: 10, showValue: 9099}]
				}]
			}
			let val4 = {
				labelText: '低风险',
				series: [{
					barWidth: 8,
					data: [{value: 10, showValue: 9099}]
				}]
			}

			this.$refs['ec_6_1'].updateOptions(val1, {
				type: 'red'
			})
			this.$refs['ec_6_2'].updateOptions(val2, {
				type: 'orange'
			})
			this.$refs['ec_6_3'].updateOptions(val3, {
				type: 'yellow'
			})
			this.$refs['ec_6_4'].updateOptions(val4, {
				type: 'blue'
			})
		},
		updateEc_8_1() {
			let val = {
				labelText: '隐患整改率',
				series: [
					{data: [90]}
				]
			}
			this.$refs['ec_8_1'].updateOptions(val)
		},
		updateEc_b_1() {
			let val = {
				legend: {
					show: true
				},
				xAxis: {
					data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
				},
				series: [
					{
						name: '应检查家次',
						data: [820, 932, 901, 934, 1290, 1330, 1320],
					},
					{
						name: '已检查家次',
						data: [820, 932, 901, 934, 1290, 1330, 1320],
					},
					{
						name: '隐患整改率',
						type: 'line',
						yAxisIndex: 1,

						data: [82, 93, 90, 34, 12, 13, 20],
					},
				]
			}
			this.$refs['ec_b_1'].updateOptions(val)
		},

		// map-start
		loadMap(map) {
			this.drawEntMarkers()
		},
		// 初始化 100 个点
		initMarkers100(lng, lat) {
			let that = this
			let points = []
			let markers = [
				'lmap-marker-risk-red',
				'lmap-marker-risk-blue',
			]
			for (let i = 0; i < 100; ++i) {
				let radius_in_degress = 1000000 / 1113000;
				let u = Math.random();
				let v = Math.random();
				let w = radius_in_degress * Math.sqrt(u);
				let t = 2 * Math.PI * v;
				let x = w * Math.cos(t);
				let y = w * Math.sin(t);
				let longitude = lng || 120.56726060807705 + y;
				let latitude = lat || 29.281860385090113 + x;

				points.push({
					lngLat: [longitude, latitude],
					icon: L.divIcon({
						iconSize: [24, 34],
						html: `<div class="${markers[i % 4]}"></div>`,
					}),
					title: '风险企业' + i
				})
			}
			return points
		},
		// 绘制企业点位
		drawEntMarkers(cityCode) {
			// 请做异步查询数据生成点位信息
			this.map_ent_markers = this.initMarkers100()
		},
		entMarkerClick(e, marker, point, settings) {
			console.log('entMarkerClick', e, marker, point, settings)
			this.map_current_ent_marker = marker

		},
		closeCurrentEntMarkerPop() {
			if (this.map_current_ent_marker) {
				this.map_current_ent_marker.closePopup()
			}
		},
		// map-end
	}
}
</script>
<style lang="less" scoped>

</style>
