<template>
	<div class="tx-demo-page fb-page-search">
		<fb-card>
			<fb-form label-width="120" ref="query-form" mode="query">
				<fb-row gutter="16">
					<fb-col span="8">
						<fb-form-item label="企业名称">
							<fb-input
								v-model="formData.enterprise"
								placeholder="请输入企业名称"
								clearable>
							</fb-input>
						</fb-form-item>
					</fb-col>
					<fb-col span="8">
						<fb-form-item label="行政区划">
							<fb-select v-model="formData.admindiv"
									   :data="selectNodes"
									   placeholder="请选择行政区划"
									   :reader="{value:'id', label: 'name'}"
									   clearable/>
						</fb-form-item>
					</fb-col>
					<fb-col span="8">
						<fb-button type="primary" icon="search" style="float: right;">查 询</fb-button>
					</fb-col>
				</fb-row>
			</fb-form>

			<div>
				<fb-button v-for="(item, idx) in ceshiData" :key="idx" @on-click="toNext(item.name, item.id, 1)">{{item.name}}</fb-button>
			</div>
		</fb-card>

		<fb-card :height="644" no-padding>
			<fb-container slot="header" pl="16px" class="map-head">
				<fb-text>共有企业</fb-text>
				<fb-text :color="colors.blue_5" class="num">2000</fb-text>
				<fb-text>家；已标注点位企业</fb-text>
				<fb-text :color="colors.green_8" class="num">1000</fb-text>
				<fb-text>家；已开展风险辨识企业</fb-text>
				<fb-text :color="colors.orange_5" class="num">900</fb-text>
				<fb-text>家；共辨识风险点总数</fb-text>
				<fb-text :color="colors.red_5" class="num">15000</fb-text>
				<fb-text>处。</fb-text>
			</fb-container>

			<div style="position: relative; height: 100%;">
				<fb-container position="absolute" top="10px" left="10px" z-index="499" width="193px" height="152px">
					<fb-card>
						<fb-table-layout>
							<fb-table-layout-cell width="20px"><fb-badge dot :dotColor="colors.red_5" dotSize="12" vertical="-1px"></fb-badge></fb-table-layout-cell>
							<fb-table-layout-cell width="90px">重大风险</fb-table-layout-cell>
							<fb-table-layout-cell class="text-right">1000</fb-table-layout-cell>
						</fb-table-layout>
						<fb-table-layout style="margin-top: 12px;">
							<fb-table-layout-cell width="20px"><fb-badge dot :dotColor="colors.orange_5" dotSize="12" vertical="-1px"></fb-badge></fb-table-layout-cell>
							<fb-table-layout-cell width="90px">较大风险</fb-table-layout-cell>
							<fb-table-layout-cell class="text-right">1000</fb-table-layout-cell>
						</fb-table-layout>
						<fb-table-layout style="margin-top: 12px;">
							<fb-table-layout-cell width="20px"><fb-badge dot :dotColor="colors.yellow_5" dotSize="12" vertical="-1px"></fb-badge></fb-table-layout-cell>
							<fb-table-layout-cell width="90px">一般风险</fb-table-layout-cell>
							<fb-table-layout-cell class="text-right">1000</fb-table-layout-cell>
						</fb-table-layout>
						<fb-table-layout style="margin-top: 12px;">
							<fb-table-layout-cell width="20px"><fb-badge dot :dotColor="colors.blue_5" dotSize="12" vertical="-1px"></fb-badge></fb-table-layout-cell>
							<fb-table-layout-cell width="90px">低风险</fb-table-layout-cell>
							<fb-table-layout-cell class="text-right">100</fb-table-layout-cell>
						</fb-table-layout>
					</fb-card>
				</fb-container>

				<fb-container position="absolute" top="10px" right="10px" z-index="499" width="24px" height="49px">
					<fb-card no-padding no-border>
						<div class="icon-wrapper" @click="mapZoomIn"><fb-icon name="add-normal" size="16"></fb-icon></div>
						<div class="icon-wrapper" @click="mapZoomOut"><fb-icon name="reduce-normal" size="16"></fb-icon></div>
					</fb-card>
				</fb-container>


				<fb-tmap
					:centerAndZoom="[116.40769]"
					@init-map="handleTMap"
					@click="TMapClick"
					ref="tmap"
				></fb-tmap>

				<tdt-map :center="[113.280637, 23.125178]" :zoom="11"></tdt-map>
			</div>
		</fb-card>

		<mapPop ref="map-pop" :title="popObj.title" v-show="mapPopObj.show"></mapPop>
		<fb-select></fb-select>
	</div>

</template>

<script>
	import mapPop from "./map-pop"


	export default {
		name: 'index-m',
		components: {
			mapPop
		},
		// 接收父组件的传参
		props: {},
		data () {
			return {
				map: '',
				formData: {
					enterprise: '',
					admindiv: ''
				},
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
				ceshiData: [
					{name: '张三', path: '/sys/index/aaa1', id: '1111'},
					{name: '张三', path: '/sys/index/aaa2', id: '2222'},
					{name: '张三', path: '/sys/index/aaa3', id: '3333'},
				],
				popObj: {
					title: ""
				}
			}
		},
		watch: {
			'formData.enterprise'(newVal) {
				this.popObj.title = newVal
			}
		},
		// 初始化方法
		mounted () {
			console.log(this.map, this.$router)
		},

		// 方法
		methods: {
			handleTMap (map) {
				this.map = map
				let T = window.T

				//创建图片对象
				var icon = new T.Icon({
					iconUrl: require("../../../assets/logo.png"),
					iconSize: new T.Point(30, 32),
					iconAnchor: new T.Point(10, 25)
				});
				//向地图上添加自定义标注
				var marker = new T.Marker(new T.LngLat(116.411794, 39.9068), {icon: icon});
				var marker1 = new T.Marker(new T.LngLat(116.421794, 39.8068), {icon: icon});
				map.addOverLay(marker);
				map.addOverLay(marker1);



				marker.addEventListener("click", this.mClick);
				marker1.addEventListener("click", () => {

					let markerInfoWin = new window.T.InfoWindow('',{minWidth: 350, maxWidth: 1000})
					markerInfoWin.setContent(this.$refs['map-pop'].$el);

					marker1.openInfoWindow(markerInfoWin);
				});
			},
			mClick (e) {
				let marker = e.target

				let markerInfoWin = new T.InfoWindow('',{minWidth: 350, maxWidth: 1000})
				markerInfoWin.setContent(this.$refs['map-pop'].$el)
				marker.openInfoWindow(markerInfoWin);
			},
			TMapClick (e) {
				console.log(e)
				console.log(e.target.getCenter().getLng() + "," + e.target.getCenter().getLat());
			},
			mapZoomIn () {
				this.map.zoomIn()
			},
			mapZoomOut () {
				this.map.zoomOut()
			},
			toNext (val, id, same) {
				this.newTabTitle = val
				this.$router.push({path: '/sys/index/index3', query: {id: this.$router.encrypt(id), tabLabel: val}})
			},
		}
	}
</script>

<style lang="less" scoped>
	@import "@/assets/styles/common.less";
	.mapDiv {
		width: 100%;
		height: 300px;
	}
	.tx-demo-page {

		.text-right {
			text-align: right;
		}

		.map-head {
			font-weight: 800;
			.num {
				margin: 0 6px;
			}
		}

		.icon-wrapper {
			width: 24px;
			height: 24px;
			text-align: center;
			line-height: 24px;
			color: #999999;
			cursor: pointer;

			&:hover {
				opacity: .8;
			}
		}
	}

</style>

