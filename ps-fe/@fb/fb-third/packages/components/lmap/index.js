import FbLmap from './FbLmap.vue'
import FbLmapGeoJson from './FbLmapGeoJson.vue'
import FbLmapMarker from './FbLmapMarker.vue'
import FbLmapMarkerCluster from './FbLmapMarkerCluster.vue'
import FbLmapMarkers from './FbLmapMarkers.vue'
import FbLmapPolygon from './FbLmapPolygon.vue'
import FbLmapPolyline from './FbLmapPolyline.vue'
import FbLmapSwitchTiles from './FbLmapSwitchTiles.vue'

const components = [
	FbLmap,
	FbLmapGeoJson,
	FbLmapMarker,
	FbLmapMarkerCluster,
	FbLmapMarkers,
	FbLmapPolygon,
	FbLmapPolyline,
	FbLmapSwitchTiles,
]

export const install = function (Vue, options) {
	components.map(component => {
		Vue.use(component.name, component)
	})
}

export default {
	install,
	components
}


