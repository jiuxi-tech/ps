<template>
	<fb-container ref="ctx" class="motion-point-wave" v-bind="$attrs"></fb-container>
</template>

<script>
/**
 * MotionPointWave
 * (c) 2021 lincong1987
 */

import { PointWave } from './PointWave'

export default {
	name: 'MotionPointWave',

	props: {
		speed: {
			type: Number,
			default: 5,
		},
		size: {
			type: Number,
			default: 3,
		},
	},

	mounted () {
		this.pointWave = new PointWave({
			el: this.$refs.ctx.$el,
			uniforms: {
				size: {
					type: 'float',
					value: 3,
				},
				field: {
					type: 'vec3',
					value: [0, 0, 0],
				},
				speed: {
					type: 'float',
					value: this.speed,
				},
			},

		})
	},

	beforeDestroy () {
		this.pointWave.remove()
	},

}
</script>

<style lang="less" scoped>
.motion-point-wave {
	position: absolute;
	left:     0;
	top:      0;
	right:    0;
	bottom:   0;
}
</style>
