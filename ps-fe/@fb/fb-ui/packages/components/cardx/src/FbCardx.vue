<template>
	<div :class="getCardClass" :style="getCardStyle">
		<div :class="getHeaderClass" v-if="showHeader">
			<slot name="header"></slot>

			<div v-if="header || $slots.title" :class="`${prefix}-cardx__header__title`" :style="getHeaderTitleStyle">
				<slot name="title">{{header}}</slot>
			</div>

			<div v-if="$slots.tags" :class="`${prefix}-cardx__header__tags`">
				<slot name="tags"></slot>
			</div>


			<div v-if="$slots.actions" :class="`${prefix}-cardx__header__actions`">
				<slot name="actions"></slot>
			</div>

		</div>

		<div :class="`${prefix}-cardx__body`" :style="getBodyStyle">
			<slot></slot>
			<slot name="bodyCc"></slot>
		</div>

		<div :class="`${prefix}-cardx__footer`" v-if="$slots.footer || $slots.footerActions">
			<slot name="footer"></slot>
		</div>
	</div>
</template>

<script>

	import { prefix } from '../../../../src/config'
	import { addClass } from '../../../utils/utils'
	/**
	 * FbCard
	 * (c) 2020 lincong1987
	 */

	import {each, isArray, isObjectLike, isString} from 'lodash-es'

	export default {
		name: 'FbCardx',
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

			// 是否显示header边框
			noHeaderBorder: {
				type: Boolean,
				default: false,
			},

			// 显示内边距
			noPadding: {
				type: Boolean,
				default: false,
			},

			bodyStyle: {
				type: [String, Object, Array],
				default: undefined,
			},

			titleSize: {
				type: [String, Number],
				default: '20',
			},
		},

		data () {
			return {prefix}
		},

		computed: {
			getCardClass () {
				let cl = [`${prefix}-cardx`, `${prefix}-card--${this.shadow}-shadow`]

				if (this.noBorder) {
					cl.push(`${prefix}-cardx--no-border`)
				} else {
					cl.push(`${prefix}-cardx--border`)
				}

				if (this.noPadding) {
					cl.push(`${prefix}-cardx--no-padding`)
				} else {
					cl.push(`${prefix}-cardx--padding`)
				}

				if (this.$slots.footer || this.$slots.footerActions) {
					cl.push(`${prefix}-cardx--has-footer`)
				}

				return cl
			},

			getCardStyle () {
				let style = []

				if (this.height) {
					style.push({'height': this.height + 'px'})
				}

				return style
			},

			getHeaderClass () {
				let cl = [`${prefix}-cardx__header clearfix`]

				if (this.noHeaderBorder) {
					cl.push(`${prefix}-cardx__header--no-border`)
				}

				return cl
			},

			getBodyStyle () {
				let style = []

				if (this.showHeader && this.height) {
					// card 内有 canvas 不显示问题
					style.push({'height': 'calc(100% - 44px)'})
				}

				if (this.bodyStyle) {

					if (isObjectLike(this.bodyStyle)) {
						style.push(this.bodyStyle)
					}

					if (isArray(this.bodyStyle)) {
						each(this.bodyStyle, (val) => {
							style.push(val)
						})
					}

					if (isString(this.bodyStyle)) {
						let arr = this.bodyStyle.split(';')
						each(arr, (item) => {
							let keyVal = item.split(':')
							let obj = {}
							obj[keyVal[0]] = keyVal[1]
							style.push(obj)
						})
					}

				}

				return style

			},

			showHeader () {

				if (this.header || this.$slots.header || this.$slots.title || this.$slots.actions || this.$slots.tags) {
					return true
				}

				return false
			},

			getHeaderTitleStyle () {
				let style = []

				if (this.titleSize) {
					style.push({'fontSize': this.titleSize + 'px'})
				}

				return style
			}
		},

		methods: {
			initBodyCc () {
				if (this.$slots.bodyCc && this.$slots.bodyCc[0] && this.$slots.bodyCc[0].elm) {
					let bodyCc = this.$slots.bodyCc[0]
					addClass(bodyCc.elm, `${prefix}-cardx__body_cc`)
					addClass(bodyCc.elm, `${prefix}-cardx__body_cc_text`)
					addClass(bodyCc.elm, `clearfix`)

					if (bodyCc.data.attrs) {
						let attrs = bodyCc.data.attrs
						if (attrs.col) {
							addClass(bodyCc.elm, `col-${bodyCc.data.attrs.col}`)
						}

						if (attrs.bg) {
							bodyCc.elm.style.background = attrs.bg
						}

					}

				}
			}
		},

		mounted() {
			this.initBodyCc()
		},

		updated() {
			this.initBodyCc()
		}
	}
</script>

<style scoped>

</style>
