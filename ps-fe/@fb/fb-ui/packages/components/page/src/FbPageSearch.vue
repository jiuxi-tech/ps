<template>
	<div :class="getClass">
		<div v-if="$slots.query" :class="`${prefix}-page-search__query-panel`">
			<slot name="query"></slot>
		</div>

		<div v-if="$slots.actions || $slots.buttons || $slots.table || $slots.pager"
			 :class="`${prefix}-page-search__list-panel`">
			<div ref="actions" v-if="$slots.actions || $slots.buttons"
				 :class="`${prefix}-page-search__list-panel__header`">
				<div v-if="$slots.buttons" :class="`${prefix}-page-search__list-panel__header__buttons`">
					<slot name="buttons"></slot>
				</div>
				<div v-if="$slots.actions" :class="`${prefix}-page-search__list-panel__header__actions`">
					<slot name="actions"></slot>
				</div>
			</div>
			<div v-if="$slots.table" :class="`${prefix}-page-search__list-panel__table`">
				<slot name="table"></slot>
			</div>
			<div v-if="$slots.pager" :class="`${prefix}-page-search__list-panel__pager`">
				<slot name="pager"></slot>
			</div>

		</div>

		<slot></slot>

		<div style="display: none">
			<slot name="footer-left"></slot>
			<slot name="footer-center"></slot>
			<slot name="footer-right"></slot>
		</div>
	</div>
</template>

<script>
/**
 * FbPageSearch
 * (c) 2020 lincong1987
 */
import {each} from 'lodash-es'
import {prefix} from '../../../../src/config'

export default {
	name: 'FbPageSearch',
	props: {
		dialog: {
			type: Object,
			required: false
		}
	},
	data() {
		return {
			prefix,
		}
	},
	mounted() {
		let _this = this;

		// 将插槽移入对话框中
		if (_this.dialog) {
			const slots = _this.$slots || {};

			each(slots, function (i, n) {
				if (["footer-left", "footer-center", "footer-right"].includes(n)) {
					_this.dialog.$emit('on-dialog-footer', n, i)
				}
			});
		}
	},
	computed: {
		getClass() {
			let arr = [`${prefix}-page`, `${prefix}-page-search`]

			return arr
		}
	},
}
</script>

<style scoped>

</style>
