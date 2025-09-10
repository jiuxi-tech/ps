<template>
	<div :class="[
		`${prefix}-loading-wrapper`,
		`${prefix}-loading-wrapper--position-${position}`
	]"
	     :style="{height: `${getSize}`,  'line-height': `${getSize}` }">
		<div v-if="type === 'icon'" :style="getStyle" :class="[
			`${prefix}-loading`,
			`${prefix}-icon`,
			`${prefix}-icon-${!spinner ? 'loading1': spinner}`,
			{
				[`${prefix}-loading--text`]: position=='bottom' && ($slots.text || text),
			}
		]"></div>

		<div v-if="type === 'css'" :style="getStyle" :class="[
			`${prefix}-loading`,
			{
				[`${prefix}-loading--text`]: position=='bottom' && ($slots.text || text),
			}
		]">

			<template v-if="css === 'orbit'">
				<div class="orbit-spinner" data-v-325a6afe="" style="height: 96px; width: 96px;"><div class="orbit one" data-v-325a6afe="" style="border-color: rgb(21, 78, 193); animation-duration: 1465ms;"></div><div class="orbit two" data-v-325a6afe="" style="border-color: rgb(21, 78, 193); animation-duration: 1465ms;"></div><div class="orbit three" data-v-325a6afe="" style="border-color: rgb(21, 78, 193); animation-duration: 1465ms;"></div></div>
			</template>


		</div>

		<div v-if="type === 'svg'" :style="getStyle" :class="[
			`${prefix}-loading`,
			{
				[`${prefix}-loading--text`]: position=='bottom' && ($slots.text || text),
			}
		]">

			<template v-if="svg === 'orbit'">

			</template>


		</div>

		<template v-if="$slots.text">
			<span :class="`${prefix}-loading__text`">
				<slot name="text"></slot>
			</span>
		</template>
		<template v-if="!$slots.text && text">
			<span :class="`${prefix}-loading__text`" :style="{
				color: textColor
			}">
				{{ text }}
				<template v-if="dot">
				</template>
			</span>
		</template>


		<slot></slot>
	</div>
</template>

<script>
/**
 * FbLoading
 * (c) 2020 lincong1987
 */

import { prefix } from '../../../../src/config'
import { assign, assignIn } from 'lodash-es'
import { getSizeStyle } from '../../../utils/propUtils'

export default {
	name: 'FbLoading',
	props: {
		// 颜色
		color: {
			type: String,
			default: '#0284FE',
		},
		// 文本颜色
		textColor: {
			type: String,
			default: undefined,
		},
		// 旋转器大小
		size: {
			type: String,
			default: '24px',
		},
		// 圈的宽度
		borderWidth: {
			type: String,
			default: '2px',
		},
		// 显示文字
		text: {
			type: [String, Number],
			default: undefined,
		},

		position: {
			type: String,
			default: 'right',
		},

		spinStyle: {
			type: [Object],
			default: undefined,
		},

		dot: {
			type: Boolean,
			default: false,
		},
		// 旋转器 icon
		spinner: {
			type: String,
			default: '',
		},
		type: {
			type: String,
			default: 'icon',
		},
		svg: {
			type: String,
			default: 'orbit',
		},
		css: {
			type: String,
			default: 'orbit',
		},
	},
	data () {
		return {
			prefix,
		}
	},
	computed: {

		getSize () {
			return getSizeStyle(this.size)
		},

		getStyle () {

			let _size = this.getSize

			let style = {
				height: _size,
				width: _size,
				fontSize: _size,
				color: this.color,
				// borderWidth: this.borderWidth,
				// borderStyle: 'solid',
			}

			// if (this.color) {
			// 	style.borderColor = `${this.color} ${this.color} transparent`
			// }

			if (this.spinStyle) {
				style = assignIn({}, style, this.spinStyle)
			}
			return style
		},
	},
}
</script>

<style scoped>

</style>
