<template>
	<!-- FbImageViewer -->
	<transition :name="`${prefix}-image-viewer-fade`">

		<div tabindex="-1" ref="wrapper" :class="`${prefix}-image-viewer__wrapper`"
		     :style="{ 'z-index': zIndex }">
			<div :class="`${prefix}-image-viewer__mask`" @click.self="handleMaskClick"></div>
			<!-- 关闭 -->
			<fb-tooltip content="关闭">
				<span :class="`${prefix}-image-viewer__btn ${prefix}-image-viewer__close`"
				      @click="close">
				<fb-icon name="close" size="20"></fb-icon>
			</span>
			</fb-tooltip>
			<!-- 左右箭头 -->
			<template v-if="!isSingle">
				<fb-tooltip content="上一张" placement="top">
				<span
					:class="[`${prefix}-image-viewer__btn ${prefix}-image-viewer__prev`,
					{ 'is-disabled': !isInfinite && isFirst }]"
					@click="prev">
					<fb-icon name="left" size="20"></fb-icon>
				</span>
				</fb-tooltip>
				<fb-tooltip content="下一张" placement="top">
				<span
					:class="[`${prefix}-image-viewer__btn ${prefix}-image-viewer__next`,
							{ 'is-disabled': !isInfinite && isLast }]"
					@click="next">
					<fb-icon name="right" size="20"></fb-icon>
				</span>
				</fb-tooltip>
			</template>


			<div :class="`${prefix}-image-viewer__info`">
				<!--	信息 预留			-->
				<fb-icon name="information" mr="4px"></fb-icon>
				您可以使用键盘的上下左右箭头控制图片
			</div>

			<!-- 工具栏 -->
			<div :class="`${prefix}-image-viewer__toolbar`">
				<div :class="`${prefix}-image-viewer__toolbar__inner`">
					<fb-tooltip content="缩小" placement="top">
						<fb-icon name="zoom-out1" size="20" @on-click="handleAction('zoomOut')"></fb-icon>
					</fb-tooltip>
					<fb-tooltip content="放大" placement="top">
						<fb-icon name="zoom-in1" size="20" @on-click="handleAction('zoomIn')"></fb-icon>
					</fb-tooltip>
					<fb-tooltip :content="mode==='contain' ? '自适应' : '1比1'" placement="top">
						<fb-icon :name="mode==='contain' ? 'off-size' : 'one-to-one'"
						         size="20"
						         @on-click="toggleMode"></fb-icon>
					</fb-tooltip>
					<fb-container height="20px" width="1px" background="#555"></fb-container>
					<fb-tooltip content="旋转" placement="top">
						<fb-icon name="rotate" size="20" @on-click="handleAction('rotate')"></fb-icon>
					</fb-tooltip>
				</div>
			</div>

			<div :class="[
				`${prefix}-image-viewer__el`,
				{

			}
			]">

				<!--				<fb-spin :show="isLoading">-->
				<!--				</fb-spin>-->
				<img
					v-for="(url, i) in imageUrls"
					v-if="i === myImageIndex"
					ref="img"
					:class="`${prefix}-image-viewer__image`"
					:key="url"
					:src="imageUrls[i]"
					alt=""
					:style="getImageStyle"

					@load="handleImgLoad"
					@error="handleImgError"
					@mousedown="handleMouseDown"
				/>

			</div>
		</div>


	</transition>
</template>

<script>
import { prefix } from '../../../../src/config'
import FbIcon from '../../icon/src/FbIcon'
import { throttle } from 'lodash-es'
import FbTooltip from '../../tooltip/src/tooltip'
import FbContainer from '../../container/src/FbContainer'

/**
 * FbImageViewer
 * (c) 2022 lincong1987
 */


