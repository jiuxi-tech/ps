/*!
 * Layers
 * (c) 2021 lincong1987
 */

//var wmtsUrl_1 = 'http://t{0-7}.tianditu.gov.cn/vec_w/wmts?tk='; //矢量底图
//var wmtsUrl_2 = 'http://t{0-7}.tianditu.gov.cn/cva_w/wmts?tk='; //矢量注记
//
//var wmtsUrl_3 = 'http://t{0-7}.tianditu.gov.cn/img_w/wmts?tk='; //影像底图
//var wmtsUrl_4 = 'http://t{0-7}.tianditu.gov.cn/cia_w/wmts?tk='; //影像注记
//
//var wmtsUrl_5 = 'http://t{0-7}.tianditu.gov.cn/ter_w/wmts?tk='; //地形底图
//var wmtsUrl_6 = 'http://t{0-7}.tianditu.gov.cn/cta_w/wmts?tk='; //地形注记
//
//var wmtsUrl_7 = 'http://t{0-7}.tianditu.gov.cn/ibo_w/wmts?tk='; //境界（省级以上）
//var wmtsUrl_8 = 'http://t{0-7}.tianditu.gov.cn/eva_w/wmts?tk='; //矢量英文注记

import L from 'leaflet'

if (L.Proj) {
	L.CRS.Baidu = new L.Proj.CRS('EPSG:900913',
		'+proj=merc +a=6378206 +b=6356584.314245179 +lat_ts=0.0 +lon_0=0.0 +x_0=0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +wktext  +no_defs',
		{
			resolutions: function () {
				let level = 19
				let res = []
				res[0] = Math.pow(2, 18)
				for (let i = 1; i < level; i++) {
					res[i] = Math.pow(2, (18 - i))
				}
				return res
			}(),
			origin: [0, 0],
			bounds: L.bounds([20037508.342789244, 0], [0, 20037508.342789244]),
		})
}


export const ZJEPSG4326 = L.CRS.ZJEPSG4326 = Object.assign({}, L.CRS.Earth, {
	code: 'EPSG:4326',
	projection: L.Projection.LonLat,
	transformation: new L.Transformation(1 / 180, 1, -1 / 180, .5),
	scale: function (t) {
		return 256 * Math.pow(2, t - 1)
	},
})

L.TileLayer.BizLayer = L.TileLayer.extend({
	
	initialize: function (type, options) { // (type, Object)
		let providers = L.TileLayer.BizLayer.layers
		
		options = options || {}
		
		let parts = type.split('.')
		
		let providerName = parts[0]
		let mapName = parts[1]
		let mapType = parts[2]
		
		let url = providers[providerName][mapName][mapType]
		options.subdomains = providers[providerName].Subdomains
		options.tk = options.tk || providers[providerName].tk
		options.token = options.token || providers[providerName].token
		options.crs = options.crs || providers[providerName].crs
		
		if ('tms' in providers[providerName]) {
			options.tms = providers[providerName]['tms']
		}
		
		L.TileLayer.prototype.initialize.call(this, url, options)
	},
})

