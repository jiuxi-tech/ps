<template>
    <!--  地图瓦片类型切换  -->
    <div class="leaflet-map-type-control" @mouseover="layerOver" @mouseout="layerOut">

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
        <div class="leaflet-map-type-cell">
            <div class="leaflet-map-type-cell-layer active">
                <img class="leaflet-map-type-cell-img" :src="layerList[layerActive].imgSrc" alt="">
                <span class="leaflet-map-type-cell-text">{{layerList[layerActive].name}}</span>
            </div>
        </div>
    </div>
</template>

<script>
//    import LeafletMap from '../mixins/LeafletMap.js'
    export default {
//        mixins: [LeafletMap],
        data() {
            return {
                layerList: {
//                    tdt_img_c: {name: '卫星', imgSrc: require('../../assets/img/global/map/map-type-weixing.png')},
//                    tdt_vec_c: {name: '路网', imgSrc: require('../../assets/img/global/map/map-type-luwang.png')},
                },
                layerActive: 'tdt_img_c',
                showlayerList: false
            }
        },
        methods: {
            layerOver() {
                this.showlayerList = true
            },
            layerOut() {
                this.showlayerList = false
            },
            changeLayer(layer, key) {

                let map = this.map = this.$parent.map || window.$map
                if (!map) {
                    this.$message.error('无地图实例')
                    return false
                }
                map.eachLayer(layer => {
                    let attr = layer.getAttribution() || {}
                    if (key == 'tdt_img_c') {
                        if (attr.layerName == 'tdt_vec_c' || attr.layerName == 'tdt_cva_c') {
                            layer.remove()
                        }
                    }

                    if (key == 'tdt_vec_c') {
                        if (attr.layerName == 'tdt_img_c' || attr.layerName == 'zj_imgmap' || attr.layerName == 'zj_imgmap_lab') {
                            layer.remove()
                        }
                    }
                })
                this.changeLayerAfterAdd(key, map)
                this.layerActive = key
            },
            changeLayerAfterAdd(key, map) {
                switch(key) {
                    case 'tdt_img_c':
                        this.addLayer({name: 'tdt_img_c'})
                        this.addLayer({name: 'zj_imgmap'})
                        this.addLayer({name: 'zj_imgmap_lab'})
                        break;
                    case 'tdt_vec_c':
                        this.addLayer({name: 'tdt_vec_c'})
                        this.addLayer({name: 'tdt_cva_c'})
                        break;
                }
            }
        }
    }
</script>

<style scoped lang="less">

</style>
