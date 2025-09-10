// this L.CRS.Baidu from https://github.com/muyao1987/leaflet-tileLayer-baidugaode/blob/master/src/tileLayer.baidu.js

if (L.Proj) {
    L.CRS.Baidu = new L.Proj.CRS('EPSG:900913', '+proj=merc +a=6378206 +b=6356584.314245179 +lat_ts=0.0 +lon_0=0.0 +x_0=0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +wktext  +no_defs', {
        resolutions: function () {
            level = 19
            var res = [];
            res[0] = Math.pow(2, 18);
            for (var i = 1; i < level; i++) {
                res[i] = Math.pow(2, (18 - i))
            }
            return res;
        }(),
        origin: [0, 0],
        bounds: L.bounds([20037508.342789244, 0], [0, 20037508.342789244])
    });
}
// EPSG3857
L.TileLayer.ChinaProvider = L.TileLayer.extend({

    initialize: function(type, options) { // (type, Object)
        var providers = L.TileLayer.ChinaProvider.providers;

        options = options || {}

        var parts = type.split('.');

        var providerName = parts[0];
        var mapName = parts[1];
        var mapType = parts[2];

        var url = providers[providerName][mapName][mapType];
        options.subdomains = providers[providerName].Subdomains;
        options.key = options.key || providers[providerName].key;

        if ('tms' in providers[providerName]) {
            options.tms = providers[providerName]['tms']
        }

        L.TileLayer.prototype.initialize.call(this, url, options);
    }
});

