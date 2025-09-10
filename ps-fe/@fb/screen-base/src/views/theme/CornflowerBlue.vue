<template>
	<fb-container className="screen-page screen-page-index">
		<screen-header/>
		<fb-container absolute top="100px" left="490px" class="custom-box-1">
			<fb-button @on-click="$refs['dialog'].show()">dialog</fb-button>
		</fb-container>

		<fb-container class="leaflet-map-box">
			<fb-lmap :options="lmap.options" :basemaps="lmap.basemaps">
				<fb-lmap-switch-tiles bottom="300px" right="500px">
				</fb-lmap-switch-tiles>
			</fb-lmap>
		</fb-container>

		<fb-container absolute top="100px" left="1200px" width="200px" height="40px">
			<screen-search @on-search="doSearch" :data="searchList" showSenior
						   @on-result-click="doResultSearch" @on-senior-click="seniorClick" noAppendButton/>
		</fb-container>


		<screen-panel-left>
			<screen-panel title="隐患到期提醒" width="464px" height="480px" left="8px" top="90px" padding="6px">
<!--				<hik-player :width="464"-->
<!--							:height="360"></hik-player>-->
			</screen-panel>
			<!--  隐患到期提醒 -->
			<screen-panel title="隐患到期提醒" width="464px" height="498px" left="8px" top="578px">
				<easy-video-wall ref="video_wall" @on-close-cell="cellClose"></easy-video-wall>
			</screen-panel>
		</screen-panel-left>

		<screen-panel no-header width="944px" height="530px" left="488px" top="444px">
			<screen-table-chart :table-prop="table"></screen-table-chart>
		</screen-panel>

		<screen-panel-right>
			<!--  隐患类别TOP10 -->
			<screen-panel title="隐患类别TOP10" width="464px" height="362px" left="1448px" top="90px">

			</screen-panel>

			<!--  重大隐患预警 -->
			<screen-panel title="重大隐患预警" width="464px" height="320px" left="1448px" top="460px">
			</screen-panel>

			<!--  行业隐患分析 -->
			<screen-panel title="行业隐患分析" width="464px" height="284px" left="1448px" top="788px">
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
import EasyVideoWall from "@/components/easy-video-wall/src/EasyVideoWall";
import HikPlayer from "@/components/hik-player/src/HikPlayer";
import ScreenTableChart from "@/components/table-chart/src/ScreenTableChart";

export default {
	route: {
		path: '/cornflower-blue'
	},
	components: {
		ScreenTableChart,
		HikPlayer,
		EasyVideoWall,
		ScreenHeader,
	},
	data() {
		return {
			searchList: [],
			lmap: {
				options: {
					center: {lat: 35.705178427902396, lng: 115.46700239051532},
					zoom: 9
				},
				basemaps: [
					{type: 'TianDiTu.Satellite.Map', name: '天地图瓦片', key: ''},
					{type: 'TianDiTu.Satellite.Annotion', name: '天地图瓦片标注', key: ''},
				]
			},
			table: {
				columns: [
					{name: 'userId',label: 'id',hidden: true,width: 1,	disabled: true,},
					{name: 'userName',label: '姓名',sortable: false,width: 80,align: 'left',},
					{name: 'userGander',label: '性别',sortable: false,align: 'left',},
					{name: 'userTel',label: '电话',sortable: false,align: 'left',},
				],
				data: [
					{userId: '11',userName:'用户11',userGander: '1',userTel: '16',tags: [1, 2, 3],},
					{userId: '12',userName:'用户12',userGander: '2',userTel: '19',},
					{userId: '13',userName:'用户13',userGander: '3',userTel: '19',},
					{userId: '14',userName:'用户14',userGander: '4',userTel: '1',},
					{userId: '15',userName:'用户15',userGander: '5',userTel: '9',},
					{userId: '16',userName:'用户16',userGander: '6',userTel: '2',},
				]
			},
		}
	},
	mounted() {
		this.$screenUtil.setTheme('cornflower-blue')
	},
	methods: {
		handleClick() {

		},
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
		seniorClick() {
			console.log('seniorClick')
			this.$refs['video_wall'].play('https://mister-ben.github.io/videojs-flvjs/bbb.flv')
			// this.$refs['video_wall'].play('http://demo.easynvr.com:10800/flv/live/stream_9.flv?token=xPoIjQzm')
			// this.$refs['video_wall'].play('http://60.190.160.126:8950/openUrl/eKirqmY/live.flv')

		},
		cellClose(cell, rowIndex, colIndex) {
			console.log(cell, rowIndex, colIndex)
		}
	}
}
</script>
<style lang="less" scoped>
</style>
