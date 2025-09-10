<template>
	<li role="menuitem"
		tabindex="-1"
		:style="[paddingStyle, itemStyle, { backgroundColor }]"
		:class="[
			`${prefix}-menu-item`,
			{
			  [`${prefix}-menu-item-selected`]: active,
			  [`${prefix}-menu-item-disabled`]: disabled,
			  [`${prefix}-menu-item-no-after`]: this.activeTextColor,
			}
		]"
		@click="handleClick"
		@mouseenter="onMouseEnter"
		@focus="onMouseEnter"
		@blur="onMouseLeave"
		@mouseleave="onMouseLeave"
	>
<!--		 			<slot></slot>-->
<!--	 			<slot name="title"></slot>-->


		<fb-tooltip
			v-if="parentMenu.$options.componentName === 'FbMenu' && rootMenu.collapse && $slots.title ||
					parentMenu.$options.componentName === 'FbMenu' && rootMenu.inlineCollapse && $slots.title"
			effect="dark"
			placement="right">
			<div slot="content">
				<slot name="title"></slot>
			</div>
			<div
				style="position: absolute;left: 0;top: 0;height: 100%;width: 100%;display: inline-block;box-sizing: border-box;padding: 0 32px;">
				<slot></slot>
			</div>
		</fb-tooltip>
		<template v-else>
			<slot></slot>
			<slot name="title"></slot>
		</template>
	</li>
</template>
<script>
	import Menu from './menu-mixin';
	import Emitter from '../../../mixins/emitter';
	import FbTooltip from '../../tooltip/src/tooltip';
	import {prefix} from '../../../../src/config'

	/**
	 * FbMenuItem
	 * (c) 2020 lincong1987
	 */
	export default {
		name: 'FbMenuItem',
		componentName: 'FbMenuItem',
		mixins: [Menu, Emitter],
		components: {FbTooltip},

		props: {
			index: {
				default: null,
				validator: val => typeof val === 'string' || val === null
			},
			route: [String, Object],
			disabled: Boolean
		},

		computed: {
			active() {
				return this.index === this.rootMenu.activeIndex;
			},
			hoverBackground() {
				return this.rootMenu.hoverBackground;
			},
			backgroundColor() {
				return this.rootMenu.backgroundColor || '';
			},
			activeTextColor() {
				return this.rootMenu.activeTextColor || '';
			},
			textColor() {
				return this.rootMenu.textColor || '';
			},
			mode() {
				return this.rootMenu.mode;
			},
			itemStyle() {
				const style = {
					color: this.active ? this.activeTextColor : this.textColor
				};
				if (this.mode === 'horizontal' && !this.isNested) {
					style.borderBottomColor = this.active
						? (this.rootMenu.activeTextColor ? this.activeTextColor : '')
						: 'transparent';
				}
				return style;
			},
			isNested() {
				return this.parentMenu !== this.rootMenu;
			}
		},


		data() {
			return {
				prefix,
			}
		},

		methods: {
			onMouseEnter() {
				if (this.mode === 'horizontal' && !this.rootMenu.backgroundColor) return;
				this.$el.style.backgroundColor = this.hoverBackground;
			},
			onMouseLeave() {
				if (this.mode === 'horizontal' && !this.rootMenu.backgroundColor) return;
				this.$el.style.backgroundColor = this.backgroundColor;
			},
			handleClick() {
				if (!this.disabled) {
					this.dispatch('FbMenu', 'item-click', this);
					this.$emit('on-click', this);
				}
			}
		},

		mounted() {
			this.parentMenu.addItem(this);
			this.rootMenu.addItem(this);
		},

		beforeDestroy() {
			this.parentMenu.removeItem(this);
			this.rootMenu.removeItem(this);
		},
	}
</script>

<style scoped>

</style>
