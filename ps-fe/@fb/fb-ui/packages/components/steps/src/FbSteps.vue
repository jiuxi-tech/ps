<template>
	<div :class="getClass">
		<slot></slot>
	</div>
</template>

<script>
	/**
	 * FbSteps
	 * (c) 2020 lincong1987
	 */
	import { prefix } from '../../../../src/config'
	export default {
		name: 'FbSteps',
		props: {

			// 当前步骤
			current: {
				type: [String, Number],
				default: 0,
			},

			value: {
				type: [String, Number],
			},

			// 数据绑定
			data: {
				type: Array,
				default () {
					return []
				},
			},

			// 尺寸
			size: {
				type: String,
				default: undefined,
			},

			align: {
				type: String,
				default: undefined,
			},

			// 垂直
			vertical: {
				type: Boolean,
				default: false,
			},

		},

		data () {
			return {
				prefix,
				totalSteps: 0,
				childWidth: '',
			}
		},

		computed: {
			getClass () {
				let arr = [`${prefix}-steps`]
				if (this.size) {
					arr.push(`${prefix}-steps--size-${this.size}`)
				}
				if (this.align) {
					arr.push(`${prefix}-steps--${this.align}`)
				}
				if (this.vertical) {
					arr.push(`${prefix}-steps--vertical`)
				}

				return arr
			},
		},

		watch: {
			current () {
				this.updateStatus()
			},
		},

		methods: {
			updateStatus () {
				const _this = this
				if (this.$children) {
					this.$children.forEach((step, index) => {
						step.index = index// + 1

						if (step.status == 'error') {
							step.myStatus = 'error'
						} else if (step.status == 'wait') {
							step.myStatus = 'wait'
						} else if (step.index > _this.current) {
							step.myStatus = 'ready'
						} else if (step.index == _this.current) {
							step.myStatus = 'doing'
						} else {
							step.myStatus = 'done'
						}
					})
				}
			},
		},

		mounted () {

			this.totalSteps = this.$children.length
			this.childWidth = `${100 / this.totalSteps}%`
			if (this.vertical) {
				this.childWidth = '100%'
			}

			this.updateStatus()
		},
	}
</script>

<style scoped>

</style>