export default {
	name: 'FbImageViewer',
	components: {
		FbContainer,
		FbTooltip,
		FbIcon,
	},

	props: {
		imageUrls: {
			type: Array,
			default: () => [],
		},
		zIndex: {
			type: Number,
			default: 2000,
		},
		beforeClose: {
			type: Function,
			default: () => {
			},
		},
		imageIndex: {
			type: Number,
			default: 0,
		},
		appendToBody: {
			type: Boolean,
			default: true,
		},
		// 点击阴影部分关闭窗口
		closeOnClickShadow: {
			type: Boolean,
			default: false,
		},
		// 按ESC键关闭窗口
		disableEsc: {
			type: Boolean,
			default: false,
		},
	},

	data () {
		let myImageIndex = this.imageIndex
		return {
			prefix,
			// contain or original
			mode: 'contain',
			isSingle: this.imageUrls.length <= 1,
			isFirst: myImageIndex === 0,
			isLast: myImageIndex === this.imageUrls.length - 1,
			isInfinite: true,
			isLoading: true,
			myImageIndex,

			imageStyle: {
				scale: 1,
				deg: 0,
				offsetX: 0,
				offsetY: 0,
			},
		}
	},

	computed: {
		getImageStyle () {

			let {
				scale,
				deg,
				offsetX,
				offsetY,
				enableTransition,
			} = this.imageStyle
			const style = {
				transform: `scale(${scale}) rotate(${deg}deg)`,
				transition: 'transform .4s',
				'margin-left': `${offsetX}px`,
				'margin-top': `${offsetY}px`,
			}
			if (this.mode === 'contain') {
				style.maxWidth = style.maxHeight = '100%'
			}
			return style

		},
	},

	methods: {

		reset () {
			this.imageStyle = {
				scale: 1,
				deg: 0,
				offsetX: 0,
				offsetY: 0,
			}
		},

		handleAction (actionName) {
			if (actionName === 'rotate') {
				this.imageStyle.deg = this.imageStyle.deg + 90
			}
			if (actionName === 'zoomIn') {
				this.imageStyle.scale = parseFloat((this.imageStyle.scale + 0.2).toFixed(3))

			}
			if (actionName === 'zoomOut') {
				if (this.imageStyle.scale > 0.2) {
					this.imageStyle.scale = parseFloat((this.imageStyle.scale - 0.2).toFixed(3))
				}
			}
		},

		toggleMode () {
			this.mode = this.mode === 'contain' ? 'original' : 'contain'
			this.reset()
		},
		close () {
			this.beforeClose()
		},
		handleMaskClick () {
			if (this.closeOnClickShadow) {
				this.beforeClose()
			}
		},

		prev () {
			if (this.isFirst && !this.isInfinite) {
				return
			}
			let len = this.imageUrls.length
			this.reset()
			this.myImageIndex = (this.myImageIndex - 1 + len) % len
		},
		next () {

			if (this.isLast && !this.isInfinite) {
				return
			}
			const len = this.imageUrls.length
			this.reset()
			this.myImageIndex = (this.myImageIndex + 1) % len
		},

		handleImgLoad (e) {
			this.isLoading = false
		},
		handleImgError () {
			this.isLoading = false
			this.$message.error('图像加载异常')
			this.close()
		},
		handleMouseDown (e) {
			const {
				offsetX,
				offsetY,
			} = this.imageStyle
			const startX = e.clientX
			const startY = e.clientY
//			this.dragEvent =

			document.onmousemove = throttle(e => {
				this.imageStyle.offsetX = offsetX + e.clientX - startX
				this.imageStyle.offsetY = offsetY + e.clientY - startY
			}, 0)

			document.onmouseup = (e) => {
				e.preventDefault()
				document.onmousemove = null
				document.onmouseup = null
			}
//			document.addEventListener('mousemove', this.dragEvent)
//			document.addEventListener('mouseup', (e) => {
//				document.removeEventListener('mousemove', this.dragEvent)
//			})
			e.preventDefault()
		},

		keyboardEvents (e) {

			e.stopPropagation()
			const keyCode = e.keyCode
			switch (keyCode) {
				// ESC
				case 27:
					if (this.disableEsc === false) {
						this.close()
					}
					break
				// SPACE
				case 32:
					this.toggleMode()
					break
				// LEFT_ARROW
				case 37:
					this.prev()
					break
				// UP_ARROW
				case 38:
					this.handleAction('zoomIn')
					break
				// RIGHT_ARROW
				case 39:
					this.next()
					break
				// DOWN_ARROW
				case 40:
					this.handleAction('zoomOut')
					break
			}
		},

		removeEvents () {

		},

	},

	mounted () {
		// this.deviceSupportInstall()

		document.addEventListener('keydown', this.keyboardEvents, false)

		if (this.appendToBody) {
			document.body.appendChild(this.$el)
		}
		this.$nextTick(() => {
			this.$refs.wrapper.focus()
		})

	},

	beforeDestroy () {
		document.removeEventListener('keydown', this.keyboardEvents, false)
	},
	destroyed () {

		document.removeEventListener('keydown', this.keyboardEvents, false)

		if (this.appendToBody && this.$el && this.$el.parentNode) {
			this.$el.parentNode.removeChild(this.$el)
		}
	},

}
</script>

<style lang="less" scoped>

</style>
