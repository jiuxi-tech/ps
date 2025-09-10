<template>
	<div :class="getClass">
		<div
			:class="`${prefix}-loading-bar__process`"
			:style="getProcessStyle"
		></div>
	</div>
</template>

<script>


	import { prefix } from '../../../../src/config'
	/**
	 * FbLoadingBar
	 * (c) 2020 lincong1987
	 */
	export default {
		name: 'FbLoadingBar',
		props: {
			// 类型, 每种类型对应一个颜色
			type: {
				type: String,
				default: 'primary',
				validate (val) {
					return ['primary', 'info', 'success', 'warn', 'danger'].includes(val)
				},
			},
			// 进度条高度
			height: {
				type: [Number],
				default: 3,
			},
			//  进度条宽度 百分比
			process: {
				type: [Number],
				default: 0,
			},
			// 不配类型的话,也可以直接上颜色
			color: {
				type: String,
				default: null,
			},

		},
		data () {
			return {
				prefix,
			}
		},

		computed: {
			getClass () {

				var arr = [`${prefix}-loading-bar`]

				arr.push(`${prefix}-loading-bar--${this.type}`)

				return arr

			},

			getProcessStyle () {

				var arr = {}

				arr.width = `${this.process}%`
				arr.height = `${this.height}px`

				if (this.color) {
					arr.color = `${this.color}`
				}
				return arr

			},
		},
		methods: {},

	}
</script>

<style scoped>

</style>
