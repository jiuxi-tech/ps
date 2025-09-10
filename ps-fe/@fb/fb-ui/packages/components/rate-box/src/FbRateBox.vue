<template>
	<div :class="[
		`${prefix}-rate-box clearfix`,
		rateNumText ? `${prefix}-rate-box_nums` : ''
	]" @click="onClick">
		<slot name="left">
			<div :class="`${prefix}-rate-box-left`"
				 :style="rateLeftStyle" @click="onRateClick">
				<div v-if="rateText">{{rateText}}</div>
				<div v-if="rateNumText">{{rateNumText}}</div>
			</div>
		</slot>
		<slot name="right">
			<div :class="`${prefix}-rate-box-right`">
				<div @click="onTitleClick" :class="{ellipsis: ellipsis}" :title="`${ellipsis ? title : ''}`">
					<slot name="title">
						{{title}}
					</slot>
				</div>
				<div @click="onSubtitleClick" :class="{ellipsis: ellipsis}" :title="`${ellipsis ? subTitle : ''}`">
					<slot name="subTitle">
						{{subTitle}}
					</slot>
				</div>
			</div>
		</slot>
	</div>
</template>

<script>
	import {prefix} from '../../../../src/config'

	/**
	 * FbRateBox 业务组件 标识box
	 * (c) 2020 lincong1987
	 */
	export default {
		name: 'FbRateBox',
		props: {
			rateText: {
				type: [String],
				default: ''
			},
			rateNumText: {
				type: [String],
				default: ''
			},
			rateBg: {
				type: [String],
				default: '#FC9C3B'
			},
			title: {
				type: [String],
				default: ''
			},
			subTitle: {
				type: [String],
				default: ''
			},
			ellipsis: {
				type: [Boolean],
				default: false
			}
		},

		data() {
			return {
				prefix,
			}
		},
		computed: {
			rateLeftStyle () {
				let style = {}
				if (this.rateBg) {
					style['background'] = this.rateBg
				}
				return style
			}
		},

		watch: {},

		methods: {
			onClick(e) {
				this.$emit('on-click', e)
			},
			onRateClick(e) {
				this.$emit('on-rate-click', e)
			},
			onTitleClick(e) {
				this.$emit('on-title-click', e)
			},
			onSubtitleClick(e) {
				this.$emit('on-subtitle-click', e)
			}
		},

		mounted() {

		},

		destroyed() {

		}
	}
</script>

<style scoped>

</style>
