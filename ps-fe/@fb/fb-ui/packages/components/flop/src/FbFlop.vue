<template>
	<div :class="`${prefix}-flop`" @click="handleClick">
		<div v-if="$slots.prefix || brandPrefix" :class="`${prefix}-flop-prefix`">
			<slot name="prefix">{{ brandPrefix }}</slot>
		</div>

		<ul v-if="type === 'brand'" ref="flopBox"
		    :class="[`${prefix}-flop-brand-ul`, `${prefix}-flop-brand-ul-${brandSize}`, `${prefix}-flop-brand-ul__${theme}`]"
		    :style="{height: brandStyle.height}">
			<li v-for="(num, i) in list" :key="i">

				<div v-if="!noPlay" :class="[
					`${prefix}-flop-brand-span`,
					`${prefix}-flop-brand-span__size_${brandSize}`,
					`${prefix}-flop-brand-span__${brandStatus}`]"
				     :style="brandSpanStyle">
					<div
						class="tt"
						:style="{transition: `all ${num===0?(duration/2):duration}ms ease-in-out 0s`,top:'-'+num.top+'px'}"
					>
						<span v-for="(item2, index2) in numList" :key="index2">{{ item2 }}</span>
					</div>
				</div>

				<span v-else :class="[
					`${prefix}-flop-brand-span`,
					`${prefix}-flop-brand-span__size_${brandSize}`,
					`${prefix}-flop-brand-span__${brandStatus}`]"
				      :style="brandSpanStyle">
					{{ num.value }}
				</span>
			</li>
		</ul>

		<ul v-if="type === 'flip'" ref="flopBox"
		    :class="[`${prefix}-flop-brand-ul`, `${prefix}-flop-brand-ul-${brandSize}`, `${prefix}-flop-brand-ul__${theme}`]"
		    :style="{height: brandStyle.height}">
			<li v-for="(num, i) in list" :key="i">
				<FbFlopper :ref="`flip-${i}`" :flipNum="noPlay ? 0 : num.value"
				           :frontText="noPlay ? num.value : !isNumber(num.value) ? num.value : 0"
				           :backText="!isNumber(num.value) ? num.value : 1"
				           :flipStyle="brandStyle"
				           :flipType="flipType"
				           :duration="duration"
				           :class="[
					`${prefix}-flop-brand-span`,
					`${prefix}-flop-brand-span__size_${brandSize}`,
					`${prefix}-flop-brand-span__${brandStatus}`]">
				</FbFlopper>
			</li>
		</ul>

		<ul v-if="type === 'simple'" ref="flopBox"
			:class="[`${prefix}-flop-brand-ul`, `${prefix}-flop-brand-ul-${brandSize}`, `${prefix}-flop-brand-ul-${type}`, `${prefix}-flop-brand-ul__${theme}`]"
			:style="{height: brandStyle.height}">
			<li>
				<span :class="[
					`${prefix}-flop-brand-span`,
					`${prefix}-flop-brand-span__size_${brandSize}`,
					`${prefix}-flop-brand-span__${brandStatus}`]"
					  :style="brandSpanStyle">
					<fb-number v-if="!noPlay"
							   :end="num"
							   v-bind="{
									...numberProps
								}"/>
					<span v-else>{{ num }}</span>
				</span>
			</li>
		</ul>

		<div v-if="$slots.suffix || brandSuffix" :class="`${prefix}-flop-suffix`">
			<slot name="suffix">{{ brandSuffix }}</slot>
		</div>
	</div>
</template>

<script>
/**
 * FbResult
 * (c) 2020 lincong1987
 */
import { prefix } from '../../../../src/config'
import { isEmpty } from 'lodash-es'
import FbFlopper from '../../flopper/src/FbFlopper'
import FbNumber from "../../number/src/FbNumber";

