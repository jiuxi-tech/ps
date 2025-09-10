<template>
	<div :class="getBadgeClass" :style="getBadgeStyle" @click="$emit('on-click')">

		<slot></slot>

		<transition :name="`${prefix}-badge-zoom`">
			<slot name="count">
				<sup :class="getCountClass" v-if="!dot && showCount" key="count" :title="title" :style="getCountStyle">
					{{badgeText}}
				</sup>
				<sup :class="getDotClass" :style="getDotStyle" v-else-if="dot" key="dot" :title="title"></sup>
			</slot>
		</transition>

	</div>
</template>

<script>

	import { prefix } from '../../../../src/config'
	import { isNumber, cloneDeep } from 'lodash-es'
	import { addClass } from '../../../utils/utils'
	import FbNumber from '../../number/src/FbNumber'
	/**
	 * FbAlert
	 *
	 * (c) 2020 lincong1987
	 */
	export default {
		name: 'FbBadge',
		components: {FbNumber},
		props: {
			// 自定义小圆点的颜色
			dotColor: {
				type: String,
				default: ''
			},
			// 展示的数字
			count: {
				type: [Number, String],
				default: ''
			},
			// 展示的数字 型号
			countSize: {
				type: [String],
				default: ''
			},
			// 展示的小圆点
			dot: {
				type: [Boolean],
				default: false
			},
			// 小圆点 大小
			dotSize: {
				type: [Number, String],
				default: '6'
			},
			vertical: {
				type: [String],
				default: ''
			},
			// 设置状态点的位置偏移，格式为 [x, y]
			offset: {
				type: [Array],
				default: () => [0, 0]
			},
			// 展示封顶的数字值
			overflowCount: {
				type: [Number, String],
				default: 99
			},
			// 当数值为 0 时，是否展示 Badge
			showZero: {
				type: [Boolean],
				default: false
			},
			// 设置 Badge 为状态点
			type: {
				type: [String],
				default: ''
			},
			// 设置状态点的样式
			numberStyle: {
				type: [Object],
				default: () => {
					return {}
				}
			},
			// 设置鼠标放在状态点上时显示的文字
			title: {
				type: [String],
				default: ''
			}
		},

		data () {
			return {
				prefix,
				badgeText: this.count,
				showCount: true,
			}
		},
		computed: {
			getBadgeClass () {
				let arr = [`${prefix}-badge`]

				if (!this.$slots.default) {
					arr.push(`${prefix}-badge-not-a-wrapper`)
				}

				return arr
			},
			getBadgeStyle () {
				let obj = {}
				if (!this.$slots.default && this.vertical) {
					obj.verticalAlign = this.vertical
				}
				return obj
			},
			getCountClass () {
				let arr = [`${prefix}-badge-count`]

				if (this.type.length > 0) {
					arr.push(`${prefix}-badge-status-${this.type}`)
				}

				if (this.countSize.length > 0) {
					arr.push(`${prefix}-badge-count-${this.countSize}`)
				}

				return arr
			},
			getCountStyle () {
				let obj = cloneDeep(this.numberStyle)

				return this.calcOffset(obj)
			},
			getDotClass () {
				let arr = [`${prefix}-badge-dot`]

				if (this.type.length > 0) {
					arr.push(`${prefix}-badge-status-${this.type}`)
				}

				return arr
			},
			getDotStyle () {
				let obj = {}
				if (this.dotColor.length > 0) {
					obj = {
						backgroundColor: this.dotColor
					}
				}

				if (this.dotSize) {
					obj.width = this.dotSize + 'px'
					obj.height = this.dotSize + 'px'
				}

				return this.calcOffset(obj)
			},
		},
		watch: {
			count () {
				this.initBadgeText()
			},
			overflowCount () {
				this.initBadgeText()
			},
			showZero () {
				this.initBadgeText()
			}
		},
		mounted() {
			this.initBadgeText()
		},
		updated() {
			if (this.$slots.count) {
				addClass(this.$children[1].$el, `${prefix}-custom-component`)
			}
		},
		methods: {
			initBadgeText () {
				if (isNumber(this.count)) {
					let absCount = Math.abs(this.count)
					let absOvrCount = Math.abs(this.overflowCount)
					if (!absOvrCount) {
						absOvrCount = 99
					}

					if (absCount > absOvrCount) {
						this.badgeText = absOvrCount + "+"
					} else {
						this.badgeText = absCount
					}

					console.info("....")

					if ((absCount === 0 && !this.showZero) || absCount < 0) {
						this.showCount = false
					} else if (!this.showCount) {
						this.showCount = true
					}
				} else {
					this.badgeText = this.count
					if (!this.count) {
						this.showCount = false
					}
				}
			},
			calcOffset (obj) {
				if (this.offset.length > 0) {
					if (this.offset[0]) {
						obj.right = this.offset[0] + 'px'
					}
					if (this.offset[1]) {
						obj.top = this.offset[1] + 'px'
					}
				}
				return obj
			}
		}
	}
</script>

<style scoped>

</style>
