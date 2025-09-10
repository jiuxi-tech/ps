<template>
    <!--  智能监测预警一张图  -->
    <fb-container className="screen-page screen-page-index">
        <screen-header/>

        <fb-container absolute left="1192px" top="98px" width="240px" height="36px">
            <screen-search @on-search="doSearch" :data="searchList"
                           @on-result-click="doResultSearch"/>
        </fb-container>
        <fb-container class="top-jksp" display="flex">
            <screen-flop-board :value="10" unit="路" :value-style="{color: '#fff', right: 'auto', left: '10px'}" no-bg></screen-flop-board>
            <screen-flop-board :value="55" unit="家" :value-style="{color: '#fff', right: '10px'}" no-bg></screen-flop-board>
        </fb-container>
        <fb-container class="top-cgsb" display="flex">
            <screen-flop-board :value="10" unit="路" :value-style="{color: '#fff', right: 'auto', left: '10px'}" no-bg></screen-flop-board>
            <screen-flop-board :value="55" unit="家" :value-style="{color: '#fff', right: '10px'}" no-bg></screen-flop-board>
        </fb-container>
        <fb-container>
            <div class="leaflet-map-box">
                <fb-lmap ref="lmap" @load-map="loadMap" :options="lmap.options" :basemaps="lmap.basemaps">
                    <fb-lmap-marker-cluster :data="map_ent_markers" @on-click="entMarkerClick"/>

                </fb-lmap>
                <fb-container class="board-1" z-index="999" absolute left="1282px" top="622px" width="158px" padding="2px 16px">
                    <fb-container v-for="(item, index) in mapLegends" :key="item.cls" :mt="index === 0 ? 0 : 8">
                        <fb-space align="center">
                            <fb-container :class="[item.cls]" width="22px" height="24px"></fb-container>
                            <p v-html="item.label"></p>
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
<!--                    <img :src="require('../assets/img/common/floor-bottom.png')" alt="">-->
                </fb-container>
            </screen-panel>
            <screen-panel title="近30天预警事件多发企业TOP5" left="8px" top="266px" width="464px" height="284px">
                <screen-custom-title-bar :data="[
                    {title: '浙江和兴船厂', value: 12, percent: 80},
                    {title: '浙江和兴船厂', value: 12, percent: 80},
                    {title: '浙江和兴船厂', value: 12, percent: 80},
                    {title: '浙江和兴船厂', value: 12, percent: 80},
                    {title: '浙江和兴船厂', value: 12, percent: 80},
                ]"></screen-custom-title-bar>
            </screen-panel>
            <screen-panel title="近30天预警事件多发行业TOP5" left="8px" top="558px" width="464px" height="236px">
                <fbec-bar-base ref="ec_3_1"></fbec-bar-base>
            </screen-panel>
            <screen-panel title="近30天预警处置趋势" left="8px" top="802px" width="464px" height="270px">
                <fbec-line-base ref="ec_4_1"></fbec-line-base>
            </screen-panel>
        </screen-panel-left>


        <screen-panel title="近30天各乡镇预警情况" left="480px" top="802px" width="960px" height="270px">
            <fb-container height="260px" mt="-30px">
                <fbec-bar-base ref="ec_b_1"></fbec-bar-base>
            </fb-container>
        </screen-panel>

        <screen-panel-right>
            <screen-panel title="企业风险分布情况" left="1448px" top="98px" width="464px" height="270px">
                <fb-container class="panel5-box">
                    <fb-container absolute left="200px" top="54px">
                        <fb-icon name="risk-alarm-fill" size="48px" color="#dcf9f4"></fb-icon>
                    </fb-container>

                    <fb-container absolute left="26px" top="10px">
                        <screen-flop-board theme="vertical_l-v" label="今年预警事件" :value="149" height="60" no-bg align="left" :value-style="{color: '#F9FD26'}"></screen-flop-board>
                    </fb-container>
                    <fb-container absolute left="26px" top="94px">
                        <screen-flop-board theme="vertical_l-v" label="总预警事件数" :value="149" height="60" no-bg align="left" :value-style="{color: '#F9FD26'}"></screen-flop-board>
                    </fb-container>
                    <fb-container absolute left="326px" top="10px">
                        <screen-flop-board theme="vertical_l-v" label="今日预警事件" :value="149" height="60" no-bg align="left" :value-style="{color: '#F9FD26'}"></screen-flop-board>
                    </fb-container>
                    <fb-container absolute left="326px" top="94px">
                        <screen-flop-board theme="vertical_l-v" label="未处置事件数" :value="149" height="60" no-bg align="left" :value-style="{color: '#F9FD26'}"></screen-flop-board>
                    </fb-container>
                </fb-container>
                <screen-custom-title-bar style="margin-top: 12px" :data="[
                    {title: '浙江和兴船厂', value: 12, percent: 80},
                ]"></screen-custom-title-bar>
            </screen-panel>
            <screen-panel title="近30天预警事件类型分布" left="1448px" top="376px" width="464px" height="240px">
                <fbec-pie-custom-legend ref="ec_6_1"></fbec-pie-custom-legend>
            </screen-panel>
            <screen-panel title="近24h监测预警记录" left="1448px" top="624px" width="464px" height="448px">
                <fb-simple-table :rownum="false" :show-header="false" :columns="panel7Table.columns" :data="panel7Table.data">
                    <template #a1="props">
                        <fb-avatar icon="pin-fill" size="24" circle background-color="rgba(29,105,203, .6)" style="border: 1px solid #419ddb"/>
                    </template>
                    <template #a5="props">
                        <fb-icon v-show="props.row.a5" name="selected-circle-fill" color="#00a862"></fb-icon>
                    </template>
                </fb-simple-table>
            </screen-panel>
        </screen-panel-right>

    </fb-container>
