<template>
	<div :class="getClass">

		<div :class="`${prefix}-simple-table-container`">

			<div :class="`${prefix}-simple-table-scroll`">
				<div :class="`${prefix}-simple-table-header`">


					<table :class="`${prefix}-simple-table-header-table`">

						<thead :class="`${prefix}-simple-table-thead`">
						<tr>
							<!-- row num -->
							<!--							<th class="fb-simple-table-th fb-simple-table-th&#45;&#45;rownum"></th>-->
							<!-- checkbox		-->
							<th :class="`${prefix}-simple-table-th ${prefix}-simple-table-th--checkbox`">
								<fb-checkbox :value="allRowSelect" @on-click="handleAllRowSelect"></fb-checkbox>
							</th>

							<th v-for="(column) in myColumns"
							    :class="`${prefix}-simple-table-th`"
							    :style="getCellStyle(column)">

								<span :class="`${prefix}-simple-table-column`">
									<div :class="`${prefix}-simple-table-column-table`">
										<span :class="`${prefix}-simple-table-column__title`">{{column.label}}</span>
										<span :class="`${prefix}-simple-table-column__sorter`"></span>
									</div>
								</span>

							</th>

						</tr>
						</thead>


					</table>

				</div>


				<div :class="`${prefix}-simple-table-body`" :style="getTableBodyStyle">
					<!--					:style="{width: `${getTotalWidth}px`}"-->
					<table :class="`${prefix}-simple-table-body-table`">

						<tbody :class="`${prefix}-simple-table-tbody`">

						<tr v-for="(row, i) in myData"
						    :class="`${prefix}-simple-table-tbody-row`"
						>
							<!--							<td class="fb-simple-table-tbody-cell fb-simple-table-tbody-cell&#45;&#45;rownum"></td>-->
							<td :class="`${prefix}-simple-table-tbody-cell ${prefix}-simple-table-tbody-cell--checkbox`">
								<fb-checkbox :value="hasRowChecked(row)"
								             @on-click="(checked)=>{handleRowSelect(checked, row)}"></fb-checkbox>
							</td>
							<td :class="`${prefix}-simple-table-tbody-cell`"
							    :style="getCellStyle(column)"
							    v-for="(column) in myColumns"
							>
								<template v-if="!column.slot">
									{{column.formatter(row[column.name], row)}}
								</template>

								<template v-if="column.slot">
									{{debug(column.slot, $slots[column.slot])}}
									<slot
										:name="column.slot"
										:row="row"
										:value="row[column.name]"
										:column="column"></slot>
								</template>


								<!--								{val: row[column.name], row, column}-->

							</td>

							<!--							v-for="(cell, j) in row"-->
						</tr>

						</tbody>

					</table>

				</div>

			</div>

		</div>

		<div :class="`${prefix}-simple-table-pager`">
			<fb-pager
				align="right"
				:current="myPager.current"
				:size="myPager.size"
				:pages="myPager.pages"
				:total="myPager.total"
				@on-change="handlePagerChange"
			></fb-pager>
		</div>

		<div v-if="showLoading" :class="`${prefix}-simple-table-loading`">
			<div :class="`${prefix}-simple-table-loading__content`">
<!--				<fb-icon name="loading1" rotating></fb-icon>-->

				<fb-loading text="加载中..."></fb-loading>
			</div>
		</div>

		<div v-if="showEmpty" :class="`${prefix}-simple-table-empty`">
			<div :class="`${prefix}-simple-table-empty__content`">
				<fb-empty type="data"></fb-empty>
			</div>
		</div>

	</div>
</template>

