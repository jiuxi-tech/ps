<template>

	<fb-container class="motion-rotate-star-wrapper" v-bind="$attrs">
		<div class="motion-rotate-star" v-for="(n, i) in num" :key="i"
		     :style="getStyle()"
		/>
	</fb-container>

</template>

<script>/**
 * MotionRotateStar.vue
 * (c) 2021 lincong1987
 */
import { random } from 'lodash-es'

export default {
	name: 'MotionRotateStar',

	props: {
		num: {
			type: Number,
			default: 800,
		},

		r: {
			type: Number,
			default: 800,
		},
	},

	data () {
		return {
			myNum: 0,
		}
	},

	mounted () {
		this.myNum = this.num
	},

	beforeDestroy () {
		this.myNum = 0
	},

	methods: {
		getStyle () {
			let s = 0.2 + Math.random()
			let curR = this.r + (Math.random() * 300)

			let height = random(1, 5)

			let style = {
				background: ["#fff83f", "#F7F7B6", "#ffffff"][random(0, 2)],
				filter: `blur(${random(0, 5)/10}px)`,
				borderRadius: `100%`,
				height: `${height}px`,
				width: `${height}px`,
				opacity: random(2, 10) / 10,
				transformOrigin: '0 0 ' + curR + 'px',
				transform: ' translate3d(0,0,-' + curR + 'px) rotateY(' + (Math.random() * 360) + 'deg) rotateX(' +
					(Math.random() * -50) + 'deg) scale(' + s + ',' + s + ')',

			}
			return style
		},
	},
}
</script>

<style lang="less" scoped>

@keyframes motion-rotate-star {
	0% {
		transform: perspective(1500px) rotateZ(20deg) rotateX(-40deg) rotateY(0);
	}
	100% {
		transform: perspective(400px) rotateZ(20deg) rotateX(-40deg) rotateY(-360deg);
	}
}

.motion-rotate-star-wrapper {
	transform:          perspective(500px);
	transform-style:    preserve-3d;
	position:           absolute;
	bottom:             0;
	perspective-origin: 50% 100%;
	left:               50%;
	animation:          motion-rotate-star 90s infinite linear;
}

.motion-rotate-star {
	width:               2px;
	height:              2px;
	background:          #F7F7B6;
	position:            absolute;
	top:                 0;
	left:                0;
	transform-origin:    0 0 -300px;
	transform:           translate3d(0, 0, -300px);
	backface-visibility: hidden;
	//	filter: blur(1px);
}
</style>
