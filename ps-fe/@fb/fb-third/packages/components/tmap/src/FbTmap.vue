<template>
	<div class="tmap" :id="mapId">
		<slot></slot>
	</div>
</template>

<script>
import { generateId } from '../../../utils/utils'

export default {
	name: 'FbTmap',
	// 接收父组件的传参
	props: {
		mapOptions: {
			type: Object,
			default: () => {
				return {}
			},
		},
		centerAndZoom: {
			type: Array,
			default: () => [],
		},
		minMaxZoom: {
			type: Array,
			default: () => [],
		},
	},
	components: {},
	data () {
		return {
			map: '',
			mapId: 'T_MAP' + generateId(), // 防止多个地图在一张页面
		}
	},
	// 初始化方法
	mounted () {
		this.initMap()
	},

	beforeDestroy () {
		this.map
	},

	// 方法
	methods: {
		initMap () {
			if (!window.T) {
				// alert('FbTMap：请加载天地图相关文件 https://www.tianditu.gov.cn/')
				this.$message.error('FbTMap：请加载天地图相关文件 https://www.tianditu.gov.cn/')
				return
			}

			const T = window.T
			const map = this.map = new T.Map(this.mapId, this.mapOptions)

			const centerAndZoom = this.centerAndZoom
			if (centerAndZoom.length > 0) {
				map.centerAndZoom(new T.LngLat(centerAndZoom[0] || 116.40769, centerAndZoom[1] || 39.89945),
					centerAndZoom[2] || 12)
			}

			if (this.minMaxZoom.length > 0) {
				if (this.minMaxZoom[0]) {
					map.setMinZoom(this.minMaxZoom[0])
				}
				if (this.minMaxZoom[1]) {
					map.setMinZoom(this.minMaxZoom[1])
				}
			}

			// 回返 map 实例
			this.$emit('init-map', map)

			this.injectEvents()
		},
		injectEvents () {
			// 注册 map 实例事件
			Object.keys(this.$listeners).forEach(event => {
				const handler = this.$listeners[event]
				if (event.indexOf('init-map') >= 0) {
					return
				} else {
					this.map.removeEventListener(event, handler)
					this.map.addEventListener(event, handler)
				}
			})
		},
		logMe () {
		},
	},
}
</script>

<style lang="less" scoped>
.tmap {
	width:  100%;
	height: 100%;
}
</style>
