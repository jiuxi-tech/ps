<template>
	<div :class="[
	  `${prefix}-card`,
	  `${prefix}-card--${shadow}-shadow`,
	  noBorder ? `${prefix}-card--no-border`: `${prefix}-card--border`,
	  noPadding ? `${prefix}-card--no-padding`: `${prefix}-card--padding`,
	  $slots.footer || $slots.footerActions ? `${prefix}-card--has-footer` : ''
	]"
		 :style="getCardStyle"
	>

		<div :class="`${prefix}-card__header`" v-if="showHeader">
			<div :class="`${prefix}-card__header__title`" :style="titleStyle">
				<slot name="title">{{ header }}</slot>
			</div>
			<div v-if="$slots.actions" :class="`${prefix}-card__header__actions`">
				<slot name="actions"></slot>
			</div>

			<slot name="header"></slot>
		</div>

		<div :class="`${prefix}-card__body`" :style="getBodyStyle">
			<slot></slot>
		</div>

		<div :class="`${prefix}-card__footer`" v-if="$slots.footer || $slots.footerActions">
			<slot name="footer"></slot>
		</div>
	</div>
</template>

<script>

import { prefix } from '../../../../src/config'
/**
 * FbCard
 * (c) 2020 lincong1987
 */

import { isArray, isObjectLike, isString, each } from 'lodash-es'

export default {
	name: 'FbCard',
	props: {
		header: {
			type: [String, Number],
			default: null,
		},
		shadow: {
			type: String,
			default: 'hover',
			validate (value) {
				return ['on', 'off', 'hover'].indexOf(value) != -1
			},
		},
		margin: {
			type: [String, Number],
			default: null,
		},

		// body 区域的高度,如果实际高度大于该值,将会出现滚动条
		height: {
			type: [String, Number],
			default: null,
		},

		// 是否显示边框
		noBorder: {
			type: Boolean,
			default: false,
		},
		// 显示内边距
		noPadding: {
			type: Boolean,
			default: false,
		},

		padding: {
			type: [String, Number],
			default: null,
		},

		bodyStyle: {
			type: [String, Object, Array],
			default: undefined,
		},

		// body内部是否滚动显示
		bodyOverflow: {
			type: [String],
			default: '',
		},

		titleStyle: {
			type: [String, Object, Array],
			default: undefined,
		},
	},

	data () {
		return {prefix}
	},

	computed: {
		getCardStyle () {
			let style = []

			if (this.height) {
				style.push({'height': typeof this.height === 'number' ? `${this.height}px` : this.height})
			}
			if (this.margin) {
				style.push({'margin': this.margin})
			}

			if (this.padding) {
				style.push({'padding': this.padding})
			}
			return style
		},

		getBodyStyle () {
			let style = []

			if (this.showHeader && this.height) {
				// card 内有 canvas 不显示问题
				style.push({'height': 'calc(100% - 44px)'})
			}

			if (this.bodyOverflow == 'y') {
				style.push({'overflowY': 'auto'})
			} else if (this.bodyOverflow == 'x') {
				style.push({'overflowX': 'auto'})
			} else if (this.bodyOverflow == 'auto') {
				style.push({'overflow': 'auto'})
			}

			if (this.bodyStyle) {

				if (isArray(this.bodyStyle)) {
					style.concat(this.bodyStyle)
				}

				if (isString(this.bodyStyle)) {
					style.push(this.bodyStyle)
				}

				if (isObjectLike(this.bodyStyle)) {
					style.push(this.bodyStyle)
				}

			}

			return style

		},

		showHeader () {

			if (this.header || this.$slots.header || this.$slots.title || this.$slots.actions) {
				return true
			}

			return false
		},
	},
}
</script>

<style scoped>

</style>
