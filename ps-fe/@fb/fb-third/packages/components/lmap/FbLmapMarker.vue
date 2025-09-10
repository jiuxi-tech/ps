<template>
    <div class="fb-lmap-marker">
        <slot></slot>
    </div>
</template>

<script>
    /**
     * FbLmapMarker.vue
     * */

    export default {
        name: 'FbLmapMarker',
        props: {
            // 标记点数据
            point: {
                type: [Object],
                default: () => {
                    return {}
                }
            },
            options: {
                type: [Object],
                default: () => {
                    return {}
                }
            },
            popObj:{
              type:Object,
              default: () => {
                return {}
              }
            },
        },
        data() {
            return {
            }
        },

        inject: ['lmap'],

        watch: {
            point: {
                handler() {
                    this.addLayer()
                },
                deep: true
            }
        },

        mounted() {
			this.$on('lmap_ready', (map) => {
				this.init(map)
			})

        },

        beforeDestroy() {
			this.removeLayer()
			this.$off('lmap_ready')
        },

        methods: {
            init () {
				this.addLayer()
            },
            addLayer() {
                this.removeLayer()
                if (!this.point.lngLat) {
                    return
                }
                this.Layer = this.lmap.addMarker(this.point, Object.assign({
                    click: (e, marker, point, settings) => {
                        this.$emit('on-click', e, marker, point, settings)
                    },
                    bindCustomPopup: {
                      // content: this.popObj,
                      options: {
                        minWidth: 262, // 弹窗最小宽防止偏移
                      },
                    },
                }, this.options))
				console.log(this.layer, this.lmap.map)
            },
            removeLayer() {
                if (this.Layer) {
                    this.Layer.remove()
                    this.Layer = null
                }
            },
			getLayer() {
            	return this.Layer
			}
        },
    }
</script>
