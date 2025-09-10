<template>
	<transition :name="`zoom-in-${position}`"
				@after-enter="afterEnter"
				@after-leave="afterLeave">

		<div v-show="isShow" :class="getClass" tabindex="-1">

			<div :class="`${prefix}-drawer-mask`" @click="close"></div>

			<!--			<div :class="`${prefix}-drawer`" :style="getDrawerStyle">-->
			<!--				<div :class="`${prefix}-drawer-card`">-->
			<!--					<template v-if="$slots.default">-->
			<!--						<slot></slot>-->
			<!--					</template>-->

			<!--					<template v-if="!$slots.default">-->
			<!--						<fb-card :header="title">-->
			<!--							<fb-button slot="actions" @on-click="close"   size="m" type="link"-->
			<!--							           icon="close"></fb-button>-->
			<!--							<slot></slot>-->
			<!--						</fb-card>-->
			<!--					</template>-->
			<!--				</div>-->
			<!--			</div>-->

			<div :class="`${prefix}-drawer`" :style="getDrawerStyle">
				<div :class="`${prefix}-drawer-card`">
					<template v-if="title">
						<fb-card v-bind="$attrs" body-overflow="auto"
								 :body-style="{height: `calc( 100% - ${title?44:0}px  - ${$slots.footer?60:0}px)`}">
							<template slot="title" v-if="title">
								{{ title }}
							</template>
							<template slot="actions" >
								<fb-button @on-click="close" size="m" type="link" v-if="showCloseBtn"
										   icon="close"></fb-button>
							</template>

							<template v-if="showContent">
								<slot></slot>
							</template>

							<template slot="footer" v-if="$slots.footer">
								<slot name="footer"></slot>
							</template>

						</fb-card>

					</template>
				</div>
			</div>
		</div>
	</transition>
</template>

<script>
import { prefix } from '../../../../src/config'
/**
 * FbDrawer
 * (c) 2020 lincong1987
 */

import { getSizeStyle } from '../../../utils/propUtils'
import { includes } from 'lodash-es'
import { addClass, removeClass } from '../../../utils/dom'
import FbCard from '../../card/src/FbCard'
import FbButton from '../../button/src/FbButton'

export default {
	name: 'FbDrawer',
	components: {
		FbButton,
		FbCard,
	},
	props: {
		value: {
			type: Boolean,
			default: false,
		},
		title: {
			type: String,
			default: undefined,
		},
		// 位置, 可选	'top' | 'right' | 'bottom' | 'left'
		position: {
			type: String,
			default: 'right',
		},
		// 自动显示
		autoShow: {
			type: Boolean,
			default: false,
		},
		// left right 时的宽度
		width: {
			type: [String, Number],
			default: 300,
		},
		// top bottom 时的搞度
		height: {
			type: [String, Number],
			default: 300,
		},

		esc: {
			type: Boolean,
			default: false,
		},

		lock: {
			type: Boolean,
			default: false,
		},

		// 防止外部 transform 对抽屉 fixed 的影响
		inBody: {
			type: Boolean,
			default: false,
		},

		showCloseBtn: {
			type: Boolean,
			default: true,
		},

		// 关闭前的回调
		beforeClose: {
			type: Function,
			default: undefined,
		},

		beforeOpen: {
			type: Function,
			default: undefined,
		},

	},
	data () {
		return {
			prefix,
			isShow: this.value,

			showContent: false,
		}
	},

	computed: {

		getDrawerStyle () {
			let arr = {}

			if (includes(['left', 'right'], this.position)) {
				arr.width = getSizeStyle(this.width)
			}
			if (includes(['top', 'bottom'], this.position)) {
				arr.height = getSizeStyle(this.height)
			}

			return arr
		},

		getClass () {
			let arr = [`${prefix}-drawer-wrapper`]

			arr.push(`${prefix}-drawer-wrapper--${this.position}`)

			return arr
		},
	},

	watch: {
		value (val) {
			console.log('val changed', val)
			if (val) {
				this.show()
			} else {
				this.hide()
			}
		},
	},

	methods: {

		afterEnter () {
			this.$emit('on-opened')
		},
		afterLeave () {
			this.$emit('on-closed')
		},

		hide () {
			this.$nextTick(() => {
				if (typeof this.beforeClose === 'function') {
					this.beforeClose(this.close)
				} else {
					this.close()
				}
			})
		},

		show () {
			this.showContent = true
			this.$nextTick(() => {

				if (typeof this.beforeOpen === 'function') {
					this.beforeOpen(this.open)
				} else {
					this.open()
				}
			})
		},

		open () {

			this.isShow = true
			this.$nextTick(() => {
				addClass(document.body, `${prefix}-drawer-wrapper-open`)
			})
			this.$emit('on-open')
			this.$emit('input', true)
		},

		close () {
			this.isShow = false
			removeClass(document.body, `${prefix}-drawer-wrapper-open`)

			this.$nextTick(() => {
				this.$emit('on-close')
				this.$emit('input', false)
			})
		},
		handleEsc (event) {

			if (!this.esc) {
				return
			}

			const keyCode = event.which || event.keyCode

			if (this.isShow === true && keyCode === 27) {
				this.close()
			}
		},

	},

	mounted () {

		document.addEventListener('keydown', this.handleEsc, false)

		if (this.isShow === true) {
			this.show()
		}

		if (this.inBody) {
			document.body.appendChild(this.$el)
		}

		if (this.autoShow === true) {
			this.show()
		}

	},
	beforeDestroy () {
		document.removeEventListener('keydown', this.handleEsc, false)
	},

	destroyed () {
		if (this.inBody && this.$el && this.$el.parentNode) {
			console.log('destroyed>removeChild')
			this.$el.parentNode.removeChild(this.$el)
		}
	},

}
</script>

<style scoped>

</style>
