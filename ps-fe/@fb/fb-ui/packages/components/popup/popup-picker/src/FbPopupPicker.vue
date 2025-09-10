<template>
	<transition :name="getTrClass">
		<div ref="popupPicker"
		     :class="getClass"
		     v-show="value">
			<slot></slot>
		</div>
	</transition>
</template>

<script>
	/**
	 * FbPopupPicker
	 * (c) 2020 lincong1987
	 */

	import { prefix } from '../../../../../src/config'

	export default {
		name: 'FbPopupPicker',
		props: {
			// 显示 管控 点击其他位置消失
			value: {
				type: Boolean,
				default: false,
			},
			// 显示位置
			position: {
				default: 'bottomLeft',
				validator (value) {
					// 这个值必须匹配下列字符串中的一个
					return [
						'',
						'bottomLeft',
						'bottomCenter',
						'bottomRight',
						'topLeft',
						'topCenter',
						'topRight',
					].indexOf(value) !== -1
				},
			},
			// 触发方式 -- 判断 绑定点击全局事件
			trigger: {
				default: '',
				validator (value) {
					// 这个值必须匹配下列字符串中的一个
					return ['', 'clickOnly'].indexOf(value) !== -1
				},
			},
		},
		data () {
			return {
				prefix,
				popupPosition: 'bottomLeft',
			}
		},
		computed: {
			getClass () {
				var arr = [`${this.prefix}-popup-picker`]

				arr.push(this.position || this.popupPosition)

				return arr
			},
			getTrClass () {
				if (this.position == 'bottomCenter' || this.position == 'topCenter') {
					return 'slide-to-down-center-x'
				} else {
					return 'slide-to-down'
				}
			},
			bindHandleHide () {
				if (this.trigger == 'clickOnly') {
					return false
				} else {
					return true
				}
			},
		},
		watch: {
			value (newVal) {
				this.$emit('input', newVal)
				if (this.bindHandleHide) {
					this.handleHide()
				}

				if (!this.position && this.value) {
					this.$nextTick(() => {
						this.calPosition()
					})
				}
			},
		},
		methods: {

			/**
			 * 计算位置
			 */
			calPosition () {
				// 可以选择的弹窗位置
				// bottomLeft | bottomRight | topLeft | topRight
				// 父元素的位置
				const {
					left, right, top, bottom,
				} = this.$parent.$el.getBoundingClientRect()

				// 得到滚动条的位置。
				const scrollTop = document.documentElement.scrollTop || window.pageYOffset || document.body.scrollTop
				const scrollLeft = document.documentElement.scrollLeft || window.pageXOffset || document.body.scrollLeft

				// 弹窗宽高，
				// 弹窗有动画，不能使用 getBoundingClientRect 获取宽高
				const contentW = this.$refs.popupPicker.offsetWidth
				const contentH = this.$refs.popupPicker.offsetHeight

				// 文档区域宽高
				const docW = document.documentElement.scrollWidth
				const docH = document.documentElement.scrollHeight

				const topSpace = top + scrollTop
				const rightSpace = docW - (left + scrollLeft)
				const bottomSpace = docH - (bottom + scrollTop)
				const leftSpace = right + scrollLeft

				// 优先级
				// bottomLeft | bottomRight | topLeft | topRight
				if (bottomSpace >= contentH) {
					if (rightSpace < contentW && leftSpace >= contentW) {
						this.popupPosition = 'bottomRight'
					} else {
						this.popupPosition = 'bottomLeft'
					}
				} else if (topSpace >= contentH) {
					if (rightSpace < contentW && leftSpace >= contentW) {
						this.popupPosition = 'topRight'
					} else {
						this.popupPosition = 'topLeft'
					}
				} else {
					this.popupPosition = 'bottomLeft'
				}
			},
			/**
			 * 点击后自动关闭
			 */
			handleHide () {
				if (this.value) {
					setTimeout(() => {
						document.addEventListener('click', this.hidePop)
					}, 0)
				} else {
					document.removeEventListener('click', this.hidePop)
				}
			},
			hidePop (e) {
				if (!this.$el.contains(e.target)) {
					this.$emit('input', false)
				}
			},
		},

		beforeDestroy () {
			document.removeEventListener('click', this.hidePop)
		},
	}
</script>

<style scoped>

</style>
