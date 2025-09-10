<template>
	<div class="jpx-sign-wrapper">
		<!--		<template  >-->
		<div class="jpx-sign-signer"
			 :style="{height: `${rect.height + 60}px` , width:`${rect.width}px`}"
		>
			<div class="jpx-sign-signer__header">
				<template v-if="$slots.header">
					<slot name="header"></slot>
				</template>
				<template v-else>
					<fb-icon class="jpx-sign-signer__header__icon" name="file-edit" size="16" mr="4px"/>
					<fb-text class="jpx-sign-signer__header__title" size="16" mr="8px">签名区</fb-text>
					<fb-text class="jpx-sign-signer__header__title-desc">请在下方虚线区内签名</fb-text>
				</template>
			</div>
			<canvas class="jpx-sign-signer__canvas" ref="canvas"
					@mousedown="handleMouseDown" @mousemove="handleMouseMove" @mouseup="handleMouseUp"/>
			<div class="jpx-sign-signer__actions" v-show="showAction">
				<fb-button @on-click="handleConfirmClick" type="primary" size="s">确认</fb-button>
				&nbsp;
				<fb-button @on-click="handleResetClick" size="s">重写</fb-button>
			</div>
		</div>
		<!--		</template>-->
		<!--		<template v-if="readonly && !myValue">-->
		<!--			<div class="jpx-sign-empty">-->
		<!--				<fb-empty text="空白"></fb-empty>-->
		<!--			</div>-->
		<!--		</template>-->
		<!--		<template v-else>-->
		<!--			<div class="jpx-sign-preview">-->
		<!--				<img :src="myValue"/>-->
		<!--			</div>-->
		<!--		</template>-->
	</div>
</template>

<script>
import FbEmpty from '../../empty/src/FbEmpty'
import FbButton from '../../button/src/FbButton'

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

/**
 * FbSign
 * (c) 2022 lincong1987
 */

