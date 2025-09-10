/*!
 * Map
 * (c) 2021 lincong1987
 */

let maps = {
    // 在线天地图影像服务地址(经纬度)
    tdt_img_c: '//'+process.env.VUE_APP_TDT_WMTS+'/img_c/wmts?layer=img&style=default&tilematrixset=c&Service=WMTS&Request=GetTile&Version=1.0.0&Format=tiles&TileMatrix={z}&TileCol={x}&TileRow={y}&tk=d5ae39080ea1a9aea012966185e6b5b1',
    //在线天地图矢量地图服务(经纬度)
    tdt_vec_c: '//'+process.env.VUE_APP_TDT_WMTS+'/vec_c/wmts?layer=vec&style=default&tilematrixset=c&Service=WMTS&Request=GetTile&Version=1.0.0&Format=tiles&TileMatrix={z}&TileCol={x}&TileRow={y}&tk=d5ae39080ea1a9aea012966185e6b5b1',
    // 在线天地图影像中文标记服务(经纬度)
    tdt_cia_c: '//'+process.env.VUE_APP_TDT_WMTS+'/cia_c/wmts?layer=cia&style=default&tilematrixset=c&Service=WMTS&Request=GetTile&Version=1.0.0&Format=tiles&TileMatrix={z}&TileCol={x}&TileRow={y}&tk=d5ae39080ea1a9aea012966185e6b5b1',
    // 在线天地图矢量中文标记服务(经纬度)
    tdt_cva_c: '//'+process.env.VUE_APP_TDT_WMTS+'/cva_c/wmts?layer=cva&style=default&tilematrixset=c&Service=WMTS&Request=GetTile&Version=1.0.0&Format=tiles&TileMatrix={z}&TileCol={x}&TileRow={y}&tk=d5ae39080ea1a9aea012966185e6b5b1',


    // 天地图影像 带标注
    // tdt_img_c: 'http://jcfx.yjt.zj.gov.cn:8081/grassRoots/cachetianditu/wmts?x={x}&y={y}&z={z}&type=img_c',
    // tdt_img_w 不行
    // tdt_img_w:
    // 'http://jcfx.yjt.zj.gov.cn:8081/grassRoots/cachetianditu/wmts?x={x}&y={y}&z={z}&type=img_w',
    // 天地图矢量 带标注
    // tdt_vec_c: 'http://jcfx.yjt.zj.gov.cn:8081/grassRoots/cachetianditu/wmts?x={x}&y={y}&z={z}&type=vec_c',
    // tdt_vec_w 不行
    //tdt_vec_w:
    // 'http://jcfx.yjt.zj.gov.cn:8081/grassRoots/cachetianditu/wmts?x={x}&y={y}&z={z}&type=vec_w',
    // 以下不使用
    // tdt_cia_c: 'http://jcfx.yjt.zj.gov.cn:8081/grassRoots/cachetianditu/wmts?x={x}&y={y}&z={z}&type=cia_c',
    // tdt_cia_w: 'http://jcfx.yjt.zj.gov.cn:8081/grassRoots/cachetianditu/wmts?x={x}&y={y}&z={z}&type=cia_w',
    // tdt_cva_c: 'http://jcfx.yjt.zj.gov.cn:8081/grassRoots/cachetianditu/wmts?x={x}&y={y}&z={z}&type=cva_c',
    // tdt_cva_w: 'http://jcfx.yjt.zj.gov.cn:8081/grassRoots/cachetianditu/wmts?x={x}&y={y}&z={z}&type=cva_w',

    // 浙江地图影像地图
    zj_imgmap: '//ditu.zjzwfw.gov.cn/services/wmts/imgmap/default/oss?' +
        'layer=img&style=default&tilematrixset=c&Service=WMTS&Request=GetTile&Version=1.0.0&Format=tiles' +
        '&TileMatrix={z}&TileCol={x}&TileRow={y}' +
        '&token=8b56a4a0-95c3-4e68-83fe-52286ec561a9',
    // 浙江标注地图影像地图
    zj_imgmap_lab: '//ditu.zjzwfw.gov.cn/services/wmts/imgmap_lab/default/oss?' +
        'layer=img&style=default&tilematrixset=c&Service=WMTS&Request=GetTile&Version=1.0.0&Format=tiles' +
        '&TileMatrix={z}&TileCol={x}&TileRow={y}' +
        '&token=89646f61-3b46-4e91-bdca-0e4b19bd5ca5',
    // 科技样式矢量地图
    zjvmap: '//ditu.zjzwfw.gov.cn/mapserver/vmap/zjvmap/getMAP' +
        '?x={x}&y={y}&l={z}' +
        '&styleId=tdt_kejiganyangshi_2017&ratio=1&titlesize=256' +
        '&token=4719ae0e69eaaab8a188078d6e22e282',
    // 县行政区划面
    zjxzqh_py: '//ditu.zjzwfw.gov.cn/mapserver/vmap/zjxzqh_py/getMAP' +
        '?x={x}&y={y}&l={z}' +
        '&styleId=zjcoupy&ratio=1&titlesize=256' +
        '&token=4719ae0e69eaaab8a188078d6e22e282',
    
   // 省各级行政区划面含海域_2021Q2
    
    zjxzqh_py202104: 'https://ditu.zjzwfw.gov.cn:443/mapserver/data/zjxzqh_py202104/getData' +
        '?x={x}&y={y}&l={z}' +
        '&styleId=zjboupy202104' +
        '&token=4719ae0e69eaaab8a188078d6e22e282',
}
import { cloneDeep } from 'lodash-es'

