/*!
 * Map
 * (c) 2021 lincong1987
 */

let maps = {
    // 全国影像地图
    tdt_img_c: `https://t0.tianditu.gov.cn/img_c/wmts?tk=eec8c7ee00d8d62dd60a274aa1a1beb5&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=c&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&FORMAT=tiles`,
    // 浙江影像地图
    zj_imgmap: `http://ditu.zjzwfw.gov.cn/services/wmts/imgmap/default/oss?token=89646f61-3b46-4e91-bdca-0e4b19bd5ca5&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=imgmap&STYLE=default&TILEMATRIXSET=esritilematirx&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&FORMAT=image%2Fjpgpng`,
    // 浙江影像地图注记
    zj_imgmap_lab: `https://ditu.zjzwfw.gov.cn/services/wmts/imgmap_lab/default/oss?token=89646f61-3b46-4e91-bdca-0e4b19bd5ca5&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=imgmap&STYLE=default&TILEMATRIXSET=esritilematirx&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&FORMAT=image%2Fjpgpng`,
}

let layouts = {};

export default {

    methods: {

        /**
         * 初始化地图
         * @param options
         */
        initMap(options) {
            if (window.$map) {
                if (window.$map._container.id == options.container) {
                    //  清空所有覆盖物
                    //  window.$map.removeAllLayer();
                    return;
                }
            }
            // 清空图层
            layouts = {};
            // 初始化地图
            window.$map = this.map = new Geoway.Map(Object.assign({}, {
                container: 'fxtsytq_4',
                pitch: 30,
                zoom: 8,

                minZoom: 7,
//				maxZoom: 12,

                maxTileCacheSize: 100,
                bearing: 0,
                center: [
                    120.72397338045653,
                    29.564950478939834,
                ],
            }, options))


            return this.map
        },

        setCenter(points, zoom) {
            this.map.flyTo({center: points, zoom: zoom})
        },
        /**
         *
         * @param options
         * @param map
         */
        addLayer(options, map) {
            // 避免重复添加
            if (layouts[options.name]) {
                return
            }

            let url = (options.name && maps[options.name]) || options.url || ''
            if (!url) {
                return
            }

            let xyz = new Geoway.XYZ([url],
                Object.assign({}, {
                    tileSize: 256,
                }, options))

            if (map) {
                map.addLayer(xyz)
            } else if (this.map) {
                this.map.addLayer(xyz)
            }

            layouts[options.name] = options;

            return xyz
        },

        removeLayer(layer) {
            layer && layer.remove()
        },

        addGeoJSON(coordinates = [], map) {
            let geoSourceLayer = new Geoway.GeoSourceLayer(map || this.map)

            if (map) {
                map.addLayer(geoSourceLayer)
            } else if (this.map) {
                this.map.addLayer(geoSourceLayer)
            }

            let wkts = coordinates.map(gem => {
                return gem[0].map(p => `${p[0]} ${p[1]}`).join(',')
            })

            wkts.forEach(wkt => {
                let feature = {}
                feature.wkt = `LINESTRING(${wkt})`
                feature.paintProperties = {
                    strokeColor: '#52FFFD',
                    strokeWidth: 2,
                    strokeOpacity: 0.8,
                    lineCap: 'round',
                    lineJoin: 'round',
                }
                geoSourceLayer.addFeature(feature)
            })

            return geoSourceLayer

        },

        addGeoJSONRed(coordinates = [], map) {
            let geoSourceLayer = new Geoway.GeoSourceLayer(map || this.map)

            if (map) {
                map.addLayer(geoSourceLayer)
            } else if (this.map) {
                this.map.addLayer(geoSourceLayer)
            }

            let wkts = coordinates.map(gem => {
                return gem[0].map(p => `${p[0]} ${p[1]}`).join(',')
            })

            wkts.forEach(wkt => {
                let feature = {}
                feature.wkt = `LINESTRING(${wkt})`
                feature.paintProperties = {
                    strokeColor: '#FF0000',
                    strokeWidth: 2,
                    strokeOpacity: 0.8,
                    lineCap: 'round',
                    lineJoin: 'round',
                }
                geoSourceLayer.addFeature(feature)
            })

            return geoSourceLayer

        },

        removeGeoLayers(geoLayers = [], map) {
            geoLayers.forEach(geoLayer => this.removeGeoLayer(geoLayer, map))
        },

        removeGeoLayer(geoLayer, map) {
            if (map) {
                map.removeLayer(geoLayer)
            } else if (this.map) {
                this.map.removeLayer(geoLayer)
            }
        },

        /**
         * 批量添加标记
         * @param points
         * @param options
         * @returns {[]}
         */
        addMarkers(points = [], options = {}, map) {
            let markers = []
            points.forEach(p => {
                markers.push(this.addMarker(p, options, map))
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
        addMarker(point = {}, options = {}, map) {
            let settings = Object.assign({}, {
                'class': 'fb-marker',
                style: {},
                offset: [0, 0],
                minLevel: 2,
                click: null,
                mouseover: null,
                mouseout: null,
            }, options)

            let el = document.createElement('div')

            el.className = settings.class
            for (let name in settings.style) {
                if ('height width top left'.split(' ').includes(name)) {
                    settings.style[name] =
                        typeof settings.style[name] === 'number'
                            ? `${settings.style[name]}px`
                            : settings.style[name]
                }
                el.style[name] = settings.style[name]
            }

            let marker = new Geoway.Marker(el, settings).setLngLat(
                point.lngLat).addTo(map || this.map)

            settings.click && el.addEventListener('click', (e) => {
                settings.click.apply(this, [e, marker, point, settings, el])
            }, false)
            settings.mouseover && el.addEventListener('mouseover', (e) => {
                settings.mouseover.apply(this, [e, marker, point, settings, el])
            }, false)
            settings.mouseout && el.addEventListener('mouseout', (e) => {
                settings.mouseout.apply(this, [e, marker, point, settings, el])
            }, false)

            settings.afterAdd &&
            settings.afterAdd.apply(this, [el, point, marker, settings])

            return marker
        },

        removeMarkers(markers = []) {
            markers.forEach(marker => this.removeMarker(marker))
        },

        removeMarker(marker) {
            marker && marker.remove()
        },

        addPopup(options, map) {
            let popup = new Geoway.Popup()
            let settings = Object.assign({}, {}, options)
            if (!options.lngLat) {
                return
            }
            popup.setLngLat(options.lngLat)
            options.text && popup.setText(options.text)
            popup.addTo(map || this.map)

            return popup
        },

    },

}
