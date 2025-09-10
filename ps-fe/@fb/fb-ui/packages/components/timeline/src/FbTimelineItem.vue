<template>
	<li :class="[`${prefix}-timeline-item`]" @click="handleClick">
		<div :class="[`${prefix}-timeline-item-tail`]" ></div>
		<div :class="getDotClass" :style="getDotStyle">
			<slot name="dot"></slot>
		</div>

		<div :class="[`${prefix}-timeline-item-content`]" ref="content">
			<slot></slot>

			<div :class="[`${prefix}-timeline-item-content_describe`]">
				<slot name="describe">{{ describe }}</slot>
			</div>
			<div :class="[`${prefix}-timeline-item-content_time`]">
				<slot name="time">{{ time }}</slot>
			</div>
		</div>

<!--		<div v-if="position === 'top'" :class="[`${prefix}-timeline-item-tail`]" ></div>-->
<!--		<div v-if="position === 'top'" :class="getDotClass" :style="getDotStyle">-->
<!--			<slot name="dot"></slot>-->
<!--		</div>-->
	</li>
</template>

<script>
	import FbIcon from '../../icon/src/FbIcon'
	import {prefix} from '../../../../src/config'
	import { closest } from '../../../utils/componentUtils'

	/**
	 * FbTimelineItem
	 * (c) 2020 lincong1987
	 */
	export default {
		name: 'FbTimelineItem',
		components: {FbIcon},
		props: {
			describe: {
				type: String,
				default: ''
			},
			time: {
				type: String,
				default: ''
			},
			// 指定圆圈颜色 blue, red, green，或自定义的色值
			dotColor: {
				type: String,
				default: 'blue'
			},
			// 自定义节点位置
			position: {
				type: String,
				default: ''
			}
		},
		data() {
			return {
				prefix,
			}
		},

		created() {
		},
		mounted() {
			this.$nextTick(() => {
				this.calcWidth()
			})
		},

		beforeDestroy() {
			this.fbTags = null
		},

		methods: {
			calcWidth() {
				const timeline = closest(this, 'FbTimeline') || this.$parent
				if (!timeline.horizontal) {
					return
				}
				const num = this.$refs['content'].offsetWidth + 30
				timeline.caleHorizontalWidth(num)
			},
			handleClick(e) {
				this.$emit('click', e)
				this.$emit('on-click', e)
			}
		},

		computed: {
			getDotClass() {
				const { prefix, dotColor, $slots } = this

				return [
					`${prefix}-timeline-item-head`,
					`${prefix}-timeline-item-head-${dotColor}`,
					{
						[`${prefix}-timeline-item-head-custom`]: $slots.dot
					}
				]
			},
			getDotStyle() {
				const { dotColor } = this
				return {
					borderColor: /blue|red|green|gray/.test(dotColor) ? undefined : dotColor
				}
			},
		},
	}
</script>

<style scoped>

</style>
