<template>
	<fb-container ref="el" class="motion-star-line-wrapper" v-bind="$attrs">
		<canvas class="motion-star-line"
				ref="canvas"
		        :height="myHeight"
		        :width="myWidth"
		        :style="{
					width: `${myWidth}px`,
					height: `${myHeight}px`
				}"
		></canvas>
	</fb-container>
</template>

<script>
/**
 * MotionStarLine
 * (c) 2021 lincong1987
 */

//import { Points } from './Points'

import { Particles } from './Particles'

export default {
	name: 'MotionStarLine',

	props: {
		width: {
			type: [String, Number],
			default: undefined,
		},

		height: {
			type: [String, Number],
			default: undefined,
		},

		num: {
			type: [Number],
			default: undefined,
		},

		background: {
			type: [String],
			default: undefined,
		},

		color: {
			type: [String],
			default: undefined,
		},

		len: {
			type: [Number],
			default: undefined,
		},
	},

	data () {
		return {
			myHeight: typeof this.height !== 'undefined' ? this.height : window.innerHeight,
			myWidth: typeof this.width !== 'undefined' ? this.width : window.innerWidth,
		}
	},

	mounted () {

		this.$nextTick(() => {
			this.pjs = new Particles(this.$refs.canvas,

				{
					'particles': {
						'number': {
							'value': 80,
							'density': {
								'enable': true,
								'value_area': 800,
							},
						},
						'color': {
							'value': '#ffffff',
						},
						'shape': {
							'type': 'circle',
							'stroke': {
								'width': 0,
								'color': '#000000',
							},
							'polygon': {
								'nb_sides': 5,
							},
							'image': {
								'src': 'img/github.svg',
								'width': 100,
								'height': 100,
							},
						},
						'opacity': {
							'value': 0.5,
							'random': false,
							'anim': {
								'enable': false,
								'speed': 1,
								'opacity_min': 0.1,
								'sync': false,
							},
						},
						'size': {
							'value': 5,
							'random': true,
							'anim': {
								'enable': false,
								'speed': 40,
								'size_min': 0.1,
								'sync': false,
							},
						},
						'line_linked': {
							'enable': true,
							'distance': 150,
							'color': '#ffffff',
							'opacity': 0.4,
							'width': 1,
						},
						'move': {
							'enable': true,
							'speed': 6,
							'direction': 'none',
							'random': false,
							'straight': false,
							'out_mode': 'out',
							'attract': {
								'enable': false,
								'rotateX': 600,
								'rotateY': 1200,
							},
						},
					},
					'interactivity': {
						'detect_on': 'canvas',
						'events': {
							'onhover': {
								'enable': true,
								'mode': 'repulse',
							},
							'onclick': {
								'enable': true,
								'mode': 'push',
							},
							'resize': true,
						},
						'modes': {
							'grab': {
								'distance': 400,
								'line_linked': {
									'opacity': 1,
								},
							},
							'bubble': {
								'distance': 400,
								'size': 40,
								'duration': 2,
								'opacity': 8,
								'speed': 3,
							},
							'repulse': {
								'distance': 200,
							},
							'push': {
								'particles_nb': 4,
							},
							'remove': {
								'particles_nb': 2,
							},
						},
					},
					'retina_detect': true,
					'config_demo': {
						'hide_card': false,
						'background_color': '#b61924',
						'background_image': '',
						'background_position': '50% 50%',
						'background_repeat': 'no-repeat',
						'background_size': 'cover',
					},
				},
			)

//			this.points = new Points({
//				el: this.$refs.el,
//				pointsNum: this.num,
//				height: this.myHeight,
//				width: this.myWidth,
//				background: this.background,
//			})
		})

	},

	beforeDestroy () {
		//this.points.remove()
	},
}
</script>

<style lang="less" scoped>

.motion-star-line-wrapper {
	// canvas
	.motion-star-line {
		position: absolute;
		top:      0;
		left:     0;
		bottom:   0;
		right:    0;
		height:   100%;
		width:    100%;
	}
}

</style>