export default {
	name: 'FbSign',
	components: {
		FbButton,
		FbEmpty,
	},
	props: {

		/**
		 * 只读
		 */
		readonly: {
			type: Boolean,
			default: false,
		},

		/**
		 * 画布宽度
		 */
		width: {
			type: [Number],
			default: 600,
		},
		/**
		 * 画布高度
		 */
		height: {
			type: [Number],
			default: 300,
		},
		/**
		 * 线宽
		 */
		lineWidth: {
			type: [Number, String],
			default: 2,
		},
		/**
		 * 线颜色
		 */
		lineColor: {
			type: String,
			default: '#000000',
		},
		/**
		 * 背景色
		 */
		backgroundColor: {
			type: String,
			default: '',
		},

		/**
		 * 背景图像
		 */
		backgroundImage: {
			type: String,
			default: '',
		},

		/**
		 * 图像输出质量
		 */
		quality: {
			type: Number,
			default: 0.6,
		},
		showAction: {
			type: Boolean,
			default: true,
		}
	},

	data () {
		return {
			myValue: '',
			hasSign: false,
			points: [],
			canvasContext: null,
			startX: 0,
			startY: 0,
			isDrawing: false,
			ratio: 1,
		}
	},

	computed: {
		rect () {
			let {
				height,
				width,
			} = this
			return {
				height: parseInt(height, 10),
				width: parseInt(width, 10),
			}
		},

	},
	watch: {},
	config: {},
	methods: {
		/**
		 * events
		 */
		handleMouseDown (e) {
			e = e || event
			e.preventDefault()
			this.isDrawing = true
			this.hasSign = true
			let obj = {
				x: e.offsetX,
				y: e.offsetY,
			}
			this.drawStart(obj)
		},
		handleMouseMove (e) {
			e = e || event
			e.preventDefault()
			if (this.isDrawing) {
				let obj = {
					x: e.offsetX,
					y: e.offsetY,
				}
				this.drawMove(obj)
			}
		},
		handleMouseUp (e) {
			e = e || event
			e.preventDefault()
			let obj = {
				x: e.offsetX,
				y: e.offsetY,
			}
			this.drawEnd(obj)
			this.isDrawing = false
		},

		handleConfirmClick () {
			this.getBase64().then((base64 => {
				this.$emit('input', base64)
				this.$emit('on-confirm', base64)
			}))
		},
		handleResetClick () {
			this.reset()
			this.$emit('on-reset')
		},

		// 绘制
		drawStart (obj) {
			this.startX = obj.x
			this.startY = obj.y
			this.canvasContext.beginPath()
			this.canvasContext.moveTo(this.startX, this.startY)
			this.canvasContext.lineTo(obj.x, obj.y)
			this.canvasContext.lineCap = 'round'
			this.canvasContext.lineJoin = 'round'
			this.canvasContext.lineWidth = this.lineWidth * this.ratio
			this.canvasContext.stroke()
			this.canvasContext.closePath()
			//this.points.push(obj)
		},
		drawMove (obj) {
			this.canvasContext.beginPath()
			this.canvasContext.moveTo(this.startX, this.startY)
			this.canvasContext.lineTo(obj.x, obj.y)
			this.canvasContext.quadraticCurveTo(this.startX, this.startY, obj.x, obj.y)
			this.canvasContext.strokeStyle = this.lineColor
			this.canvasContext.lineWidth = this.lineWidth * this.ratio
			this.canvasContext.lineCap = 'round'
			this.canvasContext.lineJoin = 'round'
			this.canvasContext.stroke()
			this.canvasContext.closePath()
			this.startY = obj.y
			this.startX = obj.x

			//this.points.push(obj)
		},
		drawEnd (obj) {
			this.canvasContext.beginPath()
			this.canvasContext.moveTo(this.startX, this.startY)
			this.canvasContext.lineTo(obj.x, obj.y)
			this.canvasContext.lineCap = 'round'
			this.canvasContext.lineJoin = 'round'
			this.canvasContext.stroke()
			this.canvasContext.closePath()
			//this.points.push(obj)
			//this.points.push({x: -1, y: -1})
		},

		// 操作
		getBase64 (options) {
			let imgQuality = options && options.quality ? options.quality : this.quality
			return new Promise((resolve, reject) => {
				if (!this.hasSign) {
					this.$message.warn(`未签名!`)
					return
				}
//				let resImgData = this.canvasContext.getImageData(0, 0, this.$refs.canvas.width,
//					this.$refs.canvas.height)
//				this.canvasContext.globalCompositeOperation = 'destination-over'
//				this.canvasContext.fillStyle = this.backgroundColor
//				this.canvasContext.fillRect(0, 0, this.$refs.canvas.width, this.$refs.canvas.height)
				// let image = this.canvasContext.getImageData(0, 0, this.rect.width, this.rect.height)
				this.myValue = this.$refs.canvas.toDataURL('image/png', imgQuality)
				resolve(this.myValue)
			})
		},

		/**
		 * 清空
		 * 外部实例方法
		 */
		clear () {
			this.reset()
		},
		reset () {
			this.canvasContext.clearRect(
				0,
				0,
				this.$refs.canvas.width,
				this.$refs.canvas.height,
			)
			this.hasSign = false
			this.myValue = ''

			this.$emit('input', '')

		},
	},
	created () {
	},
	mounted () {
		const canvas = this.$refs.canvas
//		if (!this.readonly) {

		this.canvasContext = canvas.getContext('2d')
		this.ratio = getPixelRatio(this.canvasContext)
		canvas.height = this.rect.height - 6
		canvas.width = this.rect.width - 6

		let computedStyle = window.getComputedStyle(canvas)

		canvas.style.height = `${parseFloat(computedStyle.height)}px`
		canvas.style.width = `${parseFloat(computedStyle.width)}px`

		canvas.style.background = this.backgroundColor
		// 在画板以外松开鼠标后冻结画笔
		document.onmouseup = () => {
			this.isDrawing = false
		}
//		}
	},
	beforeDestroy () {
	},

}
</script>

<style lang="less" scoped>
</style>
