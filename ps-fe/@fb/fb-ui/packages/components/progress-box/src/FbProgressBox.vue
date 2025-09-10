<template>
	<div :class="`${prefix}-progress-box`" @click="onClick">
		<slot name="top">
			<fb-table-layout>
				<fb-table-layout-cell>
					<span>{{tPrefix}}</span>
				</fb-table-layout-cell>
				<fb-table-layout-cell align="right">
					<span v-if="tNum" :class="`${prefix}-progress-box_num`" :style="numSizeStyle" @click="onTnumClick">{{tNum}}</span>
					<span>{{tSuffix}}</span>
				</fb-table-layout-cell>
			</fb-table-layout>
		</slot>
		<slot name="progress">
			<fb-progress
				:type="type"
				:format="format"
				:percent="percent"
				:showInfo="showInfo"
				:status="status"
				:strokeWidth="strokeWidth"
				:strokeLinecap="strokeLinecap"
				:width="width"
				:strokeColor="strokeColor"
				:strokeBgColor="strokeBgColor"
				:textInside="textInside"
				:textStyle="textStyle"
				@on-click="onProgressClick"
			></fb-progress>
		</slot>
		<slot name="bottom">
			<fb-table-layout>
				<fb-table-layout-cell>
					<span>{{bPrefix}}</span>
				</fb-table-layout-cell>
				<fb-table-layout-cell align="right">
					<span v-if="bNum" :class="`${prefix}-progress-box_num`" :style="numSizeStyle" @on-click="onBnumClick">{{bNum}}</span>
					<span>{{bSuffix}}</span>
				</fb-table-layout-cell>
			</fb-table-layout>
		</slot>
	</div>
</template>

<script>
	import {prefix} from '../../../../src/config'
	import FbProgress from '../../progress/src/FbProgress'
	import FbTableLayout from '../../table-layout/src/FbTableLayout'
	import FbTableLayoutCell from '../../table-layout/src/FbTableLayoutCell'

	/**
	 * FbProgressBox 业务组件
	 * (c) 2020 lincong1987
	 */
	export default {
		name: 'FbProgressBox',
		components: {FbProgress, FbTableLayout, FbTableLayoutCell},
		props: {
			// 上半区
			tPrefix: {
				type: [String, Number],
				default: ''
			},
			tNum: {
				type: [String, Number],
				default: ''
			},
			tSuffix: {
				type: [String, Number],
				default: ''
			},
			// 下半区
			bPrefix: {
				type: [String, Number],
				default: ''
			},
			bNum: {
				type: [String, Number],
				default: ''
			},
			numSize: {
				type: [String, Number],
				default: ''
			},
			bSuffix: {
				type: [String, Number],
				default: ''
			},
			type: {
				type: [String],
				default: 'line'
			},
			format: {
				type: [Function]
			},
			percent: {
				type: [Number, String],
				default: 0
			},
			showInfo: {
				type: [Boolean],
				default: true
			},
			status: {
				type: [String],
				default: 'normal'
			},
			strokeWidth: {
				type: [String, Number],
				default: 10
			},
			strokeLinecap: {
				type: String,
				default: 'round'
			},
			width: {
				type: Number,
				default: 126
			},
			strokeColor: {
				type: [String, Object, Array, Function],
				default: ''
			},
			strokeBgColor: {
				type: [String],
				default: ''
			},
			textInside: {
				type: [Boolean],
				default: false
			},
			textStyle: {
				type: [Object],
			},
			borderRadius: {
				type: [String, Number],
				default: 0
			}

		},

		data() {
			return {
				prefix,
			}
		},
		computed: {
			numSizeStyle () {
				let style = {}
				if (this.numSize) {
					style['fontSize'] = parseInt(this.numSize) + 'px'
				}
				return style
			}
		},

		watch: {},

		methods: {
			onClick(e) {
				this.$emit('on-click', e)
			},
			onTnumClick(e) {
				this.$emit('on-tnum-click', e)
			},
			onProgressClick(e) {
				this.$emit('on-progress-click', e)
			},
			onBnumClick(e) {
				this.$emit('on-bnum-click', e)
			},
		},

		mounted() {

		},

		destroyed() {

		}
	}
</script>

<style scoped>

</style>
