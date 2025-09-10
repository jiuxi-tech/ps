<template>
	<button
		:name="name"
		:value="value"
		:type="elType"
		v-ripple
		:class="[
			  `${prefix}-button`,
			  // 类型
		      type ? `${prefix}-button--` + type : '',
		      // 尺寸
		      size ? `${prefix}-button--` + size : '',
		      long ? `${prefix}-button--long` :'',

			  // 辅助样式
		      $slots.default? `${prefix}-button--has-value` :`${prefix}-button--no-value`,
		      myIcon||loading? `${prefix}-button--has-icon` :`${prefix}-button--no-icon`,
		      appendIcon? `${prefix}-button--has-append-icon` :`${prefix}-button--no-append-icon`,

		      {
		         // danger
		        [`${prefix}-button--danger`]: danger,
		         // danger
		        [`${prefix}-button--warning`]: warning,
		         // danger
		        [`${prefix}-button--success`]: success,
		         // editor
		        [`${prefix}-button--editor`]: editor,
		        // 禁用
		        [`${prefix}-button--disabled`]: disabled,
		        // 加载中
		        [`${prefix}-button--loading`]: loading,
		        // 简单
		        [`${prefix}-button--plain`]: plain,
		        // 左右圆角
		        [`${prefix}-button--round`]: round,
		        // 圆形（无文字）
		        [`${prefix}-button--circle`]: circle,
		        // hover 状态
		        [`${prefix}-button--hover`]: hover,
		        // hover 状态
		        [`${prefix}-button--mousedown`]: mousedown,
		        // copy 状态
		        [`${prefix}-button--copy-animation`]: copying,
		      }
		    ]"
		@mousedown="mousedown=true"
		@mouseup="mousedown=false"
		@click="handleClick"
		:disabled="disabled"
		:autofocus="autofocus"
	>
		<fb-icon :class="`${prefix}-button__icon-loading`" name="loading1" rotating v-if="loading"></fb-icon>
		<fb-icon :class="`${prefix}-button__icon`" :name="myIcon" :size="size" v-if="myIcon && !loading"></fb-icon>

		<transition name="fade-in">
			<span v-if="copying">复制成功</span>
			<span v-else-if="$slots.default"><slot></slot></span>
		</transition>
		<fb-icon :class="`${prefix}-button__icon-append`" :name="appendIcon" :size="size" v-if="appendIcon"></fb-icon>
	</button>
</template>

<script>
/*!
 * ${prefix}-button
 * (c) 2020 lincong1987
 */

import FbIcon from '../../icon/src/FbIcon'

import { prefix } from '../../../../src/config'
import ripple from '../../../directives/ripple'

export default {
	name: 'FbButton',

	directives: {ripple},

	components: {FbIcon},

	props: {
		value: {
			type: String,
			default: '',
		},

		copy: {
			type: String,
			default: undefined,
		},

		// button[attr=name]
		name: {
			type: String,
			default: '',
		},
		// 类型 可选 link
		type: {
			type: String,
			default: 'default',
		},
		// 尺寸 s m l
		size: {
			type: String,
			default: 'm',
		},
		// 100% 宽度
		long: {
			type: Boolean,
			default: false,
		},

		hover: {
			type: Boolean,
			default: false,
		},

		// 按钮元素的类型，可选的值为 button、submit、reset、menu
		elType: {
			type: String,
			default: 'button',
		},

		// 显示加载
		loading: {
			type: Boolean,
			default: false,
		},
		// 危险按钮
		danger: {
			type: Boolean,
			default: false,
		},
		// editor 按钮
		editor: {
			type: Boolean,
			default: false,
		},
		// warning 按钮
		warning: {
			type: Boolean,
			default: false,
		},

		// success 按钮
		success: {
			type: Boolean,
			default: false,
		},

		// 禁用
		disabled: {
			type: Boolean,
			default: false,
		},
		// 清爽按钮
		plain: {
			type: Boolean,
			default: false,
		},
		// 自动获取焦点
		autofocus: {
			type: Boolean,
			default: false,
		},
		// 圆角
		round: {
			type: Boolean,
			default: false,
		},
		// 圆形按钮，请加一个图标，不要用文字
		circle: {
			type: Boolean,
			default: false,
		},
		// 图标名称
		icon: {
			type: String,
			default: '',
		},

		appendIcon: {
			type: String,
			default: undefined,
		},
	},
	data () {
		return {
			prefix,
			mousedown: false,
			copying: false,

			myIcon: this.icon,
		}
	},

	watch: {
		icon (val) {
			this.myIcon = val
		},
	},

	methods: {
		handleClick (event) {
			if (this.copy) {
				this.doCopy()
				return
			}
			return this.$emit('on-click', event, this)
		},

		doCopy () {
			let input
			try {
				if (document.execCommand) {
					input = document.createElement('input')
					input.value = this.copy + ''
					document.body.appendChild(input)
					input.select()
					document.execCommand('copy')
					input.parentNode.removeChild(input)
				} else {
					navigator.clipboard.writeText(this.copy + '').then(
						() => {
							/* clipboard successfully set */

						},
						() => {
							/* clipboard write failed */
						})
				}

			} catch (e) {
				if (input) {
					input.parentNode.removeChild(input)
				}
			}

			this.showCopyAnimation()
		},

		showCopyAnimation () {

			let icon = this.myIcon
			this.myIcon = 'selected'
			this.copying = true
			clearTimeout(this.timer)
			this.timer = setTimeout(() => {
				this.copying = false
				this.myIcon = icon
			}, 1000)
		},
	},
}
</script>

<style scoped>

</style>
