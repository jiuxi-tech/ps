<template>
	<div :class="getClass">
		<fb-row :gutter="gutter" :vertical-gutter="gutter">

			<fb-col :span="spans[0]" v-if="$slots.tree" :class="`${prefix}-page-tree-table__tree-panel`">
				<fb-card :header="title" no-border>
					<template slot="title">
						<slot name="title"></slot>
					</template>
					<template slot="actions">
						<slot name="tree-actions"></slot>
					</template>
					<slot name="tree"></slot>
				</fb-card>
			</fb-col>

			<fb-col :span="spans[1]" :class="`${prefix}-page-tree-table__form-panel`">

				<fb-card no-border :class="`${prefix}-page-tree-table__form-card`">
					<div :class="`${prefix}-page-tree-table-card`">
						<div v-if="$slots.query" :class="`${prefix}-page-tree-table__query-panel`">
							<slot name="query"></slot>
						</div>

						<div v-if="$slots.actions || $slots.buttons || $slots.table || $slots.pager"
							 :class="`${prefix}-page-tree-table__list-panel`">

							<div v-if="$slots.actions || $slots.buttons"
								 :class="`${prefix}-page-tree-table__list-panel__header`">
								<div v-if="$slots.buttons"
									 :class="`${prefix}-page-tree-table__list-panel__header__buttons`">
									<slot name="buttons"></slot>
								</div>
								<div v-if="$slots.actions"
									 :class="`${prefix}-page-tree-table__list-panel__header__actions`">
									<slot name="actions"></slot>
								</div>
							</div>
							<div v-if="$slots.table" :class="`${prefix}-page-tree-table__list-panel__table`">
								<slot name="table"></slot>
							</div>
							<div v-if="$slots.pager" :class="`${prefix}-page-tree-table__list-panel__pager`">
								<slot name="pager"></slot>
							</div>

						</div>

						<slot></slot>
					</div>


				</fb-card>
			</fb-col>

		</fb-row>

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
	 * FbPageTreeTable
	 * (c) 2020 lincong1987
	 */
	import {each} from 'lodash-es'

	export default {
		name: 'FbPageTreeTable',
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
			dialog: {
				type: Object,
				required: false
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
				var arr = [`${prefix}-page`, `${prefix}-page-tree-table`]

				return arr
			},
		},
	}
</script>

<style scoped>

</style>
