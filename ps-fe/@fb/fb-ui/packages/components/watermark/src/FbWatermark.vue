<template>
	<div :class="getClass" :style="getStyle" @click="$emit('on-click')">
		<slot></slot>
	</div>
</template>

<script>
/**
 * FbWatermark
 * (c) 2022 lincong1987
 */
import { prefix } from '../../../../src/config'

/**
 * 返回当前显示设备的物理像素分辨率与CSS像素分辨率之比
 * @param context
 */
const getPixelRatio = (context) => {
	if (!context) {
		return 1
	}
	const backingStore =
		context.backingStorePixelRatio ||
		context.webkitBackingStorePixelRatio ||
		context.mozBackingStorePixelRatio ||
		context.msBackingStorePixelRatio ||
		context.oBackingStorePixelRatio ||
		context.backingStorePixelRatio ||
		1
	return (window.devicePixelRatio || 1) / backingStore
}

export default {
	name: 'FbWatermark',
	props: {
		/**
		 * 超级水印模式
		 * 采用 mask 实现
		 */
		mask: {
			type: Boolean,
			default: () => false,
		},
		/**
		 *  超级水印透明度
		 */
		maskOpacity: {
			type: Number,
			default: () => 0.8,
		},
		/**
		 * 内容
		 * 单行 '朱红掏'
		 * 换行 ['章了了', '13999999999', 'zht@cleverlin.com']
		 */
		content: {
			type: [String, Array],
			default: () => '',
		},
		/**
		 * 水印块的宽度
		 */
		width: {
			type: Number,
			default: () => 120,
		},
		/**
		 * 水印块的高度
		 */
		height: {
			type: Number,
			default: () => 64,
		},

		/**
		 * 旋转角度
		 */
		rotate: {
			type: Number,
			default: () => -22,
		},
		/**
		 * 水印文字尺寸
		 * 尺码 xs,s,m,l,xl,xxl,xxxl
		 * 数字 12,13,14,15...
		 * 文本 12px, 13px, 14px...
		 */
		size: {
			type: [String, Number],
			default: () => 16,
		},
		/**
		 * 水印文字颜色
		 * hex #333 #666666 #dddddd01
		 * rgba rgba(1a, 2b, 3c, 0.5)
		 */
		color: {
			type: String,
			default: () => '#F5F5F5',
		},

		/**
		 * 水印背景颜色
		 * hex #333 #666666 #dddddd01
		 * rgba rgba(1a, 2b, 3c, 0.5)
		 */
		backgroundColor: {
			type: String,
			default: () => 'transparent',
		},

		/**
		 * 水印文字字重
		 * normal bold
		 */
		weight: {
			type: String,
			default: () => 'normal',
		},

		/**
		 * 水印文字行高
		 */
		lineHeight: {
			type: Number,
			default: () => 1.2,
		},

		/**
		 * 水印文字字体
		 * 'sans-serif' 'Microsoft YaHei'
		 */
		family: {
			type: String,
			default: () => 'sans-serif',
		},

		/**
		 * 文本对齐选项。
		 * 可选的值包括：start, end, left, right or center.
		 * 默认值是 start
		 */
		align: {
			type: String,
			default: () => 'start',
		},
		/**
		 * 文本方向。
		 * 可能的值包括：ltr, rtl, inherit。
		 * 默认值是 inherit
		 */
		direction: {
			type: String,
			default: () => 'inherit',
		},

		/**
		 * 斜体
		 * 可能的值包括：false， true。
		 * 默认值是 false
		 */
		italic: {
			type: Boolean,
			default: () => false,
		},

		/**
		 * 水印图像
		 * 图像链接 http://abc.com/xxx.png
		 * 图像导入 require('./xxx.png')
		 * base64
		 */
		image: {
			type: String,
			default: () => '',
		},

		block: {
			type: Boolean,
			default: () => false,
		},

		zIndex: {
			type: Number,
			default: () => 100,
		},

		/** 水印样式 */
		markStyle: {
			type: Object,
			default: () => {
			},
		},
		/** 水印类名 */
		markClassName: {
			type: String,
			default: () => '',
		},
		/** 水印之间的水平间距 */
		gapX: {
			type: Number,
			default: () => 212,
		},
		/** 水印之间的垂直间距 */
		gapY: {
			type: Number,
			default: () => 222,
		},
		/** 追加的水印元素的z-index */

		/**
		 * 水印在canvas 画布上绘制的垂直偏移量，正常情况下，水印绘制在中间位置, 即 offsetTop = gapY / 2
		 */
		offsetTop: {
			type: Number,
			default: () => 0,
		},
		/**
		 * 水印图片距离绘制 canvas 单元的顶部距离
		 * 水印在canvas 画布上绘制的水平偏移量, 正常情况下，水印绘制在中间位置, 即 offsetTop = gapX / 2
		 */
		offsetLeft: {
			type: Number,
			default: () => 0,
		},

	},
	data () {
		return {
			prefix,
			base64Url: '',
			base64Image: '',
		}
	},

	computed: {
		getClass () {
			let arr = [`${prefix}-watermark`]

			if (this.size) {
				arr.push(`${prefix}-watermark--${this.size}`)
			}
			if (this.type) {
				arr.push(`${prefix}-watermark--${this.type}`)
			}
			return arr

		},

		getStyle () {
			let style = {}

			this.update()
			if (this.base64Image) {
				if (this.mask) {
					style['mask-type'] = `alpha`
					style['-webkit-mask-repeat'] = `repeat`
					style['-webkit-mask-size'] = `${this.gapX + this.width}px`
					style['-webkit-mask-image'] = `url('${this.base64Image}')`
				} else {
					style.backgroundRepeat = 'repeat'
					style.backgroundSize = `${this.gapX + this.width}px`
					//style.pointerEvents = 'none'
					style.backgroundImage = `url('${this.base64Image}')`
				}
			}

			//style.backgroundColor = 'red'

			return style
		},

	},

	watch: {
	},

	methods: {

		update () {
			const canvas = document.createElement('canvas')
			const ctx = canvas.getContext('2d')
			const ratio = getPixelRatio(ctx)
			const canvasWidth = `${(this.gapX + this.width) * ratio}px`
			const canvasHeight = `${(this.gapY + this.height) * ratio}px`
			const canvasOffsetLeft = this.offsetLeft || this.gapX / 2
			const canvasOffsetTop = this.offsetTop || this.gapY / 2

			canvas.setAttribute('width', canvasWidth)
			canvas.setAttribute('height', canvasHeight)

			if (ctx) {

				if (this.mask) {
					ctx.globalAlpha = this.maskOpacity
					ctx.fillStyle = this.backgroundColor || '#000000'
					ctx.fillRect(0, 0, canvas.width, canvas.height)
					ctx.globalCompositeOperation = 'destination-out'
				}

				if (this.image) {
					const img = new Image()
					img.crossOrigin = 'anonymous'
					img.referrerPolicy = 'no-referrer'
					img.src = this.image
					img.onload = () => {

						// 旋转字符 rotate
						ctx.translate(canvasOffsetLeft * ratio, canvasOffsetTop * ratio)
						ctx.rotate((Math.PI / 180) * Number(this.rotate))
						let markWidth = this.width * ratio
						let markHeight = this.height * ratio

						ctx.drawImage(img, 0, 0, markWidth, markHeight)
						this.base64Image = canvas.toDataURL()
					}
				} else {
					const markSize = Number(this.size) * ratio
					// 旋转字符 rotate
					ctx.translate(canvasOffsetLeft * ratio, canvasOffsetTop * ratio)
					ctx.rotate((Math.PI / 180) * Number(this.rotate))
					let markWidth = this.width * ratio
					let markHeight = this.height * ratio
//
					ctx.fillStyle = this.color || '#F5F5F5'

					ctx.font = `${this.italic
						? 'italic'
						: ''} ${this.weight} ${markSize}px/${markHeight}px ${this.family}`
//
//					ctx.direction = this.direction || 'ltr'
//					ctx.textAlign = this.align || 'start'

					if (this.mask) {

						if (Array.isArray(this.content)) {
							this.content.forEach(
								(item, index) => ctx.strokeText(item, 0, markSize * index * this.lineHeight))
						} else {
							ctx.strokeText(this.content, 0, 0)
						}

						if (Array.isArray(this.content)) {
							this.content.forEach((item, index) => ctx.fillText(item, 0, markSize * index * this.lineHeight))
						} else {
							ctx.fillText(this.content, 0, 0)
						}
					} else {

						if (Array.isArray(this.content)) {
							this.content.forEach(
								(item, index) => ctx.fillText(item, 0, markSize * index * this.lineHeight))
						} else {
							ctx.fillText(this.content, 0, 0)
						}
					}


					this.base64Image = canvas.toDataURL()
				}

			} else {
				// eslint-disable-next-line no-console
				console.error('当前环境不支持Canvas')
			}
		},
	},
}
</script>

<style scoped>

</style>