L.TileLayer.ChinaProvider.providers = {
    TianDiTu: {
        // Normal: {
        //     Map: "//t{s}.tianditu.com/DataServer?T=vec_w&X={x}&Y={y}&L={z}&tk={key}",
        //     Annotion: "//t{s}.tianditu.com/DataServer?T=cva_w&X={x}&Y={y}&L={z}&tk={key}"
        // },

        Normal: {
            Map: "//t{s}.tianditu.gov.cn/vec_w/wmts?layer=vec&style=default&tilematrixset=w&Service=WMTS&Request=GetTile&Version=1.0.0&Format=tiles&TileMatrix={z}&TileCol={x}&TileRow={y}&tk={key}",
            Annotion: "//t{s}.tianditu.gov.cn/cva_w/wmts?layer=cva&style=default&tilematrixset=w&Service=WMTS&Request=GetTile&Version=1.0.0&Format=tiles&TileMatrix={z}&TileCol={x}&TileRow={y}&tk={key}"
        },
        Satellite: {
            Map: "//t{s}.tianditu.gov.cn/img_w/wmts?layer=img&style=default&tilematrixset=w&Service=WMTS&Request=GetTile&Version=1.0.0&Format=tiles&TileMatrix={z}&TileCol={x}&TileRow={y}&tk={key}",
            Annotion: "//t{s}.tianditu.gov.cn/cia_w/wmts?layer=cia&style=default&tilematrixset=w&Service=WMTS&Request=GetTile&Version=1.0.0&Format=tiles&TileMatrix={z}&TileCol={x}&TileRow={y}&tk={key}"
        },
        Terrain: {
            Map: "//t{s}.tianditu.com/DataServer?T=ter_w&X={x}&Y={y}&L={z}&tk={key}",
            Annotion: "//t{s}.tianditu.com/DataServer?T=cta_w&X={x}&Y={y}&L={z}&tk={key}"
        },
        Subdomains: ['0', '1', '2', '3', '4', '5', '6', '7'],
        key: "c81b22097c7c0a051f29f8b647b4e3be"
    },

    GaoDe: {
        Normal: {
            Map: '//webrd0{s}.is.autonavi.com/appmaptile?lang=zh_cn&size=1&scale=1&style=8&x={x}&y={y}&z={z}'
        },
        Satellite: {
            Map: '//webst0{s}.is.autonavi.com/appmaptile?style=6&x={x}&y={y}&z={z}',
            Annotion: '//webst0{s}.is.autonavi.com/appmaptile?style=8&x={x}&y={y}&z={z}'
        },
        Subdomains: ["1", "2", "3", "4"]
    },

    Google: {
        Normal: {
            Map: "//www.google.cn/maps/vt?lyrs=m@189&gl=cn&x={x}&y={y}&z={z}"
        },
        Satellite: {
            Map: "//www.google.cn/maps/vt?lyrs=s@189&gl=cn&x={x}&y={y}&z={z}",
            Annotion: "//www.google.cn/maps/vt?lyrs=y@189&gl=cn&x={x}&y={y}&z={z}"
        },
        Subdomains: []
    },

    Geoq: {
        Normal: {
            Map: "//map.geoq.cn/ArcGIS/rest/services/ChinaOnlineCommunity/MapServer/tile/{z}/{y}/{x}",
            PurplishBlue: "//map.geoq.cn/ArcGIS/rest/services/ChinaOnlineStreetPurplishBlue/MapServer/tile/{z}/{y}/{x}",
            Gray: "//map.geoq.cn/ArcGIS/rest/services/ChinaOnlineStreetGray/MapServer/tile/{z}/{y}/{x}",
            Warm: "//map.geoq.cn/ArcGIS/rest/services/ChinaOnlineStreetWarm/MapServer/tile/{z}/{y}/{x}",
        },
        Theme: {
            Hydro: "//thematic.geoq.cn/arcgis/rest/services/ThematicMaps/WorldHydroMap/MapServer/tile/{z}/{y}/{x}"
        },
        Subdomains: []
    },

    Tencent: {
        Normal: {
            Map: "//rt{s}.map.gtimg.com/realtimerender?z={z}&x={x}&y={-y}&type=vector&style=0",
        },
        Satellite: {
            Map: '//p{s}.map.gtimg.com/sateTiles/{z}/11/22/{x}_{y}.jpg',
            Annotion: '//rt{s}.map.gtimg.com/tile?z={z}&x={x}&y={y}&styleid=2&version=376',
        },
        Subdomains: ['0', '1', '2'],
        // tms: true
    },

    OSM: {
        Normal: {
            Map: "//{s}.tile.osm.org/{z}/{x}/{y}.png",
        },
        Subdomains: ['a', 'b', 'c']
    },

    Baidu: {
        Normal: { // 电子
            Map: '//online{s}.map.bdimg.com/onlinelabel/?qt=tile&x={x}&y={y}&z={z}&styles=pl&scaler=1&p=1'
        },
        Satellite: { // 卫星
            Map: '//shangetu{s}.map.bdimg.com/it/u=x={x};y={y};z={z};v=009;type=sate&fm=46',
            Annotion: '//online{s}.map.bdimg.com/tile/?qt=tile&x={x}&y={y}&z={z}&styles=sl&v=020'
        },
        NormalScale2: { // 电子大字体
            Map: '//maponline{s}.bdimg.com/tile/?qt=vtile&x={x}&y={y}&z={z}&styles=ph&scaler=2&udt=&from=jsapi2_0',
        },
        Theme: {
            // 草绿
            Grassgreen: '//api{s}.map.bdimg.com/customimage/tile?&x={x}&y={y}&z={z}&scale=1&customid=grassgreen',
            // 深蓝
            Midnight: '//api{s}.map.bdimg.com/customimage/tile?&x={x}&y={y}&z={z}&scale=1&customid=midnight',
            // 浅蓝
            Bluish: '//api{s}.map.bdimg.com/customimage/tile?&x={x}&y={y}&z={z}&scale=1&customid=bluish',
            // 淡蓝
            Light: '//api{s}.map.bdimg.com/customimage/tile?&x={x}&y={y}&z={z}&scale=1&customid=light',
            // 黑色
            Dark: '//api{s}.map.bdimg.com/customimage/tile?&x={x}&y={y}&z={z}&scale=1&customid=dark',

            // 灰色
            Gray: '//api{s}.map.bdimg.com/customimage/tile?&x={x}&y={y}&z={z}&scale=1&customid=grayscale',
            // 红色
            Red: '//api{s}.map.bdimg.com/customimage/tile?&x={x}&y={y}&z={z}&scale=1&customid=redalert',
            // 粉色
            Pink: '//api{s}.map.bdimg.com/customimage/tile?&x={x}&y={y}&z={z}&scale=1&customid=pink',
            // 墨绿色D
            Darkgreen: '//api{s}.map.bdimg.com/customimage/tile?&x={x}&y={y}&z={z}&scale=1&customid=darkgreen',
            // 简约
            Hardedge: '//api{s}.map.bdimg.com/customimage/tile?&x={x}&y={y}&z={z}&scale=1&customid=hardedge',
            // 谷歌
            Google: '//api{s}.map.bdimg.com/customimage/tile?&x={x}&y={y}&z={z}&scale=1&customid=googlelite',
        },
        // Subdomains: '0123456789',
        Subdomains: '012',
        tms: true
    }

};