export default {
	name: 'FbFlop',
	components: {
		FbFlopper,
		FbNumber,
	},
	props: {
		type: {
			type: [String],
			default: 'brand',
		},
		num: {
			type: [Number, String],
			default: 0,
		},
		brandPrefix: {
			type: [Number, String],
		},
		brandSuffix: {
			type: [Number, String],
		},
		// 补位 length
		minLength: {
			type: [Number, String],
			default: 0,
		},
		// 限制 length
		maxLength: {
			type: [Number, String],
			default: 0,
		},
		brandSize: {
			type: [String],
			default: 'm',
		},
		brandStatus: {
			type: [String],
			default: 'default',
		},
		brandStyle: {
			type: [Object, Array],
			default: () => {
				return {}
			},
		},
		noPlay: {
			type: [Boolean],
			default: false,
		},
		duration: {
			type: Number,
			default: 1200,
		},
		flipType: {
			type: [String],
			default: 'down',
		},
		// 翻牌顺序
		reverse: {
			type: [Boolean],
			default: true,
		},
		theme: {
			type: [String],
			default: undefined,
		},
		size: {
			type: [String],
			default: undefined,
		},
		brandHeight: {
			type: Number,
			default: 0,
		},
		// panel 动画延迟时间
		panelDelay: {
			type: Number,
			default: 350,
		},
		numberProps: {
			type: [Object],
			default: () => ({}),
		}
	},
	data () {
		return {
			prefix,
			list: [],
			numList: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, '.'],
			queue: [],
			themes: {
				red: {
					background: 'linear-gradient(#7D2FD1, #A371EA)',
				},
				'red-lg': {
					background: 'linear-gradient(#7D2FD1, #A371EA)',
				},
			},
		}
	},
	watch: {
		numArr: {
			handler (val) {
				// this.queue.push(this.num)
				this.queue = [this.num]
				this.animate()
			},
		},
	},
	computed: {
		numArr () {
			let str = '' + this.num
			let arr = str.split('')

			let minLength = parseInt(this.minLength, 10)
			if (minLength && minLength > arr.length) {
				let len = minLength - arr.length
				for (let i = 0; i < len; i++) {
					arr.unshift(0)
				}
			}

			let maxLength = parseInt(this.maxLength, 10)
			if (maxLength && maxLength < arr.length) {
				let len = arr.length - maxLength
				arr.splice(0, len)
			}

			return arr
		},
		brandSpanStyle () {
			let style = []
			if (!isEmpty(this.brandStyle)) {
				style.push(this.brandStyle)
			}
			return style
		},
		// 动态计算翻牌器每次移动高度，防止父级动画延迟 dom 未生成 height
		brandComputeHeight() {
			let height = 0
			if (this.theme && this.theme.includes('-s')) {
				height = 32
			}
			if (this.theme && this.theme.includes('-m')) {
				height = 36
			}
			if (this.theme && this.theme.includes('-l')) {
				height = 44
			}
			if (this.theme && this.theme.includes('-xl')) {
				height = 60
			}
			if (this.brandHeight !== 0) {
				height = this.brandHeight
			}
			console.log(height)
			return height
		}
	},
	created () {
		this.timer = null
	},

	mounted () {
		this.queue.push(this.num)
		this.animate()
	},

	beforeDestroy () {
		clearTimeout(this.timer)
	},

	methods: {

		format (num) {
			let str = '' + num
			let arr = str.split('')

			let minLength = parseInt(this.minLength, 10)
			if (minLength && minLength > arr.length) {
				let len = minLength - arr.length
				for (let i = 0; i < len; i++) {
					arr.unshift(0)
				}
			}

			let maxLength = parseInt(this.maxLength, 10)
			if (maxLength && maxLength < arr.length) {
				let len = arr.length - maxLength
				arr.splice(0, len)
			}

			return arr
		},

		animate () {
			this.$nextTick(() => {
				if (this.type === 'brand') {
					this.scroll()
				} else if (this.type === 'flip') {
					this.flipScroll()
				}
			})
		},
		scroll () {

			// this.animate()
			if (this.queue.length === 0) {
				return
			}

			this.list = this.format(this.queue[0])

			let arr = []

			this.list.forEach((value) => {
				arr.push({
					value: value,
					top: 0,
				})
			})

			this.list = arr

			if (this.noPlay) return

			if (this.$refs['flopBox']) {
				let offsetHeight = parseFloat(this.$refs['flopBox'].offsetHeight || this.brandComputeHeight)
				let listLength = this.list.length
				this.list.forEach((item, index) => {
					setTimeout(() => {
						if (!this.isNumber(item.value)) {
							this.numList.push(item.value)
							let idx = this.numList.indexOf(item.value)
							item.top = parseFloat(idx * offsetHeight)
						} else {
							item.top = parseFloat((item.value * offsetHeight))
						}
					}, (this.reverse ? (listLength - index - 1) : index) * 100)
				})

			}

			this.timer = setTimeout(() => {
				this.queue.splice(0, 1)
				this.scroll()
			}, this.duration)

		},
		flipScroll () {
			this.list = this.numArr

			let arr = []

			this.list.forEach((value) => {
				arr.push({
					value: value,
					top: 0,
				})
			})

			this.list = arr

			if (this.noPlay) return

			this.list.forEach((item, index) => {
				setTimeout(() => {
					if (!this.isNumber(item.value)) {
						this.$refs[`flip-${index}`][0]._flip(this.flipType, item.value, item.value)
					}
				}, index * 100)
			})
		},
		isNumber (val) {
			if (Number(val) + '' !== 'NaN') {
				return true
			} else {
				return false
			}
		},
		handleClick (e) {
			this.$emit('on-click', this.num, e)
		},
	},
}
</script>

<style scoped>

</style>
