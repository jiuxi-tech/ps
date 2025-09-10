<template>
	<!--  地图瓦片类型切换  -->
	<div class="leaflet-map-type-control-wrapper">
		<div class="leaflet-map-type-control" :class="[`leaflet-map-type-control_hover_${hoverPosition}`]" @mouseover="layerOver" @mouseout="layerOut">
			<!--    选中项    -->
			<div class="leaflet-map-type-cell" v-if="hoverPosition === 'left'">
				<div class="leaflet-map-type-cell-layer active">
					<img class="leaflet-map-type-cell-img" :src="layerList[layerActive].imgSrc" alt="">
					<span class="leaflet-map-type-cell-text">{{layerList[layerActive].name}}</span>
				</div>
			</div>

			<!--    展示项    -->
			<transition name="fade-in" mode="out-in">
				<div v-show="showlayerList" class="leaflet-map-type-cell-list">
					<div class="leaflet-map-type-cell" @click="changeLayer(layer, key)" v-for="(layer, key) in layerList" :key="key">
						<div class="leaflet-map-type-cell-layer">
							<img class="leaflet-map-type-cell-img" :src="layer.imgSrc"
								 alt="">
							<span class="leaflet-map-type-cell-text">{{layer.name}}</span>
						</div>

					</div>
				</div>
			</transition>

			<!--    选中项    -->
			<div class="leaflet-map-type-cell" v-if="hoverPosition === 'right'">
				<div class="leaflet-map-type-cell-layer active">
					<img class="leaflet-map-type-cell-img" :src="layerList[layerActive].imgSrc" alt="">
					<span class="leaflet-map-type-cell-text">{{layerList[layerActive].name}}</span>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	/**
	 * FbLmap 基础封装 leaflet
	 * FbLmapSwitchTiles.vue 切换瓦片
	 * */

	export default {
		name: 'FbLmapSwitchTiles',
		props: {
			hoverPosition: {
				type: String,
				default: 'right'
			},
			layerData: {
				type: Object,
				default: null
			},
			lmap: {
				type: Object,
				default: null
			}
		},
		data() {
			return {
				layerList: this.layerData || {
					tdt_img_c: {name: '卫星', imgSrc: 'assets/leaflet/img/map-type-weixing.png'},
					tdt_vec_c: {name: '路网', imgSrc: 'assets/leaflet/img/map-type-luwang.png'},
				},
				layerActive: 'tdt_img_c',
				showlayerList: false
			}
		},

		watch: {
		},

		mounted() {
		},

		beforeDestroy() {

		},

		methods: {
			layerOver() {
				this.showlayerList = true
			},
			layerOut() {
				this.showlayerList = false
			},
			changeLayer(layer, key) {

				if (key === this.layerActive) {
					return
				}

				let map = this.map =  window.$map
				if (this.lmap) {
					map = this.lmap.map
				}
				if (!map) {
					this.$message.error('无地图实例')
					return false
				}
				map.eachLayer(layer => {
					let attr = layer.getAttribution() || {}
					if (attr.layerName == 'tdt_vec_c' ||
						attr.layerName == 'tdt_cva_c' ||
						attr.layerName == 'tdt_img_c' ||
						attr.layerName == 'zj_imgmap' ||
						attr.layerName == 'zj_imgmap_lab' ||
						attr.layerName == 'TianDiTu.Satellite.Map' ||
						attr.layerName == 'TianDiTu.Satellite.Annotion' ||
						attr.layerName == 'TianDiTu.Normal.Map' ||
						attr.layerName == 'TianDiTu.Normal.Map')
					{
						layer.remove()
					}
				})
				this.changeLayerAfterAdd(key, map)
				this.layerActive = key
			},
			changeLayerAfterAdd(key, map) {
				let lmap = this.lmap
				if (lmap) {
					switch(key) {
						case 'tdt_img_c':
							lmap.addChinaLayer('TianDiTu.Satellite.Map')
							lmap.addChinaLayer('TianDiTu.Satellite.Annotion')
							break;
						case 'tdt_vec_c':
							lmap.addChinaLayer('TianDiTu.Normal.Map')
							lmap.addChinaLayer('TianDiTu.Normal.Annotion')
							break;
					}
				}

				this.$emit('on-layer-change', key, map)

			}
		}
	}
</script>
