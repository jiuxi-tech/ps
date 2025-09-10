<template>
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
		><span :data-nc="textData">{{ message }}</span></div>

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
</template>
<script>

import {prefix} from '../../../../src/config'

export default {
	name: 'FbDragVerify',
	props: {
		value: {
			type: Boolean,
			default: false
		},
		width: {
			type: [Number, String],
			default: 312
		},
		height: {
			type: [Number, String],
			default: 36
		},
		text: {
			type: String,
			default: '请拖拽滑块到右侧验证'
		},
		successText: {
			type: String,
			default: '验证通过'
		},
		background: {
			type: String,
			default: '#ccc'
		},
		progressBarBg: {
			type: String,
			default: 'rgba(86, 209, 0, 0.7)'
		},
		completedBg: {
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
			type: [Number, String],
			default: '14'
		}

	},
	computed: {
		handlerStyle() {
			return {
				left: '0px',
				width: this.height + 'px',
				height: this.height + 'px',
				borderRadius: this.circle ? '50%' : '4px',
				background: this.handlerBg,
				borderColor: this.background
			}
		},
		message() {
			return this.isPassing ? this.successText : this.text
		},
		dragVerifyStyle() {
			return {
				width: this.width + 'px',
				height: this.height + 'px',
				lineHeight: this.height + 'px',
				background: this.background,
				borderRadius: this.circle ? this.height / 2 + 'px' : '4px'
			}
		},
		progressBarStyle() {
			return {
				background: this.progressBarBg,
				height: this.height + 'px',
				borderRadius: this.circle ? this.height / 2 + 'px 0 0 ' + this.height / 2 + 'px' : '4px'
			}
		},
		textStyle() {
			return {
				height: this.height + 'px',
				width: this.width + 'px',
				fontSize: this.textSize + 'px'
			}
		},
		handlerIconClass() {
			if (this.handlerIcon) {
				return `${prefix}-icon ${prefix}-icon-${this.handlerIcon}`
			}
			return ''
		},
		successIconClass() {
			if (this.successIcon) {
				return `${prefix}-icon ${prefix}-icon-${this.successIcon}`
			}
			return ''
		},
	},
	data() {
		return {
			prefix,
			isMoving: false,
			x: 0,
			isPassing: this.value,
			yt: null,
			yb: null,
			textData: 'verify'
		}
	},
	watch: {
		//监听prop传的value，如果父级有变化了，将子组件的myValue也跟着变，达到父变子变的效果
		value(newVal) {
			this.isPassing = newVal
			setTimeout(() => {
				// 快速拖拽后 选中文本取消
				window.getSelection ? window.getSelection().removeAllRanges() : document.selection.empty();
			}, 10)

			if (!newVal) {
				this.reset()
			}
		},
		//监听myValue，如果子组件中的内容变化了，通知父级组件，将新的值告诉父级组件，我更新了，父级组件接受到值后页就跟着变了
		isPassing(newVal) {
			this.$emit('input', newVal)
			setTimeout(() => {
				this.$emit("on-callback", newVal)
			}, 30)
		}
	},
	mounted() {
		this.init()
	},
	methods: {
		init() {
			if (this.isPassing) {
				this.$refs.handler.style.left = (this.width - this.height) + 'px';
				// this.$refs.progressBar.style.width = (this.width - this.height / 2) + 'px';
				this.$refs.progressBar.style.width = this.width + 'px';
				this.passVerify();
			}
			// 突然离开 移动 区域 滞留问题
			document.addEventListener('mouseup', this.dragDocFinish)
		},
		dragStart(e) {
			if (!this.isPassing) {
				this.isMoving = true;
				var handler = this.$refs.handler;
				this.x = (e.pageX || e.touches[0].pageX) - parseInt(handler.style.left.replace('px', ''), 10);
			}

		},
		dragMoving(e) {
			window.getSelection ? window.getSelection().removeAllRanges() : document.selection.empty();
			if (this.isMoving && !this.isPassing) {
				var _x = (e.pageX || e.touches[0].pageX) - this.x;
				var handler = this.$refs.handler;
				handler.style.borderColor = this.progressBarBg;
				this.$refs.progressBar.style.background = this.progressBarBg;
				if (_x > 0 && _x <= (this.width - this.height)) {
					handler.style.left = _x + 'px';
					this.$refs.progressBar.style.width = (_x + this.height / 2) + 'px';
				} else if (_x > (this.width - this.height)) {
					handler.style.left = (this.width - this.height) + 'px';
					// this.$refs.progressBar.style.width = (this.width - this.height / 2) + 'px';
					this.$refs.progressBar.style.width = this.width + 'px';
					this.passVerify();
				}
			}

		},
		dragFinish(e) {
			if (this.isMoving && !this.isPassing) {
				var _x = (e.pageX || e.changedTouches[0].pageX) - this.x;
				if (_x < (this.width - this.height)) {
					this.$refs.handler.style.left = '0';
					this.$refs.progressBar.style.width = '0';
					this.$refs.handler.style.borderColor = this.background;
				}
				this.isMoving = false;
			}
		},
		dragDocFinish(e) {
			if (this.isMoving && !this.isPassing) {
				var _x = (e.pageX || e.changedTouches[0].pageX) - this.x;
				if (_x < (this.width - this.height)) {
					this.$refs.handler.style.left = '0';
					this.$refs.progressBar.style.width = '0';
					this.$refs.handler.style.borderColor = this.background;
				}
				this.isMoving = false;
				// 解决手抖问题
				setTimeout(() => {
					if (!this.isPassing) {
						this.$refs.handler.style.left = '0';
						this.$refs.progressBar.style.width = '0';
						this.$refs.handler.style.borderColor = this.background;
					}
				}, 100)
			}
		},
		passVerify() {
			this.isPassing = true;
			this.isMoving = false;
			var handler = this.$refs.handler;
			handler.className += ' dv_handler_ok_bg';
			handler.style.borderColor = this.completedBg;
			handler.children[0].className = this.successIconClass;
			handler.children[0].style.background = this.completedBg;
			handler.children[0].style.color = this.completedColor;
			this.$refs.progressBar.style.background = this.completedBg;
			this.$refs.message.style.color = this.completedColor;
			this.textData = '';
			setTimeout(() => {
				this.$emit('on-success', true);
			}, 30)
			// 突然离开 移动 区域 滞留问题
			document.removeEventListener('mouseup', this.dragDocFinish)
		},
		reset() {
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
			this.init();
		},
	},
	beforeDestroy() {
		document.removeEventListener('mouseup', this.dragDocFinish)
	}
}
</script>
<style>

</style>
