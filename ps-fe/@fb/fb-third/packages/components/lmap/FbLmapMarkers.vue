<template>
    <div class="fb-lmap-markers">
        <slot></slot>
    </div>
</template>

<script>
    /**
     * FbLmapMarkers.vue
     * */

    export default {
        name: 'FbLmapMarkers',
        props: {
            // 标记点数据
            data: {
                type: [Array],
                default: () => []
            },
            options: {
                type: [Object],
                default: () => {
                    return {}
                }
            }
        },
        data() {
            return {
            }
        },

        inject: ['lmap'],

        watch: {
            data: {
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
                this.Layer = this.lmap.addMarkers(this.data, Object.assign({
                    click: (e, marker, point, settings) => {
                        this.$emit('on-click', e, marker, point, settings)
                    }
                }, this.options))
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
