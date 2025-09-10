<template>
	<div :class="getClass">
		<fb-row :gutter="gutter" :vertical-gutter="gutter">

			<fb-col :span="spans[0]" v-if="$slots.tree" :class="`${prefix}-page-tree-tabs__tree-panel`">
				<fb-card :class="`${prefix}-page-tree-tabs__tree-card`" :header="title" no-border>
					<template slot="actions">
						<slot name="tree-actions"></slot>
					</template>
					<slot name="tree"></slot>
				</fb-card>
			</fb-col>

			<fb-col :span="spans[1]" v-if="$slots.tabs" :class="`${prefix}-page-tree-tabs__form-panel`">
				<fb-card no-border :class="`${prefix}-page-tree-tabs__form-card`">
					<slot name="tabs"></slot>
				</fb-card>
			</fb-col>


		</fb-row>

		<slot></slot>

		<div style="display: none">
			<slot name="footer-left"></slot>
			<slot name="footer-center"></slot>
			<slot name="footer-right"></slot>
		</div>

	</div>
</template>

<script>
	import FbLayout from '../../layout/src/FbLayout'
	import FbLayoutSider from '../../layout/src/FbLayoutSider'
	import FbLayoutBody from '../../layout/src/FbLayoutBody'
	import FbCol from '../../col/src/FbCol'
	import FbRow from '../../row/src/FbRow'
	import FbCard from '../../card/src/FbCard'
	import FbTabs from '../../tab/src/FbTabs'
	import {prefix} from '../../../../src/config'


	/**
	 * FbPageTreeTabs
	 * (c) 2020 lincong1987
	 */
	import {each} from 'lodash-es'

	export default {
		name: 'FbPageTreeTabs',
		components: {
			FbTabs,
			FbCard,
			FbRow,
			FbCol,
			FbLayoutBody,
			FbLayoutSider,
			FbLayout,
		},
		props: {
			title: {
				type: String,
				default: null,
			},
			dialog: {
				type: Object,
				required: false,
			},
			gutter: {
				type: [Number, String],
				default: 8
			},
			spans: {
				type: [Array],
				default: () => {
					return [6, 18]
				}
			}
		},

		data() {
			return {
				prefix,
			}
		},

		mounted() {
			let _this = this

			// 将插槽移入对话框中
			if (_this.dialog) {
				const slots = _this.$slots || {}

				each(slots, function (i, n) {
					if (['footer-left', 'footer-center', 'footer-right'].includes(n)) {
						_this.dialog.$emit('on-dialog-footer', n, i)
					}
				})
			}
		},
		computed: {
			getClass() {
				var arr = [`${prefix}-page`, `${prefix}-page-tree-tabs`]

				return arr
			},
		},
	}
</script>

<style scoped>

</style>