export default {

    data () {
        return {
            preMarker: {
                marker: null,
                options: [],
            },
        }
    },

    methods: {
        /**
         * 数组经纬度 转 纬经度 为了 leaflet
         * @param options
         */
        lngLatTolatLng (lngLat) {
            lngLat = lngLat || []

            let latlLng = lngLat
            if (parseFloat(latlLng[0]) > 90) {
                latlLng = [lngLat[1], lngLat[0]]
            }

            return latlLng
        },

        /**
         * 初始化地图
         * @param options
         */
        initMap (options) {
            if (options.center && options.center[0] > 90) {
                options.center = this.lngLatTolatLng(options.center)
            }
            if (this.map) return

            window.$map =
                this.map = L.map(options.container || 'map', Object.assign({}, {
                    crs: L.CRS.EPSG4326,
                    preferCanvas: true,
                    renderer: L.canvas(),
                    maxZoom: 18,
                    minZoom: 6,
                    zoom: 7,
                    center: {
                        lat: 29.506136141717434,
                        lng: 120.68858698010445,
                    },
                }, options))

            this.map.on('tileerror', () => {
                // debugger
            })

            return this.map

        },

        /**
         *
         * @param points [x,y]
         * @param zoom 12
         */
        setCenter (points, zoom) {
            let center = this.lngLatTolatLng(points)

            this.map.setView(center, zoom)
        },
        /**
         *
         * @param options
         * @param map
         */
        addLayer (options, map) {

            let url = (options.name && maps[options.name]) || options.url || ''
            if (!url) {
                return
            }

            let layerOptions = Object.assign({}, {
                maxZoom: 20,
                tileSize: 256,
                zoomOffset: 1,
                // errorTileUrl:
                // 'http://t4.tianditu.gov.cn/img_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX=10&TILEROW=405&TILECOL=863&tk=eec8c7ee00d8d62dd60a274aa1a1beb5',
                attribution: {
                    layerName: options.name,
                },
            }, options)

            if (options.name === 'zj_imgmap'
                || options.name === 'zj_imgmap_lab') {
                layerOptions.bounds = [L.latLng(32, 118), L.latLng(27, 123)]
            }

            let xyz = L.tileLayer(url, layerOptions)

            if (map) {
                xyz.addTo(map)
            } else if (this.map) {
                xyz.addTo(this.map)
            }

            return xyz
        },

        removeLayer (layer) {
            layer && layer.remove()
        },

        addPolyline (coordinates = [], options = {}, map) {
            let geoSourceLayer = L.polyline([], Object.assign({}, {
                weight: 2,
                color: '#52FFFD',
                fill: false,
            }, options))

            if (map) {
                geoSourceLayer.addTo(map)
            } else if (this.map) {
                geoSourceLayer.addTo(this.map)
            }

            let wkts = coordinates.map(p => [p[1], p[0]])

            geoSourceLayer.setLatLngs(wkts)

            return geoSourceLayer

        },
        /**
         * 添加线装饰器
         * @param points
         * @param options
         * @returns geoSourceLayer
         */
        addPolylineDecorator (coordinates = [], options = {}, map) {
            let wkts = coordinates.map(p => [p[1], p[0]])

            let geoSourceLayer = L.polylineDecorator(wkts, {
                patterns: [
                    {
                        repeat: options.repeat || 50,
                        symbol: L.Symbol.arrowHead(Object.assign({}, {
                            pixelSize: 5,
                            headAngle: 75,
                            polygon: false,
                            pathOptions: {
                                stroke: true,
                                weight: 2,
                                color: '#FFFFFF',
                            },
                        }, options.symbol)),
                    },
                ],
            })

            if (map) {
                geoSourceLayer.addTo(map)
            } else if (this.map) {
                geoSourceLayer.addTo(this.map)
            }

            return geoSourceLayer

        },

        addGeoJSON (coordinates = [], map) {
            let geoSourceLayer = L.polygon([], {
                weight: 2,
                color: '#52FFFD',
                fill: false,
            })

            if (map) {
                geoSourceLayer.addTo(map)
            } else if (this.map) {
                geoSourceLayer.addTo(this.map)
            }

            let wkts = coordinates.map(gem => {
                if (gem[0][0][0]) {
                    return gem[0].map(p => [p[1], p[0]])
                } else {
                    return gem.map(p => [p[1], p[0]])
                }
            })

            geoSourceLayer.setLatLngs(wkts)

            return geoSourceLayer

        },
        addGeoJSONRed (coordinates = [], map) {
            let geoSourceLayer = L.polygon([], {
                weight: 2,
                color: '#f00',
                fill: false,

            })

            if (map) {
                geoSourceLayer.addTo(map)
            } else if (this.map) {
                geoSourceLayer.addTo(this.map)
            }

            let wkts = coordinates.map(gem => {
                return gem[0].map(p => [p[1], p[0]])
            })

            geoSourceLayer.setLatLngs(wkts)

            return geoSourceLayer

        },

        removeGeoLayers (geoLayers = [], map) {
            geoLayers.forEach(geoLayer => this.removeGeoLayer(geoLayer, map))
        },

        removeGeoLayer (geoLayer, map) {
            if (map) {
                map.removeLayer(geoLayer)
            } else if (this.map) {
                this.map.removeLayer(geoLayer)
            }
        },
        /**
         * 添加动画点
         * @param points
         * @param options
         * @returns animatedMarker
         */
        addAnimatedMarker (routeLine = [], options, map) {
            // let carIcon = L.icon({
            // 	iconSize: [37, 26],
            // 	iconAnchor: [19, 13],
            // 	iconUrl: require('../assets/img/car.png')
            // })
            // 动态marker
            let animatedMarker = L.animatedMarker(routeLine, Object.assign({}, {
                speedList: [],
                interval: 10, // 默认为100mm
                // icon: carIcon,
                // playCall: updateRealLine
            }, options))

            if (map) {
                animatedMarker.addTo(map)
            } else if (this.map) {
                animatedMarker.addTo(this.map)
            }

            return animatedMarker
        },

        /**
         * 添加点聚合
         * @param points
         * @param options
         * @returns {[]}
         */
        addMarkersCluster (
            points = [], options = {}, clusterOptions = {}, map) {
            let markersCluster = L.markerClusterGroup(
                Object.assign({}, {}, clusterOptions))

            points.forEach(p => {
                markersCluster.addLayer(this.addMarker(p, options, map))
            })

            if (map) {
                map.addLayer(markersCluster)
            } else if (this.map) {
                this.map.addLayer(markersCluster)
            }

            return markersCluster
        },
        // 修剪版 faster 点聚合
        addMarkersPruneCluster (points = [], options = {}, clusterOptions = {}, map) {
            let markersCluster = new PruneClusterForLeaflet()

            points.forEach(p => {
                let marker = this.addMarker(p, options, map)
                markersCluster.RegisterMarker(marker)
            })

            if (map) {
                map.addLayer(markersCluster)
            } else if (this.map) {
                this.map.addLayer(markersCluster)
            }

            return markersCluster
        },

        // 多线程 superCluster 点聚合
        addMarkersSuperCluster (data, options = {}, clusterOptions = {}){
            let map = this.map
            let _this = this

            _this.workerCluster = new Worker("assets/leaflet/plugins/workerCluster.js");

            this.SuperClusterReady = false;
            _this.workerCluster.postMessage({data: data})

            const markers = L.geoJson(null, {
                pointToLayer: createClusterIcon
            }).addTo(map);

            _this.workerCluster.addEventListener("message", (e) => {
                // console.log(e.data, 333333333333333)
                if (e.data.ready) {
                    _this.SuperClusterReady = true;
                    update();
                } else if (e.data.expansionZoom) {
                    map.flyTo(e.data.center, e.data.expansionZoom);
                } else {
                    markers.clearLayers();
                    markers.addData(e.data);
                }
            })

            function update() {
                if (!_this.SuperClusterReady) return;
                const bounds = map.getBounds();
                _this.workerCluster.postMessage({
                    bbox: [bounds.getWest(), bounds.getSouth(), bounds.getEast(), bounds.getNorth()],
                    zoom: map.getZoom()
                });
            }

            map.on('moveend', update);

            function createClusterIcon(feature, latlng) {
                // if (!feature.properties.cluster) return L.marker(latlng, {
                //     title: feature.properties.entFullName || 'ceshi'
                // });
                if (!feature.properties.cluster) return _this.addMarker({
                    lngLat: latlng,
                    title: feature.properties.entFullName || 'ceshi',
                    icon: L.divIcon({
                        iconSize: [24, 34],
                        html: `<div class="risk-marker-1"></div>`,
                    })
                }, options)

                const count = feature.properties.point_count;
                const size =
                    count < 100 ? 'small' :
                        count < 1000 ? 'medium' : 'large';
                const icon = L.divIcon({
                    html: `<div><span>${  feature.properties.point_count_abbreviated  }</span></div>`,
                    className: `marker-cluster marker-cluster-${size}`,
                    iconSize: L.point(40, 40)
                });

                return L.marker(latlng, {icon});
            }

            markers.on('click', (e) => {
                if (e.layer.feature.properties.cluster_id) {
                    _this.workerCluster.postMessage({
                        getClusterExpansionZoom: e.layer.feature.properties.cluster_id,
                        center: e.latlng
                    });
                }
            });
        },

        /**
         * 批量添加标记
         * @param points
         * @param options
         * @returns {[]}
         */
        addMarkers (points = [], options = {}, map) {
            let markers = {
                arr: [],
                remove(){
                    this.arr.forEach(marker => {
                        marker.remove()
                    })
                    this.arr = []
                },
                eachLayer(callback) {
                    this.arr.forEach(marker => {
                        callback(marker)
                    })
                }
            }

            points.forEach(p => {
                markers.arr.push(this.addMarker(p, options, map))
            })

            return markers
        },

        /**
         * 添加标记
         * @param point
         * @param options
         * @param map
         * @returns {*}
         */
        addMarker (point = {}, options = {}, map) {
            if (point.lngLat[0] > 90) {
                point.lngLat = this.lngLatTolatLng(point.lngLat)
            }

            let settings
            if (point.icon) {
                settings = Object.assign({}, options, point)
            } else {
                settings = Object.assign({}, options)
            }

            // 向前兼容之前语法
            let el = document.createElement('div')
            if (!settings.icon) {
                for (let name in settings.style) {
                    if ('height width top left'.split(' ').includes(name)) {
                        settings.style[name] =
                            typeof settings.style[name] === 'number'
                                ? `${settings.style[name]}px`
                                : settings.style[name]
                    }
                    el.style[name] = settings.style[name]
                }

                settings.icon = L.divIcon({
                    html: el,
                    iconSize: [
                        settings.style.width || 12,
                        settings.style.height || 12,
                    ],
                })
            }

            let marker
            if (settings.noAddTo) {
                marker = L.marker(point.lngLat, settings)
            } else {
                marker = L.marker(point.lngLat, settings).addTo(map || this.map)
            }

            settings.click && marker.on('click', (e) => {
                settings.click.apply(this, [e, marker, point, settings])
            }, false)
            settings.mouseover && marker.on('mouseover', (e) => {
                settings.mouseover.apply(this, [e, marker, point, settings])
            }, false)
            settings.mouseout && marker.on('mouseout', (e) => {
                settings.mouseout.apply(this, [e, marker, point, settings])
            }, false)

            settings.afterAdd &&
            settings.afterAdd.apply(this, [el, point, marker, settings])

            settings.bindPopup && marker.bindPopup(settings.bindPopup.content,
                settings.bindPopup.options || {})
            settings.bindCustomPopup &&
            marker.bindCustomPopup(settings.bindCustomPopup.content,
                settings.bindCustomPopup.options || {})

            return marker
        },

        removeMarkers (markers = []) {
            markers.forEach(marker => this.removeMarker(marker))
        },

        removeMarker (marker) {
            marker && marker.remove()
        },
        // img标记扩大
        markerImgChange (marker, scale = 2, icon) {
            let iconOpt = marker.getIcon().options

            if (!this.preMarker.marker) {
                this.preMarker.marker = marker
                this.preMarker.options = cloneDeep(iconOpt)
            } else {
                if (iconOpt.iconSize[0] >= this.preMarker.options.iconSize[0] *
                    scale) { // 大于倍数返回
                    return false
                }

                this.preMarkerImg(this.preMarker.marker, [],
                    L.icon(this.preMarker.options))
                this.preMarker.marker = marker
                this.preMarker.options = cloneDeep(iconOpt)
            }

            marker.setIcon(icon || L.icon(Object.assign(iconOpt, {
                iconSize: [
                    iconOpt.iconSize[0] * scale,
                    iconOpt.iconSize[1] * scale,
                ],
            })))
        },
        preMarkerImg (marker, iconSize = [10, 10], icon) {
            let iconOpt = marker.getIcon().options
            marker.setIcon(icon || L.icon(Object.assign(iconOpt, {
                iconSize: [iconSize[0], iconSize[1]],
            })))
        },
        // div标记扩大
        markerDivChange (marker, scale = 2, icon) {
            let iconOpt = marker.getIcon().options

            if (!this.preMarker.marker) {
                this.preMarker.marker = marker
                this.preMarker.options = cloneDeep(iconOpt)
            } else {
                if (iconOpt.iconSize[0] >= this.preMarker.options.iconSize[0] *
                    scale) { // 大于倍数返回
                    return false
                }

                this.preMarkerDiv(this.preMarker.marker, [],
                    L.divIcon(this.preMarker.options))
                this.preMarker.marker = marker
                this.preMarker.options = cloneDeep(iconOpt)
            }

            marker.setIcon(icon || L.divIcon(Object.assign(iconOpt, {
                iconSize: [
                    iconOpt.iconSize[0] * scale,
                    iconOpt.iconSize[1] * scale,
                ],
            })))

            console.log(marker, marker.getIcon().options)
        },
        preMarkerDiv (marker, iconSize = [10, 10], icon) {
            let iconOpt = marker.getIcon().options
            marker.setIcon(icon || L.divIcon(Object.assign(iconOpt, {
                iconSize: [iconSize[0], iconSize[1]],
            })))
        },

        addPopup (point = {}, options = {}, map) {
            if (point.lngLat[0] > 90) {
                point.lngLat = this.lngLatTolatLng(point.lngLat)
            }
            let settings = Object.assign({}, {
                className: 'fb-lmap-pop',
            }, options)

            let popup = L.popup(settings).
            setLatLng(point.lngLat).
            setContent(point.el ||
                '<p>Hello world!<br />This is a nice popup.</p>').
            openOn(map || this.map)

            return popup
        },

    },

    // destroyed() {
    //     window.$map.remove()
    // }
}
