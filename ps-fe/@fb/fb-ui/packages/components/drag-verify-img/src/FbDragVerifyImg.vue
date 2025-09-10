<!-- 拖拽拼图验证 -->
<template>
	<div :class="`${prefix}-drag-verify-content`" :style="{width: width + 'px'}">
		<transition name="fade">
			<div v-show="imgShow" ref="imgBg" :class="`${prefix}-drag-verify-imgBg`" :style="dragVerifyImgBgStyle">
				<i @click="reset" :class="`${prefix}-icon ${prefix}-icon-refresh`"></i>
				<span ref="handlerImgSlot" :class="`${prefix}-drag-verify-imgBg__slot`" :style="{top: imgSlotY + 'px', height: captchaType === 'slider' ? '100%' : ''}">
					<img ref="imgSlot" :src="imgs.slot" :style="{height: captchaType === 'slider' ? '100%' : ''}" alt="">
				</span>
			</div>
		</transition>

		<div
			ref="DragVerify"
			:class="`${prefix}-drag-verify`"
			:style="dragVerifyStyle"
			@mousemove="dragMoving"
			@mouseup="dragFinish"
			@touchmove="dragMoving"
			@touchend="dragFinish"
		>
			<div
				:class="`${prefix}-drag-verify_bar`"
				ref="progressBar"
				:style="progressBarStyle"
			>

			</div>
			<div
				:class="`${prefix}-drag-verify_text`"
				:style="textStyle"
				ref="message"
			><span :data-nc="textData">{{message}}</span></div>

			<div
				:class="`${prefix}-drag-verify_handler ${prefix}-drag-verify_handler_bg`"
				@mousedown.stop="dragStart"
				ref="handler"
				:style="handlerStyle"
				@touchstart.stop="dragStart"
			>
				<i :class="handlerIconClass"></i>
			</div>

		</div>
	</div>

