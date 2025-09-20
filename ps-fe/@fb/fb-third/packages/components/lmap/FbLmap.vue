<template>
	<div class="fb-lmap" ref="lmap">
		<slot></slot>
	</div>
</template>

<script>
	/**
	 * FbLmap 基础封装 leaflet
	 *
	 * */

	import {cloneDeep} from 'lodash-es'

	export default {
		name: 'FbLmap',
		props: {
			options: {
				type: Object,
				default: () => {
					return {}
				}
			},
			basemaps: {
				type: Array,
				default: () => []
			}
		},
		data() {
			return {
				preMarker: {
					marker: null,
					options: [],
				},
			}
		},

		provide() {
			return {
				lmap: this
			}
		},

		watch: {
			basemaps: {
				handler(newArr) {
					this.initBaseMaps(newArr)
				},
				deep: true,
			},
		},

		mounted() {
			this.initMap(Object.assign({
				container: this.$refs['lmap']
			}, this.options))
		},

		beforeDestroy() {
			window.$map = this.map = null
		},

		methods: {
			/**
			 * 数组经纬度 转 纬经度 为了 leaflet
			 * @param options
			 */
			lngLatTolatLng(lngLat) {
				lngLat = lngLat || []

				let latlLng = lngLat
				if (parseFloat(latlLng[0]) > 90) {
					latlLng = [lngLat[1], lngLat[0]]
				}

				return latlLng
			},

			// 边界高亮及遮罩效果
			drawBoundaryMask(blist, maskStyle) {
				const map = this.map
				if (this.maskLayer) {
					this.maskLayer.remove()
					this.maskLayer = null
				}
				let pNW = { lat: 90.0, lng: -180.0 };
				let pNE = { lat: 90.0, lng: 180.0 };
				let pSE = { lat: 0, lng: 180.0 };
				let pSW = { lat: 0, lng: -180.0 };
				let pArray = [];

				pArray.push(pNW);
				pArray.push(pSW);
				pArray.push(pSE);
				pArray.push(pNE);
				pArray.push(pNW);

				for (let i = 0; i < blist.length; i++) {
					let points = [];
					blist[i].forEach((v, k) => {
						points.push({lat:v[1],lng:v[0]});
					})
					pArray = pArray.concat(points);
					pArray.push(pArray[0]);
				}
				let style = Object.assign({ color:'transparent',fillColor:'#111',fillOpacity:0.9 }, maskStyle)
				this.maskLayer = L.polygon(pArray, style);
				this.maskLayer.addTo(map);
			},

			/**
			 * 初始化地图
			 * @param options
			 */
			initMap(options) {
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
					//
				})

				if (this.basemaps.length > 0) {
					this.initBaseMaps(this.basemaps)
				}

				this.$emit('load-map', this.map)



				// 兼容初始化拿不到map
				this.$children.forEach((component) => {
					component.$emit('lmap_ready', this.map)
				})

				return this.map

			},

			/**
			 *
			 * @param points [x,y]
			 * @param zoom 12
			 */
			setCenter(points, zoom) {
				let center = this.lngLatTolatLng(points)

				this.map.setView(center, zoom)
			},
			/**
			 *
			 * @param points [x,y]
			 * @param zoom 12
			 */
			flyTo(points, zoom) {
				let center = this.lngLatTolatLng(points)

				this.map.flyTo(center, zoom)
			},
			flyHome() {

				this.map.setView(this.options.center, this.options.zoom)
			},

			initBaseMaps(arr) {
				for (let i = 0; i < arr.length; i++) {
					let item = arr[i]
					this.addChinaLayer(item.type, item)
				}
			},

			/**
			 * 添加国内厂商瓦片
			 * @param options
			 * @param map
			 */
			addChinaLayer(type, option = {}, map) {
				if (!type) {
					return
				}
				if (type) {
					option.attribution = {
						layerName: type,
					}
				}
				let xyz = L.tileLayer.chinaProviderLngLat(type, option);

				const myMap = map || this.map
				let crsCode = myMap.options.crs.code
				if (crsCode === 'EPSG:3857') {
					xyz = L.tileLayer.chinaProvider(type, option);
				}

				if (map) {
					xyz.addTo(map)
				} else if (this.map) {
					xyz.addTo(this.map)
				}
			},

			/**
			 *
			 * @param options
			 * @param map
			 */
			addLayer(options, map) {

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

			removeLayer(layer) {
				layer && layer.remove()
			},

			addPolygon(coordinates = [], options = {}, map) {
				let settings = Object.assign({}, options)
				let geoSourceLayer = L.polygon([], Object.assign({}, {
					weight: 2,
					color: '#52FFFD',
					fill: true,
				}, options))

				if (map) {
					geoSourceLayer.addTo(map)
				} else if (this.map) {
					geoSourceLayer.addTo(this.map)
				}

				settings.click && geoSourceLayer.on('click', (e) => {
					settings.click.apply(this, [e, geoSourceLayer, settings])
				}, false)
				settings.mouseover && geoSourceLayer.on('mouseover', (e) => {
					settings.mouseover.apply(this, [e, geoSourceLayer, settings])
				}, false)
				settings.mouseout && geoSourceLayer.on('mouseout', (e) => {
					settings.mouseout.apply(this, [e, geoSourceLayer, settings])
				}, false)


				settings.bindPopup && geoSourceLayer.bindPopup(settings.bindPopup.content,
					settings.bindPopup.options || {})

				settings.bindCustomPopup &&
				geoSourceLayer.bindCustomPopup(settings.bindCustomPopup.content,
					settings.bindCustomPopup.options || {})

				let wkts = coordinates.map(p => this.lngLatTolatLng(p))

				geoSourceLayer.setLatLngs(wkts)
				return geoSourceLayer

			},
			addPolyline(coordinates = [], options = {}, map) {
				let settings = Object.assign({}, options)
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

				let wkts = coordinates.map(p => this.lngLatTolatLng(p))

				geoSourceLayer.setLatLngs(wkts)

				settings.click && geoSourceLayer.on('click', (e) => {
					settings.click.apply(this, [e, geoSourceLayer, settings])
				}, false)
				settings.mouseover && geoSourceLayer.on('mouseover', (e) => {
					settings.mouseover.apply(this, [e, geoSourceLayer, settings])
				}, false)
				settings.mouseout && geoSourceLayer.on('mouseout', (e) => {
					settings.mouseout.apply(this, [e, geoSourceLayer, settings])
				}, false)


				settings.bindPopup && geoSourceLayer.bindPopup(settings.bindPopup.content,
					settings.bindPopup.options || {})

				settings.bindCustomPopup &&
				geoSourceLayer.bindCustomPopup(settings.bindCustomPopup.content,
					settings.bindCustomPopup.options || {})

				return geoSourceLayer

			},
			/**
			 * 添加线装饰器
			 * @param points
			 * @param options
			 * @returns geoSourceLayer
			 */
			addPolylineDecorator(coordinates = [], options = {}, map) {
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

			/**
			 * L.geoJSON 纯正版
			 * **/
			addGeoJson(someFeatures, option, map) {
				let settings = Object.assign({}, option)
				let layerGeo = L.geoJSON(someFeatures, {
					style: Object.assign({
						weight: 2,
						color: '#52FFFD',
						fill: false,
					}, settings.style || {}),
				})

				settings.bindPopup && layerGeo.bindPopup(settings.bindPopup.content,
					settings.bindPopup.options || {})
				settings.bindCustomPopup &&
				layerGeo.bindCustomPopup(settings.bindCustomPopup.content,
					settings.bindCustomPopup.options || {})

				if (map) {
					layerGeo.addTo(map)
				} else if (this.map) {
					layerGeo.addTo(this.map)
				}
				return layerGeo
			},

			addGeoJSON(coordinates = [], map) {
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
			addGeoJSONRed(coordinates = [], map) {
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
			 * 添加动画点
			 * @param points
			 * @param options
			 * @returns animatedMarker
			 */
			addAnimatedMarker(routeLine = [], options, map) {
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
			addMarkersCluster(
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
			addMarkersPruneCluster(points = [], options = {}, clusterOptions = {}, map) {
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
			addMarkersSuperCluster(data, options = {}, clusterOptions = {}) {
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
						html: `<div><span>${feature.properties.point_count_abbreviated}</span></div>`,
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
			addMarkers(points = [], options = {}, map) {
				let markers = {
					arr: [],
					remove() {
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
			addMarker(point = {}, options = {}, map) {
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

			removeMarkers(markers = []) {
				markers.forEach(marker => this.removeMarker(marker))
			},

			removeMarker(marker) {
				marker && marker.remove()
			},
			// img标记扩大
			markerImgChange(marker, scale = 2, icon) {
				let iconOpt = cloneDeep(marker.getIcon().options)

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
			preMarkerImg(marker, iconSize = [10, 10], icon) {
				let iconOpt = marker.getIcon().options
				marker.setIcon(icon || L.icon(Object.assign(iconOpt, {
					iconSize: [iconSize[0], iconSize[1]],
				})))
			},
			// div标记扩大
			markerDivChange(marker, scale = 2, icon) {
				let iconOpt = cloneDeep(marker.getIcon().options)

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
			preMarkerDiv(marker, iconSize = [10, 10], icon) {
				let iconOpt = marker.getIcon().options
				marker.setIcon(icon || L.divIcon(Object.assign(iconOpt, {
					iconSize: [iconSize[0], iconSize[1]],
				})))
			},

			addPopup(point = {}, options = {}, map) {
				if (point.lngLat[0] > 90) {
					point.lngLat = this.lngLatTolatLng(point.lngLat)
				}
				let settings = Object.assign({}, {
					className: 'fb-lmap-pop',
				}, options)

				let popup = L.popup(settings).setLatLng(point.lngLat).setContent(point.el ||
					'<p>Hello world!<br />This is a nice popup.</p>').openOn(map || this.map)

				return popup
			},

		},
	}
</script>

<style>
	.fb-lmap {
		width: 100%;
		height: 100%;
	}
</style>
