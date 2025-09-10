<template>
	<fb-flex :class="getClass"  >

 

		<fb-row :gutter="gutter" :vertical-gutter="gutter">

			<fb-col :span="spans[0]" v-if="$slots.tree" :class="`${prefix}-page-tree__tree-panel`">
				<fb-card :header="title" no-border>
					<slot name="tree"></slot>
					<template slot="actions">
						<slot name="tree-actions"></slot>
					</template>
				</fb-card>
			</fb-col>

			<fb-col :span="spans[1]" v-if="$slots.form" :class="`${prefix}-page-tree__form-panel`">
				<fb-card no-border :class="`${prefix}-page-tree__form-card`">
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
	</fb-flex>
</template>

<script>

	/**
	 * FbPageTree
	 * (c) 2020 lincong1987
	 */
	import FbCol from '../../col/src/FbCol'
	import FbRow from '../../row/src/FbRow'
	import FbCard from '../../card/src/FbCard'

	import {each} from 'lodash-es'
	import {prefix} from '../../../../src/config'


	export default {
		name: 'FbPageTree',
		components: {
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
				required: false,
			},
			gutter: {
				type: [Number, String],
				default: 8
			},
			spans: {
				type: [Array],
				default: () => {
					return [6, 18];
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
				var arr = [`${prefix}-page`, `${prefix}-page-tree`]

				return arr
			},
		},
	}
</script>

<style scoped>

</style>
