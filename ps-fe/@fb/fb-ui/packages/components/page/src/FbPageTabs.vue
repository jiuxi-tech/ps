<template>
	<div :class="getClass">
		<div v-if="$slots.form" :class="`${prefix}-page-tabs__form-panel`">
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
	 * FbPageTabs
	 * (c) 2020 lincong1987
	 */
	import {each} from 'lodash-es'
	import { prefix } from '../../../../src/config'


	export default {
		name: 'FbPageTabs',
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
		// 在实例初始化之后，数据观测 (data observer) 和 event/watcher 事件配置之前被调用
		beforeCreate() {

		},
		// 在实例创建完成后被立即调用
		created() {

		},
		// 在挂载开始之前被调用：相关的 render 函数首次被调用。
		beforeMount() {

		},
		// 实例被挂载后调用
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
		// 数据更新时调用，发生在虚拟 DOM 打补丁之前
		beforeUpdate() {

		},
		updated() {

		},
		computed: {
			getClass() {
				var arr = [`${prefix}-page`, `${prefix}-page-tabs`]

				return arr
			},
		},
		methods: {}
	}
</script>

<style scoped>

</style>
