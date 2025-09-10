<template>
	<div :class="getClass">
		<fb-row :gutter="gutter" :vertical-gutter="gutter">

			<fb-col :span="spans[0]" v-if="$slots.list" :class="`${prefix}-page-tree-edit__tree-panel`">
				<fb-card :header="listTitle" no-border>
					<slot name="list"></slot>
					<template slot="actions">
						<slot name="list-actions"></slot>
					</template>
				</fb-card>
			</fb-col>

			<fb-col :span="spans[1]" v-if="$slots.tree" :class="`${prefix}-page-tree-edit__tree-panel`">
				<fb-card :header="treeTitle" no-border>
					<slot name="tree"></slot>
					<template slot="actions">
						<slot name="tree-actions"></slot>
					</template>
				</fb-card>
			</fb-col>

			<fb-col :span="spans[2]" v-if="$slots.form" :class="`${prefix}-page-tree-edit__form-panel`">
				<fb-card no-border :class="`${prefix}-page-tree-edit__form-card`">
					<template slot="actions">
						<slot name="actions"></slot>
					</template>

					<slot name="form"></slot>
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
	import FbCol from '../../col/src/FbCol'
	import FbRow from '../../row/src/FbRow'
	import FbCard from '../../card/src/FbCard'
	import FbTabs from '../../tab/src/FbTabs'
	import {prefix} from '../../../../src/config'


	/**
	 * FbPageTreeEdit
	 * (c) 2020 lincong1987
	 */
	import {each} from 'lodash-es'

	export default {
		name: 'FbPageTreeEdit',
		components: {
			FbTabs,
			FbCard,
			FbRow,
			FbCol,
		},
		props: {
			title: {
				type: String,
				default: null,
			},

			listTitle: {
				type: String,
				default: null,
			},
			treeTitle: {
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
					return [5, 5, 14]
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
				var arr = [`${prefix}-page`, `${prefix}-page-tree-edit`]

				return arr
			},
		},
	}
</script>

<style scoped>

</style>
