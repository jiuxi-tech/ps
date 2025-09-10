/*global importScripts Supercluster */

importScripts('./supercluster.js');

const now = Date.now();

let index;


function getRandom(min, max) {
    return min + Math.random() * (max - min);
}
// // 测试 百万点 4s
// var markersLength = 100 * 10000;
// var arr = []
// for (var i = 0; i < markersLength; i++) {
//     var coords = [getRandom(120.19, 122), getRandom(29.2, 30.26)];
//     var obj = {
//         "type": "Feature",
//         "properties": {
//             "lat_y": 43.087653,
//             "long_x": -79.044073,
//         },
//         "geometry": {
//             "type": "Point",
//             "coordinates": [
//                 -79.04411780507252,
//                 43.08771393436908
//             ]
//         }
//     }
//     obj.properties.lat_y = coords[0]
//     obj.properties.long_x = coords[1]
//     obj.geometry.coordinates = coords
//     arr.push(obj)
// }
// var newArr = arr
// index = new Supercluster({
//     log: true,
//     radius: 60,
//     extent: 256,
//     maxZoom: 18
// }).load(newArr);
// console.log(index.getTile(0, 0, 0));
// postMessage({ready: true});

self.addEventListener("message", (e) => {
    // console.log(e, 2222222222222)
    if (e.data.getClusterExpansionZoom) {
        postMessage({
            expansionZoom: index.getClusterExpansionZoom(e.data.getClusterExpansionZoom),
            center: e.data.center
        });
    } else if (e.data.bbox && e.data.zoom) {
        if (index.getClusters) {
            postMessage(index.getClusters(e.data.bbox, e.data.zoom));
        }
    } else if (e.data.data) {
        var arr = []
        for (var i = 0; i < e.data.data.length; i++) {
            var item = e.data.data[i]
            // console.log(item)
            // var coords = [getRandom(120.19, 122), getRandom(29.2, 30.26)];
            var coords = [item.longitude, item.latitude];
            var obj = {
                "type": "Feature",
                "properties": {},
                "geometry": {
                    "type": "Point",
                    "coordinates": []
                }
            }
            obj.properties = item
            obj.geometry.coordinates = coords
            arr.push(obj)
        }
        index = new Supercluster({
            log: true,
            radius: 60,
            extent: 256,
            maxZoom: 18
        }).load(arr);
        console.log(index.getTile(0, 0, 0));
        postMessage({ready: true});
    }
})

// export default self
