<template>
	<div :class="[getClass]" :style="[getStyle, {}]" ref="avatar">


		<template v-if="src">
			<img ref="content" :src="src"
			     @onerror="handleImageLoadError($event)" :alt="alt"/>
		</template>

		<template v-else-if="text">
			<span ref="content" :class="`${prefix}-avatar__text`"
			      :style="getTextStyle">{{text}}</span>
		</template>

		<template v-else-if="$slots.default">
			<span ref="content" :class="`${prefix}-avatar__text`"
			      :style="getTextStyle"><slot></slot></span>
		</template>

		<template v-else-if="icon">
			<fb-icon :name="icon" :size="fontSize"></fb-icon>
		</template>

	</div>
</template>

<script>
/**
 * FbAvatar
 * (c) 2020 lincong1987
 */

import { getSizeStyle } from '../../../utils/propUtils'
import FbIcon from '../../icon/src/FbIcon'
import { prefix } from '../../../../src/config'

const sizeMap = {
	s: 24,
	m: 32,
	l: 40,
	xl: 50,
}

export default {
	name: 'FbAvatar',
	components: {FbIcon},
	props: {
		size: {
			type: [String, Number],
			default: 'm',
		},
		// 圆型
		circle: {
			type: Boolean,
			default: false,
		},
		icon: {
			type: [String],
			default: undefined,
		},
		color: {
			type: [String],
			default: undefined,
		},
		backgroundColor: {
			type: [String],
			default: undefined,
		},
		text: {
			type: [String, Number],
			default: undefined,
		},

		src: {
			type: [String],
			default: undefined,
		},

		alt: {
			type: [String],
			default: undefined,
		},

		loadError: Function,
	},
	data () {
		return {
			prefix,
			sizeNumber: 0,
			fontSize: 0,
			scale: 1,
			isImgExist: true,
		}
	},
	computed: {

		getTextStyle () {

			let style = {fontSize: `${this.fontSize}px`}

			style.transform = `scale(${this.scale}) translateX(-50%)`


			if (this.color) {
				style.color = this.color
			}

			return style

		},

		mySizeStyle () {

			let style = {}

			let width = getSizeStyle(this.size, sizeMap)

			this.sizeNumber = parseInt(width, 10)

			this.fontSize = parseInt(this.sizeNumber * 0.65, 10)

			style.width = width
			style.height = width
			style.lineHeight = width
			style.textAlign = 'center'
			style.fontSize = getSizeStyle(this.fontSize)

			return style
		},

		getStyle () {

			let style = this.mySizeStyle

			if (this.backgroundColor) {
				style["backgroundColor"] = this.backgroundColor
			}

			if (this.color) {
				style.color = this.color
			}
			return style
		},

		getClass () {
			let arr = [`${prefix}-avatar`]

			if (this.circle == true) {
				arr.push(`${prefix}-avatar--circle`)
			}

			if (this.src) {
				arr.push(`${prefix}-avatar--image`)
			}

			return arr
		},

	},
	watch: {
		src () {
			this.$nextTick(() => {
				this.isImgExist = true
				this.scale = 1
				this.$forceUpdate()
			})
		},

		color (value) {
		},
		backgroundColor (value) {
		},
	},
	methods: {

		handleImageLoadError (event) {
			const cbError = this.loadError ? this.loadError(event) : undefined
			if (cbError !== false) {
				this.isImgExist = false
			}
		},

		setScale () {
			if (!this.$refs.content || !this.$refs.avatar) {
				return
			}
			const childrenWidth = this.$refs.content.offsetWidth // offsewtWidth avoid affecting be transform scale
			const nodeWidth = this.$refs.avatar.offsetWidth
			// denominator is 0 is no meaning
			if (
				childrenWidth === 0 ||
				nodeWidth === 0 ||
				(this.lastChildrenWidth === childrenWidth && this.lastNodeWidth === nodeWidth)
			) {
				return
			}
			this.lastChildrenWidth = childrenWidth
			this.lastNodeWidth = nodeWidth
			// add 4px gap for each side to get better performance
			this.scale = nodeWidth - 8 < childrenWidth ? (nodeWidth - 8) / childrenWidth : 1
		},

	},
	mounted () {
		this.$nextTick(() => {
			this.setScale()
			this.isMounted = true
		})
	},
	updated () {
		this.$nextTick(() => {
			this.setScale()
		})
	},

}
</script>

<style scoped>

</style>
