<template>
    <div class="fb-lmap-geo-json">
        <slot></slot>
    </div>
</template>

<script>
    /**
     * FbLmapGeoJson.vue
     * */

    import axios from 'axios'
    export default {
        name: 'FbLmapGeoJson',
        props: {
            // 行政区划编码 --- 自动触发请求到 public/mapjson
            code: {
                type: String,
                default: ''
            },
            // 行政区geoJson --- 和 code 不通用 code 优先
            geoJson: {
                type: [Object, Array],
                default: () => {
                    return {}
                }
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
            code() {
                this.init()
            },
			geoJson: {
            	handler() {
					this.init()
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
			this.removeGeoLayer()
			this.$off('lmap_ready')
		},

        methods: {
            init () {
                this.removeGeoLayer()
                if (this.code) {
                    this.addCodeJson()
                } else {
                    this.addJson()
                }
            },
            addCodeJson() {
                axios.get(`mapjson/${this.code}.json`).then((res) => {
                    if (res.data) {
                        this.geoLayer = this.lmap.addGeoJson(res.data, this.options)
                        this.geoLayer.on('click', (e) => {
                            this.$emit('on-click', e)
                        })
                    }
                })
            },
            addJson() {
				if (JSON.stringify(this.geoJson) !== "{}") {
					this.geoLayer = this.lmap.addGeoJson(this.geoJson, this.options)
                    this.geoLayer.on('click', (e) => {
                        this.$emit('on-click', e)
                    })
				}
            },
            removeGeoLayer() {
                if (this.geoLayer) {
                    this.geoLayer.remove()
                    this.geoLayer = null
                }
            },
			getLayer() {
				return this.geoLayer
			}
        },
    }
</script>
