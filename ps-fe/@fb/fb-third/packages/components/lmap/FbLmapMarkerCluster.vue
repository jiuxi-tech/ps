<template>
	<div class="fb-lmap-marker-cluster">
		<slot></slot>
	</div>
</template>

<script>
	/**
	 * FbLmapMarkerCluster.vue
	 * */
	import {cloneDeep} from 'lodash-es'

	export default {
		name: 'FbLmapMarkerCluster',
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
			},
			popObj: {
				type: Object,
				default: () => {
					return {}
				}
			},
		},
		data() {
			return {}
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
			init() {
				this.addLayer()
			},
			addLayer() {
				this.removeLayer()
				let data = cloneDeep(this.data)
				this.Layer = this.lmap.addMarkersPruneCluster(data, Object.assign({
					noAddTo: true,
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