</template>

<script>
    /**
     * zhfxts
     * (c) 2021 lincong1987
     */
    import {FbecLineBase, FbecBarBase, FbecBarHorizontal, FbecBarBaseTwoy, FbecPieCustomLegend} from '@fb/fb-third/src/index'
import {linearColors} from "@fb/fb-third/packages/components/echarts/utils";
	import ScreenHeader from "@/file/ScreenHeader";

    export default {
        components: {
			ScreenHeader,
            FbecLineBase,
            FbecBarBase,
            FbecBarHorizontal,
            FbecBarBaseTwoy,
            FbecPieCustomLegend
        },
        data() {
            return {
                mapLegends: [
                    {label: '24h有预警</br>事件企业点位', cls: 'lmap-marker-ent-red'},
                    {label: '监测企业点位', cls: 'lmap-marker-ent-blue'},
                    {label: '视频', cls: 'lmap-marker-video-green'},
                    {label: '传感', cls: 'lmap-marker-sense-orange'},
                ],

                panel7Table: {
                    columns: [
                        {name: 'a1', label: '', slot: 'a1', width: 30},
                        {name: 'a2', label: '', width: 120},
                        {name: 'a3', label: '', width: 80},
                        {name: 'a4', label: ''},
                        {name: 'a5', label: '', slot: 'a5', width: 30},
                    ],
                    data: [
                        {a1: 'a', a2: '03/26 15:55:32', a3: '人员超限', a4: '浙江金太水产品有限公司', a5: true},
                        {a1: 'a', a2: '03/26 15:55:32', a3: '人员超限', a4: '浙江金太水产品有限公司', a5: true},
                        {a1: 'a', a2: '03/26 15:55:32', a3: '人员超限', a4: '浙江金太水产品有限公司', a5: false},
                        {a1: 'a', a2: '03/26 15:55:32', a3: '人员超限', a4: '浙江金太水产品有限公司', a5: false},
                        {a1: 'a', a2: '03/26 15:55:32', a3: '人员超限', a4: '浙江金太水产品有限公司', a5: true},
                    ]
                },

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
                this.updateEc_b_1()
                this.updateEc_6_1()
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
                    grid: {
                        bottom: 12
                    },
                    xAxis: {
                        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
                    },
                    series: [
                        {
                            name: '',
                            data: [820, 932, 901, 934, 1290, 1330, 1320],
                            itemStyle: {
                                color: linearColors.y2.purple
                            }
                        }
                    ]
                }
                this.$refs['ec_3_1'].updateOptions(val)
            },
            updateEc_4_1() {
                let val = {
                    grid: {
                        bottom: 6
                    },
                    xAxis: {
                        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
                    },
                    legend: {
                        show: true,
                    },
                    series: [
                        {
                            name: '处置数',
                            type: 'line',
                            data: [120, 132, 101, 134, 90, 230, 210]
                        },
                        {
                            name: '预警数',
                            type: 'line',
                            itemStyle: {
                                color: '#FDCA30'
                            },
                            data: [220, 182, 191, 234, 290, 330, 310]
                        },
                    ]
                }
                this.$refs['ec_4_1'].updateOptions(val)
            },
            updateEc_b_1() {
                let val = {
                    grid: {
                        bottom: 12
                    },
                    legend: {
                        show: true,
                        left: 'right'
                    },
                    xAxis: {
                        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
                    },
                    series: [
                        {
                            name: '预警事件处置数',
                            data: [820, 932, 901, 934, 1290, 1330, 1320],
                        },
                        {
                            name: '预警事件发生数',
                            data: [820, 932, 901, 934, 1290, 1330, 1320],
                            itemStyle: {
                                color: linearColors.y2.yellow
                            }
                        }
                    ]
                }
                this.$refs['ec_b_1'].updateOptions(val)
            },
            updateEc_6_1() {
                /**
                 * 吸取工业经验
                 * value 为图形补偿展示
                 * showValue 为图形数值展示
                 */
                let val = {
                    series: [{
                        data: [
                            {name: '金属制品加工', value: 10, showValue: 10},
                            {name: '木制品/纸制品加工', value: 10, showValue: 10},
                            {name: '农副产品加工', value: 10, showValue: 10},
                            {name: '纺织品加工', value: 10, showValue: 10},
                            {name: '橡胶和塑料制品加工', value: 10, showValue: 10},
                            {name: '冶金/有色/建材行业加工', value: 10, showValue: 10},
                            {name: '其他 ', value: 10, showValue: 10},
                        ]
                    }]
                }
                this.$refs['ec_6_1'].updateOptions(val)
            },

            // map-start
            loadMap() {
                this.drawEntMarkers()
            },
            // 初始化 100 个点
            initMarkers100(lng, lat) {
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
            drawEntMarkers() {
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
.panel5-box {
	position: relative;
	height: 160px;
	background: linear-gradient(180deg, rgba(12, 20, 122, 0) 0%, rgba(12, 20, 122, 0.21) 67%, rgba(12, 20, 122, 0.35) 90%, rgba(12, 20, 122, 0.5) 100%);
	background-size: 100% 100%;
}

.top-jksp {
	padding-top: 30px;
	position: absolute;
	left: 787px;
	top: 106px;
	width: 162px;
	height: 75px;
	background: linear-gradient(180deg, rgba(12, 20, 122, 0) 0%, rgba(12, 20, 122, 0.21) 67%, rgba(12, 20, 122, 0.35) 90%, rgba(12, 20, 122, 0.5) 100%);
}
.top-cgsb {
	padding-top: 30px;
	position: absolute;
	left: 971px;
	top: 106px;
	width: 162px;
	height: 75px;
	background: linear-gradient(180deg, rgba(12, 20, 122, 0) 0%, rgba(12, 20, 122, 0.21) 67%, rgba(12, 20, 122, 0.35) 90%, rgba(12, 20, 122, 0.5) 100%);
}
</style>
