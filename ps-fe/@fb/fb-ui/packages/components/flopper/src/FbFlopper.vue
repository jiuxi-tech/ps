<template>
	<div :class="[`${prefix}-flopper`, flipType, {[this.goName]: isFlipping}]" :style="flopperStyle" @click="handleClick">
		<div
			ref="front"
			class="digital front"
			:class="_textClass(frontTextFromData)"
		>
			<span class="before-s" :style="flopperSpanBeforeStyle">{{frontTextFromData}}</span>
			<span class="after-s" :style="flopperSpanAfterStyle">{{frontTextFromData}}</span>
		</div>
		<div
			ref="back"
			class="digital back"
			:class="_textClass(backTextFromData)"
		>
			<span class="before-s" :style="flopperSpanBeforeStyle">{{backTextFromData}}</span>
			<span class="after-s" :style="flopperSpanAfterStyle">{{backTextFromData}}</span>
		</div>
	</div>
</template>

<script>
	/**
	 * FbFlopper
	 * (c) 2020 lincong1987
	 */
	import {prefix} from '../../../../src/config'
	import {isEmpty, isNumber} from 'lodash-es'


	export default {
		name: 'FbFlopper',
		data() {
			return {
				prefix,
				isFlipping: false,
				frontTextFromData: 0,
				backTextFromData: 1
			}
		},
		props: {
			flipNum: {
				type: [Number, String],
				default: 0
			},
			flipType: {
				type: [String],
				default: "down"
			},
			flipStyle: {
				type: [Object, Array]
			},
			// front paper text
			// 前牌文字
			frontText: {
				type: [Number, String],
				default: 0
			},
			// back paper text
			// 后牌文字
			backText: {
				type: [Number, String],
				default: 1
			},
			// flipping duration, please be consistent with the CSS animation-duration value.
			// 翻牌动画时间，与CSS中设置的animation-duration保持一致
			duration: {
				type: Number,
				default: 600
			}
		},
		computed: {
			goName() {
				if (this.duration >= 600) return 'go'
				return 'go' + this.duration
			},
			flopperStyle() {
				let arr = []
				arr.push(this.flipStyle)
				return arr
			},
			flopperSpanBeforeStyle() {
				let {flipStyle} = this

				let arr = []
				if (flipStyle && flipStyle.background) {
					let bg = flipStyle.background.trim()
					if (bg.indexOf('rgb') >= 0) {
						bg = bg.slice(bg.indexOf('rgb'), bg.indexOf('),'))
					}
					if (bg.indexOf('#') >= 0) {
						if (bg.indexOf(',#') >= 0) {
							bg = bg.slice(bg.indexOf('#'), bg.indexOf(',#'))
						}
						if (bg.indexOf(', #') >= 0) {
							bg = bg.slice(bg.indexOf('#'), bg.indexOf(', #'))
						}
					}
					arr.push({background: bg})
				}
				return arr
			},
			flopperSpanAfterStyle() {
				let {flipStyle} = this

				let arr = []
				if (flipStyle && flipStyle.background) {
					arr.push({background: flipStyle.background})
				}
				return arr
			}
		},
		watch: {
			flipNum: {
				handler(newVal) {
					let num = parseFloat(newVal)
					if (num > 0) {
						this._flip(this.flipType, 0, 1)
					}
				},
				immediate: true,
			},
			isFlipping: {
				handler(newVal) {
					if (!newVal && this.frontTextFromData < this.flipNum) {
						setTimeout(() => {
							this._flip(this.flipType, this.frontTextFromData, this.backTextFromData + 1)
						}, 20)
					}
				},
			},
			frontText: {
				handler(newVal) {
					this.frontTextFromData = newVal
				},
			},
			backText: {
				handler(newVal) {
					this.backTextFromData = newVal
				},
			}
		},
		methods: {
			_textClass(number) {
				return 'number' + number
			},
			_flip(type, front, back) {
				// 如果处于翻转中，则不执行
				if (this.isFlipping) {
					return false
				}
				this.frontTextFromData = front
				this.backTextFromData = back
				// 根据传递过来的type设置翻转方向
				this.flipType = type
				// 设置翻转状态为true
				this.isFlipping = true
				setTimeout(() => {
					// 设置翻转状态为false
					this.isFlipping = false
					this.frontTextFromData = back
				}, this.duration)
			},
			// 下翻牌
			flipDown(front, back) {
				// console.log(front, back);
				this._flip('down', front, back)
			},
			// 上翻牌
			flipUp(front, back) {
				this._flip('up', front, back)
			},
			// 设置前牌文字
			setFront(text) {
				this.frontTextFromData = text
			},
			// 设置后牌文字
			setBack(text) {
				this.backTextFromData = text
			},
			handleClick(e) {
				this.$emit('on-click', this.frontTextFromData, this.backTextFromData, e)
			}
		},
		created() {
			this.frontTextFromData = this.frontText
			this.backTextFromData = this.backText
		}
	}
</script>

<style scoped>

</style>
