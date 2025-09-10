<template>
    <div class="fb-lmap-polyline">
        <slot></slot>
    </div>
</template>

<script>
    /**
     * FbLmapPolyline 基础封装 leaflet
     * */
    export default {
        name: 'FbLmapPolyline',
		inject: ['lmap'],
        props: {
        	data: {
        		type: [Array],
				default: () => []
			},
			options: {
				type: [Object],
				default: () => {
					return {}
				}
			},
        },
        data() {
            return {
				Layer: null
            }
        },

        watch: {
        	data: {
        		handler() {
        			this.addLayer()
				},
				deep: true,
				immediate: true
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
        	init() {
				this.addLayer()
			},
			addLayer() {
				this.removeLayer()
        		if (this.data.length > 0) {
					this.Layer = this.lmap.addPolyline(this.data, this.options)
				}
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
