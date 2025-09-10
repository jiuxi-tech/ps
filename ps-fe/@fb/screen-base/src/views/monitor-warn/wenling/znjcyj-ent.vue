<template>
    <!--  智能监测预警一张图 企业页面  -->
    <fb-container className="screen-page screen-page-index">
        <screen-header/>

        <fb-container>
            <div class="leaflet-map-box">
                <fb-lmap ref="lmap" @load-map="loadMap" :options="lmap.options" :basemaps="lmap.basemaps">
                    <fb-lmap-marker-cluster :data="map_ent_markers" @on-click="entMarkerClick"/>

                </fb-lmap>
            </div>
        </fb-container>



        <screen-panel-left>
            <screen-panel no-header left="8px" top="98px" width="464px" height="974px" padding="16px 24px">
                <screen-panel-title title="某某某某某有限公司">
                    <fb-container @on-click="$router.push('/znjcyj')" absolute :top="0" right="6px" style="cursor: pointer">
<!--                        <img :src="require('../assets/img/common/return-bg.svg')" alt="">-->
                    </fb-container>
                </screen-panel-title>
                <fb-container mt="16px">
                    <fb-container>
                        <fb-space align="center">
                            <fb-avatar icon="node-fill" size="18" circle background-color="rgba(29,105,203, .6)" style="border: 1px solid #419ddb"/>
                            <fb-text>温岭市 松门镇</fb-text>
                        </fb-space>
                    </fb-container>
                    <fb-container mt="6px">
                        <fb-space align="center">
                            <fb-avatar icon="location-fill" size="18" circle background-color="rgba(29,105,203, .6)" style="border: 1px solid #419ddb"/>
                            <fb-text>松门镇xxxxx路196号</fb-text>
                        </fb-space>
                    </fb-container>
                    <fb-container mt="6px">统一社会信用码：913305000000</fb-container>
                    <fb-container mt="6px">法定代表人：李明（15757575757</fb-container>
                    <fb-container mt="6px">主要负责人：钱毅（15758009090</fb-container>
                    <fb-container mt="6px">行业分类：机械制造</fb-container>
                    <fb-container mt="6px">预警事件：<fb-text color="#35D0CA">123</fb-text> 起</fb-container>
                    <fb-container mt="6px">未处置 <fb-text color="#35D0CA">123</fb-text> 起，处置率 <fb-text color="#35D0CA">80%</fb-text></fb-container>
                </fb-container>

                <fb-container mt="16px">
                    <fb-container>
                        <fb-text style="font-weight: bold; font-size: 18px" color="#35D0CA">最新预警记录</fb-text>
                    </fb-container>
                    <fb-container mt="8px">预警时间：2019-09-09 12:21:11</fb-container>
                    <fb-container mt="6px">预警点位：二号车间楼梯</fb-container>
                    <fb-container mt="6px">预警类型：消防通道堵塞</fb-container>
                </fb-container>
                <fb-container mt="16px" class="board-1" width="416px" height="246px">
                    <!--  this.$refs['hik-player'].play(url)   -->
                    <hik-player ref="hik-player" layout="1x1" :width="416" :height="246"></hik-player>
                </fb-container>
                <fb-container mt="16px" class="board-1" width="416px" height="246px">
                    <!--  this.$refs['easy-video-wall'].play(url)   -->
                    <easy-video-wall ref="easy-video-wall" :width="416" :height="246"></easy-video-wall>
                </fb-container>
            </screen-panel>
        </screen-panel-left>


        <screen-panel-right>
            <screen-panel no-header left="1448px" top="98px" width="464px" height="974px">
                <fb-row>
                    <fb-col span="14">
                        <screen-flop-board theme="i-l-v" icon="piezometer-fill" height="64px" unit="路"
                                           :label-style="{textAlign: 'left'}"
                                           icon-bg="linear-gradient(360deg, #5CCFFF 0%, #2FA1FE 100%)"
                                           label="已安装传感</br>设备" :value="222"/>
                        <screen-flop-board mt="10px" theme="i-l-v" icon="camera-fill" height="64px" unit="路"
                                           :label-style="{textAlign: 'left'}"
                                           icon-bg="linear-gradient(360deg, #5CCFFF 0%, #2FA1FE 100%)"
                                           label="已安装视频</br>设备" :value="222"/>
                    </fb-col>
                    <fb-col span="10">
                        <fbec-pie-bar style="height: 150px" ref="ec_r_1"></fbec-pie-bar>
                    </fb-col>
                </fb-row>

                <screen-panel-title title="当前状态" mt="10px"></screen-panel-title>
                <fb-container class="board-1" height="144px" mt="8px">
                    <fb-row>
                        <fb-col span="8">
                            <screen-flop-board theme="vertical_i-l-v" icon="camera-fill" :icon-size="40"
                                               height="144px" unit='路' mt="-12px" no-bg icon-bg="radial-gradient(rgba(31, 53, 127, .1), rgba(31, 53, 127, .9))"
                                               :icon-style="{border: '1px solid #338ff5'}"
                                               label="正常" :value="99" ></screen-flop-board>
                        </fb-col>
                        <fb-col span="8">
                            <screen-flop-board theme="vertical_i-l-v" icon="camera-fill" :icon-size="40"
                                               height="144px" unit='路' mt="-12px" no-bg icon-bg="radial-gradient(rgba(253, 32, 28, .1), rgba(253, 32, 28, .9))"
                                               :icon-style="{border: '1px solid rgba(253, 32, 28, 1)'}"
                                               label="报警" :value="99" :value-style="{color: '#FD201C'}"></screen-flop-board>
                        </fb-col>
                        <fb-col span="8">
                            <screen-flop-board theme="vertical_i-l-v" icon="camera-fill" :icon-size="40"
                                               height="144px" unit='路' mt="-12px" no-bg icon-bg="radial-gradient(rgba(93, 187, 254, .1), rgba(93, 187, 254, .9))"
                                               :icon-style="{border: '1px solid rgba(93, 187, 254, 1)'}"
                                               label="离线" :value="99" :value-style="{color: '#5DBBFE'}"></screen-flop-board>
                        </fb-col>
                    </fb-row>
                </fb-container>

                <screen-panel-title title="设备详细" mt="16px"></screen-panel-title>
                <fb-container mt="8px">
                    <fb-input size="l"  placeholder="输入查询关键词" style="background: rgba(6, 45, 139, 0.8)"
                              clearable @on-enter="doSearch">
                        <fb-button slot="append-button" size="l" icon="search" type="primary"
                                   @on-click="doSearch"></fb-button>
                    </fb-input>
                </fb-container>
                <fb-container mt="8px">
                    <fb-simple-table></fb-simple-table>
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
    import { FbecPieBar } from "@fb/fb-third/src/index";
	import ScreenHeader from "@/file/ScreenHeader";
    export default {
        components: {
			ScreenHeader,
            FbecPieBar
        },
        data() {
            return {

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
                this.updateEc_r_1()
            })
        },
        methods: {
            doSearch() {

            },
            updateEc_r_1() {
                let val = {
                    labelText: '在线率',
                    series: [
                        {data: [70]}
                    ]
                }
                this.$refs['ec_r_1'].updateOptions(val)
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
                    'lmap-marker-ent-red',
                    'lmap-marker-ent-blue',
                    'lmap-marker-video-green',
                    'lmap-marker-sense-orange',
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
                            iconSize: [44, 48],
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