L.TileLayer.BizLayer.layers = {
	TianDiTu: {
		Normal: {
			Map: '//t{s}.tianditu.gov.cn/DataServer?T=vec_w&X={x}&Y={y}&L={z}&tk={tk}',
			Annotion: '//t{s}.tianditu.gov.cn/DataServer?T=cva_w&X={x}&Y={y}&L={z}&tk={tk}',
		},
		Satellite: {
			Map: '//t{s}.tianditu.gov.cn/DataServer?T=img_w&tk={tk}&L={z}&X={x}&Y={y}',
			// Map:
			// '//t{s}.tianditu.gov.cn/DataServer?T=img_w&X={x}&Y={y}&L={z}&tk={tk}',  {x}&Y={y}
			// Map:
			// '//t{s}.tianditu.gov.cn/DataServer?T=img_w&X={y}&Y={x}&L={z}&tk={tk}',  {y}&Y={x}
			Annotion: '//t{s}.tianditu.gov.cn/DataServer?T=cia_w&X={x}&Y={y}&L={z}&tk={tk}',
		},
		Terrain: {
			Map: '//t{s}.tianditu.gov.cn/DataServer?T=ter_w&X={x}&Y={y}&L={z}&tk={tk}',
			Annotion: '//t{s}.tianditu.gov.cn/DataServer?T=cta_w&X={x}&Y={y}&L={z}&tk={tk}',
		},
		//crs: L.CRS.Earth,
		Subdomains: ['0', '1', '2', '3', '4', '5', '6', '7'],
		tk: 'eec8c7ee00d8d62dd60a274aa1a1beb5',
	},
	
	AMap: {
		Normal: {
			Map: '//webrd0{s}.is.autonavi.com/appmaptile?lang=zh_cn&size=1&scale=1&style=8&x={x}&y={y}&z={z}',
		},
		Satellite: {
			Map: '//webst0{s}.is.autonavi.com/appmaptile?style=6&x={x}&y={y}&z={z}',
			Annotion: '//webst0{s}.is.autonavi.com/appmaptile?style=8&x={x}&y={y}&z={z}',
		},
		Subdomains: ['1', '2', '3', '4'],
	},
	
	Google: {
		Normal: {
			Map: '//www.google.cn/maps/vt?lyrs=m@189&gl=cn&x={x}&y={y}&z={z}',
		},
		Satellite: {
			Map: '//www.google.cn/maps/vt?lyrs=s@189&gl=cn&x={x}&y={y}&z={z}',
			Annotion: '//www.google.cn/maps/vt?lyrs=y@189&gl=cn&x={x}&y={y}&z={z}',
		},
		Subdomains: [],
	},
	
	Geoq: {
		Normal: {
			Map: '//map.geoq.cn/ArcGIS/rest/services/ChinaOnlineCommunity/MapServer/tile/{z}/{y}/{x}',
			PurplishBlue: '//map.geoq.cn/ArcGIS/rest/services/ChinaOnlineStreetPurplishBlue/MapServer/tile/{z}/{y}/{x}',
			Gray: '//map.geoq.cn/ArcGIS/rest/services/ChinaOnlineStreetGray/MapServer/tile/{z}/{y}/{x}',
			Warm: '//map.geoq.cn/ArcGIS/rest/services/ChinaOnlineStreetWarm/MapServer/tile/{z}/{y}/{x}',
		},
		Theme: {
			Hydro: '//thematic.geoq.cn/arcgis/rest/services/ThematicMaps/WorldHydroMap/MapServer/tile/{z}/{y}/{x}',
		},
		Subdomains: [],
	},
	
	OSM: {
		Normal: {
			Map: '//{s}.tile.osm.org/{z}/{x}/{y}.png',
		},
		Subdomains: ['a', 'b', 'c'],
	},
	
	Baidu: {
		Normal: {
			Map: '//online{s}.map.bdimg.com/onlinelabel/?qt=tile&x={x}&y={y}&z={z}&styles=pl&scaler=1&p=1',
		},
		Satellite: {
			Map: '//shangetu{s}.map.bdimg.com/it/u=x={x};y={y};z={z};v=009;type=sate&fm=46',
			Annotion: '//online{s}.map.bdimg.com/tile/?qt=tile&x={x}&y={y}&z={z}&styles=sl&v=020',
		},
		Subdomains: '0123456789',
		tms: true,
	},
	
	Zhejiang: {
		
		// 浙江影像地图
		Satellite: {
			Map: `//ditu.zjzwfw.gov.cn/services/wmts/imgmap/default/oss?token={token}&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=imgmap&STYLE=default&TILEMATRIXSET=esritilematirx&TILEMATRIX={z}&TILEROW={x}&TILECOL={y}&FORMAT=image/jpgpng`,
			Annotion: `//ditu.zjzwfw.gov.cn/services/wmts/imgmap_lab/default/oss?token={token}&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=imgmap&STYLE=default&TILEMATRIXSET=esritilematirx&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&FORMAT=image/jpgpng`,
		},
		Subdomains: [],
		token: `89646f61-3b46-4e91-bdca-0e4b19bd5ca5`,
		crs: ZJEPSG4326,
	},
	
}

export const BizLayer = function (type, options) {
	return new L.TileLayer.BizLayer(type, options)
}

L.tileLayer.BizLayer = BizLayer

export default {
	BizLayer,
	ZJEPSG4326,
}