<template>
	<div :class="getClass">

		<div :class="`${cls}__selected-title`">
			<slot name="title"></slot>
		</div>

		<div :class="`${cls}__selected-tags`">
			<slot name="tags"></slot>
		</div>

		<fb-row gutter="16">
			<fb-col span="8">
				<slot name="tree"></slot>
			</fb-col>

			<fb-col span="16">
				<slot name="checkbox"></slot>
			</fb-col>
		</fb-row>

	</div>
</template>

<script>


/**
 * FbDialogTreeSelect
 * (c) 2020 lincong1987
 */
import { each } from 'lodash-es'
import { prefix } from '../../../../src/config'
import FbCol from '../../../components/col/src/FbCol'
import FbCheckboxGroup from '../../../components/checkbox-group/src/FbCheckboxGroup'
import FbCard from '../../../components/card/src/FbCard'
import FbRow from '../../../components/row/src/FbRow'

export default {
	name: 'FbDialogTreeSelect',

	components: {
		FbRow,
		FbCard,
		FbCheckboxGroup,
		FbCol,
	},

	props: {
		dialog: {
			type: Object,
			required: false,
		},
	},

	data () {
		return {
			prefix,
		}
	},

	mounted () {
		let _this = this
	},
	computed: {
		getClass () {
			let arr = [`${this.prefix}-dialog-tree-select`]

			return arr
		},

		cls () {
			return `${this.prefix}-dialog-tree-select`
		},
	},

	methods: {

	},
}
</script>

<style scoped>


</style>
