<template>
	<div ref="fbListRoll" @mouseenter="clearScroll" @mouseleave="initScroll" :class="getFatherClass">
		<div ref="fbListRollBody" :class="bodyClass" :style="bodyStyle">
			<slot></slot>
		</div>
	</div>
</template>

<script>

import { prefix } from '../../../../src/config'

/**
 * FbList
 * (c) 2020 lincong1987
 */
export default {
	name: 'FbListRoll',
	props: {
		// 数据
		list: {
			type: Array,
			default: () => [],
		},
		fatherClass: {
			type: String,
			default: '',
		},
		bodyClass: {
			type: String,
			default: '',
		},
		bodyStyle: {
			type: [Array, Object, String],
			default: '',
		},
		mode: {
			type: String,
			default: 'auto',
		},
		// 开启自滚动
		scroll: {
			type: Boolean,
			default: false,
		},
		speed: {
			type: Number,
			default: 60,
		},
		direction: {
			// horizontal or vertical
			type: String,
			default: 'vertical',
		}
	},
	watch: {
		list () { // 监听数据变化，初始化滚动
			clearInterval(this.timer)
			// 更新 dom 后触发滚动
			this.$nextTick(() => {
				this.$refs['fbListRoll'].scrollTop = 0
				this.initScroll()
			})
		},
	},
	data () {
		return {
			prefix,
		}
	},
	computed: {
		getFatherClass () {
			let arr = [this.fatherClass, `${prefix}-list-roll`, `${prefix}-list-roll-` + this.mode]

			return arr
		},
		getBodyClass () {
			let arr = [this.bodyClass, `${prefix}-list-roll-body`]

			return arr
		},
	},
	created () {
		this.timer = null
	},
	mounted () {
		// 更新 dom 后触发滚动
		this.$nextTick(() => {
			this.initScroll()
		})
	},

	beforeDestroy () {
		clearInterval(this.timer)
	},

	methods: {
		initScroll () {
			if (this.scroll) {
				let parent = this.$refs['fbListRoll']
				let son = this.$refs['fbListRollBody']
				if (this.direction === 'vertical') {
					let h = son.offsetHeight - parent.offsetHeight
					if (h < 0) return
					this.timer = setInterval(() => {
						parent.scrollTop += 1
						// 容错判断归 0，有 1px 偏差
						if (parent.scrollTop > (h - 1) || parent.scrollTop === h) {
							parent.scrollTop = 0
						}
					}, this.speed)
				} else if (this.direction === 'horizontal') {
					let w = son.offsetWidth - parent.offsetWidth
					if (w < 0) return
					this.timer = setInterval(() => {
						parent.scrollLeft += 1
						// 容错判断归 0，有 1px 偏差
						if (parent.scrollLeft > (w - 1) || parent.scrollLeft === w) {
							parent.scrollLeft = 0
						}
					}, this.speed)
				}

			}
		},
		clearScroll () {
			if (this.scroll) {
				clearInterval(this.timer)
			}
		},
	},

}
</script>

<style scoped>

</style>
