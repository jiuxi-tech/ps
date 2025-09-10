<template>
	<div :class="[`${prefix}-rate-wrapper`, {
				[`${prefix}-rate-wrapper--focus`]: isFocused
	}]"

		 :tabindex="readonly ? -1 :tabindex"
		 @focus="isFocused = true"
		 @blur="isFocused = false"
		 @keydown="handleKeydown"
	>

		<fb-text v-if="prepend || $slots.prepend || $scopedSlots.prepend" :class="[`${prefix}-rate-prepend`]">
			<template v-if="$slots.prepend || $scopedSlots.prepend">
				<slot name="prepend" :star="currentStar"></slot>
			</template>
			<template v-else>
				{{ prepend }}
			</template>
		</fb-text>

		<div
			v-for="(star, index) in myData"
			:key="index"
			:class="[`${prefix}-rate`, {
						[`${prefix}-rate--readonly`]: readonly
					}]">
			<div :class="`${prefix}-rate-star`">
				<!-- 半星				-->
				<template v-if="half">
					<template v-if="tooltip">
						<fb-tooltip :content="`${star.halfStar.tooltip || star.halfStar.value}`" placement="top"
									:offset="5">
							<!-- 半星				-->
							<div :class="[`${prefix}-rate-star__half`, {
										[`${prefix}-rate-star__half--active`]: true
									}]"
								 @mouseover="handleMouseover(star.halfStar)"
								 @mouseout="handleMouseout(star.halfStar)"
								 @click="handleClick(star.halfStar)"
							>
								<template v-if="$slots.star || $scopedSlots.star">
									<fb-text
										:size="star.halfStar.size"
										:name="star.halfStar.icon"
										:color="getColor(star.halfStar)"
										:class="[`${prefix}-rate-star-slot-icon`]"
									>
										<slot name="star" :star="star.halfStar"></slot>
									</fb-text>
								</template>
								<template v-else>
									<fb-icon
										:size="star.halfStar.size"
										:name="star.halfStar.icon"
										:color="getColor(star.halfStar)"
										:class="[`${prefix}-rate-star-icon`]"
									/>
								</template>
								<fb-text
									:class="[`${prefix}-rate-star-label`]"
								>{{ star.halfStar.value }}
								</fb-text>
							</div>
						</fb-tooltip>
					</template>
					<template v-else>
						<div :class="[`${prefix}-rate-star__half`, {
						[`${prefix}-rate-star__half--active`]: true
					}]"
							 @mouseover="handleMouseover(star.halfStar)"
							 @mouseout="handleMouseout(star.halfStar)"
							 @click="handleClick(star.halfStar)"
						>
							<template v-if="$slots.star || $scopedSlots.star">
								<fb-text
									:size="star.halfStar.size"
									:name="star.halfStar.icon"
									:color="getColor(star.halfStar)"
									:class="[`${prefix}-rate-star-slot-icon`]"

								>
									<slot name="star" :star="star.halfStar"></slot>
								</fb-text>
							</template>
							<template v-else>
								<fb-icon
									:size="star.halfStar.size"
									:name="star.halfStar.icon"
									:color="getColor(star.halfStar)"
									:class="[`${prefix}-rate-star-icon`]"
								/>
							</template>

							<fb-text
								:class="[`${prefix}-rate-star-label`]"
							>{{ star.halfStar.value }}
							</fb-text>
						</div>
					</template>
				</template>

				<!-- 全星				-->
				<template v-if="tooltip">
					<fb-tooltip :content="`${star.tooltip || star.value}`" placement="top" :offset="5">
						<div :class="[`${prefix}-rate-star__full`, {
						[`${prefix}-rate-star_full--active`]: true
					}]"
							 @mouseover="handleMouseover(star)"
							 @mouseout="handleMouseout(star)"
							 @click="handleClick(star)"
						>
							<template v-if="$slots.star || $scopedSlots.star">
								<fb-text
									:size="star.size"
									:name="star.icon"
									:color="getColor(star)"
									:class="[`${prefix}-rate-star-slot-icon`]"
								>
									<slot name="star" :star="star"></slot>
								</fb-text>
							</template>
							<template v-else>
								<fb-icon
									:size="star.size"
									:name="star.icon"
									:color="getColor(star)"
									:class="[`${prefix}-rate-star-icon`]"
								/>
							</template>
							<fb-text
								:class="[`${prefix}-rate-star-label`]"
							>{{ star.value }}
							</fb-text>
						</div>
					</fb-tooltip>
				</template>
				<template v-else>
					<div :class="[`${prefix}-rate-star__full`, {
						[`${prefix}-rate-star_full--active`]: true
					}]"
						 @mouseover="handleMouseover(star)"
						 @mouseout="handleMouseout(star)"
						 @click="handleClick(star)"
					>
						<template v-if="$slots.star || $scopedSlots.star">
							<fb-text
								:size="star.size"
								:name="star.icon"
								:color="getColor(star)"
								:class="[`${prefix}-rate-star-slot-icon`]"
							>
								<slot name="star" :star="star"></slot>
							</fb-text>
						</template>
						<template v-else>
							<fb-icon
								:size="star.size"
								:name="star.icon"
								:color="getColor(star)"
								:class="[`${prefix}-rate-star-icon`]"
							/>
						</template>
						<fb-text
							:class="[`${prefix}-rate-star-label`]"
						>{{ star.value }}
						</fb-text>
					</div>
				</template>

			</div>

		</div>

		<fb-icon v-if="!readonly && clearable && activeValue > 0"
				 name="close-circle-fill"
				 :class="[`${prefix}-rate-close`]"
				 @on-click="handleClearClick"
		></fb-icon>

		<fb-text v-if="append || $slots.append || $scopedSlots.append" :class="[`${prefix}-rate-append`]">
			<template v-if="$slots.append || $scopedSlots.append">
				<slot name="append" :star="currentStar"></slot>
			</template>
			<template v-else>
				{{ append }}
			</template>
		</fb-text>

		<fb-text v-if="desc || $slots.desc || $scopedSlots.desc" :class="[`${prefix}-rate-desc`]">
			<template v-if="$slots.desc || $scopedSlots.desc">
				<slot name="desc" :star="currentStar"></slot>
			</template>
			<template v-else>
				{{ currentStar && currentStar.label }}
			</template>
		</fb-text>

	</div>
