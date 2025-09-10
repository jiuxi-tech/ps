<template>
	<fb-container class="editor-add-row" :class="{
		'editor-add-row--show': alwaysShow
	}">
		<div class="editor-add-row__container">
			<fb-space align="center">
				<fb-button type="link" icon="add-circle-fill" size="s"
						   @on-click="formEditor.addRow(viewType, rowIndex, 'row-24')"
				>新增 单行单列
				</fb-button>
				<fb-button type="link" icon="add-circle-fill" size="s"
						   @on-click="formEditor.addRow(viewType, rowIndex, 'row-12')"
				>新增 单行双列
				</fb-button>
			</fb-space>
		</div>
	</fb-container>
</template>

<script>/**
 * FbFormEditorAddRow
 * (c) 2021 lincong1987
 */
import { closest } from '@/util/componentUtils'

export default {
	name: 'FbFormEditorAddRow',

	props: {

		rowIndex: {
			type: Number,
		},

		viewType: {
			type: String,
		},

		alwaysShow: {
			type: Boolean,
			default: false,
		},

	},

	data () {
		return {
		}
	},
	created () {
		this.formEditor = closest(this, 'FbFormEditor')
	},
	beforeDestroy () {
		this.formEditor = null
	},

	methods: {},
}
</script>

<style lang="less" scoped>

</style>
