<script>

import { classNames } from './utils'
import { closest } from '../../../utils/componentUtils'
import { prefix } from '../../../../src/config'
import FbColGroup from './FbColGroup'
import FbTableHead from './FbTableHead'
import FbTableBody from './FbTableBody'

export default {
	name: 'FbBaseTable',
	components: {
		FbTableBody,
		FbTableHead,
		FbColGroup,
	},
	props: {

		freeze: {
			type: [Boolean, String],
			default: false,
		},

		columns: {
			type: [Array],
			default () {
				return []
			},
		},
		store: {
			type: [Object],
			default: null,
		},
		hasSubRow: {
			type: Boolean,
			default: false,
		},

		isAnyColumnsFreeze: {
			type: Boolean,
			default: false,
		},

		hasHead: {
			type: Boolean,
			default: false,
		},

		hasBody: {
			type: Boolean,
			default: false,
		},

	},

	data () {
		return {
			cls: `${prefix}-simple-table`,
		}
	},

	computed: {},

	watch: {},

	methods: {

		getColumns (cols) {
			const {
				columns = [],
				fixed,
			} = this.$props
			const {table} = this
			const {prefixCls} = table.$props
			return (cols || columns).map(column => ({
				...column,
				className:
					!!column.fixed && !fixed
						? classNames(`${prefixCls}-fixed-columns-in-body`, column.className, column.class)
						: classNames(column.className, column.class),
			}))
		},

	},

	created () {
		//this.table = closest(this, 'FbSimpleTable')
	},

	inject: ['table'],

	mounted () {

	},
	beforeDestroy () {
		//this.table = null
	},
	destroyed () {
		// console.log('destroyed')
	},

	render (h) {

		let {
			hasHead,
			hasBody,
			columns,
			freeze,
			isAnyColumnsFreeze,
			hasSubRow,
			table,
			cls,
		} = this

		let {
			scroll,
			myData,
			currentHoverRowPk,
		} = table

		const tableStyle = {}

		if (!freeze && scroll.x) {
			const tableWidthScrollX = isAnyColumnsFreeze ? 'max-content' : 'auto'
			tableStyle.width = scroll.x === true ? tableWidthScrollX : scroll.x
			tableStyle.width =
				typeof tableStyle.width === 'number' ? `${tableStyle.width}px` : tableStyle.width
		}

		return h('table', {
			key: 'table',
			style: tableStyle,
		}, [

			h('fb-col-group', {
				props: {
					columns,
					freeze,
				},
			}),

			hasHead && h('fb-table-head', {
				'class': [`${cls}-thead`],
				props: {
					columns,
					freeze,
				},
			}),

			hasBody && h('fb-table-body', {
				'class': [`${cls}-tbody`],
				props: {
					columns,
					freeze,
				},
			}),
		])

	},

}


</script>


<style scoped>

</style>
