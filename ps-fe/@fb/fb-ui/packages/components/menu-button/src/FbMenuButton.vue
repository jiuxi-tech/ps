<template>
	<div
		:class="[`${prefix}-menu-button`, {
			//[`${prefix}-menu-button--${triggerType}`]: !!triggerType

	}]"
		:style="{
		       width: width
		}"
		v-clickoutside="close"

	>

		<div ref="header" :class="[`${prefix}-menu-button__header`]"

		     @click.stop="()=>{triggerType === 'click' && toggle()}"
		     @mouseenter="()=>{triggerType === 'mouse' && focus()}"

		>
			<fb-button
				:class="[`${prefix}-menu-button__button`]"
				:icon="icon"
				:append-icon="appendIcon"
				:disabled="disabled"
				:size="size"
				:long="long"
				:round="round"
				:loading="loading"
				:circle="circle"
				:type="type"
				:danger="danger"
				:success="success"
				:warning="warning"
				:editor="editor"
				:hover="showMenu"
			>
				<slot>{{ label }}</slot>
			</fb-button>
		</div>


		<transition :name="transitions.zoomCenterToCorner">
			<fb-container v-show="showMenu"
			              ref="menu"
			              :class="[`${prefix}-menu-button__menus`]"
			              :width="width"
			>
				<fb-menu-button-node v-for="(menu, index) in (myData || [])" :key="index"
				                     :index="index"
				                     :icon="menu.icon"
				                     :label="menu.label"
				                     :value="menu.value"
				                     :color="menu.color"
				                     :disabled="menu.disabled"
				                     :children="menu.children"
				>
					<slot name="menu" :menu="{...menu, index}"></slot>
				</fb-menu-button-node>


			</fb-container>
		</transition>

	</div>
</template>

<script>
/*!
 * FbMenuButton
 * (c) 2021 lincong1987
 */

import FbIcon from '../../icon/src/FbIcon'

import { prefix } from '../../../../src/config'
import FbButton from '../../button/src/FbButton'
import { getViewport } from '../../../utils/utils'
import clickoutside from '../../../directives/clickoutside'
import FbContainer from '../../container/src/FbContainer'
import FbMenuButtonNode from '../../menu-button/src/FbMenuButtonNode'
import {transitions} from "../../../transitions/transitions";

export default {
	name: 'FbMenuButton',

	components: {
		FbMenuButtonNode,
		FbContainer,
		FbButton,
		FbIcon,
	},

	directives: {clickoutside},

	props: {
		label: {
			type: String,
			default: '',
		},
		value: {
			type: String,
			default: '',
		},
		width: {
			type: [String, Number],
			default: undefined,
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
			default: true,
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
			default: 'down',
		},
		data: {
			type: Array,
			default () {
				return []
			},
		},
		hideMenuAfterClick: {
			type: Boolean,
			default: true,
		},
		triggerType: {
			type: String,
			default: 'click',
		},
	},
	data () {
		return {
			prefix,
			transitions,
			showMenu: false,
			myData: this.data,

		}
	},

	watch: {
		data (val) {
			this.myData = val
		},
	},

	methods: {

		close () {
			this.showMenu = false
		},

		blur () {
			this.$emit('on-blur')
			this.showMenu = false
		},
		focus () {
			this.$emit('on-focus')
			this.showMenu = true
		},

		toggle () {
			this.$emit('on-click')
			if (this.disabled || this.readonly) return
			this.showMenu = !this.showMenu
			this.updatePosition()
		},
		updatePosition () {
			this.$nextTick(function () {
				let $menu = this.$refs.menu.$el

				let elStyle = this.$el.getBoundingClientRect()
				const bottom = getViewport().height - elStyle.bottom

				if (!this.listHeight) {
					this.listHeight = $menu.clientHeight
				}
				if (bottom - this.listHeight < 0) {
					$menu.style.top = `-${this.listHeight + 10}px`
				} else {
					$menu.style.top = `${elStyle.height}px`// '32px'
				}
			})
		},

		handleClick (event) {
			return this.$emit('on-click', event, this)
		},

		handleMenuClick (value, node) {

			this.$emit('on-menu-click', value, node)
			this.$emit('on-input', value, node)
			this.$emit('on-change', value, node)
			if (this.hideMenuAfterClick) {
				this.showMenu = false
			}

		},
	},
}
</script>

<style scoped>

</style>
