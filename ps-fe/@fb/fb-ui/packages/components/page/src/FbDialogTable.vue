<template>
	<div :class="getClass">

		<div :class="`${cls}__form`">
			<slot name="form"></slot>
		</div>

		<fb-row :gutter="gutter">
			<fb-col span="24" :class="`${cls}__table`">
				<slot name="table"></slot>
			</fb-col>
		</fb-row>

	</div>
</template>

<script>


/**
 * FbDialogTable
 * (c) 2020 lincong1987
 */
import { each } from 'lodash-es'
import { prefix } from '../../../../src/config'
import FbCol from '../../../components/col/src/FbCol'
import FbCheckboxGroup from '../../../components/checkbox-group/src/FbCheckboxGroup'
import FbCard from '../../../components/card/src/FbCard'
import FbRow from '../../../components/row/src/FbRow'

export default {
	name: 'FbDialogTable',

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
		gutter: {
			type: [Number, String],
			default: 0
		}
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
			let arr = [`${this.prefix}-dialog-table-select`]

			return arr
		},

		cls () {
			return `${this.prefix}-dialog-table-select`
		},
	},

	methods: {},
}
</script>

<style scoped>


</style>
