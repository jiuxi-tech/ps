<template>
	<div :class="getClass">
		<div v-if="$slots.form" :class="`${prefix}-page-view__form-panel`">
			<slot name="form"></slot>
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
	 * FbPageView
	 * (c) 2020 lincong1987
	 */
	import {each} from 'lodash-es'
	import { prefix } from '../../../../src/config'

	export default {
		name: 'FbPageView',
		props: {
			dialog: {
				type: Object,
				required: false
			},
			// 宽度
			width: {
				type: [Number, String],
				default: 800,
			},
			// 高度
			height: {
				type: [Number, String],
				default: 600,
			},
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

				const buttons = {};
				each(slots, function (arr, n) {
					if (["footer-left", "footer-center", "footer-right"].includes(n)) {
						buttons[n] = arr;
					}
				});

				if (buttons) {
					_this.dialog.$emit('on-dialog-footer', buttons)
				}

				// 高度设置
				_this.dialog.width = _this.width;
				_this.dialog.height = _this.height;
			}
		},
		computed: {
			getClass() {
				var arr = [`${prefix}-page`, `${prefix}-page-view`]

				return arr
			},
		},
	}
</script>

<style scoped>

</style>
