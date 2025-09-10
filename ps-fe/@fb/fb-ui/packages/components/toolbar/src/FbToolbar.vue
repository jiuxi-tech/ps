<template>
	<fb-table-layout :class="`${prefix}-toolbar`">

		<fb-table-layout-cell v-if="$slots.status" :class="`${prefix}-toolbar__status`">
			<slot name="status"></slot>
		</fb-table-layout-cell>

		<fb-table-layout-cell v-if="$slots.buttons" :class="`${prefix}-toolbar__buttons`">
			<slot name="buttons"></slot>
		</fb-table-layout-cell>

		<fb-table-layout-cell v-if="$slots.actions" align="right" :class="`${prefix}-toolbar__actions`">
			<slot name="actions"></slot>
		</fb-table-layout-cell>

		<slot></slot>

	</fb-table-layout>

</template>

<script>
/**
 * FbToolbar
 * (c) 2020 lincong1987
 */
import { prefix } from '../../../../src/config'
import FbTableLayout from '../../../components/table-layout/src/FbTableLayout'
import FbTableLayoutCell from '../../../components/table-layout/src/FbTableLayoutCell'

export default {
	name: 'FbToolbar',
	components: {FbTableLayoutCell, FbTableLayout},
	props: {



	},
	data () {
		return {
			prefix,
		}
	},

	computed: {
		getClass () {
			let arr = [`${prefix}-toolbar`]

			return arr

		},

		getStyle () {
			let style = {}

			return style
		},
	},
}
</script>

<style scoped>

</style>
