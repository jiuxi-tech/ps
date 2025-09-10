<template>
	<div ref="notification" :class="getClass" :style="{width: getSizeStyle(width)}">
		<div :class="`${prefix}-notification__header`">

			<div :class="`${prefix}-notification__title`">
				<fb-icon
					:class="[
							`${prefix}-notification__title__icon`,
							`${prefix}-notification__title__icon--${type}`,
						]"
					v-if="(myType || icon) && iconShow"
					:name="myType"
					size="xl"
					:rotating="type == 'loading'"
					:color="iconColor"
				/>
				<span :class="`${prefix}-notification__title__main`">{{ title }}</span>
			</div>
		</div>

		<fb-icon name="close" size="s" :class="`${prefix}-notification__close`"
				 @on-click="handleCloseIconClick"
		></fb-icon>

		<div :class="`${prefix}-notification__content`">
			<pre :class="`${prefix}-notification__message`" style="overflow: auto; ">{{ message }}</pre>
		</div>

		<div
			:class="[`${prefix}-notification__actions`]" v-show="showOkButton">
			<fb-button
				type="primary"
				@on-click="()=>handleAction('ok')">
				{{ okButtonText }}
			</fb-button>
		</div>
	</div>
</template>

<script>
import FbButton from '../../button/src/FbButton'
import FbIcon from '../../icon/src/FbIcon'
import { getSizeStyle } from '../../../utils/propUtils'
import { closest } from '../../../utils/componentUtils'
import { prefix } from '../../../../src/config'

/**
 * FbNotification
 * (c) 2020 lincong1987
 */
export default {
	name: 'FbNotification',
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
			default: false,
		},

		// 确定按钮文字
		okButtonText: {
			type: String,
			default: '确定',
		},

		// 确定 回调
		callback: {
			type: Function,
			default: () => {},
		},

		icon: {
			type: String,
			default: null,
		},

		iconColor: {
			type: String,
			default: null,
		},

		iconShow: {
			type: Boolean,
			default: true,
		},

	},

	data () {
		return {
			prefix,
			action: '',
			visible: false,
			wrapper: closest(this, 'FbNotificationWrapper'),
			icons: {
				alert: 'information',
				info: 'information',
				confirm: 'warning',
				success: 'selected-circle',
				error: 'close-circle',
				danger: 'close-circle',
				warning: 'warning',
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

		getClass () {
			let arr = [`${this.prefix}-notification`, `${this.prefix}-notification-type-${this.type}`]

			if ((this.myType || this.icon) && this.iconShow) {
				arr.push(`${this.prefix}-notification--has-icon`)
			} else {
				arr.push(`${this.prefix}-notification--no-icon`)
			}

			if (this.title) {
				arr.push(`${this.prefix}-notification--has-title`)
			} else {
				arr.push(`${this.prefix}-notification--no-title`)
			}

			return arr
		},
	},

	watch: {

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

				}

				this.$emit('on-close')
			})
		},

		handleCloseIconClick () {
			this.$emit('on-close')
		},

	},

	mounted () {

	},

}
</script>

<style scoped>

</style>