// EPSG4326 经纬度投影
L.TileLayer.ChinaProviderLngLat = L.TileLayer.extend({

    initialize: function(type, options) { // (type, Object)
        var providers = L.TileLayer.ChinaProviderLngLat.providers;

        options = options || {};
        options = Object.assign({
            tileSize: 256,
            zoomOffset: 1,
        }, options);

        var parts = type.split('.');
        var providerName = parts[0];
        var mapName = parts[1];
        var mapType = parts[2];

        var url = providers[providerName][mapName][mapType];
        options.subdomains = providers[providerName].Subdomains;
        options.key = options.key || providers[providerName].key;

        if ('tms' in providers[providerName]) {
            options.tms = providers[providerName]['tms'];
        }
        if (!options.bounds && providers[providerName].bounds) {
            options.bounds = providers[providerName].bounds;
        }

        L.TileLayer.prototype.initialize.call(this, url, options);
    }
});

L.TileLayer.ChinaProviderLngLat.providers = {
    TianDiTu: {
        Normal: {
            Map: "//t{s}.tianditu.gov.cn/vec_c/wmts?layer=vec&style=default&tilematrixset=c&Service=WMTS&Request=GetTile&Version=1.0.0&Format=tiles&TileMatrix={z}&TileCol={x}&TileRow={y}&tk={key}",
            Annotion: "//t{s}.tianditu.gov.cn/cva_c/wmts?layer=cva&style=default&tilematrixset=c&Service=WMTS&Request=GetTile&Version=1.0.0&Format=tiles&TileMatrix={z}&TileCol={x}&TileRow={y}&tk={key}"
        },
        Satellite: {
            Map: "//t{s}.tianditu.gov.cn/img_c/wmts?layer=img&style=default&tilematrixset=c&Service=WMTS&Request=GetTile&Version=1.0.0&Format=tiles&TileMatrix={z}&TileCol={x}&TileRow={y}&tk={key}",
            Annotion: "//t{s}.tianditu.gov.cn/cia_c/wmts?layer=cia&style=default&tilematrixset=c&Service=WMTS&Request=GetTile&Version=1.0.0&Format=tiles&TileMatrix={z}&TileCol={x}&TileRow={y}&tk={key}"
        },
        Terrain: {
            Map: "//t{s}.tianditu.gov.cn/ter_c/wmts?layer=ter&style=default&tilematrixset=c&Service=WMTS&Request=GetTile&Version=1.0.0&Format=tiles&TileMatrix={z}&TileCol={x}&TileRow={y}&tk={key}",
            Annotion: "//t{s}.tianditu.gov.cn/cta_c/wmts?layer=cta&style=default&tilematrixset=c&Service=WMTS&Request=GetTile&Version=1.0.0&Format=tiles&TileMatrix={z}&TileCol={x}&TileRow={y}&tk={key}"
        },
        Subdomains: ['0', '1', '2', '3', '4', '5', '6', '7'],
        key: "c81b22097c7c0a051f29f8b647b4e3be"
    },
    ZheJiangTDT: {
        Satellite: {
            Map: "//ditu.zjzwfw.gov.cn/services/wmts/imgmap/default/oss?layer=img&style=default&tilematrixset=w&Service=WMTS&Request=GetTile&Version=1.0.0&Format=tiles&TileMatrix={z}&TileCol={x}&TileRow={y}&token={key}",
            Annotion: "//ditu.zjzwfw.gov.cn/services/wmts/imgmap_lab/default/oss?layer=img&style=default&tilematrixset=w&Service=WMTS&Request=GetTile&Version=1.0.0&Format=tiles&TileMatrix={z}&TileCol={x}&TileRow={y}&token={key}"
        },
        Zjv: { // 科技样式矢量地图
            Map: "//ditu.zjzwfw.gov.cn/mapserver/vmap/zjvmap/getMAP?x={x}&y={y}&l={z}&styleId=tdt_kejiganyangshi_2017&ratio=1&titlesize=256&token={key}"
        },
        Zjxzqh_py: { // 县行政区划面
            Map: "//ditu.zjzwfw.gov.cn/mapserver/vmap/zjxzqh_py/getMAP?x={x}&y={y}&l={z}&styleId=zjcoupy&ratio=1&titlesize=256&token={key}"
        },
        Zjxzqh_py202104: { // 省各级行政区划面含海域_2021Q2
            Map: "https://ditu.zjzwfw.gov.cn:443/mapserver/data/zjxzqh_py202104/getData?x={x}&y={y}&l={z}&styleId=zjboupy202104&token={key}"
        },
        Subdomains: [],
        bounds: [L.latLng(32, 118), L.latLng(27, 123)],
        key: "8b56a4a0-95c3-4e68-83fe-52286ec561a9"
    },
};


// 墨卡托投影
L.tileLayer.chinaProvider = function(type, options) {
    return new L.TileLayer.ChinaProvider(type, options);
};
// 经纬度投影
L.tileLayer.chinaProviderLngLat = function(type, options) {
    return new L.TileLayer.ChinaProviderLngLat(type, options);
};
