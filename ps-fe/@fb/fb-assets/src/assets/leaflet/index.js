/*!
 * index
 * (c) 2022 lincong1987
 */

module.exports = {
	name: 'leaflet',
	title: '地图资源包',
	version: '0.0.1',
	path: 'assets/leaflet',
	head: {
		css: [
			'assets/leaflet/css/leaflet.css',
			'assets/leaflet/css/MarkerCluster.css',
			'assets/leaflet/css/MarkerCluster.Default.css',
			'assets/leaflet/css/prunecluster.css',
			'assets/leaflet/css/leafletCustom.css',
		],
		script: [
			'assets/leaflet/leaflet-src.js',
			'assets/leaflet/turf.min.js',
			'assets/leaflet/plugins/polylineDecorator.js',
			'assets/leaflet/plugins/AnimatedMarker.js',
			'assets/leaflet/plugins/leaflet.markercluster-src.js',
			'assets/leaflet/plugins/leaflet.ChineseTmsProviders.js',
			'assets/leaflet/plugins/PruneCluster.js',
			'assets/leaflet/plugins/leaflet.canvas-markers-src.js',
			'assets/leaflet/plugins/customPopup.js'
		]
	},
	body: '',
}
