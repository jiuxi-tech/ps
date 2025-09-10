<template>

	<div ref="msgbox" :class="`${prefix}-message-box`" :style="{width: getSizeStyle(width)}">
			<div :class="`${prefix}-message-box__header`">

				<div :class="`${prefix}-message-box__title`" :style="titleStyle">
					<fb-icon
						:class="[
							`${prefix}-message-box__title__icon`,
							`${prefix}-message-box__title__icon--${type}`,
						]"
						v-if="myType"
						:name="myType"
						:size="20"
						:rotating="type == 'loading'"
					/>
					<span :class="`${prefix}-message-box__title__main`">{{ title }}</span>
				</div>

				<div v-if="showClose" :class="`${prefix}-message-box__close`" @click="handleAction('cancel')">
					<fb-icon name="close"></fb-icon>
				</div>
			</div>

			<div :class="`${prefix}-message-box__content`">
				<pre :class="`${prefix}-message-box__message`"
				>{{ message }}</pre>
			</div>

			<div
				:class="[`${prefix}-message-box__actions`]">

				<div :class="`${prefix}-message-box__actions__${align}`">

					<fb-button v-if="showConfirmButton"
					           type="primary"
					           @on-click="handleAction('confirm')">
						{{ confirmButtonText }}
					</fb-button>

					<fb-button v-if="showOkButton"
					           type="primary"
					           @on-click="handleAction('ok')">
						{{ okButtonText }}
					</fb-button>

					<fb-button v-if="showCancelButton"

					           @on-click="handleAction('cancel')">
						{{ cancelButtonText }}
					</fb-button>
				</div>

			</div>
		</div>

</template>

<script>
import FbButton from '../../button/src/FbButton'
import FbIcon from '../../icon/src/FbIcon'
import { getSizeStyle } from '../../../utils/propUtils'
import { prefix } from '../../../../src/config'

/**
 * FbMessageBox
 * (c) 2020 lincong1987
 */
export default {
	name: 'FbMessageBox',
	components: {
		FbIcon,
		FbButton,
	},
	props: {
		// 标题
		title: {
			type: String,
			default: '',
		},
		titleStyle: {
			type: [String, Object, Array],
			default: '',
		},
		// 对齐
		align: {
			type: String,
			default: 'right',
		},
		// 消息
		message: {
			type: [String, Number],
			default: null,
		},
		// width
		width: {
			type: [String, Number],
			default: 420,
		},
		// 类型
		type: {
			type: String,
			default: null,
		},
		// 显示确定按钮
		showOkButton: {
			type: Boolean,
			default: true,
		},
		// 确定按钮文字
		okButtonText: {
			type: String,
			default: '确定',
		},
		// 显示确认按钮
		showConfirmButton: {
			type: Boolean,
			default: false,
		},
		// 确认按钮文字
		confirmButtonText: {
			type: String,
			default: '确认',
		},
		// 显示取消按钮
		showCancelButton: {
			type: Boolean,
			default: false,
		},
		// 取消按钮文字
		cancelButtonText: {
			type: String,
			default: '取消',
		},

		// 显示右上角 关闭图标
		showClose: {
			type: Boolean,
			default: false,
		},

		// 取消回调
		fallback: {
			type: Function,
			default () {
			},
		},

		// 确定 确认 回调
		callback: {
			type: Function,
			default () {
			},
		},
		icon: {
			type: [String],
			default: 'info',
		},
	},

	data () {
		return {
			prefix,
			action: '',
			visible: false,
			icons: {
				alert: 'information-fill',
				info: 'information-fill',
				confirm: 'warning-fill',
				success: 'selected-circle-fill',
				error: 'close-circle-fill',
				danger: 'close-circle-fill',
				warning: 'warning-fill',
				loading: 'loading2',
			},
		}
	},

	computed: {
		myType () {
			if (this.type) {
				return this.icons[this.type]
			}
			return this.icon
		},
	},

	methods: {
		getSizeStyle,

		handleAction (action) {
			this.action = action

			this.doClose()

		},

		doClose () {

			this.onClose && this.onClose()

			setTimeout(() => {
				if (this.action) {

					if (this.action === 'ok' || this.action === 'confirm') {
						this.callback && this.callback()
					}
					if (this.action === 'cancel') {
						this.fallback && this.fallback()
					}

				}

				this.$parent.hide()
			})
		},

		esc (event) {
			// 禁止esc键
			// if (this.disableEsc) return
			const which = event.which || event.keyCode

			if (which === 27) {
				this.doClose()
			}
		},

	},

	mounted () {

		document.addEventListener('keydown', this.esc, false)
	},

	beforeDestroy () {
		document.removeEventListener('keydown', this.esc, false)
		//.unbind(this.$refs.dialog)
	},

}
</script>

<style scoped>

</style>
