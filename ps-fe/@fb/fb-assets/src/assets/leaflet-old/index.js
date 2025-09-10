/*!
 * index
 * (c) 2022 lincong1987
 */

module.exports = {
	name: 'leaflet-old',
	title: '地图资源包',
	version: '0.0.1',
	path: 'assets/leaflet-old',
	head: {
		css: [
			'assets/leaflet/leaflet.css',
			'assets/leaflet/plugins/css/MarkerCluster.css',
			'assets/leaflet/plugins/css/MarkerCluster.Default.css',
			'assets/leaflet/plugins/css/prunecluster.css'
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