</template>
<script>
	import axios from 'axios'
	import { prefix } from '../../../../src/config'

	export default {
		name: 'FbDragVerifyImg',
		props: {
			// 图片请求地址
			action: {
				type: [Object, String],
				default: ""
			},
			// 禁用
			disabled: {
				type: Boolean,
				default: false
			},
			// 图片背景显示
			imgBoxShow: {
				type: Boolean,
				default: false
			},
			value: {
				type: [String],
				default: ''
			},
			width: {
				type: [String, Number],
				default: 312
			},
			height: {
				type: [String, Number],
				default: 36
			},
			text: {
				type: String,
				default: '向右拖动滑块填充拼图'
			},
			successText: {
				type: String,
				default: ''
			},
			background: {
				type: String,
				default: '#ccc'
			},
			progressBarBg: {
				type: String,
				default: 'rgba(246, 255, 236, 1)'
			},
			completedBg: {
				type: String,
				default: 'rgba(86, 209, 0, 1)'
			},
			completedBorderColor: {
				type: String,
				default: 'rgba(86, 209, 0, 1)'
			},
			completedColor: {
				type: String,
				default: '#fff'
			},
			circle: {
				type: Boolean,
				default: false
			},
			handlerIcon: {
				type: String,
				default: `next`
			},
			successIcon: {
				type: String,
				default: `selected`
			},
			failIcon: {
				type: String,
				default: `close`
			},
			failBg: {
				type: String,
				default: 'rgba(254, 242, 241, 1)'
			},
			failBorderColor: {
				type: String,
				default: 'rgba(251, 84, 78, 1)'
			},
			failDuration: {
				type: [String, Number],
				default: 400
			},
			handlerBg: {
				type: String,
				default: '#fff'
			},
			handlerColor: {
				type: String,
				default: '#666'
			},
			messageColor: {
				type: String,
				default: '#444'
			},
			textSize: {
				type: [String, Number],
				default: '14'
			},
			captchaType: {
				type: [String, Number],
				default: ''
			},
			xScale: {
				type: [Number],
				default: 1
			}
		},
		computed: {
			handlerStyle () {
				return {
					left: '0px',
					width: this.height  + 'px',
					height: this.height  + 'px',
					borderRadius: this.circle ? '50%' : '4px',
					background: this.handlerBg,
					borderColor: this.background
				}
			},
			message () {
				return this.status === 'success' ? this.successText : this.text
			},
			dragVerifyStyle () {
				return {
					width: this.width + 'px',
					height: this.height + 'px',
					lineHeight: this.height + 'px',
					background: this.background,
					borderRadius: this.circle ? this.height / 2 + 'px' : '4px'
				}
			},
			dragVerifyImgBgStyle () {
				return {
					width: this.width + 'px',
					bottom: this.height + 8 + 'px',
				}
			},
			progressBarStyle () {
				return {
					background: this.progressBarBg,
					height: this.height + 'px',
					borderRadius: this.circle ? this.height / 2 + 'px 0 0 ' + this.height / 2 + 'px' : '4px',
				}
			},
			textStyle () {
				return {
					height: this.height + 'px',
					width: this.width + 'px',
					fontSize: this.textSize + 'px'
				}
			},
			handlerIconClass () {
				if (this.handlerIcon) {
					return `${prefix}-icon ${prefix}-icon-${this.handlerIcon}`
				}
				return ''
			},
			successIconClass () {
				if (this.successIcon) {
					return `${prefix}-icon ${prefix}-icon-${this.successIcon}`
				}
				return ''
			},
			failIconClass () {
				if (this.failIcon) {
					return `${prefix}-icon ${prefix}-icon-${this.failIcon}`
				}
				return ''
			},
		},
		data () {
			return {
				prefix,
				isMoving: false,
				x: 0,
				textData: 'verify',

				status: this.value,
				imgShow: false,
				imgSlotY: 10,
				verification: '',
				imgs: {
					bg: '',
					slot: ''
				},

				// 导出对象
				captchaConfig: {
					moveEnd: 276,
					currentScale: 1,
					clientUuid: '',
					startSlidingTime: '',
					entSlidingTime: '',
					type: ''
				},
			}
		},
		watch: {
			//监听prop传的value，如果父级有变化了，将子组件的myValue也跟着变，达到父变子变的效果
			value(newVal) {
				this.status = newVal
			},
			status: {
				handler (newStatus) {
					this.$emit('input', newStatus)
					this.$nextTick(() => {
						if (newStatus === 'success') {
							this.successStatus()
							this.$emit('on-success', newStatus)
						} else if (newStatus === 'fail') {
							this.failStatus()
							this.$emit('on-fail', newStatus)
							// setTimeout(() => {
							// 	this.reset()
							// }, this.failDuration)
						} else if (!newStatus) {
							return
						} else {
							this.reset()
						}
					})
				},
				// immediate: true
			},
			imgBoxShow: {
				handler (newShow) {
					this.imgShow = newShow
				},
				immediate: true
			},
			disabled: {
				handler (newShow) {
					this.$nextTick(() => {
						let handler = this.$refs.handler
						if (newShow) {
							handler.style.cursor = 'not-allowed'
						} else {
							handler.style.cursor = 'move'
						}
					})
				},
				immediate: true
			}
		},
		mounted () {
			// this.init()
			document.addEventListener('keyup', this.closeImgDiv)
		},
		methods: {
			init () {
				this.status = ""
				this.httpRequestImg()
			},
			httpRequestImg () {
				if (typeof this.action === "string" && this.action) {
					const time = new Date().getTime()
					axios.get(this.action, {
						params: {t: time} // 防止ie缓存get请求
					}).then(res => {
						if (res.data && res.data.data) {
							if (this.captchaType === 'slider') {
								let { backgroundImage, sliderImage, y, clientUuid } = res.data.data
								this.$refs.imgBg.style.background = this.imgs.bg = '#fff url("data:image/jpg;base64,' + backgroundImage+'")'
							this.$refs.imgBg.style.backgroundSize = '100% 100%'
							this.$refs.imgBg.style.height = '184px'
							this.imgs.slot = "data:image/png;base64," + sliderImage
							this.imgSlotY = 0
							this.verification = clientUuid
							this.captchaConfig = res.data.data
							} else {
								let { shadeImage, cutoutImage, y, verification } = res.data.data
							this.$refs.imgBg.style.background = this.imgs.bg = '#fff url("data:image/jpg;base64,' + shadeImage+'")'
							this.imgs.slot = "data:image/png;base64," + cutoutImage
							this.imgSlotY = y
								this.verification = verification
							}
						} else {
							console.error('验证码数据获取失败:', res)
						}
					})
				}
			},
			dragStart (e) {
				if (!this.isMoving && this.status !== 'success' && !this.disabled) {
					this.isMoving = true;
					this.imgShow = true;
					var handler = this.$refs.handler;
					this.x = (e.pageX || e.touches[0].pageX) - parseInt(handler.style.left.replace('px', ''), 10);
					this.inStatus()
					this.captchaConfig.startSlidingTime = new Date()
					window.addEventListener('mouseup', this.dragFinish)
				}
			},
			dragMoving (e) {
				window.getSelection ? window.getSelection().removeAllRanges() : document.selection.empty();
				if (this.isMoving) {
					var _x = (e.pageX || e.touches[0].pageX) - this.x;
					var handler = this.$refs.handler;
					var handlerImgSlot = this.$refs.handlerImgSlot;
					if (_x > 0 && _x <= (this.width - this.height)) {
						handler.style.left = _x + 'px';
						handlerImgSlot.style.left = _x + 'px';
						this.$refs.progressBar.style.width = (_x + this.height / 2) + 'px';
					} else if (_x > (this.width - this.height)) {
						handler.style.left = (this.width - this.height) + 'px';
						// this.$refs.progressBar.style.width = (this.width - this.height / 2) + 'px';
						this.$refs.progressBar.style.width = this.width + 'px';
					}
				}

			},
			dragFinish (e) {
				if (this.isMoving) {
					this.isMoving = false;
					var _x = (e.pageX || e.changedTouches[0].pageX) - this.x;
					this.captchaConfig.entSlidingTime = new Date()
					// console.log(this.xScale)
					let x = this.captchaType === 'slider' ? Math.floor(_x / this.xScale) : Math.floor(_x)
					this.$emit('on-verify', {
						x,
						y: this.imgSlotY,
						verification: this.verification,
						...this.captchaConfig
					})
				}
				window.removeEventListener('mouseup', this.dragFinish)
			},
			// 进行中状态
			inStatus () {
				var handler = this.$refs.handler;
				let progressBar = this.$refs.progressBar
				handler.style.border = `1px solid ${this.completedBorderColor}`
				handler.children[0].style.color = this.completedBorderColor;
				progressBar.style.border = `1px solid ${this.completedBorderColor}`;
				progressBar.style.background = this.progressBarBg;
			},
			// 失败状态
			failStatus () {
				let handler = this.$refs.handler;
				let progressBar = this.$refs.progressBar
				handler.children[0].className = this.failIconClass;
				handler.children[0].style.color = this.completedColor;
				handler.style.border = `1px solid ${this.failBorderColor}`;
				handler.children[0].style.background = this.failBorderColor;
				progressBar.style.border = `1px solid ${this.failBorderColor}`;
				progressBar.style.background = this.failBg;
			},
			// 成功状态
			successStatus () {
				let handler = this.$refs.handler;
				let progressBar = this.$refs.progressBar
				handler.children[0].className = this.successIconClass;
				handler.children[0].style.color = this.completedColor;
				handler.style.border = `1px solid ${this.completedBorderColor}`;
				handler.children[0].style.background = this.completedBorderColor;
				progressBar.style.border = `1px solid ${this.completedBorderColor}`;
				progressBar.style.background = this.progressBarBg;
				this.isMoving = false;
				setTimeout(() => {
					this.imgShow = false
				}, 500)
			},
			reset () {
				this.$refs.handlerImgSlot.style.left = '0px';
				this.$refs.progressBar.style.border = `0px`;
				this.$refs.progressBar.style.width = '0px';
				var handler = this.$refs.handler;
				handler.style.left = '0px';
				handler.className += ' dv_handler_ok_bg';
				handler.style.borderColor = this.background;
				handler.children[0].className = this.handlerIconClass;
				handler.children[0].style.background = this.handlerBg;
				handler.children[0].style.color = this.handlerColor;
				this.$refs.progressBar.style.background = this.background;
				this.$refs.message.style.color = this.messageColor;
				this.textData = 'verify';
				this.status = '';
				this.init();
			},
			closeImgDiv (e) {
				let keyCode = e.keyCode || e.which
				if (keyCode == 27) {
					this.imgShow = false
				}
			}
		},
		beforeDestroy() {
			document.removeEventListener('keyup', this.closeImgDiv)
		},
		deactivated() {
			this.reset()
		},
	}
</script>
<style>

</style>

