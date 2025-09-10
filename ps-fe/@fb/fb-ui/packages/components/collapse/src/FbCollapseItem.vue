<template>
	<div :class="[`${prefix}-collapse-item`, {'is-active': isActive, 'is-disabled': disabled }]">
		<div
			role="tab"
			:aria-expanded="isActive"
		>
			<div @click="handleHeaderClick"
				 role="button"
				 :tabindex="disabled ? undefined : 0"
				 @keyup.space.enter.stop="handleEnterClick"
				 :class="[
						`${prefix}-collapse-item__header`,
						{
						  'focusing': focusing,
						  'is-active': isActive
						}
					]"
				 @focus="handleFocus"
				 @blur="focusing = false"
			>
				<slot name="title">{{title}}</slot>
				<i
					:class="[`${prefix}-collapse-item__arrow`, `${prefix}-icon-arrow-right`, {'is-active': isActive}, {'arrow-right-slot': isActions}]">
					<slot name="actions" :isActive="isActive">
						<fb-icon v-if="!isActive" :name="collapseIconName"></fb-icon>
						<fb-icon v-else :name="expandIconName"></fb-icon>
					</slot>
				</i>
			</div>
		</div>
		<fb-collapse-transition>
			<div
				:class="`${prefix}-collapse-item__wrap`"
				v-show="isActive"
				role="tabpanel"
				:aria-hidden="!isActive"
			>
				<div :class="`${prefix}-collapse-item__content`">
					<slot></slot>
				</div>
			</div>
		</fb-collapse-transition>
	</div>
</template>
<script>
	import {prefix} from '../../../../src/config'
	import FbCollapseTransition from '../../collapse-transition/src/FbCollapseTransition';
	import FbIcon from '../../icon/src/FbIcon';
	import Emitter from '../../../mixins/emitter';

	export default {
		name: 'FbCollapseItem',

		componentName: 'FbCollapseItem',

		mixins: [Emitter],

		components: {FbCollapseTransition, FbIcon},

		data() {
			return {
				prefix,
				contentWrapStyle: {
					height: 'auto',
					display: 'block'
				},
				contentHeight: 0,
				focusing: false,
				isClick: false,
				collapseIconName: this.collapseIcon || this.collapse.collapseIcon,
				expandIconName: this.expandIcon || this.collapse.expandIcon,
			};
		},

		inject: ['collapse'],

		props: {
			title: String,
			itemKey: {
				type: [String, Number],
				default() {
					return this._uid;
				}
			},
			disabled: Boolean,
			isActions: Boolean, // 是否是 slot=icon 开启简洁障眼法
			collapseIcon: '',
			expandIcon: '',

		},

		computed: {
			isActive() {
				return this.collapse.activeKey.indexOf(this.itemKey) > -1;
			}
		},

		methods: {
			handleFocus() {
				setTimeout(() => {
					if (!this.isClick) {
						this.focusing = true;
					} else {
						this.isClick = false;
					}
				}, 50);
			},
			handleHeaderClick() {
				if (this.disabled) return;
				this.dispatch('FbCollapse', 'item-click', this);
				this.focusing = false;
				this.isClick = true;
			},
			handleEnterClick() {
				this.dispatch('FbCollapse', 'item-click', this);
			}
		}
	};
</script>