<script>
	/**
	 * FbTreeTable
	 * (c) 2020 lincong1987
	 */

	import { assign, each, map, endsWith, isNumber, sumBy, find, findIndex, isFunction } from 'lodash-es'
	import axios from 'axios'
	import FbPager from '../../pager/src/FbPager'
	import FbIcon from '../../icon/src/FbIcon'
	import FbCheckbox from '../../checkbox/src/FbCheckbox'
	import FbLoading from '../../loading/src/FbLoading'
	import FbSimpleTable from './FbSimpleTable'
	import { prefix } from '../../../../src/config'

	export default {
		name: 'FbTreeTable',
		extends: FbSimpleTable,
		components: {
			FbLoading,
			FbCheckbox,
			FbIcon,
			FbPager,
		},
		props: {


		},
		data () {

			return {
				prefix,
				myColumns: [],//myColumns,

				myData: [],

				allRowSelect: false,

				selectedRows: [],

				rowsMap: {},

				myPager: {},

				myQueryParam: this.param,

				showLoading: false,

				showEmpty: false,

			}
		},
		computed: {

			getTableBodyStyle () {
				// overflow: scroll;
				// max-height: 300px;
				var style = {}

				// if (this.getTotalWidth > this.scroll.x) {
				//
				// }
				style['overflow-y'] = 'auto'
				style['max-height'] = this.scroll.y + 'px'

				return style
			},

			getTotalWidth () {

				var width = 100

				width = sumBy(this.myColumns, (col) => {
					return col.width || 0
				})

				if (width > this.scroll.x) {
					return width
				}

				if (width <= this.scroll.x) {
					return this.scroll.x
				}

				//
				// width = width < this.scroll.x ? '100%' : this.scroll.x
				// // console.log('width>>>', width)
				// return width

			},

			getClass () {

				var arr = [
					`${prefix}-simple-table`, {
						[`${prefix}-simple-table--table-bordered`]: this.bordered,
						[`${prefix}-simple-table--table-scroll`]: this.scroll,
						[`${prefix}-simple-table--table-striped`]: this.striped,
						[`${prefix}-simple-table--table-hover`]: this.hover,
					},
				]

				return arr
			},

		},

		watch: {
			columns () {
				this.renderColumns()
			},
			data () {
				this.renderData()
			},

			selectedRows () {
				this.allRowSelect =
					(this.selectedRows.length === this.myData.length ? true : (this.selectedRows.length == 0
						? false
						: 'indeterminate'))
			},

			// pager () {
			// 	this.renderPager()
			// },
		},

		methods: {
			getCellStyle (col) {

				var style = []

				style.push({
					'text-align': col.align || 'center',
					width: col.hidden ? 0 : this.getColumnWidth(col),
				})

				if (col.hidden) {
					style.push({display: 'none'})
				}

				return style

			},
			renderPager () {
				this.myPager = assign({}, {
					align: 'right',
					size: 10,
					page: 1,
					current: 1,
					pages: 0,
					total: 0,
				}, this.pager)

			},
			renderColumns () {
				this.myColumns = map(this.columns, (column) => {
					return assign({}, {
						name: '',
						label: '',
						sortable: false, // width: 20,
						align: 'center',
						hidden: false,
						formatter: function (val, row) {
							return val
						},
					}, column)
				})

				// console.log(this.myColumns)
			},

			renderData () {

				this.updateData(map(this.data, (row) => {
					//this.rowsMap[row[this.pk]] = row
					return row
				}))

				this.showEmpty = this.data.length == 0

			},

			updateData (data) {
				this.selectedRows = []
				this.rowsMap = {}
				this.myData = data

			},

			getCell (column, row) {

				return column.name

				// var column = find(row, (col) => {
				// 	return col.name == key
				// })

			},

			getColumn (key) {
				var column = find(this.myColumns, (col) => {
					return col.name == key
				})
				return column
			},

			getColumnWidth (column) {

				var width = column.width

				if (isNumber(width) || endsWith('px', width)) {
					return width + 'px'
				}

				if (endsWith('%', width)) {
					return width
				}

				return width

			},

			handleAllRowSelect (checked) {
				// console.log('handleAllRowSelect', checked)

				this.selectedRows = checked ? this.myData : []

				// if (checked) {
				this.$emit('on-row-select', null, this.selectedRows)
				// }

			},

			handleRowSelect (checked, row) {

				if (checked == true) {
					this.selectedRows.push(row)
				} else {
					let index = findIndex(this.selectedRows, (selectedRow) => {
						return selectedRow[this.pk] == row[this.pk]
					})
					if (index != -1) {
						this.selectedRows.splice(index, 1)
					}
				}

				// if (checked) {
				this.$emit('on-row-select', row, this.selectedRows)
				// }

			},
			hasRowChecked (row) {
				let index = findIndex(this.selectedRows, (selectedRow) => {
					return selectedRow[this.pk] == row[this.pk]
				})

				return index != -1

			},

			handlePagerChange (pager) {
				this.myPager.current = pager.current
				this.myPager.size = pager.size
				this.myPager.pages = pager.pages
				this.doSearch()
			},

			getSelectedRows () {
				return this.selectedRows
			},

			loadData () {

				this.loading(true)
				this.showEmpty = false
				axios.post(this.url, assign({}, this.myQueryParam, this.myPager)).then(data => {

					this.$emit('on-data-load', data)
					let json = this.dataFilter(data)

					this.updateTableAfterQuery(json)
				}).catch(e => {
					this.loading(false)
				})
			},

			fetchData () {

				this.loading(true)
				this.showEmpty = false

				let service = isFunction(this.service) ? this.service : this.service.list

				let param = assign({}, this.myQueryParam, this.myPager)
				// console.log("[FbSimpleTable#fetchData] param = ", JSON.stringify(param, null, '\t'))
				service(param).then(data => {

					this.$emit('on-data-load', data)
					let json = this.dataFilter(data)

					this.updateTableAfterQuery(json)
				}).catch(e => {
					this.$message.error(e)
					this.loading(false)
				})

			},

			updateTableAfterQuery (json) {

				this.loading(false)

				this.updateData(json.data.records || [])

				this.myPager.current = json.data.current
				this.myPager.pages = json.data.pages
				this.myPager.size = json.data.size
				this.myPager.total = json.data.total

				this.showEmpty = this.myPager.pages === 0

			},

			debug (value, slot) {
				// // console.log(value, slot)
			},

			doSearch (param) {
				// console.log('[FbSimpleTable] doSearch success')

				if (param) {
					this.myQueryParam = assign({}, this.myQueryParam, param)
				}

				if (this.service) {
					this.fetchData()
				}
				if (this.url) {
					this.loadData()
				}
			},

			reload () {

				if (this.service) {
					this.fetchData()
				}
				if (this.url) {
					this.loadData()
				}
			},

			loading (status) {
				this.showLoading = status
			},
		},

		mounted () {
			this.renderColumns()

			if (this.service && this.autoLoad) {
				this.renderPager()
				this.fetchData()
			} else if (this.url && this.autoLoad) {
				this.renderPager()
				this.loadData({})
			} else {
				this.renderData()
			}

		},
	}
</script>

<style scoped>

</style>
