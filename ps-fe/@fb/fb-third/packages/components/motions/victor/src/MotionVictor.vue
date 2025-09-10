<template>
	<fb-container class="motion-victor-wrapper" ref="motion-victor-wrapper" v-bind="$attrs">
		<fb-container class="motion-victor " ref="motion-victor"/>
	</fb-container>
</template>

<script>
	/**
	 * MotionVictor
	 * (c) 2021 lincong1987
	 */
	import {Victor} from './Victor'

	export default {
		name: 'MotionVictor',

		props: {
			theme: {
				type: String,
				default: 'primary',
			},

			color: {
				type: Array,
				default: undefined,
			},
		},

		data() {
			return {
				myTheme: {
					primary: ['#002c4a', '#005584'],
					green: ['#35ac03', '#3f4303'],
					red: ['#ac0908', '#cd5726'],
					blue: ['#18bbff', '#00486b'],
				},
			}
		},

		watch: {
			theme(val) {
				let color = this.myTheme[val]
				color && this.victor(color).set()
			},
			color(val) {
				let color = val
				color && color[0] && color[1] && this.victor(color).set()
			},
		},

		mounted() {
			this.victor = new Victor(this.$refs['motion-victor-wrapper'].$el, this.$refs['motion-victor'].$el)
			if (this.theme) {
				let color = this.myTheme[this.theme]
				color && this.victor(color).set()
			}
			if (this.color) {
				let color = this.color
				color && this.victor(color).set()
			}
		},

		beforeDestroy() {
			this.victor.remove()
			this.victor = null
		},

		methods: {},
	}
</script>

<style lang="less" scoped>

	.motion-victor-wrapper {
		height: 100%;
		width: 100%;
		position: absolute;

		.motion-victor {
			height: 100%;
			width: 100%;
		}
	}

</style>