</template>

<script>
/**
 * FbRate
 * (c) 2022 lincong1987
 */
import { defaults } from 'lodash-es'
import { prefix } from '../../../../src/config'
import FbText from '../../text/src/FbText'
import FbIcon from '../../icon/src/FbIcon'
import FbInput from '../../input/src/FbInput'
import FbButton from '../../button/src/FbButton'
import FbTooltip from '../../tooltip/src/tooltip'

export default {
	name: 'FbRate',
	components: {
		FbTooltip,
		FbButton,
		FbInput,
		FbIcon,
		FbText,
	},
	props: {

		/**
		 * 当前值
		 */
		value: {
			type: [Number, String],
			default: 0,
		},

		/**
		 * 是否显示半数
		 */
		half: {
			type: Boolean,
			default: false,
		},

		/**
		 * 尺寸
		 * 支持单一数值及数组
		 */
		size: {
			type: [String, Number, Array],
			default: 24,
		},

		/**
		 * 最大数量
		 */
		maxLength: {
			type: [String, Number],
			default: 5,
		},

		/**
		 * 激活项颜色
		 */
		activeColor: {
			type: [String, Array],
			default: '#FADB14',
		},

		/**
		 * 鼠标移动时高亮颜色
		 */
		hoverColor: {
			type: [String, Array],
			default: '#fadb14',
		},

		/**
		 * 不活动项的颜色
		 */
		inActiveColor: {
			type: [String, Array],
			default: '#F0EFF5',
		},

		/**
		 * 图标，支持单一图标或图标数组
		 */
		icon: {
			type: [String, Array],
			default: 'star-fill',
		},

		/**
		 * 分值描述数组
		 */
		labels: {
			type: [Array],
			default: () => null,
		},

		/**
		 * 分值数组
		 */
		values: {
			type: [Array],
			default: () => null,
		},

		/**
		 * 节点数据, 暂不支持
		 */
		data: {
			type: [Array],
			default: undefined,
		},

		/**
		 * 是否只读
		 */
		readonly: {
			type: Boolean,
			default: false,
		},

		/**
		 * 可清除
		 * 在当前激活分值上再次点击，可以取消分值
		 */
		clearable: {
			type: Boolean,
			default: false,
		},

		/**
		 * 前缀内容
		 */
		prepend: {
			type: String,
			default: undefined,
		},

		/**
		 * 后缀内容
		 */
		append: {
			type: String,
			default: undefined,
		},

		/**
		 * 是否显示评分值描述
		 */
		desc: {
			type: Boolean,
			default: false,
		},

		/**
		 * 是否在每个分值上显示 tooltip
		 * 当类型为数组时，可以显示每个元素对应的内容
		 */
		tooltip: {
			type: [Boolean, Array],
			default: false,
		},

		/**
		 * 键盘表单顺序值
		 */
		tabindex: {
			type: Number,
			default: 0,
		},
	},
	data () {

		return {
			prefix,
			// 最终值
			activeValue: this.value,
			hoverValue: -1,
			myData: this.genMyData(),
			// 数据来源模式 maxLength | data
			// 如果有 maxLength，那区间就是[1-maxLength]，
			// 如果有 data，那区间便是[1-data.length]
			dataMode: 'maxLength',

			hoverStar: null,

			hoverIndex: -1,

			isFocused: false,
		}
	},

	computed: {
		currentStar () {
			let star = null
			if (this.activeIndex >= 0) {
				star = this.getStarByIndex(this.activeIndex)
			}
			if (this.hoverIndex >= 0) {
				star = this.getStarByIndex(this.hoverIndex)
			}
			return star
		},

		activeIndex () {
			if (this.hoverIndex !== -1) {
				return -1
			}
			let star = this.getStarByValue(this.activeValue)
			if (star && star.index === 0) {
				return 0
			}
			return star && star.index || -1
		},
	},

	watch: {
		value (val, old) {
			this.activeValue = val
		},

	},

	methods: {

		//
		toInteger (val) {
			let num = 0

			if (typeof val === 'string') {
				return parseInt(val, 10)
			}

			if (typeof val === 'number') {
				return val
			}

			return num < 0 ? 0 : num
		},

		mixStar (index, defaultStar = {}, defaultProps = {}) {
			let star = defaults({}, defaultStar)

			if (Array.isArray(this.inActiveColor)) {
				star.inActiveColor = this.inActiveColor[index]
			}
			if (Array.isArray(this.activeColor)) {
				star.activeColor = this.activeColor[index]
			}
			if (Array.isArray(this.hoverColor)) {
				star.hoverColor = this.hoverColor[index]
			}
			if (Array.isArray(this.icon)) {
				star.icon = this.icon[index]
			}
			if (Array.isArray(this.size)) {
				star.size = this.size[index]
			}
			if (typeof defaultProps.label !== 'undefined') {
				star.label = defaultProps.label
			}
			if (Array.isArray(this.labels)) {
				star.label = this.labels[index]
			}
			if (typeof defaultProps.value !== 'undefined') {
				star.value = defaultProps.value
			}
			if (Array.isArray(this.values)) {
				star.value = this.values[index]
			}

			if (Array.isArray(this.tooltip)) {
				star.tooltip = this.tooltip[index]
			}

			return star
		},

		genMyData () {

			let data = []

			if (this.toInteger(this.maxLength)) {
				for (let i = 0; i < this.toInteger(this.maxLength); i++) {
					let star = {}
					star.inActiveColor = this.inActiveColor
					star.activeColor = this.activeColor
					star.hoverColor = this.hoverColor
					star.icon = this.icon
					star.size = this.size
					star.index = i
					star.half = !!this.half
					star.value = i + 1
					star.label = i + 1
					star = {
						...this.mixStar(star.index, star, {
							label: i + 1,
							value: i + 1,
						}),
					}

					if (this.half) {
						star.halfStar = {
							...this.mixStar(i * 2, star, {
								label: i + 0.5,
								value: i + 0.5,
							}),
							index: i * 2,
						}

						star.index = i * 2 + 1
						star = {...this.mixStar(i * 2 + 1, star)}
					}

					data.push(star)
				}

			} else if (this.data) {

				data = this.data.map((i) => {
					let star = {}
					star.inActiveColor = this.inActiveColor
					star.activeColor = this.activeColor
					star.hoverColor = this.hoverColor
					star.icon = this.icon
					star.size = this.size
					star.index = i

					if (this.half) {
						star.halfStar = {
							...star,
							value: i + 0.5,
							label: i + 0.5,
							index: i * 2,
						}
						star.index = i * 2 + 1
					}

					star.half = !!this.half
					star.value = i + 1
					star.label = i + 1

					return star
				})

			}

			return data

		},
		getStarByValue (value) {

			let star = this.myData.find(n => {
				if (n.value === value) {
					return true
				}
			})

			let halfStar = this.myData.find(n => {
				if (n.halfStar && n.halfStar.value === value) {
					return true
				}
			})
			return halfStar && halfStar.halfStar || star
		},

		getStarByIndex (index) {

			let halfStar = this.myData.find(n => {
				if (n.halfStar && n.halfStar.index === index) {
					return true
				}
			})

			if (halfStar && halfStar.halfStar) {
				return halfStar.halfStar
			}

			let star = this.myData.find(n => {
				if (n.index === index) {
					return true
				}
			})
			if (star) {
				return star
			}
		},

		getColor (star) {
			let color = star.inActiveColor
			if (this.activeIndex >= 0 && this.activeIndex >= star.index) {
				color = star.activeColor
			}
			if (this.hoverIndex >= 0 && this.hoverIndex >= star.index) {
				color = star.hoverColor
			}
			return color
		},

		clearValue () {
			this.changeValue({value: 0})
		},

		changeValue (star) {
			if (this.readonly) {
				return
			}

			let modelValue = star.value
			if (this.clearable && star.value === this.activeValue) {
				modelValue = 0
			}

			this.activeValue = modelValue

			this.$nextTick(() => {
				this.$emit('on-change', modelValue)
				this.$emit('input', modelValue)
			})
		},

		handleMouseover (star) {
			if (this.readonly) {
				return
			}
			this.hoverIndex = star.index
			this.hoverValue = star.value
		},
		handleMouseout (star) {
			if (this.readonly) {
				return
			}
			this.hoverIndex = -1
			this.hoverValue = ''
		},
		/**
		 * 点击事件
		 * @param star
		 */
		handleClick (star) {
			this.changeValue(star)
			this.$emit('on-click', star)
		},
		/**
		 * 键盘事件
		 * @param e
		 * @returns {boolean}
		 */
		handleKeydown (e) {
			if (this.readonly) {
				return false
			}

			// 右 +
			if (e.keyCode === 39) {
				if (this.currentStar) {
					let nextStar = this.getStarByIndex(this.currentStar.index + 1)
					if (nextStar) {
						this.changeValue(nextStar)
					}
				} else {
					this.changeValue(this.getStarByIndex(0))
				}
			}

			// 左 -
			if (e.keyCode === 37) {
				if (this.currentStar) {
					if (this.currentStar.index === 0) {
						this.changeValue({value: 0})
					} else {
						let prevStar = this.getStarByIndex(this.currentStar.index - 1)
						if (prevStar) {
							this.changeValue(prevStar)
						}
					}

				}
			}
		},

		/**
		 * 清空按钮点击
		 */
		handleClearClick () {
			this.clearValue()
		},
	},
}
</script>

<style scoped>

</style>
