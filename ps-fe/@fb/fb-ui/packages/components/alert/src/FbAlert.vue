<template>
	<transition name="zoom-in-top">


		<div :class="getClass" v-show="myShow">

			<fb-icon
				:class="[`${prefix}-alert__icon`,`${prefix}-alert__icon--${type}`]"
				v-if="myType"
				:name="myIcon"
				:size="hasDescription ? 20 : 16"
				:rotating="type == 'loading'"
			/>

			<span :class="[`${prefix}-alert__message`]">
				<slot name="message">{{ message }}</slot>
			</span>

			<span :class="`${prefix}-alert__description`">
				<slot name="description">{{ description }}</slot>
			</span>

			<div v-if="$slots.actions" :class="`${prefix}-alert__actions`">
				<slot name="actions"></slot>
			</div>

			<fb-icon
				v-if="closable"
				:class="`${prefix}-alert__close`"
				name="close"
				:size="12"
				@on-click="myShow = false"
			/>
		</div>
	</transition>
</template>

<script>

import { prefix } from '../../../../src/config'
import FbIcon from '../../icon/src/FbIcon'

/**
 * FbAlert
 *
 * (c) 2020 lincong1987
 */
export default {
	name: 'FbAlert',
	components: {FbIcon},
	props: {

		// 类型
		type: {
			type: String,
			default: 'primary',
		},

		// 消息
		message: {
			type: [String, Number],
			default: '',
		},

		// 描述
		description: {
			type: [String, Number],
			default: null,
		},

		// 可以关闭
		closable: {
			type: Boolean,
			default: false,
		},

		// 显示
		show: {
			type: Boolean,
			default: true,
		},

		// 边框
		bordered: {
			type: Boolean,
			default: false,
		},

		// 自定义图标
		icon: {
			type: String,
			default: null,
		},

	},

	data () {
		return {
			prefix,
			icons: {
				primary: 'information-fill',
				alert: 'information-fill',
				info: 'information-fill',
				confirm: 'warning-fill',
				success: 'selected-circle-fill',
				error: 'close-circle-fill',
				danger: 'close-circle-fill',
				warning: 'warning-fill',
				loading: 'loading2',
			},

			myShow: this.show,
		}
	},

	computed: {
		getClass () {
			let arr = [`${prefix}-alert`]

			if (this.type) {
				arr.push(`${prefix}-alert--${this.type}`)
			}

			if (this.hasDescription) {
				arr.push(`${prefix}-alert--description`)
			}

			if (this.closable) {
				arr.push(`${prefix}-alert--closable`)
			}

			if (this.bordered) {
				arr.push(`${prefix}-alert--bordered`)
			}

			return arr
		},

		myType () {

			if (this.type) {
				return this.icons[this.type]
			}
			return ''
		},

		myIcon () {
			if (this.icon) {
				return this.icon
			}
			if (this.type) {
				return this.icons[this.type]
			}
			return ''
		},

		hasDescription () {
			if (this.$slots.description || this.description) {
				return true
			}
			return false
		},
	},

	watch: {

		show (value) {
			this.myShow = value
		},

	},
}
</script>

<style scoped>

</style>
